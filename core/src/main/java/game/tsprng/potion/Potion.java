package game.tsprng.potion;

public class Potion {

    private final String id;
    private final String name;
    private final double luckMultiplier;
    private final int durationSeconds;

    public Potion(
        String id,
        String name,
        double luckMultiplier,
        int durationSeconds
    ) {
        this.id = id;
        this.name = name;
        this.luckMultiplier = luckMultiplier;
        this.durationSeconds = durationSeconds;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLuckMultiplier() {
        return luckMultiplier;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

}
