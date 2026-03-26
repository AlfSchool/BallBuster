import javax.swing.*;
import java.awt.*;

public class Bomb extends JPanel implements Runnable{
    private int x;
    private int y;
    private double speed;

    private JPanel sky;
    private JPanel ground;

    public Bomb(double x, double y, double speed, JPanel sky, JPanel ground) {
        this.x = (int)x;
        this.y = (int)y;
        this.speed = speed;
        this.sky = sky;
        this.sky.add(this);
        this.setSize(10, 10);
        this.ground = ground;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(0, 0, 10, 10);
    }

    @Override
    public void run() {
        while (!ground.getBounds().intersects(this.getBounds())) {
            y += (int)this.speed;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.setLocation(x, y);
        }
    }
}
