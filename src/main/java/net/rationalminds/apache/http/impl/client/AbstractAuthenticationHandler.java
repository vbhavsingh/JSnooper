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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.rationalminds.apache.http.annotation.Immutable;
import net.rationalminds.apache.http.auth.AuthScheme;
import net.rationalminds.apache.http.auth.AuthSchemeRegistry;
import net.rationalminds.apache.http.auth.AuthenticationException;
import net.rationalminds.apache.http.auth.MalformedChallengeException;
import net.rationalminds.apache.http.client.AuthenticationHandler;
import net.rationalminds.apache.http.client.protocol.ClientContext;
import net.rationalminds.core.apache.http.FormattedHeader;
import net.rationalminds.core.apache.http.Header;
import net.rationalminds.core.apache.http.HttpResponse;
import net.rationalminds.core.apache.http.protocol.HTTP;
import net.rationalminds.core.apache.http.protocol.HttpContext;
import net.rationalminds.core.apache.http.util.CharArrayBuffer;

/**
 * Base class for {@link AuthenticationHandler} implementations.
 *
 * @since 4.0
 */
@Immutable 
public abstract class AbstractAuthenticationHandler implements AuthenticationHandler {

    
    private static final List<String> DEFAULT_SCHEME_PRIORITY = 
        Collections.unmodifiableList(Arrays.asList(new String[] {
            "ntlm",
            "digest",
            "basic"
    }));
    
    public AbstractAuthenticationHandler() {
        super();
    }
    
    protected Map<String, Header> parseChallenges(
            final Header[] headers) throws MalformedChallengeException {
        
        Map<String, Header> map = new HashMap<String, Header>(headers.length);
        for (Header header : headers) {
            CharArrayBuffer buffer;
            int pos;
            if (header instanceof FormattedHeader) {
                buffer = ((FormattedHeader) header).getBuffer();
                pos = ((FormattedHeader) header).getValuePos();
            } else {
                String s = header.getValue();
                if (s == null) {
                    throw new MalformedChallengeException("Header value is null");
                }
                buffer = new CharArrayBuffer(s.length());
                buffer.append(s);
                pos = 0;
            }
            while (pos < buffer.length() && HTTP.isWhitespace(buffer.charAt(pos))) {
                pos++;
            }
            int beginIndex = pos;
            while (pos < buffer.length() && !HTTP.isWhitespace(buffer.charAt(pos))) {
                pos++;
            }
            int endIndex = pos;
            String s = buffer.substring(beginIndex, endIndex);
            map.put(s.toLowerCase(Locale.ENGLISH), header);
        }
        return map;
    }
    
    protected List<String> getAuthPreferences() {
        return DEFAULT_SCHEME_PRIORITY;
    }
    
	public AuthScheme selectScheme(
            final Map<String, Header> challenges, 
            final HttpResponse response,
            final HttpContext context) throws AuthenticationException {
        
        AuthSchemeRegistry registry = (AuthSchemeRegistry) context.getAttribute(
                ClientContext.AUTHSCHEME_REGISTRY);
        if (registry == null) {
            throw new IllegalStateException("AuthScheme registry not set in HTTP context");
        }
        
        @SuppressWarnings("unchecked")
        Collection<String> authPrefs = (Collection<String>) context.getAttribute(
                ClientContext.AUTH_SCHEME_PREF);
        if (authPrefs == null) {
            authPrefs = getAuthPreferences();
        }
        
//        if (this.log.isDebugEnabled()) {
//            this.log.debug("Authentication schemes in the order of preference: "
//                + authPrefs);
//        }

        AuthScheme authScheme = null;
        for (String id: authPrefs) {
            Header challenge = challenges.get(id.toLowerCase(Locale.ENGLISH)); 

            if (challenge != null) {
//                if (this.log.isDebugEnabled()) {
//                    this.log.debug(id + " authentication scheme selected");
//                }
                try {
                    authScheme = registry.getAuthScheme(id, response.getParams());
                    break;
                } catch (IllegalStateException e) {
//                    if (this.log.isWarnEnabled()) {
//                        this.log.warn("Authentication scheme " + id + " not supported");
//                        // Try again
//                    }
                }
            } else {
//                if (this.log.isDebugEnabled()) {
//                    this.log.debug("Challenge for " + id + " authentication scheme not available");
//                    // Try again
//                }
            }
        }
        if (authScheme == null) {
            // If none selected, something is wrong
            throw new AuthenticationException(
                "Unable to respond to any of these challenges: "
                    + challenges);
        }
        return authScheme;
    }

}
