package software.greysky.towerdefense.legacy.games.td.screens;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.FitViewport;

import software.greysky.towerdefense.TowerDefense;
import software.greysky.towerdefense.legacy.games.td.Assets;
import software.greysky.towerdefense.legacy.games.td.levels.LevelOne;

public class MainMenu implements Screen {

	private final Game game;
	private OrthographicCamera camera;
	private Stage stage;
	private TextButton playBtn, optionBtn, quitBtn;
	private Image logo, bg;
	private Random random = new Random();

	public MainMenu(final Game game) {
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
		FitViewport viewport = new FitViewport(TowerDefense.WIDTH, TowerDefense.HEIGHT, camera);
		stage = new Stage(viewport);

		TextButtonStyle btnStyle = new TextButtonStyle();
		btnStyle.font = Assets.textBtnFont;
		btnStyle.up = Assets.skin.getDrawable("btn");

		playBtn = new TextButton("Play", btnStyle);
		optionBtn = new TextButton("Options", btnStyle);
		quitBtn = new TextButton("Quit", btnStyle);

		playBtn.setPosition((stage.getWidth() / 2) - (playBtn.getWidth()/2), (stage.getHeight() / 2) + 90);
		optionBtn.setPosition((stage.getWidth() / 2) - (optionBtn.getWidth()/2), playBtn.getY() - 180);
		quitBtn.setPosition((stage.getWidth() / 2) - (quitBtn.getWidth()/2), optionBtn.getY() - 180);

		playBtn.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				Color c = pickColor();
				playBtn.setColor(c);
			}
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				playBtn.setColor(1, 1, 1, 1);
			}
			public void clicked(InputEvent e, float x, float y) {
				Gdx.input.setInputProcessor(null);
				game.setScreen(new LevelOne(game));
			}
		});
		optionBtn.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				Color c = pickColor();
				optionBtn.setColor(c);
			}
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				optionBtn.setColor(1, 1, 1, 1);
			}
			public void clicked(InputEvent e, float x, float y) {
				Gdx.input.setInputProcessor(null);
				game.setScreen(new OptionsScreen(game));
			}
		});
		quitBtn.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				Color c = pickColor();
				quitBtn.setColor(c);
			}
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				quitBtn.setColor(1, 1, 1, 1);
			}
			public void clicked(InputEvent e, float x, float y) {
				Gdx.app.exit();
			}
		});

		logo = new Image(Assets.logo);
		logo.setPosition((stage.getWidth() / 2) - (logo.getWidth() / 2), stage.getHeight() - logo.getHeight());

		bg = new Image(Assets.bg);
		bg.setPosition(0, 0);

		stage.addActor(bg);
		stage.addActor(playBtn);
		stage.addActor(optionBtn);
		stage.addActor(quitBtn);
		stage.addActor(logo);

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
