package com.tacoid.cubearena;

import actors.Cube;
import actors.DirectionSelector;
import actors.Cube.State;

import com.badlogic.gdx.graphics.OrthographicCamera;
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
	
	private TileType selectedType;
	private Tile selectedTile;
	
	private Level level = null;
	private Cube cube = null;
	private DirectionSelector directionSelector= null; 


	private Inventory inventory = null;
	
	
	public Inventory getInventory() {
		return inventory;
	}

	private GameLogic() {
		directionSelector = new DirectionSelector();
	}
	
	public void update() {
        /* Game state machine */
        switch(getState()) {
        case INIT:
        	/* Cet état ne fais pas grand chose pour le moment, on passe direct à SHOWING_LEVEL */
        	setState(GameState.SHOWING_LEVEL);
			break;
		case SHOWING_LEVEL:
			/* Cet état sert à attendre que l'animation d'apparition du niveau soit terminée. Dès que c'est le cas, on passe IDLE */
			if(getLevel().getState() == LevelState.READY) {
				setState(GameState.IDLE);
			}
			break;
		case IDLE:
			/* C'est l'état principal de l'édition du niveau qui consiste à attendre deux évènements:
			 * - Si le joueur appuis sur "Done", on lance le cube avec l'état START
			 * - Si le joueur selectionne un type de tile puis une tile, alors on lance le placement de tile avec l'état PLACING_TILE
			 */
			
			if(command == GameCommand.START) {
				/* TODO: cacher le menu d'édition et afficher le boutton stop */
				System.out.println("new state: Lauching");
				cube = new Cube();
				cube.setX(level.getStart().getX());
				cube.setY(level.getStart().getY());
				cube.setDirection(level.levelData.initDir);
				setState(GameState.LAUNCHING);
			} else if(level.isTouched()) {
				selectedTile = level.getTouchedTile();
				if(selectedTile != null) {
					state = GameState.PLACING_TILE;
					level.resetTouch();
				}
			}
			break;
		case PLACING_TILE:
			/* Cet état correspond au moment où le joueur place une tile sur le niveau.
			 * On a besoin de cet état car deux cas sont possible:
			 * - Soit la tile peut être placée directement, dans ce cas on la place et on reviens Idle
			 * - Soit la tile nécessite d'être orientée, alors affiche le choix de direction et on passe en état CHOSING_DIRECTION
			 */
			if(selectedType.isDirectionRequired()) {
				directionSelector.setX(selectedTile.getX());
				directionSelector.setY(selectedTile.getY());
				directionSelector.setVisible(true);
				setState(GameState.CHOSING_DIRECTION);
			} else {
				level.replaceTile(selectedTile, selectedType);
				selectedType = TileType.EMPTY;
				setState(GameState.IDLE);
			}
			break;
		case CHOSING_DIRECTION:
			
			if(level.isTouched()) {
				Direction d;
				if(level.getTouchedX() == selectedTile.getX()+1 && level.getTouchedY() == selectedTile.getY()) {
					d = Direction.EAST;
				} else if(level.getTouchedX() == selectedTile.getX()-1 && level.getTouchedY() == selectedTile.getY()) {
					d = Direction.WEST;
				} else if(level.getTouchedX() == selectedTile.getX() && level.getTouchedY() == selectedTile.getY()+1) {
					d = Direction.NORTH;
				} else if(level.getTouchedX() == selectedTile.getX() && level.getTouchedY() == selectedTile.getY()-1) {
					d = Direction.SOUTH;
				} else {
					level.resetTouch();
					break;
				}
				
				level.replaceTile(selectedTile, selectedType);
				level.getTile(selectedTile.getX(), selectedTile.getY()).setDirection(d);
				selectedType = TileType.EMPTY;
				setState(GameState.IDLE);
				directionSelector.setVisible(false);
				level.resetTouch();
			}
			break;
		case LAUNCHING:
			/* Cette état correpond au moment où l'utilisateur appuis sur le boutton "done" pour lancer le jeu. 
			 * Avant de lancer vraiment le jeu, on attend que le cube soit IDLE (c'est à dire que son animation d'apparition soit terminée
			 */
			getCube().setVisible(true);
			if(getCube().getState() == State.IDLE) {
				setState(GameState.RUNNING);
				System.out.println("new state: Running");
				getCube().setState(State.ROLLING);
			}
			break;
		case LOSE:
			/* Cette état correspond au moment où le cube tombe du niveau. 
			 * Pas sur qu'il soit nécessaire vu qu'on ne souhaite rien faire de particulier dans ce cas
			 */
			break;
		case QUIT:
			/* Cet état servira si on veut jouer une animation au moment où on quittera la partie */
			break;
		case RUNNING:
			/* L'état principal du jeu quand il est lancé.
			 * A chaque fois que le cube est en état idle (c'est à dire qu'il a fini son animation), on lance l'animation suivante
			 * Si on appuis sur "Stop", réinitialise tout pour revenir en état idle
			 */
			getCube().setVisible(true);
	        if(cube.getState() == State.IDLE) {
	        	if(cube.getX() >= 0 && cube.getX() < level.level.length &&
	        	   cube.getY() >= 0 && cube.getY() < level.level[0].length) {
	        		Tile currentTile = level.level[cube.getX()][cube.getY()];
	        		cube.setActiveTile(currentTile);
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
			/* Hell yeah, todo*/
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
		level = new Level(newLevel);
		inventory = new Inventory();
		inventory.addTile(TileType.PUSH, 1);
		inventory.addTile(TileType.ROTATE_RIGHT, 2);
		
		selectedType = TileType.EMPTY;
		
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

	public TileType getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(TileType selectedType) {
		this.selectedType = selectedType;
	}	
	
	public void checkTouch(OrthographicCamera cam) {
		level.checkTileTouched(cam);
	}
	public DirectionSelector getDirectionSelector() {
		return directionSelector;
	}
}
