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
}