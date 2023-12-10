package software.greysky.towerdefense.legacy.games.td.screens;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import software.greysky.towerdefense.TowerDefense;
import software.greysky.towerdefense.legacy.games.td.Assets;

public class OptionsScreen implements Screen {

	private final Game game;
	private OrthographicCamera camera;
	private Stage stage;
	private TextButton doneBtn;
	private Random random = new Random();

	public OptionsScreen(final Game game) {
		this.game = game;
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, TowerDefense.WIDTH, TowerDefense.HEIGHT);

		createStage();
	}

	public void render(float delta) {
		update(delta);
		draw(delta);
	}

	private void update(float delta) {
		stage.act(delta);
	}

	private void draw(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.draw();
	}

	private void createStage() {
		this.stage = new Stage(new FitViewport(TowerDefense.WIDTH, TowerDefense.HEIGHT, camera));

		TextButtonStyle btnStyle = new TextButtonStyle();
		btnStyle.font = new BitmapFont(Gdx.files.internal("pixel.fnt"), false);
		btnStyle.up = Assets.skin.getDrawable("btn");

		doneBtn = new TextButton("Done", btnStyle);
		doneBtn.setPosition((stage.getWidth() / 2) - (doneBtn.getWidth()/2), 100);
		doneBtn.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				Color c = pickColor();
				doneBtn.setColor(c);
			}
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				doneBtn.setColor(1, 1, 1, 1);
			}
			public void clicked(InputEvent e, float x, float y) {
				Gdx.input.setInputProcessor(null);
				game.setScreen(new MainMenu(game));
			}
		});

		Image bg = new Image(Assets.bg);
		bg.setPosition(0, 0);

		this.stage.addActor(bg);
		this.stage.addActor(doneBtn);

		Gdx.input.setInputProcessor(stage);
	}

	private Color pickColor() {
		Color color = null;
		int c = random.nextInt(6);
		if (c == 0)
			color = new Color(1, 0, 0, 1);
		else if (c == 1)
			color = new Color(0, 1, 0, 1);
		else if (c == 2)
			color = new Color(0, 1, 1, 1);
		else if (c == 3)
			color = new Color(1, 0, 1, 1);
		else if (c == 4)
			color = new Color(1, 1, 0, 1);
		else if (c == 5)
			color = new Color(Color.ORANGE);
		return color;
	}

	public void show() {}
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	public void pause() {}
	public void resume() {}
	public void hide() {}
	public void dispose() {
		stage.dispose();
	}

}
