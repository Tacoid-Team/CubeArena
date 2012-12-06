package com.tacoid.cubearena.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.Cube;
import com.tacoid.cubearena.Cube.State;


public class RotRightTile extends Tile {
	Texture rotRightTexture;
	
	public RotRightTile() {
		super();
		this.type = TileType.ROTATE_RIGHT;
		rotRightTexture = new Texture(Gdx.files.internal("textures/rotright-tile.png"), Format.RGBA4444, true);
	}
	@Override
	public void render(Matrix4 t, float delta) {
		Matrix4 transform = new Matrix4(t);
        shader.begin();
        {
	        shader.setUniformi("u_diffuse", 0);
	        rotRightTexture.bind();
	        transform.translate(x, 0.0f, -y);
			shader.setUniformMatrix("u_projView", transform);
			mesh.render(shader, GL20.GL_TRIANGLES);
        }
		shader.end();
	}
	@Override
	public void react(Cube cube) {
		cube.setState(State.ROTATE_RIGHT);
	}

}
