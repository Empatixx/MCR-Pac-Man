package UI;

import TileMap.PathFinder;

public class SolveButton extends Button{
    private PathFinder pathFinder;
    public SolveButton(int x, int y, PathFinder pathFinder) {
        super(x, y,"Solve");
        this.pathFinder = pathFinder;
    }

    @Override
    public void clickEvent() {
        pathFinder.findWay();
    }
}
