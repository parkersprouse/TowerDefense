package cs.games.td;

import com.badlogic.gdx.Game;

import cs.games.td.screens.MainMenu;

public class Main extends Game {
	
	public static final float WIDTH = 1920;
	public static final float HEIGHT = 1080;
	public static float SCALE;
	
	public void create () {
		Assets.createAssets();
		this.setScreen(new MainMenu(this));
	}

}
