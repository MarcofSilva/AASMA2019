package pt.ulisboa.tecnico.aasma.a018;

import javax.swing.border.AbstractBorder;
import java.awt.Color;

public class Block {

	public Color color;
	public AbstractBorder border;


	public Block(Color color, AbstractBorder border) {
		this.color = color;
		this.border = border;
	}
	public Color getColor(){
		return this.color;
	}

	public AbstractBorder getBorder(){
		return this.border;
	}
}
