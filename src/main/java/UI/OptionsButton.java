package UI;

import Main.Utils;

import java.awt.*;

public class OptionsButton extends Button{
    private boolean opened;
    private int hoverIndex;

    private boolean hasChanged;
    public OptionsButton(int x, int y) {
        super(x, y, "map1");
    }
    public void calculateHoverIndex(Rectangle mouse){
        for(int i = 0;i<5;i++){
            Rectangle rect = new Rectangle(x,y+i*50,100,50);
            if(mouse.intersects(rect)){
                hoverIndex = i;
                break;
            }
        }
    }
    @Override
    public void clickEvent() {
        if(!opened) opened = true;
        else {
            String newMap = "map"+(hoverIndex+1);
            if(!newMap.equals(textRender)){
                hasChanged = true;
            }
            textRender = newMap;
            opened = false;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if(opened){
            for(int i = 0;i<5;i++){
                if(hoverIndex == i){
                    g.setColor(Color.yellow);
                } else {
                    g.setColor(Color.white);
                }
                g.fillRect(x,y+i*50,100,50);
                g.setColor(Color.black);
                Font f = g.getFont();
                Utils.drawCenteredString(g,"map"+(i+1), new Rectangle(x,y+i*50,100,50),f);
                g.setColor(Color.black);
                g.drawRect(x,y+i*50,100,50);
            }
        } else {
            super.draw(g);
        }
    }
    @Override
    public boolean tryClick(Rectangle mouse) {
        if(opened){
            for(int i = 0;i<5;i++){
                Rectangle rect = new Rectangle(x,y+i*50,100,50);
                if(mouse.intersects(rect)){
                    clickEvent();
                    return true;
                }
            }
        } else {
            return super.tryClick(mouse);
        }
        return false;
    }
    public String getSelectedMap(){
        return textRender;
    }
    public boolean hasChangedMap(){
        boolean value = hasChanged;
        if(hasChanged) hasChanged = false;
        return value;
    }
}
