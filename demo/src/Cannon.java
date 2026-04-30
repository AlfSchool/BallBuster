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
    private boolean[] gameOver;
    private boolean[] paused;
    private JLabel score;

    private boolean movingLeft;
    private boolean movingRight;
    private int x;
    private int y;


    public Cannon(int x, int frameHeight, int cannonHeight, int groundHeight, JPanel sky, Terrorist terrorist, boolean[] gameOver, JLabel score, boolean[] paused, double terroristAttackSpeedIncrease) {
        sky.add(this);
        this.x = x + this.getWidth();
        this.y = frameHeight - cannonHeight;
        this.setLocation(x, y);
        this.setSize(30, groundHeight);
        this.gameOver = gameOver;
        this.paused = paused;
        this.sky = sky;
        this.terrorist = terrorist;
        this.terroristAttackSpeedIncrease = terroristAttackSpeedIncrease;
        this.bombs = terrorist.getBombs();
        this.score = score;
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(3, 0, this.getWidth() - 6, this.getHeight());
        g.setColor(Color.GRAY);
        g.drawRect(3, 0, this.getWidth() - 6, this.getHeight());
        g.setColor(Color.DARK_GRAY);
        // Draw the two smaller rectangles at the bottom with borders
    }

    private void moveLeft() {
        int step = 3;
        this.x -= step;
    }

    private void moveRight() {
        int step = 3;
        this.x += step;
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
        int ballRadius = 5;
        Cannon.Ball b = new Cannon.Ball(this.x + this.getWidth() / 2 - ballRadius, this.y, 10, this.sky, this.bombs, this.score, this.paused);
        sky.add(b);
        Thread cannonBall = new Thread(b);
        cannonBall.start();
        this.terrorist.newBombsSpeed += terroristAttackSpeedIncrease;
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
        private JLabel score;

        private boolean[] paused;

        public Ball(double x, double y, double speed, JPanel sky, ArrayList<Bomb> bombs, JLabel score, boolean[] paused) {
            this.x = (int)x;
            this.y = (int)y;
            this.speed = speed;
            this.sky = sky;
            this.bombs = bombs;
            this.score = score;
            this.paused = paused;
            this.setVisible(false);
            this.setLocation(this.x, this.y);
            this.setSize(10, 10);
            this.sky.add(this);
        }

        public boolean collided() {
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
            return false;
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillOval(0, 0, 10, 10);
            g.setColor(Color.RED);
            g.drawOval(0, 0, 10, 10);
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
