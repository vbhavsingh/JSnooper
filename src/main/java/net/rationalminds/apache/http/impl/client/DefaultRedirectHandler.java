/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package net.rationalminds.apache.http.impl.client;

import java.net.URI;
import java.net.URISyntaxException;

import net.rationalminds.apache.http.annotation.Immutable;
import net.rationalminds.apache.http.client.CircularRedirectException;
import net.rationalminds.apache.http.client.RedirectHandler;
import net.rationalminds.apache.http.client.methods.HttpGet;
import net.rationalminds.apache.http.client.methods.HttpHead;
import net.rationalminds.apache.http.client.params.ClientPNames;
import net.rationalminds.apache.http.client.utils.URIUtils;
import net.rationalminds.core.apache.http.Header;
import net.rationalminds.core.apache.http.HttpHost;
import net.rationalminds.core.apache.http.HttpRequest;
import net.rationalminds.core.apache.http.HttpResponse;
import net.rationalminds.core.apache.http.HttpStatus;
import net.rationalminds.core.apache.http.ProtocolException;
import net.rationalminds.core.apache.http.params.HttpParams;
import net.rationalminds.core.apache.http.protocol.ExecutionContext;
import net.rationalminds.core.apache.http.protocol.HttpContext;

/**
 * Default implementation of {@link RedirectHandler}.
 *
 * @since 4.0
 */
@Immutable
public class DefaultRedirectHandler implements RedirectHandler {

    
    private static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";

    public DefaultRedirectHandler() {
        super();
    }
    
    public boolean isRedirectRequested(
            final HttpResponse response,
            final HttpContext context) {
        if (response == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        }
        
        int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
        case HttpStatus.SC_MOVED_TEMPORARILY:
        case HttpStatus.SC_MOVED_PERMANENTLY:
        case HttpStatus.SC_TEMPORARY_REDIRECT:
            HttpRequest request = (HttpRequest) context.getAttribute(
                    ExecutionContext.HTTP_REQUEST);
            String method = request.getRequestLine().getMethod();
            return method.equalsIgnoreCase(HttpGet.METHOD_NAME) 
                || method.equalsIgnoreCase(HttpHead.METHOD_NAME);
        case HttpStatus.SC_SEE_OTHER:
            return true;
        default:
            return false;
        } //end of switch
    }
 
    public URI getLocationURI(
            final HttpResponse response, 
            final HttpContext context) throws ProtocolException {
        if (response == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        }
        //get the location header to find out where to redirect to
        Header locationHeader = response.getFirstHeader("location");
        if (locationHeader == null) {
            // got a redirect response, but no location header
            throw new ProtocolException(
                    "Received redirect response " + response.getStatusLine()
                    + " but no location header");
        }
        String location = locationHeader.getValue();

        URI uri;
        try {
            uri = new URI(location);            
        } catch (URISyntaxException ex) {
            throw new ProtocolException("Invalid redirect URI: " + location, ex);
        }

        HttpParams params = response.getParams();
        // rfc2616 demands the location value be a complete URI
        // Location       = "Location" ":" absoluteURI
        if (!uri.isAbsolute()) {
            if (params.isParameterTrue(ClientPNames.REJECT_RELATIVE_REDIRECT)) {
                throw new ProtocolException("Relative redirect location '" 
                        + uri + "' not allowed");
            }
            // Adjust location URI
            HttpHost target = (HttpHost) context.getAttribute(
                    ExecutionContext.HTTP_TARGET_HOST);
            if (target == null) {
                throw new IllegalStateException("Target host not available " +
                        "in the HTTP context");
            }
            
            HttpRequest request = (HttpRequest) context.getAttribute(
                    ExecutionContext.HTTP_REQUEST);
            
            try {
                URI requestURI = new URI(request.getRequestLine().getUri());
                URI absoluteRequestURI = URIUtils.rewriteURI(requestURI, target, true);
                uri = URIUtils.resolve(absoluteRequestURI, uri); 
            } catch (URISyntaxException ex) {
                throw new ProtocolException(ex.getMessage(), ex);
            }
        }
        
        if (params.isParameterFalse(ClientPNames.ALLOW_CIRCULAR_REDIRECTS)) {
            
            RedirectLocations redirectLocations = (RedirectLocations) context.getAttribute(
                    REDIRECT_LOCATIONS);
            
            if (redirectLocations == null) {
                redirectLocations = new RedirectLocations();
                context.setAttribute(REDIRECT_LOCATIONS, redirectLocations);
            }
            
            URI redirectURI;
            if (uri.getFragment() != null) {
                try {
                    HttpHost target = new HttpHost(
                            uri.getHost(), 
                            uri.getPort(),
                            uri.getScheme());
                    redirectURI = URIUtils.rewriteURI(uri, target, true);
                } catch (URISyntaxException ex) {
                    throw new ProtocolException(ex.getMessage(), ex);
                }
            } else {
                redirectURI = uri;
            }
            
            if (redirectLocations.contains(redirectURI)) {
                throw new CircularRedirectException("Circular redirect to '" +
                        redirectURI + "'");
            } else {
                redirectLocations.add(redirectURI);
            }
        }
        
        return uri;
    }

}
