import javax.swing.*;
import java.awt.*;

public class Bomb extends JPanel implements Runnable{
    private int x;
    private int y;
    private double speed;

    private JPanel sky;
    private JPanel ground;

    private boolean hasExploded;
    private boolean[] gameOver;

    public Bomb(double x, double y, double speed, JPanel sky, JPanel ground, boolean[] gameOver) {
        this.setVisible(false);
        this.x = (int)x;
        this.y = (int)y;
        this.speed = speed;
        this.gameOver = gameOver;
        this.sky = sky;
        this.sky.add(this);
        this.setSize(10, 10);
        this.setLocation(this.x, this.y);
        this.ground = ground;
        this.hasExploded = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(0, 0, 10, 10);
    }

    public void explode() {
        this.hasExploded = true;
        this.sky.remove(this);
    }

    @Override
    public void run() {
        this.setVisible(true);
        while (!gameOver[0] && (!ground.getBounds().intersects(this.getBounds()) || this.hasExploded)) {
            y += (int)this.speed;
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
