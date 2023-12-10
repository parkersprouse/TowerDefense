// package software.greysky.towerdefense;

// import com.badlogic.gdx.ApplicationAdapter;
// import com.badlogic.gdx.graphics.Texture;
// import com.badlogic.gdx.graphics.g2d.SpriteBatch;
// import com.badlogic.gdx.utils.ScreenUtils;

// public class TowerDefense extends ApplicationAdapter {
// 	SpriteBatch batch;
// 	Texture img;

// 	@Override
// 	public void create () {
// 		batch = new SpriteBatch();
// 		img = new Texture("badlogic.jpg");
// 	}

// 	@Override
// 	public void render () {
// 		ScreenUtils.clear(1, 0, 0, 1);
// 		batch.begin();
// 		batch.draw(img, 0, 0);
// 		batch.end();
// 	}

// 	@Override
// 	public void dispose () {
// 		batch.dispose();
// 		img.dispose();
// 	}
// }

package software.greysky.towerdefense;

import com.badlogic.gdx.Game;

import software.greysky.towerdefense.legacy.games.td.Assets;
import software.greysky.towerdefense.legacy.games.td.screens.MainMenu;

public class TowerDefense extends Game {

	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public static float SCALE;

	public void create () {
		Assets.createAssets();
		this.setScreen(new MainMenu(this));
	}

}
