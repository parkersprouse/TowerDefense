package cs.games.td;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import cs.games.utils.MapGenerator;

public class Enemy {

	private Sprite sprite, healthBG, healthBar;
	private Vector2 position, targetPosition, velocity;
	private int health, maxHealth, value;
	private boolean isAlive, escaped;
	private Rectangle bounds;

	private Array<Vector2> pathPoints;

	public Enemy(Vector2 pos) {
		this(pos.x, pos.y);
	}

	public Enemy(float x, float y) {
		this.position = new Vector2(x, y);
		this.sprite = Assets.atlas.createSprite("enemy");
		this.sprite.setPosition(this.position.x, this.position.y);
		this.health = 100;
		this.maxHealth = 100;
		this.value = 50;
		this.isAlive = true;
		this.escaped = false;
		this.velocity = new Vector2(0, 0);
		this.pathPoints = new Array<Vector2>();
		this.bounds = new Rectangle(x, y, 64, 64);
		this.healthBG = Assets.atlas.createSprite("healthBG");
		this.healthBar = Assets.atlas.createSprite("healthBar");
		this.healthBG.setPosition(this.position.x, this.position.y + 70);
		this.healthBar.setPosition(this.healthBG.getX() + 5, this.healthBG.getY() + 5);
	}

	public Vector2 getVeloctity() {
		return this.velocity;
	}

	public void setVelocity(Vector2 v) {
		this.velocity = v;
	}

	public void setVelocity(float x, float y) {
		setVelocity(new Vector2(x, y));
	}

	public Vector2 getPosition() {
		return this.position;
	}

	public void setPosition(Vector2 pos) {
		this.position = pos;
		this.sprite.setPosition(this.position.x, this.position.y);
		this.healthBG.setPosition(this.position.x, this.position.y + 70);
		this.healthBar.setPosition(this.healthBG.getX() + 5, this.healthBG.getY() + 5);
		this.bounds.x = this.position.x;
		this.bounds.y = this.position.y;
	}

	public void setPosition(float x, float y) {
		setPosition(new Vector2(x, y));
	}

	public void setPathPoints(Array<Tile> points) {
		this.pathPoints.clear();
		Vector2 beginningPoint = null;
		for (Tile t : points) {
			pathPoints.add(new Vector2(t.getX(), t.getY()));
			if (MapGenerator.getStartPoint().getPosition().dst(t.getPosition()) == 64) {
				beginningPoint = t.getPosition();
			}
		}

		this.targetPosition = beginningPoint;
	}

	public void takeHit(int damage) {		
		this.health -= damage;
		float scale = (float)this.health / (float)this.maxHealth;
		this.healthBar.setSize(54 * scale, this.healthBar.getHeight());
		this.healthBar.setPosition(this.healthBG.getX() + 5, this.healthBG.getY() + 5);
		if (this.health <= 0) {
			this.isAlive = false;
		}
	}
	
	public void setValue(int v) {
		this.value = v;
	}
	
	public int getValue() {
		return this.value;
	}

	public int getHealth() {
		return this.health;
	}

	public boolean getIsAlive() {
		return this.isAlive;
	}

	public boolean getIsEscaped() {
		return this.escaped;
	}
	
	public Rectangle getBounds() {
		return this.bounds;
	}

	public void update(float delta) {
		updatePositionStandings();
		chasePoint(this.targetPosition, delta);
		this.velocity.nor();

		this.position.add(this.velocity);
		setPosition(this.position);

		if (Math.abs(MapGenerator.getEndPoint().getX() - this.position.x) <= 1 && Math.abs(MapGenerator.getEndPoint().getY() - this.position.y) <= 1) {
			escaped = true;
		}
	}

	public void draw(SpriteBatch batch) {
		this.sprite.draw(batch);
		this.healthBG.draw(batch);
		this.healthBar.draw(batch);
	}

	private void updatePositionStandings() {
		// Vector2 object used to contain the current location on the enemy
		Vector2 curPoint = null;

		// Go through the list of points on the path and remove the point that that the enemy is currently
		// Standing on so that it doesn't get checked in the pathfinding
		for (int i = 0; i < this.pathPoints.size; i++) {
			curPoint = this.pathPoints.get(i);
			if (curPoint.x == this.position.x && curPoint.y == this.position.y) {
				this.pathPoints.removeIndex(i);
				break;
			}
		}

		// Once the old points have been removed, go through the remaining points and figure out
		// Which point is next to be travelled to
		for (int i = 0; i < this.pathPoints.size; i++) {
			curPoint = this.pathPoints.get(i);
			if ((Math.abs(this.position.x - curPoint.x) == 64 && Math.abs(this.position.y - curPoint.y) == 0) || (Math.abs(this.position.x - curPoint.x) == 0 && Math.abs(this.position.y - curPoint.y) == 64)) {
				this.targetPosition = curPoint;
				break;
			}
		}
	}

	private void chasePoint(Vector2 point, float delta) {

		float xdif = Math.abs(this.position.x - point.x);
		float ydif = Math.abs(this.position.y - point.y);

		if (xdif > ydif) {
			setVelocity(2 * delta, 1 * delta);
		}

		if (ydif > xdif) {
			setVelocity(1 * delta, 2 * delta);
		}

		if (xdif == 0) {
			setVelocity(0 * delta, 3 * delta);
		}

		if (ydif == 0) {
			setVelocity(3 * delta, 0 * delta);
		}

		if (xdif == ydif) {
			setVelocity(1 * delta, 1 * delta);
		}

		if (this.position.x > point.x)
			this.velocity.x *= -1;

		if (this.position.y > point.y)
			this.velocity.y *= -1;

	}

}