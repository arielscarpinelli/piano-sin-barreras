package models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.audiveris.proxymusic.Backup;

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

		Chord currentChord = null;
		
		for (Object noteOrBackupOrForward : xmlMeasure
				.getNoteOrBackupOrForward()) {
			if (noteOrBackupOrForward instanceof com.audiveris.proxymusic.Attributes) {
				acceptAttributes(measure,
						stave,
						(com.audiveris.proxymusic.Attributes) noteOrBackupOrForward);
			} else if (noteOrBackupOrForward instanceof com.audiveris.proxymusic.Note) {
				Note note = Note
						.fromXmlNote((com.audiveris.proxymusic.Note) noteOrBackupOrForward);
				if (!note.chord()) {
					currentChord = null;
				}
				if (currentChord == null) {
					currentChord = new Chord();
					stave.getNotes().add(currentChord);
				}
				currentChord.add(note);
			} else if (noteOrBackupOrForward instanceof Backup) {
				stave = new Staff(measure.staves.size() + 1);
				measure.staves.add(stave);
			}
		}

		return measure;
	}

	private static void acceptAttributes(Measure measure, Staff stave,
			com.audiveris.proxymusic.Attributes xmlAttributes) {
		stave.getNotes().add(Attributes.fromXmlAttributes(xmlAttributes));
		BigDecimal divisions = xmlAttributes.getDivisions();
		if (divisions != null) {
			measure.divisions = divisions.intValue();
		}
	}

	public void join(Measure other) {
		for(Staff stave : other.staves) {
			stave.setPosition(staves.size() + 1);
			staves.add(stave);
		}
		// TODO: join attributes
	}

}
