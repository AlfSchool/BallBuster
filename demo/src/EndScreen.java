import javax.swing.*;
import java.awt.*;

public class EndScreen extends JPanel {

    public EndScreen(JFrame frame, JLabel score, Game game) {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(45, 31, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // Game Over label
        JLabel gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setFont(new Font("Monospaced", Font.BOLD, 42));
        gameOverLabel.setForeground(Color.RED);
        gbc.gridy = 0;
        this.add(gameOverLabel, gbc);

        // Score label
        JLabel scoreLabel = new JLabel(score.getText());
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 28));
        scoreLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        this.add(scoreLabel, gbc);

        // Menu button
        JButton menuBtn = new JButton("Main Menu");
        menuBtn.setFont(new Font("Monospaced", Font.BOLD, 18));
        menuBtn.setBackground(Color.GREEN);
        menuBtn.setForeground(Color.BLACK);
        menuBtn.setFocusPainted(false);
        menuBtn.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.setLayout(new BorderLayout());
            StartScreen startScreen = new StartScreen(frame, game);
            frame.add(startScreen, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
            frame.requestFocusInWindow();
        });
        gbc.gridy = 2;
        this.add(menuBtn, gbc);
    }
}