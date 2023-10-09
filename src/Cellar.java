/*
 * Cellar.java
 *
 * Version:
 * $Id: Cellar.java,v 1.1 2011-11-09 08:50:47 jgw7654 Exp $
 *
 * Revisions:
 * $Log: Cellar.java,v $
 * Revision 1.1  2011-11-09 08:50:47  jgw7654
 * near final commit
 *
 */


import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;


/**
 * The Class Cellar containing the entire dungeon crawler game.
 * 
 * @author Jacob Wellinghoff (jgw7654)
 * @author Ian Dempsey (ijd8975)
 */
public class Cellar extends WindowAdapter implements ActionListener {	
	private static Maze GAME;
	private static JFrame frame;	
	private static FlowLayout centerLayout;
	private static DefaultListModel protectionsModel;
	private static DefaultListModel roomInventoryModel;
	private static JTextArea console;
	private static JButton addItemBtn;
	private static JButton dropItemBtn;
	private static JLabel health;
	private static JLabel level;
	private static JLabel sword;
	
	private static Boolean gameOver = false;
	
	private static JPanel hallwayPanel;
	
	private JPanel consolePanel;
	private JPanel protectionsPanel;
	private JPanel roomInventoryPanel;
	private JPanel specialPanel;
	private JPanel healthPanel;
	private JPanel levelPanel;
	private JPanel swordPanel;
	
	private JList protections;
	private JList roomInventory;
	
	
	/**
	 * Instantiates a new cellar.
	 *
	 * @param file the maze configuration file.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Cellar(String file) throws IOException {
		frame = new JFrame("Cellar");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setTitle("Cellar - Room: 0");
		frame.setLocation(100, 100);
		frame.setSize(800, 450);
		frame.setMinimumSize(new Dimension(700,300));
		
		consolePanel = new JPanel(new BorderLayout());
		console = new JTextArea();
		console.setLineWrap(true);
		console.setWrapStyleWord(true);
		console.setEditable(false);
		consolePanel.add(console,BorderLayout.CENTER);
		consolePanel.add(new JLabel(" "),BorderLayout.NORTH);
		JScrollPane consoleScroller = new JScrollPane(console);
		consoleScroller.setPreferredSize(new Dimension(250, 400));
		frame.add(consoleScroller, BorderLayout.CENTER);
		
		protectionsPanel = new JPanel(new BorderLayout());
		protectionsModel = new DefaultListModel();
		protections = new JList(protectionsModel);
		protections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane protectionsScroller = new JScrollPane(protections);
		protectionsScroller.setPreferredSize(new Dimension(250, 300));
		JLabel l = new JLabel("<html><b>&nbsp;Protections:</b></html>");
		protectionsPanel.add(l, BorderLayout.NORTH);
		dropItemBtn = new JButton("Drop Item");
		dropItemBtn.addActionListener(this);
		protectionsPanel.add(dropItemBtn, BorderLayout.SOUTH);
		protectionsPanel.add(protectionsScroller, BorderLayout.CENTER);
		frame.add(protectionsPanel, BorderLayout.WEST);
		
		roomInventoryPanel = new JPanel(new BorderLayout());
		roomInventoryModel = new DefaultListModel();
		roomInventory = new JList(roomInventoryModel);
		roomInventory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane roomInventoryScroller = new JScrollPane(roomInventory);
		roomInventoryScroller.setPreferredSize(new Dimension(250,300));
		JLabel l2 = new JLabel("<html><b>Room Inventory:</b></html>");
		roomInventoryPanel.add(l2, BorderLayout.NORTH);
		addItemBtn = new JButton("Pick-up Item");
		addItemBtn.addActionListener(this);
		roomInventoryPanel.add(addItemBtn, BorderLayout.SOUTH);
		roomInventoryPanel.add(roomInventoryScroller, BorderLayout.CENTER);
		frame.add(roomInventoryPanel, BorderLayout.EAST);
		
		centerLayout = new FlowLayout(FlowLayout.CENTER);
		
		health = new JLabel("100/100");
		healthPanel = new JPanel(centerLayout);
		healthPanel.add(new JLabel("<html><b>Health: </b></html>"));
		healthPanel.add(health);
		
		level = new JLabel("1");
		levelPanel = new JPanel(centerLayout);
		levelPanel.add(new JLabel("<html><b>Level: </b></html>"));
		levelPanel.add(level);
		
		sword = new JLabel("None");
		swordPanel = new JPanel(centerLayout);
		swordPanel.add(new JLabel("<html><b>Sword: </b></html>"));
		swordPanel.add(sword);
		
		specialPanel = new JPanel(new GridLayout(1, 3));
		specialPanel.add(healthPanel);
		specialPanel.add(levelPanel);
		specialPanel.add(swordPanel);
		frame.add(specialPanel, BorderLayout.NORTH);
		
		GAME  = new Maze(file);
		hallwayPanel = new JPanel(new FlowLayout());
		frame.add(createHallwayPanel(), BorderLayout.SOUTH);
		frame.setVisible(true);
	}
		
	/**
	 * Update, updates the gui.
	 */
	public static void update() {
		Boolean ranOnce = false;
		while (!gameOver && !ranOnce) {
			frame.add(createHallwayPanel(), BorderLayout.SOUTH);
			Room currentRoom = GAME.player().room();
			frame.setTitle("Cellar - Room: " + currentRoom.name());
			
			ArrayList<String> protections = GAME.player().protections();
			protectionsModel.clear();
			Iterator<String> protectionsItr = protections.iterator();
			while (protectionsItr.hasNext()) {
				String item = protectionsItr.next();
				if (! item.equals("Sword")) {
					protectionsModel.add(protectionsModel.getSize(), item);
				}
			}
			
			roomInventoryModel.clear();
			ArrayList<String> protectionsInRoom = currentRoom.protections();
			Iterator<String> protectItr = protectionsInRoom.iterator();
			while (protectItr.hasNext()) {
				String item = protectItr.next();
				roomInventoryModel.add(roomInventoryModel.getSize(), item);
			}
			
			int lifeLeft = GAME.player().life();
			health.setText(lifeLeft + "/100");
			if (lifeLeft <= 0) {
				print("You died. \n\nGAME OVER - YOU LOOSE!");
				addItemBtn.setEnabled(false);
				dropItemBtn.setEnabled(false);
				hallwayPanel.setVisible(false);
				gameOver = true;
				break;
			}
			
			if (GAME.player().has_sword()) sword.setText("Equipped");
			level.setText(Integer.toString(GAME.player().level()));
			ranOnce=true;
		}
	}


	/**
	 * Creates the hallway panel.
	 *
	 * @return the j panel
	 */
	private static JPanel createHallwayPanel() {
		hallwayPanel.removeAll();
		for (int i = 0; i < GAME.player().room().hallways().size(); i++) {
			String s = GAME.player().room().hallways().get(i).name();
			JButton b = makeButton(i, s);
		    hallwayPanel.add(b);
		    hallwayPanel.revalidate();
		}
		return hallwayPanel;
	}
	
	/**
	 * Make the button for the hallway panel.
	 *
	 * @param i the corespond hallway number
	 * @param name the name of the hallway/button
	 * @return the JButton
	 */
	private static JButton makeButton(final int i, final String name) {
	    JButton button = new JButton(String.valueOf(name));
	    button.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            GAME.player().room().hallways().get(i).move(GAME.player());
	        }
	    });
	    return button;
	}
		

	public Maze maze() {return GAME;}
    
    /**
     * Prints the.
     *
     * @param text the text
     */
    public static void print(String text) {
    	if (!gameOver) console.append(text + "\n\n");
    }
	
    /**
     * Method for winning the game.
     */
    public void win() {			
		print("The amulet glows a dark, pulsating red, " +
				"growing brighter as you place it around your neck.");
		print("The amulet's pulsating soon synchronizes with your" +
				" heartbeat, and you feel your powers surge.");
		print("You realize that the amulet has accepted you as its " +
				"master and you are one step closer to ultimate power.");
		print("GAME OVER - YOU WIN!");
		addItemBtn.setEnabled(false);
		dropItemBtn.setEnabled(false);
		hallwayPanel.setVisible(false);
		gameOver = true;
	}
    
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dropItemBtn) dropItem();
		else if (e.getSource() == addItemBtn) addItem();
	}
	
	/**
	 * Drops item from inventory.
	 */
	public void dropItem() {
		int where = protections.getSelectedIndex();
		if (where >= 0) {
			String item = (String) protections.getSelectedValue();
			protectionsModel.remove(where);
			GAME.player().drop_protection(item);
			update();
		}
		protections.clearSelection();
	}
	
	/**
	 * Adds the item to inventory.
	 */
	public void addItem() {
		int where = roomInventory.getSelectedIndex();
		if (where >= 0) {
			String item = (String) roomInventory.getSelectedValue();
			roomInventoryModel.remove(where);
			if (item.equals("Sword")) GAME.player().take_sword();
			else if (item.equals("Amulet")) win();
			else GAME.player().take_protection(item);
			update();
		}
		roomInventory.clearSelection();
	}
    
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
		    System.err.println("ERROR: Wrong number of arguments");
		}
		Cellar x = new Cellar(args[0]);
		GAME.player().add_cellar(x);
		update();
	}
}
