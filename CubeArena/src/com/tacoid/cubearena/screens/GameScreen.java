package com.tacoid.cubearena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.tacoid.cubearena.Cube.State;
import com.tacoid.cubearena.GameLogic;
import com.tacoid.cubearena.Level.LevelState;
import com.tacoid.cubearena.LevelFactory;

public class GameScreen implements Screen,InputProcessor {
	private static final int VIRTUAL_WIDTH = 1280;
	private static final int VIRTUAL_HEIGHT = 768;

	/* 3D Part */
	private OrthographicCamera cam;
	static ShaderProgram textureShader;
	private final Matrix4 transform = new Matrix4();
	
	/* 2D Part */
	Stage stage;
	
	public enum GameState {
		INIT,
		SHOWING_BUTTONS,
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
	
	private GameState state;
	private boolean startPressed;

	static private GameScreen instance = null;
	
	static public GameScreen getInstance() {
		if(instance == null) {
			instance = new GameScreen();
		}
		return instance;
	}
	class DoneButton extends TextButton{
	
		public DoneButton(String text, TextButtonStyle style) {
			super("Done", style);
			setClickListener(new ClickListener() {
				@Override
				public void click(Actor actor, float x, float y) {
					startPressed = true;
				}
			});
		}
	}
	
	private GameScreen() {
		stage = new Stage(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, true);
		
		Gdx.graphics.getGL20().glViewport(0,0,VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		cam = new OrthographicCamera(12, 12*768.0f/1280.0f ,35.264f);	
		cam.translate(-2.0f, 5.0f, 2.0f);
		
		BitmapFont font = new BitmapFont();
		font.scale(2.0f);
		NinePatch patch =  new NinePatch(new Texture(Gdx.files.internal("textures/button.9.png")), 2,12, 2, 12);
		TextButtonStyle style = new TextButtonStyle(patch, patch, patch, 
											       0, 0, 0, 0, 
											       font, new Color(0.0f, 0.0f, 0.0f, 1f), new Color(1.0f, 0, 0, 1f), new Color(1.0f, 0, 0, 1f));
		
		stage.addActor( new DoneButton("done", style) );

		textureShader = new ShaderProgram(Gdx.files.internal("shaders/tex-vs.glsl"),
										  Gdx.files.internal("shaders/tex-fs.glsl"));
		if (!textureShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile texture shader");

		GameLogic.getInstance().loadLevel(LevelFactory.getLevel(0));
		
		Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);
		Gdx.graphics.getGL20().glEnable(GL20.GL_DEPTH_TEST);
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
		Gdx.graphics.getGL20().glBlendFunc (GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		Gdx.input.setInputProcessor(this);
		setState(GameState.INIT);
		stage.act(Gdx.graphics.getDeltaTime());
		
		startPressed = false;
	}

	@Override
	public void render(float delta) {
		boolean showCube = false;
        Gdx.graphics.getGL20().glClearColor(0.69453125f, 0.690625f, 0.6828125f, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        GameLogic logic = GameLogic.getInstance();
		
        cam.update();
        
        transform.set(cam.combined);
        
        /* Game state machine */
        switch(getState()) {
        case INIT:
        	/*TODO show buttons */
        	System.out.println("new state: Showing buttons");
        	setState(GameState.SHOWING_BUTTONS);
			break;
		case SHOWING_BUTTONS:
			/* TODO show level */
			System.out.println("new state: Showing level");
			setState(GameState.SHOWING_LEVEL);
			break;
		case SHOWING_LEVEL:
			
			if(logic.getLevel().getState() == LevelState.READY) {
				System.out.println("new state: Idle");
				setState(GameState.IDLE);
			}
			break;
		case IDLE:
			/* Si on a selection un type de tile à poser avec un boutton */
				//state = GameState.PLACING_TILE;
			/* Si on a cliqué sur "Done" */
				/* Level.setState(LAUCHING) */
			if(startPressed) {
				System.out.println("new state: Lauching");
				logic.getCube().setState(State.APPEARING); 
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
			showCube = true;
			if(logic.getCube().getState() == State.IDLE) {
				state = GameState.RUNNING;
				System.out.println("new state: Running");
				logic.getCube().setState(State.ROLLING);
			}
			break;
		case LOSE:
			break;

		case QUIT:
			break;
		case RUNNING:
			showCube = true;
			break;

		
		case WIN:
			break;
        
        }
        startPressed = false;
        
        
        logic.update();
        
        logic.getLevel().checkTileTouched(cam);

        if(showCube) {
        	logic.getCube().render(transform, delta);
        }
        
        logic.getLevel().render(transform, delta);
        
        Gdx.graphics.getGL20().glDisable(GL20.GL_CULL_FACE);
        
        stage.draw();
	}
	
	public static ShaderProgram getTextureShader() {
		return textureShader;
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return stage.touchDown(x, y, pointer, button);
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		return stage.touchDragged(arg0, arg1, arg2);
	}


	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return stage.touchUp(x, y, pointer, button);
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

}
