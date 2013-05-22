package models;

public enum NotePitch {
	
	C("Do"),D("Re"),E("Mi"),F("Fa"),G("Sol"),A("La"),B("Si"),REST("Silencio");
	
	private String translation;

	private NotePitch(String translation) {
		this.translation = translation;
	}
	
	public String getTranslation() {
		return translation;
	}

	public NotePitch move(int direction) {
		NotePitch[] values = NotePitch.values();
		int target = ordinal() + direction;
		while (target < 0) {
			target+= values.length-1;
		}
		return values[target % (values.length-1)];
	}
	
	public boolean changesOctaveWithMove(int direction) {
		int targetOrdinal = (ordinal() + direction);
		return (targetOrdinal < 0) || (targetOrdinal >= (values().length-1));
	}
}