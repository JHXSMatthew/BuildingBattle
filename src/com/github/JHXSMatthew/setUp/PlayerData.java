package com.github.JHXSMatthew.setUp;

import java.util.List;

public class PlayerData {

	private String name;
	private List<Integer> giveMarks;
	private List<Integer> receivedMarks;
	private int win;
	private int games;
	private boolean first;
	private boolean modified = false;

	
	public PlayerData(String name,List<Integer> give, List<Integer> recived , int win,int games,boolean fb){
		this.name = name;
		this.win = win;
		this.games = games;
		giveMarks = give;
		receivedMarks = recived;
		first = fb;
	}

	public double getAverageRecived() {
		return getAverage(receivedMarks);
	}

	public double getAverageGive(){
		return getAverage(giveMarks);
	}

	private double getAverage(List<Integer> list){
		int returnValue = 0;
		int total = 0;
		try{
			int count = 1;
			for(int i : receivedMarks){
				returnValue += i * count;
				total += i;
				count ++;
			}
		}catch(Exception e){

			return 0;
		}
		if(total == 0){
			return 0;
		}
		return Math.round(returnValue/total * 100) * 0.01;
	}

	public boolean isNewPlayer(){
		return first;
	}

	public int getWins(){
		return win;
	}
	public int getGames(){
		return games;
	}
	
	public float getRate(){
		return win/games;
	}
	
	public void AddWins(){
		win ++;
		modified = true;
	}
	
	public void AddGames(){
		games ++;
		modified = true;
	}
	public boolean isModified(){
		return modified;
	}
	
	public void AddGive (int Type){
		giveMarks.set(Type - 1, giveMarks.get(Type - 1) + 1);
		modified = true;
	}
	public void AddReceived(int Type){
		receivedMarks.set(Type - 1, receivedMarks.get(Type - 1) + 1);
		modified = true;
	}
	
	public String getName(){
		return name;
	}
	
	public int getGiveValue(int type){
		return giveMarks.get(type-1);
	}
	public int getReceivedValue(int type){
		return receivedMarks.get(type-1);
	}
	
}

