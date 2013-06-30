package models;

import java.util.ArrayList;
import java.util.List;

import models.note.Note;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.audiveris.proxymusic.Backup;
import com.audiveris.proxymusic.Print;

@JsonSerialize(include = Inclusion.NON_NULL)
public class Measure {

	private int number;
	private int divisions;

	private List<Staff> staves = new ArrayList<Staff>();

	public List<Staff> getStaves() {
		return staves;
	}

	public int getNumber() {
		return number;
	}

	public int getDivisions() {
		return divisions;
	}

	public String getName() {
		return "Compás " + number;
	}

	public static Measure fromXmlMeasure(
			com.audiveris.proxymusic.ScorePartwise.Part.Measure xmlMeasure,
			ScoreContent score) {

		Measure measure = new Measure();
		measure.number = Integer.parseInt(xmlMeasure.getNumber());
		
		Note last = null;
		
		for (Object noteOrBackupOrForward : xmlMeasure
				.getNoteOrBackupOrForward()) {
			if (noteOrBackupOrForward instanceof com.audiveris.proxymusic.Note) {
				Note note = Note
						.fromXmlNote((com.audiveris.proxymusic.Note) noteOrBackupOrForward);
				if (note != null) {
					// Note may be null if print-object = no
					if (note.chord()) {
						note = last.chordWidth(note);
						measure.getStaffFor(last).getSymbols().remove(last);
					}
					measure.addNote(note);
				}
	            last = note;
			} else {
				last = null;
				if (noteOrBackupOrForward instanceof com.audiveris.proxymusic.Attributes) {
					measure.applyAttributes(Attributes.fromXmlAttributes((com.audiveris.proxymusic.Attributes) noteOrBackupOrForward));
				} else if (noteOrBackupOrForward instanceof com.audiveris.proxymusic.Barline) {
					measure.addToAllStaves(Barline.fromXml((com.audiveris.proxymusic.Barline)noteOrBackupOrForward));
				} else if (noteOrBackupOrForward instanceof Backup) {
					// Ignore backup
				} else if (noteOrBackupOrForward instanceof Print){
					// Ignore print settings
				} else {
					ScoreContent.unknown("Measure", noteOrBackupOrForward);
				}
			}
		}

		return measure;
	}

	private Staff getStaffFor(Note note) {
		ensureStaves(note.getStaff());
		return staves.get(note.getStaff() - 1);
	}

	private void ensureStaves(int quantity) {
		while(staves.size() < quantity) {
			staves.add(new Staff(staves.size() + 1));
		}
	}

	private void addNote(Note note) {
		getStaffFor(note).getSymbols().add(note);		
	}

	private void applyAttributes(Attributes attributes) {
		ensureStaves(attributes.staves);
		for(Staff staff : staves) {
			staff.getSymbols().add(0, attributes);
		}
		divisions = attributes.divisions;
	}

	private void addToAllStaves(Symbol symbol) {
		for(Staff staff : staves) {
			staff.getSymbols().add(symbol);
		}
	}

	public void join(Measure other) {
		for (Staff stave : other.staves) {
			stave.setPosition(staves.size() + 1);
			staves.add(stave);
		}
		// TODO: join attributes
	}

}
