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

import net.rationalminds.apache.http.annotation.Immutable;
import net.rationalminds.apache.http.auth.AUTH;
import net.rationalminds.apache.http.auth.AuthScheme;
import net.rationalminds.apache.http.auth.AuthState;
import net.rationalminds.apache.http.auth.AuthenticationException;
import net.rationalminds.apache.http.auth.Credentials;
import net.rationalminds.core.apache.http.HttpException;
import net.rationalminds.core.apache.http.HttpRequest;
import net.rationalminds.core.apache.http.HttpRequestInterceptor;
import net.rationalminds.core.apache.http.protocol.HttpContext;

/**
 * Generates authentication header for the target host, if required, 
 * based on the actual state of the HTTP authentication context.   
 * 
 * @since 4.0
 */
@Immutable
public class RequestTargetAuthentication implements HttpRequestInterceptor {

    
    public RequestTargetAuthentication() {
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
        
        if (request.containsHeader(AUTH.WWW_AUTH_RESP)) {
            return;
        }
        
        // Obtain authentication state
        AuthState authState = (AuthState) context.getAttribute(
                ClientContext.TARGET_AUTH_STATE);
        if (authState == null) {
            return;
        }

        AuthScheme authScheme = authState.getAuthScheme();
        if (authScheme == null) {
            return;
        }
        
        Credentials creds = authState.getCredentials();
        if (creds == null) {
         //   this.log.debug("User credentials not available");
            return;
        }

        if (authState.getAuthScope() != null || !authScheme.isConnectionBased()) {
            try {
                request.addHeader(authScheme.authenticate(creds, request));
            } catch (AuthenticationException ex) {
               //do nothing
            }
        }
    }
    
}
