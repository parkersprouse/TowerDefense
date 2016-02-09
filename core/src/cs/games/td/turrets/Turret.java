package cs.games.td.turrets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import cs.games.td.Assets;
import cs.games.td.Enemy;
import cs.games.td.TurretManager;

public abstract class Turret {

	protected TurretManager tm;
	
	protected Sprite sprite, base, highlighter;
	protected Vector2 position;
	protected float angle;
	protected float range;
	protected float timeSinceFired = 0;
	protected float timeBetweenFire;
	protected boolean firstFire = true;
	protected int damageDone;
	protected int level;
	protected Rectangle bounds;
	protected int currentPrice;
	private boolean highlight = false;
	private boolean drawLOS = false;
	private ShapeRenderer sr;
	
	// Target Management
	protected boolean hasTarget = false;
	protected Enemy target;
	
	// Personal Menu System
	public Window window;
	protected TextButton upgradeBtn, sellBtn, powerUpgradeBtn, speedUpgradeBtn;
	public Rectangle upgradeBounds, sellBounds, powerBounds, speedBounds;
	private Image upgradeLine, sellLine, powerLine, speedLine;
	
	public static enum TurretType {
		NONE,
		GUN,
		CANNON,
		LAZER
	}
	
	protected TurretType turretType;
	
	protected Turret(Vector2 pos, TurretType type, TurretManager tm) {
		this.position = pos;
		this.turretType = type;
		this.angle = 0;
		this.level = 1;
		this.bounds = new Rectangle(pos.x, pos.y, 64, 64);
		this.tm = tm;
		this.sr = new ShapeRenderer();
		//this.sr.setColor(0.4f, 0.3f, 0.3f, 0.4f);
		this.sr.setColor(0.4f, 0.4f, 0.4f, 0.4f);
		
		this.highlighter = Assets.atlas.createSprite("selectedTurretHighlight");
		this.highlighter.setPosition(this.position.x, this.position.y);
		
		createMenuSystem();
	}
	
	protected Turret(float x, float y, TurretType type, TurretManager tm) {
		this(new Vector2(x, y), type, tm);
	}
	
	public int getCurrentCost() {
		return this.currentPrice;
	}
	
	public Sprite getSprite() {
		return this.sprite;
	}
	
	public Vector2 getPosition() {
		return this.position;
	}
	
	public void setPosition(Vector2 pos) {
		this.position = pos;
		this.sprite.setPosition(this.position.x, this.position.y);
	}
	
	public void setPosition(float x, float y) {
		setPosition(new Vector2(x, y));
	}
	
	public Rectangle getBounds() {
		return this.bounds;
	}
	
	public float getAngle() {
		return this.angle;
	}
	
	/*
	 * Sets the angle that the turret should rotate
	 * @parameter ang - the float that holds the angle
	 */
	public void setAngle(float ang) {
		this.angle = ang;
		this.sprite.setRotation(this.angle);
	}
	
	/*
	 * Sets the target for this turret to attack
	 * @parameter e - the Enemy object to be set as the target
	 */
	public void setTarget(Enemy e) {
		if (e != null) {
			this.hasTarget = true;
			this.timeSinceFired = 0;
			this.target = e;
		}
	}
	
	public Enemy getTarget() {
		return this.target;
	}
	
	public boolean getHasTarget() {
		return this.hasTarget;
	}
	
	/*
	 * Look at the target provided using its position
	 * @parameter target - the Vector2 containing the position to look at
	 */
	public void lookAt(Vector2 target) {
		float tempAngle = (float)(Math.toDegrees(Math.atan2(target.x - position.x, -(target.y - position.y))) + 180.0f);
		setAngle(tempAngle);
	}
	
	/*
	 * Check if the target is in range
	 * @parameter target - the Vector2 that contains the position to determine if is range of this turret
	 */
	public boolean inRange(Vector2 target) {
		boolean inRange = false;
		
		if (Math.abs(target.dst(position)) <= this.range)
			inRange = true;
		
		return inRange;
	}
	
	public float getRange() {
		return this.range;
	}
	
	private void fire(Enemy e) {
		this.timeSinceFired += Gdx.graphics.getDeltaTime();
		
		if (this.timeSinceFired > this.timeBetweenFire || this.firstFire) {
			e.takeHit(this.damageDone);
			this.timeSinceFired -= this.timeBetweenFire;
			this.firstFire = false;
		}
	}
	
	public void update(float delta) {
		if (this.hasTarget && inRange(this.target.getPosition()) && this.target.getIsAlive() && !this.target.getIsEscaped()) {
			lookAt(this.target.getPosition());
			fire(this.target);
		}
		
		else if (this.hasTarget && (!inRange(this.target.getPosition()) || !this.target.getIsAlive() || this.target.getIsEscaped())) {
			this.hasTarget = false;
			this.target = null;
		}
	}
	
	public void draw(SpriteBatch batch) {
		if (this.base != null)
			this.base.draw(batch);
		this.sprite.draw(batch);
		
		if (this.highlight) {
			this.highlighter.draw(batch);
		}
	}
	
	public void drawLOS(Stage s) {
		this.sr.setProjectionMatrix(s.getCamera().combined);
		this.sr.begin(ShapeType.Filled);
		this.sr.identity();
		this.sr.circle(this.position.x + 32, this.position.y + 32, this.range);
		this.sr.end();
	}
	
	public void setDrawHighlight(boolean h) {
		this.highlight = h;
	}
	
	public boolean getDrawHighlight() {
		return this.highlight;
	}
	
	public void setDrawLOS(boolean d) {
		this.drawLOS = d;
	}
	
	public boolean getDrawLOS() {
		return this.drawLOS;
	}
	
	private void createMenuSystem() {
		Window.WindowStyle winStyle = new Window.WindowStyle(new BitmapFont(Gdx.files.internal("win.fnt"), false), Color.WHITE, Assets.skin.newDrawable("clearWinBG", Color.WHITE));
		
		this.window = new Window("", winStyle);
		this.window.setMovable(false);
		this.window.setModal(false);
		this.window.setLayoutEnabled(false);
		this.window.setBounds(this.position.x - 192, this.position.y - 192, 448, 448);
				
		NinePatchDrawable btnBackground = new NinePatchDrawable(new NinePatch(Assets.skin.getRegion("button"), 11, 11, 11, 11));
		TextButtonStyle btnStyle = new TextButtonStyle(btnBackground, btnBackground, btnBackground, Assets.menuFont);
		
		this.upgradeBtn = new TextButton("Upgrade", btnStyle);
		this.sellBtn = new TextButton("Sell", btnStyle);
		this.powerUpgradeBtn = new TextButton("Power", btnStyle);
		this.speedUpgradeBtn = new TextButton("Speed", btnStyle);
		
		this.upgradeBtn.setPosition((this.window.getWidth()/2) - (this.upgradeBtn.getWidth()/2), (this.position.y - this.window.getY()) + 128);
		this.sellBtn.setPosition((this.window.getWidth()/2) - (this.sellBtn.getWidth()/2), (this.position.y - this.window.getY()) - 64 - this.sellBtn.getHeight());
		this.powerUpgradeBtn.setPosition(this.upgradeBtn.getX() - this.powerUpgradeBtn.getWidth() - 32, this.upgradeBtn.getY() + 64);
		this.speedUpgradeBtn.setPosition(this.upgradeBtn.getX() + this.upgradeBtn.getWidth() + 32, this.upgradeBtn.getY() + 64);
		
		this.powerUpgradeBtn.setVisible(false);
		this.speedUpgradeBtn.setVisible(false);
		
		this.upgradeBounds = new Rectangle(this.window.getX() + this.upgradeBtn.getX(), this.window.getY() + this.upgradeBtn.getY(), this.upgradeBtn.getWidth(), this.upgradeBtn.getHeight());
		this.sellBounds = new Rectangle(this.window.getX() + this.sellBtn.getX(), this.window.getY() + this.sellBtn.getY(), this.sellBtn.getWidth(), this.sellBtn.getHeight());
		this.powerBounds = new Rectangle(this.window.getX() + this.powerUpgradeBtn.getX(), this.window.getY() + this.powerUpgradeBtn.getY(), this.powerUpgradeBtn.getWidth(), this.powerUpgradeBtn.getHeight());
		this.speedBounds = new Rectangle(this.window.getX() + this.speedUpgradeBtn.getX(), this.window.getY() + this.speedUpgradeBtn.getY(), this.speedUpgradeBtn.getWidth(), this.speedUpgradeBtn.getHeight());
		
		NinePatchDrawable menuLine = new NinePatchDrawable(new NinePatch(Assets.skin.getRegion("menuItemLine"), 1, 1, 1, 1));
		menuLine.getPatch().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.upgradeLine = new Image(menuLine);
		this.sellLine = new Image(menuLine);
		this.powerLine = new Image(menuLine);
		this.speedLine = new Image(menuLine);
		
		
		//////////////////////////////////////
		float height = Math.abs((this.position.y + 64) - (this.window.getY() + this.upgradeBtn.getY()));
		this.upgradeLine.setBounds(Math.abs((this.window.getX() - (this.position.x + 32))) - (this.upgradeLine.getWidth()/2), Math.abs(this.window.getY() - (this.position.y + 64)), 10, height);
		
		height = Math.abs((this.position.y) - (this.window.getY() + this.sellBtn.getY() + this.sellBtn.getHeight()));
		this.sellLine.setBounds(Math.abs((this.window.getX() - (this.position.x + 32))) - (this.sellLine.getWidth()/2), this.sellBtn.getY() + this.sellBtn.getHeight(), 10, height);
		
		this.powerLine.setPosition(this.upgradeBtn.getX(), this.upgradeBtn.getY() + this.upgradeBtn.getHeight() - 5);
		float angle = (float)(Math.toDegrees(Math.atan2((this.powerUpgradeBtn.getX() + this.powerUpgradeBtn.getWidth()) - (this.powerLine.getX() + (this.powerLine.getWidth()/2)), -(this.powerUpgradeBtn.getY() - this.powerLine.getY()))) + 180.0f);
		Container<Image> container = new Container<Image>(powerLine);
		container.fill();
		container.setSize(powerLine.getWidth(), powerLine.getHeight());
		container.setOrigin(container.getWidth() / 2, 0);
		container.setTransform(true);
		container.setPosition(powerLine.getX(), powerLine.getY());
		container.rotateBy(angle);
		
		this.speedLine.setPosition(this.upgradeBtn.getX() + (this.upgradeBtn.getWidth() - this.speedLine.getWidth()), this.upgradeBtn.getY() + this.upgradeBtn.getHeight() - 5);
		angle = (float)(Math.toDegrees(Math.atan2(this.speedUpgradeBtn.getX() - (this.speedLine.getX() + (this.speedLine.getWidth()/2)), -(this.speedUpgradeBtn.getY() - this.speedLine.getY()))) + 180.0f);
		Container<Image> container2 = new Container<Image>(speedLine);
		container2.fill();
		container2.setSize(speedLine.getWidth(), speedLine.getHeight());
		container2.setOrigin(container2.getWidth() / 2, 0);
		container2.setTransform(true);
		container2.setPosition(speedLine.getX(), speedLine.getY());
		container2.rotateBy(angle);
		
		this.powerLine.setVisible(false);
		this.speedLine.setVisible(false);
		//////////////////////////////////////
		
		this.upgradeBtn.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				System.out.println("upgraded");
				powerUpgradeBtn.setVisible(true);
				speedUpgradeBtn.setVisible(true);
				powerLine.setVisible(true);
				speedLine.setVisible(true);
				//levelUp();
				//window.setVisible(false);
			}
		});
		this.sellBtn.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				tm.sellTurret(Turret.this, currentPrice);
				highlight = false;
				window.remove();
				Gdx.input.setInputProcessor(null);
			}
		});
		
		this.window.add(upgradeLine);
		this.window.add(sellLine);
		this.window.add(container);
		this.window.add(container2);
		this.window.add(this.upgradeBtn);
		this.window.add(this.sellBtn);
		this.window.add(this.powerUpgradeBtn);
		this.window.add(this.speedUpgradeBtn);
		
		this.window.setVisible(false);
	}
	
	public void closeWindow() {
		this.window.setVisible(false);
		this.highlight = false;
		this.powerUpgradeBtn.setVisible(false);
		this.speedUpgradeBtn.setVisible(false);
		this.speedLine.setVisible(false);
		this.powerLine.setVisible(false);
	}
	
	
	// ABSTRACT METHODS MEANT TO BE OVERRIDDEN BY CHILDREN TURRETS
	
	/*
	 * Level up the current turret
	 */
	
	public abstract void levelUp();
	
}
