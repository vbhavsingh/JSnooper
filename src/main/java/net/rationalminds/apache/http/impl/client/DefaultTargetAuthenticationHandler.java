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

import java.util.Map;

import net.rationalminds.apache.http.annotation.Immutable;
import net.rationalminds.apache.http.auth.AUTH;
import net.rationalminds.apache.http.auth.MalformedChallengeException;
import net.rationalminds.apache.http.client.AuthenticationHandler;
import net.rationalminds.core.apache.http.Header;
import net.rationalminds.core.apache.http.HttpResponse;
import net.rationalminds.core.apache.http.HttpStatus;
import net.rationalminds.core.apache.http.protocol.HttpContext;

/**
 * Default {@link AuthenticationHandler} implementation for target host 
 * authentication.
 * 
 * @since 4.0
 */
@Immutable 
public class DefaultTargetAuthenticationHandler extends AbstractAuthenticationHandler {

    public DefaultTargetAuthenticationHandler() {
        super();
    }
    
    public boolean isAuthenticationRequested(
            final HttpResponse response, 
            final HttpContext context) {
        if (response == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        }
        int status = response.getStatusLine().getStatusCode();
        return status == HttpStatus.SC_UNAUTHORIZED;
    }

    public Map<String, Header> getChallenges(
            final HttpResponse response, 
            final HttpContext context) throws MalformedChallengeException {
        if (response == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        }
        Header[] headers = response.getHeaders(AUTH.WWW_AUTH);
        return parseChallenges(headers);
    }

}
