package game.tsprng.ui;

import game.tsprng.assets.ImageLoader;
import game.tsprng.player.Player;
import game.tsprng.roll.RollSystem;
import game.tsprng.aura.Aura;
import game.tsprng.save.SaveSystem;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel
    implements Runnable, MouseListener, KeyListener {

    // Screen settings
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private final Player player = new Player();
    private final RollSystem rollSystem = new RollSystem();
    private final RollPopup popup = new RollPopup();
    private final InventoryUI inventoryUI = new InventoryUI();
    private final SaveSystem saveSystem = new SaveSystem();
    private final CraftingUI craftingUI = new CraftingUI();

    private boolean autoRoll = false;

    private long lastAutoRoll = 0;

    private final Rectangle inventoryButton =
        new Rectangle(1120, 320, 130, 55);

    private final Rectangle autoRollButton =
        new Rectangle(540, 640, 200, 55);

    private final Rectangle craftingButton =
        new Rectangle(1120, 240, 130, 55);

    private Thread gameThread;

    public GamePanel() {

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        saveSystem.load(player);

    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();

    }

    private void performRoll() {

        player.clicked();

        Aura rolled = rollSystem.roll(player.getLuckMultiplier());

        player.getInventory().addAura(rolled);

        player.addRoll();

        popup.show("You rolled: " + rolled.getName());

        saveSystem.save(player);

    }

    @Override
    public void run() {

        while (gameThread != null) {

            player.update();

            if (autoRoll &&
                System.currentTimeMillis() - lastAutoRoll >= 250) {

                performRoll();
                lastAutoRoll = System.currentTimeMillis();
            }

            repaint();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // Background
        g.drawImage(
            ImageLoader.bg1,
            0,
            0,
            WIDTH,
            HEIGHT,
            null
        );

        // Player
        player.draw(g);

        // Roll popup
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString(popup.getText(), 400, 48);

        // Equipped aura
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString(
            "Equipped: " + player.getEquippedAura().getName(),
            400,
            96
        );

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString(
            "Luck: " + player.getLuckMultiplier() + "x",
            400,
            130
        );

        g.setColor(new Color(70, 120, 255));
        g.fillRoundRect(
            inventoryButton.x,
            inventoryButton.y,
            inventoryButton.width,
            inventoryButton.height,
            15,
            15
        );

        g.setColor(Color.WHITE);
        g.drawRoundRect(
            inventoryButton.x,
            inventoryButton.y,
            inventoryButton.width,
            inventoryButton.height,
            15,
            15
        );

        g.setColor(new Color(50, 50, 50));
        g.fillRoundRect(
            autoRollButton.x,
            autoRollButton.y,
            autoRollButton.width,
            autoRollButton.height,
            15,
            15
        );

        g.setColor(Color.WHITE);
        g.drawRoundRect(
            autoRollButton.x,
            autoRollButton.y,
            autoRollButton.width,
            autoRollButton.height,
            15,
            15
        );

        g.setColor(new Color(255, 200, 50));
        g.fillRoundRect(
            craftingButton.x,
            craftingButton.y,
            craftingButton.width,
            craftingButton.height,
            15,
            15
        );

        g.setColor(Color.WHITE);
        g.drawRoundRect(
            craftingButton.x,
            craftingButton.y,
            craftingButton.width,
            craftingButton.height,
            15,
            15
        );

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(
            "Crafting",
            craftingButton.x + 45,
            craftingButton.y + 33
        );

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(
            autoRoll ? "Auto: ON" : "Auto: OFF",
            autoRollButton.x + 15,
            autoRollButton.y + 33
        );

        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString(
            "Inventory",
            inventoryButton.x + 18,
            inventoryButton.y + 32
        );

        if (player.getActivePotion() != null) {

            // Background
            g.setColor(new Color(30, 30, 30, 220));
            g.fillRoundRect(1010, 610, 240, 80, 15, 15);

            // Border
            g.setColor(Color.WHITE);
            g.drawRoundRect(1010, 610, 240, 80, 15, 15);

            // Title
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(
                player.getActivePotion().getName(),
                1025,
                640
            );

            // Timer
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString(
                player.getPotionSecondsLeft() + "s",
                1025,
                665
            );

        }

        g.drawString("Rolls: " + player.getTotalRolls(), 20, 40);

        // Inventory (draw last so it's on top)
        inventoryUI.draw(g, player);
        craftingUI.draw(g, player);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        // Toggle Crafting menu
        if (craftingButton.contains(e.getPoint())) {

            inventoryUI.close();
            craftingUI.toggle();
            return;

        }

        // Toggle Inventory
        if (inventoryButton.contains(e.getPoint())) {

            craftingUI.close();
            inventoryUI.toggle();
            return;

        }

        // Handle clicks inside Crafting
        if (craftingUI.isOpen()) {

            craftingUI.click(
                e.getX(),
                e.getY(),
                player,
                popup
            );
            return;

        }

        // Handle clicks inside Inventory
        if (inventoryUI.isOpen()) {

            inventoryUI.click(e.getX(), e.getY(), player, popup);
            return;

        }

        // Auto Roll button
        if (autoRollButton.contains(e.getPoint())) {

            autoRoll = !autoRoll;
            return;

        }

        // Roll the cat
        if (player.contains(e.getX(), e.getY())) {

            performRoll();

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_E) {
            inventoryUI.toggle();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
