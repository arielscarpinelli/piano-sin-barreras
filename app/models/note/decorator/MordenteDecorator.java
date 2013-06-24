package models.note.decorator;

import java.util.ArrayList;
import java.util.List;

import models.note.Note;

public class MordenteDecorator extends NoteDecorator {

	private int step;

	public MordenteDecorator(Note note, String nameDecorator, int step) {
		super(note, nameDecorator);
		this.step = step;
	}

	public List<String> getSounds() {
		List<String> original = super.getSounds();
		ArrayList<String> result = new ArrayList<String>();
		result.add("arpeg");
		result.addAll(original);
		result.addAll(note.doClone().addAlter(step).getSounds());
		result.addAll(original);
		return result;
	};
}
