package software.greysky.towerdefense.legacy.games.td.turrets;

import com.badlogic.gdx.math.Vector2;

import software.greysky.towerdefense.legacy.games.td.Assets;
import software.greysky.towerdefense.legacy.games.td.TurretManager;

public class Cannon extends Turret {

	public static int price = 200;
  final int base_damage = 20;
  final int base_time_between_fire = 3;

	public Cannon(float x, float y, TurretManager tm) {
		this(new Vector2(x, y), tm);
	}

	public Cannon(Vector2 pos, TurretManager tm) {
		super(pos, TurretType.CANNON, tm);
		this.range = 200;
		this.base = Assets.atlas.createSprite("CannonrBASE");
		this.base.setPosition(this.position.x, this.position.y);
		this.sprite = Assets.atlas.createSprite("CannonrGUN");
		this.sprite.setPosition(this.position.x, this.position.y);
		this.timeBetweenFire = base_time_between_fire;
		this.damageDone = base_damage;
		this.currentPrice = price;
	}

	// @Override
	// public void levelUp() {
	// 	if (this.level == 1) {
	// 		this.level = 2;
	// 		// this.sprite = Assets.atlas.createSprite("cannon_level2");
	// 		this.damageDone = 40;
	// 		this.timeBetweenFire = 2;
	// 	}
	// 	else if (this.level == 2) {
	// 		this.level = 3;
	// 		// this.sprite = Assets.atlas.createSprite("cannon_level3");
	// 		this.damageDone = 60;
	// 		this.timeBetweenFire = 1;
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
