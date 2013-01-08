package com.tacoid.cubearena.tiles;

import java.io.InputStream;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.OvershootInterpolator;
import com.tacoid.cubearena.Actor3d;
import com.tacoid.cubearena.Cube;
import com.tacoid.cubearena.Direction;
import com.tacoid.cubearena.screens.GameScreen;

public abstract class Tile implements Actor3d {
	
	public enum TileState {
		APPEARING,
		IDLE
	};
	
	TileType type;
	private TileState state;
	Direction direction;
	int x,y;
	float z;
	
	Interpolator interp;
	float t;
	float speed;
	
	/* Rendering data */
	protected ShaderProgram shader;
	protected ShaderProgram colorShader;
	protected Mesh mesh;
	
	public Tile() {
		this.x = 0;
		this.y = 0;
		this.z = 0.0f;
		this.t = 0.0f;
		speed = 1.5f*(new Random().nextFloat()*0.6f + 0.7f);
		interp = OvershootInterpolator.$(2.0f);
		this.setState(TileState.APPEARING);
		this.direction = Direction.NORTH;
		
		shader = GameScreen.getTextureShader();
		colorShader = GameScreen.getColorShader();

		/* Mesh loading */
		InputStream in = Gdx.files.internal("data/tile.obj").read();
		mesh = ObjLoader.loadObj(in);
	}
	
	abstract public void render(Matrix4 t, float delta);
	abstract public void react(Cube cube);
	
	protected void update(float delta) {
		
		switch(getState()) {
		case APPEARING:
			t+=delta/speed;
			z = 8*interp.getInterpolation(t)-8; 
			//z = 5.0f*t-5;
			if(t >= 1.0f) {
				setState(TileState.IDLE);
				t = 0;
				z = 0.0f;
			}
			break;
		case IDLE:
			break;
		}
	}

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
	
	protected float getZ() {
		return z;
	}


	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public TileState getState() {
		return state;
	}

	public void setState(TileState state) {
		this.state = state;
	}
}
