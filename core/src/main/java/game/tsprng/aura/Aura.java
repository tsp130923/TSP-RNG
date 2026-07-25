package game.tsprng.aura;

public class Aura {

    private final String name;
    private final long maxRoll;
    private final String id;

    public Aura(String name, long maxRoll, String id) {
        this.name = name;
        this.maxRoll = maxRoll;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getMaxRoll() {
        return maxRoll;
    }

    public String getId() {
        return id;
    }

}
