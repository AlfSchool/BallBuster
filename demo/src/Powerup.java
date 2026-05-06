import javax.swing.*;
import java.awt.*;

public class Powerup extends JPanel implements Runnable {
    public enum Type {
        SPEED_BOOST,    // Green - permanent cannon movement speed
        BALL_SPEED,     // Blue - permanent cannon ball travel speed
        SIZE_UP         // Yellow - temporary cannon AND ball size increase
    }

    private int x;
    private int y;
    private double speed;
    private Type type;
    private JPanel sky;
    private boolean[] gameOver;
    private boolean[] paused;
    private boolean collected;
    private boolean landed;
    private final int radius = 20;

    public Powerup(double x, double y, double speed, Type type, JPanel sky, boolean[] gameOver, boolean[] paused) {
        this.x = (int) x;
        this.y = (int) y;
        this.speed = speed;
        this.type = type;
        this.gameOver = gameOver;
        this.paused = paused;
        this.sky = sky;
        this.collected = false;
        this.landed = false;
        this.setSize(radius, radius);
        this.setLocation(this.x, this.y);
        this.setVisible(false);
        this.sky.add(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        switch (type) {
            case SPEED_BOOST -> g.setColor(new Color(0, 255, 100));   // Green
            case BALL_SPEED -> g.setColor(new Color(0, 150, 255));     // Blue
            case SIZE_UP -> g.setColor(new Color(255, 220, 0));       // Yellow
        }
        g.fillOval(0, 0, radius, radius);
        g.setColor(Color.WHITE);
        g.drawOval(0, 0, radius - 1, radius - 1);
        // Draw icon letter
        g.setColor(Color.BLACK);
        g.setFont(new Font("Monospaced", Font.BOLD, 12));
        String letter = switch (type) {
            case SPEED_BOOST -> "S";
            case BALL_SPEED -> "B";
            case SIZE_UP -> "G";  // G for "Giant"
        };
        FontMetrics fm = g.getFontMetrics();
        int textX = (radius - fm.stringWidth(letter)) / 2;
        int textY = ((radius - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(letter, textX, textY);
    }

    public void collect() {
        this.collected = true;
        SwingUtilities.invokeLater(() -> this.sky.remove(this));
    }

    public boolean isCollected() { return collected; }
    public boolean hasLanded() { return landed; }
    public Type getType() { return type; }
    public int getRadius() { return radius; }

    @Override
    public void run() {
        this.setVisible(true);
        final int skyHeight = 760;
        while (!gameOver[0] && !collected && y < skyHeight) {
            if (this.paused[0]) {
                try { Thread.sleep(100); } catch (InterruptedException e) { throw new RuntimeException(e); }
                continue;
            }
            y += (int) this.speed;
            try { Thread.sleep(100); } catch (InterruptedException e) { throw new RuntimeException(e); }
            this.setLocation(x, y);
        }
        if (!collected && !gameOver[0]) {
            landed = true;
        }
        SwingUtilities.invokeLater(() -> {
            if (!collected) this.sky.remove(this);
        });
    }
}
