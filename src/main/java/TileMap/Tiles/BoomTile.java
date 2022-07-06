package TileMap.Tiles;

import Main.Utils;
import TileMap.Player;

import java.awt.*;

public class BoomTile extends Tile {
    public BoomTile(int energy){
        super();
        energyCost = energy;
    }

    @Override
    public void draw(Graphics2D g, int y, int x) {
        if(!used){
            g.setColor(Color.red);
            g.fillRect(x*50,y*50,50,50);
            if(energyCost != Integer.MAX_VALUE){
                g.setColor(Color.black);
                Font f = g.getFont();
                Rectangle rect = new Rectangle(x*50,y*50,50,50);
                Utils.drawCenteredString(g,""+energyCost,rect,f);

            }
        } else {
            g.setColor(Color.white);
            g.fillRect(x*50,y*50,50,50);
        }
        g.setColor(Color.GRAY);
        g.drawRect(x*50,y*50,50,50);
    }

    @Override
    public void moveEvent(Player player) {
        if(!used){
            used = true;
            player.removeEnergy(energyCost);
        }
    }

}
