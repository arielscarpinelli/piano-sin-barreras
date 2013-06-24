package models;

import junit.framework.Assert;

import models.note.NotePitch;

import org.junit.Test;

public class NotePitchTest {

	@Test
	public void testMoveOneToTheRightFromC() {
		NotePitch pitch = NotePitch.C.move(1);
		Assert.assertEquals(NotePitch.D, pitch);
	}
	
	@Test
	public void testMoveOneToTheRightFromB() {
		NotePitch pitch = NotePitch.B.move(1);
		Assert.assertEquals(NotePitch.C, pitch);
	}

	@Test
	public void testMoveOneToTheLeftFromC() {
		NotePitch pitch = NotePitch.C.move(-1);
		Assert.assertEquals(NotePitch.B, pitch);
	}
	
	@Test
	public void testMoveOneToTheLeftFromB() {
		NotePitch pitch = NotePitch.B.move(-1);
		Assert.assertEquals(NotePitch.A, pitch);
	}
}
