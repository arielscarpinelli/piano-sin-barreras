package models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import models.note.NoteOctave;
import models.note.NotePitch;

import com.audiveris.proxymusic.Clef;
import com.audiveris.proxymusic.Time;

public class Attributes extends Symbol implements Cloneable {
	
	public enum Key {
		Do_mayor, Sol_mayor, Re_mayor, La_mayor, Mi_mayor, Si_mayor, Fa_sostenido_mayor, Re_bemol_mayor, La_bemol_mayor, Mi_bemol_mayor, Si_bemol_mayor, Fa_mayor,
		La_menor, Mi_menor, Si_menor, Fa_sostenido_menor, Do_sostenido_menor, Sol_sostenido_menor, Re_sostenido_menor, Si_bemol_menor, Fa_menor, Do_menor, Sol_menor, Re_Menor; 
	}

	private int timeLower;
	private int timeUpper;
	private Key key;

	protected int divisions;
	protected int staves = 1;
	
	private List<NotePitch> clefs = new ArrayList<NotePitch>();
	private List<NoteOctave> clefLines = new ArrayList<NoteOctave>();
	
	private NotePitch clef = null;
	private NoteOctave clefLine = null;
	
	public Key getKey() {
		return key;
	}

	@Override
	public int getDuration() {
		return 0;
	}

	@Override
	public String getName() {
		String toReturn = "";
		
		if (clef != null) {
			toReturn += "Clave de " + clef.getHumanName() + " en " + clefLine.getHumanName();
		}
		
		if (key != null) {
			toReturn += " Armadura de " + key.name().replace("_", " ");
		}
		if (timeLower > 0 && timeUpper > 0) {
			toReturn += " " + timeUpper + " sobre " + timeLower;
		}
		return toReturn;
	}

	public static Attributes fromXmlAttributes(
			com.audiveris.proxymusic.Attributes xmlAttributes) {
		
		Attributes attributes = new Attributes();
		
		for(Clef clef : xmlAttributes.getClef()) {
			attributes.addClef(clef);
		}

		for(com.audiveris.proxymusic.Key key : xmlAttributes.getKey()) {
			attributes.applyKey(key);
		}

		for(Time time : xmlAttributes.getTime()) {
			attributes.applyTime(time);
		}
		
		BigDecimal divisions = xmlAttributes.getDivisions();
		if (divisions != null) {
			attributes.divisions = divisions.intValue();
		}
		
		if (xmlAttributes.getStaves() != null) {
			attributes.staves = xmlAttributes.getStaves().intValue();
		}

		return attributes;
		
	}

	private void applyKey(com.audiveris.proxymusic.Key key) {
		int fifths = key.getFifths().intValue();
		if (fifths < 0) {
			fifths += 12;
		}
		if ("minor".equals(key.getMode()) ) {
			fifths += 12;
		}
		this.key = Key.values()[fifths];
	}

	private void addClef(Clef clef) {
		clefs.add(
				NotePitch.valueOf(clef.getSign().name()));
		clefLines.add(NoteOctave.valueOf("_" + clef.getLine().toString()));
	}

	private void applyTime(Time time) {
		List<JAXBElement<String>> timeParts = time.getTimeSignature();
		if (timeParts.size() >= 2) {
			timeUpper = Integer.parseInt(timeParts.get(0).getValue());
			timeLower = Integer.parseInt(timeParts.get(1).getValue());
		}
	}

	public Attributes cloneFor(int staff) {
		Attributes result = null;
		try {
			result = (Attributes) clone();
		} catch (CloneNotSupportedException e) {} // NOT GONNA HAPPEN!
		
		staff--;
		
		if (staff < clefs.size()) {
			result.clef = clefs.get(staff);
			result.clefLine = clefLines.get(staff);
		}
		
		return result;
	}

}
