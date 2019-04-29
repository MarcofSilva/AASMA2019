package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;

public class TrafficLight extends Object {

    private int redDurationTick;
    private int yellowDurantionTick;
    private int greenDurantionTick;
    private int ticksLeft;
    public TrafficLight(Point position, int redDurationTick, int yellowDurantionTick, int greenDurantionTick, Color initial){
        super(initial, position);
        this.redDurationTick = redDurationTick;
        this.yellowDurantionTick = yellowDurantionTick;
        this.greenDurantionTick = greenDurantionTick;
        if(initial == Color.green){
            ticksLeft = greenDurantionTick;
        }
        else if(initial == Color.red) {
            ticksLeft = redDurationTick;
        }
        else {
            ticksLeft = yellowDurantionTick;
        }
    }

    public Color getColor(){
        return color;
    }

    public void tick(){
        ticksLeft--;
        if(ticksLeft == 0){
            if(color == Color.green){
                color = Color.yellow;
                ticksLeft = yellowDurantionTick;
            }
            else if(color == Color.yellow){
                color = Color.red;
                ticksLeft = redDurationTick;
            }
            else {
                color = Color.green;
                ticksLeft = greenDurantionTick;
            }
        }
    }
}
