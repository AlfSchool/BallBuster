import javax.swing.*;
import java.awt.*;

public class ExplosionAnimation extends JPanel implements Runnable {
    private int frame;
    private JPanel sky;
    private JLabel scoreIncrease;

    public ExplosionAnimation(int x, int y, JPanel sky) {
        this.frame = 0;
        this.sky = sky;
        this.setSize(60, 60);
        this.setLocation(x - 25, y - 25);
        this.setVisible(false);
        this.scoreIncrease = new JLabel("+1");
        this.scoreIncrease.setForeground(Color.WHITE);
        this.scoreIncrease.setFont(new Font("Monospaced", Font.BOLD, 10));
        this.add(scoreIncrease);
    }

    @Override
    protected void paintComponent(Graphics g) {
        switch (frame) {
            case 0 -> {
                g.setColor(new Color(255, 200, 0));
                g.fillRect(25, 20, 8, 8);
                g.fillRect(20, 25, 8, 8);
                g.fillRect(30, 25, 8, 8);
                g.fillRect(25, 30, 8, 8);
            }
            case 1 -> {
                g.setColor(new Color(255, 150, 0));
                g.fillRect(20, 15, 10, 10);
                g.fillRect(10, 20, 10, 10);
                g.fillRect(35, 20, 10, 10);
                g.fillRect(20, 35, 10, 10);
                g.fillRect(28, 22, 10, 10);
                g.setColor(new Color(255, 200, 0));
                g.fillRect(22, 22, 14, 14);
            }
            case 2 -> {
                g.setColor(new Color(255, 80, 0));
                g.fillRect(15, 10, 12, 12);
                g.fillRect(5,  20, 12, 12);
                g.fillRect(38, 18, 12, 12);
                g.fillRect(15, 38, 12, 12);
                g.fillRect(32, 35, 12, 12);
                g.setColor(new Color(255, 150, 0));
                g.fillRect(18, 18, 22, 22);
                g.setColor(new Color(255, 220, 100));
                g.fillRect(22, 22, 14, 14);
            }
            case 3 -> {
                g.setColor(new Color(180, 50, 0));
                g.fillRect(10, 8,  10, 10);
                g.fillRect(2,  18, 10, 10);
                g.fillRect(42, 15, 10, 10);
                g.fillRect(10, 40, 10, 10);
                g.fillRect(38, 38, 10, 10);
                g.setColor(new Color(100, 100, 100, 180));
                g.fillRect(16, 16, 26, 26);
            }
            case 4 -> {
                g.setColor(new Color(80, 80, 80, 120));
                g.fillRect(12, 12, 10, 10);
                g.fillRect(36, 14, 8,  8);
                g.fillRect(10, 36, 8,  8);
                g.fillRect(36, 36, 10, 10);
            }
        }
    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(() -> {
            sky.add(this);
            sky.setComponentZOrder(this, 0);
            this.setVisible(true);
            this.scoreIncrease.setVisible(true);
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