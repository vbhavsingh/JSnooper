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



import net.rationalminds.apache.http.annotation.Immutable;
import net.rationalminds.apache.http.cookie.Cookie;
import net.rationalminds.apache.http.cookie.CookieAttributeHandler;
import net.rationalminds.apache.http.cookie.CookieOrigin;
import net.rationalminds.apache.http.cookie.MalformedCookieException;
import net.rationalminds.apache.http.cookie.SetCookie;
import net.rationalminds.apache.http.cookie.SetCookie2;

/**
 * <tt>"CommentURL"</tt> cookie attribute handler for RFC 2965 cookie spec.
 *
 * @since 4.0
 */
@Immutable
public class RFC2965CommentUrlAttributeHandler implements CookieAttributeHandler {

      public RFC2965CommentUrlAttributeHandler() {
          super();
      }
      
      public void parse(final SetCookie cookie, final String commenturl)
              throws MalformedCookieException {
          if (cookie instanceof SetCookie2) {
              SetCookie2 cookie2 = (SetCookie2) cookie;
              cookie2.setCommentURL(commenturl);
          }
      }

      public void validate(final Cookie cookie, final CookieOrigin origin)
              throws MalformedCookieException {
      }

      public boolean match(final Cookie cookie, final CookieOrigin origin) {
          return true;
      }
      
  }