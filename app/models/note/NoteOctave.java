package models.note;

public enum NoteOctave {
	_0(""), _1("primera"), _2("segunda"), _3("tercera"), _4("cuarta"), _5("quinta"), _6("sexta"), _7("septima");
	
	private String human;
	private NoteOctave(String human) {
		this.human = human;
	}
	
	public String getHumanName() {
		return human;
	}

}
