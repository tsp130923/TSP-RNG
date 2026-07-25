package game.tsprng.ui;

import game.tsprng.potion.Potion;
import java.awt.Rectangle;

public class PotionSlot {

    public final Potion potion;
    public final Rectangle bounds;

    public PotionSlot(Potion potion, Rectangle bounds) {
        this.potion = potion;
        this.bounds = bounds;
    }
}
