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
			Score score) {

		Measure measure = new Measure();
		measure.number = Integer.parseInt(xmlMeasure.getNumber());

		Staff stave = new Staff(measure.staves.size() + 1);
		measure.staves.add(stave);

		for (Object noteOrBackupOrForward : xmlMeasure
				.getNoteOrBackupOrForward()) {
			if (noteOrBackupOrForward instanceof com.audiveris.proxymusic.Attributes) {
				acceptAttributes(measure,
						stave,
						(com.audiveris.proxymusic.Attributes) noteOrBackupOrForward);
			} else if (noteOrBackupOrForward instanceof com.audiveris.proxymusic.Note) {
				// TODO: check for chord
				stave.getNotes()
						.add(Note
								.fromXmlNote((com.audiveris.proxymusic.Note) noteOrBackupOrForward));
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

}
