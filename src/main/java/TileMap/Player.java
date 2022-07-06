package TileMap;

import java.awt.*;

public class Player {
    private int x;
    private int y;

    private int energy;
    private TileMap tm;

    public Player(TileMap tm){
        this.x = tm.getStartX();
        this.y =  tm.getStartY();
        this.energy = tm.getStartEnergy();
        this.tm = tm;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void draw(Graphics2D g){
        g.setColor(Color.blue);
        g.drawOval(10+(x-1)*50,10+(y-1)*50,30,30);
        g.setColor(Color.cyan);
        g.fillOval(11+(x-1)*50,11+(y-1)*50,28,28);
    }

    public boolean isDead() {
        return energy <= 0;
    }
    public void addEnergy(int energy) {
        this.energy += energy;
    }
    public void removeEnergy(int energy) {
        this.energy -= energy;
    }

    public void fixBounds() {
        if(x-1 >= tm.getWidth()) x = tm.getWidth();
        if(x-1 < 0) x = 1;
        if(y-1 >= tm.getHeight()) y = tm.getHeight();
        if(y-1 < 0) y = 1;
    }

    public int getEnergy() {
        return energy;
    }

    public void reset() {
        this.x = tm.getStartX();
        this.y =  tm.getStartY();
        this.energy = tm.getStartEnergy();
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
