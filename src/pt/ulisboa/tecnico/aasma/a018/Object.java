package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;

public abstract class Object extends Thread{

    protected Color color;
    protected Point point;

    public Object(Color color, Point point){
        this.point = point;
        this.color = color;
    }

    public Color getColor(){
        return color;
    }
}
