package com.tacoid.cubearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.tacoid.cubearena.tiles.TileType;

public class TileButtonFactory {
	
	private static TileButtonFactory instance = null;
	
	public static TileButtonFactory getInstance() {
		if(instance == null) {
			instance = new TileButtonFactory();
		}
		return instance;
	}
	
	public class ButtonSprite extends Actor {
		Sprite sprite;
		ButtonSprite(TileType type) {
			switch(type) {
			case CHANGE_DIRECTION:
				sprite = new Sprite(CubeArena.getInstance().manager.get("textures/rotleft-tile.png", Texture.class));
				break;
			case END:
				sprite = new Sprite(CubeArena.getInstance().manager.get("textures/end-tile.png", Texture.class));
				break;
			case PUSH:
				sprite = new Sprite(CubeArena.getInstance().manager.get("textures/push-tile.png", Texture.class));
				break;
			case ROTATE_LEFT:
				sprite = new Sprite(CubeArena.getInstance().manager.get("textures/rotleft-tile.png", Texture.class));
				break;
			case ROTATE_RIGHT:
				sprite = new Sprite(CubeArena.getInstance().manager.get("textures/rotright-tile.png", Texture.class));
				break;
			case START:
				sprite = new Sprite(CubeArena.getInstance().manager.get("textures/start-tile.png", Texture.class));
				break;
			case TELEPORT:
				sprite = new Sprite(CubeArena.getInstance().manager.get("textures/teleport-tile.png", Texture.class));
				break;
			case EMPTY:
			case NO_TILE:
			default:
				break;
			}
			
			this.height = sprite.getHeight();
			this.width = sprite.getWidth();
		}
		@Override
		public void draw(SpriteBatch batch, float parentAlpha) {
			sprite.setPosition(x, y);
			sprite.draw(batch);
		}

		@Override
		public Actor hit(float x, float y) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	public class TileButton extends Button implements ClickListener{
		
		TileType type;


		public TileButton(TileType type, ButtonStyle style) {
			super(new ButtonSprite(type), style);
			this.type = type;
			//this.add(new ButtonSprite());
			setClickListener(this);
		}
		
		void updateText() {
			//this.setText(type.toString());
		}

		@Override
		public void click(Actor actor, float x, float y) {
			System.out.println(this.isChecked());
			System.out.println(type.toString());
		}
		
		@Override
		public void draw (SpriteBatch batch, float parentAlpha) {
			if(isChecked())
				this.x = 20;
			else
				this.x = 0;
			super.draw(batch, parentAlpha);
		}
		
		public TileType getType() {
			return type;
		}


	}

	private TileButtonFactory() {
		
	}

	public TileButton createTileButton(TileType type) {
		BitmapFont font = new BitmapFont();
		font.scale(2.0f);
		NinePatch patch =  new NinePatch(new Texture(Gdx.files.internal("textures/button.9.png")), 2,12, 2, 12);
		ButtonStyle style = new ButtonStyle(patch, patch, patch,0,0,0,0);
		return new TileButton(type, style);
	}
}
