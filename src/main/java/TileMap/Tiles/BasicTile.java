package TileMap.Tiles;

import TileMap.Player;
import TileMap.TileMap;

import java.awt.*;


public class BasicTile extends Tile {
    protected boolean goal;

    @Override
    public void draw(Graphics2D g, int y, int x) {
        g.setColor(Color.white);
        g.fillRect(x*50,y*50,50,50);
        g.setColor(Color.GRAY);
        g.drawRect(x*50,y*50,50,50);
    }

    @Override
    public void moveEvent(Player player) {
        if(goal){
            TileMap.WIN = true;
        }
    }
    public void setGoal(){
        goal = true;
    }

    public boolean isGoal() {
        return goal;
    }
}
