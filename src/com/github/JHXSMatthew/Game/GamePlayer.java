package com.github.JHXSMatthew.Game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.WeatherType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import com.github.JHXSMatthew.BuildingBattle;
import com.github.JHXSMatthew.setUp.PlayerData;

public class GamePlayer {
	
	private Player p;
	private Game g = null;
	
	private boolean isInQ = false;
	
	private PlayerData pd ;
	
	private List<Block> placedBlock;
	
	private GamePlot currentPlot=null;
	private int voteMark=1;
	private Objective obj;
	
	public boolean falseQuit = false;
	
	public GamePlayer(Player subject ){

		p = subject;
		
		try{
			pd = BuildingBattle.getSQL().loadPlayerData(p.getName());
		}catch(Exception e){
			e.printStackTrace();
			System.out.print("Error to load player data");
		}
		
		
		placedBlock = new ArrayList<Block>();
		clearBuffs();
		setDefault();

	}
	
	public void savePD(){
		BuildingBattle.getSQL().savePlayerData(pd);
	}
	
	public PlayerData getPD(){
		return pd;
	}
	public Game getGame(){
		return g;
	}
	public void setGame(Game arg){
		g = arg;
		if(arg != null){
			BuildingBattle.getIc().giveLobbyItem(this);
		}
	}
	
	public boolean isinQ(){
		return isInQ;
	}
	
	public void setQ(boolean arg){
		isInQ = arg;
	}
	
	public Player get(){
		return p;
		
	}
	
	public void setPlot(GamePlot plot){
		currentPlot = plot;
	}
	
	public void teleportCenter(){
		p.teleport(currentPlot.getCenter());
	}



	public void addBlock(Block b){
		placedBlock.add(b);
	}
	
	public void addBlock(List<BlockState> b){
		for(BlockState temp : b){
			placedBlock.add(temp.getBlock());
		}
	
	}
		
	public void removeBlock(Block b){
		placedBlock.remove(b);
	}
	
	public boolean isBreakable(Block b){
		return placedBlock.contains(b);
	}
	

	
	public void teleportToPlot(GamePlot p){
		this.p.teleport(p.getTelePoint());
	}
	
	public GamePlot getPlot(){
		return this.currentPlot;
	}
	
	
	public void setDefault(){
		currentPlot = null;
		placedBlock.clear();
		voteMark = 0;
		g = null;
		isInQ=false;
		
		
		obj = Bukkit.getScoreboardManager().getNewScoreboard().registerNewObjective("board", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(BuildingBattle.getMsg().getMsg("score-board-title"));
		
		Score score = obj.getScore("胜利: " + pd.getWins() );
		
		score.setScore(5);
		score = obj.getScore("场次: " + pd.getGames() );
		score.setScore(6);
		score = obj.getScore("给出均分: " + pd.getAverageGive()) ;
		score.setScore(4);
		score = obj.getScore("得到均分: " + pd.getAverageRecived());
		score.setScore(3);
		score = obj.getScore(" " );
		score.setScore(2);
		score = obj.getScore(ChatColor.YELLOW + "www.mcndsj.com" );
		score.setScore(1);
		p.setLevel(0);
		
		p.setScoreboard(obj.getScoreboard());
		
		BuildingBattle.getIc().giveLobbyItem(this);
		p.teleport(BuildingBattle.getQc().getQlobby());
		
		
	}
	
	
	public void clearBuffs(){
		p.setGameMode(GameMode.ADVENTURE);
		p.setHealth(p.getMaxHealth());
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setAllowFlight(false);
		p.setFlying(false);
		p.setLevel(0);
		for(PotionEffect pf : p.getActivePotionEffects()){
			p.removePotionEffect(pf.getType());
		}
		
	}
	public void setBuildMode(){
		p.setGameMode(GameMode.CREATIVE);
		p.setHealth(p.getMaxHealth());
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setAllowFlight(true);
		p.setFlying(true);
		p.setLevel(0);
	
	}
	
	public void setVoteMode(){
		try{
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			
			p.setGameMode(GameMode.ADVENTURE);
			p.setAllowFlight(true);
			p.setFlying(true);
			voteMark =1;
			if(getPlot().getWeather() == 3){
				getPlot().setPersonalPacketEnd();
			}
			
			p.setPlayerTime(g.getCurrentView().getPlot().getTime(), false);
			if((g.getCurrentView().getPlot().getWeather() != 0)){
				p.setPlayerWeather(WeatherType.DOWNFALL);
			}else{
				p.setPlayerWeather(WeatherType.CLEAR);
			}
		
			BuildingBattle.getIc().giveVoteItems(this);
			
		}catch(Exception e){
			e.printStackTrace();
			if(p != null){
				System.out.print(p.getDisplayName());
			}
		}
	}
	
	public int getVotedMark(){
		return voteMark ;
	}

	public void setVoted(int arg){
		this.voteMark = arg;
	}
	
}
