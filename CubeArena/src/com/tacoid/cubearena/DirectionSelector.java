package com.tacoid.cubearena;

import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.tacoid.cubearena.screens.GameScreen;

public class DirectionSelector implements Actor3d {
	private boolean visible;
	private int x;
	private int y;

	private ShaderProgram colorShader;
	private Mesh arrow;
	private float[] color = {1.0f, 0.4f, 0.4f, 0.8f};
	float t;
	
	public DirectionSelector() {
		x = 0;
		y = 0;
		t = 0.0f;
		visible = false;
		colorShader = GameScreen.getColorShader();
		
		InputStream in = Gdx.files.internal("data/arrow.obj").read();
		arrow = ObjLoader.loadObj(in);
	}

	public void render(Matrix4 t, float delta) {
		if(visible) {
			renderArrow(t,delta,1,0, -90.0f);
			renderArrow(t,delta,-1,0, 90.0f);
			renderArrow(t,delta,0,-1, 180);
			renderArrow(t,delta,0,1, 0);
		}
	}
	
	public void renderArrow(Matrix4 t, float delta, int x, int y, float rot) {
		Matrix4 transform = new Matrix4(t);
		this.t+=delta;
        colorShader.begin();
        {
        	colorShader.setUniform4fv("u_color", color, 0, 4);
        	
        	transform.scale(.25f, 0.2f, .25f);
	        transform.translate((this.x+x)*4, 1.0f, -(this.y+y)*4);
	        transform.rotate(0.0f, 1.0f, 0.0f, rot);
	        transform.translate(0.0f, 0.0f,(float) Math.cos(this.t)*0.8f);
			colorShader.setUniformMatrix("u_projView", transform);
			arrow.render(colorShader, GL20.GL_TRIANGLES);
			
        }
		colorShader.end();
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
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



}
