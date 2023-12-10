package software.greysky.towerdefense.legacy.games.td.turrets;

import com.badlogic.gdx.math.Vector2;

import software.greysky.towerdefense.legacy.games.td.Assets;
import software.greysky.towerdefense.legacy.games.td.TurretManager;

public class Gun extends Turret {

	public static int price = 100;
  final int base_damage = 10;
  final int base_time_between_fire = 1;

	public Gun(float x, float y, TurretManager tm) {
		this(new Vector2(x, y), tm);
	}

	public Gun(Vector2 pos, TurretManager tm) {
		super(pos, TurretType.GUN, tm);
		this.range = 200;
		//this.base = Assets.turretAtlas.createSprite("GunBASE");
		//this.base.setPosition(this.position.x, this.position.y);
		//this.base.setSize(64, 64);
		this.sprite = Assets.atlas.createSprite("gun_level1");
		//this.sprite = Assets.turretAtlas.createSprite("GunGUN");
		this.sprite.setPosition(this.position.x, this.position.y);
		//this.sprite.setSize(64, 64);
		this.timeBetweenFire = base_time_between_fire;
		this.damageDone = base_damage;
		this.currentPrice = price;
	}

	// @Override
	// public void levelUp() {
	// 	if (this.level == 1) {
	// 		this.level = 2;
	// 		// this.sprite = Assets.atlas.createSprite("gun_level2");
	// 		this.damageDone = 20;
	// 		this.timeBetweenFire = 1;
	// 	}
	// 	else if (this.level == 2) {
	// 		this.level = 3;
	// 		// this.sprite = Assets.atlas.createSprite("gun_level3");
	// 		this.damageDone = 30;
	// 		this.timeBetweenFire = 0.5f;
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
    this.speedLevel += 1;
    this.timeBetweenFire = this.timeBetweenFire / 2;
  }

}
