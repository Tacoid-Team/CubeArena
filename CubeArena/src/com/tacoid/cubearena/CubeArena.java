package com.tacoid.cubearena;

import com.badlogic.gdx.Game;
import com.tacoid.cubearena.screens.GameScreen;

public class CubeArena extends Game {

	@Override
	public void create() {
		setScreen(GameScreen.getInstance());
	}


}
