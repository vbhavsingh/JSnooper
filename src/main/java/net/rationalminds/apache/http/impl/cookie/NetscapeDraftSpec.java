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

package net.rationalminds.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.List;

import net.rationalminds.apache.http.annotation.NotThreadSafe;
import net.rationalminds.apache.http.cookie.ClientCookie;
import net.rationalminds.apache.http.cookie.Cookie;
import net.rationalminds.apache.http.cookie.CookieOrigin;
import net.rationalminds.apache.http.cookie.CookieSpec;
import net.rationalminds.apache.http.cookie.MalformedCookieException;
import net.rationalminds.apache.http.cookie.SM;
import net.rationalminds.core.apache.http.FormattedHeader;
import net.rationalminds.core.apache.http.Header;
import net.rationalminds.core.apache.http.HeaderElement;
import net.rationalminds.core.apache.http.message.BufferedHeader;
import net.rationalminds.core.apache.http.message.ParserCursor;
import net.rationalminds.core.apache.http.util.CharArrayBuffer;

/**
 * This {@link CookieSpec} implementation conforms to the original draft 
 * specification published by Netscape Communications. It should be avoided 
 * unless absolutely necessary for compatibility with legacy code.
 * 
 * @since 4.0 
 */
@NotThreadSafe // superclass is @NotThreadSafe
public class NetscapeDraftSpec extends CookieSpecBase {

    protected static final String EXPIRES_PATTERN = "EEE, dd-MMM-yyyy HH:mm:ss z";
    
    private final String[] datepatterns; 
    
    /** Default constructor */
    public NetscapeDraftSpec(final String[] datepatterns) {
        super();
        if (datepatterns != null) {
            this.datepatterns = datepatterns.clone();
        } else {
            this.datepatterns = new String[] { EXPIRES_PATTERN };
        }
        registerAttribHandler(ClientCookie.PATH_ATTR, new BasicPathHandler());
        registerAttribHandler(ClientCookie.DOMAIN_ATTR, new NetscapeDomainHandler());
        registerAttribHandler(ClientCookie.MAX_AGE_ATTR, new BasicMaxAgeHandler());
        registerAttribHandler(ClientCookie.SECURE_ATTR, new BasicSecureHandler());
        registerAttribHandler(ClientCookie.COMMENT_ATTR, new BasicCommentHandler());
        registerAttribHandler(ClientCookie.EXPIRES_ATTR, new BasicExpiresHandler(
                this.datepatterns));
    }

    /** Default constructor */
    public NetscapeDraftSpec() {
        this(null);
    }
    
    /**
      * Parses the Set-Cookie value into an array of <tt>Cookie</tt>s.
      *
      * <p>Syntax of the Set-Cookie HTTP Response Header:</p>
      * 
      * <p>This is the format a CGI script would use to add to 
      * the HTTP headers a new piece of data which is to be stored by 
      * the client for later retrieval.</p>
      *  
      * <PRE>
      *  Set-Cookie: NAME=VALUE; expires=DATE; path=PATH; domain=DOMAIN_NAME; secure
      * </PRE>
      *
      * <p>Please note that Netscape draft specification does not fully 
      * conform to the HTTP header format. Netscape draft does not specify 
      * whether multiple cookies may be sent in one header. Hence, comma 
      * character may be present in unquoted cookie value or unquoted 
      * parameter value.</p>
      * 
      * @see <a href="http://wp.netscape.com/newsref/std/cookie_spec.html">
      *  The Cookie Spec.</a>
      *
      * @param header the <tt>Set-Cookie</tt> received from the server
      * @return an array of <tt>Cookie</tt>s parsed from the Set-Cookie value
      * @throws MalformedCookieException if an exception occurs during parsing
      */
    public List<Cookie> parse(final Header header, final CookieOrigin origin) 
            throws MalformedCookieException {
        if (header == null) {
            throw new IllegalArgumentException("Header may not be null");
        }
        if (origin == null) {
            throw new IllegalArgumentException("Cookie origin may not be null");
        }
        if (!header.getName().equalsIgnoreCase(SM.SET_COOKIE)) {
            throw new MalformedCookieException("Unrecognized cookie header '"
                    + header.toString() + "'");
        }
        NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
        CharArrayBuffer buffer;
        ParserCursor cursor;
        if (header instanceof FormattedHeader) {
            buffer = ((FormattedHeader) header).getBuffer();
            cursor = new ParserCursor(
                    ((FormattedHeader) header).getValuePos(), 
                    buffer.length());
        } else {
            String s = header.getValue();
            if (s == null) {
                throw new MalformedCookieException("Header value is null");
            }
            buffer = new CharArrayBuffer(s.length());
            buffer.append(s);
            cursor = new ParserCursor(0, buffer.length());
        }
        return parse(new HeaderElement[] { parser.parseHeader(buffer, cursor) }, origin);
    }

    public List<Header> formatCookies(final List<Cookie> cookies) {
        if (cookies == null) {
            throw new IllegalArgumentException("List of cookies may not be null");
        }
        if (cookies.isEmpty()) {
            throw new IllegalArgumentException("List of cookies may not be empty");
        }
        CharArrayBuffer buffer = new CharArrayBuffer(20 * cookies.size());
        buffer.append(SM.COOKIE);
        buffer.append(": ");
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            if (i > 0) {
                buffer.append("; ");
            }
            buffer.append(cookie.getName());
            String s = cookie.getValue();
            if (s != null) {
                buffer.append("=");
                buffer.append(s);
            }
        }
        List<Header> headers = new ArrayList<Header>(1);
        headers.add(new BufferedHeader(buffer));
        return headers;
    }

    public int getVersion() {
        return 0;
    }

    public Header getVersionHeader() {
        return null;
    }
    
    @Override
    public String toString() {
        return "netscape";
    }
    
}
