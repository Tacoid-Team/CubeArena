package com.tacoid.cubearena.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.Cube;
import com.tacoid.cubearena.Cube.State;


public class TeleportTile extends Tile {
	Texture teleportTexture;
	
	public TeleportTile() {
		super();
		this.type = TileType.TELEPORT;
		teleportTexture = new Texture(Gdx.files.internal("textures/teleport-tile.png"), Format.RGBA4444, true);
	}
	@Override
	public void render(Matrix4 t, float delta) {
		Matrix4 transform = new Matrix4(t);
        shader.begin();
        {
	        shader.setUniformi("u_diffuse", 0);
	        teleportTexture.bind();
	        transform.translate(x, 0.0f, -y);
			shader.setUniformMatrix("u_projView", transform);
			mesh.render(shader, GL20.GL_TRIANGLES);
        }
		shader.end();
	}
	@Override
	public void react(Cube cube) {
		/* TODO réimplémenter la téléportation
		if(level.getTp1().getX() == cube.getX() &&
		   level.getTp1().getY() == cube.getY()) {
			cube.setX(level.getTp2().getX());
			cube.setY(level.getTp2().getY());
		} else if(level.getTp2().getX() == cube.getX() &&
     		      level.getTp2().getY() == cube.getY()) {
 			cube.setX(level.getTp1().getX());
 			cube.setY(level.getTp1().getY());
     	}
     	*/
		cube.setState(State.ROLLING);
	}

}
