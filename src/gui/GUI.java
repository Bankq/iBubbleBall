package gui;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import level.Designer;
import logic.*;
import item.*;
import main.UserInteraction;

import org.lwjgl.*;
import org.lwjgl.examples.spaceinvaders.Game;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.CursorLoader;

public class GUI {
	
	private String windowTitle;
	private int height;
	private int width;
	private int state;
	
	private Sprite welcomeScreen;
	private Sprite selectLevel;
	private Sprite backGround;
	private Sprite backGround2;
	private Sprite backGroundDesign;
	private Sprite backGroundWaiting;
	private Sprite selectLevel2;
	private Sprite howto1;
	private Sprite howto2;
	private Sprite howto3;
	private Sprite howto4;
	private Sprite win_single;
	private Sprite win_dual;
	private Sprite lose_dual;
	
	private Audio bgm;
	private Audio button_se;
	
	private Cursor crossCursor;
	
	public static boolean gameRunning = true;
	private TextureLoader textureLoader;
	
	// Mouse & Keyboard Interaction required
	private boolean click = false;
	private boolean pressSpace = false;
	private boolean pressEsc = false;
	private boolean pressW = false;
	private boolean pressG = false;
	private boolean pressM = false;
	private boolean pressB = false;
	private boolean press1 = false;
	private boolean press2 = false;
	private boolean press3 = false;
	private boolean press4 = false;
	private boolean press5 = false;
	private boolean press6 = false;
	private boolean pressPgup = false;
	private boolean pressPgdn = false;
	private boolean pressHome = false;
	private boolean pressLctrl = false;
	private boolean pressRctrl = false;
	private int howtoCnt = 1;
	
	Position oldPos = new Position(0,0);
	UserInteraction event;
	
	private Item[] items;
	
	public GUI(UserInteraction e){
		height = 480;
		width = 640;
		windowTitle = "iBubbleGame";
		state = UserInteraction.STATE_WELCOME_SCREEN;
		items = null;
		event = e;
		initialize();
	}
	
	public GUI(UserInteraction e, boolean dual){
		height = 480;
		width = 640;
		windowTitle = "iBubbleGame";
		state = UserInteraction.STATE_WELCOME_SCREEN;
		items = null;
		event = e;
		initialize();
	}
 
	public void initialize(){
		//initialize the window beforehand
		try {
			setDisplayMode(width, height);
			Display.setTitle(windowTitle);
			Display.setFullscreen(false);
			Display.create();
			
			
			// enable textures since we're going to use these for our sprites
			glEnable(GL_TEXTURE_2D);

			// disable the OpenGL depth test since we're rendering 2D graphics
			glDisable(GL_DEPTH_TEST);
			
			glEnable(GL_BLEND); 
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glEnable(GL11.GL_ALPHA_TEST); 
			glAlphaFunc(GL11.GL_GREATER, 0.1f);

			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();

			glOrtho(0, width, height, 0, -1, 1);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glViewport(0, 0, width, height);
			
			textureLoader = new TextureLoader();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		welcomeScreen = getSprite("resources/welcome.PNG");
		selectLevel = getSprite("resources/select_level.png");
		backGround = getSprite("resources/bk.png");
		backGround2 = getSprite("resources/bk_stop.png");
		backGroundDesign = getSprite("resources/bk_design.png");
		backGroundWaiting = getSprite("resources/waiting.png");
		selectLevel2 = getSprite("resources/select_level2.png");
		howto1 = getSprite("resources/howto1.png");
		howto2 = getSprite("resources/howto2.png");
		howto3 = getSprite("resources/howto3.png");
		howto4 = getSprite("resources/howto4.png");
		win_single = getSprite("resources/congratulations.png");
		win_dual = getSprite("resources/youwin.png");
		lose_dual = getSprite("resources/youlose.png");
		
		try {
			crossCursor = CursorLoader.get().getCursor("resources/cursor.png", 11, 12);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}
		
		try {
			bgm = AudioLoader.getStreamingAudio("OGG", new File("resources/bgm.ogg").toURI().toURL());
			bgm.playAsMusic(1.0f, 1.0f, true);
			button_se = AudioLoader.getAudio("OGG", new FileInputStream("resources/button_se.ogg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean incHowtoCnt() {
		howtoCnt++;
		if (howtoCnt > 4) {
			howtoCnt = 1;
			return false;
		}
		return true;
	}
	
	public void resetHowtoCnt() {
		howtoCnt = 1;
	}
	
	public void playSoundEffect() {
		button_se.playAsSoundEffect(1.0f, 1.0f, false);
	}
	
	public void setCrossCursor() {
		try {
			Mouse.setNativeCursor(crossCursor);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public void setNormalCursor() {
		try {
			Mouse.setNativeCursor(null);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public void setItemList(Item[] items) {
		this.items = items;
	}
	
	public boolean setDisplayMode(int width, int height) {
		//this.width = width;
		//this.height = height;
	    try {
			// get modes
			DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(width, height, -1, -1, -1, -1, 60, 60);

	      org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
	          "width=" + width,
	          "height=" + height,
	          "freq=" + 60,
	          "bpp=" + org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel()
	         });
	      return true;
	    } catch (Exception e) {
	      e.printStackTrace();
	      System.out.println("Set display mode failed.");
	    }

			return false;
		}		
 
	 
	
	public void gameLoop() throws Exception {
		while (Game.gameRunning) {
			// poll input
			if (Mouse.isButtonDown(0) && !click) {
				click = true;
				oldPos.setXY(Mouse.getX(), Mouse.getY());
				//System.out.println("Mouse Down @("+Mouse.getX()+", "+Mouse.getY()+")");
				event.mouseDown(new Position(Mouse.getX(), 480-Mouse.getY()));
			} else if (Mouse.isButtonDown(0) && click) {
				if (Mouse.getDX()!=0 || Mouse.getDY()!=0)
					event.mouseDrag(new Position(Mouse.getX(), 480-Mouse.getY()));
					//System.out.println("Mouse Drag @("+Mouse.getX()+", "+Mouse.getY()+")");
			} else if (!Mouse.isButtonDown(0) && click) {
				if (Mouse.getX() == oldPos.getX() || Mouse.getY() == oldPos.getY()) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
						event.shiftClick(new Position(Mouse.getX(), 480-Mouse.getY()));
					else
						event.mouseClick(new Position(Mouse.getX(), 480-Mouse.getY()));
				}
				else
					event.mouseUp(new Position(Mouse.getX(), 480-Mouse.getY()));
				click = false;
			}
			
			
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !pressSpace) {
				pressSpace = true;
			} else if (!Keyboard.isKeyDown(Keyboard.KEY_SPACE) && pressSpace) {
				event.spacePressed();
				pressSpace = false;
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !pressEsc) {
				pressEsc = true;
			} else if (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && pressEsc) {
				event.escPressed();
				pressEsc = false;
			}
			
			if (state == UserInteraction.STATE_DESIGNER_PLACE_ITEM) {
				if (Keyboard.isKeyDown(Keyboard.KEY_W) && !pressW) {
					pressW = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_W) && pressW) {
					event.keyPressed(Designer.ITEM_WALL);
					pressW = false;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_G) && !pressG) {
					pressG = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_G) && pressG) {
					event.keyPressed(Designer.ITEM_GLASS);
					pressG = false;
				}
	
				if (Keyboard.isKeyDown(Keyboard.KEY_M) && !pressM) {
					pressM = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_M) && pressM) {
					event.keyPressed(Designer.ITEM_MAGNET);
					pressM = false;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_B) && !pressB) {
					pressB = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_B) && pressB) {
					event.keyPressed(Designer.ITEM_BAT);
					pressB = false;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_1) && !press1) {
					press1 = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_1) && press1) {
					event.keyPressed(Designer.ITEM_WOODEN_RECTANGLE);
					press1 = false;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_2) && !press2) {
					press2 = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_2) && press2) {
					event.keyPressed(Designer.ITEM_WOODEN_TRIANGLE);
					press2 = false;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_3) && !press3) {
					press3 = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_3) && press3) {
					event.keyPressed(Designer.ITEM_METAL_RECTANGLE);
					press3 = false;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_4) && !press4) {
					press4 = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_4) && press4) {
					event.keyPressed(Designer.ITEM_METAL_TRIANGLE);
					press4 = false;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_5) && !press5) {
					press5 = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_5) && press5) {
					event.keyPressed(Designer.ITEM_CUSHION);
					press5 = false;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_6) && !press6) {
					press6 = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_6) && press6) {
					event.keyPressed(Designer.ITEM_SEESAW);
					press6 = false;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR) && !pressPgup) {
					pressPgup = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_PRIOR) && pressPgup) {
					event.keyPressed(Designer.ITEM_FACTORY_BALLOON);
					pressPgup = false;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_NEXT) && !pressPgdn) {
					pressPgdn = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_NEXT) && pressPgdn) {
					event.keyPressed(Designer.ITEM_FACTORY_METAL);
					pressPgdn = false;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_HOME) && !pressHome) {
					pressHome = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_HOME) && pressHome) {
					event.keyPressed(Designer.ITEM_FACTORY_NORMAL);
					pressHome = false;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && !pressLctrl) {
					pressLctrl = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && pressLctrl) {
					event.keyPressed(Designer.ITEM_ACCELERATION_MARK_LEFT);
					pressLctrl = false;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && !pressRctrl) {
					pressRctrl = true;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && pressRctrl) {
					event.keyPressed(Designer.ITEM_ACCELERATION_MARK_RIGHT);
					pressRctrl = false;
				}
			}
			
			// clear screen
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();

			// let subsystem paint
			frameRendering();
			
			SoundStore.get().poll(0);

			// update window contents
			Display.update();
		}

		// clean up
 		Display.destroy();
 		System.exit(0);
	}
	
	
	private void frameRendering() {
		//这里做逻辑，包括接受level信息
		//获取items信息
		//获取交互信息等等。
		Display.sync(60);
		//我提供了四个方法，根据不同state调用：
		if (state == UserInteraction.STATE_WELCOME_SCREEN) refreshWelcomeScreen() ;
		else if (state == UserInteraction.STATE_SELECT_LEVEL || 
				state == UserInteraction.STATE_SELECT_CUSTOM_LEVEL || 
				state == UserInteraction.STATE_DESIGNER_SELECT_LEVEL) 
			refreshSelectLevel();
		else if (state == UserInteraction.STATE_HOW_TO_PLAY)
			refreshHowToPlay();
		else if (state == UserInteraction.STATE_SINGLE_PLAYER_WIN ||
				state == UserInteraction.STATE_DUAL_PLAYER_WIN ||
				state == UserInteraction.STATE_DUAL_PLAYER_LOSE)
			refreshWinLoseScreen();
		else if (state == UserInteraction.STATE_DUAL_PLAYER_WAITING)
			refreshWaitingScreen();
		else refreshInGame();
 
		
		if(Display.isCloseRequested()){
			Game.gameRunning = false;
		}
	}
	private void refreshWelcomeScreen(){
		glClear(GL_COLOR_BUFFER_BIT);
		//清理屏幕
		welcomeScreen.draw(0,0);
		//绘制欢迎画面
	}
	
	private void refreshHowToPlay(){
		glClear(GL_COLOR_BUFFER_BIT);
		//清理屏幕
		if (howtoCnt == 1) howto1.draw(0, 0);
		else if (howtoCnt == 2) howto2.draw(0, 0);
		else if (howtoCnt == 3) howto3.draw(0, 0);
		else howto4.draw(0, 0);
		//绘制欢迎画面
	}
	
	private void refreshSelectLevel(){
		glClear(GL_COLOR_BUFFER_BIT);
		//清除屏幕
		if (state == UserInteraction.STATE_SELECT_LEVEL)
			selectLevel.draw(0,0);
		else
			selectLevel2.draw(0, 0);
		//绘制选关画面
		
	}
	
	private void refreshWinLoseScreen() {
		glClear(GL_COLOR_BUFFER_BIT);
		//清除屏幕
		if (state == UserInteraction.STATE_SINGLE_PLAYER_WIN)
			win_single.draw(0,0);
		else if (state == UserInteraction.STATE_DUAL_PLAYER_WIN)
			win_dual.draw(0, 0);
		else if (state == UserInteraction.STATE_DUAL_PLAYER_LOSE)
			lose_dual.draw(0, 0);
		//绘制胜利/失败画面
	}
	
	private void refreshWaitingScreen() {
		glClear(GL_COLOR_BUFFER_BIT);
		//清理屏幕
		backGroundWaiting.draw(0, 0);
		//绘制欢迎画面
	}
	
	private void refreshInGame(){
		glClear(GL_COLOR_BUFFER_BIT);
		
		if (state == UserInteraction.STATE_PLACE_ITEM)
			backGround.draw(0,0);
		else if (state == UserInteraction.STATE_DESIGNER_PLACE_ITEM)
			backGroundDesign.draw(0, 0);
		else
			backGround2.draw(0, 0);
			
		//清除屏幕，重新绘制背景
		//以下逐item绘制到相应位置
		
		if (items == null) return;
		
		for(Item item:items){
			if (item instanceof Unmovable && ((Unmovable)item).isDisappear()){
    				continue;
    			}
			if(item instanceof Flag){
				drawItem(item, -20,-25);
			}
			else if(item instanceof Magnet){
				drawItem(item,-40,-40);
			}
			else if(item instanceof TriangleArea){
				drawTriangleItem(item);
			}
			else if(item instanceof RectangleArea){
				drawRectangleItem(item);
			}
			else if (item instanceof CircleArea){
				drawCircleItem(item);
			}
			else if (item instanceof WoodenSeesaw){
				drawWoodenSeesa(item);
			}
			
			
 		}
	}
	

	/*
	 * 获取item的材质
	 */
	private String getMaterial(Item item){
		String material;
		if(item instanceof WoodenItem){
			material  =  "wooden";
		}
		else if(item instanceof MetalItem){
			material = "metal";
		}
		else if(item instanceof Glass){
			material = "glass";
		}
		else if(item instanceof Cushion){
			material = "cushion";
		}
		else if(item instanceof Flag){
			material = "flag";
		}
		else if(item instanceof Magnet){
			if (((Magnet)item).isFunctioning())
				material = "magnet_on";
			else
				material = "magnet";
		}
		else if(item instanceof BallFactory) {
			if (((BallFactory)item).getTargetState() == Ball.NORMAL_BALL)
				material = "factory_normal";
			else if (((BallFactory)item).getTargetState() == Ball.METAL_BALL)
				material = "factory_metal";
			else
				material = "factory_balloon";
		}
		else if (item instanceof Ball) {
			if (((Ball)item).getState() == Ball.NORMAL_BALL) material = "ball_normal";
			else if (((Ball)item).getState() == Ball.METAL_BALL) material = "ball_metal";
			else material = "ball_balloon";
		}
		else if (item instanceof Bat)
			material = "bat";
		else if (item instanceof AccelerationMark) {
			if (((AccelerationMark)item).getDirection() == AccelerationMark.LEFT_DIRECTION)
				material = "left_mark";
			else
				material = "right_mark";
		}
		else{
			//未定义的涂黑
			material = "wall";
		}
		return material;
	}
	
	private void drawItem(Item item, int xOffset, int yOffset) {
		Sprite itemSprite = getSprite("resources/"+getMaterial(item)+".png");
		
		itemSprite.draw(item.getPosition().getX()+xOffset, item.getPosition().getY()+yOffset);
	}
	
	private void drawWoodenSeesa(Item item) {
		Sprite itemSprite = getSprite("resources/"+getMaterial(item)+".png");
		
		itemSprite.drawWoodenSeesaw(((WoodenSeesaw)item).getVertex());
	}
	private void drawRectangleItem(Item item) {
		Sprite itemSprite = getSprite("resources/"+getMaterial(item)+".png");
		
		itemSprite.drawRectangle(((RectangleArea)item).getVertex());
	}

	private void drawTriangleItem(Item item){
		Sprite itemSprite = getSprite("resources/"+getMaterial(item)+".png");
	 
		itemSprite.drawTriangle(((TriangleArea)item).getVertex());
		
	}
	
	private void drawCircleItem(Item item){
		Sprite itemSprite = getSprite("resources/"+getMaterial(item)+".png");
		 
		itemSprite.drawCircle(item.getPosition(),((CircleArea)item).getRadius());
	}
	
	public Sprite getSprite(String ref){
		return new Sprite(textureLoader, ref);
	}
}
