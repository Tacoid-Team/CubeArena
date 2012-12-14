package com.tacoid.cubearena.screens;

import com.badlogic.gdx.Screen;
import com.tacoid.cubearena.CubeArena;
import com.tacoid.cubearena.ScreenStack;

public class LoadingScreen implements Screen {
	static private LoadingScreen instance = null;
	
	static public LoadingScreen getInstance() {
		if(instance == null) {
			instance = new LoadingScreen();
		}
		return instance;
	}
	@Override
	public void render(float delta) {
		if (CubeArena.getInstance().manager.update()) {
			ScreenStack.getInstance().push(GameScreen.getInstance());
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
