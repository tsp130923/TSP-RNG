package game.tsprng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.LinkedHashMap;

public class LoadSystem {

    private static final Preferences prefs =
        Gdx.app.getPreferences("TSPRNG_SAVE");

    public static LinkedHashMap<String, Integer> loadInventory() {

        LinkedHashMap<String, Integer> inventory =
            new LinkedHashMap<>();

        String keys = prefs.getString("inventoryKeys", "");

        if (!keys.isEmpty()) {

            String[] split = keys.split(";");

            for (String aura : split) {

                if (!aura.isEmpty()) {

                    int amount =
                        prefs.getInteger("inv_" + aura, 0);

                    inventory.put(aura, amount);
                }
            }
        }

        return inventory;
    }

    public static String loadGlove() {
        Preferences prefs = Gdx.app.getPreferences("TSPRNG_SAVE");
        return prefs.getString("glove", "None");
    }

    public static double loadGloveLuck() {
        Preferences prefs = Gdx.app.getPreferences("TSPRNG_SAVE");
        return prefs.getFloat("gloveLuck", 1f);
    }

    public static long loadCoins() {
        return prefs.getLong("coins", 0L);
    }

    public static boolean loadLuckyGloveCrafted() {
        return prefs.getBoolean("luckyGloveCrafted", loadGlove().equals("Lucky Glove"));
    }

    public static boolean loadEclipseDeviceCrafted() {
        return prefs.getBoolean("eclipseDeviceCrafted", loadGlove().equals("Eclipse Device"));
    }

    public static boolean loadDogOwned() {
        return prefs.getBoolean("dogOwned", false);
    }

    public static String loadEquippedAura() {
        return prefs.getString("equippedAura", "NONE");
    }
}
