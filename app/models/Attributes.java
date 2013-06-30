package models;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.JAXBElement;

import com.audiveris.proxymusic.Clef;
import com.audiveris.proxymusic.Time;

public class Attributes extends Symbol {
	
	public enum Key {
		Do_mayor, Sol_mayor, Re_mayor, La_mayor, Mi_mayor, Si_mayor, Fa_sostenido_mayor, Re_bemol_mayor, La_bemol_mayor, Mi_bemol_mayor, Si_bemol_mayor, Fa_mayor,
		La_menor, Mi_menor, Si_menor, Fa_sostenido_menor, Do_sostenido_menor, Sol_sostenido_menor, Re_sostenido_menor, Si_bemol_menor, Fa_menor, Do_menor, Sol_menor, Re_Menor; 
	}

	private int timeLower;
	private int timeUpper;
	private Key key;

	protected int divisions;
	protected int staves = 1;
	
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
		if (timeLower > 0 && timeUpper > 0) {
			toReturn += timeUpper + " sobre " + timeLower;
		}
		if (key != null) {
			toReturn += " Armadura de " + key.name().replace("_", " ");
		}
		return toReturn;
	}

	public static Attributes fromXmlAttributes(
			com.audiveris.proxymusic.Attributes xmlAttributes) {
		
		Attributes attributes = new Attributes();
		
		for(Clef clef : xmlAttributes.getClef()) {
			attributes.applyClef(clef);
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

	private void applyClef(Clef clef) {
		// TODO Auto-generated method stub
		
	}

	private void applyTime(Time time) {
		List<JAXBElement<String>> timeParts = time.getTimeSignature();
		if (timeParts.size() >= 2) {
			timeUpper = Integer.parseInt(timeParts.get(0).getValue());
			timeLower = Integer.parseInt(timeParts.get(1).getValue());
		}
	}

}
