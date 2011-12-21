package network;
import level.Level;
import main.UserInteraction;
import item.Item;
import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class Client
{
	private ArrayList<Level> levelList;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	private Level myLevel;
	private String opponentName;
	private String myName;
	private boolean defeated;
	private boolean win;
	private boolean error;
	private UserInteraction userInteraction;
	private String address;
	private Socket socket;
	public Client(String myName,String address,UserInteraction userInteraction){
		defeated = false;
		win = false;
		error = false;
		this.myName = myName;
		this.address = address;
		this.userInteraction = userInteraction;
		new Thread(new HandleClient()).start();
	}
	@SuppressWarnings("unchecked")
	private void connect() throws UnknownHostException, IOException, ClassNotFoundException
	{	
	//	Socket socket = new Socket("localhost",8000);
		socket = new Socket(address,8000);
		toServer = new ObjectOutputStream(socket.getOutputStream());
		fromServer = new ObjectInputStream(socket.getInputStream());
		toServer.writeObject(myName);
		
		opponentName = (String)(fromServer.readObject());
		
		levelList = (ArrayList<Level>) (fromServer.readObject());
		System.out.println("Connected!\nOpponent: "+opponentName);
	}
	private void exchangeState() throws IOException, ClassNotFoundException{
		
		toServer.writeObject(new Going());
		toServer.flush();
		
		Object o = fromServer.readObject();
		if(o instanceof Win){
			defeated = true;
			releaseConnection();
			userInteraction.instantLose();
		}
		else if(o instanceof Jam){
			userInteraction.instantJam();
		}
	}
	public Level getNextLevel(){
		if(levelList.size()>0 && levelList.size()<5){
			try {
				jam();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		if(levelList.size()>0){
			int aLevel = (int) ((Math.random()*levelList.size()));
			myLevel = levelList.get(aLevel);
			levelList.remove(aLevel);
			return myLevel;
		}
		else{
			try {
				win();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			return null;
		}
	}

	public String getOpponentName(){
		return opponentName;
	}
	public boolean isDefeated(){
		return defeated;
	}
	public boolean isError(){
		return error;
	}
	private void win() throws IOException{
		win = true;
		try {
			toServer.writeObject(new Win());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	private void jam() throws IOException{
		try {
			toServer.writeObject(new Jam());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	private void releaseConnection(){
		try {
			toServer.close();
			fromServer.close();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	class HandleClient implements Runnable{
		
		public void run() {

			try {
				System.out.println("Connectting to server!");
				connect();
				
				if(!userInteraction.waitingDone()){
					socket.close();
					return;
				}
				while(!win && !defeated){
					
					exchangeState();
					Thread.sleep(100);
				}
				
			}catch (IOException e) {
				e.printStackTrace();
				error = true;
				userInteraction.instantError();
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO 自动生成 catch 块
					e1.printStackTrace();
				}
				
			} catch (ClassNotFoundException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
	}
}