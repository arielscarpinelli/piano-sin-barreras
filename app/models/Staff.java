package models;

import java.util.ArrayList;
import java.util.List;

public class Staff {

	private List<Symbol> notes = new ArrayList<Symbol>();
	private int position;
	
	public Staff(int position) {
		this.position = position;
	}

	public List<Symbol> getNotes() {
		return notes;
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

}
