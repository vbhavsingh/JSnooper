/*
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package net.rationalminds.apache.http.impl.conn;

import java.io.IOException;

import net.rationalminds.apache.http.annotation.Immutable;
import net.rationalminds.core.apache.http.io.HttpTransportMetrics;
import net.rationalminds.core.apache.http.io.SessionInputBuffer;
import net.rationalminds.core.apache.http.util.CharArrayBuffer;

/**
 * Logs all data read to the wire LOG.
 *
 * 
 * @since 4.0
 */
@Immutable
public class LoggingSessionInputBuffer implements SessionInputBuffer {
     
    /** Original session input buffer. */
    private final SessionInputBuffer in;

    /** The wire log to use for writing. */
    private final Wire wire;
    
    /**
     * Create an instance that wraps the specified session input buffer.
     * @param in The session input buffer.
     * @param wire The wire log to use.
     */
    public LoggingSessionInputBuffer(final SessionInputBuffer in, final Wire wire) {
        super();
        this.in = in;
        this.wire = wire;
    }

    public boolean isDataAvailable(int timeout) throws IOException {
        return this.in.isDataAvailable(timeout);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int l = this.in.read(b,  off,  len);
//        if (this.wire.enabled() && l > 0) {
//            this.wire.input(b, off, l);
//        }
        return l;
    }

    public int read() throws IOException {
        int l = this.in.read();
//        if (this.wire.enabled() && l != -1) {
//            this.wire.input(l);
//        }
        return l;
    }

    public int read(byte[] b) throws IOException {
        int l = this.in.read(b);
//        if (this.wire.enabled() && l > 0) {
//            this.wire.input(b, 0, l);
//        }
        return l;
    }

    public String readLine() throws IOException {
        String s = this.in.readLine();
//        if (this.wire.enabled() && s != null) {
//            this.wire.input(s + "[EOL]");
//        }
        return s;
    }

    public int readLine(final CharArrayBuffer buffer) throws IOException {
        int l = this.in.readLine(buffer);
//        if (this.wire.enabled() && l >= 0) {
//            int pos = buffer.length() - l;
//            String s = new String(buffer.buffer(), pos, l);
//            this.wire.input(s + "[EOL]");
//        }
        return l;
    }

    public HttpTransportMetrics getMetrics() {
        return this.in.getMetrics();
    }

}
