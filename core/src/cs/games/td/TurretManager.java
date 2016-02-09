package cs.games.td;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;

import cs.games.td.turrets.Cannon;
import cs.games.td.turrets.Gun;
import cs.games.td.turrets.Lazer;
import cs.games.td.turrets.Turret;
import cs.games.utils.MapGenerator;

public class TurretManager {

	private Array<Turret> turrets, turretsToCommand;
	private Stage stage, primaryStage;
	private Group group;
	public Window window;
	private TextButton buyGun, buyCannon, buyLazer, cancelBuying;
	private Rectangle windowBounds, selectionBox;
	private ShapeRenderer sr;
	private EnemyController ec;
	private boolean startBox = true;
	private boolean drawBox = false;
	private boolean turretMenuClicked, buyMenuClicked, buyingTurret;
	private float cameraOriginalX, cameraOriginalY;

	private Rectangle tempBounds = new Rectangle();

	private Vector3 mousePos = new Vector3(0, 0, 0);
	
	public static boolean showBuyWindow = false;
	
	private enum TowerType {
		GUN,
		CANNON,
		LAZER
	}
	
	private TowerType tToBuy = null;

	@SuppressWarnings("serial")
	public TurretManager(Stage stage, Stage primaryStage, Group group, Player p, EnemyController ec) {
		this.turrets = new Array<Turret>();
		this.turretsToCommand = new Array<Turret>();
		this.stage = stage;
		this.primaryStage = primaryStage;
		this.group = group;
		this.ec = ec;
		this.windowBounds = new Rectangle();
		this.selectionBox = new Rectangle() {
			public boolean overlaps(Rectangle r) {
				float left = Math.min(x, x + width);
				float right = Math.max(x, x + width);
				float top = Math.min(y, y + height);
				float bottom = Math.max(y, y + height);
				float left2 = Math.min(r.x, r.x + r.width);
				float right2 = Math.max(r.x, r.x + r.width);
				float top2 = Math.min(r.y, r.y + r.height);
				float bottom2 = Math.max(r.y, r.y + r.height);

				return left < right2 && right > left2 && top < bottom2 && bottom > top2;
			}
		};

		this.cameraOriginalX = primaryStage.getCamera().position.x;
		this.cameraOriginalY = primaryStage.getCamera().position.y;

		this.sr = new ShapeRenderer();
		this.sr.setColor(Color.WHITE);

		createMenuSystem();
		this.stage.addActor(this.window);
	}

	public void addTurret(Turret e) {
		this.turrets.add(e);
	}

	public void removeTurret(Turret e) {
		this.turrets.removeValue(e, true);
	}

	public void buyTurret(Turret t, int cost) {
		if (Player.spendMoney(cost)) {
			addTurret(t);
			group.addActor(t.window);
			hideWindows();
			t.window.setVisible(true);
			turretsToCommand.add(t);
			t.setDrawHighlight(true);
			Gdx.input.setInputProcessor(this.primaryStage);
			drawBox = false;
		}
		buyingTurret = false;
		tToBuy = null;
	}

	public void sellTurret(Turret t, int cost) {
		this.turrets.removeValue(t, true);
		Player.earnMoney(cost);
	}

	public Array<Turret> getTurrets() {
		return this.turrets;
	}

	public Turret getTurret(int index) {
		return this.turrets.get(index);
	}

	public int getNumOfTurrets() {
		return this.turrets.size;
	}

	public Stage getStage() {
		return this.stage;
	}

	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	public void update(float delta) {
		if (!showBuyWindow)
			checkInput();
		else
			checkBuyWindowInput();

		//boolean reset = false;
		for (int i = 0; i < this.turrets.size; i++) {
			Turret t = this.turrets.get(i);
			t.update(delta);
			for (int j = 0; j < ec.getNumOfEnemies(); j++) {
				Enemy e = ec.getEnemy(j);
				if (!t.getHasTarget() && t.inRange(e.getPosition()) && e.getIsAlive() && !e.getIsEscaped()) {
					/*if (!reset) {
						reset = true;
						j = 0;
					}*/
					t.setTarget(e);
				}
			}
		}

		if (drawBox) {
			for (Turret t : turrets) {
				tempBounds.x = t.getBounds().x + (this.cameraOriginalX - this.primaryStage.getCamera().position.x);
				tempBounds.y = t.getBounds().y + (this.cameraOriginalY - this.primaryStage.getCamera().position.y);
				tempBounds.width = t.getBounds().width;
				tempBounds.height = t.getBounds().height;
				if (Intersector.intersectRectangles(this.selectionBox, tempBounds, new Rectangle()) && !turretsToCommand.contains(t, true)) {
					turretsToCommand.add(t);
					t.setDrawHighlight(true);
				}
				else if (!Intersector.intersectRectangles(this.selectionBox, tempBounds, new Rectangle()) && turretsToCommand.contains(t, true)) {
					turretsToCommand.removeValue(t, true);
					t.setDrawHighlight(false);
				}
			}
		}
		
		if (showBuyWindow && !this.window.isVisible())
			this.window.setVisible(true);
		else if (!showBuyWindow && this.window.isVisible())
			this.window.setVisible(false);
	}

	/*
	 * This is the input that is checked for while the buy window is open
	 * It is only used to close the window without buying anything
	 */
	private void checkBuyWindowInput() {
		if (Gdx.input.justTouched()) {
			
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				mousePos.x = Gdx.input.getX();
				mousePos.y = Gdx.input.getY();
				stage.getViewport().unproject(mousePos);
				
				if (!windowBounds.contains(mousePos.x, mousePos.y)) {
					showBuyWindow = false;
					Gdx.input.setInputProcessor(null);
				}
			}
			
			else if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
				showBuyWindow = false;
				Gdx.input.setInputProcessor(null);
			}
			
		}
	}
	
	/*
	 * Input checking for basic gameplay
	 * - Clicking on a turret to bring up its menu
	 * - Click and drag to create the selection box
	 * - Right clicking to close all windows
	 * - Hover over the turret to show its Line of Sight
	 * - Hold SPACE to show the Line of Sight for all turrets
	 */
	private void checkInput() {

		mousePos.x = Gdx.input.getX();
		mousePos.y = Gdx.input.getY();
		stage.getViewport().unproject(mousePos);

		if (this.windowBounds.x != 0 && this.windowBounds.y != 0) {
			this.windowBounds.x = this.window.getX() + (this.cameraOriginalX - this.primaryStage.getCamera().position.x);
			this.windowBounds.y = this.window.getY() + (this.cameraOriginalY - this.primaryStage.getCamera().position.y);
		}

		// If you mouse over a turret, show its LOS
		// If you press SPACE, show all turrets' LOS
		for (Turret t : this.turrets) {
			tempBounds.x = t.getBounds().x + (this.cameraOriginalX - this.primaryStage.getCamera().position.x);
			tempBounds.y = t.getBounds().y + (this.cameraOriginalY - this.primaryStage.getCamera().position.y);
			tempBounds.width = t.getBounds().width;
			tempBounds.height = t.getBounds().height;
			if (tempBounds.contains(mousePos.x, mousePos.y) || Gdx.input.isKeyPressed(Keys.SPACE)) {
				t.setDrawLOS(true);
			}
			else {
				t.setDrawLOS(false);
			}
		}

		if (!Gdx.input.isTouched()) {
			startBox = true;
			drawBox = false;
			this.selectionBox.x = 0;
			this.selectionBox.y = 0;
			this.selectionBox.width = 0;
			this.selectionBox.height = 0;
		}

		// While the left mouse button is being held down
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			if (startBox) {
				this.selectionBox.x = mousePos.x;
				this.selectionBox.y = mousePos.y;
				startBox = false;
			}
			if (Gdx.input.getDeltaX() != 0 || Gdx.input.getDeltaY() != 0) {
				this.selectionBox.width = mousePos.x - this.selectionBox.x;
				this.selectionBox.height = mousePos.y - this.selectionBox.y;
				cancelBuyingTurret();
			}
		}

		// If the mouse is just clicked, not held down
		if (Gdx.input.justTouched()) {

			// If the left mouse is clicked
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {

				drawBox = true;	

				// Used to determine if a turret menu clicked
				turretMenuClicked = false;
				for (Turret t : turrets) {
					for (int i = 0; i < 4; i++) {
						if (i == 0) {
							tempBounds.x = t.upgradeBounds.getX() + (this.cameraOriginalX - this.primaryStage.getCamera().position.x);
							tempBounds.y = t.upgradeBounds.getY() + (this.cameraOriginalY - this.primaryStage.getCamera().position.y);
							tempBounds.width = t.upgradeBounds.getWidth();
							tempBounds.height = t.upgradeBounds.getHeight();
						}
						else if (i == 1) {
							tempBounds.x = t.sellBounds.getX() + (this.cameraOriginalX - this.primaryStage.getCamera().position.x);
							tempBounds.y = t.sellBounds.getY() + (this.cameraOriginalY - this.primaryStage.getCamera().position.y);
							tempBounds.width = t.sellBounds.getWidth();
							tempBounds.height = t.sellBounds.getHeight();
						}
						else if (i == 2) {
							tempBounds.x = t.powerBounds.getX() + (this.cameraOriginalX - this.primaryStage.getCamera().position.x);
							tempBounds.y = t.powerBounds.getY() + (this.cameraOriginalY - this.primaryStage.getCamera().position.y);
							tempBounds.width = t.powerBounds.getWidth();
							tempBounds.height = t.powerBounds.getHeight();
						}
						else if (i == 3) {
							tempBounds.x = t.speedBounds.getX() + (this.cameraOriginalX - this.primaryStage.getCamera().position.x);
							tempBounds.y = t.speedBounds.getY() + (this.cameraOriginalY - this.primaryStage.getCamera().position.y);
							tempBounds.width = t.speedBounds.getWidth();
							tempBounds.height = t.speedBounds.getHeight();
						}

						if (tempBounds.contains(mousePos.x, mousePos.y) && t.window.isVisible()) {
							turretMenuClicked = true;
							drawBox = false;
							break;
						}
					}
					
					if (turretMenuClicked) break;
				}

				buyMenuClicked = false;
				if (this.windowBounds.contains(mousePos.x, mousePos.y) && this.window.isVisible()) {
					buyMenuClicked = true;
					drawBox = false;
					this.turretsToCommand.clear();
				}

				// If the user doesn't click on the window, hide the windows
				if (!this.windowBounds.contains(mousePos.x, mousePos.y) && !turretMenuClicked) {
					hideWindows();
					this.turretsToCommand.clear();
				}

				if (!turretMenuClicked && !buyMenuClicked) {
					hideWindows();
					// Check all the turrets in existence. If there is one where we clicked,
					// then we open the menu for that turret
					for (int i = 0; i < turrets.size; i++) {
						Turret t = turrets.get(i);
						tempBounds.x = t.getBounds().x + (this.cameraOriginalX - this.primaryStage.getCamera().position.x);
						tempBounds.y = t.getBounds().y + (this.cameraOriginalY - this.primaryStage.getCamera().position.y);
						tempBounds.width = t.getBounds().width;
						tempBounds.height = t.getBounds().height;
						if (tempBounds.contains(mousePos.x, mousePos.y)) {
							cancelBuyingTurret();
							t.window.setVisible(true);
							turretsToCommand.add(t);
							t.setDrawHighlight(true);
							drawBox = false;
							Gdx.input.setInputProcessor(this.primaryStage);
							break;
						}
					}
					
					if (buyingTurret) {
						boolean canBuy = true;
						for (Turret t : this.turrets) {
							tempBounds.x = t.getBounds().x + (this.cameraOriginalX - this.primaryStage.getCamera().position.x);
							tempBounds.y = t.getBounds().y + (this.cameraOriginalY - this.primaryStage.getCamera().position.y);
							tempBounds.width = t.getBounds().width;
							tempBounds.height = t.getBounds().height;
							if (tempBounds.contains(mousePos.x, mousePos.y)) {
								canBuy = false;
								cancelBuyingTurret();
							}
						}

						if (canBuy) {
							for (Tile t : MapGenerator.getTurretSpots()) {
								tempBounds.x = t.getBounds().x + (this.cameraOriginalX - this.primaryStage.getCamera().position.x);
								tempBounds.y = t.getBounds().y + (this.cameraOriginalY - this.primaryStage.getCamera().position.y);
								tempBounds.width = t.getBounds().width;
								tempBounds.height = t.getBounds().height;
								if (tempBounds.contains(mousePos.x, mousePos.y)) {
									switch(this.tToBuy) {
									case GUN:
										buyTurret(new Gun(t.getX(), t.getY(), TurretManager.this), Gun.price);
										break;
									case CANNON:
										buyTurret(new Cannon(t.getX(), t.getY(), TurretManager.this), Cannon.price);
										break;
									case LAZER:
										buyTurret(new Lazer(t.getX(), t.getY(), TurretManager.this), Lazer.price);
										break;
									}
								}
							}
						}
					}
				}
			}

			if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
				boolean enemyClicked = false;
				if (this.turretsToCommand.size > 0) {
					for (Enemy e : ec.getEnemies()) {
						tempBounds.x = e.getBounds().x + (this.cameraOriginalX - this.primaryStage.getCamera().position.x);
						tempBounds.y = e.getBounds().y + (this.cameraOriginalY - this.primaryStage.getCamera().position.y);
						tempBounds.width = e.getBounds().width;
						tempBounds.height = e.getBounds().height;
						if (tempBounds.contains(mousePos.x, mousePos.y)) {
							for (int j = 0; j < turretsToCommand.size; j++) {
								turretsToCommand.get(j).setTarget(e);
								enemyClicked = true;
							}
						}
					}
				}

				if (!enemyClicked) {
					hideWindows();
					cancelBuyingTurret();
				}

				// Uncomment this to lose command of turrets once their target has been set
				//turretsToCommand.clear();
			}
		}
	}
	
	private void cancelBuyingTurret() {
		this.buyingTurret = false;
		this.tToBuy = null;
	}

	private void hideWindows() {
		for (Turret t : turrets) {
			t.closeWindow();
		}
		this.window.setVisible(false);
		Gdx.input.setInputProcessor(null);
	}

	private void createMenuSystem() {
		
		NinePatchDrawable winBG = new NinePatchDrawable(new NinePatch(Assets.skin.getRegion("buyMenuBG"), 11, 11, 11, 11));
		Window.WindowStyle winStyle = new Window.WindowStyle(Assets.menuFont, Color.WHITE, winBG);
		this.window = new Window("", winStyle);
		this.window.setMovable(false);
		this.window.setModal(false);
		this.window.setLayoutEnabled(false);
		this.window.setBounds((Main.WIDTH/2)-(600/2), (Main.HEIGHT/2)-(600/2), 600, 600);
		this.windowBounds.set(this.window.getX(), this.window.getY(), this.window.getWidth(), this.window.getHeight());

		TextButtonStyle btnStyle = new TextButtonStyle();
		btnStyle.font = Assets.menuFont;
		this.buyGun = new TextButton("Buy Gun", btnStyle);
		this.buyCannon = new TextButton("Buy Cannon", btnStyle);
		this.buyLazer = new TextButton("Buy Lazer", btnStyle);
		this.cancelBuying = new TextButton("Cancel", btnStyle);

		this.buyGun.setPosition(10, this.window.getHeight() - this.buyGun.getHeight() - 10);
		this.buyCannon.setPosition(10, this.buyGun.getY() - 20);
		this.buyLazer.setPosition(10, this.buyCannon.getY() - 20);
		this.cancelBuying.setPosition(this.window.getWidth() - this.cancelBuying.getWidth() - 30, 30);

		this.buyGun.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				buyingTurret = true;
				tToBuy = TowerType.GUN;
				showBuyWindow = false;
				Gdx.input.setInputProcessor(null);
			}
		});
		this.buyCannon.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				buyingTurret = true;
				tToBuy = TowerType.CANNON;
				showBuyWindow = false;
				Gdx.input.setInputProcessor(null);
			}
		});
		this.buyLazer.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				buyingTurret = true;
				tToBuy = TowerType.LAZER;
				showBuyWindow = false;
				Gdx.input.setInputProcessor(null);
			}
		});
		this.cancelBuying.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				showBuyWindow = false;
				Gdx.input.setInputProcessor(null);
			}
		});

		this.window.add(this.buyGun);
		this.window.add(this.buyCannon);
		this.window.add(this.buyLazer);
		this.window.add(this.cancelBuying);

		this.window.setVisible(false);
	}

	public void draw(SpriteBatch batch) {
		for (Turret t : turrets) {
			t.draw(batch);
		}
	}

	public void drawBox() {
		this.sr.setProjectionMatrix(stage.getCamera().combined);
		this.sr.begin(ShapeType.Line);
		this.sr.identity();

		this.sr.setColor(Color.WHITE);
		if (drawBox) {
			this.sr.rect(this.selectionBox.x, this.selectionBox.y, this.selectionBox.width, this.selectionBox.height);
		}

		this.sr.setColor(Color.YELLOW);
		if (buyingTurret) {
			for (Tile t : MapGenerator.getTurretSpots()) {
				tempBounds.x = t.getBounds().x + (this.cameraOriginalX - this.primaryStage.getCamera().position.x);
				tempBounds.y = t.getBounds().y + (this.cameraOriginalY - this.primaryStage.getCamera().position.y);
				tempBounds.width = t.getBounds().width;
				tempBounds.height = t.getBounds().height;
				if (tempBounds.contains(mousePos.x, mousePos.y)) {
					this.sr.rect(t.getPosition().x + (this.cameraOriginalX - this.primaryStage.getCamera().position.x), t.getPosition().y + (this.cameraOriginalY - this.primaryStage.getCamera().position.y), 64, 64);
				}
			}
		}
		
		this.sr.end();
	}

	public void drawLOS(SpriteBatch batch) {
		batch.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		for (Turret t : this.turrets) {
			if (t.getDrawLOS()) t.drawLOS(primaryStage);
		}
		Gdx.gl.glDisable(GL20.GL_BLEND);
		batch.begin();
	}
}