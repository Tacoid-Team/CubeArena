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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.tacoid.cubearena.GameLogic;
import com.tacoid.cubearena.LevelFactory;

public class GameScreen implements Screen,InputProcessor {
	private static final int VIRTUAL_WIDTH = 1280;
	private static final int VIRTUAL_HEIGHT = 768;

	/* 3D Part */
	private OrthographicCamera cam;

	private final Matrix4 transform = new Matrix4();
	
	/* 2D Part */
	Stage stage;
	
	Table inventoryTable;
	Table runTable;
	float zoomFactor;
	
	 // for pinch-to-zoom
	 int numberOfFingers = 0;
	 int fingerOnePointer;
	 int fingerTwoPointer;
	 float lastDistance = 0;
	 Vector3 fingerOne = new Vector3();
	 Vector3 fingerTwo = new Vector3();
	 float camx,camy;

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
					runTable.visible = true;
					inventoryTable.visible = false;
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
					runTable.visible = false;
					inventoryTable.visible = true;
					GameLogic.getInstance().stop();
				}
			});
		}
	}
	
	private GameScreen() {
		stage = new Stage(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, true);
		
		Gdx.graphics.getGL20().glViewport(0,0,VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		
		zoomFactor = 9.0f;
		camx = -2.0f;
		camy = 2.0f;
		cam = new OrthographicCamera(zoomFactor, zoomFactor*768.0f/1280.0f ,35.264f);	
		cam.translate(camx, 5.0f, camy);
		
		BitmapFont font = new BitmapFont();
		font.scale(1.0f);
		NinePatch patch =  new NinePatch(new Texture(Gdx.files.internal("textures/button.9.png")), 2,12, 2, 12);
		TextButtonStyle style = new TextButtonStyle(patch, patch, patch, 
											       0, 0, 0, 0, 
											       font, new Color(0.0f, 0.0f, 0.0f, 1f),new Color(0.0f, 0.0f, 0.0f, 1f),new Color(0.0f, 0.0f, 0.0f, 1f));
		
		GameLogic.getInstance().loadLevel(LevelFactory.getLevel(0));
		ButtonGroup bgroup = GameLogic.getInstance().getButtonGroup(); /* A faire après l'initialisation de gamelogic, vu que ça se base sur le contenu de l'inventory qui est initialisé dans gamelogic */
		
		inventoryTable = new Table();
		inventoryTable.setFillParent(true);
		inventoryTable.debug(); // turn on all debug lines (table, cell, and widget)
		for(Button but : bgroup.getButtons()) {
			inventoryTable.add(but).top();
			inventoryTable.row();
		}
		inventoryTable.add( new DoneButton(style)).left();
		inventoryTable.row();
		inventoryTable.left().bottom();

		stage.addActor(inventoryTable);
		
		
		runTable = new Table();
		runTable.setFillParent(true);
		runTable.debug();
		runTable.add( new StopButton(style)).left();
		runTable.left().bottom();
		stage.addActor(runTable);
		
		runTable.visible = false;
		
		Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);
		Gdx.graphics.getGL20().glEnable(GL20.GL_DEPTH_TEST);
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
		Gdx.graphics.getGL20().glBlendFunc (GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.graphics.getGL20().glEnable(GL20.GL_POLYGON_OFFSET_FILL); 
		
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
        
        
        if(logic.getCube() != null) {
        	logic.getCube().render(transform, delta);
        }
        
        logic.getLevel().render(transform, delta);
        
        logic.getDirectionSelector().render(transform, delta);

        Gdx.graphics.getGL20().glDisable(GL20.GL_CULL_FACE);
        
        stage.draw();
        //Table.drawDebug(stage);
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
		 // some error prevention...
		 numberOfFingers = 0;
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
		 // for pinch-to-zoom
		 numberOfFingers++;
		 if(numberOfFingers == 1)
		 {
		        fingerOnePointer = pointer;
		        fingerOne.set(x, y, 0);
		 }
		 else if(numberOfFingers == 2)
		 {
		        fingerTwoPointer = pointer;
		        fingerTwo.set(x, y, 0);
		 
		       float distance = fingerOne.dst(fingerTwo);
		        lastDistance = distance;
		 }
		return stage.touchDown(x, y, pointer, button);
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		 // for pinch-to-zoom
		if(numberOfFingers >=2) {
		 if (pointer == fingerOnePointer) {
		        fingerOne.set(x, y, 0);
		 }
		 if (pointer == fingerTwoPointer) {
		        fingerTwo.set(x, y, 0);
		 }
		 
		float distance = fingerOne.dst(fingerTwo);
		 float factor = distance / lastDistance;
		 

		 System.out.println(zoomFactor);

		if (lastDistance != distance) {
			 zoomFactor += (1.0f- factor);
			 zoomFactor = MathUtils.clamp(zoomFactor, 6, 16);
		/*	 cam.viewportHeight = zoomFactor;
		     cam.viewportWidth = zoomFactor*768.0f/1280.0f;*/

		 } 
		}
		else {
			float dx = fingerOne.x - x;
			float dy = fingerOne.y - y;
			System.out.println("dx="+dx+" dy="+dy);
			camx += 0.707107*(dx-dy) *0.01f;
			camy += 0.707107*(dx+dy) *0.01f;
			
			fingerOne.set(x, y, 0);
		}
		
		cam = new OrthographicCamera(zoomFactor, zoomFactor*768.0f/1280.0f ,35.264f);	
		cam.translate(camx, 5.0f, camy);
		
		return stage.touchDragged(x, y, pointer);
		
	}


	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		 // for pinch-to-zoom           
		 if(numberOfFingers == 1)
		 {
		        Vector3 touchPoint = new Vector3(x, y, 0);
		        cam.unproject(touchPoint);
		 }
		 numberOfFingers--;
		 
		// just some error prevention... clamping number of fingers (ouch! :-)
		 if(numberOfFingers<0){
		        numberOfFingers = 0;
		 }
		 

		lastDistance = 0;
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
