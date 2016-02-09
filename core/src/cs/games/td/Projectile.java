package cs.games.td;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
	
	private float angle;
	private float speed;
	private Vector2 position;
	private Sprite sprite;
	public Rectangle collider;
	public static enum ProjectileType {
		BULLET,
		CANNON,
		LAZER
	}
	private ProjectileType type;
	
	public Projectile(float x, float y) {
		this(new Vector2(x, y), ProjectileType.BULLET);
	}
	
	public Projectile(Vector2 pos) {
		this(pos, ProjectileType.BULLET);
	}
	
	public Projectile(float x, float y, ProjectileType t) {
		this(new Vector2(x, y), t);
	}
	
	public Projectile(Vector2 pos, ProjectileType t) {
		this.position = pos;
		this.type = t;
		
		if (t == ProjectileType.BULLET)
			this.sprite = Assets.atlas.createSprite("bullet");
		else if (t == ProjectileType.CANNON)
			this.sprite = Assets.atlas.createSprite("cannonball");
		else if (t == ProjectileType.LAZER)
			this.sprite = Assets.atlas.createSprite("lazerbeam");
	}
	
	public Sprite getSprite() {
		return this.sprite;
	}
	
	public float getAngle() {
		return this.angle;
	}
	
	public float getSpeed() {
		return this.speed;
	}
	
	public Vector2 getPosition() {
		return this.position;
	}
	
	public void setSpeed(float s) {
		this.speed = s;
	}
	
	public void setAngle(float a) {
		this.angle = a;
	}
	
	public void setPosition(float x, float y) {
		setPosition(new Vector2(x, y));
	}
	
	public void setPosition(Vector2 pos) {
		this.position = pos;
	}
	
	public ProjectileType getType() {
		return this.type;
	}

}
