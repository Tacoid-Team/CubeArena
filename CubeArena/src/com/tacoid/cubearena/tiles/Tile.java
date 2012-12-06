package com.tacoid.cubearena.tiles;

import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.Actor3d;
import com.tacoid.cubearena.Cube;
import com.tacoid.cubearena.CubeArena;
import com.tacoid.cubearena.Direction;
import com.tacoid.cubearena.screens.GameScreen;

public abstract class Tile implements Actor3d {
	
	TileType type;
	Direction direction;
	int x,y;
	
	/* Rendering data */
	protected ShaderProgram shader;
	protected Mesh mesh;
	
	public Tile() {
		this.x = 0;
		this.y = 0;
		this.direction = Direction.NORTH;
		
		shader = GameScreen.getTextureShader();

		/* Mesh loading */
		InputStream in = Gdx.files.internal("data/tile.obj").read();
		mesh = ObjLoader.loadObj(in);
	}
	
	abstract public void render(Matrix4 t, float delta);
	abstract public void react(Cube cube);

	public TileType getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
