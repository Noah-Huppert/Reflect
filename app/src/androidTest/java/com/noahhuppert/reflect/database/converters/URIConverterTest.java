package com.noahhuppert.reflect.database.converters;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Tests the {@link URIConverter}
 */
public class URIConverterTest extends TestCase {
    URIConverter converter;
    String goodUriString;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        converter = new URIConverter();

        goodUriString = "scheme://username@host:port/path#fragment";
    }

    /**
     * Test {@link URIConverter#getDBValue(URI)}, makes sure the model gets turned into a string
     */
    @SmallTest
    public void testGetDBValue(){
        try {
            URI uri = new URI(goodUriString);
            assertEquals(converter.getDBValue(uri), goodUriString);
        } catch (URISyntaxException e){
            assertEquals(false, true);
        }
    }

    /**
     * Tests {@link URIConverter#getModelValue(String)}, makes sure the model gets turned into a URI
     * from a string
     */
    @SmallTest
    public void testGetModelValue(){
        try {
            URI goodUri = new URI(goodUriString);
            assertEquals(converter.getModelValue(goodUriString), goodUri);
        } catch (URISyntaxException e){
            assertEquals(false, true);
        }
    }
}
