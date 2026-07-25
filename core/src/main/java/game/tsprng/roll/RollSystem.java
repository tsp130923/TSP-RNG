package game.tsprng.roll;

import game.tsprng.aura.Aura;
import game.tsprng.aura.AuraDatabase;

import java.util.Random;

public class RollSystem {

    public static final long MAX_ROLL = 10_000_000_000L;

    private final Random random = new Random();

    public Aura roll(double luckMultiplier) {

        long rng = (long) (random.nextDouble() * MAX_ROLL) + 1;

        for (Aura aura : AuraDatabase.ALL_AURAS) {

            long threshold = (long) (luckMultiplier * MAX_ROLL / aura.getMaxRoll());

            if (rng <= threshold) {
                return aura;
            }

        }

        return AuraDatabase.COMMON;
    }
}
