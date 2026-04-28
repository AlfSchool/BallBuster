import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Game {
    private JFrame frame;
    private JPanel sky;
    private JPanel ground;
    private boolean[] gameOver;
    private JLabel score;
    private boolean[] paused;

    public Game() {
        this.frame = new JFrame("Falling bombs");
        this.frame.setContentPane(new JPanel());
        this.frame.setLayout(new BorderLayout());
        this.frame.setResizable(false);
        int INIT_WIDTH_SCREEN = 486;
        int INIT_HEIGHT_SCREEN = 850;
        this.frame.setSize(INIT_WIDTH_SCREEN, INIT_HEIGHT_SCREEN);
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init(JFrame frame) {
        this.gameOver = new boolean[]{false};
        this.paused = new boolean[]{false};

        this.sky = new JPanel();
        this.sky.setBackground(new Color(45, 31, 255));
        this.sky.setLayout(null);
        frame.add(this.sky, BorderLayout.CENTER);

        this.ground = new JPanel();
        this.ground.setBackground(Color.GREEN);
        int GROUND_HEIGHT = 50;
        this.ground.setLayout(null);
        this.ground.setPreferredSize(new Dimension(frame.getWidth(), GROUND_HEIGHT));
        frame.add(this.ground, BorderLayout.PAGE_END);

        Terrorist terrorist = new Terrorist(this.sky, frame.getWidth(), this.ground, this.gameOver, this.paused);
        new Thread(terrorist).start();

        this.score = new JLabel("Score: 0");
        this.score.setLocation(0, 0);
        this.score.setSize(100, 20);
        this.score.setForeground(Color.BLACK);
        this.sky.add(this.score);

        int GROUND_WIDTH = frame.getWidth();
        Cannon c = new Cannon(
                GROUND_WIDTH / 2, frame.getHeight() - 50 - 80,
                this.sky, terrorist, this.gameOver, this.score, this.paused);
        frame.addKeyListener(c);
        new Thread(c).start();

        // ESC key listener for pause
        frame.addKeyListener(new java.awt.event.KeyAdapter() {
            PauseScreen pauseScreen = new PauseScreen(frame.getWidth(), frame.getHeight());

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                    paused[0] = !paused[0];
                    if (paused[0]) {
                        sky.add(pauseScreen);
                        sky.setComponentZOrder(pauseScreen, 0);
                    } else {
                        sky.remove(pauseScreen);
                    }
                    sky.revalidate();
                    sky.repaint();
                    frame.requestFocusInWindow();
                }
            }
        });

        //game over watcher
        new Thread(() -> {
            while (!gameOver[0]) {
                try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            }
            showEndScreen();
        }).start();
    }

    public void start() {
        StartScreen startScreen = new StartScreen(this.frame, this);
        this.frame.add(startScreen, BorderLayout.CENTER);
        this.frame.setVisible(true);
    }

    public void showEndScreen() {
        SwingUtilities.invokeLater(() -> {
            score.setVisible(false);
            frame.getContentPane().removeAll();
            frame.setLayout(new BorderLayout());
            EndScreen endScreen = new EndScreen(frame, score, this);
            frame.add(endScreen, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
        });
    }
}