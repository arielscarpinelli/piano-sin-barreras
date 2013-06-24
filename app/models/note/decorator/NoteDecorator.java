package models.note.decorator;

import java.util.List;

import models.note.Note;

public class NoteDecorator extends Note {

	protected Note note;
	private String nameDecorator;

	public NoteDecorator(Note note, String nameDecorator) {
		this.note = note;
		this.nameDecorator = nameDecorator;
	}

	public boolean chord() {
		return note.chord();
	}

	public int getDuration() {
		return note.getDuration();
	}

	public String getName() {
		return note.getName() + " " + nameDecorator;
	}

	public int hashCode() {
		return note.hashCode();
	}

	public boolean equals(Object obj) {
		return note.equals(obj);
	}

	public List<String> getSounds() {
		return note.getSounds();
	}

	public int compareTo(Note o) {
		return note.compareTo(o);
	}

	public Note getDiff(Note other) {
		return note.getDiff(other);
	}

	public Note addAlter(int i) {
		return note.addAlter(i);
	}

	public String toString() {
		return note.toString();
	}

}
