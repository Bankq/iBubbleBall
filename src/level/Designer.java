package level;

import item.AccelerationMark;
import item.Ball;
import item.BallFactory;
import item.Bat;
import item.Cushion;
import item.Glass;
import item.Item;
import item.Magnet;
import item.RectangleArea;
import item.RectangleMetalItem;
import item.RectangleWoodenItem;
import item.TriangleArea;
import item.TriangleMetalItem;
import item.TriangleWoodenItem;
import item.Wall;
import item.WoodenSeesaw;
import logic.Position;

public class Designer {
	public final static int ITEM_WOODEN_RECTANGLE = 1;
	public final static int ITEM_WOODEN_TRIANGLE = 2;
	public final static int ITEM_METAL_RECTANGLE = 3;
	public final static int ITEM_METAL_TRIANGLE = 4;
	public final static int ITEM_CUSHION = 5;
	public final static int ITEM_SEESAW = 6;
	public final static int ITEM_WALL = 7;
	public final static int ITEM_GLASS = 8;
	public final static int ITEM_MAGNET = 9;
	public final static int ITEM_BAT = 10;
	public final static int ITEM_FACTORY_BALLOON = 11;
	public final static int ITEM_FACTORY_METAL = 12;
	public final static int ITEM_FACTORY_NORMAL = 13;
	public final static int ITEM_ACCELERATION_MARK_LEFT = 14;
	public final static int ITEM_ACCELERATION_MARK_RIGHT = 15;
	
	private Item createdItem = null;
	private Position createdItemPosition;
	private Level level;
	private String levelName;
	
	public Designer(Level level, String name) {
		this.level = level;
		this.level.setEditmode(true);
		//this.level.loadLevel(name);
		this.level.makeNewLevel(name);
		this.levelName = name;
	}
	
	public String getLevelName() {
		return levelName;
	}
	
	public void destroyCreatedItem() {
		createdItem = null;
	}
	
	public boolean firstPosition(Position pos) {
		if (createdItem instanceof RectangleArea || createdItem instanceof TriangleArea || createdItem instanceof WoodenSeesaw) {
			createdItemPosition = pos;
			return false;
		} else {
			createdItem.setPosition(pos);
			level.editorPlaceItem(createdItem);
			createdItem = null;
			return true;
		}
	}
	
	public boolean hasCreatedItem() {
		return (createdItem != null);
	}
	
	public void secondPosition(Position pos) {
		if (createdItem == null) return;
		if (createdItem instanceof RectangleArea) {
			Position[] vertex = new Position[4];
			vertex[0] = createdItemPosition;
			vertex[2] = pos;
			vertex[1] = new Position(pos.getX(),createdItemPosition.getY());
			vertex[3] = new Position(createdItemPosition.getX(), pos.getY());
			((RectangleArea)createdItem).setVertex(vertex);
		} else if (createdItem instanceof TriangleArea) {
			Position[] vertex = new Position[3];
			vertex[0] = createdItemPosition;
			vertex[1] = pos;
			vertex[2] = new Position(createdItemPosition.getX(), pos.getY());
			((TriangleArea)createdItem).setVertex(vertex);
		} else if (createdItem instanceof WoodenSeesaw) {
			Position[] vertex = new Position[7];
			vertex[3] = createdItemPosition;
			vertex[0] = new Position((createdItemPosition.getX()+pos.getX())/2, pos.getY());
			vertex[1] = new Position(createdItemPosition.getX()/3+vertex[0].getX()*2/3,(createdItemPosition.getY()+pos.getY())/2);
			vertex[2] = new Position(createdItemPosition.getX(), vertex[1].getY());
			vertex[4] = new Position(pos.getX(), createdItemPosition.getY());
			vertex[5] = new Position(pos.getX(), vertex[1].getY());
			vertex[6] = new Position(vertex[0].getX()*2/3+pos.getX()/3,vertex[1].getY());
			((WoodenSeesaw)createdItem).setVertex(vertex);
		}
		level.editorPlaceItem(createdItem);
		createdItem = null;
	}
	
	public void createItem(int itemNo) {
		switch (itemNo) {
		case ITEM_WOODEN_RECTANGLE:
			createdItem = new RectangleWoodenItem();
			break;
		case ITEM_WOODEN_TRIANGLE:
			createdItem = new TriangleWoodenItem();
			break;
		case ITEM_METAL_RECTANGLE:
			createdItem = new RectangleMetalItem();
			break;
		case ITEM_METAL_TRIANGLE:
			createdItem = new TriangleMetalItem();
			break;
		case ITEM_CUSHION:
			createdItem = new Cushion();
			break;
		case ITEM_SEESAW:
			createdItem = new WoodenSeesaw();
			break;
		case ITEM_WALL:
			createdItem = new Wall();
			break;
		case ITEM_GLASS:
			createdItem = new Glass();
			((Glass)createdItem).setEnergyThreshold(7);
			break;
		case ITEM_MAGNET:
			createdItem = new Magnet();
			((Magnet)createdItem).setRadius(120);
			break;
		case ITEM_BAT:
			createdItem = new Bat();
			break;
		case ITEM_FACTORY_BALLOON:
			createdItem = new BallFactory(Ball.BALLOON);
			((BallFactory)createdItem).setRadius(15);
			break;
		case ITEM_FACTORY_METAL:
			createdItem = new BallFactory(Ball.METAL_BALL);
			((BallFactory)createdItem).setRadius(15);
			break;
		case ITEM_FACTORY_NORMAL:
			createdItem = new BallFactory(Ball.NORMAL_BALL);
			((BallFactory)createdItem).setRadius(15);
			break;
		case ITEM_ACCELERATION_MARK_LEFT:
			createdItem = new AccelerationMark(AccelerationMark.LEFT_DIRECTION);
			((AccelerationMark)createdItem).setRadius(15);
			break;
		case ITEM_ACCELERATION_MARK_RIGHT:
			createdItem = new AccelerationMark(AccelerationMark.RIGHT_DIRECTION);
			((AccelerationMark)createdItem).setRadius(15);
			break;
		default:
		}
	}
}
