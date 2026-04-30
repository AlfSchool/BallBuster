import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bomb extends JPanel implements Runnable {
    private int x;
    private int y;
    private double speed;

    private JPanel sky;

    private boolean hasExploded;
    private boolean[] gameOver;
    private boolean[] paused;
    final int radius = 15;
    private BufferedImage bombImage;

    public Bomb(double x, double y, double speed, JPanel sky, boolean[] gameOver, boolean[] paused) {
        this.x = (int) x;
        this.y = (int) y;
        this.speed = speed;
        this.gameOver = gameOver;
        this.paused = paused;
        this.sky = sky;
        this.hasExploded = false;
        this.setSize(radius, radius);
        this.setLocation(this.x, this.y);
        this.setVisible(false);
        this.sky.add(this);
        this.bombImage = null;
        try {
            this.bombImage = ImageIO.read(new File("assets/bomb.png"));
        } catch (IOException ignored) {}
        this.setOpaque(false);
    }

    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillOval(1, 2, 12, 12);
        g.drawImage(bombImage, 0, 0, radius, radius, null);
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
        final int skyHeight = 760;
        while (!gameOver[0] && ((this.y < skyHeight) || this.hasExploded)) {
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
        //if exploded cannon ball (not for everyone)
        if (!this.gameOver[0]) {
            this.gameOver[0] = true;
            this.hasExploded = true;
        }
    }
}