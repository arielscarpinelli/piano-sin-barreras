package models;

public class End extends Symbol {

	@Override
	public String getName() {
		return "Fin de partitura";
	}

	@Override
	public int getDuration() {
		return 0;
	}

}
