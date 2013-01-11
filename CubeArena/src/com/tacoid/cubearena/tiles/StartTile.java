package com.tacoid.cubearena.tiles;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.Cube;
import com.tacoid.cubearena.CubeArena;
import com.tacoid.cubearena.Cube.State;


public class StartTile extends Tile {
	Texture startTexture;
	
	public StartTile() {
		super();
		this.type = TileType.START;
		startTexture = CubeArena.getInstance().manager.get("textures/start-tile.png", Texture.class);
	}
	@Override
	public void render(Matrix4 t, float delta) {
		drawTileBase(t,delta);
		Matrix4 transform = new Matrix4(t);
		this.update(delta);
        shader.begin();
        {
	        shader.setUniformi("u_diffuse", 0);
	        startTexture.bind();
	        transform.translate(x, this.getZ(), -y);
	        transform.rotate(0.0f, 1.0f, 0.0f, this.direction.toAngle());
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
