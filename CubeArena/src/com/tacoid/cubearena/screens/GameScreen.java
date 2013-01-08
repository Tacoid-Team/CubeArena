package com.tacoid.cubearena.screens;

import java.util.Set;

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
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.tacoid.cubearena.GameLogic;
import com.tacoid.cubearena.LevelFactory;
import com.tacoid.cubearena.TileButtonFactory;
import com.tacoid.cubearena.tiles.TileType;

public class GameScreen implements Screen,InputProcessor {
	private static final int VIRTUAL_WIDTH = 1280;
	private static final int VIRTUAL_HEIGHT = 768;

	/* 3D Part */
	private OrthographicCamera cam;
	static ShaderProgram textureShader;
	static ShaderProgram colorShader;
	public static ShaderProgram getColorShader() {
		return colorShader;
	}

	private final Matrix4 transform = new Matrix4();
	
	/* 2D Part */
	Stage stage;

	static private GameScreen instance = null;
	
	static public GameScreen getInstance() {
		if(instance == null) {
			instance = new GameScreen();
		}
		return instance;
	}
	class DoneButton extends TextButton{
	
		public DoneButton(TextButtonStyle style) {
			super("done", style);
			setClickListener(new ClickListener() {
				@Override
				public void click(Actor actor, float x, float y) {
					GameLogic.getInstance().start();
				}
			});
		}
	}
	
	class StopButton extends TextButton{
		
		public StopButton(TextButtonStyle style) {
			super("stop", style);

			setClickListener(new ClickListener() {
				@Override
				public void click(Actor actor, float x, float y) {
					GameLogic.getInstance().stop();
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
		font.scale(1.0f);
		NinePatch patch =  new NinePatch(new Texture(Gdx.files.internal("textures/button.9.png")), 2,12, 2, 12);
		TextButtonStyle style = new TextButtonStyle(patch, patch, patch, 
											       0, 0, 0, 0, 
											       font, new Color(0.0f, 0.0f, 0.0f, 1f),new Color(0.0f, 0.0f, 0.0f, 1f),new Color(0.0f, 0.0f, 0.0f, 1f));
		
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		textureShader = new ShaderProgram(Gdx.files.internal("shaders/tex-vs.glsl"),
										  Gdx.files.internal("shaders/tex-fs.glsl"));
		colorShader = new ShaderProgram(Gdx.files.internal("shaders/color-vs.glsl"),
				  Gdx.files.internal("shaders/color-fs.glsl"));
		if (!textureShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile texture shader");

		GameLogic.getInstance().loadLevel(LevelFactory.getLevel(0));
		
		
		/* A faire après l'initialisation de gamelogic, vu que ça se base sur le contenu de l'inventory qui est initialisé dans gamelogic */
		Set<TileType> set = GameLogic.getInstance().getInventory().getTileList();
		table.row();
		for(TileType type : set) {
			table.add(TileButtonFactory.getInstance().createTileButton(type));
		}
		table.add( new DoneButton(style)).width(100);
		table.add( new StopButton(style));
		table.left().bottom();
		
		Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);
		Gdx.graphics.getGL20().glEnable(GL20.GL_DEPTH_TEST);
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
		Gdx.graphics.getGL20().glBlendFunc (GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		Gdx.input.setInputProcessor(this);
		stage.act(Gdx.graphics.getDeltaTime());
		
		//startPressed = false;
	}

	@Override
	public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(0.3f, 0.3f, 0.3f, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        GameLogic logic = GameLogic.getInstance();
		
        cam.update();
        
        transform.set(cam.combined);
        
        logic.update();
        
        logic.checkTouch(cam);

        logic.getCube().render(transform, delta);

        logic.getLevel().render(transform, delta);
        
        Gdx.graphics.getGL20().glDisable(GL20.GL_CULL_FACE);
        
        stage.draw();
        Table.drawDebug(stage);
	}
	
	public static ShaderProgram getTextureShader() {
		return textureShader;
	}


	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
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
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		return false;
	}

}
