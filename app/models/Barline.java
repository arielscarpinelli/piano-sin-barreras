package models;

import com.audiveris.proxymusic.BackwardForward;

public class Barline extends Symbol {

	private boolean directionForward;
	
	public static Barline fromXml(
			com.audiveris.proxymusic.Barline xmlBarline) {
		Barline barline = new Barline();
		barline.directionForward = BackwardForward.FORWARD.equals(xmlBarline.getRepeat().getDirection());
		return barline;
	}

	@Override
	public String getName() {
		return directionForward ? "Abre barras de repetición" : "Cierra barras de repetición";
	}

	@Override
	public int getDuration() {
		return 0;
	}

	public boolean isDirectionForward() {
		return directionForward;
	}

}
