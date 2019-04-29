package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.AbstractBorder;

public class Board {

	private static final Color BLACK = Color.black;
	private static final Color WHITE = Color.white;
	private static final Color GRAY = Color.gray;


	public static int nX = 30, nY = 30;
	private static Block[][] board;
	private static Object[][] objects;
	private static List<Car> cars;
	private static List<TrafficLight> trafficLights;


	private static void buildHorizontalroad(int lane, int startPoint, int lenght){
		for(int k = 0; k<2; k++){
			for(int i = startPoint; i<startPoint + lenght; i++){
				board[i][lane+k] = new RoadHBlock(BLACK, new ComplexBoarder(WHITE, WHITE, BLACK, BLACK));
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
					board[lane+k][j] = new RoadVBlock(BLACK, new ComplexBoarder(BLACK, BLACK, WHITE, WHITE));
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
		trafficLights.add(new TrafficLight(leftDown,5,2,3,Color.green));
		trafficLights.add(new TrafficLight(new Point(leftDown.x,leftDown.y + 3),5,2,3,Color.red));
		trafficLights.add(new TrafficLight(new Point(leftDown.x + 3,leftDown.y),5,2,3,Color.red));
		trafficLights.add(new TrafficLight(new Point(leftDown.x + 3,leftDown.y + 3),5,2,3,Color.green));
	}

	public static void initialize() {
		board = new Block[nX][nY];
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
		board[24][8] = new RoadIntersectBlock(BLACK, new ComplexBoarder(BLACK, WHITE, BLACK, BLACK));
		board[7][15] = new RoadIntersectBlock(BLACK, new ComplexBoarder(BLACK, BLACK, WHITE, BLACK));
		board[7][16] = new RoadIntersectBlock(BLACK, new ComplexBoarder(BLACK, BLACK, WHITE, BLACK));

		Color buildingColor = Color.gray;
		for(int i=0; i<nX; i++){
			for(int j=0; j<nY; j++)
				if(board[i][j] == null){
					board[i][j] = new BuildingBlock(buildingColor, new ComplexBoarder(buildingColor, buildingColor, buildingColor, buildingColor));
				}
		}

		cars = new ArrayList<>();

		cars.add(new CarNormal(new Point(0,4),0));
		cars.add(new CarAutonomous(new Point(4,4),0));
		cars.add(new CarNormal(new Point(26,9), 180));
		cars.add(new CarAutonomous(new Point(29,9), 180));
		cars.add(new CarNormal(new Point(8,0),270));
		cars.add(new CarAutonomous(new Point(8,4),270));
		cars.add(new CarNormal(new Point(7,26), 90));
		cars.add(new CarAutonomous(new Point(7,29), 90));

		trafficLights = new ArrayList<>();
		createTrafficLights(new Point(22, 22));

		createTrafficLights(new Point(6, 22));
		createTrafficLights(new Point(6, 3));

		objects = new Object[nX][nY];
		for(Car car : cars) {
			objects[car.point.x][car.point.y]= car;
		}
		for(TrafficLight traficlight : trafficLights) {
			objects[traficlight.point.x][traficlight.point.y]= traficlight;
		}

	}
	
	/****************************
	 ***** B: BOARD METHODS *****
	 ****************************/
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
			this.time = time*100;
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
		}
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
