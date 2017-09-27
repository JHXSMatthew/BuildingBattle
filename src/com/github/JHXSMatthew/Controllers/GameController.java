package com.github.JHXSMatthew.Controllers;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.JHXSMatthew.BuildingBattle;
import com.github.JHXSMatthew.Game.Game;
import com.github.JHXSMatthew.Game.GamePlayer;
import com.github.JHXSMatthew.setUp.Map;


public class GameController {
	
	private ConcurrentHashMap<Integer,Game> gameHash;
//	private ArrayList<Game> gameList;
	public int count = 0;
	
	public  GameController(){
		gameHash = new ConcurrentHashMap<Integer,Game>();
		new BukkitRunnable(){

			@Override
			public void run() {
				Iterator<Entry<Integer, Game>> ilt = gameHash.entrySet().iterator();
				Bukkit.getLogger().info("== Chck Game Begin ==");
				while(ilt.hasNext()){
					Entry<Integer, Game> entry = ilt.next();
					Bukkit.getLogger().info("|_ Game: " + entry.getValue().getname() +entry.getKey());
					Bukkit.getLogger().info("  |_ Players: " + entry.getValue().getPlayerInGame());
					Bukkit.getLogger().info("  |_ States: " + entry.getValue().getState());
					Bukkit.getLogger().info("  |_ IsWorldLoaded: " + Bukkit.getWorlds().contains(entry.getValue().getDown().getWorld()));
					
				}
				
				Bukkit.getLogger().info("== Chck loadedWorlds ==");
				for(World w : Bukkit.getWorlds()){
					Bukkit.getLogger().info(w.getName());

				}
				
				Bukkit.getLogger().info("_______ Task Finish ______");
				
			}
			
		}.runTaskTimer(BuildingBattle.get(), 0, 20 * 60 * 5);
		
//		gameList = new ArrayList<Game>();
	}
	
	public void disposeAllGame(){
		Iterator<java.util.Map.Entry<Integer, Game>> iter = gameHash.entrySet().iterator();
		while(iter.hasNext()){
			java.util.Map.Entry<Integer, Game> entry = iter.next();
			System.out.print("Deleting world " + gameHash.get(entry.getKey()).getname()+entry.getKey() + "!");
			BuildingBattle.getWc().deleteWorld(gameHash.get(entry.getKey()).getname()+entry.getKey());
			
			iter.remove();

		}
	}
	
	public void disposeGame( int num ){
		
		System.out.print("Deleting world " + gameHash.get(num).getname()+num + "!");
		
		BuildingBattle.getWc().deleteWorld(gameHash.get(num).getname()+num);
		gameHash.get(num).self_Dispose();
		gameHash.remove(num);
		
		
	}
	
	
	
	public boolean JoinGame(GamePlayer p){
		Iterator<java.util.Map.Entry<Integer, Game>> iter = gameHash.entrySet().iterator();
		while(iter.hasNext()){
			java.util.Map.Entry<Integer, Game> entry = iter.next();
			if(!entry.getValue().isAvaliable()) continue;
			if(p != null) {
				entry.getValue().GameJoin(p);
				return true;
			}
		}
		if(grabNewGame()){
			JoinGame(p);
			return true;
		}
		
		
		return false;
	}
	
	public boolean grabNewGame(){
		
		for(int i =0 ; i < Integer.MAX_VALUE ; i ++){
		
			if(!gameHash.containsKey(i)){
				Map temp = BuildingBattle.getMc().getranMap(i);
				
				gameHash.put(i, new Game(temp.name,i,temp.up,temp.down,temp.lobby,temp.Player,temp.time));
				
				count ++;
				return true;
			}
			//System.out.print(i);
		}
		return false;
	}
	
	public void setMessageTo(Game g,String msg){
	
		if(g==null){
			BuildingBattle.getPc().setMessageTo(msg);
			BuildingBattle.getlog().info("����->" + msg.replaceAll("��.", ""));
		}else{
			g.allMsg(msg);
			BuildingBattle.getlog().info(g.getname() + "->"+ msg.replaceAll("��.", ""));
		}
		
	}
	

}
