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

import static java.util.Objects.requireNonNull;

/**
 * @author pcal
 * @since 0.0.1
 */
class Slf4jSystemLogger implements SystemLogger {

    private final Logger slf4j;
    private boolean forceDebugEnabled = false;

    Slf4jSystemLogger(Logger slf4j) {
        this.slf4j = requireNonNull(slf4j);
    }

    @Override
    public void setForceDebugEnabled(boolean forceDebugEnabled) {
        this.forceDebugEnabled = forceDebugEnabled;
    }

    @Override
    public void error(String message) {
        this.slf4j.error(message);
    }

    @Override
    public void error(String message, Throwable t) {
        this.slf4j.error(message, t);
    }

    @Override
    public void warn(String message) {
        this.slf4j.warn(message);
    }

    @Override
    public void info(String message) {
        this.slf4j.info(message);
    }

    @Override
    public void debug(String message) {
        if (this.forceDebugEnabled) {
            this.slf4j.info("[DEBUG] " + message);
        } else {
            this.slf4j.debug(message);
        }
    }

    @Override
    public void debug(String message, Throwable t) {
        if (this.forceDebugEnabled) {
            this.slf4j.info("[DEBUG] " + message, t);
        } else {
            this.slf4j.debug(message, t);
        }
    }
}
