import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Game {
    private JFrame frame;
    private JPanel sky;
    private JPanel ground;

    public Game() {
        //size and location of window
        this.frame = new JFrame("Falling bombs");
        this.frame.setContentPane(new JPanel());
        this.frame.setLayout(new BorderLayout());
        int INIT_WIDTH_SCREEN = 486;
        int INIT_HEIGHT_SCREEN = 850;
        this.frame.setSize(INIT_WIDTH_SCREEN, INIT_HEIGHT_SCREEN);
        this.frame.setLocationRelativeTo(null);

        this.sky = new JPanel();
        this.sky.setBackground(new Color(45, 31, 255));
        this.frame.add(this.sky, BorderLayout.CENTER);

        this.ground = new JPanel();
        this.ground.setBackground(Color.GREEN);
        int GROUND_WIDTH = frame.getWidth();
        int GROUND_HEIGHT = 50;
        this.ground.setPreferredSize(new Dimension(GROUND_WIDTH, GROUND_HEIGHT));
        this.frame.add(this.ground, BorderLayout.PAGE_END);
    }

    public void start() {
        frame.setVisible(true);
    }
}
