package Main;

import TileMap.Player;
import TileMap.TileMap;
import UI.CheckBox;
import UI.RestartButton;
import UI.SolveButton;
import TileMap.PathFinder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Panel extends JPanel implements Runnable{
    public static int WIDTH = 500;
    public static int HEIGHT = 500;

    private Thread thread;

    private TileMap tm;
    private Player player;

    private RestartButton restartButton;
    private SolveButton solveButton;
    private CheckBox checkBox;

    public static boolean safeMode;

    public Panel(){
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        tm = new TileMap("map.json");
        player = new Player(tm);
        WIDTH = tm.getTileSize()*tm.getWidth()+225;
        HEIGHT = tm.getTileSize()*tm.getHeight()+50;
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));

        restartButton = new RestartButton(WIDTH-200,20,player,tm);
        solveButton = new SolveButton(WIDTH-200,90,new PathFinder(player,tm));
        checkBox = new CheckBox(WIDTH-75,20);
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
        Point mouseP = getMousePosition();
        if(mouseP != null){
            Rectangle rect = new Rectangle(mouseP.x,mouseP.y,1,1);

            if(restartButton.intersects(rect)) restartButton.setHover(true);
            else restartButton.setHover(false);

            if(solveButton.intersects(rect)) solveButton.setHover(true);
            else solveButton.setHover(false);

            if(checkBox.intersects(rect)) checkBox.setHover(true);
            else checkBox.setHover(false);
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
        player.draw(g2);

        if(TileMap.GAME_OVER){
            g.setColor(Color.red);
            Utils.drawCenteredString(g,"You have lost..",
                    new Rectangle(0,HEIGHT-50,WIDTH,50), g.getFont());
        } else if (TileMap.WIN){
            g.setColor(Color.green);
            Utils.drawCenteredString(g,"You have won!",
                    new Rectangle(0,HEIGHT-50,WIDTH,50), g.getFont());
        } else if (checkBox.recentlyActivated()){
            g.setColor(Color.orange);
            Utils.drawCenteredString(g,"Safe mode activated!",
                    new Rectangle(0,HEIGHT-50,WIDTH,50), g.getFont());
        } else {
            g.setColor(Color.yellow);
            Utils.drawCenteredString(g,"Energy: "+player.getEnergy(),
                    new Rectangle(0,HEIGHT-50,WIDTH,50), g.getFont());
        }

        restartButton.draw(g2);
        solveButton.draw(g2);
        checkBox.draw(g2);

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
            safeMode = checkBox.isEnabled();
            player.removeEnergy(1);
            boolean sucessfulMove = tm.moveEvent(destY-1,destX-1,player);
            if(!sucessfulMove){ // safe mode or endless wall
                player.setPosition(x,y);
                player.addEnergy(1);
            }
        }
    }

    public void mousePressed() {
        Point mouseP = getMousePosition();
        Rectangle rect = new Rectangle(mouseP.x,mouseP.y,1,1);

        restartButton.tryClick(rect);
        solveButton.tryClick(rect);
        checkBox.tryClick(rect);
    }
}
