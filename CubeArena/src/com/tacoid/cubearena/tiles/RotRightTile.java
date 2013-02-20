package com.tacoid.cubearena.tiles;


import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.CubeArena;
import com.tacoid.cubearena.actors.Cube;
import com.tacoid.cubearena.actors.Cube.State;


public class RotRightTile extends Tile {
	Texture rotRightTexture;
	float theta;
	static float current_theta = 0.0f;
	public RotRightTile() {
		super();
		theta = current_theta;
		this.type = TileType.ROTATE_RIGHT;
		rotRightTexture = CubeArena.getInstance().manager.get("textures/rotright-tile.png", Texture.class);
	}
	@Override
	public void renderTile(Matrix4 t, float delta) {
		Matrix4 transform = new Matrix4(t);
        shader.begin();
        {
	        shader.setUniformi("u_diffuse", 0);
	        rotRightTexture.bind();
	        transform.translate(x, getZ() + 0.001f, -y);
	        transform.rotate(0.0f, -1.0f, 0.0f, theta);
			shader.setUniformMatrix("u_projView", transform);
			decal.render(shader, GL20.GL_TRIANGLES);
			theta+=100*delta;
			current_theta = theta;
        }
		shader.end();
	}
	@Override
	public void react(Cube cube) {
		cube.setState(State.ROTATE_RIGHT);
	}

}
