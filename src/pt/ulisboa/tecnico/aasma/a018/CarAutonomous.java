package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CarAutonomous extends Car {
    private Point ahead;
    private Point pathAhead;

    private List<Point> path = new ArrayList<>();

    public CarAutonomous(Point point, int direction){
        super(point, direction,Color.BLUE);
        Board.incAutonomous();
    }

    public CarAutonomous(Point point){
        super(point,Color.BLUE);
        Board.incAutonomous();
    }

    @Override
    void stay(){

    }

    /**********************
     **** A: decision *****
     **********************/

    public void agentDecision() {
        this.totalTicks++;
        ahead = aheadPosition();
        if(!isRoad(ahead)){

        }
        else if(path.size() > 0){
            if(intoIntersection(ahead)){
                Point trafficLightLocal = new Point(point.x,point.y);
                switch(direction) {
                    case 0: trafficLightLocal.y--; break;
                    case 90: trafficLightLocal.x--; break;
                    case 180: trafficLightLocal.y++; break;
                    default: trafficLightLocal.x++;
                }
                if(!isGreen(trafficLightLocal)){
                    stay();
                }
                else if(!Board.isEmpty(ahead)){
                    stay();
                }
                else {
                    moveAhead(ahead);
                    path.remove(0);
                }
            }
            else {
                pathAhead = path.get(0);
                if(!comparePoints(ahead, pathAhead)){
                    if(direction == 270){
                        if(pathAhead.y != point.y){
                            moveAhead(ahead);
                            path.remove(0);
                        }
                        else {
                            if(pathAhead.x > point.x){
                                rotateRight();
                            }
                            else {
                                rotateLeft();
                            }
                        }
                    }
                    else if(direction == 90){
                        if(pathAhead.y != point.y){
                            moveAhead(ahead);
                            path.remove(0);
                        }
                        else {
                            if(pathAhead.x > point.x){
                                rotateLeft();
                            }
                            else {
                                rotateRight();
                            }
                        }
                    }
                    else if(direction == 0){
                        if(pathAhead.x != point.x){
                            moveAhead(ahead);
                            path.remove(0);
                        }
                        else {
                            if(pathAhead.y > point.y){
                                rotateLeft();
                            }
                            else {
                                rotateRight();
                            }
                        }
                    }
                    else {
                        if(pathAhead.x != point.x){
                            moveAhead(ahead);
                            path.remove(0);
                        }
                        else {
                            if(pathAhead.y > point.y){
                                rotateRight();
                            }
                            else {
                                rotateLeft();
                            }
                        }
                    }
                    return;
                }
                else {
                    if(!Board.isEmpty(ahead)){
                        stay();
                    }

                    else {
                        moveAhead(ahead);
                        path.remove(0);
                    }
                }
            }

        }
        else if(intoIntersection(ahead)){
            if(path.size() == 0){
                Intersection intersectAux = Board.getIntersection(point);
                List<String> possibleWays = intersectAux.getPossibleExits();
                int index = random.nextInt(possibleWays.size());
                String decision = possibleWays.get(index);
                path = intersectAux.calcPathIntersect(point,intersectAux.getDestination(decision), direction);
            }
            Point trafficLightLocal = new Point(point.x,point.y);
            switch(direction) {
                case 0: trafficLightLocal.y--; break;
                case 90: trafficLightLocal.x--; break;
                case 180: trafficLightLocal.y++; break;
                default: trafficLightLocal.x++;
            }
            if(!isGreen(trafficLightLocal)){
                stay();
            }
            else if(!Board.isEmpty(ahead)){
                stay();
            }
            else {
                moveAhead(ahead);
                path.remove(0);
            }
        }
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
        else if(!Board.isEmpty(ahead)){
            stay();
        }

        else {
            moveAhead(ahead);
        }
    }

    public void moveAhead(Point ahead) {
        Board.updateEntityPosition(point,ahead);
        this.totalStepsGiven++;
        this.point = ahead;
    }

    public boolean comparePoints(Point p1, Point p2){
        if(p1.x != p2.x){
            return false;
        }
        return p1.y == p2.y;
    }

}
