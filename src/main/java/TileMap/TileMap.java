package TileMap;

import Main.Panel;
import TileMap.Tiles.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;

public class TileMap {
    private int height;
    private int width;

    public static boolean GAME_OVER = false;
    public static boolean WIN = false;

    private Tile[][] tiles;

    private int startX;
    private int startY;
    private int startEnergy;

    private int tileSize;

    public TileMap(String filepath){
        tileSize = 50;
        StringBuilder builder = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(filepath);
            int ch;
            while((ch = fileReader.read()) != -1){
                builder.append((char)ch);
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject(builder.toString());

        width = obj.getInt("SIRKA");
        height = obj.getInt("VYSKA");

        tiles = new Tile[height][width];

        int destX, destY;

        startX = obj.getJSONObject("START").getInt("X");
        startY = obj.getJSONObject("START").getInt("Y");
        startEnergy = obj.getJSONObject("START").getInt("E");

        destX = obj.getJSONObject("CIL").getInt("X");
        destY = obj.getJSONObject("CIL").getInt("Y");

        JSONArray array = obj.getJSONArray("PLAN");
        for(int i = 0;i<array.length();i++){
            String row = array.getString(i);
            String[] splitRow = row.split(",");
            for(int j = 0;j<splitRow.length;j++){
                if(splitRow[j].equals("V")){
                    tiles[i][j] = new BasicTile();
                    if(i == destY-1 && j == destX-1) ((BasicTile) tiles[destY-1][destX-1]).setGoal(); // positions start at 1, indexes at 0
                } else if (splitRow[j].charAt(0) == 'J'){
                    int energy = Integer.parseInt(splitRow[j].substring(1));
                    tiles[i][j] = new FoodTile(energy);
                } else if (splitRow[j].charAt(0) == 'Z'){
                    int energy;
                    if(splitRow[j].length() != 1) energy = Integer.parseInt(splitRow[j].substring(1));
                    else energy = Integer.MAX_VALUE;
                    tiles[i][j] = new WallTile(energy);
                } else if (splitRow[j].charAt(0) == 'B'){
                    int energy;
                    if(splitRow[j].length() != 1) energy = Integer.parseInt(splitRow[j].substring(1));
                    else energy = Integer.MAX_VALUE;
                    tiles[i][j] = new BoomTile(energy);
                }
            }
        }

    }

    public int getTileSize() {
        return tileSize;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.BLACK);
        g.drawRect(0,0,500,500);
        for(int y = 0;y<height;y++){
            for(int x = 0;x<width;x++){
                tiles[y][x].draw(g, y , x);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public boolean moveEvent(int y, int x, Player p){
        if(!GAME_OVER && !WIN){
            if(tiles[y][x] instanceof WallTile){
                if(!((WallTile)tiles[y][x]).canMove()){
                    return false;
                }
            }
            int preEnergy = p.getEnergy();
            tiles[y][x].moveEvent(p);
            if(p.isDead() && preEnergy >= 0 && Panel.safeMode){
                p.setEnergy(preEnergy);
                return false;
            }
            else if(p.isDead()){
                GAME_OVER = true;
            }
            return true;
        }
        return false;
    }

    public void resetMap() {
        for(int y = 0;y<height;y++){
            for(int x = 0;x<width;x++){
                tiles[y][x].reuse();
            }
        }
        GAME_OVER = false;
        WIN = false;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void reuseTile(int destX, int destY) {
        tiles[destY][destX].reuse();
    }
}
