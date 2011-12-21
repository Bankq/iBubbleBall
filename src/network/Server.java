package network;
import item.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import level.Level;

public class Server{
	public Server(){
		
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(8000);
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		int sessionNo = 1;
		while(true){
			try{
				Socket player1 = serverSocket.accept();
				System.out.println("player1 connected!");
				Socket player2 = serverSocket.accept();
				System.out.println("player2 connected!");
				sessionNo++;
				
				HandleSession task = new HandleSession(player1,player2);
				new Thread(task).start();
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
	}
		
	public static void main(String[] args){
		System.out.println("Server starts!");
		new Server();	
	}
}
class HandleSession implements Runnable{
	private Socket player1;
	private Socket player2;
	
	private ArrayList<Level> levels;

	private ObjectInputStream fromPlayer1;
	private ObjectInputStream fromPlayer2;
	private ObjectOutputStream toPlayer1;
	private ObjectOutputStream toPlayer2;
	
	HandleSession(Socket player1,Socket player2){
		this.player1 = player1;
		this.player2 = player2;
		levels = new ArrayList<Level>();
		try {
			fromPlayer1 = new ObjectInputStream(this.player1.getInputStream());
			fromPlayer2 = new ObjectInputStream(this.player2.getInputStream());
			toPlayer1 = new ObjectOutputStream(this.player1.getOutputStream());
			toPlayer2 = new ObjectOutputStream(this.player2.getOutputStream());
			
		} catch (IOException e) {
			try {
				if(player1!=null){
					player1.close();
				}
				if(player2!=null){
					player2.close();
				}
			} catch (IOException e1) {
				// TODO 自动生成 catch 块
				e1.printStackTrace();
			}
		}
	}
	
	public void run() {
		System.out.println("generate levels!");
		boolean[] mark = new boolean[31];
		for (int i=0;i<mark.length;i++) mark[i] = false;
		while(levels.size()<5){
			int aLevel = (int) ((Math.random()*30));
			if(aLevel != 0 && !mark[aLevel]){
				mark[aLevel] = true;
				Level level = new Level();
				level.initLevel("dual_levels/level"+aLevel);
				levels.add(level);
			}
		}
		try {
			if(toPlayer1 != null && toPlayer2 !=null){
				String name2 = (String)fromPlayer2.readObject();
				String name1 = (String)fromPlayer1.readObject();
				toPlayer1.writeObject(name2);
				toPlayer2.writeObject(name1);
				System.out.println(name1+"	VS	"+name2);
				
				toPlayer1.writeObject(levels);
				toPlayer2.writeObject(levels);
				Session task1 = new Session(1);
				Session task2 = new Session(2);
				new Thread(task1).start();
				new Thread(task2).start();
			}
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}	
	}
	class Session implements Runnable{
		private int side;
		Session(int side){
			this.side=side;
		}
		private synchronized void exchange() throws IOException, ClassNotFoundException {
			if(side ==1){
				Object o = fromPlayer1.readObject();
				
				toPlayer2.writeObject(o);
				toPlayer2.flush();
			}
			else{
				Object o = fromPlayer2.readObject();
				
				toPlayer1.writeObject(o);
				toPlayer1.flush();
			}
		}
		public void run() {
			
			try {
				while(true){
					exchange();
				}
			} catch (IOException e) {
				try {
					e.printStackTrace();
					fromPlayer1.close();
					fromPlayer2.close();
					toPlayer1.close();
					toPlayer2.close();
					
				} catch (IOException e1) {
					// TODO 自动生成 catch 块
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}		
	}
}