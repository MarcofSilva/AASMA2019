package pt.ulisboa.tecnico.aasma.a018;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;


/**
 * Graphical interface
 * @author Rui Henriques
 */
public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	static JTextField speed;
	static JPanel boardPanel;
	static JButton run, reset, step;
	private int nX, nY;

	public class Cell extends JPanel {

		private static final long serialVersionUID = 1L;
		
		public List<Object> objects = new ArrayList<Object>();
		
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
		setSize(1000, 1000);
		//button panel on top
		add(createButtonPanel());

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
		repaint();
	}


	private Component createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setSize(new Dimension(700,50));
		panel.setLocation(new Point(0,0));
		
		step = new JButton("Step");
		panel.add(step);
		step.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
					try {
						time = Integer.valueOf(speed.getText());
					} catch(Exception e){
						JTextPane output = new JTextPane();
						output.setText("Please insert an integer value to set the time per step\nValue inserted = "+speed.getText());
						JOptionPane.showMessageDialog(null, output, "Error", JOptionPane.PLAIN_MESSAGE);
					}
					if(time>0){
						Board.run(time);
	 					run.setText("Stop");						
					}
 				} else {
					Board.stop();
 					run.setText("Run");
 				}
			}
		});
		speed = new JTextField(" time per step in [1,100]");
		speed.setMargin(new Insets(5,5,5,5));
		panel.add(speed);
		
		return panel;
	}

}
