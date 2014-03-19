package edu.wmich.gic.entity;

import java.util.ArrayList;
import java.util.List;

public class Player {
	
	// Public variable
	public int points;
	public int id = 0;
	public List<Minion> minions;
	public String name;
	
	public Player(String _name, int _id){
		id = _id;
		name = _name;
		points = 100;
		minions = new ArrayList<Minion>();
	}
	
	public boolean hasMinions(){
		return minions.size() > 0 ? true : false;
	}
}
