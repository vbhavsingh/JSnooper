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

import java.io.Serializable;

import net.rationalminds.core.apache.http.ProtocolVersion;
import net.rationalminds.core.apache.http.StatusLine;

/**
 * Basic implementation of {@link StatusLine}
 *
 * @version $Id: BasicStatusLine.java,v 1.2 2011/11/23 17:34:23 va6260 Exp $
 *
 * @since 4.0
 */
public class BasicStatusLine implements StatusLine, Cloneable, Serializable {

    private static final long serialVersionUID = -2443303766890459269L;

    // ----------------------------------------------------- Instance Variables

    /** The protocol version. */
    private final ProtocolVersion protoVersion;

    /** The status code. */
    private final int statusCode;

    /** The reason phrase. */
    private final String reasonPhrase;

    // ----------------------------------------------------------- Constructors
    /**
     * Creates a new status line with the given version, status, and reason.
     *
     * @param version           the protocol version of the response
     * @param statusCode        the status code of the response
     * @param reasonPhrase      the reason phrase to the status code, or
     *                          <code>null</code>
     */
    public BasicStatusLine(final ProtocolVersion version, int statusCode,
                           final String reasonPhrase) {
        super();
        if (version == null) {
            throw new IllegalArgumentException
                ("Protocol version may not be null.");
        }
        if (statusCode < 0) {
            throw new IllegalArgumentException
                ("Status code may not be negative.");
        }
        this.protoVersion = version;
        this.statusCode   = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    // --------------------------------------------------------- Public Methods

    public int getStatusCode() {
        return this.statusCode;
    }

    public ProtocolVersion getProtocolVersion() {
        return this.protoVersion;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public String toString() {
        // no need for non-default formatting in toString()
        return BasicLineFormatter.DEFAULT
            .formatStatusLine(null, this).toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
