package com.tacoid.cubearena;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.tacoid.cubearena.screens.LoadingScreen;

public class CubeArena extends Game {
	
	static private CubeArena instance = null;
	public AssetManager manager;
	
	static public CubeArena getInstance() {
		if(instance == null) {
			instance = new CubeArena();
		}
		return instance;
	}
	
	private CubeArena() {
		
	}

	@Override
	public void create() {
		ScreenStack sStack = ScreenStack.getInstance();
		manager = new AssetManager();
		
		loadAssets();
		
		sStack.clear();
		sStack.push(LoadingScreen.getInstance());
	}
	
	private void loadAssets() {
		
		/* Textures */
		manager.load("textures/change-tile.png", Texture.class);
		manager.load("textures/empty-tile.png", Texture.class);
		manager.load("textures/end-tile.png", Texture.class);
		manager.load("textures/no-tile.png", Texture.class);
		manager.load("textures/push-tile.png", Texture.class);
		manager.load("textures/rotleft-tile.png", Texture.class);
		manager.load("textures/rotright-tile.png", Texture.class);
		manager.load("textures/start-tile.png", Texture.class);
		manager.load("textures/teleport-tile.png", Texture.class);
		manager.load("textures/cube.png", Texture.class);
		manager.load("textures/button.9.png", Texture.class);	
	}
}
