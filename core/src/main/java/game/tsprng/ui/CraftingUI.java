package game.tsprng.ui;

import game.tsprng.aura.AuraDatabase;
import game.tsprng.device.DeviceDatabase;
import game.tsprng.player.Player;
import game.tsprng.potion.PotionDatabase;

import java.awt.*;

public class CraftingUI {

    public void close() {
        open = false;
    }

    private final Rectangle closeButton =
        new Rectangle(1118, 60, 32, 32);

    private int selectedCraft = 0;

    private boolean open = false;

    public void toggle() {
        open = !open;
    }

    public boolean isOpen() {
        return open;
    }

    private final Rectangle craftButton =
        new Rectangle(760, 570, 180, 50);

    public void draw(Graphics g, Player player) {

        if (!open) {
            return;
        }

        // Background
        g.setColor(new Color(30, 30, 30, 220));
        g.fillRoundRect(120, 50, 1040, 620, 20, 20);

        // Border
        g.setColor(Color.WHITE);
        g.drawRoundRect(120, 50, 1040, 620, 20, 20);

        // Title
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Crafting", 150, 90);

        // Divider
        g.drawLine(420, 110, 420, 630);

// Left side
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Potions", 150, 130);

        if (selectedCraft == 0) {
            g.setColor(new Color(90, 130, 255));
        } else {
            g.setColor(new Color(60, 60, 60));
        }

        g.fillRoundRect(140, 150, 240, 40, 10, 10);

        g.setColor(Color.WHITE);
        g.drawRoundRect(140, 150, 240, 40, 10, 10);

        g.drawString("Lucky Potion I", 155, 177);

        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Gloves", 150, 240);

        g.setFont(new Font("Arial", Font.PLAIN, 20));

        if (selectedCraft == 1) {
            g.setColor(new Color(90, 130, 255)); // selected
        } else {
            g.setColor(new Color(60, 60, 60));   // normal
        }
        g.fillRoundRect(140, 260, 240, 40, 10, 10);

        g.setColor(Color.WHITE);
        g.drawRoundRect(140, 260, 240, 40, 10, 10);

        g.drawString("Lucky Glove", 155, 287);

        if (selectedCraft == 0) {

            g.setFont(new Font("Arial", Font.BOLD, 26));
            g.drawString("Lucky Potion I", 470, 150);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("2x Luck", 470, 185);
            g.drawString("Duration: 60 seconds", 470, 215);

            g.drawString("Requires:", 470, 260);
            g.drawString("Common x25", 490, 300);
            g.drawString("Rare x5", 490, 330);

        } else {

            g.setFont(new Font("Arial", Font.BOLD, 26));
            g.drawString("Lucky Glove", 470, 150);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("2x Luck", 470, 185);

            g.drawString("Requires:", 470, 230);
            g.drawString("Common x50", 490, 270);
            g.drawString("Rare x10", 490, 300);
            g.drawString("Gemstone x3", 490, 330);

        }

        String buttonText;

        if (selectedCraft == 0) {

            buttonText = "Craft";

        } else {

            if (!player.ownsLuckyGlove()) {
                buttonText = "Craft";
            } else if (player.getEquippedDevice() == DeviceDatabase.LUCKY_GLOVE) {
                buttonText = "Equipped";
            } else {
                buttonText = "Equip";
            }

        }

        if (buttonText.equals("Craft") || buttonText.equals("Equip")) {
            g.setColor(new Color(70, 180, 70));
        } else {
            g.setColor(new Color(100, 100, 100));
        }

        g.fillRoundRect(
            craftButton.x,
            craftButton.y,
            craftButton.width,
            craftButton.height,
            10,
            10
        );

        g.setColor(Color.WHITE);
        g.drawRoundRect(
            craftButton.x,
            craftButton.y,
            craftButton.width,
            craftButton.height,
            10,
            10
        );

        g.setFont(new Font("Arial", Font.BOLD, 22));

        if (selectedCraft == 0) {

            // Potion
            buttonText = "Craft";

        } else {

            // Lucky Glove
            if (!player.ownsLuckyGlove()) {
                buttonText = "Craft";
            } else if (player.getEquippedDevice() == DeviceDatabase.LUCKY_GLOVE) {
                buttonText = "Equipped";
            } else {
                buttonText = "Equip";
            }

        }

        g.drawString(buttonText, 805, 602);

        g.setColor(new Color(180, 60, 60));
        g.fillRoundRect(
            closeButton.x,
            closeButton.y,
            closeButton.width,
            closeButton.height,
            8,
            8
        );

        g.setColor(Color.WHITE);
        g.drawRoundRect(
            closeButton.x,
            closeButton.y,
            closeButton.width,
            closeButton.height,
            8,
            8
        );

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("X", closeButton.x + 10, closeButton.y + 21);
    }


    public void click(int mouseX, int mouseY, Player player, RollPopup popup) {

        if (closeButton.contains(mouseX, mouseY)) {
            toggle();
            return;
        }

        Rectangle potionButton =
            new Rectangle(140, 150, 240, 40);

        Rectangle gloveButton =
            new Rectangle(140, 260, 240, 40);

        if (potionButton.contains(mouseX, mouseY)) {
            selectedCraft = 0;
        }

        if (gloveButton.contains(mouseX, mouseY)) {
            selectedCraft = 1;
        }

        if (craftButton.contains(mouseX, mouseY)) {

            // Lucky Potion I
            if (selectedCraft == 0) {

                if (player.getInventory().getCount(AuraDatabase.COMMON) >= 25 &&
                    player.getInventory().getCount(AuraDatabase.RARE) >= 5) {

                    player.getInventory().removeAura(AuraDatabase.COMMON, 25);
                    player.getInventory().removeAura(AuraDatabase.RARE, 5);

                    player.getPotionInventory().addPotion(
                        PotionDatabase.LUCKY_POTION_I
                    );

                    popup.show("Crafted Lucky Potion I!");

                } else {

                    popup.show("Not enough materials!");

                }

            }

            // Lucky Glove
            else {

                if (!player.ownsLuckyGlove()) {

                    if (player.getInventory().getCount(AuraDatabase.COMMON) >= 50 &&
                        player.getInventory().getCount(AuraDatabase.RARE) >= 10 &&
                        player.getInventory().getCount(AuraDatabase.GEMSTONE) >= 3) {

                        player.getInventory().removeAura(AuraDatabase.COMMON, 50);
                        player.getInventory().removeAura(AuraDatabase.RARE, 10);
                        player.getInventory().removeAura(AuraDatabase.GEMSTONE, 3);

                        player.setOwnsLuckyGlove(true);

                        popup.show("Crafted Lucky Glove!");

                    }

                } else {

                    if (player.getEquippedDevice() != DeviceDatabase.LUCKY_GLOVE) {

                        player.setEquippedDevice(DeviceDatabase.LUCKY_GLOVE);

                        popup.show("Equipped Lucky Glove!");

                    }

                }

            }

        }

    }
}
