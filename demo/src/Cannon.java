import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

public class Cannon extends JPanel implements Runnable {
    private JPanel sky;
    private Terrorist terrorist;
    private double terroristAttackSpeedIncrease;
    private ArrayList<Bomb> bombs;
    private ArrayList<Powerup> powerups;
    private boolean[] gameOver;
    private boolean[] paused;
    private JLabel score;

    private boolean movingLeft;
    private boolean movingRight;
    private int x;
    private int y;
    private int baseY;

    // Powerup effects
    private double moveSpeedMultiplier = 1.0;
    private double ballSpeedMultiplier = 1.0;   // How fast balls TRAVEL upward
    private boolean sizeBoostActive = false;
    private int baseWidth = 15;
    private int baseHeight;
    private int currentWidth;
    private int currentHeight;
    private Thread sizeBoostTimer;

    public Cannon(int x, int frameHeight, int cannonHeight, int groundHeight, JPanel sky, Terrorist terrorist, boolean[] gameOver, JLabel score, boolean[] paused, double terroristAttackSpeedIncrease) {
        sky.add(this);
        this.baseHeight = groundHeight;
        this.currentWidth = baseWidth;
        this.currentHeight = baseHeight;
        this.x = x + this.getWidth();
        this.baseY = frameHeight - cannonHeight;
        this.y = baseY;
        this.setLocation(x, y);
        this.setSize(currentWidth, currentHeight);
        this.gameOver = gameOver;
        this.paused = paused;
        this.sky = sky;
        this.terrorist = terrorist;
        this.terroristAttackSpeedIncrease = terroristAttackSpeedIncrease;
        this.bombs = terrorist.getBombs();
        this.powerups = new ArrayList<>();
        this.score = score;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        // Add a colored stripe to indicate active powerups
        if (moveSpeedMultiplier > 1.0) {
            g.setColor(new Color(0, 255, 100));
            g.fillRect(0, 0, this.getWidth(), 3);
        }
        if (ballSpeedMultiplier > 1.0) {
            g.setColor(new Color(0, 150, 255));
            g.fillRect(0, 3, this.getWidth(), 3);
        }
        if (sizeBoostActive) {
            g.setColor(new Color(255, 220, 0));
            g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
            g.drawRect(1, 1, this.getWidth() - 3, this.getHeight() - 3);
        }
    }

    private void moveLeft() {
        int step = (int)(3 * moveSpeedMultiplier);
        this.x -= step;
        if (this.x < 0) this.x = 0;
    }

    private void moveRight() {
        int step = (int)(3 * moveSpeedMultiplier);
        this.x += step;
        int maxX = sky.getWidth() - this.getWidth();
        if (this.x > maxX) this.x = maxX;
    }

    @Override
    public void run() {
        while (!this.gameOver[0]) {
            if (!this.paused[0]) {
                if (movingLeft) moveLeft();
                if (movingRight) moveRight();
            }
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.setLocation(this.x, this.y);
        }
    }

    public void shoot() {
        double ballSpeed = 10 * ballSpeedMultiplier;
        Cannon.Ball b = new Cannon.Ball(
            this.x + this.getWidth() / 4, 
            this.y, 
            ballSpeed, 
            this.sky, 
            this.bombs, 
            this.powerups, 
            this.score, 
            this.paused,
            sizeBoostActive  // Pass whether size boost is active
        );
        sky.add(b);
        Thread cannonBall = new Thread(b);
        cannonBall.start();
        this.terrorist.newBombsSpeed += terroristAttackSpeedIncrease;
    }

    public void applyPowerup(Powerup.Type type) {
        switch (type) {
            case SPEED_BOOST -> {
                moveSpeedMultiplier += 0.3;
                showFloatingText("SPEED UP!", new Color(0, 255, 100));
            }
            case BALL_SPEED -> {
                ballSpeedMultiplier += 0.3;
                showFloatingText("BALL SPEED UP!", new Color(0, 150, 255));
            }
            case SIZE_UP -> {
                activateSizeBoost();
                showFloatingText("GIANT MODE!", new Color(255, 220, 0));
            }
        }
        repaint();
    }

    private void activateSizeBoost() {
        if (sizeBoostActive && sizeBoostTimer != null) {
            sizeBoostTimer.interrupt();
        }
        sizeBoostActive = true;
        currentWidth = baseWidth + 20;
        currentHeight = baseHeight + 10;

        SwingUtilities.invokeLater(() -> {
            this.setSize(currentWidth, currentHeight);
            this.y = baseY - 10;
            this.setLocation(this.x, this.y);
            this.revalidate();
            this.repaint();
        });

        sizeBoostTimer = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                return;
            }
            SwingUtilities.invokeLater(() -> {
                sizeBoostActive = false;
                currentWidth = baseWidth;
                currentHeight = baseHeight;
                this.setSize(currentWidth, currentHeight);
                this.y = baseY;
                this.setLocation(this.x, this.y);
                this.revalidate();
                this.repaint();
            });
        });
        sizeBoostTimer.start();
    }

    private void showFloatingText(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospaced", Font.BOLD, 14));
        label.setForeground(color);
        label.setSize(150, 20);
        label.setLocation(this.x - 20, this.y - 30);
        sky.add(label);
        sky.setComponentZOrder(label, 0);

        new Thread(() -> {
            try {
                for (int i = 0; i < 30; i++) {
                    SwingUtilities.invokeLater(() -> label.setLocation(label.getX(), label.getY() - 1));
                    Thread.sleep(50);
                }
                SwingUtilities.invokeLater(() -> sky.remove(label));
            } catch (InterruptedException e) {
                SwingUtilities.invokeLater(() -> sky.remove(label));
            }
        }).start();
    }

    public void addPowerup(Powerup p) {
        this.powerups.add(p);
    }

    public void removePowerup(Powerup p) {
        this.powerups.remove(p);
    }

    public ArrayList<Powerup> getPowerups() {
        return powerups;
    }

    public void setupKeyBindings(JComponent component) {
        InputMap im = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = component.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "moveLeft");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "moveRight");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "moveLeft");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "moveRight");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "shoot");

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "stopLeft");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "stopRight");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "stopLeft");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "stopRight");

        am.put("moveLeft",  new AbstractAction() { public void actionPerformed(ActionEvent e) { if (!paused[0] && !gameOver[0]) movingLeft = true; } });
        am.put("moveRight", new AbstractAction() { public void actionPerformed(ActionEvent e) { if (!paused[0] && !gameOver[0]) movingRight = true; } });
        am.put("shoot",     new AbstractAction() { public void actionPerformed(ActionEvent e) { if (!paused[0] && !gameOver[0]) shoot(); } });
        am.put("stopLeft",  new AbstractAction() { public void actionPerformed(ActionEvent e) { movingLeft = false; } });
        am.put("stopRight", new AbstractAction() { public void actionPerformed(ActionEvent e) { movingRight = false; } });
    }

    class Ball extends JPanel implements Runnable{
        private int x;
        private int y;
        private double speed;

        private JPanel sky;
        private ArrayList<Bomb> bombs;
        private ArrayList<Powerup> powerups;
        private JLabel score;

        private boolean[] paused;
        private boolean giantBall;  // Whether this ball is giant-sized

        public Ball(double x, double y, double speed, JPanel sky, ArrayList<Bomb> bombs, ArrayList<Powerup> powerups, JLabel score, boolean[] paused, boolean giantBall) {
            this.x = (int)x;
            this.y = (int)y;
            this.speed = speed;
            this.sky = sky;
            this.bombs = bombs;
            this.powerups = powerups;
            this.score = score;
            this.paused = paused;
            this.giantBall = giantBall;
            this.setVisible(false);
            this.setLocation(this.x, this.y);
            // If giant, ball is bigger
            int size = giantBall ? 18 : 10;
            this.setSize(size, size);
            this.sky.add(this);
        }

        public boolean collided() {
            // Check bomb collision
            for (int i = 0; i < this.bombs.size(); i++) {
                if (this.bombs.get(i).getBounds().intersects(this.getBounds())) {
                    this.bombs.get(i).explode();
                    this.bombs.remove(i);
                    this.sky.remove(this);
                    int currentPoints = Integer.parseInt(this.score.getText().split(" ")[1]);
                    int scorePointsUpdated =  currentPoints + 1;
                    this.score.setText("Score: " + scorePointsUpdated);
                    return true;
                }
            }
            // Check powerup collision
            for (int i = 0; i < this.powerups.size(); i++) {
                Powerup p = this.powerups.get(i);
                if (!p.isCollected() && p.getBounds().intersects(this.getBounds())) {
                    p.collect();
                    applyPowerup(p.getType());
                    this.powerups.remove(i);
                    this.sky.remove(this);
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void paintComponent(Graphics g) {
            int size = giantBall ? 18 : 10;
            g.setColor(Color.BLACK);
            g.fillOval(0, 0, size, size);
            g.setColor(Color.RED);
            g.drawOval(0, 0, size, size);
            // If giant, add a yellow ring
            if (giantBall) {
                g.setColor(new Color(255, 220, 0));
                g.drawOval(1, 1, size - 2, size - 2);
            }
        }

        @Override
        public void run() {
            this.setVisible(true);
            while (y > 0 && !this.collided()) {
                if (this.paused[0]) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                y -= (int)this.speed;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.setLocation(x, y);
            }
            this.sky.remove(this);
        }
    }
}
