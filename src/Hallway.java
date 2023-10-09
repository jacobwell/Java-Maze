/*
 * Hallway.java
 *
 * Version:
 * $Id: Hallway.java,v 1.1 2011-11-09 08:50:46 jgw7654 Exp $
 *
 * Revisions:
 * $Log: Hallway.java,v $
 * Revision 1.1  2011-11-09 08:50:46  jgw7654
 * near final commit
 *
 */



/**
 * The Class Hallway for hallway objects.
 * 
 * @author Jacob Wellinghoff (jgw7654)
 * @author Ian Dempsey (ijd8975)
 */
public class Hallway {
	
	///////////////////////////////////////////////////////
	//	                  VARIABLES                      //
	///////////////////////////////////////////////////////
	
	private String	name;
	private Room 	entrance;
	private Room	exit;
	
	///////////////////////////////////////////////////////
	//	                  CONSTRUCTOR                    //
	///////////////////////////////////////////////////////
	
	/**
	 * Instantiates a new hallway.
	 *
	 * @param _name_ the _name_
	 * @param _entrance_ the _entrance_
	 * @param _exit_ the _exit_
	 */
	public Hallway(String _name_, Room _entrance_, Room _exit_) {
		name     =  _name_;
		entrance =  _entrance_;
		exit     =  _exit_;
	}
	
	///////////////////////////////////////////////////////
	//	                    METHODS                      //
	///////////////////////////////////////////////////////
	
	///////////////////// ACCESSORS ///////////////////////
	
	/**
	 * Name.
	 *
	 * @return the string
	 */
	public String	name() 		{return name;}
	
	/**
	 * Entrance.
	 *
	 * @return the room
	 */
	public Room		entrance()  {return entrance;}
	
	/**
	 * Exit.
	 *
	 * @return the room
	 */
	public Room		exit()  	{return exit;}
	
	///////////////////// MOVEMENT /////////////////////////
	
	/**
	 * Move.
	 *
	 * @param p the p
	 */
	public void move(Player p) {
		if 		(p.room().equals(entrance)) {p.teleport(exit);}
		else if (p.room().equals(exit)) 	{p.teleport(entrance);}
	}
	
	/**
	 * Move.
	 *
	 * @param m the m
	 */
	public void move(Monster m) {
		if 		(m.room().equals(entrance)) {m.teleport(exit);}
		else if (m.room().equals(exit)) 	{m.teleport(entrance);}
	}
	
	///////////////////////////////////////////////////////
}
