import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Game {
    private JFrame frame;
    private JPanel sky;
    private JPanel ground;
    private Bomb[] bombs;

    public Game() {
        //size and location of window
        this.frame = new JFrame("Falling bombs");
        this.frame.setContentPane(new JPanel());
        this.frame.setLayout(new BorderLayout());
        int INIT_WIDTH_SCREEN = 486;
        int INIT_HEIGHT_SCREEN = 850;
        this.frame.setSize(INIT_WIDTH_SCREEN, INIT_HEIGHT_SCREEN);
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.sky = new JPanel();
        this.sky.setBackground(new Color(45, 31, 255));
        this.sky.setLayout(null);
        this.frame.add(this.sky, BorderLayout.CENTER);

        this.ground = new JPanel();
        this.ground.setBackground(Color.GREEN);
        int GROUND_WIDTH = frame.getWidth();
        int GROUND_HEIGHT = 50;
        this.ground.setLayout(null);
        this.ground.setPreferredSize(new Dimension(GROUND_WIDTH, GROUND_HEIGHT));
        this.frame.add(this.ground, BorderLayout.PAGE_END);

        this.bombs = new Bomb[10];
        for (int i = 0; i < bombs.length; i++) {
            this.bombs[i] = new Bomb(Math.random() * frame.getWidth(), 0, Math.random() * 10, this.sky, this.ground);
        }
        for (Bomb b : bombs) {
            new Thread(b).start();
        }

        Cannon c = new Cannon(this.frame.getWidth() / 2, this.frame.getHeight() - GROUND_HEIGHT - 80, this.sky, this.bombs);
        this.frame.addKeyListener(c);
        Thread cThread = new Thread(c);
        cThread.start();
    }

    public void start() {
        frame.setVisible(true);
    }
}
