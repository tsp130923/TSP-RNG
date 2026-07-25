package game.tsprng;

import game.tsprng.assets.ImageLoader;
import game.tsprng.ui.GamePanel;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {

        ImageLoader.load();

        JFrame window = new JFrame("TSP RNG");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        GamePanel gamePanel = new GamePanel();

        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();

    }
}
