package com.noahhuppert.reflect;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

public class FailTest extends TestCase {
    @SmallTest
    public void testFail(){
        assertEquals(false, true);
    }
}
