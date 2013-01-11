package com.tacoid.cubearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.tacoid.cubearena.Cube.State;
import com.tacoid.cubearena.tiles.Tile;
import com.tacoid.cubearena.tiles.TileFactory;
import com.tacoid.cubearena.tiles.TileType;
import com.tacoid.cubearena.tiles.Tile.TileState;

public class Level implements Actor3d {
	
	public enum LevelState {
		BUSY,
		READY
	};
	
	/* Utilisé pour la détection du touché */
	final Plane xzPlane = new Plane(new Vector3(0, 1, 0), 0);
	final Vector3 intersection = new Vector3();
	
	/* Etat d'animation du niveau */
	private LevelState state;
	
	/* Etat courrant du niveau */
	public Tile[][] level; 
	
	/* Etat initial du niveau, pas sur que ce soit utile de le gardee, sauf si on veut pouvoir rapidement réinitialiser */
	public LevelData levelData;
	
	/* On conserve les références de certaines tiles, comme les End/Start, et les téléporteurs */
	private Tile start;
	private Tile end;
	private Tile tp1;
	private Tile tp2;
	
	private boolean touched;
	private int touchedX;
	private int touchedY;
	private Tile touchedTile;

	protected Tile getTouchedTile() {
		return touchedTile;
	}

	public Level(LevelData ld) {
		setState(LevelState.BUSY);
		levelData = ld;
		this.level = new Tile[levelData.dimX][levelData.dimY];
		start = null;
		end = null;
		tp1 = null;
		tp2 = null;
		for(int i=0; i<this.level.length; i++) {
			for(int j=0; j<this.level[i].length; j++) {
				this.level[i][j] = TileFactory.createNewTile(TileType.fromValue(levelData.data[i][j]), i, j);
				if(this.level[i][j].getType() == TileType.END) {
					end = this.level[i][j];
				} else if(this.level[i][j].getType() == TileType.START) {
					start = this.level[i][j];
				} else if(this.level[i][j].getType() == TileType.TELEPORT) {
					if(tp1 == null) {
						tp1 = this.level[i][j];
					} else {
						tp2 = this.level[i][j];
					}
				}
			}
		}
	}
	
	public void initCube(Cube cube) {
		if(this.start != null) {
			cube.setX(start.getX());
			cube.setY(start.getY());
			cube.setState(State.IDLE);
		}
	}
	
	@Override
	public void render(Matrix4 transform, float delta) {
		boolean busy = false;
		for(int i=0; i<level.length; i++) {
			for(int j=0; j<level[i].length; j++) {
				if(level[i][j].getState() != TileState.IDLE) {
					busy = true;
				}
				level[i][j].render(transform, delta);
			}
		}
		
		if(!busy) {
			state = LevelState.READY;
		}
	}

	public void checkTileTouched(OrthographicCamera cam) {
		if(Gdx.input.justTouched()) {
			Ray pickRay = cam.getPickRay(Gdx.input.getX(), Gdx.input.getY());
			Intersector.intersectRayPlane(pickRay, xzPlane, intersection);
			int x = (int)(intersection.x+0.5);
			int z = (int)(0.5-intersection.z);
			touchedTile = getTile(x,z);
			touchedX = x;
			touchedY = z;
			touched = true;
		}
	}
	
	public Tile getTile(int x, int y) {
		if(x >= 0 && x < level.length && y >= 0 && y < level[0].length) {
			return level[x][y];
		} else {
			return null;
		}
	}
	
	public void replaceTile(Tile oldTile, TileType newTileType) {
		int x = oldTile.getX();
		int y = oldTile.getY();
		
		/*XXX: C'est temporaire. En faisant ça, l'ancienne tile disparait instantanément, c'est très laid */
		level[x][y] = TileFactory.createNewTile(newTileType, x, y);
	}

	
	public Tile getStart() {
		return start;
	}

	public void setStart(Tile start) {
		this.start = start;
	}

	public Tile getEnd() {
		return end;
	}

	public void setEnd(Tile end) {
		this.end = end;
	}

	public Tile getTp1() {
		return tp1;
	}

	public void setTp1(Tile tp1) {
		this.tp1 = tp1;
	}

	public Tile getTp2() {
		return tp2;
	}

	public void setTp2(Tile tp2) {
		this.tp2 = tp2;
	}

	public LevelState getState() {
		return state;
	}

	public void setState(LevelState state) {
		this.state = state;
	}
	
	protected boolean isTouched() {
		return touched;
	}
	
	public void resetTouch() {
		touched = false;
	}
	
	protected int getTouchedX() {
		return touchedX;
	}
	
	protected int getTouchedY() {
		return touchedY;
	}


}
