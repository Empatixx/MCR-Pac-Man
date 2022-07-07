package UI;

import TileMap.PathFinder;
import TileMap.Player;
import TileMap.TileMap;

public class RestartButton extends Button{
    private Player player;
    private TileMap tm;
    private PathFinder pathFinder;
    public RestartButton(int x, int y, PathFinder pathFinder, Player player, TileMap tm) {
        super(x, y, "Restart");
        this.player = player;
        this.tm = tm;
        this.pathFinder = pathFinder;
    }

    @Override
    public void clickEvent() {
        tm.resetMap();
        player.reset();
        pathFinder.resetPaths();
    }
}
