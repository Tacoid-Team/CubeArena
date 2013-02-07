package com.tacoid.cubearena.tiles;

import actors.Cube;
import actors.Cube.State;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.CubeArena;


public class RotLeftTile extends Tile {
	Texture rotLeftTexture;
	
	public RotLeftTile() {
		super();
		this.type = TileType.ROTATE_LEFT;
		rotLeftTexture = CubeArena.getInstance().manager.get("textures/rotleft-tile.png", Texture.class);
	}
	@Override
	public void renderTile(Matrix4 t, float delta) {
		Matrix4 transform = new Matrix4(t);
        shader.begin();
        {
	        shader.setUniformi("u_diffuse", 0);
	        rotLeftTexture.bind();
	        transform.translate(x, getZ() + 0.001f, -y);
			shader.setUniformMatrix("u_projView", transform);
			decal.render(shader, GL20.GL_TRIANGLES);
        }
		shader.end();
	}
	@Override
	public void react(Cube cube) {
		cube.setState(State.ROTATE_LEFT);
	}

}
