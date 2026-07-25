package game.tsprng.ui;

import game.tsprng.aura.Aura;

import java.awt.Rectangle;

public class InventorySlot {

    public Aura aura;
    public Rectangle bounds;

    public InventorySlot(Aura aura, Rectangle bounds) {
        this.aura = aura;
        this.bounds = bounds;
    }
}
