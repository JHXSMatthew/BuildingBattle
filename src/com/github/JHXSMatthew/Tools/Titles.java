package com.github.JHXSMatthew.Tools;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;


public class Titles {
	
	public static void newTitle(Player player, String Title, Integer In, Integer Stay, Integer Out) {
		PlayerConnection playerconnection = ((CraftPlayer)player).getHandle().playerConnection;
		
		PacketPlayOutTitle TitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, In, Stay.intValue(), Out.intValue());
		playerconnection.sendPacket(TitlePacket);
	    
	      IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + Title + "\"}");
	      PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
	      playerconnection.sendPacket(packetPlayOutTitle);
	    
		
	}
	
	public static void newTitle(Player player, String Title, String subTitle, Integer In, Integer Stay, Integer Out) {
		PlayerConnection playerconnection = ((CraftPlayer)player).getHandle().playerConnection;
		
		PacketPlayOutTitle TitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, In, Stay.intValue(), Out.intValue());
		playerconnection.sendPacket(TitlePacket);
	    
	      IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subTitle + "\"}");
	      PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
	      playerconnection.sendPacket(packetPlayOutSubTitle);
	    
	      IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + Title + "\"}");
	      PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
	      playerconnection.sendPacket(packetPlayOutTitle);
	    
		
	}
	
	public static void sendActionBar(Player p, String msg) {
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + msg + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
	}
}