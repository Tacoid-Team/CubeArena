package com.tacoid.cubearena.tiles;


import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.CubeArena;
import com.tacoid.cubearena.actors.Cube;
import com.tacoid.cubearena.actors.Cube.State;

public class NoTile extends Tile {
	Texture noTileTexture;
	final boolean show = false;
	public NoTile() {
		super();
		this.isVisible = false;
		this.type = TileType.NO_TILE;
		noTileTexture = CubeArena.getInstance().manager.get("textures/no-tile.png", Texture.class);
		this.setState(TileState.IDLE);
	}

	@Override
	public void renderTile(Matrix4 t, float delta) {
		if(show) {
			Matrix4 transform = new Matrix4(t);
	        shader.begin();
	        {
		        shader.setUniformi("u_diffuse", 0);
		        noTileTexture.bind();
		        transform.translate(x, getZ() + 0.001f, -y);
				shader.setUniformMatrix("u_projView", transform);
				mesh.render(shader, GL20.GL_TRIANGLES);
	        }
			shader.end();
		}
	}

	@Override
	public void react(Cube cube) {
		cube.setState(State.FALLING);
	}
}
