package com.github.JHXSMatthew.Objects;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;

import com.github.JHXSMatthew.Game.GamePlayer;

public class Message {

	String prefix = ChatColor.AQUA + "YourCraft >> ";
	HashMap<String,String> msg ;
	
	public Message(){
		msg = new HashMap<String,String>();
		
		
		msg.put("ri-ni-da-ye", prefix + "我是你大爷");
		msg.put("score-board-title", ChatColor.YELLOW + ChatColor.BOLD.toString() + "建筑战争");
		msg.put("not-enough-people", prefix + "人数不足.");
		msg.put("wait-count-1", ChatColor.GREEN + ChatColor.BOLD.toString()+ "== 距离游戏开始 ");
		msg.put("wait-count-2", " 秒 ==");
		msg.put("player-leave-msg", ChatColor.YELLOW + " 离开游戏");
		msg.put("player-join-msg", ChatColor.YELLOW + " 加入游戏");
		msg.put("cant-get-out-bound", prefix + ChatColor.RED +  "请您不要尝试脱离正常游戏范围！");
		msg.put("give-mark-1", prefix + ChatColor.AQUA +  "您给出了 "  + ChatColor.RED );
		msg.put("change-mark-1", prefix + ChatColor.AQUA +  "您改成了 "  + ChatColor.RED );
		msg.put("give-mark-2",    ChatColor.AQUA + " 评价!");
		msg.put("could-not-self-vote", prefix + ChatColor.YELLOW + "您无法为自己的作品投票");
	}
	
	public void sendMessage(List<GamePlayer> p, String num){
		for(GamePlayer temp : p){
			temp.get().sendMessage(msg.get(num));
		}
	}
	public String getMsg(String s){
		return this.msg.get(s);
	}
	
	public String phaseVote(int vote){
		switch(vote){
		case 1:
			return "普通";
		case 2:
			return "及格";
		case 3:
			return "良好";
		case 4:
			return "优秀";
		case 5:
			return "绝佳";
		case 6:
			return "超神";
		default:
			return "尚未投票";
		}
	}
	

}
