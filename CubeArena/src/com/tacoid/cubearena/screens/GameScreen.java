package com.tacoid.cubearena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.tacoid.cubearena.GameLogic;
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

	static private GameScreen instance = null;
	
	static public GameScreen getInstance() {
		if(instance == null) {
			instance = new GameScreen();
		}
		return instance;
	}
	class TestActor extends Actor {
		Sprite s;
		TestActor(){
			s = new Sprite(new Texture(Gdx.files.internal("textures/change-tile.png")));
		}
		
		@Override
		public void draw(SpriteBatch batch, float parentAlpha) {
			s.draw(batch);
		}

		@Override
		public Actor hit(float x, float y) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private GameScreen() {
		stage = new Stage(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, true);
		
		Gdx.graphics.getGL20().glViewport(0,0,VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		cam = new OrthographicCamera(12, 12*768.0f/1280.0f ,35.264f);	
		cam.translate(-2.0f, 5.0f, 2.0f);
		
		stage.addActor( new TestActor() );

		textureShader = new ShaderProgram(Gdx.files.internal("shaders/tex-vs.glsl"),
										  Gdx.files.internal("shaders/tex-fs.glsl"));
		if (!textureShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile texture shader");

		GameLogic.getInstance().loadLevel(LevelFactory.getLevel(0));
		
		Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);
		Gdx.graphics.getGL20().glEnable(GL20.GL_DEPTH_TEST);
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
		Gdx.graphics.getGL20().glBlendFunc (GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		
        Gdx.graphics.getGL20().glClearColor(0.69453125f, 0.690625f, 0.6828125f, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        GameLogic logic = GameLogic.getInstance();
		
        cam.update();
        
        transform.set(cam.combined);
        
        logic.update();
        
        logic.getLevel().checkTileTouched(cam);

        logic.getCube().render(transform, delta);
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
