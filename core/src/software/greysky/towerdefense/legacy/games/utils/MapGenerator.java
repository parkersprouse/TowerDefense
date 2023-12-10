package software.greysky.towerdefense.legacy.games.utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

import software.greysky.towerdefense.legacy.games.td.Tile;

public class MapGenerator {

	private static Array<Tile> turretSpots, pathSpots;
	private static Tile startPoint, endPoint;
	private static TiledMapTileLayer baseLayer, turretLayer, pathLayer, startPointLayer, endPointLayer;

	public static void generateMap(String file) {
		TiledMap map = new TmxMapLoader().load(file);

		baseLayer = (TiledMapTileLayer)map.getLayers().get("Tile Layer 1");
		pathLayer = (TiledMapTileLayer)map.getLayers().get("path");
		turretLayer = (TiledMapTileLayer)map.getLayers().get("turrets");
		startPointLayer = (TiledMapTileLayer)map.getLayers().get("startpoint");
		endPointLayer = (TiledMapTileLayer)map.getLayers().get("endpoint");

		// Find the spots that turrets can be placed on
		turretSpots = new Array<Tile>();
		int width = turretLayer.getWidth();
		int height = turretLayer.getHeight();
		Cell c = null;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				c = turretLayer.getCell(i, j);
				if (c != null) {
					turretSpots.add(new Tile(i * 64, j * 64));
				}
			}
		}

		// Find the spots the enemies can travel on
		pathSpots = new Array<Tile>();
		width = pathLayer.getWidth();
		height = pathLayer.getHeight();
		c = null;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				c = pathLayer.getCell(i, j);
				if (c != null) {
					pathSpots.add(new Tile(i * 64, j * 64));
				}
			}
		}

		// Start point
		width = startPointLayer.getWidth();
		height = startPointLayer.getHeight();
		c = null;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				c = startPointLayer.getCell(i, j);
				if (c != null) {
					startPoint = new Tile(i * 64, j * 64);
				}
			}
		}

		// End point
		width = endPointLayer.getWidth();
		height = endPointLayer.getHeight();
		c = null;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				c = endPointLayer.getCell(i, j);
				if (c != null) {
					endPoint = new Tile(i * 64, j * 64);
				}
			}
		}
	}

	public static TiledMapTileLayer getBaseLayer() {
		return baseLayer;
	}

	public static Array<Tile> getTurretSpots() {
		return turretSpots;
	}

	public static TiledMapTileLayer getTurretLayer() {
		return turretLayer;
	}

	public static Array<Tile> getPathSpots() {
		return pathSpots;
	}

	public static TiledMapTileLayer getPathLayer() {
		return pathLayer;
	}

	public static Tile getStartPoint() {
		return startPoint;
	}

	public static Tile getEndPoint() {
		return endPoint;
	}

}
