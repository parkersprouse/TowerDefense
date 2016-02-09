package cs.games.td.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import cs.games.td.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tower Defense";
		config.width = 1920;
		config.height = 1080;
		config.resizable = true;
		config.vSyncEnabled = true;
		config.fullscreen = false;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		new LwjglApplication(new Main(), config);
	}
}
