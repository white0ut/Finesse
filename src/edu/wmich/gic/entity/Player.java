package edu.wmich.gic.entity;

import java.util.ArrayList;
import java.util.List;

public class Player {
	
	// Public variable
	public int points;
	private List<Minion> minions;
	
	
	public Player(){
		minions = new ArrayList<Minion>();
	}
	
	public boolean hasMinions(){
		return minions.size() > 0 ? true : false;
	}
}
