package game.tsprng.aura;

import java.util.List;

public class AuraDatabase {

    public static final Aura COMMON =
        new Aura("Common", 2L, "common");

    public static final Aura RARE =
        new Aura("Rare", 25L, "rare");

    public static final Aura GEMSTONE =
        new Aura("Gemstone", 125L, "gemstone");

    public static final Aura FORTUNE =
        new Aura("Fortune", 650L, "fortune");

    public static final Aura POLARITY =
        new Aura("Polarity", 2_000L, "polarity");

    public static final Aura CAKE =
        new Aura("CAKE!!!", 6_767L, "cake");

    public static final Aura INFERNO =
        new Aura("Inferno", 20_000L, "inferno");

    public static final Aura AQUATIC =
        new Aura("Aquatic", 45_000L, "aquatic");

    public static final Aura MYSTERY =
        new Aura("Mystery", 99_999L, "mystery");

    public static final Aura ZOMBIE =
        new Aura("Zombie", 650_000L, "zombie");

    public static final Aura ORBIT =
        new Aura("Orbit", 2_500_000L, "orbit");

    public static final Aura COSMIC =
        new Aura("Cosmic", 6_000_000L, "cosmic");

    public static final Aura VOYAGER =
        new Aura("Voyager", 15_000_000L, "voyager");

    public static final Aura CLOCKWORK =
        new Aura("Clockwork", 35_000_000L, "clockwork");

    public static final Aura SHIPWRECK =
        new Aura("Shipwreck", 85_000_000L, "shipwreck");

    public static final Aura ANGEL =
        new Aura("Angel", 175_000_000L, "angel");

    public static final Aura LEVIATHAN =
        new Aura("Leviathan", 350_000_000L, "leviathan");

    public static final Aura ERROR =
        new Aura("Error", 550_000_000L, "error");

    public static final Aura AEGIS =
        new Aura("Aegis", 850_000_000L, "aegis");

    public static final Aura LIGHT =
        new Aura("Light", 1_250_000_000L, "light");

    public static final Aura BALANCE =
        new Aura("Balance", 2_500_000_000L, "balance");

    public static final Aura OBLIVION =
        new Aura("Oblivion", 10_000_000_000L, "oblivion");

    public static final Aura ADMIN =
        new Aura("im a admin", Long.MAX_VALUE, "shh this folder is admin only");

    public static final List<Aura> ALL_AURAS = List.of(
        OBLIVION,
        BALANCE,
        LIGHT,
        AEGIS,
        ERROR,
        LEVIATHAN,
        ANGEL,
        SHIPWRECK,
        CLOCKWORK,
        VOYAGER,
        COSMIC,
        ORBIT,
        ZOMBIE,
        MYSTERY,
        AQUATIC,
        INFERNO,
        CAKE,
        POLARITY,
        FORTUNE,
        GEMSTONE,
        RARE,
        COMMON
    );

    public static Aura getById(String id) {

        for (Aura aura : ALL_AURAS) {

            if (aura.getId().equals(id)) {
                return aura;
            }

        }

        return null;
    }

}
