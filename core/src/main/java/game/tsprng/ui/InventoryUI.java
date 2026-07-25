package game.tsprng.ui;

import game.tsprng.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import game.tsprng.aura.Aura;
import game.tsprng.potion.Potion;

public class InventoryUI {

    private final List<InventorySlot> slots = new ArrayList<>();
    private final List<PotionSlot> potionSlots = new ArrayList<>();

    private final Rectangle closeButton =
        new Rectangle(1030, 70, 30, 30);

    private int selectedTab = 0; // 0 = Auras, 1 = Potions

    private boolean open = false;

    public void toggle() {
        open = !open;
    }

    public boolean isOpen() {
        return open;
    }

    public void close() {
        open = false;
    }


    public void draw(Graphics g, Player player) {

        slots.clear();
        potionSlots.clear();

        if (!open) {
            return;
        }

        g.setColor(new Color(30, 30, 30, 220));
        g.fillRoundRect(200, 60, 880, 600, 20, 20);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("Inventory", 240, 100);

        // Auras tab
        if (selectedTab == 0) {
            g.setColor(new Color(90, 130, 255));
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
        g.drawString("Auras", 240, 140);

        // Potions tab
        if (selectedTab == 1) {
            g.setColor(new Color(90, 130, 255));
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
        g.drawString("Potions", 360, 140);

        g.setColor(Color.WHITE);

        // ======================
        // AURA INVENTORY
        // ======================
        if (selectedTab == 0) {

            int y = 180;

            for (Aura aura : player.getInventory().getAuras().keySet()) {

                int count = player.getInventory().getCount(aura);

                Rectangle bounds = new Rectangle(220, y - 25, 500, 40);

                slots.add(new InventorySlot(aura, bounds));

                g.setColor(new Color(60, 60, 60));
                g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10);

                g.setColor(Color.WHITE);
                g.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10);

                g.drawString(
                    aura.getName() + " x" + count,
                    bounds.x + 15,
                    bounds.y + 28
                );

                y += 50;
            }
        }

        // ======================
        // POTION INVENTORY
        // ======================
        if (selectedTab == 1) {

            int y = 180;

            for (Potion potion : player.getPotionInventory().getPotions().keySet()) {

                int count = player.getPotionInventory().getCount(potion);

                Rectangle bounds = new Rectangle(220, y - 25, 500, 40);
                potionSlots.add(new PotionSlot(potion, bounds));

                g.setColor(new Color(60, 60, 60));
                g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10);

                g.setColor(Color.WHITE);
                g.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10);

                g.drawString(
                    potion.getName() + " x" + count,
                    bounds.x + 15,
                    bounds.y + 28
                );

                y += 50;
            }
        }

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

            Rectangle auraTab = new Rectangle(230, 115, 100, 35);
            Rectangle potionTab = new Rectangle(350, 115, 120, 35);

            if (auraTab.contains(mouseX, mouseY)) {
                selectedTab = 0;
                return;
            }

            if (potionTab.contains(mouseX, mouseY)) {
                selectedTab = 1;
                return;
            }

            for (InventorySlot slot: slots) {

                if (slot.bounds.contains(mouseX, mouseY)) {

                    player.setEquippedAura(slot.aura);
                    return;

                }

            }

            if (selectedTab == 1) {

                for (PotionSlot slot : potionSlots) {

                    if (slot.bounds.contains(mouseX, mouseY)) {

                        player.usePotion(slot.potion);
                        popup.show("Used " + slot.potion.getName() + "!");

                        return;
                    }

                }

            }

        }}
