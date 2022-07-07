package TileMap;

import TileMap.Tiles.*;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class PathFinder {
    private TileMap tileMap;
    private Path shortest;
    private Path mostEnergy;

    public PathFinder(TileMap tm){
        tileMap = tm;
    }
    public void findWay(){
        shortest = null;
        mostEnergy = null;
        PathCell start = new PathCell(null,tileMap.getStartX()-1, tileMap.getStartY()-1,0,tileMap.getStartEnergy());
        findWayFromCell(start);
    }
    public void findWayFromCell(PathCell startPoint){
        Tile[][] tiles = tileMap.getTiles();
        int height = tileMap.getHeight();
        int width = tileMap.getWidth();

        int destX = 0, destY = 0;
        for(int y = 0;y<height;y++){
            for(int x = 0;x<width;x++){
                if(tiles[y][x] instanceof BasicTile && ((BasicTile) tiles[y][x]).isGoal()){
                    destX = x;
                    destY = y;
                    break;
                }
            }
        }
        Queue<PathCell> opened = new LinkedList<>();
        LinkedList<Path> paths = new LinkedList<>();

        addCells(opened, startPoint);
        do{
            PathCell cell = opened.remove();

            cell.energyLeft--;
            if(tiles[cell.y][cell.x] instanceof FoodTile && wasTileAlreadyNotUsed(cell)){
                cell.energyLeft += tiles[cell.y][cell.x].getEnergyCost();
                cell.setNewPathChain();
                findWayFromCell(cell);
                continue;
            }
            if(tiles[cell.y][cell.x] instanceof BoomTile && wasTileAlreadyNotUsed(cell)){
                cell.energyLeft -= tiles[cell.y][cell.x].getEnergyCost();
            }
            if(tiles[cell.y][cell.x] instanceof WallTile){
                cell.energyLeft -= tiles[cell.y][cell.x].getEnergyCost();
            }

            if(cell.x == destX && cell.y == destY && cell.energyLeft > 0) {
                Path path = new Path(cell.energyLeft,cell.cost);
                path.add(cell);
                do{
                    PathCell prev = cell.previousCell;
                    cell = cell.previousCell;
                    if(prev != null){
                        path.add(prev);
                    }
                } while (cell.previousCell != null);

                paths.add(path);
            } else if(cell.energyLeft > 0){
                addCells(opened,cell);
            }

        } while (!opened.isEmpty());
        shortest = getTheShortestPath(paths);
        mostEnergy = getTheMostEnergyPath(paths);
    }
    private Path getTheShortestPath(LinkedList<Path> paths){
        //Path shortest = null;
        for(Path p : paths){
            if(shortest == null || shortest.finalCost > p.finalCost) shortest = p;
        }
        //System.out.println("FCOST: "+shortest.finalCost+" ENERGY:"+shortest.finalEnergy);
        return shortest;
    }
    public Path getTheMostEnergyPath(LinkedList<Path> paths){
        //Path mostEnergy = null;
        for(Path p : paths){
            if(mostEnergy == null || mostEnergy.finalEnergy < p.finalEnergy){
                mostEnergy = p;
                PathCell cell;
                while((cell = p.get()) != null){
                    System.out.println("X: "+cell.x+" Y: "+cell.y+" E:"+cell.energyLeft);
                }
                System.out.println("==========");
            }
        }
        //System.out.println("FCOST: "+mostEnergy.finalCost+" ENERGY:"+mostEnergy.finalEnergy);
        return mostEnergy;
    }
    private void addCells(Queue<PathCell> opened, PathCell prev){
        boolean top, bottom, right, left;
        int x = prev.x;
        int y = prev.y;

        top = y - 1 >= 0;
        bottom = y + 1 < tileMap.getHeight();
        right = x + 1 < tileMap.getWidth();
        left = x - 1 >= 0;

        int prevCost = prev.cost;
        //found = true;
        LinkedList<PathCell> closed = new LinkedList<>();
        PathCell closedCell = prev;
        while(closedCell != null){
            closed.add(closedCell);
            if(closedCell.newPathChain) break;
            closedCell = closedCell.previousCell;
        }
        if(top){
            PathCell cell = new PathCell(prev, x,y-1,prevCost+1,prev.energyLeft);
            if(wasntOpenedYet(cell,closed)){
                opened.add(cell);
            }
        }
        if(bottom){
            PathCell cell = new PathCell(prev, x,y+1,prevCost+1,prev.energyLeft);
            if(wasntOpenedYet(cell,closed)){
                opened.add(cell);
            }
        }
        if(right){
            PathCell cell = new PathCell(prev,x+1,y,prevCost+1,prev.energyLeft);
            if(wasntOpenedYet(cell,closed)){
                opened.add(cell);
            }
        }
        if(left){
            PathCell cell = new PathCell(prev,x-1,y,prevCost+1,prev.energyLeft);
            if(wasntOpenedYet(cell,closed)){
                opened.add(cell);
            }
        }
    }
    private boolean wasntOpenedYet(PathCell newCell, LinkedList<PathCell> closed){
        for(PathCell cell : closed){
            if(cell.x == newCell.x && newCell.y == cell.y) return false;
        }
        return true;
    }
    private static class PathCell{
        private int x;
        private int y;
        private int cost;
        private int energyLeft;
        private PathCell previousCell;

        private boolean newPathChain; // starts a new path finding from this pathcell
        public PathCell(PathCell prev, int x, int y, int cost, int energyLeft){
            this.x = x;
            this.y = y;
            this.energyLeft = energyLeft;
            this.cost = cost;
            previousCell = prev;
        }

        public void setNewPathChain() {
            this.newPathChain = true;
        }
    }
    private static class Path{
        private final LinkedList<PathCell> cells;
        private int index;
        private final int finalEnergy;
        private final int finalCost;
        public Path(int finalEnergy, int finalCost){
            cells = new LinkedList<>();
            this.finalCost = finalCost;
            this.finalEnergy = finalEnergy;
            index = 0;
        }
        public void add(PathCell cell) { cells.add(cell);}
        public PathCell get(){
            if(index >= cells.size()){
                index = 0;
                return null;
            }
            PathCell cell = cells.get(index);
            index++;
            return cell;
        }
    }
    public void drawPath(Graphics2D g, boolean type){
        Color myColour = new Color(255, 0, 255, 100);
        g.setStroke(new BasicStroke(3));
        g.setColor(myColour);
        if(type){ // the most energy
            if(mostEnergy == null) return;
            PathCell cell;
            while((cell = mostEnergy.get()) != null){
                g.fillOval(18+cell.x*50,18+cell.y*50,14,14);
                if(cell.previousCell != null){
                    g.drawLine(25+cell.x*50,25+cell.y*50,25+cell.previousCell.x*50,25+cell.previousCell.y*50);
                }
            }
        } else { // the shortest
            if(shortest == null) return;
            PathCell cell;
            while((cell = shortest.get()) != null){
                g.fillOval(18+cell.x*50,18+cell.y*50,14,14);
                if(cell.previousCell != null){
                    g.drawLine(25+cell.x*50,25+cell.y*50,25+cell.previousCell.x*50,25+cell.previousCell.y*50);
                }
            }
        }
    }
    public void resetPaths(){
        shortest = null;
        mostEnergy = null;
    }
    private boolean wasTileAlreadyNotUsed(PathCell cell){
        PathCell prev = cell;
        while((prev = prev.previousCell) != null){
            if(cell.x == prev.x && cell.y == prev.y) return false;
        }
        return true;
    }
}
