package software.greysky.towerdefense.legacy.games.td;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import software.greysky.towerdefense.TowerDefense;

public class Assets {

	public static TextureAtlas atlas;
	public static Sprite logo, bg, gameOver;
	public static BitmapFont hudFont, menuFont, textBtnFont;
	public static Skin skin;

	private static FreeTypeFontGenerator gen;
	private static FreeTypeFontParameter par = new FreeTypeFontParameter();

	public static void createAssets() {
		atlas = new TextureAtlas("assets.atlas");
		logo = atlas.createSprite("logo");
		gameOver = atlas.createSprite("TempYouSuck");
		bg = atlas.createSprite("bg");

		skin = new Skin();
		skin.addRegions(atlas);

		//hudFont = new BitmapFont(Gdx.files.internal("skin/turretmenu/flag48.fnt"), false);

		TowerDefense.SCALE = 1.0f * Gdx.graphics.getWidth() / TowerDefense.WIDTH;
		if (TowerDefense.SCALE < 1) TowerDefense.SCALE = 1;

		// Font used in the turret menus
		gen = new FreeTypeFontGenerator(Gdx.files.internal("lucon.ttf"));
		par.size = (int)(20*TowerDefense.SCALE);
		menuFont = gen.generateFont(par);
		// menuFont.setScale((float) (1.0f / TowerDefense.SCALE));
		menuFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// Font used in the UI - money, wave, health
		gen = new FreeTypeFontGenerator(Gdx.files.internal("PixelFlag.ttf"));
		par.size = (int)(48*TowerDefense.SCALE);
		hudFont = gen.generateFont(par);
		// hudFont.setScale((float) (1.0f / TowerDefense.SCALE));
		hudFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// Font used in the big buttons
		/*
		gen = new FreeTypeFontGenerator(Gdx.files.internal("PixelFlag.ttf"));
		par.size = (int)(48*TowerDefense.SCALE);
		hudFont = gen.generateFont(par);
		hudFont.setScale((float) (1.0f / TowerDefense.SCALE));
		hudFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		*/
		textBtnFont = new BitmapFont(Gdx.files.internal("pixel.fnt"), false);
	}

}
