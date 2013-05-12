package models;

public enum NoteType {

	_WHOLE("redonda"), _HALF("blanca"), _QUARTER("negra"), _EIGHTH("corchea"), _16TH("semicorchea"), _32ND("fusa"), _64TH("semifusa");

	private String translation;

	private NoteType(String translation) {
		this.translation = translation;
	}
	
	public String getTranslation() {
		return translation;
	}
}
