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
import java.util.List;

import net.rationalminds.apache.http.annotation.Immutable;
import net.rationalminds.apache.http.client.CookieStore;
import net.rationalminds.apache.http.cookie.Cookie;
import net.rationalminds.apache.http.cookie.CookieOrigin;
import net.rationalminds.apache.http.cookie.CookieSpec;
import net.rationalminds.apache.http.cookie.MalformedCookieException;
import net.rationalminds.apache.http.cookie.SM;
import net.rationalminds.core.apache.http.Header;
import net.rationalminds.core.apache.http.HeaderIterator;
import net.rationalminds.core.apache.http.HttpException;
import net.rationalminds.core.apache.http.HttpResponse;
import net.rationalminds.core.apache.http.HttpResponseInterceptor;
import net.rationalminds.core.apache.http.protocol.HttpContext;

/**
 * Response interceptor that populates the current {@link CookieStore} with data 
 * contained in response cookies received in the given the HTTP response.
 * 
 * @since 4.0
 */
@Immutable
public class ResponseProcessCookies implements HttpResponseInterceptor {

    
    public ResponseProcessCookies() {
        super();
    }
    
    public void process(final HttpResponse response, final HttpContext context) 
            throws HttpException, IOException {
        if (response == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        }
        
        // Obtain actual CookieSpec instance
        CookieSpec cookieSpec = (CookieSpec) context.getAttribute(
                ClientContext.COOKIE_SPEC);
        if (cookieSpec == null) {
            return;
        }
        // Obtain cookie store
        CookieStore cookieStore = (CookieStore) context.getAttribute(
                ClientContext.COOKIE_STORE);
        if (cookieStore == null) {
           // this.log.info("CookieStore not available in HTTP context");
            return;
        }
        // Obtain actual CookieOrigin instance
        CookieOrigin cookieOrigin = (CookieOrigin) context.getAttribute(
                ClientContext.COOKIE_ORIGIN);
        if (cookieOrigin == null) {
         //   this.log.info("CookieOrigin not available in HTTP context");
            return;
        }
        HeaderIterator it = response.headerIterator(SM.SET_COOKIE);
        processCookies(it, cookieSpec, cookieOrigin, cookieStore);
        
        // see if the cookie spec supports cookie versioning.
        if (cookieSpec.getVersion() > 0) {
            // process set-cookie2 headers.
            // Cookie2 will replace equivalent Cookie instances
            it = response.headerIterator(SM.SET_COOKIE2);
            processCookies(it, cookieSpec, cookieOrigin, cookieStore);
        }
    }
     
    private void processCookies(
            final HeaderIterator iterator, 
            final CookieSpec cookieSpec,
            final CookieOrigin cookieOrigin,
            final CookieStore cookieStore) {
        while (iterator.hasNext()) {
            Header header = iterator.nextHeader();
            try {
                List<Cookie> cookies = cookieSpec.parse(header, cookieOrigin);
                for (Cookie cookie : cookies) {
                    try {
                        cookieSpec.validate(cookie, cookieOrigin);
                        cookieStore.addCookie(cookie);

                       
                    } catch (MalformedCookieException ex) {
                           
                    }
                }
            } catch (MalformedCookieException ex) {
               
            }
        }
    }
    
}
