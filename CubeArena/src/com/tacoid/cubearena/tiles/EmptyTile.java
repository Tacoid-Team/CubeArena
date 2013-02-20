package com.tacoid.cubearena.tiles;

import actors.Cube;
import actors.Cube.State;
import com.badlogic.gdx.math.Matrix4;


public class EmptyTile extends Tile {

	
	
	EmptyTile() {
		super();

		this.type = TileType.EMPTY;
	}

	public void renderTile(Matrix4 t, float delta) {
	}

	@Override
	public void react(Cube cube) {
		cube.setState(State.ROLLING);
	}
	
}
