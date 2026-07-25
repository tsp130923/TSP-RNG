package game.tsprng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.LinkedHashMap;

public class SaveSystem {

    private static final Preferences prefs =
        Gdx.app.getPreferences("TSPRNG_SAVE");

    public static void save(
        String equippedAura,
        LinkedHashMap<String, Integer> inventory
    ) {

        // save equipped aura
        prefs.putString("equippedAura", equippedAura);

        // clear old inventory first
        prefs.remove("inventoryKeys");

        StringBuilder keys = new StringBuilder();

        for (String aura : inventory.keySet()) {

            prefs.putInteger(
                "inv_" + aura,
                inventory.get(aura)
            );

            keys.append(aura).append(";");
        }

        prefs.putString("inventoryKeys", keys.toString());

        prefs.flush();
    }

    public static void save(
        String equippedAura,
        LinkedHashMap<String, Integer> inventory,
        String glove,
        double gloveLuck
    ) {
        save(equippedAura, inventory, glove, gloveLuck, LoadSystem.loadCoins());
    }

    public static void save(
        String equippedAura,
        LinkedHashMap<String, Integer> inventory,
        String glove,
        double gloveLuck,
        long coins
    ) {
        save(
            equippedAura,
            inventory,
            glove,
            gloveLuck,
            coins,
            LoadSystem.loadLuckyGloveCrafted(),
            LoadSystem.loadEclipseDeviceCrafted(),
            LoadSystem.loadDogOwned()
        );
    }

    public static void save(
        String equippedAura,
        LinkedHashMap<String, Integer> inventory,
        String glove,
        double gloveLuck,
        long coins,
        boolean luckyGloveCrafted,
        boolean eclipseDeviceCrafted,
        boolean dogOwned
    ) {
        Preferences prefs = Gdx.app.getPreferences("TSPRNG_SAVE");

        prefs.putString("equippedAura", equippedAura);

        prefs.putString("glove", glove);
        prefs.putFloat("gloveLuck", (float) gloveLuck);
        prefs.putLong("coins", coins);
        prefs.putBoolean("luckyGloveCrafted", luckyGloveCrafted);
        prefs.putBoolean("eclipseDeviceCrafted", eclipseDeviceCrafted);
        prefs.putBoolean("dogOwned", dogOwned);

        prefs.remove("inventoryKeys");

        StringBuilder keys = new StringBuilder();

        for (String aura : inventory.keySet()) {

            prefs.putInteger(
                "inv_" + aura,
                inventory.get(aura)
            );

            keys.append(aura).append(";");
        }

        prefs.putString("inventoryKeys", keys.toString());

        prefs.flush();
    }
}
