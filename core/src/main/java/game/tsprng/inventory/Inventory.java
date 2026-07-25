package game.tsprng.inventory;

import game.tsprng.aura.Aura;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    public Map<Aura, Integer> getAuras() {
        return auras;
    }

    private final Map<Aura, Integer> auras = new HashMap<>();

    public void addAura(Aura aura) {
        auras.put(aura, auras.getOrDefault(aura, 0) + 1);
    }

    public void removeAura(Aura aura, int amount) {

        if (!auras.containsKey(aura)) {
            return;
        }

        int newAmount = auras.get(aura) - amount;

        if (newAmount <= 0) {
            auras.remove(aura);
        } else {
            auras.put(aura, newAmount);
        }
    }

    public int getCount(Aura aura) {
        return auras.getOrDefault(aura, 0);
    }

    public boolean hasAura(Aura aura) {
        return auras.containsKey(aura);
    }

    public void clear() {
        auras.clear();
    }

    public void setCount(Aura aura, int amount) {

        if (amount <= 0) {
            auras.remove(aura);
        } else {
            auras.put(aura, amount);
        }

    }

    public Map<Aura, Integer> getInventory() {
        return auras;
    }

}
