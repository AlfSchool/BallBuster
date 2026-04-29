import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Game {
    private JFrame frame;
    private JPanel sky;
    private JPanel ground;
    private boolean[] gameOver;
    private Cannon cannon;
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

    public void init(JFrame frame, double terroristAttackSpeed) {
        this.gameOver = new boolean[]{false};
        this.paused = new boolean[]{false};

        BufferedImage skyImage = null;
        try {
            skyImage = ImageIO.read(new File("assets/sky.png"));
        } catch (IOException ignored) {}
        BufferedImage skyFinalImage = skyImage;
        this.sky = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(skyFinalImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        this.sky.setOpaque(false);
        this.sky.setLayout(null);
        frame.add(this.sky, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();

        BufferedImage groundImage = null;
        try {
            groundImage = ImageIO.read(new File("assets/moonTexture.png"));
        } catch (IOException ignored) {}
        final BufferedImage groundFinalImage = groundImage;
        this.ground = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(groundFinalImage, 0, 0, this);
            }
        };
        int GROUND_HEIGHT = 50;
        this.ground.setLayout(null);
        this.ground.setPreferredSize(new Dimension(frame.getWidth(), GROUND_HEIGHT));
        frame.add(this.ground, BorderLayout.PAGE_END);

        Terrorist terrorist = new Terrorist(this.sky, frame.getWidth(), this.ground, this.gameOver, this.paused);
        new Thread(terrorist).start();

        this.score = new JLabel("Score: 0");
        this.score.setFont(new Font("Monospaced", Font.BOLD, 15));
        this.score.setForeground(Color.WHITE);
        this.score.setLocation(0, 0);
        this.score.setSize(100, 20);
        this.sky.add(this.score);

        int GROUND_WIDTH = frame.getWidth();
        this.cannon = new Cannon(
                GROUND_WIDTH / 2, frame.getContentPane().getHeight(), 80, GROUND_HEIGHT,
                this.sky, terrorist, this.gameOver, this.score, this.paused, terroristAttackSpeed);
        this.cannon.setupKeyBindings(this.frame.getRootPane());
        new Thread(this.cannon).start();

        // ESC key listener for pause
        frame.addKeyListener(new java.awt.event.KeyAdapter() {
            PauseScreen pauseScreen = new PauseScreen(frame.getWidth(), frame.getHeight());

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                    paused[0] = !paused[0];
                    if (paused[0]) {
                        sky.add(pauseScreen);
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
            while (!this.gameOver[0]) {
                try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            }
            showEndScreen();
        }).start();
    }

    public void start() {
        StartScreen startScreen = new StartScreen(this.frame, this);
        this.frame.add(startScreen);
        this.frame.setVisible(true);
    }

    public void showEndScreen() {
        SwingUtilities.invokeLater(() -> {
            score.setVisible(false);
            frame.getContentPane().removeAll();
            EndScreen endScreen = new EndScreen(frame, score, this);
            frame.add(endScreen);
        });
    }
}