package models.note;

public enum NoteType {

	_WHOLE("redonda"), _HALF("blanca"), _QUARTER("negra"), _EIGHTH("corchea"), _16TH("semicorchea"), _32ND("fusa"), _64TH("semifusa");

	private String humanName;

	private NoteType(String humanName) {
		this.humanName = humanName;
	}
	
	public String getHumanName() {
		return humanName;
	}
}
