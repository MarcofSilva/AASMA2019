import pt.ulisboa.tecnico.aasma.a018.GUI;

import java.awt.*;

public class Main {

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            public void run(){
                try{
                    GUI frame = new GUI();
                    frame.setVisible(true);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
