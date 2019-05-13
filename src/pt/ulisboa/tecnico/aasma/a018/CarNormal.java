package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CarNormal extends Car {
    private Point ahead;
    private IntersectionManager intersectManager;
    private List<Point> path = new ArrayList<>();
    private boolean hasWaitedInGreen = false;
    private boolean delay = false;

    private List<Point> pathLog = new ArrayList<>();
    private List<Point> pathManagerLog = new ArrayList<>();
    private String pathOpLog = "";

    public CarNormal(Point point){
        super(point, Color.MAGENTA);
        Board.incNormal();
    }

    @Override
    void stay(){
        delay = true;
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
            int aux = tf.getTicksTilGreen();
            for(int i = 0; i < aux; i++){
                path.add(new Point(point.x, point.y));
            }
            if(aux > 0 ){
                path.add(new Point(point.x, point.y));
            }
        }
        path.addAll(intersectAux.calcPathIntersect(point, intersectAux.getDestination(decision), direction));
        pathLog.clear();
        pathLog.addAll(path);
        pathOpLog = "";
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

    public void agentDecision() {
        this.totalTicks++;
        ahead = aheadPosition();
        if(!isRoad(ahead)){ }
        else if(path.size() > 0){
            if(intoIntersection(ahead)){
                Point pathManager = intersectManager.getAction(myID);
                pathManagerLog.add(pathManager);
                if(comparePoints(pathManager, point) && Board.getIntersection(point).gettrafficLights() &&  calcTF().color != Color.GREEN){
                    path.remove(0);
                    pathOpLog += "RemTL; ";
                    stay();
                }
                else if(comparePoints(pathManager, point) && Board.getIntersection(point).gettrafficLights() &&  calcTF().color == Color.GREEN && delay){
                    delay = false;
                    path.remove(0);
                    pathOpLog += "RemDel; ";

                }
                else{
                    if(comparePoints(pathManager, point)){
                        hasWaitedInGreen = true;
                        stay();
                    }
                    else {
                        moveAhead(ahead);
                        pathOpLog += "RemWalkedInto; ";
                        path.remove(0);
                    }
                }
            }
            else{
                Point pathManager = intersectManager.getAction(myID);
                pathManagerLog.add(pathManager);
                if(comparePoints(pathManager, point) && path.contains(ahead)){
                    stay();
                    return;
                }
                if(!comparePoints(ahead, pathManager)){
                    path.remove(0);
                    pathOpLog += "RemRotated; ";

                    if(path.size() == 0){
                        System.out.print("");
                    }

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
                    pathOpLog += "RemWalkedInside; ";
                }
            }
        }
        else if(inCurve()) {
            switch (((RoadCurveBlock) Board.getBlock(point)).getAction()) {
                case "left":
                    if(!(Board.getBlock(ahead) instanceof RoadCurveBlock)){
                        rotateLeft();
                    }
                    else {
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
                    else {
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
        if(delay){
            delay = false;
            if(path.size() == 0){
                return;
            }
        }
        Board.updateEntityPosition(point, ahead);
        this.totalStepsGiven++;
        this.point = ahead;
        this.totaldistance++;
    }

    public boolean comparePoints(Point p1, Point p2){
        if(p1.x != p2.x){
            return false;
        }
        return p1.y == p2.y;
    }
}
