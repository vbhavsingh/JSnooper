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
import net.rationalminds.apache.http.conn.ManagedClientConnection;
import net.rationalminds.apache.http.conn.routing.HttpRoute;
import net.rationalminds.core.apache.http.HttpException;
import net.rationalminds.core.apache.http.HttpRequest;
import net.rationalminds.core.apache.http.HttpRequestInterceptor;
import net.rationalminds.core.apache.http.protocol.ExecutionContext;
import net.rationalminds.core.apache.http.protocol.HTTP;
import net.rationalminds.core.apache.http.protocol.HttpContext;

/**
 * This protocol interceptor is responsible for adding <code>Connection</code> 
 * or <code>Proxy-Connection</code> headers to the outgoing requests, which 
 * is essential for managing persistence of <code>HTTP/1.0</code> connections. 
 * 
 * @since 4.0
 */
@Immutable
public class RequestClientConnControl implements HttpRequestInterceptor {

    private static final String PROXY_CONN_DIRECTIVE = "Proxy-Connection";
    
    public RequestClientConnControl() {
        super();
    }
    
    public void process(final HttpRequest request, final HttpContext context) 
            throws HttpException, IOException {
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }

        String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")) {
            request.setHeader(PROXY_CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            return;
        }
        
        // Obtain the client connection (required)
        ManagedClientConnection conn = (ManagedClientConnection) context.getAttribute(
                ExecutionContext.HTTP_CONNECTION);
        if (conn == null) {
            throw new IllegalStateException("Client connection not specified in HTTP context");
        }

        HttpRoute route = conn.getRoute();
        
        if (route.getHopCount() == 1 || route.isTunnelled()) {
            if (!request.containsHeader(HTTP.CONN_DIRECTIVE)) {
                request.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            }
        }
        if (route.getHopCount() == 2 && !route.isTunnelled()) {
            if (!request.containsHeader(PROXY_CONN_DIRECTIVE)) {
                request.addHeader(PROXY_CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            }
        }
    }
    
}
