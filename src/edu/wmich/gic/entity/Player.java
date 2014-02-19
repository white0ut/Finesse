package edu.wmich.gic.entity;

import java.util.ArrayList;
import java.util.List;

public class Player {
	
	// Public variable
	public int points;
	public List<Minion> minions;
	public String name;
	
	public Player(String _name){
		name = _name;
		points = 0;
		minions = new ArrayList<Minion>();
	}
	
	public boolean hasMinions(){
		return minions.size() > 0 ? true : false;
	}
}
