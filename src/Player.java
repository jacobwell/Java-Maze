/*
 * Player.java
 *
 * Version:
 * $Id: Player.java,v 1.1 2011-11-09 08:50:46 jgw7654 Exp $
 *
 * Revisions:
 * $Log: Player.java,v $
 * Revision 1.1  2011-11-09 08:50:46  jgw7654
 * near final commit
 *
 */


import java.util.ArrayList;


/**
 * The Class Player containing all player information and methods.
 * 
 * @author Jacob Wellinghoff (jgw7654)
 * @author Ian Dempsey (ijd8975)
 */
public class Player 
{
	///////////////////////////////////////////////////////
	//	                   VARIABLES                     //
	///////////////////////////////////////////////////////
	
	private Cellar  CELLAR;
	private Room 	room;
	private int		life;
	private	int		level;
	private	int		protection_cap;
	private boolean has_sword;
	private Monster monster;
	private ArrayList<String> protections;
	
	///////////////////////////////////////////////////////
	//	                  CONSTRUCTOR                    //
	///////////////////////////////////////////////////////
	
	/**
	 * Instantiates a new player.
	 *
	 * @param _room_ the _room_
	 * @param _monster_ the _monster_
	 */
	public Player(Room _room_, Monster _monster_) {
		room  = _room_;
		life  = 100; 
		level = 1;
		protection_cap = level;
		has_sword = false;
		monster = _monster_;
		protections = new ArrayList<String>();
	}
	
	///////////////////////////////////////////////////////
	//	                  METHODS                        //
	///////////////////////////////////////////////////////
	
	//////////////////// ACCESSORS ////////////////////////
	
	public Cellar	cellar()			{return CELLAR;}
	public Room		room()  			{return room;}
	public int		life()  			{return life;}
	public int		level()				{return level;}
	public int 		protection_cap() 	{return protection_cap;}
	public boolean	has_sword()			{return has_sword;}
	public ArrayList<String> protections() {return protections;}
	
	////////////////////// UTILITY ////////////////////////
	
	/**
	 * Prints the string to the GUI console.
	 *
	 * @param text the String for printing.
	 */
	public void print(String text)	 {Cellar.print(text); update();}
	
	/**
	 * Update the Cellar/GUI
	 */
	public void update()			 {Cellar.update();}
	
	/**
	 * Add cellar to player instance variables.
	 *
	 * @param c the Cellar object.
	 */
	public void add_cellar(Cellar c) {CELLAR = c; room.playerEntered(this);}
	
	///////////////////// INVENTORY ////////////////////////
	
	/**
	 * Uses a protection and levels up.
	 *
	 * @param p the String for which protection to use
	 */
	public void use_protection(String p) {
		protections.remove(p); 
		print("Your " + p + " defened you against the trap!");
		levelUp();
		update();
	}
	
	/**
	 * Drop protection.
	 *
	 * @param p the String for which protection to be dropped.
	 */
	public void drop_protection(String p) {
		protections.remove(p);
		update();
		print("Dropped " + p + "."); 
		room.receive_protection(p); 
		update();
		advanceMonster();
	}
	
	/**
	 * Clear all protections from inventory.
	 */
	public void clear_protections() {protections.clear();}
	
	/**
	 * Take a protection from the room.
	 *
	 * @param p the String for which protection item
	 */
	public void take_protection(String p) {
		if (protections.size() < protection_cap) {
			room.lose_protection(p); 
			protections.add(p);
			print("Aquired " + p + ".");
			update();
			advanceMonster();
		}
		else {print("Inventory full.");}
	} 
	
	/**
	 * Take the amulet from the room.
	 */
	public void take_amulet() {
		if (room.has_amulet()) {
			room.lose_amulet();
			print("You walk up to the altar in the center of the room, reach out and take the amulet."); 
			CELLAR.win();
		}
	}
	
	/**
	 * Take the sword from the room.
	 */
	public void	take_sword()  {
		room.lose_sword(); 
		has_sword = true; 
		print("You pull the sword from the stone slit in the floor, it glimmers fiendishly in the torchlight.");
		advanceMonster();
	}
	
	///////////////////// MOVEMENT /////////////////////////
	
	/**
	 * Teleport's player to r
	 *
	 * @param r the Room
	 */
	public void teleport(Room r) {
		room.playerLeft();
		room = r; 
		room.playerEntered(this);
		update();
		advanceMonster();
	}
	
	///////////////////// DAMAGE //////////////////////////
	
	/**
	 * Damages the player.
	 *
	 * @param d the damage taken.
	 */
	public void damage(int d) {
		life -= d; 
		print("You took " + d + " damage!");
		update();
	}
	
	/**
	 * Sets the player's life.
	 *
	 * @param hp the new _life
	 */
	public void set_life(int hp) {
		life = hp; 
		print("You now have " + life + " life remaining.");
		update();
	}
	
	/**
	 * Level up the player.
	 */
	public void levelUp() {
		level++; 
		protection_cap++; 
		print("Leveled up to " + level + "!");
		update();
	}
	
	/**
	 * Advance monster to next random room.
	 */
	public void advanceMonster() { 
		if (monster != null && monster.alive()) {monster.creep(); update();}
	}
	///////////////////////////////////////////////////////
}
