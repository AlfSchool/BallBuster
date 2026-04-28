import javax.swing.*;
import java.awt.*;

public class StartScreen extends JPanel {

    public StartScreen(JFrame frame, Game game) {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(45, 31, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // Title
        JLabel title = new JLabel("Falling Bombs");
        title.setFont(new Font("Monospaced", Font.BOLD, 36));
        title.setForeground(Color.RED);
        gbc.gridy = 0;
        this.add(title, gbc);

        // Difficulty label
        JLabel diffLabel = new JLabel("Difficulty:");
        diffLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        diffLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        this.add(diffLabel, gbc);

        // Difficulty buttons
        JPanel diffPanel = new JPanel();
        diffPanel.setBackground(new Color(45, 31, 255));
        String[] difficulties = {"Easy", "Medium", "Hard"};
        for (String d : difficulties) {
            JButton btn = new JButton(d);
            btn.setFont(new Font("Monospaced", Font.PLAIN, 14));
            btn.setBackground(new Color(80, 80, 80));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            diffPanel.add(btn);
        }
        gbc.gridy = 2;
        this.add(diffPanel, gbc);

        // Start button
        JButton startBtn = new JButton("Start Game");
        startBtn.setFont(new Font("Monospaced", Font.BOLD, 18));
        startBtn.setBackground(Color.GREEN);
        startBtn.setForeground(Color.BLACK);
        startBtn.setFocusPainted(false);
        startBtn.addActionListener(e -> {
            frame.remove(this);
            game.init(frame);
            frame.repaint();
            frame.requestFocusInWindow();
        });
        gbc.gridy = 3;
        this.add(startBtn, gbc);

        // Quit button
        JButton quitBtn = new JButton("Quit");
        quitBtn.setFont(new Font("Monospaced", Font.BOLD, 18));
        quitBtn.setBackground(Color.RED);
        quitBtn.setForeground(Color.WHITE);
        quitBtn.setFocusPainted(false);
        quitBtn.addActionListener(e -> System.exit(0));
        gbc.gridy = 4;
        this.add(quitBtn, gbc);
    }
}