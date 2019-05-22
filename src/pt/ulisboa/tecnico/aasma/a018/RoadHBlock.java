package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;

public class RoadHBlock extends Block {
    private static final Color BLACK = Color.black;
    private static final Color WHITE = Color.white;

    public RoadHBlock(){
        super(BLACK, new Board.ComplexBoarder(WHITE, WHITE, BLACK, BLACK));
    }
}
