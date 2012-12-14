package com.tacoid.cubearena.tiles;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.Cube;
import com.tacoid.cubearena.CubeArena;


public class EndTile extends Tile {
	Texture endTexture;
	
	public EndTile() {
		super();
		this.type = TileType.END;
		endTexture = CubeArena.getInstance().manager.get("textures/end-tile.png", Texture.class);
	}
	@Override
	public void render(Matrix4 t, float delta) {
		Matrix4 transform = new Matrix4(t);
		this.update(delta);
        shader.begin();
        {
	        shader.setUniformi("u_diffuse", 0);
	        endTexture.bind();
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
