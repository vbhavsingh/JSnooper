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

package net.rationalminds.apache.http.client.params;


import net.rationalminds.apache.http.auth.params.AuthPNames;
import net.rationalminds.apache.http.conn.params.ConnConnectionPNames;
import net.rationalminds.apache.http.conn.params.ConnManagerPNames;
import net.rationalminds.apache.http.conn.params.ConnRoutePNames;
import net.rationalminds.apache.http.cookie.params.CookieSpecPNames;
import net.rationalminds.core.apache.http.params.CoreConnectionPNames;
import net.rationalminds.core.apache.http.params.CoreProtocolPNames;


/**
 * Collected parameter names for the HttpClient module.
 * This interface combines the parameter definitions of the HttpClient
 * module and all dependency modules or informational units.
 * It does not define additional parameter names, but references
 * other interfaces defining parameter names.
 * <br/>
 * This interface is meant as a navigation aid for developers.
 * When referring to parameter names, you should use the interfaces
 * in which the respective constants are actually defined.
 *
 * @since 4.0
 */
public interface AllClientPNames extends
    CoreConnectionPNames, CoreProtocolPNames,
    ClientPNames, AuthPNames, CookieSpecPNames,
    ConnConnectionPNames, ConnManagerPNames, ConnRoutePNames {

    // no additional definitions
}

