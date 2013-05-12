package models;

public enum NoteOctave {
	_0(""), _1("primera"), _2("segunda"), _3("tercera"), _4("cuarta"), _5("quinta"), _6("sexta"), _7("septima");
	
	private String translation;
	private NoteOctave(String translation) {
		this.translation = translation;
	}
	
	public String getTranslation() {
		return translation;
	}

}
