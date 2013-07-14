package models;

import java.util.ArrayList;
import java.util.List;

import models.note.Note;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Voice {

	private List<Symbol> symbols = new ArrayList<Symbol>();
	private int position;
	private String originalName;
	private ArrayList<Integer> staveUsage = new ArrayList<Integer>();
	
	public Voice(String originalName, int position) {
		this.originalName = originalName;
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
		
		return "Voz " + position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@JsonIgnore
	public String getOriginalName() {
		return originalName;
	}

	public void add(Note note) {
		symbols.add(note);
		while(note.getStaff() > staveUsage.size()) {
			staveUsage.add(new Integer(0));
		}
		staveUsage.set(note.getStaff()-1, staveUsage.get(note.getStaff()-1) + 1);
	}

	public void add(Symbol symbol) {
		symbols.add(symbol);
	}
	
	//@JsonIgnore
	public int getStaff() {
		int maxIndex = 0;
		for(int i = 1; i < staveUsage.size(); i++) {
			if (staveUsage.get(i) > staveUsage.get(maxIndex)) {
				maxIndex = i;
			}
		}
		return maxIndex + 1;
	}

	public void prepend(Symbol symbol) {
		symbols.add(0, symbol);
	}
}
