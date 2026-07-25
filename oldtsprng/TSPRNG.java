package game.tsprng;

import com.badlogic.gdx.Game;

public class TSPRNG extends Game {

    @Override
    public void create() {

        setScreen(new GameScreen());
    }
}
