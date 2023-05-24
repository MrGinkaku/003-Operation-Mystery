package main;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable
{
    // SCREEN SETTINGS
    final int originalTileSize = 16; //16x16 tile
    final int scale = 3;

    // SCREEN SETTTTINGS
    public final int tileSize = originalTileSize * scale; //48x48 tile
    public final int maxScreenHorizontal = 16;
    public final int maxScreenVertical = 12;
    public final int screenWidth = tileSize * maxScreenHorizontal; // 786px
    public final int screenHeight = tileSize * maxScreenVertical; // 576px

    // WORLD SETTINGS
    public final int maxWorldHorizontal = 50;
    public final int maxWorldVertical = 50;
    public final int worldWidth = tileSize * maxWorldHorizontal;
    public final int worldHeight = tileSize * maxWorldVertical;

    // FPS
    int FPS = 60;

    // TileManager 
    TileManager tileM = new TileManager(this);

    // KeyHandler
    KeyHandler keyH = new KeyHandler();

    // Collision Checker
    public CollisionChecker ColChecker = new CollisionChecker(this);

    // Game Thread
    Thread gameThread;

    // Player
    public Player player = new Player(this,keyH);

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override // Delta Accumulator Method
    public void run()
    {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        // FPS SETTINGS
        while (gameThread != null)
        {
            currentTime = System.nanoTime();
            delta = delta + (currentTime - lastTime)/drawInterval;
            timer = timer + (currentTime - lastTime);

            lastTime = currentTime;

            if (delta >= 1)
            {
                
                update();

                repaint();

                delta = delta - 1;
                drawCount = drawCount + 1;
            }

            if(timer >= 100000000)
            {
                System.out.println("FPS: " +drawCount*10);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // @Override
    // public void run()
    // {

    //     double drawInterval = 1000000000/FPS; // 0.01666 seconds
    //     double nextDrawTime = System.nanoTime() + drawInterval;

    //     while (gameThread != null)
    //     {

    //         update();
            
    //         repaint();

            
    //         try 
    //         {
    //             double remainingTime = nextDrawTime - System.nanoTime();
    //             remainingTime = remainingTime/1000000;

    //             if(remainingTime < 0)
    //             {
    //                 remainingTime = 0;
    //             }

    //             Thread.sleep((long) remainingTime);

    //             nextDrawTime = nextDrawTime + drawInterval;
    //         } 
    //         catch (InterruptedException e) 
    //         {
    //             e.printStackTrace();
    //         }
    //     }
    //     //Diisi dengan GameLoop
    // }

    public void update()
    {
       player.update();
    }

    public void paintComponent(Graphics graph)
    {
        super.paintComponent(graph);

        Graphics2D g2D = (Graphics2D)graph;
        
        tileM.draw(g2D);
        
        player.draw(g2D);

        g2D.dispose();
    }
}