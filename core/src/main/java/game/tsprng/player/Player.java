package game.tsprng.player;

import game.tsprng.assets.ImageLoader;
import game.tsprng.aura.Aura;
import game.tsprng.aura.AuraDatabase;
import game.tsprng.device.Device;
import game.tsprng.inventory.Inventory;
import game.tsprng.inventory.PotionInventory;
import game.tsprng.potion.Potion;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import java.awt.Color;
import java.awt.Graphics;

public class Player {

    private int x = 640;
    private int y = 360;

    private final Inventory inventory = new Inventory();

    private Aura equippedAura = AuraDatabase.COMMON;

    private double luckMultiplier = 1.0;

    private long totalRolls = 0;

    private static final int BASE_SIZE = 256;

    private double time = 0;

    private double rotation = 0;

    private double scale = 1.0;
    private double scaleVelocity = 0.0;

    private Device equippedDevice = null;

    private boolean ownsLuckyGlove = false;

    private Potion activePotion;
    private long potionEndTime;

    public void update() {

        time += 0.05;
        rotation = Math.sin(time) * 25;

        // Bounce physics
        scale += scaleVelocity;
        scaleVelocity *= 0.85;

        // Pull back to normal size
        scale += (1.0 - scale) * 0.15;

        // Expire active potion
        if (activePotion != null &&
            System.currentTimeMillis() >= potionEndTime) {

            activePotion = null;

        }

    }

    public int getPotionSecondsLeft() {

        if (activePotion == null) {
            return 0;
        }

        return (int)Math.max(
            0,
            (potionEndTime - System.currentTimeMillis()) / 1000
        );

    }

    public void clicked() {

        scaleVelocity += 0.15;

    }

    private final PotionInventory potionInventory =
        new PotionInventory();

    public PotionInventory getPotionInventory() {
        return potionInventory;
    }

    public boolean ownsLuckyGlove() {
        return ownsLuckyGlove;
    }

    public void setOwnsLuckyGlove(boolean ownsLuckyGlove) {
        this.ownsLuckyGlove = ownsLuckyGlove;
    }

    public Device getEquippedDevice() {
        return equippedDevice;
    }

    public void setEquippedDevice(Device device) {
        equippedDevice = device;
    }

    public boolean contains(int mouseX, int mouseY) {

        int drawSize = (int) (BASE_SIZE * scale);

        return mouseX >= x - drawSize / 2 &&
            mouseX <= x + drawSize / 2 &&
            mouseY >= y - drawSize / 2 &&
            mouseY <= y + drawSize / 2;

    }

    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        int drawSize = (int) (BASE_SIZE * scale);

        AffineTransform old = g2.getTransform();

        g2.rotate(Math.toRadians(rotation), x, y);

        g2.drawImage(
            ImageLoader.cat,
            x - drawSize / 2,
            y - drawSize / 2,
            drawSize,
            drawSize,
            null
        );

        g2.setTransform(old);
    }

    public void usePotion(Potion potion) {

        if (potionInventory.getCount(potion) <= 0) {
            return;
        }

        potionInventory.removePotion(potion, 1);

        activePotion = potion;

        potionEndTime =
            System.currentTimeMillis() +
                potion.getDurationSeconds() * 1000L;

    }

    public Potion getActivePotion() {
        return activePotion;
    }

    public long getPotionEndTime() {
        return potionEndTime;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Aura getEquippedAura() {
        return equippedAura;
    }

    public void setEquippedAura(Aura aura) {
        equippedAura = aura;
    }

    public double getLuckMultiplier() {

        double luck = 1.0;

        // Device bonus
        if (equippedDevice != null) {
            luck *= equippedDevice.getLuckMultiplier();
        }

        // Potion bonus
        if (activePotion != null) {
            luck *= activePotion.getLuckMultiplier();
        }

        return luck;

    }

    public void setLuckMultiplier(double luckMultiplier) {
        this.luckMultiplier = luckMultiplier;
    }

    public long getTotalRolls() {
        return totalRolls;
    }

    public void setTotalRolls(long totalRolls) {
        this.totalRolls = totalRolls;
    }

    public void addRoll() {
        totalRolls++;
    }

}
