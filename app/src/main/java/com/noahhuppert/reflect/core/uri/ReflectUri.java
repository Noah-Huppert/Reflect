package com.noahhuppert.reflect.core.uri;

import com.noahhuppert.reflect.core.CommunicationType;

/**
 * Stores information about contacting a person.
 *
 * If the Uri type is XMPP it will follow the format:
 *      username:password@host:port
 *
 * If the Uri type is SMS it will follow the format:
 *      cellphoneNumber
 */
public class ReflectUri {
    /**
     * The Uri String
     */
    private String uri;

    public ReflectUri(String uri) {
        this.uri = uri;
    }

    public ReflectUri(XMPPReflectUri uri){
        this.uri = uri.toString();
    }

    /* Actions */
    public XMPPReflectUri parseAsXMPP(){
        try {
            return XMPPReflectUri.Parse(getUri());
        } catch(RuntimeException e){
            return null;
        }
    }

    /* Getters */
    public String getUri() {
        return uri;
    }

    /* Setters */
    public void setUri(String uri) {
        this.uri = uri;
    }
}
