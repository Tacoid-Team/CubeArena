package com.tacoid.cubearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

	public class TileButton extends TextButton{
		
		TileType type;

		public TileButton(TileType type, TextButtonStyle style) {
			super(type.toString(), style);
			this.type = type;
		}
		
		void updateText() {
			this.setText(type.toString());
		}

	}

	private TileButtonFactory() {
		
	}

	public TileButton createTileButton(TileType type) {
		BitmapFont font = new BitmapFont();
		font.scale(1.0f);
		NinePatch patch =  new NinePatch(new Texture(Gdx.files.internal("textures/button.9.png")), 2,12, 2, 12);
		TextButtonStyle style = new TextButtonStyle(patch, patch, patch, 
											       0, 0, 0, 0, 
											       font, new Color(0.0f, 0.0f, 0.0f, 1f),new Color(0.0f, 0.0f, 0.0f, 1f),new Color(0.0f, 0.0f, 0.0f, 1f));
		
		return new TileButton(type, style);
	}
}
