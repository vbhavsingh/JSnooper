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

import java.util.Collection;

import net.rationalminds.apache.http.annotation.Immutable;
import net.rationalminds.apache.http.cookie.CookieSpec;
import net.rationalminds.apache.http.cookie.CookieSpecFactory;
import net.rationalminds.apache.http.cookie.params.CookieSpecPNames;
import net.rationalminds.core.apache.http.params.HttpParams;

/**
 * {@link CookieSpecFactory} implementation that creates and initializes
 * {@link NetscapeDraftSpec} instances.
 * <p>
 * The following parameters can be used to customize the behavior of this 
 * class: 
 * <ul>
 *  <li>{@link com.e2e.apache.http.cookie.params.CookieSpecPNames#DATE_PATTERNS}</li>
 * </ul>
 *
 * @since 4.0
 */
@Immutable
public class NetscapeDraftSpecFactory implements CookieSpecFactory {    

    public CookieSpec newInstance(final HttpParams params) {
        if (params != null) {
        	
        	String[] patterns = null;
        	Collection<?> param = (Collection<?>) params.getParameter(
        			CookieSpecPNames.DATE_PATTERNS);
        	if (param != null) {
        		patterns = new String[param.size()];
        		patterns = param.toArray(patterns);
        	}
            return new NetscapeDraftSpec(patterns);
        } else {
            return new NetscapeDraftSpec();
        }
    }

}
