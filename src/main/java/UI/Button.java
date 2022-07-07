package UI;

import Main.Utils;

import java.awt.*;

import static java.awt.Color.black;

public abstract class Button {
    protected int x,y;
    private boolean hover;
    protected String textRender;
    public Button(int x, int y, String textRender){
        this.x = x;
        this.y = y;
        this.textRender = textRender;
    }
    public void draw(Graphics2D g){
        if(hover){
            g.setColor(Color.yellow);
        } else {
            g.setColor(Color.white);
        }
        g.fillRect(x,y,100,50);
        g.setColor(black);
        Font f = g.getFont();
        Utils.drawCenteredString(g,textRender, new Rectangle(x,y,100,50),f);
        g.setColor(black);
        g.drawRect(x,y,100,50);
    }
    public boolean tryClick(Rectangle mouse){
        if(intersects(mouse)){
            clickEvent();
            return true;
        }
        return false;
    }
    public abstract void clickEvent();

    public void setHover(boolean hover) {
        this.hover = hover;
    }
    public boolean intersects(Rectangle mouse) {
        return mouse.intersects(new Rectangle(x, y, 100, 50));
    }

    public void reposition(int x, int y){
        this.x = x;
        this.y = y;
    }
}
