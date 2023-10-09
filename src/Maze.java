/*
 * Maze.java
 *
 * Version:
 * $Id: Maze.java,v 1.1 2011-11-09 08:50:46 jgw7654 Exp $
 *
 * Revisions:
 * $Log: Maze.java,v $
 * Revision 1.1  2011-11-09 08:50:46  jgw7654
 * near final commit
 *
 */


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;


/**
 * The Class Maze containing all the information and methods for the maze (model).
 * 
 * @author Jacob Wellinghoff (jgw7654)
 * @author Ian Dempsey (ijd8975)
 */
public class Maze {

	///////////////////////////////////////////////////////
	//	                   VARIABLES                     //
	///////////////////////////////////////////////////////
	
	private Player  player;
	private Monster monster;
	private ArrayList<Room> rooms;
	
	///////////////////////////////////////////////////////
	//	                  CONSTRUCTOR                    //
	///////////////////////////////////////////////////////
	
	/**
	 * Instantiates a new maze.
	 *
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Maze(String file) throws IOException {
		rooms = new ArrayList<Room>();
		String[] config = readConfig(file);
		
		int traps = Integer.parseInt(config[0]);
		ArrayList<Trap> tempTraps = new ArrayList<Trap>();
		for (int i = 1 ; i <= traps; i++ ) {
			String[] trapInfo = config[i].split(" ");
			if(trapInfo.length>3) {
				int tInt = Integer.parseInt(trapInfo[2]);
				Trap t = new Trap(trapInfo[0],trapInfo[1],tInt,trapInfo[3]);
				tempTraps.add(t);
			}
			else {
				Trap t = new Trap(trapInfo[0],trapInfo[1],-1,trapInfo[2]);
				tempTraps.add(t);
			}
		}
		
		String[] roomInfo = config[traps+1].split(" ");
		int temp = 0;
		int lastRoomLine = traps + 1 + 2 * Integer.parseInt(roomInfo[0]);
		for (int i = traps+2; i <= lastRoomLine; i+=2) {
			Trap theTrap = null;
			ArrayList<String> protections;
			if (!config[i].equals("none")) {
				String[] trapNames = config[i].split(" ");
				for (String trap : trapNames) {
					Iterator<Trap> trapItr = tempTraps.iterator();
					while (trapItr.hasNext()) {
						Trap aTrap = trapItr.next();
						if (aTrap.name().equals(trap)) theTrap = aTrap;
					}
				}
			}
			if (!config[i+1].equals("none")) {
				String[] items = config[i+1].split(" ");
				protections = new ArrayList<String>(Arrays.asList(items));
			}
			else protections = new ArrayList<String>();
			rooms.add(new Room(Integer.toString(temp), theTrap, protections));
			temp++;
		}
		
		int hallways = Integer.parseInt(config[lastRoomLine+1]);
		int lastHallwayLine = lastRoomLine + hallways + 1;
		ArrayList<Hallway> tempHallways = new ArrayList<Hallway>();
		for (int i = lastRoomLine+2; i <= lastHallwayLine; i++ ) {
			String[] hallInfo = config[i].split(" ");
			Room r1 = rooms.get(Integer.parseInt(hallInfo[1]));
			Room r2 = rooms.get(Integer.parseInt(hallInfo[2]));
			Hallway h = new Hallway(hallInfo[0], r1, r2);
			tempHallways.add(h);
		}
		
		Iterator<Hallway> hallItr = tempHallways.iterator();
		while(hallItr.hasNext()) {
			Hallway aHallway = hallItr.next();
			Room aRoom = aHallway.entrance();
			aRoom.add_hallway(aHallway);
			aRoom = aHallway.exit();
			aRoom.add_hallway(aHallway);
		}
		
		rooms.get(Integer.parseInt(roomInfo[1])).receive_protection("Amulet");
		String[] monsterInfo = new String[] {};
		if (Integer.parseInt(config[lastHallwayLine+1]) == 1) {
			monsterInfo = config[lastHallwayLine+2].split(" ");
		}
		if (monsterInfo.length == 2) {
			Room room = rooms.get(Integer.parseInt(monsterInfo[1]));
			monster = new Monster(monsterInfo[0], room);
		}
		else monster = null;
		player  = new Player(rooms.get(0), monster);
	}
	
	///////////////////////////////////////////////////////
	//	                   METHODS                       //
	///////////////////////////////////////////////////////
	
	///////////////////// ACCESSORS ///////////////////////
		
	/**
	 * Player.
	 *
	 * @return the player
	 */
	public Player player() {return player;}
	
	/**
	 * Monster.
	 *
	 * @return the monster
	 */
	public Monster monster() {return monster;}
	
	/**
	 * Rooms.
	 *
	 * @return the array list
	 */
	public ArrayList<Room> rooms() {return rooms;}
	
	/**
	 * Rooms.
	 *
	 * @param i the i
	 * @return the room
	 */
	public Room rooms(int i) {return rooms.get(i);}
	
	////////////////////// UTILITY ////////////////////////
	
	/**
	 * Read config.
	 *
	 * @param file the file
	 * @return the string[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String[] readConfig(String file) throws IOException {
	    ArrayList<String> keywords = new ArrayList<String>();
	    try {
	    	Scanner scanner = new Scanner(new FileInputStream(file ));
	    	while (scanner.hasNextLine()) keywords.add(scanner.nextLine());
	    }
	    catch (IOException e) {
	        System.err.println("ERROR: Cannot read configuration file " + file);
	        throw e;
	    }
	    return  keywords.toArray(new String[keywords.size()]);
	  }
	
	///////////////////////////////////////////////////////

}
