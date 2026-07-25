package game.tsprng;

import com.badlogic.gdx.math.MathUtils;

public class RollSystem {

    public static Aura roll(double luck) {

        double r = MathUtils.random();

        // clamp luck so potion boosts stay intentional without overflowing odds
        luck = Math.max(1.0, luck);

        // =========================
        // VERY RARE AURAS
        // =========================
        if (r < (1.0 / 10_000_000_000.0) * luck) {
            return new Aura("OBLIVION, the truth seeker", 10000000000L);
        }

        if (r < (1.0 / 2_500_000_000.0) * luck) {
            return new Aura("EQUINOX, the force between positive and negative", 2500000000L);
        }

        if (r < (1.0 / 1_200_000_000.0) * luck) {
            return new Aura("LUMINOSITY, the absolute radiance", 1200000000L);
        }

        if  (r < (1.0 / 825_000_000.0) * luck) {
            return new Aura("AEGIS, 1 in 825,000,000", 825000000L);
        }

        if (r < (1.0 / 503_000_000.0) * luck) {
            return new Aura("MATRIX_OVERDRIVE, 1 in 503,000,000", 503000000L);
        }

        if (r < (1.0 / 400_000_000.0) * luck) {
            return new Aura("ABYSSALHUNTER, 1 in 400,000,000", 400000000L);
        }

        if (r < (1.0 / 175_000_000.0) * luck) {
            return new Aura("SYMPHONY, 1 in 175,000,000", 175000000L);
        }

        if (r < (1.0 / 70_000_000.0) * luck) {
            return new Aura("SAILOR_FLYINGDUTCHMAN, 1 in 70,000,000", 70000000L);
        }

        if (r < (1.0 / 30_000_000.0) * luck) {
            return new Aura("ARCANE_DARK, 1 in 30,000,000", 30000000L);
        }

        if (r < (1.0 / 12_000_000.0) * luck) {
            return new Aura("SAILOR, 1 in 12,000,000", 12000000L);
        }

        if (r < (1.0 / 5_000_000.0) * luck) {
            return new Aura("GALAXY, 1 in 5,000,000", 5000000L);
        }

        if (r < (1.0 / 2_000_000.0) * luck) {
            return new Aura("GRAVITATIONAL, 1 in 2,000,000", 2000000L);
        }

        if (r < (1.0 / 666_666.0) * luck) {
            return new Aura("UNDEAD_DEVIL, 1 in 666,666", 666666L);
        }

        if (r < (1.0 / 99_999.0) * luck) {
            return new Aura("EXOTIC, 1 in 99,999", 99999L);
        }

        if (r < (1.0 / 40_000.0) * luck) {
            return new Aura("AQUATIC, 1 in 40,000", 40000L);
        }

        if (r < (1.0 / 12_500.0) * luck) {
            return new Aura("RAGE_HEATED, 1 in 12,500", 12500L);
        }

        if (r < (1.0 / 6_900.0) * luck) {
            return new Aura("FLUSHED, 1 in 6,900", 6900L);
        }

        if (r < (1.0 / 2048.0) * luck) {
            return new Aura("MAGNETIC, 1 in 2,048", 2048L);
        }

        if (r < (1.0 / 777.0) * luck) {
            return new Aura("JACKPOT, 1 in 777", 777L);
        }

        if (r < (1.0 / 150.0) * luck) {
            return new Aura("TOPAZ, 1 in 150", 150L);
        }

        if (r < (1.0 / 16.0) * luck) {
            return new Aura("RARE, 1 in 16", 16L);
        }

        return new Aura("COMMON, 1 in 2", 2L);
    }

    public static Aura rollHeavenly() {

        Aura[] pool = {
            new Aura("OBLIVION, the truth seeker", 10000000000L),
            new Aura("EQUINOX, the force between positive and negative", 2500000000L),
            new Aura("LUMINOSITY, the absolute radiance", 1200000000L),
            new Aura("AEGIS, 1 in 825,000,000", 825000000L),
            new Aura("MATRIX_OVERDRIVE, 1 in 503,000,000", 503000000L),
            new Aura("ABYSSALHUNTER, 1 in 400,000,000", 400000000L),
            new Aura("SYMPHONY, 1 in 175,000,000", 175000000L),
            new Aura("SAILOR_FLYINGDUTCHMAN, 1 in 70,000,000", 70000000L),
            new Aura("ARCANE_DARK, 1 in 30,000,000", 30000000L),
            new Aura("SAILOR, 1 in 12,000,000", 12000000L),
            new Aura("GALAXY, 1 in 5,000,000", 5000000L),
            new Aura("GRAVITATIONAL, 1 in 2,000,000", 2000000L)
        };

        double totalWeight = 0;

        for (Aura aura : pool) {
            long adjusted = Math.max(1, Math.round(aura.rarity / 350000.0));
            totalWeight += 1.0 / adjusted;
        }

        double roll = MathUtils.random() * totalWeight;
        double current = 0;

        for (Aura aura : pool) {

            long adjusted = Math.max(1, Math.round(aura.rarity / 350000.0));
            current += 1.0 / adjusted;

            if (roll <= current) {
                return aura;
            }
        }

        return pool[pool.length - 1];
    }

    public static Aura rollBound() {

        Aura[] pool = {
            new Aura("OBLIVION, the truth seeker", 10000000000L),
            new Aura("EQUINOX, the force between positive and negative", 2500000000L),
            new Aura("LUMINOSITY, the absolute radiance", 1200000000L),
            new Aura("AEGIS, 1 in 825,000,000", 825000000L),
            new Aura("MATRIX_OVERDRIVE, 1 in 503,000,000", 503000000L),
            new Aura("ABYSSALHUNTER, 1 in 400,000,000", 400000000L),
            new Aura("SYMPHONY, 1 in 175,000,000", 175000000L),
            new Aura("SAILOR_FLYINGDUTCHMAN, 1 in 70,000,000", 70000000L),
            new Aura("ARCANE_DARK, 1 in 30,000,000", 30000000L),
            new Aura("SAILOR, 1 in 12,000,000", 12000000L),
            new Aura("GALAXY, 1 in 5,000,000", 5000000L),
            new Aura("GRAVITATIONAL, 1 in 2,000,000", 2000000L),
            new Aura("UNDEAD_DEVIL, 1 in 666,666", 666666L),
            new Aura("EXOTIC, 1 in 99,999", 99999L),
            new Aura("AQUATIC, 1 in 40,000", 40000L),
            new Aura("RAGE_HEATED, 1 in 12,500", 12500L)
        };

        double totalWeight = 0;

        // calculate weighted pool with compression
        for (Aura aura : pool) {
            long adjusted = Math.max(1, Math.round(aura.rarity / 20000.0));
            totalWeight += 1.0 / adjusted;
        }

        double roll = MathUtils.random() * totalWeight;
        double current = 0;

        for (Aura aura : pool) {

            long adjusted = Math.max(1, Math.round(aura.rarity / 20000.0));
            current += 1.0 / adjusted;

            if (roll <= current) {
                return aura;
            }
        }

        return pool[pool.length - 1];
    }
}
