package Main;

import TileMap.Player;
import TileMap.TileMap;
import UI.CheckBox;
import UI.OptionsButton;
import UI.RestartButton;
import UI.SolveButton;
import TileMap.PathFinder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Panel extends JPanel implements Runnable{
    public static int WIDTH;
    public static int HEIGHT;

    private Thread thread;

    private TileMap tm;
    private Player player;

    private RestartButton restartButton;
    private SolveButton solveButton;
    private OptionsButton optionsButton;
    private CheckBox safeModeCheckBox;
    private CheckBox pathTypeCheckBox;
    public static boolean safeMode;

    public Panel(){
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        tm = new TileMap("Maps\\map1.json");
        player = new Player(tm);
        WIDTH = tm.getTileSize()*tm.getWidth()+225;
        HEIGHT = tm.getTileSize()*tm.getHeight()+50;

        int minWIDTH = 425;
        int minHEIGHT = 300;
        if(WIDTH < minWIDTH) WIDTH = minWIDTH;
        if(HEIGHT < minHEIGHT) HEIGHT = minHEIGHT;

        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));

        safeModeCheckBox = new CheckBox(WIDTH-75,20);
        pathTypeCheckBox = new CheckBox(WIDTH-75,90);

        PathFinder pathFinder = new PathFinder(tm);
        restartButton = new RestartButton(WIDTH-200,160,pathFinder,player,tm);
        solveButton = new SolveButton(WIDTH-200,90,pathFinder,pathTypeCheckBox);
        optionsButton = new OptionsButton(WIDTH-200,20);

    }

    /**
     * initial function
     */
    public void start(){
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while(thread != null){
            // logics
            update();
            // draw
            repaint();
            try{
                Thread.sleep(1000/60);
            } catch (Exception e){

            }


        }
    }
    public void update() {
        if(optionsButton.hasChangedMap()){
            tm = new TileMap("Maps\\"+optionsButton.getSelectedMap()+".json");
            player = new Player(tm);
            WIDTH = tm.getTileSize()*tm.getWidth()+225;
            HEIGHT = tm.getTileSize()*tm.getHeight()+50;

            int minWIDTH = 425;
            int minHEIGHT = 300;
            if(WIDTH < minWIDTH) WIDTH = minWIDTH;
            if(HEIGHT < minHEIGHT) HEIGHT = minHEIGHT;
            setPreferredSize(new Dimension(WIDTH,HEIGHT));
            Main.window.setPreferredSize(null);
            Main.window.pack();

            safeModeCheckBox = new CheckBox(WIDTH-75,20);
            pathTypeCheckBox = new CheckBox(WIDTH-75,90);

            PathFinder pathFinder = new PathFinder(tm);
            optionsButton.reposition(WIDTH-200,20);

            restartButton = new RestartButton(WIDTH-200,160,pathFinder,player,tm);
            solveButton = new SolveButton(WIDTH-200,90,pathFinder,pathTypeCheckBox);
        }

        Point mouseP = getMousePosition();
        if(mouseP != null){
            Rectangle rect = new Rectangle(mouseP.x,mouseP.y,1,1);

            if(restartButton.intersects(rect)) restartButton.setHover(true);
            else restartButton.setHover(false);

            if(solveButton.intersects(rect)) solveButton.setHover(true);
            else solveButton.setHover(false);

            if(safeModeCheckBox.intersects(rect)) safeModeCheckBox.setHover(true);
            else safeModeCheckBox.setHover(false);

            if(pathTypeCheckBox.intersects(rect)) pathTypeCheckBox.setHover(true);
            else pathTypeCheckBox.setHover(false);

            optionsButton.calculateHoverIndex(rect);
            if(optionsButton.intersects(rect)) optionsButton.setHover(true);
            else optionsButton.setHover(false);
        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // make font bigger
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2F);
        g.setFont(newFont);

        tm.draw(g2);

        restartButton.draw(g2);
        solveButton.draw(g2);
        optionsButton.draw(g2);
        safeModeCheckBox.draw(g2);
        pathTypeCheckBox.draw(g2);

        player.draw(g2);

        if(TileMap.GAME_OVER){
            g.setColor(Color.red);
            Utils.drawCenteredString(g,"You have lost..",
                    new Rectangle(0,HEIGHT-50,WIDTH,50), g.getFont());
        } else if (TileMap.WIN){
            g.setColor(Color.green);
            Utils.drawCenteredString(g,"You have won!",
                    new Rectangle(0,HEIGHT-50,WIDTH,50), g.getFont());
        } else if (pathTypeCheckBox.recentlyChanged()){
            g.setColor(Color.orange);
            if(pathTypeCheckBox.isEnabled()){
                Utils.drawCenteredString(g,"solving: the most energy path",
                        new Rectangle(0,HEIGHT-50,WIDTH,50), g.getFont());
            } else {
                Utils.drawCenteredString(g,"solving: the shortest path",
                        new Rectangle(0,HEIGHT-50,WIDTH,50), g.getFont());
            }
        } else if (safeModeCheckBox.recentlyChanged() && safeModeCheckBox.isEnabled()){
            g.setColor(Color.orange);
            Utils.drawCenteredString(g,"Safe mode activated!",
                    new Rectangle(0,HEIGHT-50,WIDTH,50), g.getFont());
        } else {
            g.setColor(Color.yellow);
            Utils.drawCenteredString(g,"Energy: "+player.getEnergy(),
                    new Rectangle(0,HEIGHT-50,WIDTH,50), g.getFont());
        }

        g2.dispose();
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int x = player.getX();
        int y = player.getY();

        if(TileMap.GAME_OVER || TileMap.WIN){
            return;
        }
        if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S){
            player.setPosition(x,y+1);
        }
        else if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W){
            player.setPosition(x,y-1);
        }
        else if(keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D){
            player.setPosition(x+1,y);
        }
        else if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A){
            player.setPosition(x-1,y);
        } else {
            return;
        }
        player.fixBounds();
        int destX = player.getX();
        int destY = player.getY();

        if(destX != x || destY != y){ // player moved
            safeMode = safeModeCheckBox.isEnabled();
            player.removeEnergy(1);
            boolean sucessfulMove = tm.moveEvent(destY-1,destX-1,player);
            if(!sucessfulMove){ // safe mode or endless wall
                tm.reuseTile(destX-1,destY-1);
                player.setPosition(x,y);
                player.addEnergy(1);
                TileMap.GAME_OVER = false;
                TileMap.WIN = false;
            }
        }
    }

    public void mousePressed() {
        Point mouseP = getMousePosition();
        Rectangle rect = new Rectangle(mouseP.x,mouseP.y,1,1);

        boolean click = optionsButton.tryClick(rect);
        if(click) return;

        restartButton.tryClick(rect);
        solveButton.tryClick(rect);
        safeModeCheckBox.tryClick(rect);
        pathTypeCheckBox.tryClick(rect);
    }
}
