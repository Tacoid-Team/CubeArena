package com.tacoid.cubearena;

import com.tacoid.cubearena.Cube.State;
import com.tacoid.cubearena.tiles.Tile;

public class GameLogic {

	private static GameLogic instance = null;
	
	public static GameLogic getInstance() {
		if(instance == null) {
			instance = new GameLogic();
		}
		return instance;
	}
	
	public enum GameState {
		IDLE,
		RUNNING,
		WIN,
		LOSE
	}
	
	private Level level = null;
	private Cube cube = null;
	private GameState state = GameState.IDLE;
	
	private GameLogic() {
		cube = new Cube();
	}
	
	public void update() {
        if(cube.getState() == State.IDLE) {
        	if(cube.getX() >= 0 && cube.getX() < level.level.length &&
        	   cube.getY() >= 0 && cube.getY() < level.level[0].length) {
        		Tile currentTile = level.level[cube.getX()][cube.getY()];
        		currentTile.react(cube);
        	}
        }
        
	}
	
	public void loadLevel(LevelData level) {
		this.level = new Level(level);
		state = GameState.IDLE;
		cube.setDirection(Direction.EAST);
		this.level.initCube(cube);
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Cube getCube() {
		return cube;
	}

	public void setCube(Cube cube) {
		this.cube = cube;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}
	
}
