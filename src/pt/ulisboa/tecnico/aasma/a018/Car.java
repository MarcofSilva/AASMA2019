package pt.ulisboa.tecnico.aasma.a018;

import sun.rmi.runtime.Log;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

/**
 * Car behavior
 * @author Rui Henriques
 */
public abstract class Car extends Object {

	public int direction;

	public Color color;
	protected Random random;
	protected int totalTicks;
	protected int totaldistance; // ticks that actually moved
	protected int totalActionsTaken; // ticks that he didnt stay still

	/*protected String decision;
	protected String myID;*/

	public Car(Point point, int direction, Color color){
		super(color, point);
		this.direction = direction;
		this.random = new Random();
	}

	public Car(Point point, Color color){
		super(color, point);
		if(point.x == 0){
			this.direction = 0;
		}
		else if(point.y == 29){
			this.direction = 90;
		}
		else if(point.x == 29){
			this.direction = 180;
		}
		else {
			this.direction = 270;
		}
		this.random = new Random();
	}

	
	/********************/
	/**** B: sensors ****/
	/********************/

	protected float getStepsStopped(){
		float metric = ((totalTicks - totalActionsTaken)/(float)totaldistance)*100;
		return metric;
	}

	protected boolean isGreen(Point point){
		Point trafficLightLocal = new Point(point.x, point.y);
		switch(direction){
			case 0:
				trafficLightLocal.y--;
				break;
			case 90:
				trafficLightLocal.x--;
				break;
			case 180:
				trafficLightLocal.y++;
				break;
			default:
				trafficLightLocal.x++;
		}
		TrafficLight tf = (TrafficLight) Board.getObject(trafficLightLocal);
		if(tf != null) {
			return tf.getColor() == Color.green;
		}
		return true;
	}

	protected boolean intoIntersection(Point ahead){
		if(Board.getBlock(ahead) instanceof RoadIntersectBlock){
			if(Board.getBlock(point) instanceof RoadIntersectBlock){
				return false;
			}
			return true;
		}
		return false;
	}

	protected boolean inCurve(){
		return Board.getBlock(point) instanceof RoadCurveBlock;
	}

	/* Check if the cell ahead is a wall */
	protected boolean isRoad(Point ahead) {
		if(ahead.x<0 || ahead.y<0 || ahead.x>=Board.nX || ahead.y>=Board.nY){
			Board.removeCar(this);
			return false;
		}
		return true;
	}

	/**********************/
	/**** C: actuators ****/
	/**********************/

	
	/* Rotate agent to right */
	public void rotateRight() {
		totalActionsTaken++;
		direction = (direction+90)%360;
	}
	
	/* Rotate agent to left */
	public void rotateLeft() {
		totalActionsTaken++;
		int aux  = direction-90;
		if(aux < 0){
			aux = 360 + aux;
		}
		direction = (aux)%360;
	}
	
	/* Move agent forward */
	abstract void stay();
	abstract void agentDecision();
	//abstract void signIntoIntersection();

	
	/**********************/
	/**** D: auxiliary ****/
	/**********************/

	/* Position ahead */
	protected Point aheadPosition() {
		Point newpoint = new Point(point.x,point.y);
		switch(direction) {
			case 0: newpoint.x++; break;
			case 90: newpoint.y--; break;
			case 180: newpoint.x--; break;
			default: newpoint.y++;
		}
		return newpoint;
	}

	protected Point twoaheadPosition(){
		Point newpoint = new Point(point.x,point.y);
		switch(direction) {
			case 0: {
				newpoint.x = newpoint.x + 2;
				break;
			}
			case 90: {
				newpoint.y = newpoint.y - 2;
				break;
			}
			case 180: {
				newpoint.x = newpoint.x - 2;
				break;
			}
			default: {
				newpoint.y = newpoint.y + 2;
				break;
			}
		}
		return newpoint;
	}
}
