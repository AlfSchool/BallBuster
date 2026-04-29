import javax.swing.*;
import java.util.ArrayList;

public class Terrorist implements Runnable{
    private JPanel sky;
    private JPanel ground;
    private int skyWidth;
    private boolean[] gameOver;
    private boolean[] paused;

    private ArrayList<Bomb> bombs;
    public double newBombsSpeed;

    public Terrorist(JPanel sky, int skyWidth, JPanel ground, boolean[] gameOver, boolean[] paused) {
        this.sky = sky;
        this.skyWidth = skyWidth;
        this.ground = ground;
        this.gameOver = gameOver;
        this.paused = paused;
        this.bombs = new ArrayList<>();
        this.newBombsSpeed = 10;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            while (this.paused[0]);
            launchBomb();
        }
        while (!gameOver[0]) {
            if (this.paused[0]) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            launchBomb();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void launchBomb() {
        Bomb b = new Bomb(
                Math.random() * (skyWidth - 30) + 10, 0, Math.random() * newBombsSpeed,
                this.sky, this.ground, this.gameOver, this.paused
        );
        this.bombs.add(b);
        new Thread(b).start();
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }
}
