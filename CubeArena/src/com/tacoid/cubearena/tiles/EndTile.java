package com.tacoid.cubearena.tiles;


import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.CubeArena;
import com.tacoid.cubearena.actors.Cube;


public class EndTile extends Tile {
	Texture endTexture;
	
	public EndTile() {
		super();
		this.type = TileType.END;
		endTexture = CubeArena.getInstance().manager.get("textures/end-tile.png", Texture.class);
	}
	@Override
	public void renderTile(Matrix4 t, float delta) {
		Matrix4 transform = new Matrix4(t);
		this.update(delta);

        shader.begin();
        {
	        shader.setUniformi("u_diffuse", 0);
	        endTexture.bind();
	        transform.translate(x, getZ() + 0.001f, -y);
			shader.setUniformMatrix("u_projView", transform);
			decal.render(shader, GL20.GL_TRIANGLES);
        }
		shader.end();
	}
	@Override
	public void react(Cube cube) {
		// TODO Auto-generated method stub
		
	}

}
