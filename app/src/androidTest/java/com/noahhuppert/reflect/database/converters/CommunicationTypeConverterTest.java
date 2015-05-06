package com.noahhuppert.reflect.database.converters;

import android.test.suitebuilder.annotation.SmallTest;

import com.noahhuppert.reflect.messaging.CommunicationType;

import junit.framework.TestCase;

/**
 * Tests the {@link CommunicationTypeConverter}
 */
public class CommunicationTypeConverterTest extends TestCase{
    CommunicationTypeConverter converter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        converter = new CommunicationTypeConverter();
    }

    /**
     * Tests {@link com.noahhuppert.reflect.database.converters.CommunicationTypeConverter#getDBValue(com.noahhuppert.reflect.messaging.CommunicationType)},
     * makes sure a CommunicationType is converted into a String properly
     */
    @SmallTest
    public void testGetDBValue(){
        for(CommunicationType t : CommunicationType.values()){
            assertEquals(converter.getDBValue(t), t.toString());
        }
    }

    /**
     * Tests {@link com.noahhuppert.reflect.database.converters.CommunicationTypeConverter#getModelValue(String)},
     * makes sure a String is converted into a CommunicationType
     */
    @SmallTest
    public void testGetModelValue(){
        for(CommunicationType t : CommunicationType.values()){
            assertEquals(converter.getModelValue(t.toString()), t);
        }
    }
}
