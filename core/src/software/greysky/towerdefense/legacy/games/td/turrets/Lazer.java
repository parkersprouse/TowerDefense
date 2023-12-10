package software.greysky.towerdefense.legacy.games.td.turrets;

import com.badlogic.gdx.math.Vector2;

import software.greysky.towerdefense.legacy.games.td.Assets;
import software.greysky.towerdefense.legacy.games.td.TurretManager;

public class Lazer extends Turret {

	public static int price = 300;
  final int base_damage = 30;
  final int base_time_between_fire = 4;

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
		this.timeBetweenFire = base_time_between_fire;
		this.damageDone = base_damage;
		this.currentPrice = price;
	}

	// @Override
	// public void levelUp() {
	// 	if (this.level == 1) {
	// 		this.level = 2;
	// 		// this.sprite = Assets.atlas.createSprite("lazer_level2");
	// 		this.damageDone = 60;
	// 		this.timeBetweenFire = 3;
	// 	}
	// 	else if (this.level == 2) {
	// 		this.level = 3;
	// 		// this.sprite = Assets.atlas.createSprite("lazer_level3");
	// 		this.damageDone = 90;
	// 		this.timeBetweenFire = 2;
	// 	}
	// }

  @Override
  public void powerUpgraded() {
    if (this.powerLevel == 3) return;
    this.powerLevel += 1;
    this.damageDone = base_damage * powerLevel;
  }

  @Override
  public void speedUpgraded() {
    if (this.speedLevel == 3) return;
    this.timeBetweenFire = base_time_between_fire - speedLevel;
    this.speedLevel += 1;
  }
}
