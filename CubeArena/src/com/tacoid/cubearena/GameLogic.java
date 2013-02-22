package com.tacoid.cubearena;

import java.util.Set;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.tacoid.cubearena.Level.LevelState;
import com.tacoid.cubearena.TileButtonFactory.TileButton;
import com.tacoid.cubearena.actors.Cube;
import com.tacoid.cubearena.actors.DirectionSelector;
import com.tacoid.cubearena.actors.Cube.State;
import com.tacoid.cubearena.tiles.Tile;
import com.tacoid.cubearena.tiles.TileType;
import com.tacoid.cubearena.tiles.Tile.TileState;

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
		REMOVING_TILE,
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

	private Tile selectedTile;
	private Tile previousSelectedTile;
	
	private Level level = null;
	private Cube cube = null;
	private DirectionSelector directionSelector= null; 

	private ButtonGroup buttonGroup = null;

	private Inventory inventory = null;
	
	
	public Inventory getInventory() {
		return inventory;
	}

	private GameLogic() {
		directionSelector = new DirectionSelector();
		buttonGroup = new ButtonGroup();
		buttonGroup.setMinCheckCount(0);
		buttonGroup.setMaxCheckCount(1);
	}
	
	public void update() {
		
		TileButtonFactory.TileButton checkedButton = (TileButton)(buttonGroup.getChecked());
		
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
			//getCube().setVisible(false);
			if(command == GameCommand.START) {
				/* TODO: cacher le menu d'édition et afficher le boutton stop */
				
				command = GameCommand.NONE; /* La commande est traitée, on reset */
				System.out.println("new state: Lauching");
				cube = new Cube();
				cube.setX(level.getStart().getX());
				cube.setY(level.getStart().getY());
				cube.setDirection(level.levelData.initDir);
				setState(GameState.LAUNCHING);
			} else if(level.isTouched()) {
				
				if(checkedButton != null) {
					selectedTile = level.getTouchedTile();
					if(selectedTile != null) {
						if( selectedTile.isReplaceable()) {
							state = GameState.PLACING_TILE;
							level.resetTouch();
						}
					}
				} else {
					selectedTile = level.getTouchedTile();
					if(selectedTile != null) {
						if(selectedTile.isRemovable()) {
							state = GameState.REMOVING_TILE;
							level.resetTouch();
							previousSelectedTile = selectedTile;
							selectedTile.setState(TileState.FLOATING);
						}
					}
				}
			}
			break;
		case PLACING_TILE:
			/* Cet état correspond au moment où le joueur place une tile sur le niveau.
			 * On a besoin de cet état car deux cas sont possible:
			 * - Soit la tile peut être placée directement, dans ce cas on la place et on reviens Idle
			 * - Soit la tile nécessite d'être orientée, alors affiche le choix de direction et on passe en état CHOSING_DIRECTION
			 */
			if(checkedButton.getType().isDirectionRequired()) {
				directionSelector.setX(selectedTile.getX());
				directionSelector.setY(selectedTile.getY());
				directionSelector.setVisible(true);
				setState(GameState.CHOSING_DIRECTION);
			} else {


				replaceTile(selectedTile, checkedButton.getType());
				selectedTile.changeType(checkedButton.getType(), Direction.NORTH);
				setState(GameState.IDLE);
				buttonGroup.getChecked().setChecked(false);
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
				
				replaceTile(selectedTile, checkedButton.getType(), d);
				setState(GameState.IDLE);
				directionSelector.setVisible(false);
				level.resetTouch();
				buttonGroup.getChecked().setChecked(false);
				
			}
			break;
		case REMOVING_TILE:
			if(level.isTouched()) {
				selectedTile = level.getTouchedTile();
				if(previousSelectedTile.getX() == selectedTile.getX() &&
				   previousSelectedTile.getY() == selectedTile.getY()) {
					replaceTile(selectedTile, TileType.EMPTY);				
				} else {
					previousSelectedTile.setState(TileState.RETURN_TO_ZERO);
				}
				setState(GameState.IDLE);
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
			System.out.println("You won!");
			if(getCube().getState() == State.IDLE) {
				setState(GameState.IDLE);
				getCube().setVisible(false);
			}
			break;
        
        }      
	}
	
	public void replaceTile(Tile tile, TileType newtype) {
		replaceTile(tile, newtype, Direction.NORTH);
	}
	
	public void replaceTile(Tile tile, TileType newType, Direction dir) {
		inventory.removeTile( newType, 1);
		if(tile.getType() != TileType.EMPTY) {
			inventory.addTile(tile.getType(), 1);
		}
		tile.changeType(newType, dir);
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
		inventory.addTile(TileType.TELEPORT, 2);
		
		buttonGroup = new ButtonGroup();
		buttonGroup.setMinCheckCount(0);
		buttonGroup.setMaxCheckCount(1);
		
		Set<TileType> set = inventory.getTileList();
		
		for(TileType type : set) {
			Button but = TileButtonFactory.getInstance().createTileButton(type);
			buttonGroup.add(but);
		}

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
	
	public void checkTouch(OrthographicCamera cam) {
		level.checkTileTouched(cam);
	}
	public DirectionSelector getDirectionSelector() {
		return directionSelector;
	}
	
	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}
}
