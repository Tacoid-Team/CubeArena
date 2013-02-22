package com.tacoid.cubearena;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.tacoid.cubearena.actors.Cube;
import com.tacoid.cubearena.actors.Cube.State;
import com.tacoid.cubearena.tiles.TeleportTile;
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
	private Tile[] tp;
	
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
		tp = new Tile[2];
		for(int i=0; i<this.level.length; i++) {
			for(int j=0; j<this.level[i].length; j++) {
				this.level[i][j] = TileFactory.createNewTile(TileType.fromValue(levelData.data[i][j]), i, j);
				if(this.level[i][j].getType() == TileType.END) {
					end = this.level[i][j];
				} else if(this.level[i][j].getType() == TileType.START) {
					start = this.level[i][j];
				} else if(this.level[i][j].getType() == TileType.TELEPORT) {
					addTeleport(this.level[i][j]);
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
		float bias = 50.0f;
		
		Gdx.graphics.getGL20().glPolygonOffset(bias,bias);
		for(int i=0; i<level.length; i++) {
			for(int j=0; j<level[i].length; j++) {
				if(level[i][j].getState() != TileState.IDLE) {
					busy = true;
				}
				level[i][j].render(transform, delta);
			}
		}
		//Gdx.graphics.getGL20().glDisable(GL20.GL_DEPTH_TEST);
		
		
		for(int i=0; i<level.length; i++) {
			
			for(int j=0; j<level[i].length; j++) {
				if(level[i][j].getState() != TileState.IDLE) {
					busy = true;
				}
				Gdx.graphics.getGL20().glPolygonOffset(bias,bias);
				level[i][j].renderTile(transform, delta);
				bias -= 2.0f;
			}
			
		}
		//Gdx.graphics.getGL20().glEnable(GL20.GL_DEPTH_TEST);
		
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
	
	public Tile replaceTile(Tile oldTile, TileType newTileType) {
		int x = oldTile.getX();
		int y = oldTile.getY();
		
		if(oldTile.getType() == TileType.TELEPORT) {
			 removeTeleport(oldTile);
		}
		
		level[x][y] = TileFactory.createNewTile(newTileType, x, y);

		if(newTileType == TileType.TELEPORT) {
			addTeleport(level[x][y]);
		}
		return level[x][y];
	}
	
	public void addTeleport(Tile t) {
		if(t instanceof TeleportTile) {
			TeleportTile tpTile = (TeleportTile) t;
			if(tp[0] == null) {
				tp[0] = tpTile;
				tpTile.setId(0);
			} else {
				tp[1] = tpTile;
				tpTile.setId(1);
			}
		}
	}
	
	public void removeTeleport(Tile t) {
		if(t instanceof TeleportTile) {
			TeleportTile tpTile = (TeleportTile) t;
			tp[tpTile.getId()] = null;
		}
	}
	
	public Tile getTeleport(int id) {
		return tp[id];
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
