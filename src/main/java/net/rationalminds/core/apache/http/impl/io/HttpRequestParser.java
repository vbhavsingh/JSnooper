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

package net.rationalminds.core.apache.http.impl.io;

import java.io.IOException;

import net.rationalminds.core.apache.http.HttpException;
import net.rationalminds.core.apache.http.HttpMessage;
import net.rationalminds.core.apache.http.HttpRequest;
import net.rationalminds.core.apache.http.HttpRequestFactory;
import net.rationalminds.core.apache.http.ParseException;
import net.rationalminds.core.apache.http.RequestLine;
import net.rationalminds.core.apache.http.io.SessionInputBuffer;
import net.rationalminds.core.apache.http.message.LineParser;
import net.rationalminds.core.apache.http.message.ParserCursor;
import net.rationalminds.core.apache.http.params.HttpParams;
import net.rationalminds.core.apache.http.util.CharArrayBuffer;

/**
 * HTTP request parser that obtain its input from an instance
 * of {@link SessionInputBuffer}.
 * <p>
 * The following parameters can be used to customize the behavior of this
 * class:
 * <ul>
 *  <li>{@link org.apache.http.params.CoreConnectionPNames#MAX_HEADER_COUNT}</li>
 *  <li>{@link org.apache.http.params.CoreConnectionPNames#MAX_LINE_LENGTH}</li>
 * </ul>
 *
 * @since 4.0
 */
public class HttpRequestParser extends AbstractMessageParser {

    private final HttpRequestFactory requestFactory;
    private final CharArrayBuffer lineBuf;

    /**
     * Creates an instance of this class.
     *
     * @param buffer the session input buffer.
     * @param parser the line parser.
     * @param requestFactory the factory to use to create
     *    {@link HttpRequest}s.
     * @param params HTTP parameters.
     */
    public HttpRequestParser(
            final SessionInputBuffer buffer,
            final LineParser parser,
            final HttpRequestFactory requestFactory,
            final HttpParams params) {
        super(buffer, parser, params);
        if (requestFactory == null) {
            throw new IllegalArgumentException("Request factory may not be null");
        }
        this.requestFactory = requestFactory;
        this.lineBuf = new CharArrayBuffer(128);
    }

    protected HttpMessage parseHead(
            final SessionInputBuffer sessionBuffer)
        throws IOException, HttpException, ParseException {

        this.lineBuf.clear();
        int i = sessionBuffer.readLine(this.lineBuf);
        if (i == -1) {
            throw new net.rationalminds.core.apache.http.ConnectionClosedException("Client closed connection");
        }
        ParserCursor cursor = new ParserCursor(0, this.lineBuf.length());
        RequestLine requestline = this.lineParser.parseRequestLine(this.lineBuf, cursor);
        return this.requestFactory.newHttpRequest(requestline);
    }

}
