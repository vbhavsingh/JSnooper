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
import java.util.Collection;

import net.rationalminds.apache.http.annotation.Immutable;
import net.rationalminds.apache.http.client.params.ClientPNames;
import net.rationalminds.core.apache.http.Header;
import net.rationalminds.core.apache.http.HttpException;
import net.rationalminds.core.apache.http.HttpRequest;
import net.rationalminds.core.apache.http.HttpRequestInterceptor;
import net.rationalminds.core.apache.http.protocol.HttpContext;

/**
 * Request interceptor that adds default request headers.
 * 
 * @since 4.0
 */
@Immutable
public class RequestDefaultHeaders implements HttpRequestInterceptor {

    public RequestDefaultHeaders() {
        super();
    }
    
    public void process(final HttpRequest request, final HttpContext context) 
            throws HttpException, IOException {
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        
        String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")) {
            return;
        }
        
        // Add default headers
        @SuppressWarnings("unchecked")
        Collection<Header> defHeaders = (Collection<Header>) request.getParams().getParameter(
                ClientPNames.DEFAULT_HEADERS);

        if (defHeaders != null) {
            for (Header defHeader : defHeaders) {
                request.addHeader(defHeader);
            }
        }
    }
    
}
