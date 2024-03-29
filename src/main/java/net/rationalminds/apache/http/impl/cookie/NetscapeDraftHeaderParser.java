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

import java.util.ArrayList;
import java.util.List;

import net.rationalminds.apache.http.annotation.Immutable;
import net.rationalminds.core.apache.http.HeaderElement;
import net.rationalminds.core.apache.http.NameValuePair;
import net.rationalminds.core.apache.http.ParseException;
import net.rationalminds.core.apache.http.message.BasicHeaderElement;
import net.rationalminds.core.apache.http.message.BasicHeaderValueParser;
import net.rationalminds.core.apache.http.message.ParserCursor;
import net.rationalminds.core.apache.http.util.CharArrayBuffer;

/**
 *
 * @since 4.0
 */
@Immutable
public class NetscapeDraftHeaderParser {

    public final static NetscapeDraftHeaderParser DEFAULT = new NetscapeDraftHeaderParser();
    
    private final static char[] DELIMITERS = new char[] { ';' };
    
    private final BasicHeaderValueParser nvpParser;
    
    public NetscapeDraftHeaderParser() {
        super();
        this.nvpParser = BasicHeaderValueParser.DEFAULT;
    }
    
    public HeaderElement parseHeader(
            final CharArrayBuffer buffer,
            final ParserCursor cursor) throws ParseException {
        if (buffer == null) {
            throw new IllegalArgumentException("Char array buffer may not be null");
        }
        if (cursor == null) {
            throw new IllegalArgumentException("Parser cursor may not be null");
        }
        NameValuePair nvp = this.nvpParser.parseNameValuePair(buffer, cursor, DELIMITERS);
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        while (!cursor.atEnd()) {
            NameValuePair param = this.nvpParser.parseNameValuePair(buffer, cursor, DELIMITERS);
            params.add(param);
        }
        return new BasicHeaderElement(
                nvp.getName(), 
                nvp.getValue(), params.toArray(new NameValuePair[params.size()]));
    }

}
