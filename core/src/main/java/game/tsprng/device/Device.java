package game.tsprng.device;

public class Device {

    private final String name;
    private final double luckMultiplier;

    public Device(String name, double luckMultiplier) {
        this.name = name;
        this.luckMultiplier = luckMultiplier;
    }

    public String getName() {
        return name;
    }

    public double getLuckMultiplier() {
        return luckMultiplier;
    }
}
