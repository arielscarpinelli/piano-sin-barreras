package models.note;

import javax.xml.bind.JAXBElement;

import models.note.decorator.MordenteDecorator;

public enum Ornament {
	MORDENT("mordent") {
		@Override
		public Note decorate(Note note) {
			return new MordenteDecorator(note, "mordente inferior", -2);
		}
	},
	INVERTED_MORDENT("inverted-mordent") {
		@Override
		public Note decorate(Note note) {
			return new MordenteDecorator(note, "mordente superior", 2);
		}
	};

	private String elementName;

	private Ornament(String elementName) {
		this.elementName = elementName;
	}
	
	public static Ornament fromElement(JAXBElement<?> ornamentElement) {
		String name = ornamentElement.getName().getLocalPart();
		for(Ornament ornament : Ornament.values()) {
			if (ornament.elementName.equals(name)) {
				return ornament;
			}
		}
		return null;
	}

	public Note decorate(Note note) {
		return note;
	}

}
