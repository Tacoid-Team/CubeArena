package com.tacoid.cubearena.tiles;

import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.actors.Cube;
import com.tacoid.cubearena.actors.Cube.State;


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
