package software.greysky.towerdefense.legacy.games.td;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import software.greysky.towerdefense.legacy.games.td.screens.Level;
import software.greysky.towerdefense.legacy.games.utils.MapGenerator;

public class EnemyController {

	private Array<Enemy> enemies;
	private float counter;
	private int numEnemiesSpawned;
	private Level level;
	public static boolean isActive = false;
	public static int numEnemiesGone = 0;
	private float startX, startY;

	private Enemy e = null;

	public EnemyController(Level l) {
		this.enemies = new Array<Enemy>();
		this.numEnemiesSpawned = 0;
		this.level = l;
		this.startX = MapGenerator.getStartPoint().getX();
		this.startY = MapGenerator.getStartPoint().getY();
	}

	public void addEnemy(Enemy e) {
		e.setPathPoints(MapGenerator.getPathSpots());
		this.enemies.add(e);
	}

	public void removeEnemy(Enemy e) {
		this.enemies.removeValue(e, true);
	}

	public Array<Enemy> getEnemies() {
		return this.enemies;
	}

	public Enemy getEnemy(int index) {
		return this.enemies.get(index);
	}

	public int getNumOfEnemies() {
		return this.enemies.size;
	}

	public void update(float delta) {

		for (int i = 0; i < getNumOfEnemies(); i++) {
			e = getEnemy(i);
			e.update(delta);
			if (!e.getIsAlive() || e.getIsEscaped()) {
				if (!e.getIsAlive()) {
					Player.earnMoney(e.getValue());
				}
				removeEnemy(e);
				numEnemiesGone++;
				if (e.getIsEscaped())
					this.level.takeHit();
			}
		}

		if (isActive) {
			counter += delta;
			if (counter > this.level.getTimeBetweenEnemies()) {
				counter = 0;
				addEnemy(new Enemy(this.startX, this.startY));
				this.numEnemiesSpawned++;
				if (this.numEnemiesSpawned == this.level.getNumEnemies()) {
					isActive = false;
					this.numEnemiesSpawned = 0;
				}
			}
		}
	}

	public void draw(SpriteBatch batch) {
		for (int i = 0; i < this.enemies.size; i++) {
			this.enemies.get(i).draw(batch);
		}
	}

}
