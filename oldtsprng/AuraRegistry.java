package game.tsprng;

import java.util.HashMap;

public class AuraRegistry {

    private static HashMap<String, Aura> auras = new HashMap<>();

    public static void register(Aura aura) {
        auras.put(aura.name, aura);
    }

    public static Aura get(String name) {
        return auras.getOrDefault(name, new Aura(name, 2L));
    }
}
