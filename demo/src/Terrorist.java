import javax.swing.*;
import java.util.ArrayList;

public class Terrorist implements Runnable{
    private JPanel sky;
    private JPanel ground;
    private int skyWidth;
    private boolean[] gameOver;

    public ArrayList<Bomb> bombs;

    public Terrorist(JPanel sky, int skyWidth, JPanel ground, boolean[] gameOver) {
        this.sky = sky;
        this.skyWidth = skyWidth;
        this.ground = ground;
        this.gameOver = gameOver;
        this.bombs = new ArrayList<>();
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            launchBomb();
        }
        while (!gameOver[0]) {
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
                Math.random() * (skyWidth-3) + 10, 0, Math.random() * 10,
                this.sky, this.ground, this.gameOver
        );
        this.bombs.add(b);
        new Thread(b).start();
    }
}
