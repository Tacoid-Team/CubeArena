package com.tacoid.cubearena.tiles;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.Cube;
import com.tacoid.cubearena.CubeArena;
import com.tacoid.cubearena.Cube.State;


public class EmptyTile extends Tile {

	private final Texture emptyTexture;
	
	EmptyTile() {
		super();
		this.type = TileType.EMPTY;
		emptyTexture = CubeArena.getInstance().manager.get("textures/empty-tile.png", Texture.class);
	}

	public void render(Matrix4 t, float delta) {
		Matrix4 transform = new Matrix4(t);
		this.update(delta);
        shader.begin();
        {
	        shader.setUniformi("u_diffuse", 0);
	        emptyTexture.bind();
	        transform.translate(x, this.getZ(), -y);
			shader.setUniformMatrix("u_projView", transform);
			mesh.render(shader, GL20.GL_TRIANGLES);
        }
		shader.end();
	}

	@Override
	public void react(Cube cube) {
		cube.setState(State.ROLLING);
	}
	
}
