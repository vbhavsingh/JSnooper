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

package net.rationalminds.core.apache.http.entity;

import java.io.IOException;
import java.io.OutputStream;

import net.rationalminds.core.apache.http.Header;
import net.rationalminds.core.apache.http.HttpEntity;
import net.rationalminds.core.apache.http.message.BasicHeader;
import net.rationalminds.core.apache.http.protocol.HTTP;

/**
 * Abstract base class for entities.
 * Provides the commonly used attributes for streamed and self-contained
 * implementations of {@link HttpEntity HttpEntity}.
 *
 * @since 4.0
 */
public abstract class AbstractHttpEntity implements HttpEntity {

    protected Header contentType;
    protected Header contentEncoding;
    protected boolean chunked;

    /**
     * Protected default constructor.
     * The attributes of the created object remain
     * <code>null</code> and <code>false</code>, respectively.
     */
    protected AbstractHttpEntity() {
        super();
    }


    /**
     * Obtains the Content-Type header.
     * The default implementation returns the value of the
     * {@link #contentType contentType} attribute.
     *
     * @return  the Content-Type header, or <code>null</code>
     */
    public Header getContentType() {
        return this.contentType;
    }


    /**
     * Obtains the Content-Encoding header.
     * The default implementation returns the value of the
     * {@link #contentEncoding contentEncoding} attribute.
     *
     * @return  the Content-Encoding header, or <code>null</code>
     */
    public Header getContentEncoding() {
        return this.contentEncoding;
    }

    /**
     * Obtains the 'chunked' flag.
     * The default implementation returns the value of the
     * {@link #chunked chunked} attribute.
     *
     * @return  the 'chunked' flag
     */
    public boolean isChunked() {
        return this.chunked;
    }


    /**
     * Specifies the Content-Type header.
     * The default implementation sets the value of the
     * {@link #contentType contentType} attribute.
     *
     * @param contentType       the new Content-Encoding header, or
     *                          <code>null</code> to unset
     */
    public void setContentType(final Header contentType) {
        this.contentType = contentType;
    }

    /**
     * Specifies the Content-Type header, as a string.
     * The default implementation calls
     * {@link #setContentType(Header) setContentType(Header)}.
     *
     * @param ctString     the new Content-Type header, or
     *                     <code>null</code> to unset
     */
    public void setContentType(final String ctString) {
        Header h = null;
        if (ctString != null) {
            h = new BasicHeader(HTTP.CONTENT_TYPE, ctString);
        }
        setContentType(h);
    }


    /**
     * Specifies the Content-Encoding header.
     * The default implementation sets the value of the
     * {@link #contentEncoding contentEncoding} attribute.
     *
     * @param contentEncoding   the new Content-Encoding header, or
     *                          <code>null</code> to unset
     */
    public void setContentEncoding(final Header contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    /**
     * Specifies the Content-Encoding header, as a string.
     * The default implementation calls
     * {@link #setContentEncoding(Header) setContentEncoding(Header)}.
     *
     * @param ceString     the new Content-Encoding header, or
     *                     <code>null</code> to unset
     */
    public void setContentEncoding(final String ceString) {
        Header h = null;
        if (ceString != null) {
            h = new BasicHeader(HTTP.CONTENT_ENCODING, ceString);
        }
        setContentEncoding(h);
    }


    /**
     * Specifies the 'chunked' flag.
     * The default implementation sets the value of the
     * {@link #chunked chunked} attribute.
     *
     * @param b         the new 'chunked' flag
     */
    public void setChunked(boolean b) {
        this.chunked = b;
    }


    /**
     * The default implementation does not consume anything.
     *
     * @deprecated Either use {@link #getContent()} and call {@link java.io.InputStream#close()} on that;
     * otherwise call {@link #writeTo(OutputStream)} which is required to free the resources.
     */
    public void consumeContent() throws IOException {
    }

}
