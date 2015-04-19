package com.noahhuppert.reflect.core.uri;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for storing information about XMPP type Uris
 *
 * Takes strings with the format of:
 *      username:password@host:port
 */
public class XMPPReflectUri {
    /**
     * Default XMPP port
     */
    public static final Integer DEFAULT_PORT = 5222;

    /**
     * Username in Uri
     */
    private String username;

    /**
     * Password in uri
     */
    private String password;

    /**
     * Host in uri
     */
    private String host;

    /**
     * Port in uri
     */
    private Integer port;

    /**
     * Manual constructor
     * @param username Username in Uri
     * @param password Password in Uri
     * @param host Host in Uri
     * @param port Port in Uri
     */
    public XMPPReflectUri(String username, String password, String host, Integer port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port != null ? port : DEFAULT_PORT;
    }

    public XMPPReflectUri(XMPPReflectUri xmppReflectUri){
        username = xmppReflectUri.getUsername();
        password = xmppReflectUri.getPassword();
        host = xmppReflectUri.getHost();
        port = xmppReflectUri.getPort();
    }

    /**
     * Constructor that parses a Uri string
     * @param uri Uri string to be parsed
     */
    public XMPPReflectUri(String uri) throws InvalidXMPPUriException {
        this(Parse(uri));
    }

    /* Actions */
    @Override
    public String toString() {
        String outPassword = getPassword() != null ? ":" + getPassword() : "";

        return getUsername() + outPassword + "@" + getHost() + ":" + getPort();
    }

    public static XMPPReflectUri Parse(String uri) throws InvalidXMPPUriException{
        XMPPReflectUri xmppReflectUri = new XMPPReflectUri(null, null, null, null);

        String[] uriPartsArray = uri.split("((?<=[:@])|(?=[:@]))");//Splits into [username, :, password, @, host, :, port]
        ArrayList<String> uriParts = new ArrayList<String>(Arrays.asList(uriPartsArray));

        /*
        Valid:
            username:password@host:port
            username:password@host
            username@host
            username@host:port
         */
        int partsSize = uriParts.size() - 1;
        int colinCount = StringUtils.countMatches(uri, ":");
        int atCount = StringUtils.countMatches(uri, "@");
        if(atCount != 1 || (partsSize < 2 || partsSize > 6) || colinCount > 1){
            throw new InvalidXMPPUriException("Invalid XMPP uri");
        }

        String prevPart = "";
        int i = 0;
        for(String part : uriParts){
            if(i == 0){
                xmppReflectUri.setUsername(part);
            } else if(i == 2 && prevPart.equals(":")){
                xmppReflectUri.setPassword(part);
            } else if((i == 2 || i == 4) && prevPart.equals("@")){
                xmppReflectUri.setHost(part);
            } else if(i == 6 && prevPart.equals(":")){
                try {
                    xmppReflectUri.setPort(Integer.parseInt(part));
                } catch(NumberFormatException e){}
            }

            prevPart = part;
            i++;
        }

        return xmppReflectUri;
    }

    /* Getters */
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    /**
     * Gets port or returns default port if null
     * @return Port or default port if port is null
     */
    public Integer getPort() {
        if(port == null){
            return DEFAULT_PORT;
        }

        return port;
    }

    /* Setters */
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
