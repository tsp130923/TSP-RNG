package game.tsprng.inventory;

import game.tsprng.potion.Potion;

import java.util.HashMap;
import java.util.Map;

public class PotionInventory {

    private final Map<Potion, Integer> potions = new HashMap<>();

    public void addPotion(Potion potion) {
        potions.put(potion, potions.getOrDefault(potion, 0) + 1);
    }

    public void removePotion(Potion potion, int amount) {

        if (!potions.containsKey(potion)) {
            return;
        }

        int newAmount = potions.get(potion) - amount;

        if (newAmount <= 0) {
            potions.remove(potion);
        } else {
            potions.put(potion, newAmount);
        }

    }

    public int getCount(Potion potion) {
        return potions.getOrDefault(potion, 0);
    }

    public Map<Potion, Integer> getPotions() {
        return potions;
    }

    public void setCount(Potion potion, int amount) {

        if (potion == null) {
            return;
        }

        if (amount <= 0) {
            potions.remove(potion);
        } else {
            potions.put(potion, amount);
        }

    }

}
