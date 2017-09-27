package com.github.JHXSMatthew.Controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.github.JHXSMatthew.setUp.PlayerData;
import com.huskehhh.mysql.mysql.MySQL;

public class MySQLController {

	private Connection c =null;
	private MySQL my;
	private static String TABLENAME  = "BuildingBattle";
	
	public MySQLController(){
	    this.my = new MySQL("192.168.123.2", "3306", "games", "game", "NO_PUBLIC_INFO");
	}
	
	public void openConnection(){
	    try {
			c = my.openConnection();
		} catch (ClassNotFoundException e) {
			System.out.print("Connection error !");
			e.printStackTrace();
		} catch (SQLException e1) {
			System.out.print("Connection error !");
			e1.printStackTrace();
		}
	}
	
	
	public void clsoeConnection() throws SQLException{
		this.c.close();
	}
	
	
	
	public void closeDB() throws SQLException{
		this.my.closeConnection();
	}
	
	public PlayerData loadPlayerData(String name) throws SQLException, ClassNotFoundException{
		if(!this.my.checkConnection()){
			this.c = this.my.openConnection();
		}
		Statement s = this.c.createStatement();
		ResultSet result = s.executeQuery("SELECT * FROM `" + TABLENAME + "` Where `Name`='"+name+"';");
		PlayerData current = null;
		if(result.next()){
			List<Integer> give = new ArrayList<Integer>();
			List<Integer> received = new ArrayList<Integer>();
			
			for(int i = 1 ; i < 7 ; i ++){
				try{
					give.add(result.getInt("G"+i));
					received.add(result.getInt("R"+i));
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
			int win = 0;
			int games = 0;
			try{
				win = result.getInt("Win");
				games = result.getInt("Games");
			}catch(SQLException e){
				e.printStackTrace();
			}
			current = new PlayerData(name,give,received,win,games,false);
		}else{
			List<Integer> give = new ArrayList<Integer>();
			List<Integer> received = new ArrayList<Integer>();
			
			for(int i = 1 ; i < 7 ; i ++){
				give.add(0);
				received.add(0);	
			}
			current = new PlayerData(name,give,received,0,0,true);
		}
		
		s.close();
		result.close();
		s=null;
		
		return current;
		
	}
	public boolean savePlayerData(PlayerData data){
		try{
			String name = data.getName();
			Statement s = this.c.createStatement();
			if(data.isNewPlayer()){
				s.executeUpdate("INSERT INTO `"+ TABLENAME +"` (`Name`,`Games`,`Win`,`G1`,`G2`,`G3`,`G4`,`G5`,`G6`,`R1`,`R2`,`R3`,`R4`,`R5`,`R6`)"
						+ " VALUES ('"+name+"','"+ data.getGames()  +"','"+data.getWins() +"','"+data.getGiveValue(1) +"','" + data.getGiveValue(2) + "','" + data.getGiveValue(3) + "','" + data.getGiveValue(4) 
						+ "','" + data.getGiveValue(5) + "','" + data.getGiveValue(6) +"','" + data.getReceivedValue(1) + "','" +data.getReceivedValue(2) + "','" + data.getReceivedValue(3) 
						+ "','" + data.getReceivedValue(4) + "','" + data.getReceivedValue(5) + "','" + data.getReceivedValue(6) + "');");
			}else{
				if(data.isModified()){

					s.executeUpdate("UPDATE `"+ TABLENAME +"` SET `Games`='"+data.getGames() +"',`Win`='"+data.getWins()
							+"',`G1`='"+ data.getGiveValue(1) 
							+"',`G2`='"+ data.getGiveValue(2)
							+"',`G3`='"+ data.getGiveValue(3) 
							+"',`G4`='"+ data.getGiveValue(4)
							+"',`G5`='"+ data.getGiveValue(5)
							+"',`G6`='"+ data.getGiveValue(6)
							+"',`R1`='"+ data.getReceivedValue(1)
							+"',`R2`='"+ data.getReceivedValue(2)
							+"',`R3`='"+ data.getReceivedValue(3)
							+"',`R4`='"+ data.getReceivedValue(4)
							+"',`R5`='"+ data.getReceivedValue(5)
							+"',`R6`='"+ data.getReceivedValue(6)
							+ "' Where `Name`='"+name+"';");
					

					/*
					s.executeUpdate("UPDATE `"+ TABLENAME +"` (`Games`,`Win`,`G1`,`G2`,`G3`,`G4`,`G5`,`G6`,`R1`,`R2`,`R3`,`R4`,`R5`,`R6`)"
							+ " VALUES ('"+ data.getGames() +"','"+data.getGiveValue(1) +"','" + data.getGiveValue(2) + "','" + data.getGiveValue(3) + "','" + data.getGiveValue(4) 
							+ "','" + data.getGiveValue(5) + "','" + data.getGiveValue(6) +"','" + data.getReceivedValue(1) + "','" +data.getReceivedValue(2) + "','" + data.getReceivedValue(3) 
							+ "','" + data.getReceivedValue(4) + "','" + data.getReceivedValue(5) + "','" + data.getReceivedValue(6) + "') Where `Name`='"+ data.getName()+ "';");
					//s.executeUpdate("UPDATE `expControl` SET `Amount`='"+amount +"' Where `Name`='"+name+"';");
					  */
					 
				}
			}
			
			s.close();
			s = null;
			return true;
		}catch(SQLException e){
			
			e.printStackTrace();
			return false;
		}
		
	}
	
	public float getUser(String name) throws ClassNotFoundException, SQLException{
		if(!this.my.checkConnection()){
			this.c = this.my.openConnection();
		}
		Statement s = this.c.createStatement();
		ResultSet result = s.executeQuery("SELECT * FROM `expControl` Where `Name`='"+name+"';");
		if(result.next()){
			float i = result.getFloat("amount");
			s.close();
			result.close();
			return i;
			
		}else{
			return -1;
		}
	
	}
	
	

	
	public void deletePlayer(String name) throws ClassNotFoundException, SQLException{
		if(!this.my.checkConnection()){
			this.c = this.my.openConnection();
		}
		Statement s = this.c.createStatement();
		s.executeUpdate("DELETE FROM `expControl` Where `Name`='"+name+"';");
		s.close();
		s=null;
	}
	
	public void updatePlayer(String name, float amount) throws ClassNotFoundException, SQLException{
		if(!this.my.checkConnection()){
			this.c = this.my.openConnection();
		}
		Statement s = this.c.createStatement();
		float i = getUser(name);
		
		if(i == -1){
			s.executeUpdate("INSERT INTO `expControl` (`Name`,`Amount`) VALUES ('"+name+"','"+ amount + "');");
		}else{
			if(i != Bukkit.getPlayer(name).getExp()){
				s.executeUpdate("UPDATE `expControl` SET `Amount`='"+amount +"' Where `Name`='"+name+"';");
			}
			
		}
		
		s.close();
		s=null;
	}

	public int getWhackScore(String name) throws ClassNotFoundException, SQLException{
		if(!this.my.checkConnection()){
			this.c = this.my.openConnection();
		}
		Statement s = this.c.createStatement();
		ResultSet result = s.executeQuery("SELECT `score` FROM `WackAmole` Where `name`='"+name+"';");
		if(!result.next()){
			return -1;
		}
		int i = result.getInt("score");
		s.close();
		result.close();
		return i;
	}
}
