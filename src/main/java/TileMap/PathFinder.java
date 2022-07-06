package TileMap;

import TileMap.Tiles.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class PathFinder {
    private TileMap tileMap;
    private Player player;
    public PathFinder(Player p, TileMap tm){
        player = p;
        tileMap = tm;
    }
    public void findWay(){
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

        addCells(opened, tileMap.getStartX()-1, tileMap.getStartY()-1,null,tileMap.getStartEnergy());
        do{
            PathCell cell = opened.remove();

            cell.energyLeft--;
            if(tiles[cell.y][cell.x] instanceof FoodTile){
                cell.energyLeft += tiles[cell.y][cell.x].getEnergyCost();
            }
            if(tiles[cell.y][cell.x] instanceof BoomTile){
                cell.energyLeft -= tiles[cell.y][cell.x].getEnergyCost();
            }
            if(tiles[cell.y][cell.x] instanceof WallTile){
                cell.energyLeft -= tiles[cell.y][cell.x].getEnergyCost();
            }

            if(cell.x == destX && cell.y == destY) {
                Path path = new Path(cell.energyLeft,cell.cost);
                path.add(cell);
                do{
                    PathCell prev = cell.previousCell;
                    cell = cell.previousCell;
                    if(prev != null) path.add(prev);
                } while (cell.previousCell != null);

                paths.add(path);
            } else if(cell.energyLeft > 0){
                addCells(opened,cell.x,cell.y,cell,cell.energyLeft);
            }

        } while (!opened.isEmpty());
        getShortestPath(paths);

    }
    public Path getShortestPath(LinkedList<Path> paths){
        Path shortest = null;
        for(Path p : paths){
            if(shortest == null || shortest.finalCost > p.finalCost) shortest = p;
            System.out.println("COST: "+shortest.finalCost+" ENERGY:"+shortest.finalEnergy);
        }
        System.out.println("FCOST: "+shortest.finalCost+" ENERGY:"+shortest.finalEnergy);
        return shortest;
    }
    private void addCells(Queue<PathCell> opened, int x, int y, PathCell prev,int energy){
        boolean top, bottom, right, left;
        top = y - 1 >= 0;
        bottom = y + 1 < tileMap.getHeight();
        right = x + 1 < tileMap.getWidth();
        left = x - 1 >= 0;

        int prevCost;
        if(prev != null){
            prevCost = prev.cost;
        } else prevCost = 0;
        //found = true;
        LinkedList<PathCell> closed = new LinkedList<>();
        PathCell closedCell = prev;
        while(closedCell != null){
            closed.add(closedCell);
            closedCell = closedCell.previousCell;
        }
        if(top){
            PathCell cell = new PathCell(prev, x,y-1,prevCost+1,energy);
            if(wasntOpenedYet(cell,closed)){
                opened.add(cell);
            }
        }
        if(bottom){
            PathCell cell = new PathCell(prev, x,y+1,prevCost+1,energy);
            if(wasntOpenedYet(cell,closed)){
                opened.add(cell);
            }
        }
        if(right){
            PathCell cell = new PathCell(prev,x+1,y,prevCost+1,energy);
            if(wasntOpenedYet(cell,closed)){
                opened.add(cell);
            }
        }
        if(left){
            PathCell cell = new PathCell(prev,x-1,y,prevCost+1,energy);
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
        public PathCell(PathCell prev, int x, int y, int cost, int energyLeft){
            this.x = x;
            this.y = y;
            this.energyLeft = energyLeft;
            this.cost = cost;
            previousCell = prev;
        }
    }
    private static class Path{
        private final Stack<PathCell> cells;
        private int finalEnergy;
        private int finalCost;
        public Path(int finalEnergy, int finalCost){
            cells = new Stack<>();
            this.finalCost = finalCost;
            this.finalEnergy = finalEnergy;
        }
        public void add(PathCell cell) { cells.add(cell);}
        public PathCell get(){
            return cells.pop();
        }
    }
}
