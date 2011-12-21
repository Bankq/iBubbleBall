package network;

import item.Item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class readStream {
	public static void main(String[] args){
		File findfile = new File("temp.txt");
		ObjectInputStream input = null;
		try {
			input = new ObjectInputStream(new FileInputStream(findfile));
			while(true){
				Item[] item = (Item[]) input.readObject();
				if(item !=null && item.length>0)
					System.out.println("Server(level1):"+item[0].getPosition().getX()+","+item[0].getPosition().getY());
				else
					break;
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
}
