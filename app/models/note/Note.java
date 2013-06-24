package models.note;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBElement;

import models.ScoreContent;
import models.Symbol;

import com.audiveris.proxymusic.EmptyPlacement;
import com.audiveris.proxymusic.Grace;
import com.audiveris.proxymusic.Notations;
import com.audiveris.proxymusic.Ornaments;
import com.audiveris.proxymusic.Pitch;
import com.audiveris.proxymusic.Tie;
import com.audiveris.proxymusic.YesNo;

public class Note extends Symbol implements Cloneable, Comparable<Note> {
	
	protected NotePitch pitch = NotePitch.REST;
	protected int octave = 0;
	private NoteType type = null;
	private int duration = 0;
	protected int alter = 0;
	private int dots = 0;
	private boolean chord;
	private Boolean grace = null;

	protected Note() {}
	
	public Note(NotePitch pitch, int octave, NoteType type) {
		this.pitch = pitch;
		this.type = type;
		this.octave = octave;
	}

	public boolean chord() {
		return chord;
	}
	
	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public String getName() {
		
		String name = "";

		if (grace != null) {
			name = "Apoyatura ";
			if (grace) {
				name += "breve ";
			}
		}

		name += (pitch != null ? pitch.getHumanName() : "");

		if (Math.abs(alter) == 2) {
			name += " doble";
		}
		if (alter < 0) {
			name += " bemol";
		}
		if (alter > 0) {
			name += " sostenido";
		}
		
		if (octave > 0) {
			name += " " + NoteOctave.valueOf("_" + octave).getHumanName();
		}
		
		if (type != null) {
			name += " " + type.getHumanName();
		}
		
		if (dots > 0) {
			if (dots == 1) {
				name += " puntillo";
			} else {
				name += " " + dots + " puntillos";
			}
		}

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
				
		BigDecimal aDuration = xmlNote.getDuration();
		note.duration = (aDuration != null) ? aDuration.intValue() : 0;
		
		List<EmptyPlacement> dots = xmlNote.getDot();
		if (dots != null) {
			for(@SuppressWarnings("unused") EmptyPlacement dot : dots) {
				note.dots++;
			}
		}
		
		note.chord = (xmlNote.getChord() != null);
		
		Grace grace = xmlNote.getGrace();
		note.grace = (grace != null) ? YesNo.YES.equals(grace.getSlash()) : null;
		
		for (Notations notations : xmlNote.getNotations()) {
			note = applyNotations(notations.getTiedOrSlurOrTuplet(), note);
		}
		
		for (Tie tie : xmlNote.getTie()) {
			ScoreContent.unknown("Note", tie);
		}

		return note;
	}

	private static Note applyNotations(List<Object> notations, Note note) {
		for(Object notation : notations) {
			if (notation instanceof Ornaments) {
				note = applyOrnaments(((Ornaments) notation).getTrillMarkOrTurnOrDelayedTurn(), note);
//			} else if (notation instanceof AccidentalMark) {	
//			} else if (notation instanceof Arpeggiate) {
//			} else if (notation instanceof Articulations) {
//			} else if (notation instanceof Dynamics) {
//			} else if (notation instanceof Fermata) {
//			} else if (notation instanceof Glissando) {
//			} else if (notation instanceof NonArpeggiate) {
//			} else if (notation instanceof OtherNotation) {
//			} else if (notation instanceof Slide) {
//			} else if (notation instanceof Slur) {
//			} else if (notation instanceof Technical) {
//			} else if (notation instanceof Tied) {
//			} else if (notation instanceof Tuplet) {
				
				
			} else {
				ScoreContent.unknown("Notations", notation);
			}
		}
		return note;
	}

	private static Note applyOrnaments(
			List<JAXBElement<?>> ornaments, Note note) {
		for (JAXBElement<?> ornamentElement : ornaments) {
			Ornament ornament = Ornament.fromElement(ornamentElement);
			if (ornament != null) {
				note = ornament.decorate(note);
			} else {
				ScoreContent.unknown("Ornaments", ornamentElement.getName());
			}
		}
		return note;
	}

	@Override
	public List<String> getSounds() {
		return Arrays.asList(this.flatOrSquare().getSound());
	}

	private String getSound() {
		return  pitch.toString() + ((alter == -1) ? "b" : "")+ octave;
	}

	protected Note flatOrSquare() {
		return (alter == 0 || (alter == -1 && pitch.canBeFlat())) ? this : this.consumeAlter().flatOrSquare();
	}

	private Note consumeAlter() {
		Note result = doClone();
		int sign = (alter > 0) ? 1 : -1;
		result.pitch = pitch.move(sign);				
		result.alter -= pitch.stepSize(sign);
		if (pitch.changesOctaveWithMove(sign)) {
			result.octave += sign;
		}
		return result;
	}

	public Note doClone() {
		try {
			return (Note) this.clone();
		} catch (CloneNotSupportedException e) {} // Not gona happen!
		return null;
	}

	@Override
	public int compareTo(Note o) {
		int compare = Double.compare(octave, o.octave);
		return compare == 0 ? pitch.compareTo(o.pitch) : compare;
	}

	public Note getDiff(Note other) {
		if (other == null) {
			return this;
		} else {
			Note diff = new Note();
			if (!pitch.equals(other.pitch) || alter != other.alter) {
				diff.pitch = pitch;
				diff.alter = alter;
			} else {
				diff.pitch = null;
			}
			
			if (!type.equals(other.type) || dots != other.dots) {
				diff.type = type;
				diff.dots = dots;
			}

			diff.octave = (octave != other.octave) ? octave : 0;
			diff.duration = duration; // Even if they are equal, I wan't to know the duration
			diff.chord = chord;

			return diff;
		}
	}

	public Chord chordWidth(Note note) {
		return new Chord(this, note);
	}

	public Note addAlter(int i) {
		alter =+ i;
		return this;
	}

}
