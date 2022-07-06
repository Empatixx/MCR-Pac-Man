package UI;


import TileMap.Player;
import TileMap.TileMap;

public class RestartButton extends Button{
    private Player player;
    private TileMap tm;
    public RestartButton(int x, int y, Player player, TileMap tm) {
        super(x, y, "Restart");
        this.player = player;
        this.tm = tm;
    }

    @Override
    public void clickEvent() {
        tm.resetMap();
        player.reset();
    }
}
