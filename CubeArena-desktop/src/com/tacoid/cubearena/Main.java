package com.tacoid.cubearena;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "CubeArena";
		cfg.useGL20 = true;
		cfg.width = 1280;
		cfg.height = 768;
		
		new LwjglApplication(CubeArena.getInstance(), cfg);
	}
}
