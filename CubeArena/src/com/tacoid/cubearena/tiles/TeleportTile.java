package com.tacoid.cubearena.tiles;


import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.CubeArena;
import com.tacoid.cubearena.GameLogic;
import com.tacoid.cubearena.actors.Cube;
import com.tacoid.cubearena.actors.Cube.State;


public class TeleportTile extends Tile {
	private Texture teleportTexture;
	private int id;

	public TeleportTile() {
		super();
		id = 0;
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
		System.out.println("Taking teleport "+ id );
		Tile t = GameLogic.getInstance().getLevel().getTeleport((id==1)?0:1);
		if(t != null) {
			cube.setX(t.getX());
			cube.setY(t.getY());
		}
			
		cube.setState(State.ROLLING);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}

