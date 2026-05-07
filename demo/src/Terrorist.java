import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Terrorist implements Runnable{
    private JPanel sky;
    private JPanel ground;
    private int skyWidth;
    private boolean[] gameOver;
    private boolean[] paused;

    private ArrayList<Bomb> bombs;
    private ArrayList<Powerup> powerups;
    private Cannon cannon;
    public double newBombsSpeed;
    private Random random;

    public Terrorist(JPanel sky, int skyWidth, JPanel ground, boolean[] gameOver, boolean[] paused) {
        this.sky = sky;
        this.skyWidth = skyWidth;
        this.ground = ground;
        this.gameOver = gameOver;
        this.paused = paused;
        this.bombs = new ArrayList<>();
        this.powerups = new ArrayList<>();
        this.newBombsSpeed = 10;
        this.random = new Random();
    }

    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            while (this.paused[0]);
            launchBomb();
        }
        int waveCount = 0;
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
            waveCount++;
            if (waveCount >= 5 + random.nextInt(4)) {
                launchPowerup();
                waveCount = 0;
            }
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

    private void launchPowerup() {
        Powerup.Type[] types = Powerup.Type.values();
        Powerup.Type type = types[random.nextInt(types.length)];
        double speed = 2 + Math.random() * 3; 
        Powerup p = new Powerup(
                Math.random() * (skyWidth - 50) + 10, 0, speed,
                type, this.sky, this.gameOver, this.paused
        );
        this.powerups.add(p);
        if (cannon != null) {
            cannon.addPowerup(p);
        }
        new Thread(p).start();
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public ArrayList<Powerup> getPowerups() {
        return powerups;
    }
}
