import javax.swing.*;
import java.awt.*;

public class PauseScreen extends JPanel {

    public PauseScreen(int width, int height) {
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        this.setSize(width, height);
        this.setLocation(0, 0);

        // dark transparent overlay
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel pauseLabel = new JLabel("PAUSED");
        pauseLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        pauseLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        this.add(pauseLabel, gbc);

        JLabel hintLabel = new JLabel("Press ESC to resume");
        hintLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        hintLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 1;
        this.add(hintLabel, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(0, 0, 0, 120)); // transparent dark overlay
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}