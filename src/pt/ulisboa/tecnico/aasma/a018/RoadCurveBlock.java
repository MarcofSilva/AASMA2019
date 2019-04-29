package pt.ulisboa.tecnico.aasma.a018;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoadCurveBlock extends Block {

    private String _action;

    public RoadCurveBlock(Color color, AbstractBorder border, String action) {
        super(color, border);
        _action = action;
    }

    public String getAction() {
        return _action;
    }
}
