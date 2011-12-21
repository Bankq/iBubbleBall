package logic;

import java.io.Serializable;
import java.util.ArrayList;

import item.*;
import level.*;
import org.jbox2d.collision.AABB;  
import org.jbox2d.collision.CircleDef;  
import org.jbox2d.collision.CircleShape;
import org.jbox2d.collision.PolygonDef;  
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;  
import org.jbox2d.common.XForm;
import org.jbox2d.dynamics.Body;  
import org.jbox2d.dynamics.BodyDef;  
import org.jbox2d.dynamics.World;  
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;


public class GameLogic implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5506332331816896792L;
	private Level level;
    private final static float RATE = 100.0f;
	private AABB worldAABB;
	private World world;
	private final static float timeStep = (float)(1.0/60.0); 
	private int iterations = 10;
	private Ball bubbleBall;
	private Body ballBody;
	private Flag flag;
	private float[] ballMass;
	private ArrayList<Coin> coins;
	private ArrayList<Magnet> magnets;
	private ArrayList<BallFactory> ballFactories;
	private ArrayList<AccelerationMark> accelerationMark;
	private int leftAccelerationTime,rightAccelerationTime,balloonLeftAccelerationTime,balloonRightAccelerationTime; 
	
    public GameLogic(Level level){
    	this.level = level;
    }
    public void logicInit(){
    	
    	worldAABB = new AABB();  
	    worldAABB.lowerBound.set(-40.0f,-32.0f); 
	    worldAABB.upperBound.set(40.0f, 32.0f);
	       
	    Vec2 gravity = new Vec2(0.0f,10.0f);  
	    boolean doSleep = true;  
	       
	    world = new World(worldAABB, gravity, doSleep); 
    	ArrayList<Item> settings = level.getSettings();
    	ArrayList<Item> dynamicTools = level.getDynamicTools();
    	
    	coins = new ArrayList<Coin>();
    	magnets = new ArrayList<Magnet>();
    	ballFactories = new ArrayList<BallFactory>();
    	accelerationMark = new ArrayList<AccelerationMark>();
    	
    	leftAccelerationTime=0;
    	rightAccelerationTime=0; 
    	
    	for(int i=0;i<settings.size();i++){
    		Item item = settings.get(i);
    		
    		if(item instanceof BallFactory){
    			ballFactories.add((BallFactory)item);  	
    	    }
    		else if(item instanceof Wall){
    			createWall((Wall)item);  	
    	    }
    		else if(item instanceof RectangleMetalItem){
    			createRectangleMetalItem((RectangleMetalItem)item);  	
    	    }
    		else if(item instanceof TriangleMetalItem){
    			createTriangleMetalItem((TriangleMetalItem)item);  	
    	    }
    	}


    	for(int i=0;i<dynamicTools.size();i++){
    		Item item = dynamicTools.get(i);
    		if(item instanceof Bat){
    			createBat((Bat)item);  	
    	    }
    		else if(item instanceof Ball){
    			createBall((Ball)item);
    			bubbleBall = (Ball)item;
    		}
    		else if(item instanceof Flag){
    			flag = (Flag)item;
    		}
    		else if(item instanceof Coin){
    			coins.add((Coin)item);  	
    	    }
    		else if(item instanceof Glass){
    			createGlass((Glass)item);  	
    	    }
    		else if(item instanceof Magnet){
    			magnets.add((Magnet)item);  	
    	    }
    		if(item instanceof AccelerationMark){
    			accelerationMark.add((AccelerationMark)item);  	
    	    }
    		else if(item instanceof Cushion){
    			createCushion((Cushion)item);  	
    	    }
    		else if(item instanceof RectangleWoodenItem){
    			createRectangleWoodenItem((RectangleWoodenItem)item);  	
    	    }
    		else if(item instanceof TriangleWoodenItem){
    			createTriangleWoodenItem((TriangleWoodenItem)item);  	
    	    }
    		else if(item instanceof WoodenSeesaw){
    			createWoodenSeesaw((WoodenSeesaw)item);  	
    	    }
    	}
    }
    public void move(){
    	world.step(timeStep, iterations);//¿ªÊ¼Ä£Äâ  
    	for(Body body =world.getBodyList();body !=null;body=body.getNext()){
    		Item item = (Item)(body.getUserData());
    		if(item == null) continue;
    		if(item instanceof Gravity){
    			if(item instanceof CircleArea){
    				Vec2 position = body.getPosition();
        			//System.out.printf("x: %f	y: %f\n",body.getPosition().x,body.getPosition().y);
        		//	System.out.printf("c.x: %f	c.y: %f	r:%f\n",center.x,center.y,radius);
        			
    				item.setPosition(new Position((int)(position.x*RATE)+320,
    						(int)(position.y*RATE)+240));
    			}
    			else if(item instanceof RectangleArea){
    				Vec2 position = body.getPosition();
    				XForm xf = new XForm();
    		    	xf.position.set(position);
    		    	xf.R.set(body.getAngle());
    		    	
    		    	PolygonShape shape = (PolygonShape)(body.getShapeList());
    		    	
    	            Vec2[] localVertices = shape.getVertices();
    				
    				Vec2[] vertices = new Vec2[Settings.maxPolygonVertices];
    				
    				Position[] newPosition = new Position[4];
    				
    				for (int j = 0; j <4; j++) {
    					vertices[j] = XForm.mul(xf, localVertices[j]);
    					newPosition[j]=new Position();
    					newPosition[j].setX((int) (vertices[j].x*RATE)+320);
    					newPosition[j].setY((int) (vertices[j].y*RATE)+240);
    				}
    				((RectangleArea) item).setVertex(newPosition);	
    			}
    			else if(item instanceof TriangleArea){
    				Vec2 position = body.getPosition();
    				XForm xf = new XForm();
    		    	xf.position.set(position);
    		    	xf.R.set(body.getAngle());
    		    	
    		    	PolygonShape shape = (PolygonShape)(body.getShapeList());
    		    	
    	            Vec2[] localVertices = shape.getVertices();
    				
    				Vec2[] vertices = new Vec2[Settings.maxPolygonVertices];
    				
    				Position[] newPosition = new Position[3];
    				
    				for (int j = 0; j <3; j++) {
    					vertices[j] = XForm.mul(xf, localVertices[j]);
    					newPosition[j]=new Position();
    					newPosition[j].setX((int) (vertices[j].x*RATE)+320);
    					newPosition[j].setY((int) (vertices[j].y*RATE)+240);
    				}
    				((TriangleArea) item).setVertex(newPosition);
    				
    				//System.out.println("("+position.x+","+position.y+")");
    			}
    			else if(item instanceof WoodenSeesaw){
    				Vec2 position = body.getPosition();
    				XForm xf = new XForm();
    				xf.position.set(position);
    		    	xf.R.set(body.getAngle());
    		    	
    		    	PolygonShape shape = (PolygonShape)(body.getShapeList());

    		    	Vec2[] localVertices = shape.getVertices();
    	            
    		    	Vec2[] vertices = new Vec2[Settings.maxPolygonVertices];

    				for (int i = 0; i <4;  ++i) {
    					vertices[i] = XForm.mul(xf, localVertices[i]);
    				}
    				
    				Position[] newPosition = new Position[7];
    				for(int j=0;j<7;j++)
    					newPosition[j]=new Position();
    				newPosition[2].setX((int) (vertices[0].x*RATE)+320);
    				newPosition[2].setY((int) (vertices[0].y*RATE)+240);
    				newPosition[3].setX((int) (vertices[1].x*RATE)+320);
    				newPosition[3].setY((int) (vertices[1].y*RATE)+240);
    				newPosition[4].setX((int) (vertices[2].x*RATE)+320);
    				newPosition[4].setY((int) (vertices[2].y*RATE)+240);
    				newPosition[5].setX((int) (vertices[3].x*RATE)+320);
    				newPosition[5].setY((int) (vertices[3].y*RATE)+240);;
    				
    				localVertices = ((PolygonShape)(shape.getNext())).getVertices();
    				
    				
    				vertices = new Vec2[Settings.maxPolygonVertices];

    				for (int i = 0; i <3;  ++i) {
    					vertices[i] = XForm.mul(xf, localVertices[i]);
    				}
    				newPosition[0].setX((int) (vertices[0].x*RATE)+320);
    				newPosition[0].setY((int) (vertices[0].y*RATE)+240);
    				newPosition[1].setX((int) (vertices[1].x*RATE)+320);
    				newPosition[1].setY((int) (vertices[1].y*RATE)+240);
    				newPosition[6].setX((int) (vertices[2].x*RATE)+320);
    				newPosition[6].setY((int) (vertices[2].y*RATE)+240);
    				
    				((WoodenSeesaw) item).setVertex(newPosition);
    			}
    		}
    		else if(item instanceof Bat){
    			Vec2 position = body.getPosition();
				XForm xf = new XForm();
		    	xf.position.set(position);
		    	xf.R.set(body.getAngle());
		    	
		    	PolygonShape shape = (PolygonShape)(body.getShapeList());
		    	
	            Vec2[] localVertices = shape.getVertices();
				
				Vec2[] vertices = new Vec2[Settings.maxPolygonVertices];
				
				Position[] newPosition = new Position[4];
				
				for (int j = 0; j <4; j++) {
					vertices[j] = XForm.mul(xf, localVertices[j]);
					newPosition[j]=new Position();
					newPosition[j].setX((int) (vertices[j].x*RATE)+320);
					newPosition[j].setY((int) (vertices[j].y*RATE)+240);
				}
				((RectangleArea) item).setVertex(newPosition);
				if(((Bat)item).isFunctioning()){
					((RevoluteJoint)(body.m_jointList.joint)).m_enableMotor=true;
				}
				else{
					((RevoluteJoint)(body.m_jointList.joint)).m_enableMotor=false;
					body.m_angularVelocity=0;
				}
    		}
    	}
    }
    public boolean collisionDetection(){
    	//System.out.print(ballBody.m_linearVelocity.x+","+ballBody.m_linearVelocity.y);
    	//System.out.println(","+ballBody.m_force.x+","+ballBody.m_force.y);
    	
    	if(contact(bubbleBall,flag,0.5)){
    		return true;
    	}
    	for(int i=0;i<coins.size();i++){
    		if(contact(bubbleBall,coins.get(i),0.9)){
    			coins.get(i).trigger();
    		}
    	}
    	for(int i=0;i<magnets.size();i++){
    		if(contact(bubbleBall,magnets.get(i),1.0)){
    			if(magnets.get(i).isFunctioning() && bubbleBall.getState()==Ball.METAL_BALL){
    				Vec2 force = new Vec2();
    				force.x = magnets.get(i).getPosition().getX()-ballBody.getPosition().x;
    				force.y = magnets.get(i).getPosition().getY()-ballBody.getPosition().y;
    				float a = force.x*force.x+force.y*force.y;
    				force.x = (float) (force.x/Math.sqrt(a))/a*magnets.get(i).getRadius()*magnets.get(i).getRadius()*30;
    				force.y = (float) (force.y/Math.sqrt(a))/a*magnets.get(i).getRadius()*magnets.get(i).getRadius()*30;
    				
    				ballBody.applyForce(force, ballBody.getPosition());
    				
    				//System.out.print(ballBody.m_linearVelocity.x+","+ballBody.m_linearVelocity.y);
    		    	//System.out.println(","+ballBody.m_force.x+","+ballBody.m_force.y+","+a);
    			}
    		}
    	}
    	for(int i=0;i<ballFactories.size();i++){
    		if(contact(bubbleBall,ballFactories.get(i),0.5)){
    			bubbleBall.setState(ballFactories.get(i).getTargetState());
    		}
    	}
    	if(bubbleBall.getState()==Ball.NORMAL_BALL){
    		ballBody.m_mass = ballMass[Ball.NORMAL_BALL];
    		ballBody.getShapeList().m_restitution = 0.5f;
		}
		else if(bubbleBall.getState()==Ball.METAL_BALL){
			ballBody.m_mass = ballMass[Ball.METAL_BALL];
	  		ballBody.getShapeList().m_restitution = 0.2f;
		}
		else{
			ballBody.m_linearVelocity= new Vec2(0,0);
			ballBody.m_mass = ballMass[Ball.BALLOON];
			ballBody.getShapeList().m_restitution = 0.0f;
			ballBody.getShapeList().m_friction = 0.0f;
			ballBody.applyForce(new Vec2(0,-50), ballBody.getPosition());
			//System.out.println(ballBody.getAngularVelocity());
		}
    	for(int i=0;i<accelerationMark.size();i++){
    		if(contact(bubbleBall,accelerationMark.get(i),1.0)){
    			if(accelerationMark.get(i).getDirection()==AccelerationMark.LEFT_DIRECTION){
    				if(bubbleBall.getState() != Ball.BALLOON){
    					leftAccelerationTime = 6;  
    					rightAccelerationTime = 0;
    				}
    				else{
    					balloonLeftAccelerationTime = 90;  
    					balloonRightAccelerationTime = 0;
    				}
    			}
    			else if(accelerationMark.get(i).getDirection()==AccelerationMark.RIGHT_DIRECTION){
    				if(bubbleBall.getState() != Ball.BALLOON){
    					rightAccelerationTime = 6;  
    					leftAccelerationTime = 0;
    				}
    				else{
    					balloonRightAccelerationTime = 90;  
    					balloonLeftAccelerationTime = 0;
    				}
    			}
    		}
    	}
    	if(bubbleBall.getState() == Ball.BALLOON){
    		if(balloonLeftAccelerationTime>0){
    			ballBody.applyForce(new Vec2(-40,0), ballBody.getPosition());
    			balloonLeftAccelerationTime--;
    		}
    		if(balloonRightAccelerationTime>0){
    			ballBody.applyForce(new Vec2(40,0), ballBody.getPosition());
    			balloonRightAccelerationTime--;
    		}
    	}
    	else{
    		if(leftAccelerationTime>0){
    			ballBody.applyForce(new Vec2(-5,0), ballBody.getPosition());
    			leftAccelerationTime--;
    		}
    		if(rightAccelerationTime>0){
    			ballBody.applyForce(new Vec2(5,0), ballBody.getPosition());
    			rightAccelerationTime--;
    		}	
    	}
    	for(ContactEdge b = ballBody.getContactList();b != null;b=b.next){
			Item item = (Item) b.other.getUserData();
			if(item instanceof Glass){
				/*float energy = (ballBody.m_linearVelocity.x*ballBody.m_linearVelocity.x
							  +ballBody.m_linearVelocity.y*ballBody.m_linearVelocity.y)
							  *ballBody.m_mass*0.5f;*/
				if(((Glass)item).pass(ballBody.m_mass, new Vector(ballBody.m_linearVelocity.x, ballBody.m_linearVelocity.y))){
					((Glass)item).setDisappear(true);
					world.destroyBody(b.other);
				}
//				System.out.println(energy);
			}
		}
    	
    	return false;
    }
    private boolean contact(CircleArea ca1,CircleArea ca2,double rate){
    	int dx = ((Item)ca1).getPosition().getX()-((Item)ca2).getPosition().getX();
    	int dy = ((Item)ca1).getPosition().getY()-((Item)ca2).getPosition().getY();
    	int d = ca1.getRadius()+ca2.getRadius();
    	//System.out.printf("dx: %d, dy: %d,d: %d\n",dx,dy,d);
    	if((dx*dx+dy*dy)<(d*d*rate)){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    public synchronized boolean refreshLogic(){
    	move();
    	return collisionDetection();
    }
    private void createBall(Ball ball){
    	CircleDef shape = new CircleDef();  
        shape.density = 1;  
        shape.friction = 0.7f; 
        shape.restitution = 0.5f;  
        shape.radius = ball.getRadius()/RATE;  
            
        BodyDef bodyDef = new BodyDef();  
        bodyDef.position.set(ball.getPosition().getX()/RATE-320/RATE, 
        		ball.getPosition().getY()/RATE-240/RATE);  
        bodyDef.userData=ball;        
        Body body = world.createDynamicBody(bodyDef);  
        body.createShape(shape);  
        body.setMassFromShapes();
        ballBody = body;
        ballMass = new float[3];
        ballMass[Ball.NORMAL_BALL] = ballBody.getMass();
        ballMass[Ball.METAL_BALL] = ballBody.getMass()*5;
        ballMass[Ball.BALLOON] = ballBody.getMass()/10;
        
    }
    private void createBat(Bat bat){
    	CircleDef fixShape = new CircleDef();  
        fixShape.density = 0;  ;  
        fixShape.radius = 1/RATE;  
            
        BodyDef bodyDef = new BodyDef();  
        bodyDef.position.set(-3.8f, -3.0f);  
        bodyDef.userData=null;        
        Body fix = world.createStaticBody(bodyDef);  
        fix.createShape(fixShape);  
        fix.setMassFromShapes();
    	
    	
    	PolygonDef shape = new PolygonDef();  
        shape.density = 2;  
        shape.friction = 0.5f;
        shape.restitution = 1.0f;
        
        for(int i=0;i<4;i++){
        	float x = bat.getVertex()[i].getX()-bat.getPosition().getX();
        	float y = bat.getVertex()[i].getY()-bat.getPosition().getY();
        	shape.vertices.add(new Vec2(x/RATE,y/RATE));
        }
	        
        bodyDef = new BodyDef(); 
        bodyDef.position.set(bat.getPosition().getX()/RATE-320/RATE, 
        		bat.getPosition().getY()/RATE-240/RATE);
        bodyDef.userData = bat;
        Body body = world.createDynamicBody(bodyDef);  
        body.createShape(shape);  
        body.setMassFromShapes();
        
        RevoluteJointDef rjd = new RevoluteJointDef();
        rjd.initialize(body,fix, body.getPosition());
		rjd.motorSpeed = -5.0f * (float)Math.PI;
		rjd.maxMotorTorque = 10000000.0f;
		rjd.enableMotor = false;

		world.createJoint(rjd);   		
    }
    private void createCushion(Cushion cushion){
    	PolygonDef shape = new PolygonDef();  
        shape.density = 0;  
        shape.friction = 0.0f;
        shape.restitution = 1.0f;
        
        for(int i=0;i<4;i++){
        	float x = cushion.getVertex()[i].getX()-cushion.getPosition().getX();
        	float y = cushion.getVertex()[i].getY()-cushion.getPosition().getY();
        	shape.vertices.add(new Vec2(x/RATE,y/RATE));
        }
	        
        BodyDef bodyDef = new BodyDef(); 
        bodyDef.position.set(cushion.getPosition().getX()/RATE-320/RATE, 
        		cushion.getPosition().getY()/RATE-240/RATE);
        bodyDef.userData = cushion;
        Body body = world.createStaticBody(bodyDef);  
        body.createShape(shape);  
        body.setMassFromShapes();
    }
    private void createGlass(Glass glass){
    	PolygonDef shape = new PolygonDef();  
        shape.density = 0;  
        shape.friction = 0.5f;
        shape.restitution = 0.5f;
        
        for(int i=0;i<4;i++){
        	float x = glass.getVertex()[i].getX()-glass.getPosition().getX();
        	float y = glass.getVertex()[i].getY()-glass.getPosition().getY();
        	shape.vertices.add(new Vec2(x/RATE,y/RATE));
        }
	        
        BodyDef bodyDef = new BodyDef(); 
        bodyDef.position.set(glass.getPosition().getX()/RATE-320/RATE, 
        		glass.getPosition().getY()/RATE-240/RATE);
        bodyDef.userData = glass;
        Body body = world.createStaticBody(bodyDef);  
        body.createShape(shape);  
        body.setMassFromShapes();
    }
    private void createRectangleWoodenItem(RectangleWoodenItem rectangleWoodenItem){
    	PolygonDef shape = new PolygonDef();  
        shape.density = 1;  
        shape.friction = 0.5f;
     //   shape.restitution = 0.5f;
        
        for(int i=0;i<4;i++){
        	float x = rectangleWoodenItem.getVertex()[i].getX()-rectangleWoodenItem.getPosition().getX();
        	float y = rectangleWoodenItem.getVertex()[i].getY()-rectangleWoodenItem.getPosition().getY();
        	shape.vertices.add(new Vec2(x/RATE,y/RATE));
        }
	        
        BodyDef bodyDef = new BodyDef(); 
        bodyDef.position.set(rectangleWoodenItem.getPosition().getX()/RATE-320/RATE, 
        		rectangleWoodenItem.getPosition().getY()/RATE-240/RATE);
        bodyDef.userData = rectangleWoodenItem;

        Body body = world.createDynamicBody(bodyDef); 
        
        
        body.createShape(shape);  
        body.setMassFromShapes();
    }
    private void createRectangleMetalItem(RectangleMetalItem rectangleMetalItem){
    	PolygonDef shape = new PolygonDef();  
        shape.density = 0;  
        shape.restitution = 0.5f;
        shape.friction = 0.7f;
        
        for(int i=0;i<4;i++){
        	float x = rectangleMetalItem.getVertex()[i].getX()-rectangleMetalItem.getPosition().getX();
        	float y = rectangleMetalItem.getVertex()[i].getY()-rectangleMetalItem.getPosition().getY();
        	shape.vertices.add(new Vec2(x/RATE,y/RATE));
        }
	        
        BodyDef bodyDef = new BodyDef(); 
        bodyDef.position.set(rectangleMetalItem.getPosition().getX()/RATE-320/RATE, 
        		rectangleMetalItem.getPosition().getY()/RATE-240/RATE);
        bodyDef.userData = rectangleMetalItem;
        Body body = world.createStaticBody(bodyDef);  
        body.createShape(shape);  
        body.setMassFromShapes();
    }
    private void createTriangleWoodenItem(TriangleWoodenItem triangleWoodenItem){
    	PolygonDef shape = new PolygonDef();  
        shape.density = 2;  
        shape.friction = 0.5f;
        shape.restitution = 0.3f;
        
        for(int i=0;i<3;i++){
        	float x = triangleWoodenItem.getVertex()[i].getX()-triangleWoodenItem.getPosition().getX();
        	float y = triangleWoodenItem.getVertex()[i].getY()-triangleWoodenItem.getPosition().getY();
        	shape.vertices.add(new Vec2(x/RATE,y/RATE));
        }
	        
        BodyDef bodyDef = new BodyDef(); 
        bodyDef.position.set(triangleWoodenItem.getPosition().getX()/RATE-320/RATE, 
        		triangleWoodenItem.getPosition().getY()/RATE-240/RATE);
        bodyDef.userData = triangleWoodenItem;
        Body body = world.createDynamicBody(bodyDef);  
        body.createShape(shape);  
        body.setMassFromShapes();
    }
    private void createTriangleMetalItem(TriangleMetalItem triangleMetalItem){
    	PolygonDef shape = new PolygonDef();  
        shape.density = 0;  
        shape.restitution = 0.5f;
        shape.friction = 0.7f;
        
        for(int i=0;i<3;i++){
        	float x = triangleMetalItem.getVertex()[i].getX()-triangleMetalItem.getPosition().getX();
        	float y = triangleMetalItem.getVertex()[i].getY()-triangleMetalItem.getPosition().getY();
        	shape.vertices.add(new Vec2(x/RATE,y/RATE));
        }
	        
        BodyDef bodyDef = new BodyDef(); 
        bodyDef.position.set(triangleMetalItem.getPosition().getX()/RATE-320/RATE, 
        		triangleMetalItem.getPosition().getY()/RATE-240/RATE);
        bodyDef.userData = triangleMetalItem;
        Body body = world.createStaticBody(bodyDef);  
        body.createShape(shape);  
        body.setMassFromShapes();
    }
    private void createWall(Wall wall){
    	PolygonDef shape = new PolygonDef();  
        shape.density = 0;  
        shape.restitution = 0.5f;
        shape.friction = 0.7f;
        
        for(int i=0;i<4;i++){
        	float x = wall.getVertex()[i].getX()-wall.getPosition().getX();
        	float y = wall.getVertex()[i].getY()-wall.getPosition().getY();
        	shape.vertices.add(new Vec2(x/RATE,y/RATE));
        }
	        
        BodyDef bodyDef = new BodyDef(); 
        bodyDef.position.set(wall.getPosition().getX()/RATE-320/RATE, 
        		wall.getPosition().getY()/RATE-240/RATE);
        bodyDef.userData = wall;
        Body body = world.createStaticBody(bodyDef);  
        body.createShape(shape);  
        body.setMassFromShapes();
    }
    private void createWoodenSeesaw(WoodenSeesaw woodenSeesaw){
    	PolygonDef shape = new PolygonDef();  
        shape.density = 5;  
        shape.friction = 0.5f;
        shape.restitution = 0.3f; 
        
        float[] x=new float[7];
        float[] y=new float[7];
        for(int i=0;i<7;i++){
        	x[i]=woodenSeesaw.getVertex()[i].getX()-woodenSeesaw.getPosition().getX();
        	y[i]=woodenSeesaw.getVertex()[i].getY()-woodenSeesaw.getPosition().getY();
        }
        shape.vertices.add(new Vec2(x[0]/RATE,y[0]/RATE));
        shape.vertices.add(new Vec2(x[1]/RATE,y[1]/RATE));  
        shape.vertices.add(new Vec2(x[6]/RATE,y[6]/RATE));
        
        PolygonDef shape1 = new PolygonDef();  
        shape1.density = 2;  
        shape1.friction = 0.0f;
        shape1.restitution = 1.0f; 
        shape1.vertices.add(new Vec2(x[2]/RATE,y[2]/RATE));	        
        shape1.vertices.add(new Vec2(x[3]/RATE,y[3]/RATE));
        shape1.vertices.add(new Vec2(x[4]/RATE,y[4]/RATE));
        shape1.vertices.add(new Vec2(x[5]/RATE,y[5]/RATE));	       
        
        BodyDef bodyDef = new BodyDef(); 
        bodyDef.position.set(woodenSeesaw.getPosition().getX()/RATE-320/RATE, 
        		woodenSeesaw.getPosition().getY()/RATE-240/RATE);
        bodyDef.userData = woodenSeesaw;
        
        Body body = world.createDynamicBody(bodyDef);  
        body.createShape(shape);  
        body.createShape(shape1);  
        body.setMassFromShapes();
    }
    
    
}
