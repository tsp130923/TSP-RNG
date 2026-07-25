package game.tsprng.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import game.tsprng.TSPRNG;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(true);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new TSPRNG();
    }
}