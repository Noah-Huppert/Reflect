package com.noahhuppert.reflect.core.uri;

import com.noahhuppert.reflect.core.messaging.CommunicationType;

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
    @Override
    public String toString() {
        try{
            return asXMPPUri().toString();
        } catch (InvalidXMPPUriException e){
            return getUri();
        }
    }

    public CommunicationType getUriType(){
        return null;
    }

    public XMPPReflectUri asXMPPUri() throws InvalidXMPPUriException{
        return XMPPReflectUri.Parse(getUri());
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
