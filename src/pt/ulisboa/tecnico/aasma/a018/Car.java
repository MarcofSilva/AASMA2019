package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;
import java.util.Random;

public abstract class Car extends Object {

    public int direction;

    public Color color;
    protected Random random;
    protected int totalTicks;
    protected int totaldistance; // distance covered
    protected int totalActionsTaken; // ticks that he didnt stay still

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
        else{
            this.direction = 270;
        }
        this.random = new Random();
    }

    protected float getStepsStopped(){
        float metric = ((totalTicks - totalActionsTaken)/(float) totaldistance)*100;
        return metric;
    }

    protected float getStepsStopped2(){
        float metric = ((totalTicks)/(float) totaldistance);
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
        if(tf != null){
            return tf.getColor() == Color.green;
        }
        return true;
    }

    protected boolean intoIntersection(Point ahead){
        if(Board.getBlock(ahead) instanceof RoadIntersectBlock){
            return !(Board.getBlock(point) instanceof RoadIntersectBlock);
        }
        return false;
    }

    protected boolean inCurve(){
        return Board.getBlock(point) instanceof RoadCurveBlock;
    }

    /* Check if the cell ahead is a wall */
    protected boolean isRoad(Point ahead){
        if(ahead.x < 0 || ahead.y < 0 || ahead.x >= Board.nX || ahead.y >= Board.nY){
            Board.removeCar(this);
            return false;
        }
        return true;
    }


    /* Rotate agent to right */
    public void rotateRight(){
        totalActionsTaken++;
        direction = (direction + 90)%360;
    }

    /* Rotate agent to left */
    public void rotateLeft(){
        totalActionsTaken++;
        int aux = direction - 90;
        if(aux < 0){
            aux = 360 + aux;
        }
        direction = (aux)%360;
    }

    public void rotateAccordingly(Point pointToRotate){
        if(direction == 270){
            if(pointToRotate.x > point.x){
                rotateRight();
            }
            else{
                rotateLeft();
            }
        }
        else if(direction == 90){
            if(pointToRotate.x > point.x){
                rotateLeft();
            }
            else{
                rotateRight();
            }
        }
        else if(direction == 0){
            if(pointToRotate.y > point.y){
                rotateLeft();
            }
            else{
                rotateRight();
            }
        }
        else{
            if(pointToRotate.y > point.y){
                rotateRight();
            }
            else{
                rotateLeft();
            }
        }
    }


    abstract void stay();

    abstract void agentDecision();
    //abstract void signIntoIntersection();


    /**********************/
    /**** D: auxiliary ****/
    /**********************/

    /* Position ahead */
    protected Point aheadPosition(){
        Point newpoint = new Point(point.x, point.y);
        switch(direction){
            case 0:
                newpoint.x++;
                break;
            case 90:
                newpoint.y--;
                break;
            case 180:
                newpoint.x--;
                break;
            default:
                newpoint.y++;
        }
        return newpoint;
    }

    protected Point twoaheadPosition(){
        Point newpoint = new Point(point.x, point.y);
        switch(direction){
            case 0:{
                newpoint.x = newpoint.x + 2;
                break;
            }
            case 90:{
                newpoint.y = newpoint.y - 2;
                break;
            }
            case 180:{
                newpoint.x = newpoint.x - 2;
                break;
            }
            default:{
                newpoint.y = newpoint.y + 2;
                break;
            }
        }
        return newpoint;
    }
}
