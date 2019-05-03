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

	protected boolean isGreen(Point point){
		TrafficLight tf = (TrafficLight) Board.getObject(point);
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
	public void rotateRandomly() {
		if(random.nextBoolean()) rotateLeft();
		else rotateRight();
	}
	
	/* Rotate agent to right */
	public void rotateRight() {
		direction = (direction+90)%360;
	}
	
	/* Rotate agent to left */
	public void rotateLeft() {
		direction = (direction-90)%360;
	}
	
	/* Move agent forward */
	public void stay(){

	}

	abstract void agentDecision();

	
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
}
