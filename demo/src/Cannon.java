import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Cannon extends JPanel implements KeyListener, Runnable{
    private int x;
    private int y;
    private JPanel sky;
    private Bomb[] bombs;
    private boolean[] gameOver;
    private JLabel score;

    public Cannon(int x, int y, JPanel sky, Bomb[] bombs, boolean[] gameOver, JLabel score) {
        super();
        this.gameOver = gameOver;
        this.x = x;
        this.y = y;
        this.sky = sky;
        this.setSize(200, 400);
        this.setLocation(x, y);
        this.sky.add(this);
        this.bombs = bombs;
        this.score = score;
    }

    private void moveLeft() {
        int step = 10;
        this.x -= step;
    }

    private void moveRight() {
        int step = 10;
        this.x += step;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 10, 40);
    }

    @Override
    public void run() {
        while (!this.gameOver[0]) {
            this.setLocation(this.x, this.y);
        }
    }

    public void shoot() {
        Cannon.Ball b = new Cannon.Ball(this.x, this.y, 10, this.sky, this.bombs, this.score);
        sky.add(b);
        Thread cannonBall = new Thread(b);
        cannonBall.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (this.gameOver[0]) {
            return;
        }
        if ((int)e.getKeyChar() == 100) {
            moveRight();
        }
        if ((int)e.getKeyChar() == 97) {
            moveLeft();
        }
        if ((int)e.getKeyChar() == 32) {
            shoot();
        }
        System.out.println((int)e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    class Ball extends JPanel implements Runnable{
        private int x;
        private int y;
        private double speed;

        private JPanel sky;
        private Bomb[] bombs;
        private JLabel score;

        private boolean isOutOfScreen;
        private boolean hasExploded;

        public Ball(double x, double y, double speed, JPanel sky, Bomb[] bombs, JLabel score) {
            this.x = (int)x;
            this.y = (int)y;
            this.speed = speed;
            this.sky = sky;
            this.bombs = bombs;
            this.score = score;
            this.sky.add(this);
            this.setSize(10, 10);
            this.isOutOfScreen = false;
            this.hasExploded = false;
        }

        public boolean collided() {
            for (int i = 0; i < this.bombs.length; i++) {
                if (this.bombs[i].getBounds().intersects(this.getBounds())) {
                    this.bombs[i].explode();
                    int currentPoints = Integer.parseInt(this.score.getText().split(" ")[1]);
                    int scorePointsUpdated =  currentPoints + 1;
                    this.score.setText("Score: " + scorePointsUpdated);
                    System.out.println("updated text");
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (this.isOutOfScreen) {
                return;
            }
            if (this.hasExploded) {
                return;
            }
            g.setColor(Color.BLACK);
            g.fillOval(0, 0, 10, 10);
        }

        @Override
        public void run() {
            while (y > 0 && !this.collided()) {
                y -= (int)this.speed;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.setLocation(x, y);
            }
            if (y > 0) {
                this.isOutOfScreen = true;
            } else {
                this.hasExploded = true;
            }
        }
    }
}
