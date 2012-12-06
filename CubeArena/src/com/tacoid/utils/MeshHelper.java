package com.tacoid.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class MeshHelper {
	private Mesh mesh;
	private ShaderProgram meshShader;

	public MeshHelper() {
		createShader();
	}
	
	public void createMesh(float[] vertices) {
		mesh = new Mesh(true, vertices.length, 0, new VertexAttribute(Usage.Position, 3, "a_position"),
                								  new VertexAttribute(Usage.ColorPacked, 4, "a_color"),
                								  new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));

		mesh.setVertices(vertices);
	}
	
	public void drawMesh() {
		if (mesh == null) {
			throw new IllegalStateException("drawMesh called before a mesh has been created.");
		}
		
		meshShader.begin();
		mesh.render(meshShader, GL20.GL_TRIANGLES);
		meshShader.end();
	}

	private void createShader() {
		meshShader = new ShaderProgram(Gdx.files.internal("shaders/vertexShader.glsl"), 
									   Gdx.files.internal("shaders/fragmentShader.glsl"));
		if(meshShader.isCompiled() == false) 
			throw new IllegalStateException(meshShader.getLog());
	}
	
	public void dispose() {
		mesh.dispose();
		meshShader.dispose();
	}
}