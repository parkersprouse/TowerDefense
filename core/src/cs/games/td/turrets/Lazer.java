package cs.games.td.turrets;

import com.badlogic.gdx.math.Vector2;

import cs.games.td.Assets;
import cs.games.td.TurretManager;

public class Lazer extends Turret {
	
	public static int price = 300;

	public Lazer(float x, float y, TurretManager tm) {
		this(new Vector2(x, y), tm);
	}
	
	public Lazer(Vector2 pos, TurretManager tm) {
		super(pos, TurretType.LAZER, tm);
		this.range = 200;
		this.base = Assets.atlas.createSprite("LazerBASE");
		this.base.setPosition(this.position.x, this.position.y);
		this.sprite = Assets.atlas.createSprite("LazerGUN");
		this.sprite.setPosition(this.position.x, this.position.y);
		this.timeBetweenFire = 4;
		this.damageDone = 30;
		this.currentPrice = price;
	}
	
	@Override
	public void levelUp() {
		if (this.level == 1) {
			this.level = 2;
			this.sprite = Assets.atlas.createSprite("lazer_level2");
			this.damageDone = 60;
			this.timeBetweenFire = 3;
		}
		else if (this.level == 2) {
			this.level = 3;
			this.sprite = Assets.atlas.createSprite("lazer_level3");
			this.damageDone = 90;
			this.timeBetweenFire = 2;
		}
	}
}