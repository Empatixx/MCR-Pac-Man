package TileMap.Tiles;

import Main.Utils;
import TileMap.Player;

import javax.naming.InterruptedNamingException;
import java.awt.*;

public class WallTile extends Tile {
    public WallTile(int energy){
        super();
        energyCost = energy;
    }

    @Override
    public void draw(Graphics2D g, int y, int x) {
        g.setColor(Color.black);
        g.fillRect(x*50,y*50,50,50);

        if(energyCost != Integer.MAX_VALUE){ // player can move here
            g.setColor(Color.white);
            Font f = g.getFont();
            Rectangle rect = new Rectangle(x*50,y*50,50,50);
            Utils.drawCenteredString(g,""+energyCost,rect,f);

        }
        g.setColor(Color.GRAY);
        g.drawRect(x*50,y*50,50,50);
    }

    @Override
    public void moveEvent(Player player) {
        player.removeEnergy(energyCost);
    }
    public boolean canMove(){
        return energyCost != Integer.MAX_VALUE;
    }
}
