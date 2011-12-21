package main;

import gui.GUI;
import item.Item;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import level.Designer;
import level.Level;
import logic.Position;
import network.Client;

public class UserInteraction {
	
	public final static int STATE_WELCOME_SCREEN = 0;
	public final static int STATE_SELECT_LEVEL = 1;
	public final static int STATE_SELECT_CUSTOM_LEVEL = 2;
	public final static int STATE_PLACE_ITEM = 3;
	public final static int STATE_GAME_START = 4;
	public final static int STATE_DESIGNER_SELECT_LEVEL = 5;
	public final static int STATE_DESIGNER_PLACE_ITEM = 6;
	public final static int STATE_DUAL_PLAYER_WAITING = 7;
	public final static int STATE_HOW_TO_PLAY = 8;
	public final static int STATE_SINGLE_PLAYER_WIN = 9;
	public final static int STATE_DUAL_PLAYER_WIN = 10;
	public final static int STATE_DUAL_PLAYER_LOSE = 11;
	
	private final static int SINGLE_PLAYER_BUTTON_XMIN = 365;
	private final static int SINGLE_PLAYER_BUTTON_YMIN = 240;
	private final static int SINGLE_PLAYER_BUTTON_XMAX = 615;
	private final static int SINGLE_PLAYER_BUTTON_YMAX = 285;
	
	private final static int DUAL_PLAYER_BUTTON_XMIN = 365;
	private final static int DUAL_PLAYER_BUTTON_YMIN = 300;
	private final static int DUAL_PLAYER_BUTTON_XMAX = 615;
	private final static int DUAL_PLAYER_BUTTON_YMAX = 345;
	
	private final static int DESIGNER_BUTTON_XMIN = 365;
	private final static int DESIGNER_BUTTON_YMIN = 360;
	private final static int DESIGNER_BUTTON_XMAX = 615;
	private final static int DESIGNER_BUTTON_YMAX = 405;
	
	private final static int HOWTO_BUTTON_XMIN = 365;
	private final static int HOWTO_BUTTON_YMIN = 420;
	private final static int HOWTO_BUTTON_XMAX = 615;
	private final static int HOWTO_BUTTON_YMAX = 465;
	
	private final static int START_BUTTON_XMIN = 463;
	private final static int START_BUTTON_YMIN = 0;
	private final static int START_BUTTON_XMAX = 640;
	private final static int START_BUTTON_YMAX = 98;
	
	private final static int OK_BUTTON_XMIN = 360;
	private final static int OK_BUTTON_YMIN = 240;
	private final static int OK_BUTTON_XMAX = 620;
	private final static int OK_BUTTON_YMAX = 290;
	
	private final static int RETURN_BUTTON_XMIN = 360;
	private final static int RETURN_BUTTON_YMIN = 240;
	private final static int RETURN_BUTTON_XMAX = 620;
	private final static int RETURN_BUTTON_YMAX = 290;
	
	private final static int NEXTLEVEL_BUTTON_XMIN = 360;
	private final static int NEXTLEVEL_BUTTON_YMIN = 300;
	private final static int NEXTLEVEL_BUTTON_XMAX = 620;
	private final static int NEXTLEVEL_BUTTON_YMAX = 350;
	
	private int gameState;
	private Level level;
	private GUI gui;
	private Timer timer;
	private Item draggedItem;
	private Designer designer;
	private Thread thread;
	private Client client;
	private boolean dualPlayer = false;
	
	public UserInteraction() {
		gameState = STATE_WELCOME_SCREEN;
		level = null;
    	
    	thread = new Thread() {
    		public void run() {
    			gui = new GUI(UserInteraction.this);
    			try {
					gui.gameLoop();
				} catch (Exception e) {
					e.printStackTrace();
				}
    	    }
    	};
    	thread.start();
    	
    	timer = new Timer(1000/60, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean pass = level.refresh();

				
				if (dualPlayer) {
					
					if (client.isDefeated()) {
						gameState = STATE_DUAL_PLAYER_LOSE;
						gui.setState(gameState);
					} else if (client.isError()) {
						JOptionPane.showMessageDialog(null, "Network error!");
						System.err.println("network err");
						System.exit(-1);
					}
				}
				if (pass){
		    		timer.stop();
		    		// Return to level select screen
		    		if (!dualPlayer) {
		    			gameState = STATE_SINGLE_PLAYER_WIN;
		    			gui.setState(gameState);
		    		} else {
		    			gameState = STATE_PLACE_ITEM;
		    			gui.setState(gameState);
		    			
		    			gui.playSoundEffect();
						try {
							level = client.getNextLevel();
						} catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "Network error!");
							System.exit(-1);
						}
						if (level == null) {
							client = null;
							gameState = STATE_DUAL_PLAYER_WIN;
							gui.setState(gameState);
							dualPlayer = false;
						}
						if (level != null) gui.setItemList(level.getAllItems());
		    		}
		    	}
			}
		});
    	
    	
	}
	
	public void mouseClick(Position mousePos) throws Exception {
		if (gameState == STATE_WELCOME_SCREEN) {
			if (mousePos.getX() > SINGLE_PLAYER_BUTTON_XMIN && mousePos.getX() < SINGLE_PLAYER_BUTTON_XMAX &&
					mousePos.getY() > SINGLE_PLAYER_BUTTON_YMIN && mousePos.getY() < SINGLE_PLAYER_BUTTON_YMAX) {
				gameState = STATE_SELECT_LEVEL;
				gui.setState(STATE_SELECT_LEVEL);
				gui.playSoundEffect();
			} else if (mousePos.getX() > DUAL_PLAYER_BUTTON_XMIN && mousePos.getX() < DUAL_PLAYER_BUTTON_XMAX &&
					mousePos.getY() > DUAL_PLAYER_BUTTON_YMIN && mousePos.getY() < DUAL_PLAYER_BUTTON_YMAX) {
				
				JTextField jtfName = new JTextField("Kangcha");
				JTextField jtfIp = new JTextField("localhost");
				
				JPanel p = new JPanel(new BorderLayout(5,5));
				JPanel p1 = new JPanel(new GridLayout(2,1,5,5));
				JPanel p2 = new JPanel(new GridLayout(2,1,5,5));
				p1.add(new JLabel("Your Name:"));
				p1.add(new JLabel("Server IP:"));
				p2.add(jtfName);
				p2.add(jtfIp);
				p.add(p1, BorderLayout.WEST);
				p.add(p2, BorderLayout.CENTER);
				
				int res = JOptionPane.showConfirmDialog(null, p, "Connection", JOptionPane.OK_CANCEL_OPTION);
				if (res == JOptionPane.CANCEL_OPTION) return;
				
				
				gameState = STATE_DUAL_PLAYER_WAITING;
				gui.setItemList(null);
				gui.setState(gameState);
				dualPlayer = true;
				
				client = new Client(jtfName.getText(), jtfIp.getText(), this);
				
			} else if (mousePos.getX() > DESIGNER_BUTTON_XMIN && mousePos.getX() < DESIGNER_BUTTON_XMAX &&
					mousePos.getY() > DESIGNER_BUTTON_YMIN && mousePos.getY() < DESIGNER_BUTTON_YMAX) {
				gameState = STATE_DESIGNER_SELECT_LEVEL;
				gui.setState(gameState);
				gui.playSoundEffect();
			}
			else if (mousePos.getX() > HOWTO_BUTTON_XMIN && mousePos.getX() < HOWTO_BUTTON_XMAX &&
					mousePos.getY() > HOWTO_BUTTON_YMIN && mousePos.getY() < HOWTO_BUTTON_YMAX) {
				gameState = STATE_HOW_TO_PLAY;
				gui.setState(gameState);
			}
		}
		else if (gameState == STATE_SELECT_LEVEL || gameState == STATE_SELECT_CUSTOM_LEVEL || gameState == STATE_DESIGNER_SELECT_LEVEL) {
			int r, c;
			//System.out.println("y = "+mousePos.getY());
			if (mousePos.getY()>120 && mousePos.getY()<200) r = 0;
			else if (mousePos.getY()>240 && mousePos.getY()<320) r = 1;
			else if (mousePos.getY()>360 && mousePos.getY()<440) r = 2;
			else r = -1;
			//System.out.println("x = "+mousePos.getX());
			if (mousePos.getX()>40 && mousePos.getX()<120) c = 1;
			else if (mousePos.getX()>160 && mousePos.getX()<240) c = 2;
			else if (mousePos.getX()>280 && mousePos.getX()<360) c = 3;
			else if (mousePos.getX()>400 && mousePos.getX()<480) c = 4;
			else if (mousePos.getX()>520 && mousePos.getX()<600) c = 5;
			else c = -1;
			System.out.println("r = "+r+" c = "+c);
			if (r!=-1 && c!=-1) {
				int levelSelected = r * 5 + c;
				gui.playSoundEffect();
				if (gameState == STATE_SELECT_LEVEL) {
					level = new Level("level" + levelSelected);
					gui.setItemList(level.getAllItems());
					gameState = STATE_PLACE_ITEM;
					gui.setState(STATE_PLACE_ITEM);
				} else if (gameState == STATE_SELECT_CUSTOM_LEVEL) {
					level = new Level("level_c" + levelSelected);
					gui.setItemList(level.getAllItems());
					gameState = STATE_PLACE_ITEM;
					gui.setState(STATE_PLACE_ITEM);
				} else if (gameState == STATE_DESIGNER_SELECT_LEVEL) {
					level = new Level();
					designer = new Designer(level, "level_c" + levelSelected);
					gui.setItemList(level.getAllItems());
					gameState = STATE_DESIGNER_PLACE_ITEM;
					gui.setState(STATE_DESIGNER_PLACE_ITEM);
				}
			}
			if (r==-1 && c==-1 && gameState == STATE_SELECT_LEVEL) {
				gameState = STATE_SELECT_CUSTOM_LEVEL;
				gui.setState(gameState);
				gui.playSoundEffect();
			}
		}
		else if (gameState == STATE_PLACE_ITEM|| (gameState == STATE_DESIGNER_PLACE_ITEM && !designer.hasCreatedItem())) {
			if (mousePos.getX() > START_BUTTON_XMIN && mousePos.getX() < START_BUTTON_XMAX &&
					mousePos.getY() > START_BUTTON_YMIN && mousePos.getY() < START_BUTTON_YMAX) {
				gameState = STATE_GAME_START;
				gui.setState(STATE_GAME_START);
				System.out.println("Started");
				level.startLevel();
				timer.start();
				gui.playSoundEffect();
				return;
			}
			
			try {
				level.itemRotate(mousePos, Math.PI/8);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (gameState == STATE_GAME_START) {
			if (mousePos.getX() > START_BUTTON_XMIN && mousePos.getX() < START_BUTTON_XMAX &&
					mousePos.getY() > START_BUTTON_YMIN && mousePos.getY() < START_BUTTON_YMAX) {
				gameState = STATE_PLACE_ITEM;
				gui.setState(gameState);
				System.out.println("Stopped");
				timer.stop();
				level.stopLevel();
				gui.setItemList(level.getAllItems());
				gui.playSoundEffect();
				return;
			}
			
			try {
				level.changeDTools(mousePos);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (gameState == STATE_DUAL_PLAYER_WAITING) {
			if (mousePos.getX() > RETURN_BUTTON_XMIN && mousePos.getX() < RETURN_BUTTON_XMAX &&
					mousePos.getY() > RETURN_BUTTON_YMIN && mousePos.getY() < RETURN_BUTTON_YMAX) {
				gameState = STATE_WELCOME_SCREEN;
				gui.setState(gameState);
			}
		} else if (gameState == STATE_HOW_TO_PLAY) {
			if (!gui.incHowtoCnt()) {
				gameState = STATE_WELCOME_SCREEN;
				gui.setState(gameState);
			}
		} else if (gameState == STATE_SINGLE_PLAYER_WIN) {
			if (mousePos.getX() > RETURN_BUTTON_XMIN && mousePos.getX() < RETURN_BUTTON_XMAX &&
					mousePos.getY() > RETURN_BUTTON_YMIN && mousePos.getY() < RETURN_BUTTON_YMAX) {
				gameState = STATE_SELECT_LEVEL;
				gui.setState(gameState);
			} else if (mousePos.getX() > NEXTLEVEL_BUTTON_XMIN && mousePos.getX() < NEXTLEVEL_BUTTON_XMAX &&
					mousePos.getY() > NEXTLEVEL_BUTTON_YMIN && mousePos.getY() < NEXTLEVEL_BUTTON_YMAX) {
				int id = level.getId();
				if (id < 15 && id > 0) {
					gameState = STATE_PLACE_ITEM;
					level = new Level("level"+(id+1));
					gui.setState(gameState);
					gui.setItemList(level.getAllItems());
				}
			}
		} else if (gameState == STATE_DUAL_PLAYER_WIN) {
			if (mousePos.getX() > OK_BUTTON_XMIN && mousePos.getX() < OK_BUTTON_XMAX &&
					mousePos.getY() > OK_BUTTON_YMIN && mousePos.getY() < OK_BUTTON_YMAX) {
				gameState = STATE_WELCOME_SCREEN;
				gui.setState(gameState);
			}
		} else if (gameState == STATE_DUAL_PLAYER_LOSE) {
			if (mousePos.getX() > OK_BUTTON_XMIN && mousePos.getX() < OK_BUTTON_XMAX &&
					mousePos.getY() > OK_BUTTON_YMIN && mousePos.getY() < OK_BUTTON_YMAX) {
				gameState = STATE_WELCOME_SCREEN;
				gui.setState(gameState);
			}
		} else {
			System.err.println("illegal gameState");
			// error recovery
		}
	}
	
	public void mouseDown(Position mousePos) throws Exception {
		if (gameState == STATE_PLACE_ITEM || (gameState == STATE_DESIGNER_PLACE_ITEM && !designer.hasCreatedItem())) {
			draggedItem = level.itemSearch(mousePos);
		} else if (gameState == STATE_DESIGNER_PLACE_ITEM && designer.hasCreatedItem()) {
			if (designer.firstPosition(mousePos)) gui.setNormalCursor();
			gui.setItemList(level.getAllItems());
		}
	}
	
	public void mouseDrag(Position mousePos) throws Exception {
		if (gameState == STATE_PLACE_ITEM || (gameState == STATE_DESIGNER_PLACE_ITEM && !designer.hasCreatedItem())) {
			if (draggedItem != null) {
				if (level.validatePosition(draggedItem, mousePos)) {
					gui.setItemList(level.getAllItems());
					draggedItem = level.itemSearch(mousePos);
				}
			}
		}
		// Other states: do nothing
	}
	
	public void shiftClick(Position mousePos) throws Exception{
		if (gameState == STATE_DESIGNER_PLACE_ITEM) {
			level.editorDeleteItem(mousePos);
			gui.setItemList(level.getAllItems());
		}
	}
	
	public void mouseUp(Position mousePos) throws Exception {
		if (gameState == STATE_PLACE_ITEM || (gameState == STATE_DESIGNER_PLACE_ITEM && !designer.hasCreatedItem())) {
			if (draggedItem != null) {
				//System.out.println("mouseUP");
				level.distributeItem(draggedItem, mousePos);
			}
		} else if (gameState == STATE_DESIGNER_PLACE_ITEM && designer.hasCreatedItem()) {
			designer.secondPosition(mousePos);
			gui.setItemList(level.getAllItems());
			gui.setNormalCursor();
		}
	}
	
	public void spacePressed() throws Exception {
		if (gameState == STATE_PLACE_ITEM) {
			gameState = STATE_GAME_START;
			gui.setState(STATE_GAME_START);
			System.out.println("Started");
			level.startLevel();
			timer.start();
			gui.playSoundEffect();
		} else if (gameState == STATE_GAME_START) {
			gameState = STATE_PLACE_ITEM;
			gui.setState(STATE_PLACE_ITEM);
			System.out.println("Stopped");
			timer.stop();
			level.stopLevel();
			gui.setItemList(level.getAllItems());
			gui.playSoundEffect();
		} else if (gameState == STATE_DESIGNER_PLACE_ITEM) {
			level.saveLevel(designer.getLevelName());
			level = null;
			designer = null;
			gameState = STATE_DESIGNER_SELECT_LEVEL;
			gui.setState(gameState);
		}
	}
	
	public void escPressed() {
		if (gameState == STATE_SELECT_LEVEL|| gameState == STATE_SELECT_CUSTOM_LEVEL || gameState == STATE_DESIGNER_SELECT_LEVEL) {
			gameState = STATE_WELCOME_SCREEN;
			gui.setState(gameState);
		}  else if (gameState == STATE_PLACE_ITEM  & !dualPlayer) {
			gameState = STATE_SELECT_LEVEL;
			gui.setState(gameState);
		} else if (gameState == STATE_DESIGNER_PLACE_ITEM) {
			if (!designer.hasCreatedItem()) {
				gameState = STATE_DESIGNER_SELECT_LEVEL;
				gui.setState(gameState);
				designer = null;
			}
			else {
				designer.destroyCreatedItem();
				gui.setNormalCursor();
			}
		} else if (gameState == STATE_HOW_TO_PLAY) {
			gui.resetHowtoCnt();
			gameState = STATE_WELCOME_SCREEN;
			gui.setState(gameState);
		} 
	}
	
	public void keyPressed(int itemNo) {
		if (gameState != STATE_DESIGNER_PLACE_ITEM)
			return;
		designer.createItem(itemNo);
		gui.setCrossCursor();
	}
	
	public boolean waitingDone() {
		try {
			level = client.getNextLevel();
		} catch (Exception e) {
			e.printStackTrace();
			instantError();
		}
		if (gameState != STATE_DUAL_PLAYER_WAITING && gameState != STATE_GAME_START) return false;
		
		gui.setItemList(level.getAllItems());
		
		
		gameState = STATE_PLACE_ITEM;
		gui.setState(gameState);
		System.out.println("Change state!!!");
		gui.playSoundEffect();
		
		return true;
	}
	
	public void instantLose() {
		if (level.isStarted()) {	
			try {
				timer.stop();
				level.stopLevel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		gameState = STATE_DUAL_PLAYER_LOSE;
		gui.setState(gameState);
		dualPlayer = false;
	}
	
	public void instantJam() {
		if (level.isStarted()) {	
			try {
				timer.stop();
				level.stopLevel();
				gameState = STATE_PLACE_ITEM;
				gui.setState(gameState);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		level.jamItems();
		gui.setItemList(level.getAllItems());
	}
	
	public void instantError() {
		JOptionPane.showMessageDialog(null, "Network error");
		client = null;
		System.gc();
		
		gameState = STATE_WELCOME_SCREEN;
		gui.setState(gameState);
		dualPlayer = false;
	}
	
	public static void main(String args[]){
		new UserInteraction();
	}
}