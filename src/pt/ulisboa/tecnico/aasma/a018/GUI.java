package pt.ulisboa.tecnico.aasma.a018;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {

    static final int CARS_MIN = 0;
    static final int CARS_MAX = 100;
    static final int CARS_INIT = 50;
    private static final long serialVersionUID = 1L;
    static JTextField speed;
    static JPanel boardPanel;
    static JButton run, reset, step, communication, trafficlights;
    static JSlider percentCars;
    static JTextField nrCars;
    static JTextArea allmediantext, allmedian;
    static JTextArea automediantext, automedian;
    static JTextArea normmediantext, normmedian;
    static JTextArea percentCarsText, nrCarText, introductionText2, communcationText, trafficlightsText;
    static JTextArea legend1, legend2;
    static int NRCARS_INIT = 20;
    private int nX, nY;

    public GUI(){
        setTitle("AASMA Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(1250, 1000);
        add(createButtonPanel());
        add(legendPanel());
        add(optionsPanel1());
        add(optionsPanel2());
        add(optionsPanel3());
        add(optionsPanel4());

        add(metricsPanel0());
        add(metricsPanel1());
        add(metricsPanel2());
        add(metricsPanel3());


        Board.initialize();
        Board.associateGUI(this);

        boardPanel = new JPanel();
        boardPanel.setSize(new Dimension(945, 875));
        boardPanel.setLocation(new Point(10, 50));

        nX = Board.nX;
        nY = Board.nY;
        boardPanel.setLayout(new GridLayout(nX, nY));
        for(int i = 0; i < nX; i++){
            for(int j = 0; j < nY; j++){
                boardPanel.add(new Cell());
            }
        }

        displayBoard();
        Board.displayObjects();
        update();
        add(boardPanel);
    }

    public void displayBoard(){
        for(int i = 0; i < nX; i++){
            for(int j = 0; j < nY; j++){
                int row = nY - j - 1, col = i;
                Block block = Board.getBlock(new Point(i, j));
                JPanel p = ((JPanel) boardPanel.getComponent(row*nX + col));
                p.setBackground(block.color);
                p.setBorder(block.getBorder());
            }
        }
    }

    public void removeObject(Object object){
        int row = nY - object.point.y - 1, col = object.point.x;
        Cell p = (Cell) boardPanel.getComponent(row*nX + col);
        p.setBorder(Board.getBlock(object.point).getBorder());
        p.objects.remove(object);

    }

    public void displayObject(Object object){
        int row = nY - object.point.y - 1, col = object.point.x;
        Cell p = (Cell) boardPanel.getComponent(row*nX + col);
        p.setBorder(Board.getBlock(object.point).getBorder());
        p.objects.add(object);
    }

    public void update(){
        allmedian.setText(String.format("%.2f", Board.medianAll));
        automedian.setText(String.format("%.2f", Board.medianAuto));
        normmedian.setText(String.format("%.2f", Board.medianNormal));
        repaint();
    }

    private Component createButtonPanel(){
        JPanel panel = new JPanel();
        panel.setSize(new Dimension(1150, 50));
        panel.setLocation(new Point(0, 0));

        step = new JButton("Step");
        panel.add(step);
        step.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0){
                int nrCarsaux = -1;
                try{
                    nrCarsaux = Integer.valueOf(nrCars.getText());
                } catch (Exception e){
                    JTextPane output = new JTextPane();
                    output.setText("Please insert an integer value to set the number of cars\nValue inserted = " + nrCars.getText());
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
            public void actionPerformed(ActionEvent arg0){
                Board.reset();
            }
        });
        run = new JButton("Run");
        panel.add(run);
        run.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0){
                if(run.getText().equals("Run")){
                    int time = -1;
                    int nrCarsaux = -1;
                    try{
                        time = Integer.valueOf(speed.getText());
                    } catch (Exception e){
                        JTextPane output = new JTextPane();
                        output.setText("Please insert an integer value to set the time per step\nValue inserted = " + speed.getText());
                        JOptionPane.showMessageDialog(null, output, "Error", JOptionPane.PLAIN_MESSAGE);
                    }
                    try{
                        nrCarsaux = Integer.valueOf(nrCars.getText());
                    } catch (Exception e){
                        JTextPane output = new JTextPane();
                        output.setText("Please insert an integer value to set the number of cars\nValue inserted = " + nrCars.getText());
                        JOptionPane.showMessageDialog(null, output, "Error", JOptionPane.PLAIN_MESSAGE);
                    }
                    if(time > 0 && nrCarsaux > 0){
                        Board.notifyNumberCars(nrCarsaux);
                        Board.notifyPercent(percentCars.getValue());
                        Board.run(time);
                        run.setText("Stop");
                    }
                }
                else{
                    Board.stop();
                    run.setText("Run");
                }
            }
        });
        speed = new JTextField("Time between steps");
        speed.setMargin(new Insets(5, 5, 5, 5));
        panel.add(speed);

        return panel;
    }

    private Component legendPanel(){
        JPanel legendPanel = new JPanel();
        legendPanel.setSize(new Dimension(250, 50));
        legendPanel.setLocation(new Point(950, 50));

        legend1 = new JTextArea("Autonomous Cars:");
        legendPanel.add(legend1);
        Legend l1 = new Legend();
        l1.color = Color.cyan;
        legendPanel.add(l1);

        legend2 = new JTextArea("Normal Cars:");
        legendPanel.add(legend2);
        Legend l2 = new Legend();
        l2.color = Color.magenta;
        legendPanel.add(l2);

        return legendPanel;
    }

    private Component optionsPanel1(){
        JPanel optionsPanel = new JPanel();
        optionsPanel.setSize(new Dimension(200, 50));
        optionsPanel.setLocation(new Point(975, 125));

        nrCarText = new JTextArea("Number of cars:");
        optionsPanel.add(nrCarText);

        nrCars = new JTextField(String.valueOf(NRCARS_INIT));
        nrCars.setMargin(new Insets(5, 5, 5, 5));
        optionsPanel.add(nrCars);

        return optionsPanel;
    }

    private Component optionsPanel2(){
        JPanel optionsPanel = new JPanel();
        optionsPanel.setSize(new Dimension(200, 100));
        optionsPanel.setLocation(new Point(975, 175));

        percentCarsText = new JTextArea(" % of autonomous cars:");
        optionsPanel.add(percentCarsText);

        percentCars = new JSlider(JSlider.HORIZONTAL, CARS_MIN, CARS_MAX, CARS_INIT);

        percentCars.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){
                Board.notifyPercent(percentCars.getValue());
            }
        });

        percentCars.setMajorTickSpacing(25);
        percentCars.setMinorTickSpacing(5);
        percentCars.setPaintTicks(true);
        percentCars.setPaintLabels(true);
        optionsPanel.add(percentCars);

        return optionsPanel;
    }

    private Component optionsPanel3(){
        JPanel optionsPanel = new JPanel();
        optionsPanel.setSize(new Dimension(200, 50));
        optionsPanel.setLocation(new Point(975, 275));

        communcationText = new JTextArea("Communication: ");
        optionsPanel.add(communcationText);

        communication = new JButton("Off");
        optionsPanel.add(communication);
        communication.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0){
                if(communication.getText().equals("Off")){
                    communication.setText("On");
                    Board.setCommunication();

                }
                else{
                    communication.setText("Off");
                    Board.setCommunication();
                }
            }
        });
        return optionsPanel;
    }

    private Component optionsPanel4(){
        JPanel optionsPanel = new JPanel();
        optionsPanel.setSize(new Dimension(200, 50));
        optionsPanel.setLocation(new Point(975, 325));

        trafficlightsText = new JTextArea("Traffic Lights: ");
        optionsPanel.add(trafficlightsText);

        trafficlights = new JButton("On");
        optionsPanel.add(trafficlights);
        trafficlights.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0){
                if(trafficlights.getText().equals("Off")){
                    trafficlights.setText("On");
                    percentCars.setEnabled(true);
                    percentCars.setValue(CARS_INIT);
                    communication.setEnabled(true);
                    Board.setTrafficLights();
                    Board.reset();

                }
                else{
                    trafficlights.setText("Off");
                    percentCars.setValue(100);
                    percentCars.setEnabled(false);
                    if(communication.getText().equals("Off")){
                        communication.setText("On");
                        Board.setCommunication();
                    }
                    communication.setEnabled(false);
                    Board.notifyPercent(100);
                    Board.setTrafficLights();
                    Board.reset();

                }
            }
        });

        return optionsPanel;
    }

    private Component metricsPanel0(){
        JPanel metricsPanel = new JPanel();
        metricsPanel.setSize(new Dimension(200, 30));
        metricsPanel.setLocation(new Point(975, 460));

        introductionText2 = new JTextArea("Ticks per distance:");
        metricsPanel.add(introductionText2);
        return metricsPanel;
    }

    private Component metricsPanel1(){
        JPanel metricsPanel = new JPanel();
        metricsPanel.setSize(new Dimension(200, 30));
        metricsPanel.setLocation(new Point(975, 490));

        allmediantext = new JTextArea("AllCars:");
        allmedian = new JTextArea("DUMMY");
        metricsPanel.add(allmediantext);
        metricsPanel.add(allmedian);

        return metricsPanel;
    }

    private Component metricsPanel2(){
        JPanel metricsPanel = new JPanel();
        metricsPanel.setSize(new Dimension(200, 30));
        metricsPanel.setLocation(new Point(975, 520));

        automediantext = new JTextArea("Autonomous:");
        automedian = new JTextArea("DUMMY");
        metricsPanel.add(automediantext);
        metricsPanel.add(automedian);

        return metricsPanel;
    }

    private Component metricsPanel3(){
        JPanel metricsPanel = new JPanel();
        metricsPanel.setSize(new Dimension(200, 30));
        metricsPanel.setLocation(new Point(975, 550));

        normmediantext = new JTextArea("Normal:");
        normmedian = new JTextArea("DUMMY");
        metricsPanel.add(normmediantext);
        metricsPanel.add(normmedian);

        return metricsPanel;
    }

    public class Cell extends JPanel {

        private static final long serialVersionUID = 1L;

        public List<Object> objects = new ArrayList<>();

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            for(Object object : objects){
                g.setColor(object.getColor());
                if(object instanceof TrafficLight){
                    g.fillPolygon(new int[]{5, 26, 26, 5}, new int[]{5, 5, 26, 26}, 4);
                    break;
                }
                else{
                    switch(((Car) object).direction){
                        case 0:
                            g.fillPolygon(new int[]{5, 16, 26, 26, 16, 5}, new int[]{5, 5, 9, 21, 26, 26}, 6);
                            break;
                        case 90:
                            g.fillPolygon(new int[]{5, 26, 26, 21, 9, 5}, new int[]{5, 5, 16, 26, 26, 16}, 6);
                            break;
                        case 180:
                            g.fillPolygon(new int[]{5, 15, 26, 26, 15, 5}, new int[]{9, 5, 5, 26, 26, 21}, 6);
                            break;
                        default:
                            g.fillPolygon(new int[]{5, 9, 21, 26, 26, 5}, new int[]{15, 5, 5, 15, 26, 26}, 6);
                            break;
                    }
                }
            }
        }
    }

    public class Legend extends JPanel {

        public Color color;

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            g.setColor(color);
            g.fillPolygon(new int[]{0, 50, 80, 80, 50, 0}, new int[]{0, 0, 40, 60, 80, 80}, 6);
        }
    }

}
