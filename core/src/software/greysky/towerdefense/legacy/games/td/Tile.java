package software.greysky.towerdefense.legacy.games.td;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Tile {

	private Rectangle bounds;
	private float x, y;
	private Vector2 position;

	public Tile(float x, float y) {
		this.x = x;
		this.y = y;
		this.position = new Vector2(x, y);
		this.bounds = new Rectangle(x, y, 64, 64);
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public Rectangle getBounds() {
		return this.bounds;
	}

	public Vector2 getPosition() {
		return this.position;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		this.position = new Vector2(x, y);
		this.bounds = new Rectangle(x, y, 64, 64);
	}
}
