package game.tsprng;

import java.util.HashMap;

public class InventoryClickSystem {

    static class Box {
        String aura;
        float x, y, w, h;

        Box(String aura, float x, float y, float w, float h) {
            this.aura = aura;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }

    private static final HashMap<String, Box> boxes = new HashMap<>();

    public static void register(String aura, float x, float y, float w, float h) {
        boxes.put(aura, new Box(aura, x, y, w, h));
    }

    public static String checkClick(float mx, float my) {

        for (Box b : boxes.values()) {

            if (mx >= b.x && mx <= b.x + b.w &&
                my >= b.y && my <= b.y + b.h) {
                return b.aura;
            }
        }

        return null;
    }

    public static void clear() {
        boxes.clear();
    }
}
