package TileMap.Tiles;

import TileMap.Player;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class BasicTile extends Tile {
    protected boolean goal;

    private Image flag;

    @Override
    public void draw(Graphics2D g, int y, int x) {
        g.setColor(Color.white);
        g.fillRect(x*50,y*50,50,50);
        g.setColor(Color.GRAY);
        g.drawRect(x*50,y*50,50,50);

        if(goal){
            g.drawImage(flag,5+x*50,5+y*50,40,40,null);
        }
    }

    @Override
    public void moveEvent(Player player) {
        if(goal){
            TileMap.WIN = true;
        }
    }
    public void setGoal(){
        goal = true;
        try {
            File f = new File("Maps\\flag.png");
            flag = ImageIO.read(f.toURI().toURL());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isGoal() {
        return goal;
    }
}
