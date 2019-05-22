package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;


/**
 * Graphical interface
 * @author Rui Henriques
 */
public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	static JTextField speed;
	static JPanel boardPanel;
	static JButton run, reset, step, communication;
	static JSlider percentCars;
	static JTextField nrCars;
	private int nX, nY;
	static JTextArea allmediantext, allmedian;
	static JTextArea automediantext, automedian;
	static JTextArea normmediantext, normmedian;
	static JTextArea introductionText, communcationText;

	static final int CARS_MIN = 0;
	static final int CARS_MAX = 100;
	static final int CARS_INIT = 50;

	public class Cell extends JPanel {

		private static final long serialVersionUID = 1L;
		
		public List<Object> objects = new ArrayList<>();
		
        @Override
        protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			for(Object object : objects) {
				g.setColor(object.getColor());
				if(object instanceof TrafficLight) {
					g.fillPolygon(new int[]{5, 26, 26, 5}, new int[]{5, 5, 26, 26}, 4); break;
				}
				else {
					switch(((Car) object).direction) {
						case 0: g.fillPolygon(new int[]{5, 16, 26, 26, 16, 5}, new int[]{5, 5, 9, 21, 26 ,26}, 6); break;
						case 90: g.fillPolygon(new int[]{5, 26, 26, 21, 9, 5}, new int[]{5, 5, 16, 26 ,26 ,16}, 6); break;
						case 180: g.fillPolygon(new int[]{5, 15, 26, 26, 15, 5}, new int[]{9, 5, 5, 26, 26 ,21}, 6); break;
						default : g.fillPolygon(new int[]{5, 9, 21, 26, 26, 5}, new int[]{15, 5, 5, 15, 26 ,26}, 6); break;
					}
				}
			}
        }
	}

	public GUI() {
		setTitle("AASMA Project");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		//size of the window to show the board
		setSize(1200, 1000);
		//button panel on top
		add(createButtonPanel());
		add(metricsPanel());

		Board.initialize();
		Board.associateGUI(this);
		
		boardPanel = new JPanel();
		//size of the board itself
		boardPanel.setSize(new Dimension(945,875));
		//left corner of the board
		boardPanel.setLocation(new Point(10,50));
		
		nX = Board.nX;
		nY = Board.nY;
		boardPanel.setLayout(new GridLayout(nX,nY));
		for(int i=0; i<nX; i++){
			for(int j=0; j<nY; j++){
				boardPanel.add(new Cell());
			}
		}

		displayBoard();
		Board.displayObjects();
		update();
		add(boardPanel);
	}

	public void displayBoard() {
		for(int i=0; i<nX; i++){
			for(int j=0; j<nY; j++){
				int row=nY-j-1, col=i;
				Block block = Board.getBlock(new Point(i,j));
				JPanel p = ((JPanel)boardPanel.getComponent(row*nX+col));
				p.setBackground(block.color);
				p.setBorder(block.getBorder());
			}
		}
	}
	
	public void removeObject(Object object) {
		int row=nY-object.point.y-1, col=object.point.x;
		Cell p = (Cell)boardPanel.getComponent(row*nX+col);
		p.setBorder(Board.getBlock(object.point).getBorder());
		p.objects.remove(object);

	}
	
	public void displayObject(Object object) {
		int row=nY-object.point.y-1, col=object.point.x;
		Cell p = (Cell)boardPanel.getComponent(row*nX+col);
		p.setBorder(Board.getBlock(object.point).getBorder());
		p.objects.add(object);
	}

	public void update() {
		allmedian.setText(String.format("%.2f",Board.medianAll));
		automedian.setText(String.format("%.2f",Board.medianAuto));
		normmedian.setText(String.format("%.2f",Board.medianNormal));
		repaint();
	}


	private Component createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setSize(new Dimension(1150,50));
		panel.setLocation(new Point(0,0));

		step = new JButton("Step");
		panel.add(step);
		step.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int nrCarsaux = -1;
				try{
					nrCarsaux = Integer.valueOf(nrCars.getText());
				}catch (Exception e){
					JTextPane output = new JTextPane();
					output.setText("Please insert an integer value to set the number of cars\nValue inserted = "+nrCars.getText());
					JOptionPane.showMessageDialog(null, output, "Error", JOptionPane.PLAIN_MESSAGE);
				}
				if(nrCarsaux > 0){
					Board.notifyNumberCars(nrCarsaux);
					Board.notifyPercent(percentCars.getValue());
				}
				if(run.getText().equals("Run")) Board.step();
				else Board.stop();
			}
		});
		reset = new JButton("Reset");
		panel.add(reset);
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Board.reset();
			}
		});
		run = new JButton("Run");
		panel.add(run);
		run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(run.getText().equals("Run")){
					int time = -1;
					int nrCarsaux = -1;
					try {
						time = Integer.valueOf(speed.getText());
					} catch(Exception e){
						JTextPane output = new JTextPane();
						output.setText("Please insert an integer value to set the time per step\nValue inserted = "+speed.getText());
						JOptionPane.showMessageDialog(null, output, "Error", JOptionPane.PLAIN_MESSAGE);
					}
					try{
						nrCarsaux = Integer.valueOf(nrCars.getText());
					}catch (Exception e){
						JTextPane output = new JTextPane();
						output.setText("Please insert an integer value to set the number of cars\nValue inserted = "+nrCars.getText());
						JOptionPane.showMessageDialog(null, output, "Error", JOptionPane.PLAIN_MESSAGE);
					}
					if(time>0 && nrCarsaux > 0){
						Board.notifyNumberCars(nrCarsaux);
						Board.run(time);
						run.setText("Stop");
					}
 				} else {
					Board.stop();
 					run.setText("Run");
 				}
			}
		});
		speed = new JTextField("time per step in [1,100]");
		speed.setMargin(new Insets(5,5,5,5));
		panel.add(speed);

		nrCars = new JTextField("Nr of Cars");
		nrCars.setMargin(new Insets(5,5,5,5));
		panel.add(nrCars);

		percentCars = new JSlider(JSlider.HORIZONTAL, CARS_MIN, CARS_MAX, CARS_INIT);

		percentCars.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Board.notifyPercent(percentCars.getValue());
			}
		});

		//Turn on labels at major tick marks.
		percentCars.setMajorTickSpacing(25);
		percentCars.setMinorTickSpacing(5);
		percentCars.setPaintTicks(true);
		percentCars.setPaintLabels(true);
		panel.add(percentCars);

		communcationText = new JTextArea("Communication in Autonomous Cars: ");
		panel.add(communcationText);

		communication = new JButton("Off");
		panel.add(communication);
		communication.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(communication.getText().equals("Off")){
					communication.setText("On");
					Board.setCommunication();

				} else {
					communication.setText("Off");
					Board.setCommunication();
				}
			}
		});

		return panel;
	}

	private Component metricsPanel(){
		JPanel metricsPanel = new JPanel();
		metricsPanel.setSize(new Dimension(150, 900));
		metricsPanel.setLocation(new Point(1000,100));

		introductionText = new JTextArea("Average steps stopped:");
		metricsPanel.add(introductionText);

		allmediantext = new JTextArea("AllCars:");
		allmedian = new JTextArea("yo");
		metricsPanel.add(allmediantext);
		metricsPanel.add(allmedian);

		automediantext = new JTextArea("Autonomous:");
		automedian = new JTextArea("yo");
		metricsPanel.add(automediantext);
		metricsPanel.add(automedian);

		normmediantext = new JTextArea("Normal:");
		normmedian = new JTextArea("yo");
		metricsPanel.add(normmediantext);
		metricsPanel.add(normmedian);

		return metricsPanel;
	}


}
