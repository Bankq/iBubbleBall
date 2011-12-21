package level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import item.*;
import logic.*;

public class Level implements Serializable{
	String filename;
	Integer id;
	ArrayList<Item> tools, settings, dynamicTools;
	Double rate;
	boolean isStarted;
	boolean editmode;
	ArrayList<Item> dToolsBackup;
	Integer thresholdLine;
	GameLogic gl;

	public Level() {
		resetOption();
	}

	public Level(String filename) {
		initLevel(filename);
	}

	public void initLevel(String filename) {
		this.filename = filename;
		resetOption();
		loadLevel(filename);
	}
    public void setEditmode(boolean em){
    	editmode=em;
    }
	public void resetOption() {
		editmode = false;
		tools = new ArrayList<Item>();
		settings = new ArrayList<Item>();
		dynamicTools = new ArrayList<Item>();
		dToolsBackup = new ArrayList<Item>();
		rate = 0.0;
		id = 0;
		isStarted = false;
		thresholdLine = 100;
	}
    public void editorPlaceItem(Item it){
    	tools.add(it);
    	distributeItem(it, it.getPosition());
    }
    public boolean isStarted(){
    	return isStarted;
    }
    public void makeNewLevel(String levelname){

			Ball ball = new Ball(new Position(50, 150), 25);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			Flag flag = new Flag(new Position(600, 150), 36);// 40*10
			dynamicTools.add(flag);
	//		this.id = 15;
			this.saveLevel(levelname);
			filename=levelname;
	//	    this.loadLevel(levelname);
    }
    public void editorDeleteItem(Position pos) throws Exception{
    	Item it=itemSearch(pos);
    	System.out.println("kangcha");
    	if (it==null) return;
    	if (it instanceof Ball
    	|| it instanceof Flag)
    		return;
    	int i;
    	for (i = 0; i < tools.size(); i++) {
			if (it == tools.get(i)) {
				tools.remove(i);
				return ;
			}
		}
    	for (i = 0; i < settings.size(); i++) {
			if (it == settings.get(i)) {
				settings.remove(i);
				return ;
			}
		}
    	for (i = 0; i < dynamicTools.size(); i++) {
			if (it == dynamicTools.get(i)) {
				dynamicTools.remove(i);
				return ;
			}
		}
    }
	private int getRandom(int range){
	    return (new Random()).nextInt(range);
	}
	public void jamItems(){
		int i;
		int newposx=0,newposy=0;
    	for (i = 0; i < settings.size(); i++) {
			newposx=getRandom(420);
			newposy=getRandom(thresholdLine-1);
			Item it=settings.get(i);
			if (it instanceof Wall)
				continue;
			if (it instanceof TriangleArea)
				((TriangleArea)it).updateVertex(new Position(newposx,newposy));
			else if (it instanceof RectangleArea)
				((RectangleArea)it).updateVertex(new Position(newposx,newposy));
			else if (it instanceof WoodenSeesaw)
				((WoodenSeesaw)it).updateVertex(new Position(newposx,newposy));
			else 
				it.setPosition(new Position(newposx,newposy));
    	}
    	for (i = 0; i < dynamicTools.size(); i++) {
			newposx=getRandom(420);
			newposy=getRandom(thresholdLine-1);
			Item it=dynamicTools.get(i);
			if (it instanceof Ball
			|| it instanceof Flag
			|| it instanceof Glass)
				continue;
			
			if (it instanceof TriangleArea)
				((TriangleArea)it).updateVertex(new Position(newposx,newposy));
			else if (it instanceof RectangleArea)
				((RectangleArea)it).updateVertex(new Position(newposx,newposy));
			else if (it instanceof WoodenSeesaw)
				((WoodenSeesaw)it).updateVertex(new Position(newposx,newposy));
			else 
				it.setPosition(new Position(newposx,newposy));
		}
	}
	protected void loadLevel(String filename) {
		File findfile = new File("levels/"+filename);
		if (!findfile.exists()){
			System.out.println("no file");
			return;
		}
		resetOption();
		ObjectInputStream input;
		try {
			input = new ObjectInputStream(new FileInputStream(findfile));
			id = (Integer) input.readObject();
			rate = (Double) input.readObject();
			int toollen = (Integer) input.readObject();
			for (int i = 0; i < toollen; i++) {
				tools.add((Item) input.readObject());
			}
			int settingslen = (Integer) input.readObject();
			for (int i = 0; i < settingslen; i++) {
				settings.add((Item) input.readObject());
			}
			int dynamicToolslen = (Integer) input.readObject();
			for (int i = 0; i < dynamicToolslen; i++) {
				dynamicTools.add((Item) input.readObject());
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveLevel(String filename) {
		File findfile = new File("levels/"+filename);
		ObjectOutputStream output;
		try {
			output = new ObjectOutputStream(new FileOutputStream(findfile));
			output.writeObject(id);
			output.writeObject(rate);
			output.writeObject((Integer) tools.size());
			for (int i = 0; i < tools.size(); i++) {
				output.writeObject(tools.get(i));
			}
			output.writeObject((Integer) settings.size());
			for (int i = 0; i < settings.size(); i++) {
				output.writeObject(settings.get(i));
			}
			output.writeObject((Integer) dynamicTools.size());
			for (int i = 0; i < dynamicTools.size(); i++) {
				output.writeObject(dynamicTools.get(i));
			}
			output.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startLevel() throws Exception {
		isStarted = true;
		if (editmode) {
			this.saveLevel(filename);
		}
		for (int i = 0; i < dynamicTools.size(); i++) {
			dToolsBackup.add((Item) (dynamicTools.get(i).clone()));
		}
		gl = new GameLogic(this);
		gl.logicInit();
	}

	public void stopLevel() throws Exception {
		isStarted = false;
		restoreItems();
		gl = null;
	}

	boolean updateItemVertexes(Item it, Position pos) {
		// deal with convex polygon and seesaw
		if (it instanceof TriangleArea) {
			((TriangleArea) it).updateVertex(pos);
			return true;
		}
		if (it instanceof RectangleArea) {
			((RectangleArea) it).updateVertex(pos);
			return true;
		}
		if (it instanceof WoodenSeesaw) {
			((WoodenSeesaw) it).updateVertex(pos);
			return true;
		}
		return false;
	}

	Position[] getItemVertexes(Item it) throws Exception {
		// deal with convex polygon and seesaw
		if (it instanceof TriangleArea) {
			return ((TriangleArea) it).getVertex().clone();
		}
		if (it instanceof RectangleArea) {
			return ((RectangleArea) it).getVertex().clone();
		}
		if (it instanceof WoodenSeesaw) {
			Position[] pos = new Position[5];
			Position[] seesawpos = ((WoodenSeesaw) it).getVertex();
			pos[0] = (Position) (seesawpos[3].clone());
			pos[1] = (Position) (seesawpos[4].clone());
			pos[2] = (Position) (seesawpos[5].clone());
			pos[3] = (Position) (seesawpos[0].clone());
			pos[4] = (Position) (seesawpos[2].clone());
			return pos;
		}
		return null;
	}

	public boolean overlap(Item it1, Item it2) throws Exception {
		Position[] v1 = getItemVertexes(it1);
		Position[] v2 = getItemVertexes(it2);
		if (v1 != null) {
			for (int i = 0; i < v1.length; i++)
				if (inItemArea(it2, v1[i]))
					return true;
		}
		if (v2 != null) {
			for (int i = 0; i < v2.length; i++)
				if (inItemArea(it1, v2[i]))
					return true;
		}
		if (v1 != null && v2 != null) {
			if (crossConvexPolygon(v1, v2)) {
				System.out.println("cross!");
				return true;
			}
		}

		if (v1 == null && v2 != null && it1 instanceof CircleArea) {
			int r1 = ((CircleArea) it1).getRadius();
			if (it1 instanceof Magnet)
				r1 = r1 / 2;
			if (circleOnLine(v2, it1.getPosition(), r1))
				return true;
			if (this.inConvexPolygon(v2, it1.getPosition()))
				return true;
		}
		if (v1 != null && v2 == null && it2 instanceof CircleArea) {
			int r2 = ((CircleArea) it2).getRadius();
			if (it2 instanceof Magnet)
				r2 = r2 / 2;
			if (circleOnLine(v1, it2.getPosition(), r2))
				return true;
			if (this.inConvexPolygon(v1, it2.getPosition()))
				return true;
		}
		if (v1 == null && v2 == null && it1 instanceof CircleArea
				&& it2 instanceof CircleArea) {
			CircleArea ca1 = (CircleArea) it1;
			CircleArea ca2 = (CircleArea) it2;
			double dist = getDist(it1.getPosition(), it2.getPosition());
			int r1 = ca1.getRadius();
			int r2 = ca2.getRadius();
			if (it1 instanceof Magnet)
				r1 = r1 / 2;
			if (it2 instanceof Magnet)
				r2 = r2 / 2;
			if (dist < (r1 + r2))
				return true;
		}
		// System.out.println("NOToverlap!");
		return false;
	}

	public void itemRotate(Position pos, double targetangle) throws Exception {
		Item it = itemSearch(pos);
		if (it == null)
			return;

		Item tit = (Item) (it.clone());
		if (tit instanceof RectangleArea) {
			((RectangleArea) tit).rotateVertex(targetangle);
			/*
			 * Position[] ver=(((RectangleArea)tit).getVertex());
			 * System.out.println(ver[0].getX()+","+ver[0].getY());
			 */

		} else if (tit instanceof TriangleArea) {
			((TriangleArea) tit).rotateVertex(targetangle);
		} else if (tit instanceof WoodenSeesaw) {
			((WoodenSeesaw) tit).rotateVertex(targetangle);
		}
		if (!editmode) {
			if (it instanceof Wall || it instanceof Glass || it instanceof Ball
					|| it instanceof Flag)
				return;
		}
		if (it instanceof CircleArea && !(it instanceof Bat))
			return;

		Position[] vertexes = getItemVertexes(tit);
		/*
		 * boolean upthres=false,downthres=false; for (int
		 * i=0;i<vertexes.length;i++){ if (vertexes[i].getY()<= thresholdLine)
		 * upthres=true; if (vertexes[i].getY()> thresholdLine) downthres=true;
		 * }
		 * 
		 * if (upthres && downthres) return ;
		 */
		if (tit instanceof Wall || tit instanceof Glass){
			if (vertexes[0].getY() < thresholdLine
			|| vertexes[1].getY() < thresholdLine
			|| vertexes[2].getY() < thresholdLine
			|| vertexes[3].getY() < thresholdLine)
				return ;
		}
		for (int i = 0; i < settings.size(); i++) {
			if ((it != settings.get(i)) && overlap(tit, settings.get(i))) {
				return;
			}
		}
		for (int i = 0; i < dynamicTools.size(); i++) {
			if ((it != dynamicTools.get(i))
					&& overlap(tit, dynamicTools.get(i))) {
				return;
			}
		}

		it.setPosition(tit.getPosition());
		if (it instanceof RectangleArea)
			((RectangleArea) it).setVertex(((RectangleArea) tit).getVertex());
		if (it instanceof TriangleArea)
			((TriangleArea) it).setVertex(((TriangleArea) tit).getVertex());
		if (it instanceof WoodenSeesaw)
			((WoodenSeesaw) it).setVertex(((WoodenSeesaw) tit).getVertex());
	}

	private double calcTriangleArea(Position a, Position b, Position c) {
		double ab, bc, ca;
		ab = getDist(a, b);
		bc = getDist(b, c);
		ca = getDist(c, a);
		double p = (ab + bc + ca) / 2;
		return Math.sqrt(p * (p - ab) * (p - bc) * (p - ca));
	}

	public boolean inConvexPolygon(Position[] vertexes, Position pos) {
		double ori_area = 0, test_area = 0;
		for (int i = 2; i < vertexes.length; i++) {
			ori_area += calcTriangleArea(vertexes[0], vertexes[i - 1],
					vertexes[i]);
		}
		for (int i = 1; i < vertexes.length; i++) {
			test_area += calcTriangleArea(pos, vertexes[i - 1], vertexes[i]);
		}
		test_area += calcTriangleArea(pos, vertexes[vertexes.length - 1],
				vertexes[0]);

		if (Math.abs(ori_area - test_area) < 1E-8)
			return true;
		return false;
	}

	private boolean circleOnLine(Position[] v1, Position pos, int rad) {
		for (int i = 0; i < v1.length; i++) {
			Position pa1 = v1[i];
			Position pa2 = v1[(i + 1) % v1.length];
			if ((calcTriangleArea(pa1, pa2, pos) * 2.0 / getDist(pa1, pa2)) < rad) {
				double p1 = getDist(pa1, pos);
				double p2 = getDist(pa2, pos);
				double p3 = getDist(pa1, pa2);
				if (p1 * p1 + p3 * p3 > p2 * p2
						&& (p2 * p2 + p3 * p3 > p1 * p1))
					return true;
			}
		}
		return false;
	}

	private boolean crossConvexPolygon(Position[] v1, Position[] v2) {
		for (int i = 0; i < v1.length; i++) {
			Position pa1 = v1[i];
			Position pa2 = v1[(i + 1) % v1.length];
			for (int j = 0; j < v2.length; j++) {
				Position pb1 = v2[j];
				Position pb2 = v2[(j + 1) % v2.length];
				if (lineCross(pa1, pa2, pb1, pb2))
					return true;
			}
		}
		// System.out.println("NOTcross!");
		return false;
	}

	private boolean lineCross(Position posa1, Position posa2, Position posb1,
			Position posb2) {
		// System.out.println("a1:"+posa1.getX()+","+posa1.getY()+" a2:"+posa2.getX()+","+posa2.getY()+" b1:"+posb1.getX()+","+posb1.getY()+" b2:"+posb2.getX()+","+posb2.getY()+"\n");

		if (posa1.getX() == posa2.getX() && posb1.getX() == posb2.getX()) {
			return false;
		}

		double px = 0, py = 0;
		if (posa1.getX() == posa2.getX() && posb1.getX() != posb2.getX()) {
			double linebk = ((double) posb1.getY() - posb2.getY())
					/ (posb1.getX() - posb2.getX());
			double linebb = posb1.getY() - linebk * posb1.getX();
			px = posa2.getX();
			py = px * linebk + linebb;
		}
		if (posb1.getX() == posb2.getX() && posa1.getX() != posa2.getX()) {
			double lineak = ((double) posa1.getY() - posa2.getY())
					/ (posa1.getX() - posa2.getX());
			double lineab = posa1.getY() - lineak * posa1.getX();
			px = posb2.getX();
			py = px * lineak + lineab;
		}
		if (posb1.getX() != posb2.getX() && posa1.getX() != posa2.getX()) {

			double lineak = ((double) posa1.getY() - posa2.getY())
					/ (posa1.getX() - posa2.getX());
			double lineab = posa1.getY() - lineak * posa1.getX();
			double linebk = ((double) posb1.getY() - posb2.getY())
					/ (posb1.getX() - posb2.getX());
			double linebb = posb1.getY() - linebk * posb1.getX();
			if (Math.abs(lineak - linebk) < 1E-8)
				return false;
			px = (lineab - linebb) / (linebk - lineak);
			py = lineak * px + lineab;
		}

		// System.out.println("a1:"+posa1.getX()+","+posa1.getY()+" a2:"+posa2.getX()+","+posa2.getY()+" b1:"+posb1.getX()+","+posb1.getY()+" b2:"+posb2.getX()+","+posb2.getY()+" newp:"+px+","+py+"\n");
		Position newp = new Position((int) px, (int) py);
		if (Math.abs(getDist(newp, posa1) + getDist(newp, posa2)
				- getDist(posa1, posa2)) < 1E-1
				&& Math.abs(getDist(newp, posb1) + getDist(newp, posb2)
						- getDist(posb1, posb2)) < 1E-1) {

			return true;
		}

		// System.out.println("1:"+Math.abs(getDist(newp,posa1)+getDist(newp,posa2)-
		// getDist(posa1,posa2))+"\n");
		// System.out.println("2:"+Math.abs(getDist(newp,posb1)+getDist(newp,posb2)-
		// getDist(posb1,posb2))+"\n");
		return false;
	}

	private double getDist(Position a, Position b) {
		return Math.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX())
				+ (a.getY() - b.getY()) * (a.getY() - b.getY()));
	}

	public boolean inItemArea(Item it, Position pos) throws Exception {
		Position[] vertexes = getItemVertexes(it);
		if (it instanceof Bat) {
			int ra = (int) getDist(vertexes[0], vertexes[1]) / 2;
			Position cen = vertexes[0];
			double dist = getDist(cen, pos);
			if (dist <= ra || inConvexPolygon(vertexes, pos))
				return true;
			return false;
		}
		if (it instanceof Magnet) {
			int ra = ((CircleArea) it).getRadius() / 2;
			Position cen = it.getPosition();
			double dist = getDist(cen, pos);
			if (dist <= ra)
				return true;
			return false;
		}
		if (vertexes != null) {
			return inConvexPolygon(vertexes, pos);
		}
		if (it instanceof CircleArea) {
			int ra = ((CircleArea) it).getRadius();
			Position cen = it.getPosition();
			double dist = Math.sqrt((cen.getX() - pos.getX())
					* (cen.getX() - pos.getX()) + (cen.getY() - pos.getY())
					* (cen.getY() - pos.getY()));
			if (dist <= ra)
				return true;
			return false;
		}
		System.out.println("cannot decide whether in item area.");
		return false;
	}

	public Item itemSearch(Position pos) throws Exception {
		for (int i = 0; i < tools.size(); i++)
			if (inItemArea(tools.get(i), pos)) {
				return tools.get(i);
			}
		for (int i = 0; i < settings.size(); i++)
			if (inItemArea(settings.get(i), pos)) {
				return settings.get(i);
			}
		for (int i = 0; i < dynamicTools.size(); i++)
			if (inItemArea(dynamicTools.get(i), pos)) {
				/*
				 * Position[]
				 * ver=(((RectangleArea)(dynamicTools.get(i))).getVertex());
				 * System.out.println(ver[0].getX()+","+ver[0].getY());
				 */
				return dynamicTools.get(i);
			}

		return null;
	}

	public boolean refresh() {
		if (isStarted == true) {
			return gl.refreshLogic();
		}
		return false;
	}

	public void restoreItems() {
		dynamicTools = dToolsBackup;
		dToolsBackup = new ArrayList<Item>();
	}

	public void distributeItem(Item it, Position pos) {
//		System.out.println("Tools");
		boolean intools = true;
		int i;
		for (i = 0; i < tools.size(); i++)
			if (it == tools.get(i))
				break;
		if (i < tools.size())
			intools = true;
		else
			intools = false;
		if (pos.getY() < thresholdLine && intools) {
			if (it instanceof Wall) {
				tools.remove(i);
				settings.add(it);
			}
			if (it instanceof Ball
				|| it instanceof Flag
				 || it instanceof Glass) {
						tools.remove(i);
						dynamicTools.add(it);
			}
		}
		if (pos.getY() >= thresholdLine && intools) {
			// Tools down
		//	System.out.println("Tools down");
			if (it instanceof MetalItem || it instanceof BallFactory
			 || it instanceof Wall) {
				tools.remove(i);
				settings.add(it);
			}
			if (it instanceof DynamicItem || it instanceof WoodenItem
					|| it instanceof Cushion || it instanceof AccelerationMark
					|| it instanceof WoodenSeesaw
					 || it instanceof Glass) {
				tools.remove(i);
				dynamicTools.add(it);
			}
		}
		if (pos.getY() < thresholdLine && !intools) {
			// Tools up

	//		System.out.println("Tools up");
			if (it instanceof MetalItem || it instanceof BallFactory) {
				int j;
				for (j = 0; j < settings.size(); j++)
					if (it == settings.get(j))
						break;
				settings.remove(j);
				tools.add(it);
			}
			if (it instanceof DynamicItem || it instanceof WoodenItem
					|| it instanceof Cushion || it instanceof AccelerationMark
					|| it instanceof WoodenSeesaw) {
				int j;
				for (j = 0; j < dynamicTools.size(); j++)
					if (it == dynamicTools.get(j))
						break;
				dynamicTools.remove(j);
				tools.add(it);
			}
		}
	}

	public boolean validatePosition(Item it, Position pos) throws Exception {
		if (!editmode) {
			if (it instanceof Wall || it instanceof Glass || it instanceof Ball
					|| it instanceof Flag)
				return false;
		}
		
		Item tit = (Item) (it.clone());
		// tit.setPosition(pos);
		if (updateItemVertexes(tit, pos)) {
			/*
			 * Position[] vertexes=getItemVertexes(tit); boolean
			 * upthres=false,downthres=false; for (int
			 * i=0;i<vertexes.length;i++){ if (vertexes[i].getY()<=
			 * thresholdLine) upthres=true; if (vertexes[i].getY()>
			 * thresholdLine) downthres=true; }
			 * 
			 * if (upthres && downthres) return false;
			 */
		} else {
			if (it instanceof CircleArea) {
				/*
				 * Position pa1=new Position(0,thresholdLine); Position pa2=new
				 * Position(640,thresholdLine); int
				 * rad=((CircleArea)it).getRadius(); if ((calcTriangleArea(pa1,
				 * pa2, it.getPosition())*2.0/getDist(pa1,pa2))<rad){ return
				 * false; }
				 */
				tit.setPosition(pos);
			}
		}
		if (tit.getPosition().getY() < thresholdLine && (tit instanceof Ball
				|| tit instanceof Flag))
			return false;
		if (tit instanceof Wall || tit instanceof Glass){
			Position[] vertexes=getItemVertexes(tit); 
			if (vertexes[0].getY() < thresholdLine
			|| vertexes[1].getY() < thresholdLine
			|| vertexes[2].getY() < thresholdLine
			|| vertexes[3].getY() < thresholdLine)
				return false;
		}
		for (int i = 0; i < settings.size(); i++) {
			// System.out.println("in set"+i);
			if ((it != settings.get(i)) && overlap(tit, settings.get(i))) {
				// System.out.println("overlapset");
				return false;
			}
		}
		for (int i = 0; i < dynamicTools.size(); i++) {
			// System.out.println("in dTools"+i);
			if ((it != dynamicTools.get(i))
					&& overlap(tit, dynamicTools.get(i))) {
				// System.out.println("overlapdT"+i);
				return false;
			}
		}
		for (int i = 0; i < tools.size(); i++) {
			if (it == tools.get(i)) {
				tools.set(i, tit);
				return true;
			}
		}
		for (int i = 0; i < dynamicTools.size(); i++) {
			if (it == dynamicTools.get(i)) {
				dynamicTools.set(i, tit);
				return true;
			}
		}
		for (int i = 0; i < settings.size(); i++) {
			if (it == settings.get(i)) {
				settings.set(i, tit);
				return true;
			}
		}
		return true;
	}

	public Item[] getAllItems() {
		ArrayList<Item> tallitems = new ArrayList<Item>();

		for (int i = 0; i < tools.size(); i++) {
			tallitems.add(tools.get(i));
		}
		for (int i = 0; i < settings.size(); i++) {
			tallitems.add(settings.get(i));
		}
		for (int i = 0; i < dynamicTools.size(); i++) {
			if (dynamicTools.get(i) instanceof Unmovable) {
				if (!((Unmovable) dynamicTools.get(i)).isDisappear()) {
					tallitems.add(dynamicTools.get(i));
				}
			} else
				tallitems.add(dynamicTools.get(i));
		}
		Item[] alt = new Item[tallitems.size()];
		for (int i = 0; i < tallitems.size(); i++)
			alt[i] = tallitems.get(i);
		return alt;
	}

	public void changeDTools(Position pos) throws Exception {
		Item it = itemSearch(pos);
		if (it instanceof DynamicItem) {
			boolean fuc = ((DynamicItem) it).isFunctioning();
			((DynamicItem) it).setFunctioning(!fuc);
		}

	}

	public void makeLevel() throws Exception {

		// kangchaml

		int runid = 8;
		{

			Position[] rwitpos = new Position[4];
			rwitpos[0] = new Position(5, 0);
			rwitpos[1] = new Position(10, 0);
			rwitpos[2] = new Position(10, 15);
			rwitpos[3] = new Position(5, 15);
			Position[] rwitpos1 = new Position[4];
			rwitpos1[0] = new Position(0, 5);
			rwitpos1[1] = new Position(15, 5);
			rwitpos1[2] = new Position(15, 10);
			rwitpos1[3] = new Position(0, 10);

			System.out.println(this.crossConvexPolygon(rwitpos, rwitpos1));
		}

		// kangcha1
		if (runid == 0) {
			RectangleWoodenItem rwit = new RectangleWoodenItem();// 20*20
			Position[] rwitpos = new Position[4];
			rwitpos[0] = new Position(40, 110);
			rwitpos[1] = new Position(60, 110);
			rwitpos[2] = new Position(60, 130);
			rwitpos[3] = new Position(40, 130);
			rwit.setVertex(rwitpos);

			// tools.add(rwit);

			Magnet mgt = new Magnet(new Position(40, 200));
			mgt.setRadius(10);

			// tools.add(mgt);

			// in game area
			TriangleWoodenItem twi = new TriangleWoodenItem(new Position(300,
					250));// 20*20
			Position[] twipos = new Position[3];
			twipos[0] = new Position(280, 230);
			twipos[1] = new Position(280, 200);
			twipos[2] = new Position(310, 230);
			twi.setVertex(twipos);

			dynamicTools.add(twi);

			TriangleMetalItem tmi = new TriangleMetalItem(new Position(130, 60));// 20*20
			Position[] tmipos = new Position[3];
			tmipos[0] = new Position(120, 50);
			tmipos[1] = new Position(140, 50);
			tmipos[2] = new Position(140, 80);
			tmi.setVertex(tmipos);

			// dynamicTools.add(tmi);

			RectangleWoodenItem rwi = new RectangleWoodenItem(new Position(150,
					120));// 20*20
			Position[] rwipos = new Position[4];
			rwipos[0] = new Position(140, 110);
			rwipos[1] = new Position(160, 110);
			rwipos[2] = new Position(160, 130);
			rwipos[3] = new Position(140, 130);
			rwi.setVertex(rwipos);

			// dynamicTools.add(rwi);

			Magnet mg = new Magnet(new Position(200, 60));
			mg.setRadius(10);

			// dynamicTools.add(mg);

			// settings
			Wall wall1 = new Wall(new Position(450, 250));// 10*80
			Position[] wall1pos = new Position[4];
			wall1pos[0] = new Position(400, 100);
			wall1pos[1] = new Position(500, 100);
			wall1pos[2] = new Position(500, 400);
			wall1pos[3] = new Position(400, 400);
			wall1.setVertex(wall1pos);

			settings.add(wall1);

			Wall wall2 = new Wall(new Position(300, 360));// 40*10
			Position[] wall2pos = new Position[4];
			wall2pos[0] = new Position(200, 320);
			wall2pos[1] = new Position(400, 320);
			wall2pos[2] = new Position(400, 400);
			wall2pos[3] = new Position(200, 400);
			wall2.setVertex(wall2pos);

			settings.add(wall2);

			Wall wall3 = new Wall(new Position(225, 250));// 40*10
			Position[] wall3pos = new Position[4];
			wall3pos[0] = new Position(200, 100);
			wall3pos[1] = new Position(250, 100);
			wall3pos[2] = new Position(250, 400);
			wall3pos[3] = new Position(200, 400);
			wall3.setVertex(wall3pos);

			settings.add(wall3);

			Glass glass = new Glass(new Position(300, 210));// 40*10
			Position[] glasspos = new Position[4];
			glasspos[0] = new Position(280, 200);
			glasspos[1] = new Position(420, 200);
			glasspos[2] = new Position(420, 220);
			glasspos[3] = new Position(280, 220);
			glass.setVertex(glasspos);
			glass.setEnergyThreshold(7);

			dynamicTools.add(glass);

			Ball ball = new Ball(new Position(300, 20), 12);// 40*10
			ball.setState(Ball.METAL_BALL);
			dynamicTools.add(ball);

			Flag flag = new Flag(new Position(20, 20), 15);// 40*10

			dynamicTools.add(flag);

			// BallFactory factory=new BallFactory(new
			// Position(300,240),Ball.METAL_BALL, 12);//40*10
			// settings.add(factory);

			// Coin coin=new Coin(new Position(300,240), 12);//40*10
			// dynamicTools.add(coin);

			// AccelerationMark am=new AccelerationMark(new
			// Position(300,240),1,12);//40*10
			// dynamicTools.add(am);

			Magnet ma = new Magnet(new Position(330, 140), 30);// 40*10
			ma.setFunctioning(true);
			dynamicTools.add(ma);

			Bat bat = new Bat(new Position(50, 215));// 40*10
			Position[] batPos = new Position[4];
			batPos[0] = new Position(50, 210);
			batPos[1] = new Position(100, 210);
			batPos[2] = new Position(100, 220);
			batPos[3] = new Position(50, 220);
			bat.setVertex(batPos);
			bat.setFunctioning(true);
			dynamicTools.add(bat);

			this.saveLevel("level0");
		}

		// kangcha1
		if (runid == 1) {
			// settings
			Wall wall1 = new Wall();// 10*80
			Position[] wall1pos = new Position[4];
			wall1pos[0] = new Position(0, 300);
			wall1pos[1] = new Position(640, 300);
			wall1pos[2] = new Position(640, 480);
			wall1pos[3] = new Position(0, 480);
			wall1.setVertex(wall1pos);

			settings.add(wall1);
			// dynamicTools
			TriangleWoodenItem twi1 = new TriangleWoodenItem();// 20*20
			Position[] twipos1 = new Position[3];
			twipos1[0] = new Position(90, 20);
			twipos1[1] = new Position(170, 100);
			twipos1[2] = new Position(90, 100);
			twi1.setVertex(twipos1);
			dynamicTools.add(twi1);

			Ball ball = new Ball(new Position(100, 150), 30);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			Flag flag = new Flag(new Position(425, 264), 36);// 40*10

			dynamicTools.add(flag);
			this.id = 1;
			this.saveLevel("level1");
		}
		// kangcha2
		if (runid == 2) {
			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(18, 171);
			wallpos1[1] = new Position(190, 283);
			wallpos1[2] = new Position(180, 300);
			wallpos1[3] = new Position(9, 189);
			wall1.setVertex(wallpos1);
			settings.add(wall1);

			Wall wall2 = new Wall();// 40*10
			Position[] wallpos2 = new Position[4];
			wallpos2[0] = new Position(400, 278);
			wallpos2[1] = new Position(640, 278);
			wallpos2[2] = new Position(640, 300);
			wallpos2[3] = new Position(400, 300);
			wall2.setVertex(wallpos2);

			settings.add(wall2);

			RectangleMetalItem rmi2 = new RectangleMetalItem();// 20*20
			Position[] rmipos2 = new Position[4];
			rmipos2[0] = new Position(25, 60);
			rmipos2[1] = new Position(225, 60);
			rmipos2[2] = new Position(225, 100);
			rmipos2[3] = new Position(25, 100);
			rmi2.setVertex(rmipos2);
			settings.add(rmi2);

			Ball ball = new Ball(new Position(47, 150), 30);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			Flag flag = new Flag(new Position(558, 241), 36);// 40*10
			dynamicTools.add(flag);

			this.id = 2;
			this.saveLevel("level2");
		}
		// kangcha3
		if (runid == 3) {
			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(1, 300);
			wallpos1[1] = new Position(266, 300);
			wallpos1[2] = new Position(266, 338);
			wallpos1[3] = new Position(1, 338);
			wall1.setVertex(wallpos1);
			settings.add(wall1);

			Wall wall2 = new Wall();// 40*10
			Position[] wallpos2 = new Position[4];
			wallpos2[0] = new Position(400, 300);
			wallpos2[1] = new Position(500, 300);
			wallpos2[2] = new Position(500, 337);
			wallpos2[3] = new Position(400, 337);
			wall2.setVertex(wallpos2);
			settings.add(wall2);

			Wall wall3 = new Wall();// 40*10
			Position[] wallpos3 = new Position[4];
			wallpos3[0] = new Position(533, 225);
			wallpos3[1] = new Position(639, 225);
			wallpos3[2] = new Position(639, 262);
			wallpos3[3] = new Position(534, 262);
			wall3.setVertex(wallpos3);
			settings.add(wall3);

			RectangleWoodenItem rwi = new RectangleWoodenItem();// 20*20
			Position[] rwipos = new Position[4];
			rwipos[0] = new Position(25, 30);
			rwipos[1] = new Position(225, 30);
			rwipos[2] = new Position(225, 70);
			rwipos[3] = new Position(25, 70);
			rwi.setVertex(rwipos);
			dynamicTools.add(rwi);

			RectangleWoodenItem rwi1 = new RectangleWoodenItem();// 20*20
			Position[] rwipos1 = new Position[4];
			rwipos1[0] = new Position(25, 80);
			rwipos1[1] = new Position(225, 80);
			rwipos1[2] = new Position(225, 120);
			rwipos1[3] = new Position(25, 120);
			rwi1.setVertex(rwipos1);
			dynamicTools.add(rwi1);

			Flag flag = new Flag(new Position(45, 264), 36);// 40*10
			dynamicTools.add(flag);

			Ball ball = new Ball(new Position(600, 150), 30);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			this.id = 3;
			this.saveLevel("level3");
		}
		// kangcha4
		if (runid == 4) {
			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(1, 300);
			wallpos1[1] = new Position(134, 300);
			wallpos1[2] = new Position(134, 345);
			wallpos1[3] = new Position(1, 345);
			wall1.setVertex(wallpos1);
			settings.add(wall1);

			Wall wall2 = new Wall();// 40*10
			Position[] wallpos2 = new Position[4];
			wallpos2[0] = new Position(133, 315);
			wallpos2[1] = new Position(266, 315);
			wallpos2[2] = new Position(266, 345);
			wallpos2[3] = new Position(134, 345);
			wall2.setVertex(wallpos2);
			settings.add(wall2);

			Wall wall3 = new Wall();// 40*10
			Position[] wallpos3 = new Position[4];
			wallpos3[0] = new Position(267, 262);
			wallpos3[1] = new Position(399, 262);
			wallpos3[2] = new Position(399, 345);
			wallpos3[3] = new Position(267, 345);
			wall3.setVertex(wallpos3);
			settings.add(wall3);

			Wall wall4 = new Wall();// 40*10
			Position[] wallpos4 = new Position[4];
			wallpos4[0] = new Position(400, 286);
			wallpos4[1] = new Position(460, 286);
			wallpos4[2] = new Position(460, 345);
			wallpos4[3] = new Position(400, 345);
			wall4.setVertex(wallpos4);
			settings.add(wall4);

			Wall wall5 = new Wall();// 40*10
			Position[] wallpos5 = new Position[4];
			wallpos5[0] = new Position(500, 465);
			wallpos5[1] = new Position(627, 465);
			wallpos5[2] = new Position(627, 479);
			wallpos5[3] = new Position(500, 479);
			wall5.setVertex(wallpos5);
			settings.add(wall5);

			RectangleWoodenItem rwi = new RectangleWoodenItem();// 20*20
			Position[] rwipos = new Position[4];// 200*40
			rwipos[0] = new Position(88, 147);
			rwipos[1] = new Position(288, 147);
			rwipos[2] = new Position(288, 187);
			rwipos[3] = new Position(88, 187);
			rwi.setVertex(rwipos);
			dynamicTools.add(rwi);

			RectangleWoodenItem rwi1 = new RectangleWoodenItem();// 20*20
			Position[] rwipos1 = new Position[4];// 80*80
			rwipos1[0] = new Position(80, 187);
			rwipos1[1] = new Position(160, 187);
			rwipos1[2] = new Position(160, 267);
			rwipos1[3] = new Position(80, 267);
			rwi1.setVertex(rwipos1);
			dynamicTools.add(rwi1);

			Flag flag = new Flag(new Position(559, 429), 36);// 40*10
			dynamicTools.add(flag);

			Ball ball = new Ball(new Position(100, 50), 30);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			this.id = 4;
			this.saveLevel("level4");

		}
		// kangcha5
		if (runid == 5) {
			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(334, 150);
			wallpos1[1] = new Position(640, 150);
			wallpos1[2] = new Position(640, 180);
			wallpos1[3] = new Position(334, 180);
			wall1.setVertex(wallpos1);
			settings.add(wall1);

			Wall wall2 = new Wall();// 40*10
			Position[] wallpos2 = new Position[4];
			wallpos2[0] = new Position(1, 270);
			wallpos2[1] = new Position(267, 270);
			wallpos2[2] = new Position(267, 300);
			wallpos2[3] = new Position(1, 300);
			wall2.setVertex(wallpos2);
			settings.add(wall2);

			Wall wall3 = new Wall();// 40*10
			Position[] wallpos3 = new Position[4];
			wallpos3[0] = new Position(401, 300);
			wallpos3[1] = new Position(534, 300);
			wallpos3[2] = new Position(534, 330);
			wallpos3[3] = new Position(401, 330);
			wall3.setVertex(wallpos3);
			settings.add(wall3);

			Wall wall4 = new Wall();// 40*10
			Position[] wallpos4 = new Position[4];
			wallpos4[0] = new Position(1, 345);
			wallpos4[1] = new Position(400, 345);
			wallpos4[2] = new Position(400, 375);
			wallpos4[3] = new Position(1, 375);
			wall4.setVertex(wallpos4);
			settings.add(wall4);

			Wall wall5 = new Wall();// 40*10
			Position[] wallpos5 = new Position[4];
			wallpos5[0] = new Position(1, 465);
			wallpos5[1] = new Position(650, 465);
			wallpos5[2] = new Position(650, 480);
			wallpos5[3] = new Position(1, 480);
			wall5.setVertex(wallpos5);
			settings.add(wall5);

			RectangleWoodenItem rwi = new RectangleWoodenItem();// 20*20
			Position[] rwipos = new Position[4];
			rwipos[0] = new Position(100, 213);
			rwipos[1] = new Position(300, 213);
			rwipos[2] = new Position(300, 250);
			rwipos[3] = new Position(100, 250);
			rwi.setVertex(rwipos);
			dynamicTools.add(rwi);

			TriangleWoodenItem twi = new TriangleWoodenItem();// 20*20
			Position[] twipos = new Position[3];
			twipos[0] = new Position(25, 10);
			twipos[1] = new Position(105, 90);
			twipos[2] = new Position(25, 90);
			twi.setVertex(twipos);
			dynamicTools.add(twi);
			this.itemRotate(new Position(24, 90), -Math.PI / 2);

			Flag flag = new Flag(new Position(59, 429), 36);// 40*10
			dynamicTools.add(flag);

			Ball ball = new Ball(new Position(280, 151), 30);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			this.id = 5;
			this.saveLevel("level5");

		}
		// kangcha6
		if (runid == 6) {
			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(200, 276);
			wallpos1[1] = new Position(333, 276);
			wallpos1[2] = new Position(333, 337);
			wallpos1[3] = new Position(200, 337);
			wall1.setVertex(wallpos1);
			settings.add(wall1);

			Wall wall2 = new Wall();// 40*10
			Position[] wallpos2 = new Position[4];
			wallpos2[0] = new Position(132, 337);
			wallpos2[1] = new Position(400, 337);
			wallpos2[2] = new Position(400, 397);
			wallpos2[3] = new Position(133, 397);
			wall2.setVertex(wallpos2);
			settings.add(wall2);

			Wall wall3 = new Wall();// 40*10
			Position[] wallpos3 = new Position[4];
			wallpos3[0] = new Position(66, 397);
			wallpos3[1] = new Position(467, 397);
			wallpos3[2] = new Position(467, 451);
			wallpos3[3] = new Position(66, 451);
			wall3.setVertex(wallpos3);
			settings.add(wall3);

			Wall wall4 = new Wall();// 40*10
			Position[] wallpos4 = new Position[4];
			wallpos4[0] = new Position(1, 449);
			wallpos4[1] = new Position(639, 449);
			wallpos4[2] = new Position(639, 478);
			wallpos4[3] = new Position(1, 478);
			wall4.setVertex(wallpos4);
			settings.add(wall4);

			RectangleWoodenItem rwi = new RectangleWoodenItem();// 20*20
			Position[] rwipos = new Position[4];
			rwipos[0] = new Position(55, 26);
			rwipos[1] = new Position(255, 26);
			rwipos[2] = new Position(255, 66);
			rwipos[3] = new Position(55, 66);
			rwi.setVertex(rwipos);
			dynamicTools.add(rwi);

			RectangleWoodenItem rwi2 = new RectangleWoodenItem();// 20*20
			Position[] rwipos2 = new Position[4];
			rwipos2[0] = new Position(55, 80);
			rwipos2[1] = new Position(255, 80);
			rwipos2[2] = new Position(255, 120);
			rwipos2[3] = new Position(55, 120);
			rwi2.setVertex(rwipos2);
			dynamicTools.add(rwi2);

			Flag flag = new Flag(new Position(559, 414), 36);// 40*10
			dynamicTools.add(flag);

			Ball ball = new Ball(new Position(35, 148), 30);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			this.id = 6;
			this.saveLevel("level6");
		}
		// kangcha7
		if (runid == 7) {
			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(1, 225);
			wallpos1[1] = new Position(133, 225);
			wallpos1[2] = new Position(133, 255);
			wallpos1[3] = new Position(1, 255);
			wall1.setVertex(wallpos1);
			settings.add(wall1);

			Wall wall2 = new Wall();// 40*10
			Position[] wallpos2 = new Position[4];
			wallpos2[0] = new Position(266, 225);
			wallpos2[1] = new Position(393, 225);
			wallpos2[2] = new Position(393, 255);
			wallpos2[3] = new Position(266, 255);
			wall2.setVertex(wallpos2);
			settings.add(wall2);

			Wall wall3 = new Wall();// 40*10
			Position[] wallpos3 = new Position[4];
			wallpos3[0] = new Position(467, 225);
			wallpos3[1] = new Position(567, 225);
			wallpos3[2] = new Position(567, 255);
			wallpos3[3] = new Position(467, 255);
			wall3.setVertex(wallpos3);
			settings.add(wall3);

			RectangleWoodenItem rwi = new RectangleWoodenItem();// 20*20
			Position[] rwipos = new Position[4];
			rwipos[0] = new Position(5, 25);
			rwipos[1] = new Position(205, 25);
			rwipos[2] = new Position(205, 65);
			rwipos[3] = new Position(5, 65);
			rwi.setVertex(rwipos);
			dynamicTools.add(rwi);

			RectangleWoodenItem rwi2 = new RectangleWoodenItem();// 20*20
			Position[] rwipos2 = new Position[4];
			rwipos2[0] = new Position(210, 25);
			rwipos2[1] = new Position(410, 25);
			rwipos2[2] = new Position(410, 65);
			rwipos2[3] = new Position(210, 65);
			rwi2.setVertex(rwipos2);
			dynamicTools.add(rwi2);

			/*
			 * RectangleWoodenItem rwi3=new RectangleWoodenItem();//20*20
			 * Position[] rwipos3=new Position[4]; rwipos3[0]=new
			 * Position(5,80); rwipos3[1]=new Position(205,80); rwipos3[2]=new
			 * Position(205,120); rwipos3[3]=new Position(5,120);
			 * rwi3.setVertex(rwipos3); dynamicTools.add(rwi3);
			 * 
			 * RectangleWoodenItem rwi4=new RectangleWoodenItem();//20*20
			 * Position[] rwipos4=new Position[4]; rwipos4[0]=new
			 * Position(210,80); rwipos4[1]=new Position(410,80); rwipos4[2]=new
			 * Position(410,120); rwipos4[3]=new Position(210,120);
			 * rwi4.setVertex(rwipos4); dynamicTools.add(rwi4);
			 */

			Ball ball = new Ball(new Position(602, 150), 30);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			AccelerationMark am = new AccelerationMark(new Position(440, 51),
					AccelerationMark.LEFT_DIRECTION, 15);// 40*10
			dynamicTools.add(am);

			Flag flag = new Flag(new Position(39, 189), 36);// 40*10
			dynamicTools.add(flag);

			this.id = 7;
			this.saveLevel("level7");
		}

		// kangcha8
		if (runid == 8) {
			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(1, 176);
			wallpos1[1] = new Position(356, 285);
			wallpos1[2] = new Position(349, 312);
			wallpos1[3] = new Position(1, 207);
			wall1.setVertex(wallpos1);
			settings.add(wall1);

			RectangleMetalItem rwi1 = new RectangleMetalItem();// 20*20
			Position[] rwipos1 = new Position[4];
			rwipos1[0] = new Position(125, 70);
			rwipos1[1] = new Position(325, 70);
			rwipos1[2] = new Position(325, 110);
			rwipos1[3] = new Position(125, 110);
			rwi1.setVertex(rwipos1);
			settings.add(rwi1);

			RectangleMetalItem rwi2 = new RectangleMetalItem();// 20*20
			Position[] rwipos2 = new Position[4];
			rwipos2[0] = new Position(335, 70);
			rwipos2[1] = new Position(535, 70);
			rwipos2[2] = new Position(535, 110);
			rwipos2[3] = new Position(335, 110);
			rwi2.setVertex(rwipos2);
			settings.add(rwi2);

	/*		TriangleMetalItem tmi = new TriangleMetalItem();// 20*20
			Position[] tmipos = new Position[3];
			tmipos[0] = new Position(30, 15);
			tmipos[1] = new Position(110, 95);
			tmipos[2] = new Position(30, 95);
			tmi.setVertex(tmipos);
			settings.add(tmi);*/

			Ball ball = new Ball(new Position(134, 149), 30);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

	/*		AccelerationMark am = new AccelerationMark(new Position(166, 36),
					AccelerationMark.LEFT_DIRECTION, 15);// 40*10
			dynamicTools.add(am);*/

			Flag flag = new Flag(new Position(38, 338), 36);// 40*10
			dynamicTools.add(flag);

			Cushion cusion = new Cushion();// 20*20
			Position[] cupos = new Position[4];
			cupos[0] = new Position(205, 34);
			cupos[1] = new Position(278, 34);
			cupos[2] = new Position(278, 51);
			cupos[3] = new Position(205, 51);
			cusion.setVertex(cupos);
			dynamicTools.add(cusion);

			this.id = 8;
			this.saveLevel("level8");
		}

		// kangcha9
		if (runid == 9) {
			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(1, 120);
			wallpos1[1] = new Position(6, 120);
			wallpos1[2] = new Position(6, 480);
			wallpos1[3] = new Position(1, 480);
			wall1.setVertex(wallpos1);
			settings.add(wall1);

			Wall wall2 = new Wall();// 40*10
			Position[] wallpos2 = new Position[4];
			wallpos2[0] = new Position(5, 120);
			wallpos2[1] = new Position(640, 120);
			wallpos2[2] = new Position(640, 125);
			wallpos2[3] = new Position(5, 125);
			wall2.setVertex(wallpos2);
			settings.add(wall2);

			Wall wall3 = new Wall();// 40*10
			Position[] wallpos3 = new Position[4];
			wallpos3[0] = new Position(75, 195);
			wallpos3[1] = new Position(305, 195);
			wallpos3[2] = new Position(305, 200);
			wallpos3[3] = new Position(75, 200);
			wall3.setVertex(wallpos3);
			settings.add(wall3);

			Wall wall4 = new Wall();// 40*10
			Position[] wallpos4 = new Position[4];
			wallpos4[0] = new Position(311, 420);
			wallpos4[1] = new Position(546, 420);
			wallpos4[2] = new Position(546, 425);
			wallpos4[3] = new Position(311, 425);
			wall4.setVertex(wallpos4);
			settings.add(wall4);

			Wall wall5 = new Wall();// 40*10
			Position[] wallpos5 = new Position[4];
			wallpos5[0] = new Position(75, 195);
			wallpos5[1] = new Position(80, 195);
			wallpos5[2] = new Position(80, 420);
			wallpos5[3] = new Position(75, 420);
			wall5.setVertex(wallpos5);
			settings.add(wall5);

			Wall wall6 = new Wall();// 40*10
			Position[] wallpos6 = new Position[4];
			wallpos6[0] = new Position(147, 200);
			wallpos6[1] = new Position(151, 200);
			wallpos6[2] = new Position(151, 420);
			wallpos6[3] = new Position(147, 420);
			wall6.setVertex(wallpos6);
			settings.add(wall6);

			Wall wall7 = new Wall();// 40*10
			Position[] wallpos7 = new Position[4];
			wallpos7[0] = new Position(227, 200);
			wallpos7[1] = new Position(232, 200);
			wallpos7[2] = new Position(232, 420);
			wallpos7[3] = new Position(227, 420);
			wall7.setVertex(wallpos7);
			settings.add(wall7);

			Wall wall8 = new Wall();// 40*10
			Position[] wallpos8 = new Position[4];
			wallpos8[0] = new Position(306, 200);
			wallpos8[1] = new Position(312, 200);
			wallpos8[2] = new Position(312, 420);
			wallpos8[3] = new Position(306, 420);
			wall8.setVertex(wallpos8);
			settings.add(wall8);

			Wall wall9 = new Wall();// 40*10
			Position[] wallpos9 = new Position[4];
			wallpos9[0] = new Position(385, 196);
			wallpos9[1] = new Position(392, 196);
			wallpos9[2] = new Position(392, 420);
			wallpos9[3] = new Position(385, 420);
			wall9.setVertex(wallpos9);
			settings.add(wall9);

			Wall wall10 = new Wall();// 40*10
			Position[] wallpos10 = new Position[4];
			wallpos10[0] = new Position(466, 196);
			wallpos10[1] = new Position(473, 196);
			wallpos10[2] = new Position(473, 420);
			wallpos10[3] = new Position(466, 420);
			wall10.setVertex(wallpos10);
			settings.add(wall10);

			Wall wall11 = new Wall();// 40*10
			Position[] wallpos11 = new Position[4];
			wallpos11[0] = new Position(546, 196);
			wallpos11[1] = new Position(553, 196);
			wallpos11[2] = new Position(553, 420);
			wallpos11[3] = new Position(546, 420);
			wall11.setVertex(wallpos11);
			settings.add(wall11);

			BallFactory bf = new BallFactory(new Position(30, 380),
					Ball.BALLOON, 15);// 20*20

			settings.add(bf);

			Ball ball = new Ball(new Position(40, 300), 25);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			AccelerationMark am = new AccelerationMark(new Position(30, 150),
					AccelerationMark.RIGHT_DIRECTION, 15);// 40*10
			dynamicTools.add(am);

			Flag flag = new Flag(new Position(585, 173), 36);// 40*10
			dynamicTools.add(flag);

			Wall wall12 = new Wall();// 40*10
			Position[] wallpos12 = new Position[4];
			wallpos12[0] = new Position(549, 209);
			wallpos12[1] = new Position(621, 209);
			wallpos12[2] = new Position(621, 211);
			wallpos12[3] = new Position(549, 211);
			wall12.setVertex(wallpos12);
			settings.add(wall12);

			this.id = 9;
			this.saveLevel("level9");
		}

		/*
		 * if (runid==10){
		 * 
		 * Wall wall1=new Wall();//40*10 Position[] wallpos1=new Position[4];
		 * wallpos1[0]=new Position(553,360); wallpos1[1]=new Position(639,360);
		 * wallpos1[2]=new Position(639,367); wallpos1[3]=new Position(553,367);
		 * wall1.setVertex(wallpos1); settings.add(wall1);
		 * 
		 * TriangleMetalItem tmi=new TriangleMetalItem();//20*20 Position[]
		 * tmipos=new Position[3]; tmipos[0]=new Position(5,15); tmipos[1]=new
		 * Position(85,100); tmipos[2]=new Position(5,100);
		 * tmi.setVertex(tmipos); settings.add(tmi);
		 * 
		 * TriangleMetalItem tmi1=new TriangleMetalItem();//20*20 Position[]
		 * tmipos1=new Position[3]; tmipos1[0]=new Position(95,15);
		 * tmipos1[1]=new Position(175,100); tmipos1[2]=new Position(95,100);
		 * tmi1.setVertex(tmipos1); settings.add(tmi1);
		 * 
		 * TriangleMetalItem tmi2=new TriangleMetalItem();//20*20 Position[]
		 * tmipos2=new Position[3]; tmipos2[0]=new Position(185,15);
		 * tmipos2[1]=new Position(265,100); tmipos2[2]=new Position(185,100);
		 * tmi2.setVertex(tmipos2); settings.add(tmi2);
		 * 
		 * TriangleMetalItem tmi3=new TriangleMetalItem();//20*20 Position[]
		 * tmipos3=new Position[3]; tmipos3[0]=new Position(275,15);
		 * tmipos3[1]=new Position(355,100); tmipos3[2]=new Position(275,100);
		 * tmi3.setVertex(tmipos3); settings.add(tmi3);
		 * 
		 * Ball ball=new Ball(new Position(67,133),30);//40*10
		 * ball.setState(Ball.NORMAL_BALL); dynamicTools.add(ball);
		 * 
		 * Flag flag=new Flag(new Position(599,323),36);//40*10
		 * dynamicTools.add(flag);
		 * 
		 * 
		 * this.id=10; this.saveLevel("level10"); }
		 */
		// kangcha10
		if (runid == 10) {
			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(200, 180);
			wallpos1[1] = new Position(240, 180);
			wallpos1[2] = new Position(240, 480);
			wallpos1[3] = new Position(200, 480);
			wall1.setVertex(wallpos1);
			settings.add(wall1);
			Wall wall2 = new Wall();// 40*10
			Position[] wallpos2 = new Position[4];
			wallpos2[0] = new Position(240, 460);
			wallpos2[1] = new Position(640, 460);
			wallpos2[2] = new Position(640, 480);
			wallpos2[3] = new Position(240, 480);
			wall2.setVertex(wallpos2);
			settings.add(wall2);

			RectangleWoodenItem rwi1 = new RectangleWoodenItem();// 20*20
			Position[] rwipos1 = new Position[4];
			rwipos1[0] = new Position(300, 50);
			rwipos1[1] = new Position(380, 50);
			rwipos1[2] = new Position(380, 130);
			rwipos1[3] = new Position(300, 130);
			rwi1.setVertex(rwipos1);
			dynamicTools.add(rwi1);

			WoodenSeesaw wss = new WoodenSeesaw();
			Position[] wsspos = new Position[7];
			wsspos[0] = new Position(440, 440);
			wsspos[1] = new Position(420, 410);
			wsspos[2] = new Position(320, 410);
			wsspos[3] = new Position(320, 380);
			wsspos[4] = new Position(560, 380);
			wsspos[5] = new Position(560, 410);
			wsspos[6] = new Position(460, 410);
			wss.setVertex(wsspos);
			dynamicTools.add(wss);

			Ball ball = new Ball(new Position(540, 90), 25);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			Flag flag = new Flag(new Position(40, 440), 36);// 40*10
			dynamicTools.add(flag);

			this.id = 10;
			this.saveLevel("level10");

		}
		// kangcha11
		if (runid == 11) {
			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(327, 188);
			wallpos1[1] = new Position(440, 188);
			wallpos1[2] = new Position(440, 225);
			wallpos1[3] = new Position(327, 225);
			wall1.setVertex(wallpos1);
			settings.add(wall1);

			Wall wall2 = new Wall();// 40*10
			Position[] wallpos2 = new Position[4];
			wallpos2[0] = new Position(266, 225);
			wallpos2[1] = new Position(500, 225);
			wallpos2[2] = new Position(500, 262);
			wallpos2[3] = new Position(266, 262);
			wall2.setVertex(wallpos2);
			settings.add(wall2);

			Wall wall3 = new Wall();// 40*10
			Position[] wallpos3 = new Position[4];
			wallpos3[0] = new Position(180, 299);
			wallpos3[1] = new Position(225, 264);
			wallpos3[2] = new Position(229, 270);
			wallpos3[3] = new Position(192, 300);
			wall3.setVertex(wallpos3);
			settings.add(wall3);

			Wall wall4 = new Wall();// 40*10
			Position[] wallpos4 = new Position[4];
			wallpos4[0] = new Position(1, 299);
			wallpos4[1] = new Position(639, 299);
			wallpos4[2] = new Position(649, 329);
			wallpos4[3] = new Position(1, 329);
			wall4.setVertex(wallpos4);
			settings.add(wall4);

			BallFactory bf = new BallFactory(new Position(165, 55),
					Ball.BALLOON, 15);// 20*20
			settings.add(bf);

			BallFactory bf1 = new BallFactory(new Position(280, 55),
					Ball.NORMAL_BALL, 15);// 20*20
			settings.add(bf1);

			AccelerationMark am = new AccelerationMark(new Position(320, 55),
					AccelerationMark.RIGHT_DIRECTION, 15);// 40*10
			dynamicTools.add(am);

			Ball ball = new Ball(new Position(33, 150), 30);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			Flag flag = new Flag(new Position(578, 264), 36);// 40*10
			dynamicTools.add(flag);

			TriangleWoodenItem tmi1 = new TriangleWoodenItem();// 20*20
			Position[] tmipos1 = new Position[3];
			tmipos1[0] = new Position(30, 35);
			tmipos1[1] = new Position(110, 115);
			tmipos1[2] = new Position(30, 115);
			tmi1.setVertex(tmipos1);
			dynamicTools.add(tmi1);

			this.id = 11;
			this.saveLevel("level11");
		}
		// kangcha12
		if (runid == 12) {

			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(1, 262);
			wallpos1[1] = new Position(266, 262);
			wallpos1[2] = new Position(266, 428);
			wallpos1[3] = new Position(1, 428);
			wall1.setVertex(wallpos1);
			settings.add(wall1);

			Wall wall2 = new Wall();// 40*10
			Position[] wallpos2 = new Position[4];
			wallpos2[0] = new Position(265, 262);
			wallpos2[1] = new Position(353, 262);
			wallpos2[2] = new Position(353, 265);
			wallpos2[3] = new Position(265, 265);
			wall2.setVertex(wallpos2);
			settings.add(wall2);

			Wall wall3 = new Wall();// 40*10
			Position[] wallpos3 = new Position[4];
			wallpos3[0] = new Position(353, 262);
			wallpos3[1] = new Position(453, 262);
			wallpos3[2] = new Position(453, 389);
			wallpos3[3] = new Position(353, 389);
			wall3.setVertex(wallpos3);
			settings.add(wall3);

			Wall wall4 = new Wall();// 40*10
			Position[] wallpos4 = new Position[4];
			wallpos4[0] = new Position(266, 473);
			wallpos4[1] = new Position(640, 473);
			wallpos4[2] = new Position(640, 479);
			wallpos4[3] = new Position(266, 479);
			wall4.setVertex(wallpos4);
			settings.add(wall4);

			BallFactory bf = new BallFactory(new Position(243, 60),
					Ball.BALLOON, 15);// 20*20
			settings.add(bf);

			Ball ball = new Ball(new Position(26, 149), 30);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			Flag flag = new Flag(new Position(298, 301), 36);// 40*10
			dynamicTools.add(flag);

			TriangleWoodenItem tmi1 = new TriangleWoodenItem();// 20*20
			Position[] tmipos1 = new Position[3];
			tmipos1[0] = new Position(25, 10);
			tmipos1[1] = new Position(105, 90);
			tmipos1[2] = new Position(25, 90);
			tmi1.setVertex(tmipos1);
			dynamicTools.add(tmi1);

			TriangleWoodenItem tmi2 = new TriangleWoodenItem();// 20*20
			Position[] tmipos2 = new Position[3];
			tmipos2[0] = new Position(115, 10);
			tmipos2[1] = new Position(195, 90);
			tmipos2[2] = new Position(115, 90);
			tmi2.setVertex(tmipos2);
			dynamicTools.add(tmi2);

			this.id = 12;
			this.saveLevel("level12");
		}
		// kangcha13
		if (runid == 13) {
			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(120, 172);
			wallpos1[1] = new Position(531, 172);
			wallpos1[2] = new Position(531, 201);
			wallpos1[3] = new Position(120, 201);
			wall1.setVertex(wallpos1);
			settings.add(wall1);

			Wall wall2 = new Wall();// 40*10
			Position[] wallpos2 = new Position[4];
			wallpos2[0] = new Position(120, 200);
			wallpos2[1] = new Position(160, 200);
			wallpos2[2] = new Position(326, 340);
			wallpos2[3] = new Position(326, 380);
			wall2.setVertex(wallpos2);
			settings.add(wall2);

			Wall wall3 = new Wall();// 40*10
			Position[] wallpos3 = new Position[4];
			wallpos3[0] = new Position(490, 200);
			wallpos3[1] = new Position(530, 200);
			wallpos3[2] = new Position(324, 380);
			wallpos3[3] = new Position(324, 340);
			wall3.setVertex(wallpos3);
			settings.add(wall3);

			Wall wall4 = new Wall();// 40*10
			Position[] wallpos4 = new Position[4];
			wallpos4[0] = new Position(529, 122);
			wallpos4[1] = new Position(533, 122);
			wallpos4[2] = new Position(533, 200);
			wallpos4[3] = new Position(529, 200);
			wall4.setVertex(wallpos4);
			settings.add(wall4);

			BallFactory bf = new BallFactory(new Position(105, 55),
					Ball.BALLOON, 15);// 20*20
			settings.add(bf);
			BallFactory bf1 = new BallFactory(new Position(205, 55),
					Ball.NORMAL_BALL, 15);// 20*20
			settings.add(bf1);

			AccelerationMark am = new AccelerationMark(new Position(199, 25),
					AccelerationMark.LEFT_DIRECTION, 15);// 40*10
			dynamicTools.add(am);

			AccelerationMark am1 = new AccelerationMark(new Position(269, 25),
					AccelerationMark.RIGHT_DIRECTION, 15);// 40*10
			dynamicTools.add(am1);

			Ball ball = new Ball(new Position(615, 149), 30);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			Flag flag = new Flag(new Position(492, 144), 36);// 40*10
			dynamicTools.add(flag);

			this.id = 13;
			this.saveLevel("level13");
		}
		// kangcha14
		if (runid == 14) {

			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(0, 187);
			wallpos1[1] = new Position(1, 187);
			wallpos1[2] = new Position(1, 478);
			wallpos1[3] = new Position(0, 478);
			wall1.setVertex(wallpos1);
			settings.add(wall1);

			Wall wall2 = new Wall();// 40*10
			Position[] wallpos2 = new Position[4];
			wallpos2[0] = new Position(1, 478);
			wallpos2[1] = new Position(638, 478);
			wallpos2[2] = new Position(638, 479);
			wallpos2[3] = new Position(1, 479);
			wall2.setVertex(wallpos2);
			settings.add(wall2);

			Wall wall3 = new Wall();// 40*10
			Position[] wallpos3 = new Position[4];
			wallpos3[0] = new Position(638, 187);
			wallpos3[1] = new Position(640, 187);
			wallpos3[2] = new Position(640, 479);
			wallpos3[3] = new Position(638, 479);
			wall3.setVertex(wallpos3);
			settings.add(wall3);

			Wall wall4 = new Wall();// 40*10
			Position[] wallpos4 = new Position[4];
			wallpos4[0] = new Position(1, 187);
			wallpos4[1] = new Position(299, 187);
			wallpos4[2] = new Position(299, 189);
			wallpos4[3] = new Position(1, 189);
			wall4.setVertex(wallpos4);
			settings.add(wall4);

			Wall wall5 = new Wall();// 40*10
			Position[] wallpos5 = new Position[4];
			wallpos5[0] = new Position(1, 262);
			wallpos5[1] = new Position(199, 262);
			wallpos5[2] = new Position(199, 264);
			wallpos5[3] = new Position(1, 264);
			wall5.setVertex(wallpos5);
			settings.add(wall5);

			Wall wall6 = new Wall();// 40*10
			Position[] wallpos6 = new Position[4];
			wallpos6[0] = new Position(48, 337);
			wallpos6[1] = new Position(299, 337);
			wallpos6[2] = new Position(299, 339);
			wallpos6[3] = new Position(48, 339);
			wall6.setVertex(wallpos6);
			settings.add(wall6);

			Wall wall7 = new Wall();// 40*10
			Position[] wallpos7 = new Position[4];
			wallpos7[0] = new Position(46, 337);
			wallpos7[1] = new Position(48, 337);
			wallpos7[2] = new Position(48, 414);
			wallpos7[3] = new Position(46, 414);
			wall7.setVertex(wallpos7);
			settings.add(wall7);

			Wall wall8 = new Wall();// 40*10
			Position[] wallpos8 = new Position[4];
			wallpos8[0] = new Position(48, 412);
			wallpos8[1] = new Position(500, 412);
			wallpos8[2] = new Position(500, 414);
			wallpos8[3] = new Position(48, 414);
			wall8.setVertex(wallpos8);
			settings.add(wall8);

			Wall wall9 = new Wall();// 40*10
			Position[] wallpos9 = new Position[4];
			wallpos9[0] = new Position(299, 187);
			wallpos9[1] = new Position(301, 187);
			wallpos9[2] = new Position(301, 337);
			wallpos9[3] = new Position(299, 337);
			wall9.setVertex(wallpos9);
			settings.add(wall9);

			Wall wall10 = new Wall();// 40*10
			Position[] wallpos10 = new Position[4];
			wallpos10[0] = new Position(373, 187);
			wallpos10[1] = new Position(438, 187);
			wallpos10[2] = new Position(438, 412);
			wallpos10[3] = new Position(373, 412);
			wall10.setVertex(wallpos10);
			settings.add(wall10);

			Wall wall11 = new Wall();// 40*10
			Position[] wallpos11 = new Position[4];
			wallpos11[0] = new Position(519, 188);
			wallpos11[1] = new Position(557, 188);
			wallpos11[2] = new Position(557, 412);
			wallpos11[3] = new Position(519, 412);
			wall11.setVertex(wallpos11);
			settings.add(wall11);

			BallFactory bf = new BallFactory(new Position(365, 65),
					Ball.BALLOON, 15);// 20*20
			settings.add(bf);

			RectangleWoodenItem rwi1 = new RectangleWoodenItem();// 20*20
			Position[] rwipos1 = new Position[4];
			rwipos1[0] = new Position(15, 50);
			rwipos1[1] = new Position(75, 50);
			rwipos1[2] = new Position(75, 110);
			rwipos1[3] = new Position(15, 110);
			rwi1.setVertex(rwipos1);
			dynamicTools.add(rwi1);

			RectangleWoodenItem rwi2 = new RectangleWoodenItem();// 20*20
			Position[] rwipos2 = new Position[4];
			rwipos2[0] = new Position(115, 50);
			rwipos2[1] = new Position(175, 50);
			rwipos2[2] = new Position(175, 110);
			rwipos2[3] = new Position(115, 110);
			rwi2.setVertex(rwipos2);
			dynamicTools.add(rwi2);

			RectangleWoodenItem rwi3 = new RectangleWoodenItem();// 20*20
			Position[] rwipos3 = new Position[4];
			rwipos3[0] = new Position(205, 50);
			rwipos3[1] = new Position(265, 50);
			rwipos3[2] = new Position(265, 110);
			rwipos3[3] = new Position(205, 110);
			rwi3.setVertex(rwipos3);
			dynamicTools.add(rwi3);

			RectangleWoodenItem rwi4 = new RectangleWoodenItem();// 20*20
			Position[] rwipos4 = new Position[4];
			rwipos4[0] = new Position(295, 50);
			rwipos4[1] = new Position(355, 50);
			rwipos4[2] = new Position(355, 110);
			rwipos4[3] = new Position(295, 110);
			rwi4.setVertex(rwipos4);
			dynamicTools.add(rwi4);

			/*
			 * RectangleWoodenItem rwi5=new RectangleWoodenItem();//20*20
			 * Position[] rwipos5=new Position[4]; rwipos5[0]=new
			 * Position(385,50); rwipos5[1]=new Position(445,50); rwipos5[2]=new
			 * Position(445,110); rwipos5[3]=new Position(385,110);
			 * rwi5.setVertex(rwipos5); dynamicTools.add(rwi5);
			 * 
			 * RectangleWoodenItem rwi6=new RectangleWoodenItem();//20*20
			 * Position[] rwipos6=new Position[4]; rwipos6[0]=new
			 * Position(475,50); rwipos6[1]=new Position(535,50); rwipos6[2]=new
			 * Position(535,110); rwipos6[3]=new Position(475,110);
			 * rwi6.setVertex(rwipos6); dynamicTools.add(rwi6);
			 */

			AccelerationMark am = new AccelerationMark(new Position(199, 22),
					AccelerationMark.LEFT_DIRECTION, 15);// 40*10
			dynamicTools.add(am);

			AccelerationMark am1 = new AccelerationMark(new Position(234, 22),
					AccelerationMark.RIGHT_DIRECTION, 15);// 40*10
			dynamicTools.add(am1);

			AccelerationMark am2 = new AccelerationMark(new Position(268, 22),
					AccelerationMark.RIGHT_DIRECTION, 15);// 40*10
			dynamicTools.add(am2);

			Ball ball = new Ball(new Position(22, 130), 25);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			Flag flag = new Flag(new Position(245, 223), 36);// 40*10
			dynamicTools.add(flag);

			this.id = 14;
			this.rate = 9999.0;
			this.saveLevel("level14");
		}
		// kangcha15
		if (runid == 15) {

			Wall wall1 = new Wall();// 40*10
			Position[] wallpos1 = new Position[4];
			wallpos1[0] = new Position(635, 200);
			wallpos1[1] = new Position(640, 200);
			wallpos1[2] = new Position(640, 480);
			wallpos1[3] = new Position(635, 480);
			wall1.setVertex(wallpos1);
			settings.add(wall1);

			BallFactory bf = new BallFactory(new Position(50, 200),
					Ball.METAL_BALL, 15);// 20*20
			settings.add(bf);

			Ball ball = new Ball(new Position(50, 100), 25);// 40*10
			ball.setState(Ball.NORMAL_BALL);
			dynamicTools.add(ball);

			Magnet ma = new Magnet(new Position(150, 150), 120);// 40*10
			ma.setFunctioning(false);
			dynamicTools.add(ma);

			Bat bat = new Bat();// 40*10
			Position[] batPos = new Position[4];
			batPos[0] = new Position(100, 410);
			batPos[1] = new Position(240, 410);
			batPos[2] = new Position(240, 430);
			batPos[3] = new Position(100, 430);
			bat.setVertex(batPos);
			bat.setFunctioning(false);
			dynamicTools.add(bat);

			Glass gl = new Glass();
			Position[] glasspos = new Position[4];
			glasspos[0] = new Position(500, 310);
			glasspos[1] = new Position(635, 310);
			glasspos[2] = new Position(635, 350);
			glasspos[3] = new Position(500, 350);
			gl.setVertex(glasspos);
			gl.setEnergyThreshold(7);
			dynamicTools.add(gl);

			Glass gl1 = new Glass();
			Position[] glasspos1 = new Position[4];
			glasspos1[0] = new Position(480, 360);
			glasspos1[1] = new Position(520, 360);
			glasspos1[2] = new Position(520, 480);
			glasspos1[3] = new Position(480, 480);
			gl1.setVertex(glasspos1);
			gl1.setEnergyThreshold(7);
			dynamicTools.add(gl1);

			Flag flag = new Flag(new Position(600, 440), 36);// 40*10
			dynamicTools.add(flag);
			this.id = 15;
			this.saveLevel("level14");
		}
	}

	public static void main(String[] args) throws Exception {
		Level lv = new Level();
		lv.makeLevel();

	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public Double getRate() {
		return rate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setThresholdLine(int thresholdLine) {
		this.thresholdLine = thresholdLine;
	}

	public Integer getThresholdLine() {
		return thresholdLine;
	}

	public void setSettings(ArrayList<Item> settings) {
		this.settings = settings;
	}

	public ArrayList<Item> getSettings() {
		return settings;
	}

	public void setTools(ArrayList<Item> tools) {
		this.tools = tools;
	}

	public ArrayList<Item> getTools() {
		return tools;
	}

	public void setDynamicTools(ArrayList<Item> dynamicTools) {
		this.dynamicTools = dynamicTools;
	}

	public ArrayList<Item> getDynamicTools() {
		return dynamicTools;
	}

}
