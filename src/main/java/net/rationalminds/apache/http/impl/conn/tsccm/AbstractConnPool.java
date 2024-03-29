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
package net.rationalminds.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.rationalminds.apache.http.annotation.GuardedBy;
import net.rationalminds.apache.http.annotation.ThreadSafe;
import net.rationalminds.apache.http.conn.ConnectionPoolTimeoutException;
import net.rationalminds.apache.http.conn.OperatedClientConnection;
import net.rationalminds.apache.http.conn.routing.HttpRoute;
import net.rationalminds.apache.http.impl.conn.IdleConnectionHandler;

/**
 * An abstract connection pool.
 * It is used by the {@link ThreadSafeClientConnManager}.
 * The abstract pool includes a {@link #poolLock}, which is used to
 * synchronize access to the internal pool datastructures.
 * Don't use <code>synchronized</code> for that purpose!
 *
 * @since 4.0
 */
@ThreadSafe
@SuppressWarnings("deprecation")
public abstract class AbstractConnPool implements RefQueueHandler {

    /**
     * The global lock for this pool.
     */
    protected final Lock poolLock;
    /**
     * References to issued connections.
     * Must hold poolLock when accessing.
     */
    @GuardedBy("poolLock")
    protected Set<BasicPoolEntry> leasedConnections;
    /** The handler for idle connections. Must hold poolLock when accessing. */
    @GuardedBy("poolLock")
    protected IdleConnectionHandler idleConnHandler;
    /** The current total number of connections. */
    @GuardedBy("poolLock")
    protected int numConnections;
    /** Indicates whether this pool is shut down. */
    protected volatile boolean isShutDown;
    @Deprecated
    protected Set<BasicPoolEntryRef> issuedConnections;
    @Deprecated
    protected ReferenceQueue<Object> refQueue;

    /**
     * Creates a new connection pool.
     */
    protected AbstractConnPool() {
        leasedConnections = new HashSet<BasicPoolEntry>();
        idleConnHandler = new IdleConnectionHandler();

        boolean fair = false; //@@@ check parameters to decide
        poolLock = new ReentrantLock(fair);
    }

    /**
     * @deprecated do not sue
     */
    @Deprecated
    public void enableConnectionGC()
            throws IllegalStateException {
    }

    /**
     * Obtains a pool entry with a connection within the given timeout.
     *
     * @param route     the route for which to get the connection
     * @param timeout   the timeout, 0 or negative for no timeout
     * @param tunit     the unit for the <code>timeout</code>,
     *                  may be <code>null</code> only if there is no timeout
     *
     * @return  pool entry holding a connection for the route
     *
     * @throws ConnectionPoolTimeoutException
     *         if the timeout expired
     * @throws InterruptedException
     *         if the calling thread was interrupted
     */
    public final BasicPoolEntry getEntry(
            HttpRoute route,
            Object state,
            long timeout,
            TimeUnit tunit)
            throws ConnectionPoolTimeoutException, InterruptedException,IOException {
        return requestPoolEntry(route, state).getPoolEntry(timeout, tunit);
    }

    /**
     * Returns a new {@link PoolEntryRequest}, from which a {@link BasicPoolEntry}
     * can be obtained, or the request can be aborted.
     */
    public abstract PoolEntryRequest requestPoolEntry(HttpRoute route, Object state);

    /**
     * Returns an entry into the pool.
     * The connection of the entry is expected to be in a suitable state,
     * either open and re-usable, or closed. The pool will not make any
     * attempt to determine whether it can be re-used or not.
     *
     * @param entry     the entry for the connection to release
     * @param reusable  <code>true</code> if the entry is deemed 
     *                  reusable, <code>false</code> otherwise.
     * @param validDuration The duration that the entry should remain free and reusable.
     * @param timeUnit The unit of time the duration is measured in.
     */
    public abstract void freeEntry(BasicPoolEntry entry, boolean reusable, long validDuration, TimeUnit timeUnit) throws IOException;

    @Deprecated
    public void handleReference(Reference<?> ref) {
    }

    @Deprecated
    protected abstract void handleLostEntry(HttpRoute route);

    /**
     * Closes idle connections.
     *
     * @param idletime  the time the connections should have been idle
     *                  in order to be closed now
     * @param tunit     the unit for the <code>idletime</code>
     */
    public void closeIdleConnections(long idletime, TimeUnit tunit) {

        // idletime can be 0 or negative, no problem there
        if (tunit == null) {
            throw new IllegalArgumentException("Time unit must not be null.");
        }

        poolLock.lock();
        try {
            idleConnHandler.closeIdleConnections(tunit.toMillis(idletime));
        } finally {
            poolLock.unlock();
        }
    }

    public void closeExpiredConnections() {
        poolLock.lock();
        try {
            idleConnHandler.closeExpiredConnections();
        } finally {
            poolLock.unlock();
        }
    }

    /**
     * Deletes all entries for closed connections.
     */
    public abstract void deleteClosedConnections() throws IOException;

    /**
     * Shuts down this pool and all associated resources.
     * Overriding methods MUST call the implementation here!
     */
    public void shutdown() throws IOException{

        poolLock.lock();
        try {

            if (isShutDown) {
                return;
            }

            // close all connections that are issued to an application
            Iterator<BasicPoolEntry> iter = leasedConnections.iterator();
            while (iter.hasNext()) {
                BasicPoolEntry entry = iter.next();
                iter.remove();
                closeConnection(entry.getConnection());
            }
            idleConnHandler.removeAll();

            isShutDown = true;

        } finally {
            poolLock.unlock();
        }
    }

    /**
     * Closes a connection from this pool.
     *
     * @param conn      the connection to close, or <code>null</code>
     */
    protected void closeConnection(final OperatedClientConnection conn) throws IOException {
        if (conn != null) {

            conn.close();

        }
    }
} // class AbstractConnPool

