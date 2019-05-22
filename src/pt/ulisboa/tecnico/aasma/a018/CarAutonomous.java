package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CarAutonomous extends Car {
    private Point ahead;
    private ArrayList<Point> path = new ArrayList<>();
    private String decision = "";
    private Point destination = null;

    public CarAutonomous(Point point){
        super(point, Color.CYAN);
        Board.incAutonomous();
    }

    @Override
    void stay(){

    }

    protected Point threepositionsAhead(){
        Point newpoint = new Point(point.x, point.y);
        switch(direction){
            case 0:
                newpoint.x = newpoint.x + 3;
                break;
            case 90:
                newpoint.y = newpoint.y - 3;
                break;
            case 180:
                newpoint.x = newpoint.x - 3;
                break;
            default:
                newpoint.y = newpoint.y + 3;
        }
        return newpoint;
    }

    public Point calcSide(){
        Point newPoint = new Point(point.x, point.y);
        switch(direction){
            case 0:
                newPoint.y++;
                break;
            case 90:
                newPoint.x++;
                break;
            case 180:
                newPoint.y--;
                break;
            default:
                newPoint.x--;
        }
        return newPoint;
    }

    public Point getConflictingPosition(){
        Point newpoint = new Point(point.x, point.y);
        switch(direction){
            case 0:
                newpoint.x++;
                newpoint.y++;
                break;
            case 90:
                newpoint.x++;
                newpoint.y--;
                break;
            case 180:
                newpoint.x--;
                newpoint.y--;
                break;
            default:
                newpoint.x--;
                newpoint.y++;
        }
        return newpoint;
    }

    public ArrayList<Point> calcPath(){
        path.clear();
        Intersection intersectAux = Board.getIntersection(point);
        List<String> possibleWays = intersectAux.getPossibleExits();
        int index = random.nextInt(possibleWays.size());
        decision = possibleWays.get(index);
        return intersectAux.calcPathIntersect(point, intersectAux.getDestination(decision), direction);
    }

    public void moveAheadConditionally(){
        if(Board.isEmpty(ahead)){
            moveAhead(ahead);
            if(path.size() > 0){
                path.remove(0);
            }
        }
        else{
            stay();
        }
    }

    public void agentDecision(){
        ahead = aheadPosition();
        this.totalTicks++;

        if(intoIntersection(ahead)){
            if(path.size() == 0){
                path = calcPath();
                destination = path.get(path.size() - 1);
            }
        }
        if(path.size() > 0){
            if(intoIntersection(ahead)){
                if(isGreen(point)){
                    if(decision.equals("R")){
                        moveAheadConditionally();
                    }
                    else if(Board.communication){
                        Point twoahead = twoaheadPosition();
                        if(Board.isEmpty(twoahead)){
                            moveAheadConditionally();
                        }
                        else{
                            Car car = (Car) Board.getObject(twoahead);
                            if(car instanceof CarNormal){
                                stay();
                            }
                            else{
                                CarAutonomous carAuto = (CarAutonomous) car;
                                //if he wants to get out of the intersection i can go(he can just leave)
                                if(comparePoints(carAuto.getDestination(), threepositionsAhead())){
                                    moveAheadConditionally();
                                }
                                else{
                                    Car conflictCar = (Car) Board.getObject(getConflictingPosition());
                                    if(conflictCar == null){
                                        moveAheadConditionally();
                                    }
                                    else if(conflictCar instanceof CarNormal){
                                        stay();
                                    }
                                    else{
                                        CarAutonomous conflictAuto = (CarAutonomous) conflictCar;
                                        if(comparePoints(calcSide(), conflictAuto.getDestination())){
                                            moveAheadConditionally();
                                        }
                                        else{
                                            stay();
                                        }
                                    }
                                }
                            }
                        }

                    }
                    else{
                        if(Board.isEmpty(ahead)){
                            if(decision.equals("R")){
                                moveAheadConditionally();
                            }
                            else if(Board.isEmpty(twoaheadPosition())){
                                moveAheadConditionally();
                            }
                            else{
                                stay();
                            }
                        }
                        else{
                            stay();
                        }
                    }
                }
                else{
                    stay();
                }
            }
            else{
                if(comparePoints(path.get(0), ahead)){
                    moveAheadConditionally();
                }
                else{
                    path.remove(0);
                    Point pointToRotate = path.get(0);
                    rotateAccordingly(pointToRotate);
                }
            }
        }
        else if(inCurve()){
            switch(((RoadCurveBlock) Board.getBlock(point)).getAction()){
                case "left":
                    if(!(Board.getBlock(ahead) instanceof RoadCurveBlock)) rotateLeft();
                    else{
                        moveAheadConditionally();
                    }
                    break;
                case "right":
                    if((Board.getBlock(ahead) instanceof RoadCurveBlock)) rotateRight();
                    else{
                        moveAheadConditionally();
                    }
                    break;
                case "continue":
                    moveAheadConditionally();
                    break;
            }
        }
        else if(!isRoad(ahead)){
            stay();
        }
        else{
            moveAheadConditionally();
        }
    }

    public void moveAhead(Point ahead){
        Board.updateEntityPosition(point, ahead);
        this.totalActionsTaken++;
        this.totaldistance++;
        this.point = ahead;
    }

    public Point getDestination(){
        return destination;
    }

    public boolean comparePoints(Point p1, Point p2){
        if(p1.x != p2.x){
            return false;
        }
        return p1.y == p2.y;
    }
}