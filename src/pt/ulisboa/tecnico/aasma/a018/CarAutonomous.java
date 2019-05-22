package pt.ulisboa.tecnico.aasma.a018;

import org.omg.PortableServer.POA;

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

    protected Point threepositionsAhead() {
        Point newpoint = new Point(point.x,point.y);
        switch(direction) {
            case 0: newpoint.x = newpoint.x + 3; break;
            case 90: newpoint.y = newpoint.y - 3; break;
            case 180: newpoint.x = newpoint.x - 3; break;
            default: newpoint.y = newpoint.y + 3;
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

    public Point getConflictingPosition2(){
        Point newpoint = new Point(point.x, point.y);
        switch(direction){
            case 0:
                newpoint.x = newpoint.x+2;
                newpoint.y++;
                break;
            case 90:
                newpoint.x++;
                newpoint.y = newpoint.y-2;
                break;
            case 180:
                newpoint.x = newpoint.x-2;
                newpoint.y--;
                break;
            default:
                newpoint.x--;
                newpoint.y = newpoint.y+2;
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

/*    public void signIntoIntersection(){
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
*/
    /**********************
     **** A: decision *****
     **********************/

    public void moveAheadConditionally(){
        if(Board.isEmpty(ahead)){
            moveAhead(ahead);
            if(path.size() > 0){
                path.remove(0);
            }
        }
        else {
            stay();
        }
    }
    public void agentDecision() {
        ahead = aheadPosition();
        this.totalTicks++;

        if(intoIntersection(ahead)){
            if(path.size() == 0){
                path = calcPath();
                destination = path.get(path.size()-1);
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
                        else {
                            Car car = (Car) Board.getObject(twoahead);
                            if(car instanceof CarNormal){
                                stay();
                            }
                            else {
                                CarAutonomous carAuto = (CarAutonomous) car;
                                //with traffic lights, deadlocks are way easier to prevent, so we separate it
                                if(Board.trafficLightState){
                                    //if he turned right we dont see it, so there is only front and left
                                    if(carAuto.getDecision().equals("F")){
                                        moveAheadConditionally();
                                    }
                                    else {
                                        Car conflictCar = (Car) Board.getObject(getConflictingPosition());
                                        if(conflictCar == null){
                                            moveAheadConditionally();
                                        }
                                        else if(conflictCar instanceof CarNormal){
                                            stay();
                                        }
                                        else {
                                            CarAutonomous conflictAuto = (CarAutonomous) conflictCar;
                                            if(conflictAuto.getDecision().equals("L")){
                                                stay();
                                            }
                                            else {
                                                 moveAheadConditionally();
                                            }
                                        }
                                    }
                                }
                                else {
                                    //if he wants to get out of the intersection i can go(he can just leave)
                                    if(comparePoints(carAuto.getDestination(), threepositionsAhead())){
                                        moveAheadConditionally();
                                    }
                                    else {
                                        Car conflictCar = (Car) Board.getObject(getConflictingPosition());
                                        if(conflictCar == null){
                                            moveAheadConditionally();
                                        }
                                        if(conflictCar instanceof CarNormal){
                                            stay();
                                        }
                                        else {
                                            CarAutonomous conflictAuto = (CarAutonomous) conflictCar;
                                            if(comparePoints(calcSide(), conflictAuto.getDestination())){
                                                moveAheadConditionally();
                                            }
                                            else {
                                                stay();
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                    else {
                        if(Board.isEmpty(ahead)){
                            if(decision.equals("R")){
                                moveAheadConditionally();
                            }
                            else if(Board.isEmpty(twoaheadPosition())){
                                moveAheadConditionally();
                            }
                            else {
                                stay();
                            }
                        }
                        else {
                            stay();
                        }
                    }
                }
                else {
                    stay();
                }
            }
            else {
                if(comparePoints(path.get(0), ahead)){
                    moveAheadConditionally();
                }
                else {
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
            }
        }
        else if(inCurve()) {
        switch (((RoadCurveBlock) Board.getBlock(point)).getAction()) {
                case "left":
                    if(!(Board.getBlock(ahead) instanceof RoadCurveBlock))
                        rotateLeft();
                    else {
                        moveAheadConditionally();
                    }
                    break;
                case "right":
                    if((Board.getBlock(ahead) instanceof RoadCurveBlock))
                        rotateRight();
                    else {
                        moveAheadConditionally();
                    }
                    break;
                case "continue":
                    moveAheadConditionally();
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

    public String getDecision(){
        return decision;
    }
}

/*

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
*/

