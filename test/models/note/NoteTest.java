package models.note;

import static org.junit.Assert.*;

import models.note.Note;
import models.note.NotePitch;
import models.note.NoteType;

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

	@Test
	public void testFlatOrSquareFullStep() {
		Note note = new Note(NotePitch.F, 5, NoteType._EIGHTH);
		note.alter = 1;
		Note flatOrSquare = note.flatOrSquare();
		assertEquals(NotePitch.G, flatOrSquare.pitch);
		assertEquals(-1, flatOrSquare.alter);
	}

	@Test
	public void testFlatOrSquareHalfStep() {
		Note note = new Note(NotePitch.E, 5, NoteType._EIGHTH);
		note.alter = 1;
		Note flatOrSquare = note.flatOrSquare();
		assertEquals(NotePitch.F, flatOrSquare.pitch);
		assertEquals(0, flatOrSquare.alter);
	}

	@Test
	public void testFlatOrSquareOctave() {
		Note note = new Note(NotePitch.B, 5, NoteType._EIGHTH);
		note.alter = 1;
		Note flatOrSquare = note.flatOrSquare();
		assertEquals(NotePitch.C, flatOrSquare.pitch);
		assertEquals(0, flatOrSquare.alter);
		assertEquals(6, flatOrSquare.octave);
	}

	@Test
	public void testFlatOrSquareDoubleAlter() {
		Note note = new Note(NotePitch.F, 5, NoteType._EIGHTH);
		note.alter = 2;
		Note flatOrSquare = note.flatOrSquare();
		assertEquals(NotePitch.G, flatOrSquare.pitch);
		assertEquals(0, flatOrSquare.alter);
	}

	@Test
	public void testFlatOrSquareDoubleNegative() {
		Note note = new Note(NotePitch.F, 5, NoteType._EIGHTH);
		note.alter = -2;
		Note flatOrSquare = note.flatOrSquare();
		assertEquals(NotePitch.E, flatOrSquare.pitch);
		assertEquals(-1, flatOrSquare.alter);
	}

	@Test
	public void testFlatOrSquareDoubleAlterOctave() {
		Note note = new Note(NotePitch.B, 5, NoteType._EIGHTH);
		note.alter = 2;
		Note flatOrSquare = note.flatOrSquare();
		assertEquals(NotePitch.D, flatOrSquare.pitch);
		assertEquals(-1, flatOrSquare.alter);
		assertEquals(6, flatOrSquare.octave);
	}

	@Test
	public void testFlatOrSquareTripeAlterOctave() {
		Note note = new Note(NotePitch.D, 5, NoteType._EIGHTH);
		note.alter = -3;
		Note flatOrSquare = note.flatOrSquare();
		assertEquals(NotePitch.B, flatOrSquare.pitch);
		assertEquals(0, flatOrSquare.alter);
		assertEquals(4, flatOrSquare.octave);
	}
}
