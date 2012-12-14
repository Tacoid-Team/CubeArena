package com.tacoid.cubearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.tiled.TileSet;
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
	
	private LevelState state;
	
	public Tile[][] level; 
	public LevelData levelData;
	
	public Tile start;
	public Tile end;
	public Tile tp1;
	public Tile tp2;

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
	
	final Plane xzPlane = new Plane(new Vector3(0, 1, 0), 0);
	final Vector3 intersection = new Vector3();
	public Tile checkTileTouched(OrthographicCamera cam) {
		Tile touched = null;
		if(Gdx.input.justTouched()) {
			Ray pickRay = cam.getPickRay(Gdx.input.getX(), Gdx.input.getY());
			Intersector.intersectRayPlane(pickRay, xzPlane, intersection);
			int x = (int)(intersection.x+0.5);
			int z = (int)(0.5-intersection.z);
			if(x >= 0 && x < level.length && z >= 0 && z < level[0].length) {
				touched = level[x][z];
				level[x][z] = TileFactory.createNewTile(TileType.EMPTY, x, z);
			}
		}
		
		return touched;
	}
	
	public void initCube(Cube cube) {
		if(this.start != null) {
			cube.setX(start.getX());
			cube.setY(start.getY());
			cube.setState(State.IDLE);
		}
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

}
