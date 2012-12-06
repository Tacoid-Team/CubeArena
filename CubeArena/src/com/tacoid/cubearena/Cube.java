package com.tacoid.cubearena;

import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.tacoid.cubearena.screens.GameScreen;

public class Cube implements Actor3d {
	
	public enum State {
		ROLLING,
		ROTATE_LEFT,
		ROTATE_RIGHT,
		STRAFFING,
		APPEARING,
		VANISHING,
		FALLING,
		IDLE
	};
	
	private int x;
	private int y;
	
	private State state;
	private Direction direction;
	private float t = 0.0f;
	
	/* Rendering data */
	private ShaderProgram shader;
	private Mesh mesh;
	private Texture texture;
	
	public Cube() {
		x = 0;
		y = 0;
		state = State.IDLE;
		direction = Direction.EAST;
		shader = GameScreen.getTextureShader();
		
		/* Texture loading */
		texture = new Texture(Gdx.files.internal("textures/cube.png"), Format.RGBA4444, true);
		
		/* Mesh loading */
		InputStream in = Gdx.files.internal("data/cube.obj").read();
		mesh = ObjLoader.loadObj(in);
		
	}
	
	public void render(Matrix4 t, float delta) {
		Matrix4 transform = new Matrix4(t);
        shader.begin();
        {
	        texture.bind();
	        transform.translate(x, 0.0f, -y);
			switch(state) {
			case ROLLING:
				animRolling(transform, delta);
				break;
			case ROTATE_LEFT:
				animRotate(transform, delta, true);
				break;
			case ROTATE_RIGHT:
				animRotate(transform, delta, false);
				break;
			case FALLING:
				animFalling(transform, delta);
				break;
			case APPEARING:
			case IDLE:
			case STRAFFING:
			case VANISHING:
				break;
			}
			shader.setUniformMatrix("u_projView", transform);
			mesh.render(shader, GL20.GL_TRIANGLES);
        }
		shader.end();
	}
	
	private void animFalling(Matrix4 transform, float delta) {
		t+=5.0f;
		transform.translate(new Vector3(.0f, -t/50, 0.0f));
		transform.translate(new Vector3(.0f, 0.5f, 0.0f));
		switch(direction) {
		case EAST:
	        transform.rotate(new Vector3(0.0f, 0.0f, 1.0f), -t);
	        break;
		case WEST:
	        transform.rotate(new Vector3(0.0f, 0.0f, 1.0f), t);
	        break;
		case SOUTH:
	        transform.rotate(new Vector3(1.0f, 0.0f, 0.0f), t);
			break;
		case NORTH:
	        transform.rotate(new Vector3(1.0f, 0.0f, 0.0f), -t);
			break;
		}
		transform.translate(new Vector3(.0f, -0.5f, 0.0f));
	}
	
	private void animRotate(Matrix4 transform, float delta, boolean left) {

		t+=0.04f;
        transform.rotate(new Vector3(0, 1, 0), (left?1:-1)*t*90.0f);
		if(t > 1.0f) {
			switch(direction) {
			case EAST:
		        direction=left?Direction.NORTH:Direction.SOUTH;
		        break;
			case WEST:
				direction=left?Direction.SOUTH:Direction.NORTH;
		        break;
			case SOUTH:
				direction=left?Direction.EAST:Direction.WEST;
				break;
			case NORTH:
				direction=left?Direction.WEST:Direction.EAST;
				break;
			}
			t = 0;
			state = State.ROLLING;
		}
	}
	
	private void animRolling(Matrix4 transform, float delta) {
        
        //transform.rotate(new Vector3(1, 0, 0), 90);
		t+=0.03f;
		
		switch(direction) {
		case EAST:
	        transform.translate(.5f, 0.0f, 0.0f);
	        transform.rotate(new Vector3(0.0f, 0.0f, 1.0f), -rollFunction(t));
	        transform.translate(-.5f, 0.0f, 0.0f);
	        if(t > 1.0f)
	        	x++;
			break;
		case WEST:
	        transform.translate(-.5f, 0.0f, 0.0f);
	        transform.rotate(new Vector3(0.0f, 0.0f, 1.0f), rollFunction(t));
	        transform.translate(.5f, 0.0f, 0.0f);
	        if(t > 1.0f)
	        	x--;
			break;
		case SOUTH:
	        transform.translate(0.0f, 0.0f, .5f);
	        transform.rotate(new Vector3(1.0f, 0.0f, 0.0f), rollFunction(t));
	        transform.translate(0.0f, 0.0f, -.5f);
	        if(t > 1.0f)
	        	y--;
			break;
		case NORTH:
			transform.translate(0.0f, 0.0f, -.5f);
	        transform.rotate(new Vector3(1.0f, 0.0f, 0.0f), -rollFunction(t));
	        transform.translate(0.0f, 0.0f, .5f);
	        if(t > 1.0f)
	        	y++;
			break;
		}
        
		if(t > 1.0f) {
			t = 0f;
			state = State.IDLE;
		}
	}
	
	/* For t from 0 to 1, returns the angle in degrees */
	private float rollFunction(float t) {
		float angleRad = (float) Math.PI/2 * t * t;
		return (float) (angleRad * 180.0 / Math.PI);
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

}
