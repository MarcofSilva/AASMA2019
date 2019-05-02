package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;

public class CarNormal extends Car {
    private Point ahead;

    public CarNormal(Point point, int direction){
        super(point, direction,Color.MAGENTA);
        Board.incNormal();
    }
    public CarNormal(Point point){
        super(point, Color.MAGENTA);
        Board.incNormal();
    }
    /**********************
     **** A: decision *****
     **********************/

    public void agentDecision() {
        ahead = aheadPosition();
        //checks if i can move ahead

        if(intoIntersection(ahead)){
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
                moveAhead(ahead);
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
                            moveAhead(ahead);
                    }
                    break;
                case "right":
                    if((Board.getBlock(ahead) instanceof RoadCurveBlock))
                        rotateRight();
                    else {
                        if(!Board.isEmpty(ahead));
                        else
                            moveAhead(ahead);
                    }
                    break;
                case "continue":
                    if(!Board.isEmpty(ahead));
                    else
                        moveAhead(ahead);
                    break;
            }
        }
        else if(!isRoad(ahead)) {
            stay();
        }

        else if(!Board.isEmpty(ahead)){
            stay();
        }

        else {
            moveAhead(ahead);
        }
    }

    public void moveAhead(Point ahead) {
        Board.updateEntityPosition(point,ahead);
        this.point = ahead;
    }


}
