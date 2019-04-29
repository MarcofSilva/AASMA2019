package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.AbstractBorder;

/**
 * Environment
 * @author Rui Henriques
 */
public class Board {

	public static int nX = 30, nY = 30;
	private static Block[][] board;
	private static Object[][] objects;
	private static List<Car> cars;
	private static List<TrafficLight> trafficLights;


	private static void buildHorizontalroad(int j){
		for(int k = 0; k<2; k++){
			for(int i = 0; i<nX; i++){
				board[i][j+k] = new RoadHBlock(Color.black,new ComplexBoarder(Color.white, Color.black));
			}
		}

	}
	private static void buildVerticalroad(int i){
		for(int k = 0; k<2; k++){
			for(int j = 0; j<nX; j++){
				if(board[i+k][j] != null){
					board[i+k][j] = new RoadIntersectBlock(Color.black,new ComplexBoarder(Color.black, Color.black));
				}
				else {
					board[i+k][j] = new RoadVBlock(Color.black, new ComplexBoarder(Color.black, Color.white));
				}
			}
		}

	}
	public static void initialize() {
		board = new Block[nX][nY];
		buildHorizontalroad(14);
		buildVerticalroad(14);

		Color buildingColor = Color.gray;
		for(int i=0; i<nX; i++){
			for(int j=0; j<nY; j++)
				if(board[i][j] == null){
					board[i][j] = new BuildingBlock(buildingColor, new ComplexBoarder(buildingColor, buildingColor));
				}
		}

		cars = new ArrayList<>();

		cars.add(new CarNormal(new Point(0,14),0));
		cars.add(new CarAutonomous(new Point(4,14),0));
		cars.add(new CarNormal(new Point(26,15), 180));
		cars.add(new CarAutonomous(new Point(29,15), 180));
		cars.add(new CarNormal(new Point(15,0),270));
		cars.add(new CarAutonomous(new Point(15,4),270));
		cars.add(new CarNormal(new Point(14,26), 90));
		cars.add(new CarAutonomous(new Point(14,29), 90));

		trafficLights = new ArrayList<>();
		trafficLights.add(new TrafficLight(new Point(13,13),5,2,3,Color.green));
		trafficLights.add(new TrafficLight(new Point(13,16),5,2,3,Color.red));
		trafficLights.add(new TrafficLight(new Point(16,13),5,2,3,Color.red));
		trafficLights.add(new TrafficLight(new Point(16,16),5,2,3,Color.green));

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
		return board[point.x][point.y];
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
		Color first;
		Color second;

		public ComplexBoarder(Color horizontal, Color vertical){
			first = horizontal;
			second = vertical;
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

			g.setColor(first);

			g.translate(x, y);

			// top
			g.fillRect(0, 0, width, insets.top);
			// bottom
			g.fillRect(0, height - insets.bottom, width, insets.bottom);

			g.setColor(second);
			// left
			g.fillRect(0, insets.top, insets.left, height - insets.top  - insets.bottom);
			g.fillRect(width - insets.right, insets.top, insets.right, height- insets.top - insets.bottom);
			g.translate(-x, -y);
		}
	}
}
