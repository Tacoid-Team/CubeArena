package com.tacoid.cubearena;

import com.tacoid.cubearena.Cube.State;
import com.tacoid.cubearena.Level.LevelState;
import com.tacoid.cubearena.tiles.Tile;
import com.tacoid.cubearena.tiles.TileType;

public class GameLogic {

	private static GameLogic instance = null;
	
	public static GameLogic getInstance() {
		if(instance == null) {
			instance = new GameLogic();
		}
		return instance;
	}
	
	public enum GameState {
		INIT,
		SHOWING_LEVEL,
		IDLE,
		PLACING_TILE,
		CHOSING_DIRECTION,
		LAUNCHING,
		RUNNING,
		WIN,
		LOSE,
		QUIT
	};
	
	enum GameCommand {
		NONE,
		START,
		STOP
	};
	
	private GameState state;
	private GameCommand command;
	
	private Level level = null;
	private Cube cube = null;
	private Inventory inventory = null;
	
	public Inventory getInventory() {
		return inventory;
	}

	private GameLogic() {
		cube = new Cube();
	}
	
	public void update() {
		getCube().setVisible(false);
        /* Game state machine */
        switch(getState()) {
        case INIT:
        	System.out.println("new state: Showing level");
        	setState(GameState.SHOWING_LEVEL);
			break;
		case SHOWING_LEVEL:
			if(getLevel().getState() == LevelState.READY) {
				System.out.println("new state: Idle");
				setState(GameState.IDLE);
			}
			break;
		case IDLE:
			/* TODO: Si on a selection un type de tile à poser avec un boutton */
				//state = GameState.PLACING_TILE;

			if(command == GameCommand.START) {
				/* TODO: cacher le menu d'édition et afficher le boutton stop */
				System.out.println("new state: Lauching");
				getCube().setState(State.APPEARING); 
				setState(GameState.LAUNCHING);
			}
			break;
		case PLACING_TILE:
			/* Si la tile est placée */
				/* Si la tile nécessite d'être orientée */
					/* state = GameState.CHOSING_DIRECTION */
				/* Sinon */
			System.out.println("new state: Idle");
					setState(GameState.IDLE);
			break;
		case CHOSING_DIRECTION:
			/* Si la direction est choisie */
			/* Animer l'apparition de la tile*/
			setState(GameState.IDLE);
			break;
		case LAUNCHING:
			getCube().setVisible(true);
			if(getCube().getState() == State.IDLE) {
				setState(GameState.RUNNING);
				System.out.println("new state: Running");
				getCube().setState(State.ROLLING);
			}
			break;
		case LOSE:
			break;

		case QUIT:
			break;
		case RUNNING:
			getCube().setVisible(true);
	        if(cube.getState() == State.IDLE) {
	        	if(cube.getX() >= 0 && cube.getX() < level.level.length &&
	        	   cube.getY() >= 0 && cube.getY() < level.level[0].length) {
	        		Tile currentTile = level.level[cube.getX()][cube.getY()];
	        		currentTile.react(cube);
	        	} else {
	            	cube.setState(State.FALLING);
	            }
	        }
	        if(command == GameCommand.STOP) {
	        	getLevel().initCube(cube);
	        	getCube().setVisible(false);
	        	state = GameState.IDLE;
	        }
			break;
		case WIN:
			break;
        
        }      
	}
	
	public void start() {
		command = GameCommand.START;
	}
	
	public void stop() {
		command = GameCommand.STOP;
	}
	
	public void loadLevel(LevelData newLevel) {
		cube.setDirection(newLevel.initDir);
		level = new Level(newLevel);
		level.initCube(cube);
		inventory = new Inventory();
		inventory.addTile(TileType.CHANGE_DIRECTION, 3);
		inventory.addTile(TileType.ROTATE_RIGHT, 2);
		state = GameState.IDLE;
		command = GameCommand.NONE;
	}

	public Level getLevel() {
		return level;
	}

	public Cube getCube() {
		return cube;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}	
}
