package game.tsprng.potion;

public class PotionDatabase {

    public static final Potion LUCKY_POTION_I =
        new Potion(
            "lucky_potion_i",
            "Lucky Potion I",
            2.0,
            60
        );

    public static Potion getById(String id) {

        if (LUCKY_POTION_I.getId().equals(id)) {
            return LUCKY_POTION_I;
        }

        return null;
    }

}
