package com.noahhuppert.reflect.uri;

import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.utils.EnumUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A class that provides static methods to help build messaging scheme style URIs
 */
public class MessagingUriBuilder {
    /**
     * The string to use for the scheme in the messaging scheme
     */
    public static final String MESSAGING_URI_SCHEME = "messaging";

    /**
     * Builds a messaging style URI by taking convenient enums and ids and assembling them
     * @param resourceType The type of resource to point to, the user info in the scheme
     * @param resourceProvider The provider of the resource to point to, the host in the
     *                         scheme
     * @param resourceId The id of the resource to point to, the path in the scheme
     * @return A built URI with the provided data
     * @throws URISyntaxException Thrown if the formatting of the URI is incorrect
     */
    public static URI Build(MessagingUriResourceType resourceType, MessagingUriResourceProvider resourceProvider, String resourceId) throws URISyntaxException{
        return new URI(MESSAGING_URI_SCHEME, EnumUtils.PartEnumToString(resourceType), EnumUtils.PartEnumToString(resourceProvider), -1, "/" + resourceId, null, null);
    }

    /**
     * Builds a messaging style URI for pointing to a XMPP contact
     * @param username The username of the XMPP contact
     * @param host The host of the XMPP contact
     * @param port The port of the XMPP contact
     * @return A build URI with the provided data
     * @throws URISyntaxException Thrown if the formatting of the URI is incorrect
     */
    public static URI BuildXMPPContact(String username, String host, int port) throws URISyntaxException{
        return Build(MessagingUriResourceType.CONTACT, MessagingUriResourceProvider.XMPP, host + ":" + port + "/" + username);
    }

    /**
     * Builds a messaging style URI for pointing to a XMPP contact from a jabber scheme URI
     * @param uri The Jabber scheme URI
     * @return A built URI with the provided data
     * @throws URISyntaxException Thrown if the formatting of the URI is incorrect
     * @throws InvalidUriException Thrown if the scheme in the provided URI is not "jabber"
     */
    public static URI BuildXMPPContact(URI uri) throws URISyntaxException, InvalidUriException {
        if(!uri.getScheme().equals("jabber")){
            throw new InvalidUriException("URI must use the jabber scheme", uri.toString());
        }

        String username = uri.getUserInfo();
        String host = uri.getHost();
        int port = uri.getPort();

        return BuildXMPPContact(username, host, port);
    }
}
