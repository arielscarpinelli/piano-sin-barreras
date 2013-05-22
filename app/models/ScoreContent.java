package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.audiveris.proxymusic.ScorePartwise;
import com.audiveris.proxymusic.ScorePartwise.Part;

public class ScoreContent {
	private List<Measure> measures = new ArrayList<Measure>();

	public List<Measure> getMeasures() {
		return measures;
	}

	public static ScoreContent fromScorePartwise(ScorePartwise xmlScore) {
		ScoreContent score = new ScoreContent();
		for(Part part : xmlScore.getPart()) {
			List<Measure> partMeasures = new ArrayList<Measure>();
			for(com.audiveris.proxymusic.ScorePartwise.Part.Measure measure : part.getMeasure()) {
				partMeasures.add(Measure.fromXmlMeasure(measure, score));
			}
			score.joinMeasures(partMeasures);
		}
		
		score.addEnd();
		
		return score;
	}

	private void joinMeasures(List<Measure> otherMeasures) {
		if (measures.isEmpty()) {
			measures = otherMeasures;
		} else {
			Iterator<Measure> mine = measures.iterator();
			Iterator<Measure> their = otherMeasures.iterator();
			while(their.hasNext()) {
				if(mine.hasNext()) {
					mine.next().join(their.next());
				} else {
					measures.add(their.next());
				}
			}
		}
		
	}

	public void addEnd() {
		
		Measure measure = new Measure();
		measures.add(measure);
		
		Staff staff = new Staff(0);
		measure.getStaves().add(staff);

		staff.getSymbols().add(new End());

	}
}
