package game.tsprng.ui;

public class RollPopup {

    private String text = "";
    private long showUntil = 0;

    public void show(String message) {
        text = message;
        showUntil = System.currentTimeMillis() + 2000;
    }

    public String getText() {

        if (System.currentTimeMillis() > showUntil) {
            return "";
        }

        return text;
    }

}
