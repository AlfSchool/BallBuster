import javax.swing.*;
import java.awt.*;

public class ExplosionAnimation extends JPanel implements Runnable {
    private int frame;
    private JPanel sky;

    public ExplosionAnimation(int x, int y, JPanel sky) {
        this.frame = 0;
        this.sky = sky;
        this.setOpaque(false);
        this.setSize(60, 60);
        this.setLocation(x - 25, y - 25);
        this.setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (frame) {
            case 0 -> {
                g2.setColor(new Color(255, 200, 0));
                g2.fillRect(25, 20, 8, 8);
                g2.fillRect(20, 25, 8, 8);
                g2.fillRect(30, 25, 8, 8);
                g2.fillRect(25, 30, 8, 8);
            }
            case 1 -> {
                g2.setColor(new Color(255, 150, 0));
                g2.fillRect(20, 15, 10, 10);
                g2.fillRect(10, 20, 10, 10);
                g2.fillRect(35, 20, 10, 10);
                g2.fillRect(20, 35, 10, 10);
                g2.fillRect(28, 22, 10, 10);
                g2.setColor(new Color(255, 200, 0));
                g2.fillRect(22, 22, 14, 14);
            }
            case 2 -> {
                g2.setColor(new Color(255, 80, 0));
                g2.fillRect(15, 10, 12, 12);
                g2.fillRect(5,  20, 12, 12);
                g2.fillRect(38, 18, 12, 12);
                g2.fillRect(15, 38, 12, 12);
                g2.fillRect(32, 35, 12, 12);
                g2.setColor(new Color(255, 150, 0));
                g2.fillRect(18, 18, 22, 22);
                g2.setColor(new Color(255, 220, 100));
                g2.fillRect(22, 22, 14, 14);
            }
            case 3 -> {
                g2.setColor(new Color(180, 50, 0));
                g2.fillRect(10, 8,  10, 10);
                g2.fillRect(2,  18, 10, 10);
                g2.fillRect(42, 15, 10, 10);
                g2.fillRect(10, 40, 10, 10);
                g2.fillRect(38, 38, 10, 10);
                g2.setColor(new Color(100, 100, 100, 180));
                g2.fillRect(16, 16, 26, 26);
            }
            case 4 -> {
                g2.setColor(new Color(80, 80, 80, 120));
                g2.fillRect(12, 12, 10, 10);
                g2.fillRect(36, 14, 8,  8);
                g2.fillRect(10, 36, 8,  8);
                g2.fillRect(36, 36, 10, 10);
            }
        }
    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(() -> {
            sky.add(this);
            sky.setComponentZOrder(this, 0);
            this.setVisible(true);
        });

        for (int i = 0; i <= 4; i++) {
            final int f = i;
            SwingUtilities.invokeLater(() -> {
                this.frame = f;
                this.repaint();
            });
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        SwingUtilities.invokeLater(() -> sky.remove(this));
    }
}