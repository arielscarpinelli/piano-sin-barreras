package models;

import java.util.ArrayList;
import java.util.List;

public class Staff {

	private List<Symbol> symbols = new ArrayList<Symbol>();
	private int position;
	
	public Staff(int position) {
		this.position = position;
	}

	public List<Symbol> getSymbols() {
		return symbols;
	}

	public String getName() {
		if (position == 0) {
			return "";
		}
		if (position == 1) {
			return "Mano derecha";
		}
		if (position == 2) {
			return "Mano izquierda";
		}
		
		return "Pentagrama " + position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
