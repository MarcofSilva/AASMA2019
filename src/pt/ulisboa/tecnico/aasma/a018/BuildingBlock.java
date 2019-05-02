package pt.ulisboa.tecnico.aasma.a018;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class BuildingBlock extends Block {
    private static final Color buildingColor = Color.gray;

    public BuildingBlock(){
        super(buildingColor, new Board.ComplexBoarder(buildingColor, buildingColor, buildingColor, buildingColor));
    }
}
