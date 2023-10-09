/*
 * Trap.java
 *
 * Version:
 * $Id: Trap.java,v 1.1 2011-11-09 08:50:46 jgw7654 Exp $
 *
 * Revisions:
 * $Log: Trap.java,v $
 * Revision 1.1  2011-11-09 08:50:46  jgw7654
 * near final commit
 *
 */



/**
 * The Class Trap containing all the information and methods relevant to a drop
 * 
 * @author Jacob Wellinghoff (jgw7654)
 * @author Ian Dempsey (ijd8975)
 */
public class Trap {
	
	///////////////////////////////////////////////////////
	//	                  VARIABLES                      //
	///////////////////////////////////////////////////////
	
	private String	name;
	private	String	type;
	private int 	modifier;
	private String  defense;
	
	///////////////////////////////////////////////////////
	//	                  CONSTRUCTOR                    //
	///////////////////////////////////////////////////////
	
	/**
	 * Instantiates a new trap.
	 *
	 * @param _name_ the name of the trap
	 * @param _type_ the type of the trap
	 * @param _modifier_ the modifier (which room or damage) of the trap
	 * @param _defense_ the protections item's name to defend against the trap.
	 */
	public Trap(String _name_, String _type_, int _modifier_, String _defense_)
	{
		name = _name_;
		type = _type_;
		modifier = _modifier_;
		defense = _defense_;
	}
	
	///////////////////////////////////////////////////////
	//	                  METHODS                        //
	///////////////////////////////////////////////////////
	
	//////////////////// ACCESSORS ////////////////////////
	

	public String 	name() 		  {return name;}
	public String 	type() 		  {return type;}
	public int		modifier() 	  {return modifier;}
	public String 	defense()	  {return defense;}
	
	//////////////////// ACTIVATION ///////////////////////
	
	/**
	 * Activate.
	 *
	 * @param p the Player which the trap is acted upon.
	 */
	public void activate(Player p) { 
		if ( p.protections().contains(defense)) {
			p.use_protection(defense); 
			return;
		}
		else {
			p.print("A " + name + " trap has ensnared you!");
			if 		(type.equals("weaken")) {p.damage(modifier); p.update();}
			else if (type.equals("warp")) {
				p.print("You immediately feel your body violently " +
						"crumble into dust and scatter into the air.");
				p.print("After a moment of nothingness, the dust rejoins " +
						"itself, reconstructing you in a new location.");
				p.teleport(p.cellar().maze().rooms(modifier));
			}
			else if (type.equals("vanish")) {
				p.print("Your bag now feels much lighter. Upon opening it " +
						"you find that your possessions have turned to ash.");
				p.clear_protections();
			}
		}
		p.update();
	}
	
	///////////////////////////////////////////////////////
}
