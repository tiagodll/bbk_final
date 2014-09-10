package com.dalligna.trackYourBag;

import android.test.AndroidTestCase;

public class UtilTester extends AndroidTestCase {
	public void testOnlineState() {
		assertTrue(Util.checkOnlineState(getContext()));
	}
}
