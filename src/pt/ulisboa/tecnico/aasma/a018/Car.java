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
	private Point ahead;

	public Color color;
	protected Random random;

	public Car(Point point, int direction, Color color){
		super(color, point);
		this.direction = direction;
		this.random = new Random();
	}
	
	/**********************
	 **** A: decision ***** 
	 **********************/
	
	public void agentDecision() {
	  ahead = aheadPosition();
	  //checks if i can move ahead

	  if(intoIntersection()){
		  Point trafficLightLocal = new Point(point.x,point.y);
		  switch(direction) {
			  case 0: trafficLightLocal.y--; break;
			  case 90: trafficLightLocal.x--; break;
			  case 180: trafficLightLocal.y++; break;
			  default: trafficLightLocal.x++;
		  }
		  if(!isGreen(trafficLightLocal)){

		  }
		  else if(!Board.isEmpty(ahead)){

		  }
		  else {
			  moveAhead();
		  }
	  }

	  //TODO clean this, to much repeated code
	  else if(inCurve()) {
	  	switch (((RoadCurveBlock) Board.getBlock(point)).getAction()) {
			case "left":
				if(!(Board.getBlock(ahead) instanceof RoadCurveBlock))
					rotateLeft();
				else {
					if (!Board.isEmpty(ahead)) ;
					else
						moveAhead();
				}
				break;
			case "right":
				if((Board.getBlock(ahead) instanceof RoadCurveBlock))
					rotateRight();
				else {
					if(!Board.isEmpty(ahead));
					else
						moveAhead();
				}
				break;
			case "continue":
				if(!Board.isEmpty(ahead));
				else
					moveAhead();
				break;
		}
	  }
	  else if(!isRoad()) {
	  	//Intentionally left blank
	  }

	  else if(!Board.isEmpty(ahead)){
	  	//Intentionally left blank
	  }

	  else {
		  moveAhead();
	  }
	}
	
	/********************/
	/**** B: sensors ****/
	/********************/

	private boolean isGreen(Point point){
		TrafficLight tf = (TrafficLight) Board.getObject(point);
		if(tf != null) {
			return tf.getColor() == Color.green;
		}
		return true;
	}

	private boolean intoIntersection(){
		if(Board.getBlock(ahead) instanceof RoadIntersectBlock){
			if(Board.getBlock(point) instanceof RoadIntersectBlock){
				return false;
			}
			return true;
		}
		return false;
	}

	private boolean inCurve(){
		return Board.getBlock(point) instanceof RoadCurveBlock;
	}

	/* Check if the cell ahead is a wall */
	private boolean isRoad() {
		if(ahead.x<0 || ahead.y<0 || ahead.x>=Board.nX || ahead.y>=Board.nY){
			return false;
		}
		if(Board.getBlock(ahead).getColor() == Color.black){
			return true;
		}
		return false;
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
	public void moveAhead() {
		Board.updateEntityPosition(point,ahead);
		point = ahead;
	}

	
	/**********************/
	/**** D: auxiliary ****/
	/**********************/

	/* Position ahead */
	private Point aheadPosition() {
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
