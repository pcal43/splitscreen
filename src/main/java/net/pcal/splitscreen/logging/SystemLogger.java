/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 pcal.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.pcal.splitscreen.logging;

import org.slf4j.Logger;

/**
 * Singleton logger instance that writes to the serverside console.
 *
 * @author pcal
 * @since 0.0.1
 */
public interface SystemLogger {

    static SystemLogger syslog() {
        return Singleton.INSTANCE;
    }

    void setForceDebugEnabled(boolean debug);

    void error(String message);

    void error(String message, Throwable t);

    default void error(Throwable e) {
        this.error(e.getMessage(), e);
    }

    void warn(String message);

    void info(String message);

    void debug(String message);

    void debug(String message, Throwable t);

    default void debug(Throwable t) {
        this.debug(t.getMessage(), t);
    }

    class Singleton {
        private static SystemLogger INSTANCE = null;

        public static void register(Logger slf4j) {
            Singleton.INSTANCE = new Slf4jSystemLogger(slf4j);
        }
    }
}
