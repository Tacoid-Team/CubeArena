package com.tacoid.cubearena;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tacoid.cubearena.tiles.TileType;

public class Inventory {
	
	Map<TileType,Integer> inventory;
	
	Inventory() {
		inventory = new HashMap<TileType, Integer>();
	}
	
	public void addTile(TileType type, int amount) {
		Integer oldAmount = 0; 
		if(inventory.containsKey(type)) {
			oldAmount = inventory.get(type);
		}
		inventory.put(type, oldAmount+amount);
	}
	
	public void removeTile(TileType type, int amount) {
		Integer oldAmount = 0; 
		if(inventory.containsKey(type)) {
			oldAmount = inventory.get(type);
			inventory.put(type, Math.max(oldAmount-amount,0));
		}
	}
	
	public int getAmount(TileType type) {
		if(inventory.containsKey(type)) {
			return inventory.get(type);
		} else {
			return 0;
		}
	}
	
	public Set<TileType> getTileList() {
		return inventory.keySet();
	}
}
