package com.tacoid.cubearena;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ShaderManager {
	static private ShaderManager instance = null;
	
	static public ShaderManager getInstance() {
		if(instance == null) {
			instance = new ShaderManager();
		}
		return instance;
	}
	
	private Map<String, ShaderProgram> shaders;
	
	private ShaderManager() {
		shaders = new HashMap<String,ShaderProgram>();
	}
	
	public void loadShader(String name, String vs, String fs) {
		ShaderProgram program = new ShaderProgram(Gdx.files.internal(vs), Gdx.files.internal(fs));
		if (!program.isCompiled()) throw new GdxRuntimeException("Couldn't compile texture shader");
		shaders.put(name, program );
	}
	
	public ShaderProgram getShader(String name) {
		return shaders.get(name);
	}

}
