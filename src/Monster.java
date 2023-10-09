/*
 * Monster.java
 *
 * Version:
 * $Id: Monster.java,v 1.1 2011-11-09 08:50:47 jgw7654 Exp $
 *
 * Revisions:
 * $Log: Monster.java,v $
 * Revision 1.1  2011-11-09 08:50:47  jgw7654
 * near final commit
 *
 */


import java.util.Random;


/**
 * The Class Monster containg all the monster info and methods.
 * 
 * @author Jacob Wellinghoff (jgw7654)
 * @author Ian Dempsey (ijd8975)
 */
public class Monster {
	///////////////////////////////////////////////////////
	//	                   VARIABLES                     //
	///////////////////////////////////////////////////////
	
	private String	name;
	private Room 	room;
	private boolean alive;
	private Random  r;

	//////////////////////////////////////////////////////
	//	                 CONSTRUCTOR                    //
	//////////////////////////////////////////////////////
	
	/**
	 * Instantiates a new monster.
	 *
	 * @param _name_ the _name_
	 * @param _room_ the _room_
	 */
	public Monster(String _name_, Room _room_) {
		name  =  _name_;
		room  =  _room_;
		alive =  true;
	}
	
	///////////////////////////////////////////////////////
	//	                   METHODS                       //
	///////////////////////////////////////////////////////
	
	//////////////////// ACCESSORS ////////////////////////
	
	/**
	 * Name.
	 *
	 * @return the string
	 */
	public String  name()  {return name;}
	
	/**
	 * Room.
	 *
	 * @return the room
	 */
	public Room    room()  {return room;}
	
	/**
	 * Alive.
	 *
	 * @return true, if successful
	 */
	public boolean alive() {return alive;}
	
	///////////////////// MOVEMENT /////////////////////////
	
	/**
	 * Teleport.
	 *
	 * @param r the Room
	 */
	public void teleport(Room r) {
		room.monsterLeft(); 
		room = r; 
		room.monsterEntered(this);
	}
	
	/**
	 * Creep.
	 */
	public void creep() {
		r = new Random();
		int index = r.nextInt(room.hallways().size());
		room.hallways().get(index).move(this);
	}
	
	///////////////////// DAMAGE //////////////////////////
	
	/**
	 * Attack.
	 *
	 * @param p the Player
	 */
	public void attack(Player p) {
		p.print("Out of the shadows, a " + name + " charges at you--" +
				"its blood-stained fangs aiming for your throat.");
		if (p.has_sword()) {
			p.print("You plunge your sword into the mouth of the " + name +
					", halting its attack and spraying the cold" +
					" ground with black blood.");
			room.monsterLeft();
			alive = false;
			room = null;
			p.levelUp();
			return;
		}
		else {
			p.print("The " + name + " bites into your flesh, +" +
					"ripping off a large chunk and swallowing it--" +
					"you scream in pain as it rears for another attack.");
			int hp = ((p.life() / 2) > 10) ? (p.life() / 2) : (p.life() - 10);
			p.set_life(hp);
			p.update();
		}
	}
	
	///////////////////////////////////////////////////////
}
