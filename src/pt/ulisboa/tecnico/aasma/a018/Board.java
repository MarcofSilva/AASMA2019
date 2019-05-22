package pt.ulisboa.tecnico.aasma.a018;


import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.border.AbstractBorder;

public class Board {

	private static final Color BLACK = Color.black;
	private static final Color WHITE = Color.white;
	private static final Color GRAY = Color.gray;

	private static int autonomousCount;
	private static int normalCount;

	private static List<Point> spawnPoints = new ArrayList<>();
	private static List<Point> deSpawnPoints = new ArrayList<>();
	private static List<Car> carsToRemove = new ArrayList<>();
	//private static List<IntersectionManager> intersectManagers = new ArrayList<>();

	public static boolean TRAFFICLIGHTS = true;
	public static int nX = 30, nY = 30, nrCars, totalCarCounter,totalACarCounter, totalNcar, percentCars = 50;
	public static double medianAll = 0.0, medianNormal = 0.0, medianAuto = 0.0;
	public static double medianAll2 = 0.0, medianNormal2 = 0.0, medianAuto2 = 0.0;
	private static Block[][] board;
	private static Object[][] objects;
	private static List<Car> cars;
	private static List<TrafficLight> trafficLights;
	private static Intersection[][] intersections;
	public static boolean communication = false;

	public static void incAutonomous(){
		autonomousCount++;
	}
	public static void incNormal(){
		normalCount++;
	}
	public static void setCommunication(){communication = !communication;}
	public static void notifyPercent(int percent){
		percentCars = percent;
	}
	public static void notifyNumberCars(int numberCars){
		nrCars = numberCars;
	}

	private static void checkNumberOfCars(){
		int missingCars = nrCars - cars.size();
		if(missingCars > 0){
			Collections.shuffle(spawnPoints);
			//TODO
			for(int i = 0; i < missingCars && i < 8; i++){
				if(autonomousCount < nrCars*(percentCars/100.0)){
					spawnCar( new CarAutonomous(spawnPoints.get(i)));
				}
				else {
					spawnCar( new CarNormal(spawnPoints.get(i)));
				}
			}
		}
	}
	private static void buildHorizontalroad(int lane, int startPoint, int lenght){
		for(int k = 0; k<2; k++){
			for(int i = startPoint; i<startPoint + lenght; i++){
				board[i][lane+k] = new RoadHBlock();
			}
		}
	}

	private static void buildVerticalroad(int lane, int startPoint, int lenght){
		for(int k = 0; k<2; k++){
			for(int j = startPoint; j<startPoint + lenght; j++){
				if(board[lane+k][j] != null){
					board[lane+k][j] = new RoadIntersectBlock(BLACK, new ComplexBoarder(BLACK, BLACK, BLACK, BLACK));
				}
				else {
					board[lane+k][j] = new RoadVBlock();
				}
			}
		}
	}

	//0 for right and 1 for left
	private static void buildCurve(String curveType, Point cornerPoint){
		String action1 = "left";
		String action2 = "right";
		String action3 = "continue";

		switch (curveType) {
			case "UL":
				board[cornerPoint.x][cornerPoint.y] = new RoadCurveBlock(BLACK, new ComplexBoarder(WHITE, BLACK, WHITE, BLACK), action1);
				board[cornerPoint.x+1][cornerPoint.y-1] = new RoadCurveBlock(BLACK, new ComplexBoarder(WHITE, BLACK, WHITE, BLACK), action2);
				board[cornerPoint.x][cornerPoint.y-1] = new RoadCurveBlock(BLACK, new ComplexBoarder(BLACK, BLACK, WHITE, WHITE), action3);
				board[cornerPoint.x+1][cornerPoint.y] = new RoadCurveBlock(BLACK, new ComplexBoarder(WHITE, WHITE, BLACK, BLACK), action3);
				break;
			case "UR":
				board[cornerPoint.x][cornerPoint.y] = new RoadCurveBlock(BLACK, new ComplexBoarder(WHITE, BLACK, BLACK, WHITE), action1);
				board[cornerPoint.x-1][cornerPoint.y-1] = new RoadCurveBlock(BLACK, new ComplexBoarder(WHITE, BLACK, BLACK, WHITE), action2);
				board[cornerPoint.x-1][cornerPoint.y] = new RoadCurveBlock(BLACK, new ComplexBoarder(WHITE, WHITE, BLACK, BLACK), action3);
				board[cornerPoint.x][cornerPoint.y-1] = new RoadCurveBlock(BLACK, new ComplexBoarder(BLACK, BLACK, WHITE, WHITE), action3);
				break;
			case "DR":
				board[cornerPoint.x][cornerPoint.y] = new RoadCurveBlock(BLACK, new ComplexBoarder(BLACK, WHITE, BLACK, WHITE), action1);
				board[cornerPoint.x-1][cornerPoint.y+1] = new RoadCurveBlock(BLACK, new ComplexBoarder(BLACK, WHITE, BLACK, WHITE), action2);
				board[cornerPoint.x-1][cornerPoint.y] = new RoadCurveBlock(BLACK, new ComplexBoarder(WHITE, WHITE, BLACK, BLACK), action3);
				board[cornerPoint.x][cornerPoint.y+1] = new RoadCurveBlock(BLACK, new ComplexBoarder(BLACK, BLACK, WHITE, WHITE), action3);
				break;
			case "DL":
				board[cornerPoint.x][cornerPoint.y] = new RoadCurveBlock(BLACK, new ComplexBoarder(BLACK, WHITE, WHITE, BLACK), action1);
				board[cornerPoint.x+1][cornerPoint.y+1] = new RoadCurveBlock(BLACK, new ComplexBoarder(BLACK, WHITE, WHITE, BLACK), action2);
				board[cornerPoint.x][cornerPoint.y+1] = new RoadCurveBlock(BLACK, new ComplexBoarder(BLACK, BLACK, WHITE, WHITE), action3);
				board[cornerPoint.x+1][cornerPoint.y] = new RoadCurveBlock(BLACK, new ComplexBoarder(WHITE, WHITE, BLACK, BLACK), action3);
				break;
		}
	}

	private static void createTrafficLights(Point leftDown) {
		createTrafficLight(new TrafficLight(leftDown,17,2,5,Color.green));
		createTrafficLight(new TrafficLight(new Point(leftDown.x,leftDown.y + 3),17,2,5,Color.red));
		createTrafficLight(new TrafficLight(new Point(leftDown.x + 3,leftDown.y),17,2,5,Color.red));
		createTrafficLight(new TrafficLight(new Point(leftDown.x + 3,leftDown.y + 3),17,2,5,Color.green));
	}

	public static void createTrafficLight(TrafficLight tl){
		trafficLights.add(tl);
		objects[tl.point.x][tl.point.y] = tl;
	}

	private static void initializeMap(){
		//Change here the map's layout
		//Horizontal lanes
		buildHorizontalroad(4, 0, 23);
		buildHorizontalroad(8, 17, 13);
		buildHorizontalroad(15, 7, 8);
		buildHorizontalroad(23, 0, 30);

		//Vertical lanes
		buildVerticalroad(7, 0, 30);
		buildVerticalroad(15, 10, 5);
		buildVerticalroad(23, 0, 4);
		buildVerticalroad(23, 8, 22);

		//Curves
		buildCurve("UR", new Point(16, 16));
		buildCurve("DL", new Point(15, 8));
		buildCurve("UR", new Point(24, 5));

		//Martelada para dar cover de uma intersecao de tres saidas
		board[23][8] = new RoadIntersectBlock(BLACK, new ComplexBoarder(BLACK, WHITE, BLACK, BLACK));
		board[23][8] = new RoadIntersectBlock(BLACK, new ComplexBoarder(BLACK, WHITE, BLACK, BLACK));
		board[24][8] = new RoadIntersectBlock(BLACK, new ComplexBoarder(BLACK, WHITE, BLACK, BLACK));
		board[7][15] = new RoadIntersectBlock(BLACK, new ComplexBoarder(BLACK, BLACK, WHITE, BLACK));
		board[7][16] = new RoadIntersectBlock(BLACK, new ComplexBoarder(BLACK, BLACK, WHITE, BLACK));

		for(int i=0; i<nX; i++){
			for(int j=0; j<nY; j++)
				if(board[i][j] == null){
					board[i][j] = new BuildingBlock();
				}
		}
	}
	private static void initiateObjects(){
		createTrafficLights(new Point(22, 22));
		createTrafficLights(new Point(6, 22));
		createTrafficLights(new Point(6, 3));

	}

	private static void initializeLists(){
		spawnPoints = new ArrayList<>();
		spawnPoints.add(new Point(0, 23));
		spawnPoints.add(new Point( 7, 29));
		spawnPoints.add(new Point(0,4));
		spawnPoints.add(new Point( 23, 29));
		spawnPoints.add(new Point( 29, 24));
		spawnPoints.add(new Point( 29, 9));
		spawnPoints.add(new Point( 24, 0));
		spawnPoints.add(new Point( 8, 0));
/*
		//idk the utility of these but they are here anyway
		deSpawnPoints.add(new Point( 0, 5));
		deSpawnPoints.add(new Point( 0, 24));
		deSpawnPoints.add(new Point( 8, 29));
		deSpawnPoints.add(new Point( 24, 29));
		deSpawnPoints.add(new Point( 29, 23));
		deSpawnPoints.add(new Point( 29, 8));
		deSpawnPoints.add(new Point( 23, 0));
		deSpawnPoints.add(new Point( 7, 0));*/
	}

	private static void initializeIntersections(){
		//3 way
		intersections[8][14] = new Intersection(new Point(8,14),270, Arrays.asList(new String[]{"F", "R"}),false);
		intersections[7][17] = new Intersection(new Point(7,17),90, Arrays.asList(new String[]{"F", "L"}),false);
		intersections[9][16] = new Intersection(new Point(9,16),180, Arrays.asList(new String[]{"R", "L"}), false);

		// 3 way
		intersections[22][8] = new Intersection(new Point(22,8),0, Arrays.asList(new String[]{"F", "L"}), false);
		intersections[23][10] = new Intersection(new Point(23,10),90, Arrays.asList(new String[]{"R", "L"}), false);
		intersections[25][9] = new Intersection(new Point(25,9),180, Arrays.asList(new String[]{"F", "R"}),false);

		//4 way
		intersections[6][4] = new Intersection(new Point(6,4),0, Arrays.asList(new String[]{"F", "R", "L"}),TRAFFICLIGHTS);
		intersections[7][6] = new Intersection(new Point(7,6),90, Arrays.asList(new String[]{"F", "R", "L"}),TRAFFICLIGHTS);
		intersections[8][3] = new Intersection(new Point(8,3),270, Arrays.asList(new String[]{"F", "R", "L"}),TRAFFICLIGHTS);
		intersections[9][5] = new Intersection(new Point(9,5),180, Arrays.asList(new String[]{"F", "R", "L"}),TRAFFICLIGHTS);

		//4 way
		intersections[6][23] = new Intersection(new Point(6,23),0, Arrays.asList(new String[]{"F", "R", "L"}), TRAFFICLIGHTS);
		intersections[7][25] = new Intersection(new Point(7,25),90, Arrays.asList(new String[]{"F", "R", "L"}), TRAFFICLIGHTS);
		intersections[8][22] = new Intersection(new Point(8,22),270, Arrays.asList(new String[]{"F", "R", "L"}), TRAFFICLIGHTS);
		intersections[9][24] = new Intersection(new Point(9,24),180, Arrays.asList(new String[]{"F", "R", "L"}), TRAFFICLIGHTS);

		//4 way
		intersections[22][23] = new Intersection(new Point(22,23),0, Arrays.asList(new String[]{"F", "R", "L"}), TRAFFICLIGHTS);
		intersections[23][25] = new Intersection(new Point(23,25),90, Arrays.asList(new String[]{"F", "R", "L"}), TRAFFICLIGHTS);
		intersections[24][22] = new Intersection(new Point(24,22),270, Arrays.asList(new String[]{"F", "R", "L"}), TRAFFICLIGHTS);
		intersections[25][24] = new Intersection(new Point(25,24),180, Arrays.asList(new String[]{"F", "R", "L"}), TRAFFICLIGHTS);
	}

	public static void initialize() {
		initializeLists();
		autonomousCount = 0;
		normalCount = 0;
		medianAll = 0.0;
		medianNormal = 0.0;
		medianAuto = 0.0;
		medianAll2 = 0.0;
		medianNormal2 = 0.0;
		medianAuto2 = 0.0;
		totalCarCounter = 0;
		totalACarCounter = 0;
		totalNcar = 0;
		board = new Block[nX][nY];
		initializeMap();
		intersections = new Intersection[nX][nY];
		initializeIntersections();
		objects = new Object[nX][nY];
		cars = new ArrayList<>();
		trafficLights = new ArrayList<>();
		if(TRAFFICLIGHTS){
			initiateObjects();
		}
	}

	/*public static void resetManagers(){
		for(IntersectionManager im : intersectManagers){
			im.reset();
		}
	}*/
	
	/****************************
	 ***** B: BOARD METHODS *****
	 ****************************/

	public static Intersection getIntersection(Point point){
		return intersections[point.x][point.y];
	}
	public static Object getObject(Point point) {
		return objects[point.x][point.y];
	}
	public static Block getBlock(Point point) {
		if(point.x >= 0 && point.y >= 0 && point.x < board.length && point.y < board[0].length)
			return board[point.x][point.y];
		return null;
	}

	public static boolean isEmpty(Point point){
		if(objects[point.x][point.y] == null){
			return  true;
		}
		return false;
	}

	public static void updateEntityPosition(Point point, Point newpoint) {
		objects[newpoint.x][newpoint.y] = objects[point.x][point.y];
		objects[point.x][point.y] = null;

	}	
	public static void removeEntity(Point point) {
		objects[point.x][point.y] = null;
	}
	public static void insertEntity(Car car, Point point) {
		objects[point.x][point.y] = car;
	}

	/***********************************
	 ***** C: ELICIT AGENT ACTIONS *****
	 ***********************************/
	
	private static RunThread runThread;
	private static GUI GUI;

	public static class RunThread extends Thread {
		
		int time;
		
		public RunThread(int time){
			this.time = time*1;
		}
		
	    public void run() {
	    	while(true){
	    		Board.step();
				try {
					sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	}
	    }
	}
	
	public static void run(int time) {
		Board.runThread = new RunThread(time);
		Board.runThread.start();
	}

	public static void reset() {
		removeObjects();
		initialize();
		//resetManagers();
		GUI.displayBoard();
		displayObjects();	
		GUI.update();
	}

	public static void step() {
		GUI.displayBoard();
		removeObjects();
		for(TrafficLight tl : trafficLights){
			tl.tick();
		}

		for(Car a : cars){
			a.agentDecision();
		}/*
		for(Car a : cars){
			a.signIntoIntersection();
		}
		for(IntersectionManager im : intersectManagers){
			im.resolve();
		}
		for(Car a : cars){
			a.agentDecision();
		}*/
		removeCars();
		checkNumberOfCars();
		displayObjects();
		GUI.update();
	}

	public static void stop() {
		runThread.interrupt();
		runThread.stop();
	}

	public static void displayObjects(){
		for(TrafficLight tf : trafficLights) GUI.displayObject(tf);
		for(Car car : cars) GUI.displayObject(car);
	}
	
	public static void removeObjects(){
		for(TrafficLight tf : trafficLights) GUI.removeObject(tf);
		for(Car car : cars) GUI.removeObject(car);
	}

	private static void removeCars(){
		for(Car car : carsToRemove){
			objects[car.point.x][car.point.y] = null;
			cars.remove(car);
		}
		carsToRemove.clear();
	}
	public static void removeCar(Car car){
		float d1 = car.getStepsStopped();
		float d2 = car.getStepsStopped2();
        medianAll = (medianAll*totalCarCounter + d1)/(totalCarCounter+1);
        medianAll2 = (medianAll2*totalCarCounter + d2)/(totalCarCounter+1);
	    totalCarCounter++;
		carsToRemove.add(car);
		if(car instanceof CarAutonomous){
			autonomousCount--;
			medianAuto = (medianAuto*totalACarCounter + d1)/(totalACarCounter+1);
			medianAuto2 = (medianAuto2*totalACarCounter + d2)/(totalACarCounter+1);
			totalACarCounter++;

		}
		else {
			normalCount--;
			medianNormal = (medianNormal*totalNcar + d1)/(totalNcar+1);
			medianNormal2 = (medianNormal2*totalNcar + d2)/(totalNcar+1);
			totalNcar++;
		}
	}
	public static void spawnCar(Car car){
		cars.add(car);
		objects[car.point.x][car.point.y] = car;
	}

	public static void associateGUI(GUI graphicalInterface) {
		GUI = graphicalInterface;
	}

	public static class ComplexBoarder extends AbstractBorder {
		Color _top, _bottom, _left, _right;

		public ComplexBoarder(Color top, Color bottom, Color left, Color right){
			_top = top;
			_bottom = bottom;
			_left = left;
			_right = right;
		}
		public boolean isBorderOpaque() {
			return true;
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(3, 3, 3, 3);
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width,
								int height) {
			Insets insets = getBorderInsets(c);
			g.translate(x, y);

			// top
			g.setColor(_top);
			g.fillRect(0, 0, width, insets.top);

			// bottom
			g.setColor(_bottom);
			g.fillRect(0, height - insets.bottom, width, insets.bottom);

			// left
			g.setColor(_left);
			g.fillRect(0, insets.top, insets.left, height - insets.top  - insets.bottom);

			// right
			g.setColor(_right);
			g.fillRect(width - insets.right, insets.top, insets.right, height- insets.top - insets.bottom);

			g.translate(-x, -y);
		}
	}
}
