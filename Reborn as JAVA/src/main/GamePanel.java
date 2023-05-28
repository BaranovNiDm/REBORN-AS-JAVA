package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //НАСТРОЙКИ ЭКРАНА
    final int originalTileSize = 16; //16 на 16 тайлы
    final int scale = 3;

    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    //FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    //дефолтная позиция игрока
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000/FPS; //0.166666 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {

            long currentTime = System.nanoTime();
            //System.out.println("Running");
            // 1 ОБНОВЛЕНИЕ: обновляем информацию такую как позиции персонажей

            update();
            // 2 РИСОВАНИЕ: рисуем экран с обновленной информацией
            repaint();
            try {
                double remainingTime = (nextDrawTime - System.nanoTime())/1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void update() {
        if(keyH.upPressed == true) {
            playerY -= playerSpeed;
        }
        else if (keyH.downPressed == true) {
            playerY += playerSpeed;
        }
        else if(keyH.leftPressed == true) {
            playerX -= playerSpeed;
        }
        else if(keyH.rightPressed == true) {
            playerX += playerSpeed;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.white);

        g2.fillRect(playerX,playerY,tileSize,tileSize);

        g2.dispose();
    }
}
