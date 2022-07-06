package TileMap.Tiles;

import TileMap.Player;

import java.awt.*;

public abstract class Tile {
    protected boolean used;
    protected int energyCost;
    public Tile(){
        used = false;
        energyCost = 0;
    }
    public abstract void draw(Graphics2D g, int y, int x);
    public abstract void moveEvent(Player player);
    public void reuse(){
        used = false;
    }

    public int getEnergyCost() {
        return energyCost;
    }
}
