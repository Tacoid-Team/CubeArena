package com.tacoid.cubearena.tiles;

import actors.Cube;
import actors.Cube.State;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.CubeArena;


public class TeleportTile extends Tile {
	
	static int teleportCounter = 0;
	
	private Texture teleportTexture;
	private int teleportId;
	
	
	
	public TeleportTile() {
		super();
		teleportCounter++;
		teleportId = teleportCounter;
		this.type = TileType.TELEPORT;
		teleportTexture = CubeArena.getInstance().manager.get("textures/teleport-tile.png", Texture.class);
	}
	@Override
	public void renderTile(Matrix4 t, float delta) {
		Matrix4 transform = new Matrix4(t);
        shader.begin();
        {
	        shader.setUniformi("u_diffuse", 0);
	        teleportTexture.bind();
	        transform.translate(x, getZ() + 0.001f, -y);
			shader.setUniformMatrix("u_projView", transform);
			decal.render(shader, GL20.GL_TRIANGLES);
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
	public int getTeleportId() {
		return teleportId;
	}
}
