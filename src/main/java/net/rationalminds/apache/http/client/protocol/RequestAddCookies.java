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

package net.rationalminds.apache.http.client.protocol;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rationalminds.apache.http.annotation.Immutable;
import net.rationalminds.apache.http.client.CookieStore;
import net.rationalminds.apache.http.client.methods.HttpUriRequest;
import net.rationalminds.apache.http.client.params.HttpClientParams;
import net.rationalminds.apache.http.conn.ManagedClientConnection;
import net.rationalminds.apache.http.conn.scheme.Scheme;
import net.rationalminds.apache.http.conn.scheme.SchemeRegistry;
import net.rationalminds.apache.http.cookie.Cookie;
import net.rationalminds.apache.http.cookie.CookieOrigin;
import net.rationalminds.apache.http.cookie.CookieSpec;
import net.rationalminds.apache.http.cookie.CookieSpecRegistry;
import net.rationalminds.apache.http.cookie.SetCookie2;
import net.rationalminds.core.apache.http.Header;
import net.rationalminds.core.apache.http.HttpException;
import net.rationalminds.core.apache.http.HttpHost;
import net.rationalminds.core.apache.http.HttpRequest;
import net.rationalminds.core.apache.http.HttpRequestInterceptor;
import net.rationalminds.core.apache.http.ProtocolException;
import net.rationalminds.core.apache.http.protocol.ExecutionContext;
import net.rationalminds.core.apache.http.protocol.HttpContext;

/**
 * Request interceptor that matches cookies available in the current
 * {@link CookieStore} to the request being executed and generates 
 * corresponding <code>Cookie</code> request headers.
 * <p>
 * The following parameters can be used to customize the behavior of this 
 * class: 
 * <ul>
 *  <li>{@link com.e2e.apache.http.cookie.params.CookieSpecPNames#DATE_PATTERNS}</li>
 *  <li>{@link com.e2e.apache.http.cookie.params.CookieSpecPNames#SINGLE_COOKIE_HEADER}</li>
 *  <li>{@link com.e2e.apache.http.client.params.ClientPNames#COOKIE_POLICY}</li>
 * </ul>
 * 
 * @since 4.0
 */
@Immutable
public class RequestAddCookies implements HttpRequestInterceptor {

    
    public RequestAddCookies() {
        super();
    }
    
    public void process(final HttpRequest request, final HttpContext context) 
            throws HttpException, IOException {
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        }
        
        String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")) {
            return;
        }
        
        // Obtain cookie store
        CookieStore cookieStore = (CookieStore) context.getAttribute(
                ClientContext.COOKIE_STORE);
        if (cookieStore == null) {
           // this.log.info("Cookie store not available in HTTP context");
            return;
        }
        
        // Obtain the registry of cookie specs
        CookieSpecRegistry registry = (CookieSpecRegistry) context.getAttribute(
                ClientContext.COOKIESPEC_REGISTRY);
        if (registry == null) {
            //this.log.info("CookieSpec registry not available in HTTP context");
            return;
        }
        
        // Obtain the target host (required)
        HttpHost targetHost = (HttpHost) context.getAttribute(
                ExecutionContext.HTTP_TARGET_HOST);
        if (targetHost == null) {
            throw new IllegalStateException("Target host not specified in HTTP context");
        }
        
        // Obtain the client connection (required)
        ManagedClientConnection conn = (ManagedClientConnection) context.getAttribute(
                ExecutionContext.HTTP_CONNECTION);
        if (conn == null) {
            throw new IllegalStateException("Client connection not specified in HTTP context");
        }

        String policy = HttpClientParams.getCookiePolicy(request.getParams());

        
        URI requestURI;
        if (request instanceof HttpUriRequest) {
            requestURI = ((HttpUriRequest) request).getURI();
        } else {
            try {
                requestURI = new URI(request.getRequestLine().getUri());
            } catch (URISyntaxException ex) {
                throw new ProtocolException("Invalid request URI: " + 
                        request.getRequestLine().getUri(), ex);
            }
        }
        
        String hostName = targetHost.getHostName();
        int port = targetHost.getPort();
        if (port < 0) {

            // Obtain the scheme registry
            SchemeRegistry sr = (SchemeRegistry) context.getAttribute(
                    ClientContext.SCHEME_REGISTRY);
            if (sr != null) {
                Scheme scheme = sr.get(targetHost.getSchemeName());
                port = scheme.resolvePort(port);
            } else {
                port = conn.getRemotePort();
            }
        }
        
        CookieOrigin cookieOrigin = new CookieOrigin(
                hostName, 
                port, 
                requestURI.getPath(),
                conn.isSecure());
        
        // Get an instance of the selected cookie policy
        CookieSpec cookieSpec = registry.getCookieSpec(policy, request.getParams());
        // Get all cookies available in the HTTP state
        List<Cookie> cookies = new ArrayList<Cookie>(cookieStore.getCookies());
        // Find cookies matching the given origin
        List<Cookie> matchedCookies = new ArrayList<Cookie>();
        Date now = new Date();
        for (Cookie cookie : cookies) {
            if (!cookie.isExpired(now)) {
                if (cookieSpec.match(cookie, cookieOrigin)) {
                    
                    matchedCookies.add(cookie);
                }
            } else {
               //do nothing
            }
        }
        // Generate Cookie request headers
        if (!matchedCookies.isEmpty()) {
            List<Header> headers = cookieSpec.formatCookies(matchedCookies);
            for (Header header : headers) {
                request.addHeader(header);
            }
        }
        
        int ver = cookieSpec.getVersion();
        if (ver > 0) {
            boolean needVersionHeader = false;
            for (Cookie cookie : matchedCookies) {
                if (ver != cookie.getVersion() || !(cookie instanceof SetCookie2)) {
                    needVersionHeader = true;
                }
            }

            if (needVersionHeader) {
                Header header = cookieSpec.getVersionHeader();
                if (header != null) {
                    // Advertise cookie version support
                    request.addHeader(header);
                }
            }
        }
        
        // Stick the CookieSpec and CookieOrigin instances to the HTTP context
        // so they could be obtained by the response interceptor
        context.setAttribute(ClientContext.COOKIE_SPEC, cookieSpec);
        context.setAttribute(ClientContext.COOKIE_ORIGIN, cookieOrigin);
    }
    
}
