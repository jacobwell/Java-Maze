/*
 * Room.java
 *
 * Version:
 * $Id: Room.java,v 1.1 2011-11-09 08:50:47 jgw7654 Exp $
 *
 * Revisions:
 * $Log: Room.java,v $
 * Revision 1.1  2011-11-09 08:50:47  jgw7654
 * near final commit
 *
 */


import java.util.ArrayList;


/**
 * The Class Room containing all the information and methods for a room.
 * 
 * @author Jacob Wellinghoff (jgw7654)
 * @author Ian Dempsey (ijd8975)
 */
public class Room {
	
	///////////////////////////////////////////////////////
	//	                 VARIABLES                       //
	///////////////////////////////////////////////////////
	
	private String name;
	private Trap   trap;
	private Player  player;
	private Monster monster;
	
	private	ArrayList<String>  protections;
	private ArrayList<Hallway> hallways;
	
	private boolean has_sword;
	private boolean	has_amulet;
	private boolean	has_player;
	private boolean	has_monster;
	
	////////////////////////////////////////////////////////
	//	                   CONSTRUCTOR                    //
	////////////////////////////////////////////////////////
	
	/**
	 * Instantiates a new room.
	 *
	 * @param _name_ the name of the room
	 * @param _trap_ the trap in the room (if any)
	 * @param _protections_ the protections in the room
	 */
	public Room(String _name_, Trap _trap_, ArrayList<String> _protections_) {
		name 		= _name_;
		protections = _protections_;
		trap 		= _trap_;
		hallways 	= new ArrayList<Hallway>();
	}
	
	////////////////////////////////////////////////////////
	//	                   METHODS                        //
	////////////////////////////////////////////////////////
	
	///////////////////// ACCESSORS ////////////////////////
	
	public String name() {return name;}
	public ArrayList<String>  protections() {return protections;}
	public ArrayList<Hallway> hallways() 	{return hallways;}
	public boolean 	has_sword()   {return has_sword;}
	public boolean 	has_amulet()  {return has_amulet;}
	public boolean 	has_player()  {return has_player;}
	public boolean 	has_monster() {return has_monster;}
	
	////////////////////// UTILITY ////////////////////////
	
	public void add_hallway(Hallway h) {hallways.add(h);}
	
	///////////////////// INVENTORY ////////////////////////
	
	public void lose_protection(String p) {protections.remove(p);}
	public void lose_amulet()			  {has_amulet = false;}
	public void lose_sword(){has_sword  = false; protections.remove("Sword");}
	public void receive_protection(String p) {protections.add(p);}
	public void receive_amulet()			 {has_amulet = true;}
	public void receive_sword()				 {has_sword  = true;}

	///////////////////// MOVEMENT /////////////////////////
	
	/**
	 * Player entered.
	 *
	 * @param p the p
	 */
	public void playerEntered(Player p) {
		player = p; 
		has_player = true;
		player.print("You entered room " + name + ".");
		if (trap != null)	trap.activate(player);
		if (has_sword())	player.take_sword();
		if (has_monster())	monster.attack(player);
	}
	
	/**
	 * Monster entered.
	 *
	 * @param m the m
	 */
	public void monsterEntered(Monster m) {
		monster = m; 
		has_monster = true;
		if (has_player()) {monster.attack(player);}
	}
	
	/**
	 * Removes player from room.
	 */
	public void playerLeft()  {player  = null; has_player  = false;}
	
	/**
	 * Removes monster from room.
	 */
	public void monsterLeft() {monster = null; has_monster = false;}
	
	///////////////////////////////////////////////////////
}
