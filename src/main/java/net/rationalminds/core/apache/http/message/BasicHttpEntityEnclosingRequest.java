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

package net.rationalminds.core.apache.http.message;

import net.rationalminds.core.apache.http.Header;
import net.rationalminds.core.apache.http.HttpEntity;
import net.rationalminds.core.apache.http.HttpEntityEnclosingRequest;
import net.rationalminds.core.apache.http.ProtocolVersion;
import net.rationalminds.core.apache.http.RequestLine;
import net.rationalminds.core.apache.http.protocol.HTTP;

/**
 * Basic implementation of {@link HttpEntityEnclosingRequest}.
 *
 * @since 4.0
 */
public class BasicHttpEntityEnclosingRequest
            extends BasicHttpRequest implements HttpEntityEnclosingRequest {

    private HttpEntity entity;

    public BasicHttpEntityEnclosingRequest(final String method, final String uri) {
        super(method, uri);
    }

    public BasicHttpEntityEnclosingRequest(final String method, final String uri,
            final ProtocolVersion ver) {
        super(method, uri, ver);
    }

    public BasicHttpEntityEnclosingRequest(final RequestLine requestline) {
        super(requestline);
    }

    public HttpEntity getEntity() {
        return this.entity;
    }

    public void setEntity(final HttpEntity entity) {
        this.entity = entity;
    }

    public boolean expectContinue() {
        Header expect = getFirstHeader(HTTP.EXPECT_DIRECTIVE);
        return expect != null && HTTP.EXPECT_CONTINUE.equalsIgnoreCase(expect.getValue());
    }

}
