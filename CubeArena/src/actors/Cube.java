package actors;

import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.tacoid.cubearena.Actor3d;
import com.tacoid.cubearena.CubeArena;
import com.tacoid.cubearena.Direction;
import com.tacoid.cubearena.ShaderManager;
import com.tacoid.cubearena.tiles.Tile;

public class Cube implements Actor3d {
	
	public enum State {
		ROLLING,
		ROTATE_LEFT,
		ROTATE_RIGHT,
		PUSH,
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
	private boolean visible;
	private Tile activeTile;
	
	/* Rendering data */
	private ShaderProgram shader;
	private Mesh mesh;
	private Texture texture;
	
	public Cube() {
		x = 0;
		y = 0;
		state = State.APPEARING;
		direction = Direction.EAST;
		shader = ShaderManager.getInstance().getShader("texture");
		setVisible(false);
		
		/* Texture loading */
		texture = CubeArena.getInstance().manager.get("textures/cube.png", Texture.class);
		
		/* Mesh loading */
		InputStream in = Gdx.files.internal("data/cube.obj").read();
		mesh = ObjLoader.loadObj(in);
		
		setActiveTile(null);
		
	}
	
	public void render(Matrix4 t, float delta) {
		if(visible) {
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
					animAppearing(transform, delta);
					break;
				case PUSH:
					animPush(transform, delta);
					break;
				case IDLE:
				case VANISHING:
					break;
				}
				shader.setUniformMatrix("u_projView", transform);
				mesh.render(shader, GL20.GL_TRIANGLES);
	        }
			shader.end();
		}
	}
	
	private void animAppearing(Matrix4 transform, float delta) {
		t+=2.0f*delta;
		transform.scale(t,t,t);
		if(t > 1.0f) {
			t = 0;
			state = State.IDLE;
		}
	}
	
	private void animFalling(Matrix4 transform, float delta) {
		t+=2.0f*delta;
		transform.translate(new Vector3(.0f, -t*3.0f, 0.0f));
		transform.translate(new Vector3(.0f, 0.5f, 0.0f));
		switch(direction) {
		case EAST:
	        transform.rotate(new Vector3(0.0f, 0.0f, 1.0f), -t*120.0f);
	        break;
		case WEST:
	        transform.rotate(new Vector3(0.0f, 0.0f, 1.0f), t*120.0f);
	        break;
		case SOUTH:
	        transform.rotate(new Vector3(1.0f, 0.0f, 0.0f), t*120.0f);
			break;
		case NORTH:
	        transform.rotate(new Vector3(1.0f, 0.0f, 0.0f), -t*120.0f);
			break;
		}
		transform.translate(new Vector3(.0f, -0.5f, 0.0f));
	}
	
	private void animPush(Matrix4 transform, float delta) {
		t+=2.0f*delta;
		switch(activeTile.getDirection()) {
		case EAST:
			transform.translate(t, 0.0f, 0.0f);
	        if(t > 1.0f)
	        	x++;
	        break;
		case WEST:
			transform.translate(-t, 0.0f, 0.0f);
	        if(t > 1.0f)
	        	x--;
	        break;
		case SOUTH:
			transform.translate(0.0f, 0.0f, t);
			if(t > 1.0f)
	        	y--;
			break;
		case NORTH:
			if(t > 1.0f)
	        	y++;
			transform.translate(0.0f, 0.0f, -t);
			break;
		}
		
		if(t > 1.0f) {
			t = 0f;
			state = State.IDLE;
		}
	}
	
	private void animRotate(Matrix4 transform, float delta, boolean left) {

		t+=2.0f*delta;
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
		t+=2.0f*delta;
		
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
		t = 0.0f;
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

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Tile getActiveTile() {
		return activeTile;
	}

	public void setActiveTile(Tile activeTile) {
		this.activeTile = activeTile;
	}

}
