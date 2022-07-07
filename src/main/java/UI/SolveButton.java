package UI;

import TileMap.PathFinder;

import java.awt.*;

public class SolveButton extends Button{
    private PathFinder pathFinder;
    private CheckBox typeBox;
    public SolveButton(int x, int y, PathFinder pathFinder, CheckBox typeBox) {
        super(x, y,"Solve");
        this.pathFinder = pathFinder;
        this.typeBox = typeBox;
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        pathFinder.drawPath(g,typeBox.isEnabled());
    }

    @Override
    public void clickEvent() {
        pathFinder.findWay();
    }

}
