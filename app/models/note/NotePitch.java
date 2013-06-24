package models.note;

public enum NotePitch {
	
	C("Do", false),D("Re"),E("Mi"),F("Fa", false),G("Sol"),A("La"),B("Si"),REST("Silencio");
	
	private String humanName;
	private boolean canBeFlat;

	private NotePitch(String humanName) {
		this(humanName, true);
	}
	
	private NotePitch(String humanName, boolean canBeFlat) {
		this.humanName = humanName;
		this.canBeFlat = canBeFlat;
	}

	public String getHumanName() {
		return humanName;
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

	public int stepSize(int sign) {
		return (sign < 0) ? (canBeFlat ? -2 : -1) : -move(1).stepSize(-1) ;
	}

	public boolean canBeFlat() {
		return canBeFlat;
	}
}