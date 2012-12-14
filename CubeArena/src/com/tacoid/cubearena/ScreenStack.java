package com.tacoid.cubearena;

import java.util.Stack;
import com.badlogic.gdx.Screen;

public class ScreenStack extends Stack<Screen>{

	private static final long serialVersionUID = 1L;
	private static ScreenStack instance = null;
	
	public static ScreenStack getInstance() {
		if(instance == null) {
			instance = new ScreenStack();
		}
		return instance;
	}
	
	private ScreenStack() {
		super();
	}
	
	@Override
	public Screen pop() {
		Screen s = super.pop();
		CubeArena.getInstance().setScreen(super.peek());
		return s;
	}
	
	@Override
	public Screen push(Screen screen) {
		super.push(screen);
		CubeArena.getInstance().setScreen(screen);
		return screen;
	}
}
