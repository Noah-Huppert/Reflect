package com.noahhuppert.reflect.database.converters;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the StringListConverter
 */
public class StringListConverterTest extends TestCase {
    StringListConverter converter;
    List<String> stringList;
    String dbValueString;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        converter = new StringListConverter();

        stringList = new ArrayList<String>();
        stringList.add("string1");
        stringList.add("string2");
        stringList.add("string3");

        dbValueString = "[\"string1\",\"string2\",\"string3\"]";
    }

    /**
     * Tests {@link com.noahhuppert.reflect.database.converters.StringListConverter#getDBValue(java.util.List)},
     * makes sure a List is converted into a String properly
     */
    @SmallTest
    public void testGetDBValue(){
        assertEquals(converter.getDBValue(stringList), dbValueString);
    }

    /**
     * Tests {@link com.noahhuppert.reflect.database.converters.StringListConverter#getModelValue(String)},
     * makes sure a String is converted into a List properly
     */
    @SmallTest
    public void testGetModelValue(){
        assertEquals(converter.getModelValue(dbValueString), stringList);
    }
}
