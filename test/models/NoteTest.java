package models;

import static org.junit.Assert.*;

import org.junit.Test;

public class NoteTest {

	@Test
	public void testComparePitches() {
		Note note1 = new Note(NotePitch.C, 4, NoteType._EIGHTH);
		Note note2 = new Note(NotePitch.D, 4, NoteType._EIGHTH);
		assertTrue(note1.compareTo(note2) < 0);
		assertTrue(note2.compareTo(note1) > 0);
	}

	@Test
	public void testCompareOctaves() {
		Note note1 = new Note(NotePitch.D, 3, NoteType._EIGHTH);
		Note note2 = new Note(NotePitch.C, 4, NoteType._EIGHTH);
		assertTrue(note1.compareTo(note2) < 0);
		assertTrue(note2.compareTo(note1) > 0);
	}

}
