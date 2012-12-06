package com.tacoid.cubearena.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.Cube;


public class PushTile extends Tile {
	Texture pushTexture;
	
	public PushTile() {
		super();
		this.type = TileType.PUSH;
		pushTexture = new Texture(Gdx.files.internal("textures/push-tile.png"), Format.RGBA4444, true);
	}
	@Override
	public void render(Matrix4 t, float delta) {
		Matrix4 transform = new Matrix4(t);
        shader.begin();
        {
	        shader.setUniformi("u_diffuse", 0);
	        pushTexture.bind();
	        transform.translate(x, 0.0f, -y);
			shader.setUniformMatrix("u_projView", transform);
			mesh.render(shader, GL20.GL_TRIANGLES);
        }
		shader.end();
	}
	@Override
	public void react(Cube cube) {
		// TODO Auto-generated method stub
		
	}

}
