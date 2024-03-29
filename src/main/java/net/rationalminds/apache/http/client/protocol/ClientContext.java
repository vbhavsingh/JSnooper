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

/**
 * {@link org.apache.http.protocol.HttpContext} attribute names for 
 * client side HTTP protocol processing.
 *
 * @since 4.0
 */
public interface ClientContext {
    
    /**
     * Attribute name of a {@link com.e2e.apache.http.conn.scheme.Scheme} 
     * object that represents the actual protocol scheme registry.
     */
    public static final String SCHEME_REGISTRY   = "http.scheme-registry"; 
    /**
     * Attribute name of a {@link com.e2e.apache.http.cookie.CookieSpecRegistry} 
     * object that represents the actual cookie specification registry.
     */
    public static final String COOKIESPEC_REGISTRY   = "http.cookiespec-registry"; 

    /**
     * Attribute name of a {@link com.e2e.apache.http.cookie.CookieSpec} 
     * object that represents the actual cookie specification.
     */
    public static final String COOKIE_SPEC           = "http.cookie-spec"; 

    /**
     * Attribute name of a {@link com.e2e.apache.http.cookie.CookieOrigin} 
     * object that represents the actual details of the origin server.
     */
    public static final String COOKIE_ORIGIN         = "http.cookie-origin"; 
    
    /**
     * Attribute name of a {@link com.e2e.apache.http.client.CookieStore} 
     * object that represents the actual cookie store.
     */
    public static final String COOKIE_STORE          = "http.cookie-store"; 

    /**
     * Attribute name of a {@link com.e2e.apache.http.auth.AuthSchemeRegistry} 
     * object that represents the actual authentication scheme registry.
     */
    public static final String AUTHSCHEME_REGISTRY   = "http.authscheme-registry"; 

    /**
     * Attribute name of a {@link com.e2e.apache.http.client.CredentialsProvider} 
     * object that represents the actual crednetials provider.
     */
    public static final String CREDS_PROVIDER        = "http.auth.credentials-provider"; 

    /**
     * Attribute name of a {@link com.e2e.apache.http.auth.AuthState} 
     * object that represents the actual target authentication state.
     */
    public static final String TARGET_AUTH_STATE     = "http.auth.target-scope"; 

    /**
     * Attribute name of a {@link com.e2e.apache.http.auth.AuthState} 
     * object that represents the actual proxy authentication state.
     */
    public static final String PROXY_AUTH_STATE      = "http.auth.proxy-scope";

    /**
     * RESERVED. DO NOT USE!!!
     */
    public static final String AUTH_SCHEME_PREF      = "http.auth.scheme-pref";
    
    /**
     * Attribute name of a {@link java.lang.Object} object that represents 
     * the actual user identity such as user {@link java.security.Principal}.
     */
    public static final String USER_TOKEN            = "http.user-token";
    
}
