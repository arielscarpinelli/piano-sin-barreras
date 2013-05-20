package models;

import java.util.ArrayList;
import java.util.List;

public class Chord extends Note {
	
	private List<Note> notes = new ArrayList<Note>();

	public List<Note> getNotes() {
		return notes;
	}
	
	public void add(Note note) {
		notes.add(note);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

}
