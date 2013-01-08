package com.tacoid.cubearena.tiles;

import java.util.Random;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.Cube;
import com.tacoid.cubearena.CubeArena;
import com.tacoid.cubearena.Cube.State;


public class EmptyTile extends Tile {

	private final Texture emptyTexture;
	private final float color[];
	
	EmptyTile() {
		super();
		color = new float[4];
		color[0] = new Random().nextFloat()*0.1f + 0.75f; 
		color[1] = color[0];
		color[2] = color[0];
		color[3] = 1.0f;
		this.type = TileType.EMPTY;
		emptyTexture = CubeArena.getInstance().manager.get("textures/empty-tile.png", Texture.class);
	}

	public void render(Matrix4 t, float delta) {
		Matrix4 transform = new Matrix4(t);
		this.update(delta);
        colorShader.begin();
        {
	        colorShader.setUniform4fv("u_color", color, 0, 4);
	        transform.translate(x, this.getZ(), -y);
			colorShader.setUniformMatrix("u_projView", transform);
			mesh.render(shader, GL20.GL_TRIANGLES);
        }
		colorShader.end();
	}

	@Override
	public void react(Cube cube) {
		cube.setState(State.ROLLING);
	}
	
}
