package com.github.JHXSMatthew.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.util.Vector;

import com.github.JHXSMatthew.BuildingBattle;
import com.github.JHXSMatthew.Tools.Titles;

public class Game {

	private String name;
	private int number;
	private Location up;
	private Location down;
	private int player ;
	//time is in seconds
	private int time;
	private Location lobby;

	private ArrayList<GamePlayer> inGame ;
	private HashMap<GamePlayer, Integer > markMap;


	private int size=35;

	private int height = 14;

	//0 = watting
	//1 = starting
	//2 = Building
	//3 = rating
	//4 = Finish
	//-1 = error


	private String subject = null;
	private short gameState=0;
	private GamePlayer currentView = null;
	private  GamePlayer winner= null;

	private int fireworkCount = 0;

	private int timeLeft_buffer = 300;

	private BukkitTask currentTask=null;
	private Objective objective;

	private List<GamePlot> plot;

	public void self_Dispose(){
		inGame = null;
		markMap = null;
		plot = null;
	}


	public Game(String name,int i,Location upt, Location downt,Location lobbyt, int p , int t) {
		this.name = name;
		number = i;
		up = upt.clone();
		down = downt.clone();
		lobby = lobbyt.clone();
		player = p ;
		time = t;
		plot = new ArrayList<GamePlot>();
		inGame = new ArrayList<GamePlayer>();
		for(int i1= 0 ; i1 < player ; i1 ++){
			plot.add(new GamePlot(this,i1));
		}

		subject = BuildingBattle.getSc().getRanSub();
		markMap = new HashMap<GamePlayer, Integer>();


		//	System.out.print("up" + up.toString());
		//	System.out.print("down" + down.toString());

	}

	/*
	 * pre-Condition : 1.Not full people 2.In state 0
	 *
	 * post-Condition : 1. scoreBoard updated 2.Player join game 3.GameStart check 4. message
	 *
	 *
	 */
	public void GameJoin(GamePlayer p ){
		if(gameState > 1){
			p.get().kickPlayer("Error join not Correct State");
			return;
		}
		if(inGame.size() >= player){
			p.get().kickPlayer("Error full people join");
			return;
		}


		p.setQ(false);
		inGame.add(p);
		allMsg( ChatColor.YELLOW+  p.get().getName()+ BuildingBattle.getMsg().getMsg("player-join-msg") + ChatColor.AQUA+" ("+inGame.size()+"/"+ player+ ")") ;
		p.setGame(this);
		p.get().teleport(lobby);
		if(inGame.size() >= 0.5 * player && gameState == 0){
			switchState((short)1);
		}

		Titles.newTitle(p.get(), ChatColor.GREEN + "建筑战争" ,ChatColor.GRAY+ "www.mcndsj.com", 10, 30, 10);
		upDateScoreBoard();

	}

	public int getPlayerInGame(){
		return inGame.size();
	}

	public void GameQuit(GamePlayer p){
		if(gameState == 4){
			p.setGame(null);
			p.clearBuffs();
			p.setDefault();
			Titles.newTitle(p.get(), ChatColor.GREEN + "建筑战争" ,ChatColor.GRAY+ "www.mcndsj.com", 10, 30, 10);
			return;
		}else{
			switch(gameState){
				case 0 :
					inGame.remove(p);
					upDateScoreBoard();
					p.setDefault();
					break;
				case 1 :
					inGame.remove(p);
					upDateScoreBoard();
					p.setDefault();
					break;
				case 2 :
					inGame.remove(p);
					p.getPlot().setOwner(null);
					upDateScoreBoard();
					p.setDefault();
					break;
				case 3 :
					if(currentView == p){
						currentTask.cancel();
						markMap.put(p, 0);
						nextVotevotePlayer();
						inGame.remove(p);
						p.getPlot().setOwner(null);
					}else{
						inGame.remove(p);
						p.getPlot().setOwner(null);
					}
					break;
			}

		}

		if(inGame.size() <=1 && gameState > 1){
/*
			if(inGame.size() == 1){
				inGame.get(0).get().teleport(BuildingBattle.getQc().getQlobby());
				inGame.get(0).get().sendMessage(ChatColor.RED + "人数不足，游戏取消。");
				inGame.get(0).setDefault();
			}
*/

			currentTask.cancel();

			for(GamePlayer temp: inGame ){
				temp.setDefault();
				temp.get().teleport(BuildingBattle.getQc().getQlobby());
			}

			BuildingBattle.getGc().disposeGame(number);
			Titles.newTitle(p.get(), ChatColor.GREEN + "建筑战争" ,ChatColor.GRAY+ "www.mcndsj.com", 10, 30, 10);
		}else{
			allMsg( ChatColor.YELLOW +  p.get().getName()+ BuildingBattle.getMsg().getMsg("player-leave-msg") + ChatColor.AQUA+" ("+inGame.size()+"/"+ player+ ")") ;
		}
	}





	private void upDateScoreBoard(){

		objective = Bukkit.getScoreboardManager().getNewScoreboard().registerNewObjective("board", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(BuildingBattle.getMsg().getMsg("score-board-title"));




		if(gameState == 0){
			Score score = objective.getScore(" ");
			score.setScore(6);
			score = objective.getScore("模式: 建筑战争");
			score.setScore(5);
			score = objective.getScore("人数: " + inGame.size() + "/" + player);
			score.setScore(4);

			score = objective.getScore("  ");
			score.setScore(3);

			score = objective.getScore("状态: 等待中...");
			score.setScore(2);

			score = objective.getScore("   ");
			;score.setScore(1);


			score = objective.getScore(ChatColor.YELLOW + "www.mcndsj.com");
			score.setScore(0);


		}

		if(gameState == 1){
			Score score = objective.getScore(" ");
			score.setScore(6);
			score = objective.getScore("地图: 建筑战争");
			score.setScore(5);
			score = objective.getScore("人数: " + inGame.size() + "/" + player);
			score.setScore(4);

			score = objective.getScore(" ");
			score.setScore(3);

			score = objective.getScore("状态: 开始中...");
			score.setScore(2);

			score = objective.getScore("  ");
			;score.setScore(1);


			score = objective.getScore(ChatColor.YELLOW + "www.mcndsj.com");
			score.setScore(0);
		}

		if(gameState == 2){

			int minutes = timeLeft_buffer /60;
			int seconds = timeLeft_buffer %60;
			Score score ;
			if(seconds < 10){

				score = objective.getScore("时间: " + ChatColor.GREEN +  minutes + ":0" + seconds );
			}else{
				score =objective.getScore("时间: " + ChatColor.GREEN +  minutes + ":" + seconds );
			}

			score.setScore(6);
			score = objective.getScore(" ");
			score.setScore(5);

			score = objective.getScore("主题:");
			score.setScore(4);

			score = objective.getScore(ChatColor.GREEN + subject);
			score.setScore(3);

			score = objective.getScore("  ");
			score.setScore(2);

			score = objective.getScore("人数: " + ChatColor.GREEN + inGame.size());
			score.setScore(1);

			score = objective.getScore("   ");
			score.setScore(0);

			score = objective.getScore(ChatColor.YELLOW + "www.mcndsj.com");
			score.setScore(-1);
		}

		if(gameState == 3){


			Score score = objective.getScore("主题:" );
			score.setScore(9);
			score = objective.getScore(ChatColor.GREEN + subject);
			score.setScore(8);

			score = objective.getScore(" ");
			score.setScore(7);

			score = objective.getScore("当前作品: ");
			score.setScore(6);

			score = objective.getScore(ChatColor.GREEN + currentView.get().getName());
			score.setScore(5);

			score = objective.getScore("  ");
			score.setScore(4);

			score = objective.getScore("评分:" );
			score.setScore(3);

			score = objective.getScore(ChatColor.GREEN + BuildingBattle.getMsg().phaseVote(1));
			score.setScore(2);

			score = objective.getScore("   ");
			score.setScore(1);

			score = objective.getScore(ChatColor.YELLOW + "www.mcndsj.com");
			score.setScore(0);

		}

		if(gameState == 4){


			Score score = objective.getScore("主题:" );
			score.setScore(9);
			score = objective.getScore(ChatColor.GREEN + subject);
			score.setScore(8);

			score = objective.getScore(" ");
			score.setScore(7);

			score = objective.getScore("获胜玩家: ");
			score.setScore(6);

			score = objective.getScore(ChatColor.GREEN + winner.get().getName());
			score.setScore(5);

			score = objective.getScore("  ");
			score.setScore(4);

			score = objective.getScore("距离传送:" );
			score.setScore(3);

			score = objective.getScore(ChatColor.GREEN + String.valueOf(timeLeft_buffer));
			score.setScore(2);

			score = objective.getScore("   ");
			score.setScore(1);

			score = objective.getScore(ChatColor.YELLOW + "www.mcndsj.com");
			score.setScore(0);

		}


		for(GamePlayer p : inGame){
			p.get().setScoreboard(objective.getScoreboard());
		}
	}



	public void UpdatevoteIndiScoreboard(GamePlayer p,int mark){
		Objective objective = Bukkit.getScoreboardManager().getNewScoreboard().registerNewObjective("board", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(BuildingBattle.getMsg().getMsg("score-board-title"));

		Score score = objective.getScore("主题:" );
		score.setScore(9);
		score = objective.getScore(ChatColor.GREEN + subject);
		score.setScore(8);

		score = objective.getScore(" ");
		score.setScore(7);

		score = objective.getScore("当前作品: ");
		score.setScore(6);

		score = objective.getScore(ChatColor.GREEN + currentView.get().getName());
		score.setScore(5);

		score = objective.getScore("  ");
		score.setScore(4);

		score = objective.getScore("评分:" );
		score.setScore(3);

		score = objective.getScore(ChatColor.GREEN + BuildingBattle.getMsg().phaseVote(mark));
		score.setScore(2);

		score = objective.getScore("   ");
		score.setScore(1);

		score = objective.getScore(ChatColor.YELLOW + "www.mcndsj.com");
		score.setScore(0);

		p.get().setScoreboard(objective.getScoreboard());
	}




	private void startCount(int t ,short state){
		if(t == 0 || state == -1){
			return;
		}

		if(state == 1){
			currentTask = new BukkitRunnable(){
				int count = t;
				@Override
				public void run() {
					if(count > 0){
						count--;
						if(inGame.size() < 0.5 * player){
							BuildingBattle.getMsg().sendMessage(inGame, "not-enough-people");
							allActionBar_Exp(0,ChatColor.RED + "== 人数不足 ==");
							allSound(Sound.VILLAGER_HIT);
							switchState((short)0);
							cancel();
						}else{
							allActionBar_Exp(count, BuildingBattle.getMsg().getMsg("wait-count-1") + count + BuildingBattle.getMsg().getMsg("wait-count-2"));
							if(count <= 5){
								allTitle(ChatColor.GREEN.toString() + count,null,20);
								allSound(Sound.CLICK);
							}
						}
					}else{
						switchState((short) 2);
						cancel();
					}
				}

			}.runTaskTimer(BuildingBattle.get(), 0, 20);

			return;
		}

		if(state == 2){


			currentTask = new BukkitRunnable(){
				int count = t;
				@Override
				public void run() {
					if(count > 0){
						count--;
						if(count == 120){
							allSound(Sound.CLICK);
							allTitle(null, ChatColor.RED + "建筑时间还剩 120 秒", 20);
						}

						if(count == 60){
							allSound(Sound.CLICK);
							allTitle(null, ChatColor.RED + "建筑时间还剩 60 秒", 20);
						}


						if(count == 30){
							allSound(Sound.CLICK);
							allTitle(null, ChatColor.RED + "建筑时间还剩 30 秒", 20);
						}


						if(count  <= 10){
							allSound(Sound.CLICK);
							allTitle(null, ChatColor.RED + "建筑时间还剩 "+  count+ " 秒", 20);
						}
						timeLeft_buffer = count;
						upDateScoreBoard();
						/*
						for(GamePlayer p : inGame){
							if(p.getPlot().getWeather() < 2){
								continue;
							}

							BuildingBattle.get().packet.refreshChunk(up.getWorld(), p.get().getLocation().getChunk(),p.get());
						}
						*/

					}else{
						switchState((short) 3);
						cancel();
					}
				}

			}.runTaskTimer(BuildingBattle.get(), 0, 20);

			return;
		}


		if(state == 3){

			currentTask = new BukkitRunnable(){
				int count = t;

				@Override
				public void run() {


					if(count > 0){
						count --;
						allActionBar_Exp(count,ChatColor.RED + ChatColor.BOLD.toString() + "==本次投票还剩 " + count + " 秒==");

				/*		if(currentView.getPlot().getWeather() < 2){
							for(GamePlayer p : inGame){
								BuildingBattle.get().packet.refreshChunk(up.getWorld(), p.get().getLocation().getChunk(),p.get());
							}
						}
		*/

					}else{

						calcMark();
						nextVotevotePlayer();


						cancel();
					}
				}

			}.runTaskTimer(BuildingBattle.get(), 0, 20);

			return;

		}

		if(state == 4){

			new BukkitRunnable(){
				int count = t;
				@Override
				public void run() {
					if(count > 0){
						count --;
						timeLeft_buffer = count;
						upDateScoreBoard();
						allActionBar_Exp(count,ChatColor.RED + ChatColor.BOLD.toString() + "==距离传送还剩 " + count + " 秒==");

							/*
							if(winner.getPlot().getWeather() < 2){
								for(GamePlayer p : inGame){
									BuildingBattle.get().packet.refreshChunk(up.getWorld(), p.get().getLocation().getChunk(),p.get());
								}
							}
							*/

					}else{
						for(GamePlayer p : inGame){
							GameQuit(p);
						}
						for(Player temp : up.getWorld().getPlayers()){
							GamePlayer gp = BuildingBattle.getPc().getPlayer(temp);
							GameQuit(gp);
						}

						BuildingBattle.getGc().disposeGame(number);
						cancel();
					}
				}

			}.runTaskTimer(BuildingBattle.get(), 0, 20);

			return;

		}

	}



	private void calcMark(){
		int sum = 0;
		for(GamePlayer p : inGame){
			if(!p.equals( currentView)){
				sum += p.getVotedMark();
				currentView.getPD().AddReceived(p.getVotedMark());
				p.getPD().AddGive(p.getVotedMark());
				p.setVoted(0);
			}
		}

		markMap.put(currentView, sum);

	}

	private void nextVotevotePlayer(){
		GamePlayer player = null;
		for(GamePlayer temp : inGame){
			if(!markMap.containsKey(temp)){
				player = temp;
				break;
			}
		}
		if(player == null){
			switchState((short)4);
			return;
		}

		currentView = player;
		markMap.put(player, 0);

		//lightning weather
		if(currentView.getPlot().getWeather() == 3){
			final GamePlayer record = currentView;
			Location l =currentView.getPlot().getCenter();
			Random r = new Random();
			new BukkitRunnable(){

				@Override
				public void run() {
					if(!currentView.equals(record) || gameState != 3){
						cancel();
					}

					l.getWorld().strikeLightningEffect(new Location(l.getWorld(),l.getX() + r.nextInt(10) - 5 , l.getY() - 5 , l.getZ() + r.nextInt(10) -5 ));
				}
			}.runTaskTimer(BuildingBattle.get(), 0, 60);
		}

		// teleport + other  weather
		List<Chunk> relevant = currentView.getPlot().getRelevantChunks();
		for(GamePlayer p : inGame){
			p.teleportToPlot(currentView.getPlot());
			p.setVoteMode();
			for(Chunk c : relevant ){
				c.getWorld().setStorm(true);
				c.getWorld().setThundering(true);
				c.getWorld().setThunderDuration(Integer.MAX_VALUE);
				c.getWorld().setWeatherDuration(Integer.MAX_VALUE);
			}




		}

		startCount(20,(short)3);
		upDateScoreBoard();
	}

	public void doMarkChange(GamePlayer p,int mark, boolean first){

	/*
		if(p == currentView){
			p.setVoted(true);
			return;
		}
		if(markMap.containsKey(currentView)){
			markMap.put(currentView, markMap.get(currentView) + mark);
		}else{
			markMap.put(currentView,  mark);
		}

		p.setVoted(true);
		*/

		UpdatevoteIndiScoreboard(p,mark);
		if(first){
			p.get().sendMessage(BuildingBattle.getMsg().getMsg("give-mark-1") + BuildingBattle.getMsg().phaseVote(mark) + BuildingBattle.getMsg().getMsg("give-mark-2"));
		}else{
			p.get().sendMessage(BuildingBattle.getMsg().getMsg("change-mark-1") + BuildingBattle.getMsg().phaseVote(mark)  + BuildingBattle.getMsg().getMsg("give-mark-2"));
		}




		switch(mark){
			case 1:
				p.get().playSound(p.get().getLocation(), Sound.CAT_MEOW, 1F,  0.22F);
				break;
			case 2:
				p.get().playSound(p.get().getLocation(), Sound.CAT_MEOW, 1F,  0.25F);
				break;
			case 3:
				p.get().playSound(p.get().getLocation(), Sound.CAT_MEOW, 1F,  0.5F);
				break;
			case 4:
				p.get().playSound(p.get().getLocation(), Sound.CAT_MEOW, 1F,  1F);
				break;
			case 5:
				p.get().playSound(p.get().getLocation(), Sound.CAT_MEOW, 1F,  1.5F);
				break;
			case 6:
				p.get().playSound(p.get().getLocation(), Sound.CAT_MEOW, 1F,  2F);
				break;
		}
	}


	public void switchState(short to){
		/*
		if(this.gameState - to  != -1){
			System.out.print("Error");
			return;
		}
		*/

		if(to == 0){
			gameState =0;
			upDateScoreBoard();
			return;
		}

		if(to == 1){
			gameState = 1;
			startCount(30,to);
		}

		if(to == 2){
			int temp = 0;
			gameState = 2;
			for(GamePlot area : plot){
				if(temp < inGame.size()){
					GamePlayer p = inGame.get(temp);
					//System.out.print(p.get().getDisplayName());
					area.setOwner(p);
					p.setPlot(area);
					p.teleportCenter();
					p.setBuildMode();
					Titles.newTitle(p.get(), subject, time/60 +"分钟建筑时间开始，请按照主题建筑", 20, 60, 20);
				}else{
					area.setOwner(null);
				}
				temp ++;
			}

			for(GamePlayer p : inGame){
				p.get().setPlayerWeather(WeatherType.CLEAR);
				p.get().setPlayerTime(0, true);
				BuildingBattle.getIc().giveGameBeginItem(p);

			}
			startCount(time,to);

		}

		if(to == 3){
			gameState = 3;
			allSound(Sound.LEVEL_UP);
			nextVotevotePlayer();
			upDateScoreBoard();

		}

		if(to == 4){
			gameState = 4;

			ArrayList<Entry<GamePlayer, Integer>> entryList = new ArrayList<Map.Entry<GamePlayer, Integer>>(markMap.entrySet());
			Collections.sort(entryList, new Comparator<Map.Entry<GamePlayer,Integer>>(){
				@Override
				public int compare(Entry<GamePlayer, Integer> o1,
								   Entry<GamePlayer, Integer> o2) {
					return o1.getValue().compareTo(o2.getValue());
				}
			});

			Collections.reverse(entryList);

			winner = entryList.get(0).getKey();
			currentView = winner;
			int i = 0;
			for(Map.Entry<GamePlayer,Integer> et : entryList){
				GamePlayer p = et.getKey();
				p.getPD().AddGames();


				System.out.print( entryList.get(i).getKey().get().getName() + " , " +entryList.get(i).getValue());
				p.teleportToPlot(winner.getPlot());
				p.get().setPlayerTime(winner.getPlot().getTime(), false);
				switch(getCurrentView().getPlot().getWeather()){
					case 0:
						p.get().setPlayerWeather(WeatherType.CLEAR);
						break;
					case 1:
						p.get().setPlayerWeather(WeatherType.DOWNFALL);
						break;
					case 3:
						p.get().setPlayerWeather(WeatherType.DOWNFALL);
						break;
				}
				p.get().sendMessage(ChatColor.YELLOW +"====================游 戏 结 束====================");
				p.get().sendMessage(ChatColor.YELLOW +"                                             ");
				p.get().sendMessage(ChatColor.YELLOW +"            第一名 "+winner.get().getDisplayName() +  "("+ entryList.get(0).getValue() + ")                          ");
				if(inGame.size() >= 3){
					p.get().sendMessage(ChatColor.YELLOW +"            第二名 "+ entryList.get(1).getKey().get().getDisplayName()+  "("+ entryList.get(1).getValue() + ")                                  ");
					p.get().sendMessage(ChatColor.YELLOW +"            第三名   " + entryList.get(2).getKey().get().getDisplayName() +"("+ entryList.get(2).getValue() + ")                              ");

				}else if(inGame.size() >= 2){
					p.get().sendMessage(ChatColor.YELLOW +"            第二名 "+ entryList.get(1).getKey().get().getDisplayName() + "("+ entryList.get(1).getValue() + ")                             ");
				}else{
					p.get().sendMessage(ChatColor.YELLOW +"                                             ");
				}
				p.get().sendMessage(ChatColor.YELLOW + "            您是第 "+ (entryList.indexOf(et) +1)+" 名  ("+ et.getValue()+")                    ");
				p.get().sendMessage(ChatColor.YELLOW +"                                             ");
				p.get().sendMessage(ChatColor.YELLOW +"==================您将在10秒内传送回大厅===================");

				i++;
			}
			if(BuildingBattle.economy != null){
				try{
					BuildingBattle.economy.depositPlayer(Bukkit.getOfflinePlayer(winner.get().getUniqueId()), null, 2);
				}catch (Exception e){
					Bukkit.getLogger().info("EXCEPTION WHILE DEPOSIT TO ID : " + winner.get().getName());
					e.printStackTrace();
				}
				winner.get().sendMessage(ChatColor.DARK_GREEN + "YourCraft >> " + ChatColor.WHITE + "恭喜您积极参与游戏，获得金币奖励 "+ChatColor.GOLD +  "2.00 " + ChatColor.WHITE + " 枚!");
			}

			gameFinish(winner);
			startCount(10,(short) 4);
		}





	}


	private void gameFinish(GamePlayer winner){
		winner.getPD().AddWins();
		launchFireworkDisplay(winner.getPlot().getCenter().getWorld(), winner.getPlot().getCenter());

	}
	public boolean isAvaliable(){
		if(gameState <= 1 && inGame.size() < player){
			return true;
		}

		return false;
	}

	public void launchFireworkDisplay(final World w, final Location loc) {
		Firework fw = (Firework) w.spawn(loc.clone().add(new Vector(getRandomNum(5, -5), 1, getRandomNum(5, -5))), Firework.class);
		FireworkMeta meta = fw.getFireworkMeta();
		FireworkEffect effect = BuildingBattle.getNms().getFireworkEffect(getRandomColor(),getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomType());
		meta.addEffect(effect);
		meta.setPower(getRandomNum(4, 1));
		fw.setFireworkMeta(meta);
		fireworkCount++;
		if (fireworkCount < 20) {
			new BukkitRunnable() {
				public void run() {
					launchFireworkDisplay(w, loc);
				}
			}.runTaskLater(BuildingBattle.get(), 5 );
		}
	}

	public Type getRandomType() {
		int type = getRandomNum(5, 1);
		switch (type) {
			case 1: return Type.STAR;
			case 2: return Type.CREEPER;
			case 3: return Type.BURST;
			case 4: return Type.BALL_LARGE;
			case 5: return Type.BALL;
			default: return Type.STAR;
		}
	}

	public int getState(){
		return gameState;
	}
	public GamePlayer getCurrentView(){
		return currentView;
	}

	public Color getRandomColor() {
		int color = getRandomNum(17, 1);
		switch (color) {
			case 1: return Color.AQUA;
			case 2: return Color.BLACK;
			case 3: return Color.BLUE;
			case 4: return Color.FUCHSIA;
			case 5: return Color.GRAY;
			case 6: return Color.GREEN;
			case 7: return Color.LIME;
			case 8: return Color.MAROON;
			case 9: return Color.NAVY;
			case 10: return Color.OLIVE;
			case 11: return Color.ORANGE;
			case 12: return Color.PURPLE;
			case 13: return Color.RED;
			case 14: return Color.SILVER;
			case 15: return Color.TEAL;
			case 16: return Color.WHITE;
			case 17: return Color.YELLOW;
			default: return Color.RED;
		}
	}

	public int getRandomNum(int max, int min) {
		Random rand = new Random();
		int ii = min + rand.nextInt(((max - (min)) + 1));
		return ii;
	}



	public Location getUp(){
		return up;
	}

	public Location getDown(){
		return down;
	}

	public int getPlotSize(){
		return this.size;
	}
	public int getHeight(){
		return this.height;
	}

	public boolean isOutOfHeight(double h){
		if(h > up.getY() + 3 || h < down.getY() - 3){
			return true;
		}

		return false;
	}



	public String getname() {

		return name;
	}



	private void allTitle(String msg, String sub,int t){
		if(sub == null){
			for(GamePlayer p : this.inGame){
				Titles.newTitle(p.get(), msg, 10, t, 30);
			}
		}else if(msg == null){
			for(GamePlayer p : this.inGame){
				Titles.newTitle(p.get(), " ", sub, 10, t, 40);
			}
		}else{
			for(GamePlayer p : this.inGame){
				Titles.newTitle(p.get(), msg, sub, 10, t, 40);
			}
		}
	}




	public void allMsg(String msg){
		for(GamePlayer p : this.inGame){
			p.get().sendMessage(msg);
		}
	}

	private void allSound(Sound s){
		for(GamePlayer p : this.inGame){
			p.get().playSound(p.get().getLocation(), s, 1, 1);

		}
	}


	private void allActionBar_Exp(int level,String msg){
		for(GamePlayer p : this.inGame){
			p.get().setLevel(level);
			Titles.sendActionBar(p.get(), msg);

		}
	}


}


