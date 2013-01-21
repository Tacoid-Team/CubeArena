package com.tacoid.cubearena.tiles;

import actors.Cube;
import actors.Cube.State;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.CubeArena;
import com.tacoid.cubearena.Direction;


public class ChangeTile extends Tile {
	Texture changeTexture;
	
	public ChangeTile() {
		super();
		this.type = TileType.CHANGE_DIRECTION;
		CubeArena.getInstance().manager.get("textures/change-tile.png", Texture.class);
		changeTexture = CubeArena.getInstance().manager.get("textures/change-tile.png", Texture.class);
	}

	@Override
	public void render(Matrix4 t, float delta) {
		drawTileBase(t,delta);
		Matrix4 transform = new Matrix4(t);
        shader.begin();
        {
	        shader.setUniformi("u_diffuse", 0);
	        changeTexture.bind();
	        transform.rotate(0.0f, 1.0f, 0.0f, this.direction.toAngle());
	        transform.translate(x, 0.0f, -y);
			shader.setUniformMatrix("u_projView", transform);
			mesh.render(shader, GL20.GL_TRIANGLES);
        }
		shader.end();
	}

	@Override
	public void react(Cube cube) {
		switch(this.getDirection()) {
		case EAST:
			cube.setDirection(Direction.EAST);
			break;
		case NORTH:
			cube.setDirection(Direction.NORTH);
			break;
		case SOUTH:
			cube.setDirection(Direction.SOUTH);
			break;
		case WEST:
			cube.setDirection(Direction.WEST);
			break;
		}
		cube.setState(State.ROLLING);
	}
}
