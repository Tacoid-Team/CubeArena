package com.tacoid.cubearena.tiles;


public class TileFactory {
	public static Tile createNewTile(TileType type, int x, int y) {
		Tile tile = null;
		switch(type) {
		case CHANGE_DIRECTION:
			tile = new ChangeTile();
			break;
		case EMPTY:
			tile = new EmptyTile();
			break;
		case END:
			tile = new EndTile();
			break;
		case PUSH:
			tile = new PushTile();
			break;
		case ROTATE_LEFT:
			tile = new RotLeftTile();
			break;
		case ROTATE_RIGHT:
			tile = new RotRightTile();
			break;
		case START:
			tile = new StartTile();
			break;
		case TELEPORT:
			tile = new TeleportTile();
			break;
		case NO_TILE:
			tile = new NoTile();
			break;	
		}
		tile.setX(x);
		tile.setY(y);
		return tile;
	}
}

