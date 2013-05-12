package models;

import java.math.BigDecimal;

import com.audiveris.proxymusic.Pitch;

public class Note extends Symbol {
	
	private NotePitch pitch = NotePitch.REST;
	private int octave = 0;
	private NoteType type = NoteType._WHOLE;
	private int duration = 0;
	private int alter = 0;

	public NotePitch getPitch() {
		return pitch;
	}

	public int getOctave() {
		return octave;
	}

	public int getDuration() {
		return duration;
	}

	public int getAlter() {
		return alter;
	}

	public String getName() {
		String name = pitch.getTranslation();
		if (alter == -1) {
			name += " bemol ";
		}
		if (alter == 1) {
			name += " sostenido ";
		}
		name += " " + NoteOctave.valueOf("_" + octave).getTranslation() + " " + type.getTranslation();
		return name;
	}
	
	public static Note fromXmlNote(com.audiveris.proxymusic.Note xmlNote) {
		Note note = new Note();

		Pitch pitch = xmlNote.getPitch();
		if (pitch != null) {
			note.pitch = NotePitch.valueOf(pitch.getStep().value().toUpperCase());
			note.octave = Integer.valueOf(pitch.getOctave());
			BigDecimal alter = pitch.getAlter();
			if (alter != null) {
				note.alter = alter.intValue();
			}
		}

		com.audiveris.proxymusic.NoteType type = xmlNote.getType();
		if (type != null) {
			note.type = NoteType.valueOf("_" + type.getValue().toUpperCase());
		}
		
		note.duration = xmlNote.getDuration().intValue();
		
		return note;
	}
	
}
