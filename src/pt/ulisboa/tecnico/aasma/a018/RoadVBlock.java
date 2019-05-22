package pt.ulisboa.tecnico.aasma.a018;

import java.awt.*;

public class RoadVBlock extends Block {
    private static final Color BLACK = Color.black;
    private static final Color WHITE = Color.white;

    public RoadVBlock(){
        super(BLACK, new Board.ComplexBoarder(BLACK, BLACK, WHITE, WHITE));
    }
}
