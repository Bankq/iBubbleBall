package logic;

import level.*;
import item.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import level.Level;

public class DebugDraw extends JFrame{
	private GameWorld gameWorld = new GameWorld();
	private static Level level;
	private static GameLogic gameLogic;
	public DebugDraw()
	{
		add(gameWorld);
	}
	public static void main(String[] args) throws Exception{
    	level=new Level();
  //  	level.makeLevel();
    	level.initLevel("level10");
    	
    	gameLogic = new GameLogic(level);
    	gameLogic.logicInit();
    	
    	DebugDraw iBubbleBall = new DebugDraw();
    	iBubbleBall.setTitle("iBubbleBall(debug version)");
    	iBubbleBall.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	iBubbleBall.setSize(700,540);
    	iBubbleBall.setResizable(false);
    	iBubbleBall.setVisible(true);
    }
	static class GameWorld extends JPanel{
		private Timer timer;
		public GameWorld(){
		    timer = new Timer(17, new TimerListener());
		    timer.start();
		}
		public void paintComponent(Graphics g){
		    	super.paintComponent(g);
		    	
		    	ArrayList<Item> settings = level.getSettings();
		    	ArrayList<Item> dynamicTools = level.getDynamicTools();
		    	
		    	for(int i=0;i<settings.size();i++)
		    	{
		    		Item item = settings.get(i);
		    		
		    		if(item instanceof BallFactory){
		    			g.drawOval(item.getPosition().getX()-((BallFactory) item).getRadius(), 
		    					item.getPosition().getY()-((BallFactory) item).getRadius(), 
		    					2*((BallFactory) item).getRadius(), 
		    					2*((BallFactory) item).getRadius());	
		    	    }
		    		else if(item instanceof Wall){
		    			int[] x=new int[4];
		    			int[] y=new int[4];
		    			for(int j=0;j<4;j++)
		    			{
		    				x[j]=((Wall) item).getVertex()[j].getX();
		    				y[j]=((Wall) item).getVertex()[j].getY();
		    			}
		    			g.setColor(Color.black);
		    			g.fillPolygon(x,y,4);
		    	    }
		    		else if(item instanceof RectangleMetalItem){
		    			int[] x=new int[4];
		    			int[] y=new int[4];
		    			for(int j=0;j<4;j++)
		    			{
		    				x[j]=((RectangleMetalItem) item).getVertex()[j].getX();
		    				y[j]=((RectangleMetalItem) item).getVertex()[j].getY();
		    			}
		    			g.setColor(Color.gray);
		    			g.fillPolygon(x,y,4);  	
		    	    }
		    		else if(item instanceof TriangleMetalItem){
		    			int[] x=new int[3];
		    			int[] y=new int[3];
		    			for(int j=0;j<3;j++)
		    			{
		    				x[j]=((TriangleMetalItem) item).getVertex()[j].getX();
		    				y[j]=((TriangleMetalItem) item).getVertex()[j].getY();
		    			}
		    			g.setColor(Color.gray);
		    			g.fillPolygon(x,y,3);  	  	 	
		    	    }
		    	}
		    
		    	for(int i=0;i<dynamicTools.size();i++)
		    	{
		    		Item item = dynamicTools.get(i);
		    		if(item instanceof Bat){
		    			int[] x=new int[4];
		    			int[] y=new int[4];
		    			for(int j=0;j<4;j++)
		    			{
		    				x[j]=((Bat) item).getVertex()[j].getX();
		    				y[j]=((Bat) item).getVertex()[j].getY();
		    			}
		    			g.drawPolygon(x,y,4);  		
		    	    }
		    		else if(item instanceof Ball){
		    			g.setColor(Color.GREEN);
		    			g.fillOval(item.getPosition().getX()-((Ball) item).getRadius(), 
		    					item.getPosition().getY()-((Ball) item).getRadius(), 
		    					2*((Ball) item).getRadius(), 
		    					2*((Ball) item).getRadius());
		    			//System.out.printf("x: %d.y: %d\n",item.getPosition().getX(),item.getPosition().getY());
		    		}
		    		else if(item instanceof Flag){
		    				g.setColor(Color.red);
		    				g.fillOval(item.getPosition().getX()-((Flag) item).getRadius(), 
		    						item.getPosition().getY()-((Flag) item).getRadius(), 
		    						2*((Flag) item).getRadius(), 
		    						2*((Flag) item).getRadius());
		    				//System.out.println(item.getPosition().getX()+","+item.getPosition().getY()+","+((Flag) item).getRadius());
		    				
		    				
		    		}
		    		else if(item instanceof Magnet){
		    			g.drawOval(item.getPosition().getX()-((Magnet) item).getRadius(), 
		    					item.getPosition().getY()-((Magnet) item).getRadius(), 
		    					2*((Magnet) item).getRadius(), 
		    					2*((Magnet) item).getRadius()); 	
		    	    }
		    		if(item instanceof Cushion){
		    			int[] x=new int[4];
		    			int[] y=new int[4];
		    			for(int j=0;j<4;j++)
		    			{
		    				x[j]=((Cushion) item).getVertex()[j].getX();
		    				y[j]=((Cushion) item).getVertex()[j].getY();
		    			}
		    			g.setColor(Color.cyan);
		    			g.fillPolygon(x,y,4);  	 	
		    	    }
		    		else if(item instanceof AccelerationMark){
		    			g.setColor(Color.orange);
		    			g.fillOval(item.getPosition().getX()-((AccelerationMark) item).getRadius(), 
		    					item.getPosition().getY()-((AccelerationMark) item).getRadius(), 
		    					2*((AccelerationMark) item).getRadius(), 
		    					2*((AccelerationMark) item).getRadius());  	  	
		    	    }
		    		else if(item instanceof RectangleWoodenItem){
		    			int[] x=new int[4];
		    			int[] y=new int[4];
		    			for(int j=0;j<4;j++)
		    			{
		    				x[j]=((RectangleWoodenItem) item).getVertex()[j].getX();
		    				y[j]=((RectangleWoodenItem) item).getVertex()[j].getY();
		    			}
		    			g.setColor(Color.yellow);
		    			g.fillPolygon(x,y,4);  			
		    	    }
		    		else if(item instanceof TriangleWoodenItem){
		    			int[] x=new int[3];
		    			int[] y=new int[3];
		    			for(int j=0;j<3;j++)
		    			{
		    				x[j]=((TriangleWoodenItem) item).getVertex()[j].getX();
		    				y[j]=((TriangleWoodenItem) item).getVertex()[j].getY();
		    			}
		    			g.setColor(Color.yellow);
		    			g.fillPolygon(x,y,3);  		 	
		    	    }
		    		else if(item instanceof WoodenSeesaw){
		    			int[] x=new int[7];
		    			int[] y=new int[7];
		    			for(int j=0;j<7;j++)
		    			{
		    				x[j]=((WoodenSeesaw) item).getVertex()[j].getX();
		    				y[j]=((WoodenSeesaw) item).getVertex()[j].getY();
		    			}
		    			g.setColor(Color.yellow);
		    			g.fillPolygon(x,y,7);  		
		    	    }
		    		else if(item instanceof Coin){
		    			if(!((Coin) item).isDisappear()){
		    				g.setColor(Color.PINK);
		    				g.fillOval(item.getPosition().getX()-((Coin) item).getRadius(), 
		    						item.getPosition().getY()-((Coin) item).getRadius(), 
		    						2*((Coin) item).getRadius(), 
		    						2*((Coin) item).getRadius());
		    			}
		    	    }
		    		else if(item instanceof Glass){
		    			if(!((Glass) item).isDisappear()){
		    				int[] x=new int[4];
		    				int[] y=new int[4];
		    				for(int j=0;j<4;j++)
		    				{
		    					x[j]=((Glass) item).getVertex()[j].getX();
		    					y[j]=((Glass) item).getVertex()[j].getY();
		    				}
		    				g.drawPolygon(x,y,4);
		    			}
		    	    }
		    	}
		}
		    	
		class TimerListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
		    	if(gameLogic.refreshLogic()){
		    		JOptionPane.showMessageDialog(null, "You win!");
		    		timer.stop();
		    	}
		    	else{
		    		repaint();
		    	}
		    	
			}
		}
	}
}
