package game.tsprng.save;

import game.tsprng.aura.Aura;
import game.tsprng.aura.AuraDatabase;
import game.tsprng.player.Player;
import game.tsprng.potion.Potion;
import game.tsprng.potion.PotionDatabase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class SaveSystem {

    private static final File SAVE_FILE =
        new File(System.getProperty("user.home"), "TSPRNG/save.txt");

    public void save(Player player) {


        try {

            SAVE_FILE.getParentFile().mkdirs();

            FileWriter writer = new FileWriter(SAVE_FILE);

            writer.write(
                "totalRolls=" + player.getTotalRolls() + "\n"
            );

            for (Aura aura : player.getInventory().getAuras().keySet()) {

                writer.write(
                    aura.getId() + "=" +
                        player.getInventory().getCount(aura) + "\n"
                );

            }

            for (Potion potion : player.getPotionInventory().getPotions().keySet()) {

                writer.write(
                    "potion_" + potion.getId() + "=" +
                        player.getPotionInventory().getCount(potion) + "\n"
                );

            }

            writer.write("ownsLuckyGlove=" + player.ownsLuckyGlove() + "\n");

            writer.close();

            System.out.println("Game saved!");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void load(Player player) {

        if (!SAVE_FILE.exists()) {
            System.out.println("No save found.");
            return;
        }

        try {

            BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE));

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.startsWith("potion_")) {

                    String[] parts = line.split("=");

                    String id = parts[0].substring("potion_".length());

                    Potion potion = PotionDatabase.getById(id);

                    if (potion != null) {

                        int amount = Integer.parseInt(parts[1]);

                        player.getPotionInventory().setCount(potion, amount);

                    }

                    continue;
                }

                if (line.startsWith("totalRolls=")) {

                    long rolls = Long.parseLong(
                        line.substring("totalRolls=".length())
                    );

                    player.setTotalRolls(rolls);

                } else if (line.startsWith("ownsLuckyGlove=")) {

                    boolean owned = Boolean.parseBoolean(
                        line.substring("ownsLuckyGlove=".length())
                    );

                    player.setOwnsLuckyGlove(owned);

                } else {

                    String[] parts = line.split("=");

                    if (parts.length == 2) {

                        Aura aura = AuraDatabase.getById(parts[0]);

                        if (aura != null) {

                            int amount = Integer.parseInt(parts[1]);

                            player.getInventory().setCount(aura, amount);

                        }

                    }

                }

            }

            reader.close();

            System.out.println("Game loaded!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
