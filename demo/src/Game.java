import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Game {
    private JFrame frame;
    private JPanel sky;
    private JPanel ground;
    private Bomb[] bombs;
    private boolean[] gameOver;
    private JLabel score;

    public Game() {
        this.gameOver = new boolean[]{false};
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

        Terrorist terrorist = new Terrorist(this.sky, frame.getWidth(), this.ground, this.gameOver);
        new Thread(terrorist).start();

        this.score = new JLabel("Score: 0");
        this.score.setLocation(0, 0);
        this.score.setSize(100, 20);
        this.score.setForeground(Color.BLACK);
        this.sky.add(this.score);

        Cannon c = new Cannon(
                this.frame.getWidth() / 2, this.frame.getHeight() - GROUND_HEIGHT - 80,
                this.sky, terrorist.bombs, this.gameOver, this.score);
        this.frame.addKeyListener(c);
        Thread cThread = new Thread(c);
        cThread.start();
    }

    public void start() {
        frame.setVisible(true);
    }
}
