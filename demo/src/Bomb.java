import javax.swing.*;
import java.awt.*;

public class Bomb extends JPanel implements Runnable {
    private int x;
    private int y;
    private double speed;

    private JPanel sky;
    private JPanel ground;

    private boolean hasExploded;
    private boolean[] gameOver;
    private boolean[] paused;

    public Bomb(double x, double y, double speed, JPanel sky, JPanel ground, boolean[] gameOver, boolean[] paused) {
        this.x = (int) x;
        this.y = (int) y;
        this.speed = speed;
        this.gameOver = gameOver;
        this.paused = paused;
        this.sky = sky;
        this.ground = ground;
        this.hasExploded = false;
        this.setSize(10, 10);
        this.setLocation(this.x, this.y);
        this.setVisible(false);
        this.sky.add(this);
    }

    @Override
    public void paintComponent (Graphics g){
        g.setColor(Color.RED);
        g.fillOval(0, 0, 10, 10);
    }

    public void explode() {
        this.hasExploded = true;
        SwingUtilities.invokeLater(() -> this.sky.remove(this));
        ExplosionAnimation explosion = new ExplosionAnimation(this.x, this.y, this.sky);
        new Thread(explosion).start();
    }

    @Override
    public void run() {
        this.setVisible(true);
        while (!gameOver[0] && (!ground.getBounds().intersects(this.getBounds()) || this.hasExploded)) {
            if (this.paused[0]) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            y += (int) this.speed;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.setLocation(x, y);
        }
        //this if happens just for the exploded cannon ball and not for each of them
        if (!this.gameOver[0]) {
            this.gameOver[0] = true;
            this.hasExploded = true;
        }
    }
}