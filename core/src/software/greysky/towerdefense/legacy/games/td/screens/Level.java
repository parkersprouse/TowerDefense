package software.greysky.towerdefense.legacy.games.td.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import software.greysky.towerdefense.TowerDefense;
import software.greysky.towerdefense.legacy.games.td.Assets;
import software.greysky.towerdefense.legacy.games.td.EnemyController;
import software.greysky.towerdefense.legacy.games.td.Player;
import software.greysky.towerdefense.legacy.games.td.TurretManager;

public abstract class Level implements Screen {

	// Base Components
	protected final Game game;
	protected OrthographicCamera camera;
	protected SpriteBatch batch;
	protected Player player;
	protected boolean gameOver = false, isComplete = false;
	private Vector3 newCameraPos;

	// Stage Components
	protected Stage stage, hudStage;
	protected Group turretGroup;
	protected TextButton nextWaveBtn, buyTowerBtn;
	protected Label waveLabel, healthLabel, moneyLabel;
	protected Image gameOverImg;

	// Map Components
	protected OrthogonalTiledMapRenderer mapRenderer;
	protected TiledMap map;
	protected String mapFile;
	protected int mapWidth, mapHeight;

	// Managers
	protected TurretManager tm;
	protected EnemyController ec;

	// Level Wave Components
	protected int wave, finalWave, numEnemies;
	protected float timeBetweenEnemies;
	protected int levelHealth;

	protected Level(final Game game) {
		this.game = game;
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, TowerDefense.WIDTH, TowerDefense.HEIGHT);
		this.newCameraPos = new Vector3(this.camera.position.x, this.camera.position.y, 0);

		createStage();

		this.batch = new SpriteBatch();
		this.player = new Player();
		this.moneyLabel.setText("[Money|" + this.player.getMoney() + "]");
	}

	public int getWave() {
		return this.wave;
	}

	public int getFinalWave() {
		return this.finalWave;
	}

	public int getNumEnemies() {
		return this.numEnemies;
	}

	public float getTimeBetweenEnemies() {
		return this.timeBetweenEnemies;
	}

	public abstract void nextWave();

	public abstract void takeHit();

	public abstract void gameOver();

	public abstract void levelComplete();

	public boolean isComplete() {
		return this.isComplete;
	}

	public void render(float delta) {
		delta = Math.min(delta, 0.1f);

		if (!gameOver && !isComplete)
			update(delta);
		draw(delta);

		if (this.gameOver) {
			gameOver();
			if (!this.gameOverImg.isVisible())
				this.gameOverImg.setVisible(true);
		}
		else if (this.isComplete) levelComplete();
	}

	private void update(float delta) {
		this.moneyLabel.setText("[Money|" + this.player.getMoney() + "]");

		if (Gdx.input.getInputProcessor() == null)
			Gdx.input.setInputProcessor(this.hudStage);

		this.stage.act(delta);
		this.hudStage.act(delta);
		this.ec.update(delta);
		this.tm.update(delta);

		input(delta);

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && !this.gameOver && !this.isComplete) {
			Gdx.app.exit();
		}

		if (this.levelHealth <= 0) this.gameOver = true;

		if (this.wave == this.finalWave && EnemyController.numEnemiesGone == this.numEnemies) this.isComplete = true;

		if (EnemyController.isActive && this.nextWaveBtn.isVisible())
			this.nextWaveBtn.setVisible(false);
		else if (!EnemyController.isActive && !this.nextWaveBtn.isVisible() && !this.gameOver && !this.isComplete && this.wave < this.finalWave)
			this.nextWaveBtn.setVisible(true);
	}

	private void input(float delta) {
		if (Gdx.input.isKeyPressed(Keys.W)){// && 64*this.mapWidth > Main.WIDTH) {
			//this.camera.translate(0, 200 * delta, 0);
			this.newCameraPos.y += 400 * delta;
		}
		else if (Gdx.input.isKeyPressed(Keys.S)) {
			//this.camera.translate(0, -200 * delta, 0);
			this.newCameraPos.y -= 400 * delta;
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			//this.camera.translate(-200 * delta, 0, 0);
			this.newCameraPos.x -= 400 * delta;
		}
		else if (Gdx.input.isKeyPressed(Keys.D)){// && 64*this.mapHeight > Main.HEIGHT) {
			//this.camera.translate(200 * delta, 0, 0);
			this.newCameraPos.x += 400 * delta;
		}

		this.camera.position.lerp(this.newCameraPos, 0.2f);

		/*
		// Lower bounds (left and bottom)
		if (camera.position.x < (0 + (camera.viewportWidth/2)) - 128) {
			camera.position.x = (camera.viewportWidth/2) - 128;
			this.newCameraPos.x = (camera.viewportWidth/2) - 128;
		}
		if (camera.position.y < (0 + (camera.viewportHeight/2)) - 128) {
			camera.position.y = (camera.viewportHeight/2) - 128;
			this.newCameraPos.y = (camera.viewportHeight/2) - 128;
		}
		// Upper bounds (right and top)
		if (camera.position.x > ((64*this.mapWidth - camera.viewportWidth/2) + 128)){// && 64*this.mapWidth >= Main.WIDTH) {
			camera.position.x = (64*this.mapWidth - camera.viewportWidth/2) + 128;
			this.newCameraPos.x = (64*this.mapWidth - camera.viewportWidth/2) + 128;
		}
		if (camera.position.y > ((64*this.mapHeight - camera.viewportHeight/2) + 128)){// && 64*this.mapHeight >= Main.HEIGHT) {
			camera.position.y = (64*this.mapHeight - camera.viewportHeight/2) + 128;
			this.newCameraPos.y = (64*this.mapHeight - camera.viewportHeight/2) + 128;
		}
		 */

		//this.camera.position.x = Math.round((this.camera.position.x * 64) / 64);
		//this.camera.position.y = Math.round((this.camera.position.y * 64) / 64);
	}

	private void draw(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.mapRenderer.setView(this.camera);
		this.mapRenderer.render();

		this.batch.setProjectionMatrix(this.camera.combined);
		this.batch.begin();
		this.ec.draw(batch);
		this.tm.drawLOS(batch);
		this.tm.draw(batch);
		this.batch.end();

		this.stage.draw();
		this.hudStage.draw();

		this.tm.drawBox();
		//this.tm.drawMenuLines();
	}

	private void createStage() {
		this.stage = new Stage(new FitViewport(TowerDefense.WIDTH, TowerDefense.HEIGHT, this.camera));
		this.hudStage = new Stage(new FitViewport(TowerDefense.WIDTH, TowerDefense.HEIGHT));

		TextButtonStyle btnStyle = new TextButtonStyle();
		btnStyle.font = new BitmapFont(Gdx.files.internal("pixel.fnt"), false);
		btnStyle.up = Assets.skin.getDrawable("btn");

		this.gameOverImg = new Image(Assets.gameOver);
		this.gameOverImg.setPosition((this.stage.getWidth() / 2) - (this.gameOverImg.getWidth() / 2), this.stage.getHeight() - this.gameOverImg.getHeight());
		this.gameOverImg.setVisible(false);

		LabelStyle labelStyle = new LabelStyle(new BitmapFont(Gdx.files.internal("flag48.fnt"), false), Color.WHITE);

		this.nextWaveBtn = new TextButton("Next Wave", btnStyle);
		this.nextWaveBtn.setBounds(1610, 50, 300, 75);

		this.buyTowerBtn = new TextButton("Buy Tower", btnStyle);
		this.buyTowerBtn.setBounds(1610, 1080 - 150, 300, 75);

		this.waveLabel = new Label("[Wave|" + this.wave + "/" + this.finalWave + "]", labelStyle);
		this.waveLabel.setPosition((this.hudStage.getWidth() / 2) - (this.waveLabel.getWidth() / 2), this.hudStage.getHeight() - 50);

		this.healthLabel = new Label("[Health|" + this.levelHealth + "]", labelStyle);
		this.healthLabel.setPosition(this.waveLabel.getX() + this.waveLabel.getWidth() + 200, this.hudStage.getHeight() - 50);

		this.moneyLabel = new Label("[Money|" + 0 + "]", labelStyle);
		this.moneyLabel.setPosition(870 - this.moneyLabel.getWidth() - 200, this.hudStage.getHeight() - 50);

		NinePatchDrawable topUI = new NinePatchDrawable(new NinePatch(Assets.skin.getRegion("topUIback"), 5, 5, 0, 5));
		Image topUIback = new Image(topUI);
		topUIback.setBounds(0, TowerDefense.HEIGHT - 55, TowerDefense.WIDTH, 55);

		this.nextWaveBtn.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				nextWaveBtn.setColor(1, 0, 0, 1);
			}
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				nextWaveBtn.setColor(1, 1, 1, 1);
			}
			public void clicked(InputEvent e, float x, float y) {
				EnemyController.isActive = true;
				nextWave();
			}
		});

		this.buyTowerBtn.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				buyTowerBtn.setColor(1, 0, 0, 1);
			}
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				buyTowerBtn.setColor(1, 1, 1, 1);
			}
			public void clicked(InputEvent e, float x, float y) {
				TurretManager.showBuyWindow = true;
				//Gdx.input.setInputProcessor(hudStage);
			}
		});

		this.turretGroup = new Group();
		this.stage.addActor(this.turretGroup);
		this.hudStage.addActor(topUIback);
		this.hudStage.addActor(this.nextWaveBtn);
		this.hudStage.addActor(this.buyTowerBtn);
		this.hudStage.addActor(this.waveLabel);
		this.hudStage.addActor(this.healthLabel);
		this.hudStage.addActor(this.moneyLabel);
		this.hudStage.addActor(this.gameOverImg);
	}

	public void resize(int width, int height) {
		this.stage.getViewport().update(width, height, false);
		this.hudStage.getViewport().update(width, height, false);
	}

	public void dispose() {
		this.batch.dispose();
		this.stage.dispose();
		this.hudStage.dispose();
	}

	public void show() {}
	public void pause() {}
	public void resume() {}
	public void hide() {}
}
