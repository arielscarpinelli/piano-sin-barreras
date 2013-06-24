package models;

import java.math.BigDecimal;
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

		Staff stave = new Staff(measure.staves.size() + 1);
		measure.staves.add(stave);

		Note last = null;
		
		for (Object noteOrBackupOrForward : xmlMeasure
				.getNoteOrBackupOrForward()) {
			if (noteOrBackupOrForward instanceof com.audiveris.proxymusic.Attributes) {
				acceptAttributes(measure,
						stave,
						(com.audiveris.proxymusic.Attributes) noteOrBackupOrForward);
			} else if (noteOrBackupOrForward instanceof com.audiveris.proxymusic.Note) {
				Note note = Note
						.fromXmlNote((com.audiveris.proxymusic.Note) noteOrBackupOrForward);
				if (note.chord()) {
					note = last.chordWidth(note);
					stave.getSymbols().remove(last);
				}
				stave.getSymbols().add(note);
                last = note;
			} else if (noteOrBackupOrForward instanceof com.audiveris.proxymusic.Barline) {
				stave.getSymbols().add(Barline.fromXml((com.audiveris.proxymusic.Barline)noteOrBackupOrForward));
				last = null;
			} else if (noteOrBackupOrForward instanceof Backup) {
				stave = new Staff(measure.staves.size() + 1);
				measure.staves.add(stave);
				last = null;
			} else if (noteOrBackupOrForward instanceof Print){
				// Ignore print settings
			} else {
				ScoreContent.unknown("Measure", noteOrBackupOrForward);
			}
		}

		return measure;
	}

	private static void acceptAttributes(Measure measure, Staff stave,
			com.audiveris.proxymusic.Attributes xmlAttributes) {
		stave.getSymbols().add(Attributes.fromXmlAttributes(xmlAttributes));
		BigDecimal divisions = xmlAttributes.getDivisions();
		if (divisions != null) {
			measure.divisions = divisions.intValue();
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
