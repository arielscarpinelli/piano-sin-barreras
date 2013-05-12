package models;

import java.util.ArrayList;
import java.util.List;

import com.audiveris.proxymusic.ScorePartwise;
import com.audiveris.proxymusic.ScorePartwise.Part;

public class Score {
	private List<Measure> measures = new ArrayList<Measure>();

	public List<Measure> getMeasures() {
		return measures;
	}

	public static Score fromScorePartwise(ScorePartwise xmlScore) {
		Score score = new Score();
		for(Part part : xmlScore.getPart()) {
			for(com.audiveris.proxymusic.ScorePartwise.Part.Measure measure : part.getMeasure()) {
				score.measures.add(Measure.fromXmlMeasure(measure, score));
			}			
		}
		
		score.addEnd();
		
		return score;
	}

	public void addEnd() {
		
		Measure measure = new Measure();
		measures.add(measure);
		
		Staff staff = new Staff(0);
		measure.getStaves().add(staff);

		staff.getNotes().add(new End());

	}
}
