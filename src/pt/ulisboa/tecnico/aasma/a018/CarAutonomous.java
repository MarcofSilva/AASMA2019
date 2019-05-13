package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CarAutonomous extends Car {
    private Point ahead;
    private IntersectionManager intersectManager;
    private List<Point> path = new ArrayList<>();
    private boolean hasWaitedInGreen = false;

    public CarAutonomous(Point point){
        super(point, Color.BLUE);
        Board.incAutonomous();
    }

    @Override
    void stay(){

    }

    public TrafficLight calcTF(){
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
        return (TrafficLight) Board.getObject(trafficLightLocal);
    }

    public void calcPath(){
        path.clear();
        Intersection intersectAux = Board.getIntersection(point);
        if(intersectAux.gettrafficLights()){
            TrafficLight tf = calcTF();
            for(int i = 0; i < tf.getTicksTilGreen(); i++){
                path.add(new Point(point.x, point.y));
            }
        }
        path.addAll(intersectAux.calcPathIntersect(point, intersectAux.getDestination(decision), direction));
    }
    public void signIntoIntersection(){
        ahead = aheadPosition();
        if(intoIntersection(ahead) && path.size() == 0){
            Intersection intersectAux = Board.getIntersection(point);
            List<String> possibleWays = intersectAux.getPossibleExits();
            int index = random.nextInt(possibleWays.size());
            decision = possibleWays.get(index);
            calcPath();
            intersectManager = intersectAux.getManager();
            myID = intersectManager.signInIntersection(path, this);
        }
        else if(intoIntersection(ahead)) {
            if(Board.getIntersection(point).gettrafficLights() && calcTF().color == Color.YELLOW && hasWaitedInGreen){
                hasWaitedInGreen = false;
                calcPath();
                intersectManager.updatePath(myID, path);
            }
        }
    }

    /**********************
     **** A: decision *****
     **********************/

    public void agentDecision(){
        this.totalTicks++;
        ahead = aheadPosition();
        if(!isRoad(ahead)){ }
        else if(path.size() > 0){
            if(intoIntersection(ahead)){
                Point pathManager = intersectManager.getAction(myID);
                if(comparePoints(pathManager, point) && Board.getIntersection(point).gettrafficLights() && calcTF().color != Color.GREEN){
                    path.remove(0);
                    stay();
                }
                else{
                    if(comparePoints(pathManager, point)){
                        hasWaitedInGreen = true;
                        stay();
                    }
                    else {
                        moveAhead(ahead);
                        path.remove(0);
                    }
                }
            }
            else{
                Point pathManager = intersectManager.getAction(myID);
                if(comparePoints(pathManager, point) && path.contains(ahead)){
                    stay();
                    return;
                }
                if(!comparePoints(ahead, pathManager)){
                    path.remove(0);
                    Point pointToRotate = path.get(0);
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
                else{
                    moveAhead(ahead);
                    path.remove(0);
                }
            }

        }
        else if(inCurve()){
            switch(((RoadCurveBlock) Board.getBlock(point)).getAction()){
                case "left":
                    if(!(Board.getBlock(ahead) instanceof RoadCurveBlock)){
                        rotateLeft();
                    }
                    else{
                        if(!Board.isEmpty(ahead));
                        else{
                            moveAhead(ahead);
                        }
                    }
                    break;
                case "right":
                    if((Board.getBlock(ahead) instanceof RoadCurveBlock)){
                        rotateRight();
                    }
                    else{
                        if(!Board.isEmpty(ahead));
                        else{
                            moveAhead(ahead);
                        }
                    }
                    break;
                case "continue":
                    if(!Board.isEmpty(ahead));
                    else{
                        moveAhead(ahead);
                    }
                    break;
            }
        }
        else if(!Board.isEmpty(ahead)){
            stay();
        }
        else{
            moveAhead(ahead);
        }
    }

    public void moveAhead(Point ahead){
        Board.updateEntityPosition(point, ahead);
        this.totalStepsGiven++;
        this.totaldistance++;
        this.point = ahead;
    }

    public boolean comparePoints(Point p1, Point p2){
        if(p1.x != p2.x){
            return false;
        }
        return p1.y == p2.y;
    }
}
