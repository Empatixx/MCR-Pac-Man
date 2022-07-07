package UI;

import java.awt.*;

import static java.awt.Color.*;

public class CheckBox {
    private int x,y;
    private boolean hover;
    private boolean enabled;
    private long timeActivated;
    public CheckBox(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void draw(Graphics2D g){
        if(hover){
            g.setColor(Color.yellow);
        } else {
            g.setColor(Color.white);
        }
        g.fillRect(x,y,50,50);
        g.setColor(black);
        g.drawRect(x,y,50,50);
        if(enabled){
            g.setColor(orange);
            g.fillRect(x+5,y+5,40,40);
        }
    }
    public void tryClick(Rectangle mouse){
        if(intersects(mouse)){
            clickEvent();
        }
    }
    public void clickEvent(){
        enabled = !enabled;
        timeActivated = System.currentTimeMillis();
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }
    public boolean intersects(Rectangle mouse) {
        return mouse.intersects(new Rectangle(x, y, 50, 50));
    }

    public boolean recentlyChanged(){
        return System.currentTimeMillis() - timeActivated < 2000;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
