package models;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Chord extends Note {
	
	private SortedSet<Note> notes = new TreeSet<Note>();
	
	public Chord(Note note, Note note2) {
		add(note);
		add(note2);
	}

	@Override
	public List<String> getSounds() {
		List<String> sounds = new ArrayList<String>();
		for(Note note : notes) {
			sounds.addAll(note.getSounds());
		}
		return sounds;
	}

	public void add(Note note) {
		notes.add(note);
	}

	@Override
	public String getName() {
		String result = "";
		Note lastNote = null;
		for (Note note : notes) {
			result += note.getDiff(lastNote).getName() + " ";
			lastNote = note;
		}
		return result;
	}

	@Override
	public int getDuration() {
		int duration = 0;
		for (Note note : notes) {
			int noteDuration = note.getDuration();
			if (noteDuration > duration) {
				duration = noteDuration;
			}
		}
		return duration;
	}

	@Override
	public Chord chordWidth(Note note) {
		add(note);
		return this;
	}
}
