package com.noahhuppert.reflect.uri;

import android.net.Uri;

import com.noahhuppert.reflect.exceptions.InvalidUriException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.MessagingResourceType;

import java.net.URI;

/**
 * A class holding many utilities for deconstructing a URI using the messaging scheme
 */
public class MessagingUriUtils {
    /**
     * Checks to make sure that the provided URI contains all the necessary parts of a URI using the
     * messaging scheme
     *
     * For a URI to be considered valid the following must be true
     *      - URI uses "messaging" scheme
     *      - A resource type is provided, the user info part of the URI
     *      - A resource provider if provided, the host part of the URI
     *      - The path is not empty
     *
     * This method does not check to make sure that the resource type or provider is a valid value.
     * To completely check use {@link com.noahhuppert.reflect.uri.MessagingUriUtils#CheckForValidMessagingUri(java.net.URI)}.
     * The reason behind this is that this method is used to make sure that the general format is
     * correct so that methods like {@link com.noahhuppert.reflect.uri.MessagingUriUtils#GetResourceProvider(java.net.URI)}
     * can get required parts of the URI without checking if they exist. {@link com.noahhuppert.reflect.uri.MessagingUriUtils#CheckForValidMessagingUri(java.net.URI)}
     * checks to make sure that the values are valid
     *
     * @param uri The URI to check
     * @throws InvalidUriException Thrown if the the URI is not valid
     */
    public static void SoftCheckForValidMessagingUri(URI uri) throws InvalidUriException{
        if(!uri.getScheme().equals(MessagingUriBuilder.MESSAGING_URI_SCHEME)){
            throw new InvalidUriException("The URI provided must use the messaging scheme", uri.toString());
        }

        if(uri.getUserInfo() == null || uri.getUserInfo().isEmpty()){
            throw new InvalidUriException("The URI provided must contain a resource type, in the user info part of a URI", uri.toString());
        }

        if(uri.getHost() == null || uri.getHost().isEmpty()){
            throw new InvalidUriException("The URI provided must contain a resource provider, in the host part of the URI", uri.toString());
        }

        if(uri.getPath() == null || uri.getPath().isEmpty()){
            throw new InvalidUriException("The URI provided must contain a valid path", uri.toString());
        }
    }

    /**
     * Checks to make sure the provided URI is valid. To be valid it must meet the requirements of
     * {@link com.noahhuppert.reflect.uri.MessagingUriUtils#SoftCheckForValidMessagingUri(java.net.URI)}
     * and the values of the resource type and provider must be valid
     *
     * @param uri The URI to check
     * @throws InvalidUriException Thrown if the URI is invalid
     */
    public static void CheckForValidMessagingUri(URI uri) throws InvalidUriException {
        SoftCheckForValidMessagingUri(uri);

        GetResourceType(uri);

        GetResourceProvider(uri);
    }

    /**
     * Gets the Enum value of the resource type in the URI
     * @param uri The URI to get the resource type from
     * @return The Enum value of the resource type
     * @throws InvalidUriException Throws if the URI is not valid
     */
    public static MessagingResourceType GetResourceType(URI uri) throws InvalidUriException{
        SoftCheckForValidMessagingUri(uri);

        String resourceTypeURIString = uri.getUserInfo().toUpperCase();

        try{
            return MessagingResourceType.valueOf(resourceTypeURIString);
        } catch(IllegalArgumentException e){
            throw new InvalidUriException("The URI provided does not contain a valid resource type", uri.toString());
        }
    }

    /**
     * Gets the Enum value of the resource provider in the URI
     * @param uri The URI to get the resource provider from
     * @return The Enum value of the resource provider
     * @throws InvalidUriException Thrown if the URI is not valid
     */
    public static CommunicationType GetResourceProvider(URI uri) throws InvalidUriException{
        SoftCheckForValidMessagingUri(uri);

        String resourceProviderURIString = uri.getHost().toUpperCase();

        try {
            return CommunicationType.valueOf(resourceProviderURIString);
        } catch (IllegalArgumentException e){
            throw new InvalidUriException("The URI provided does not contain a valid resource provider", uri.toString());
        }
    }

    public static Uri ToAndroidUri(URI uri){
        return Uri.parse(uri.toString());
    }
}
