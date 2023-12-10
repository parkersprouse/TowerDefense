package software.greysky.towerdefense.legacy.games.td.levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import software.greysky.towerdefense.legacy.games.td.EnemyController;
import software.greysky.towerdefense.legacy.games.td.TurretManager;
import software.greysky.towerdefense.legacy.games.td.screens.Level;
import software.greysky.towerdefense.legacy.games.td.screens.MainMenu;
import software.greysky.towerdefense.legacy.games.utils.MapGenerator;

public class LevelOne extends Level {

	public LevelOne(Game game) {
		super(game);
		this.mapFile = "map2.tmx";
		this.map = new TmxMapLoader().load(mapFile);
		this.mapRenderer = new OrthogonalTiledMapRenderer(map);

		MapGenerator.generateMap(this.mapFile);

		this.mapWidth = MapGenerator.getBaseLayer().getWidth();
		this.mapHeight = MapGenerator.getBaseLayer().getHeight();
		this.wave = 0;
		this.levelHealth = 1;
		this.finalWave = 10;
		this.numEnemies = 8;
		this.timeBetweenEnemies = 3;
		this.ec = new EnemyController(this);
		this.tm = new TurretManager(this.hudStage, this.stage, this.turretGroup, this.player, this.ec);
		this.waveLabel.setText("[Wave|" + wave + "/" + finalWave + "]");
		this.healthLabel.setText("[Health|" + this.levelHealth + "]");
	}

	public void nextWave() {
		this.wave++;
		waveLabel.setText("[Wave|" + wave + "/" + finalWave + "]");
		if (this.wave % 2 == 0) this.numEnemies += 3;
		else this.numEnemies += 2;
		EnemyController.numEnemiesGone = 0;
	}

	public void takeHit() {
		this.levelHealth--;
		this.healthLabel.setText("[Health|" + this.levelHealth + "]");
		if (this.levelHealth == 0) {
			System.out.println("ded");
		}
	}

	public void gameOver() {
		System.out.println("game over");
		this.nextWaveBtn.setVisible(false);
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.setScreen(new MainMenu(game));
		}
	}

	public void levelComplete() {
		System.out.println("level complete");
		this.nextWaveBtn.setVisible(false);
	}

}
