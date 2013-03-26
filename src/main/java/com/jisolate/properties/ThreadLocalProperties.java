/*
 * Copyright (c) 2013 Christian Gleissner.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the jisolate nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.jisolate.properties;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadLocalProperties extends Properties {

    private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalProperties.class);
    private static final long serialVersionUID = 1L;

    public static synchronized void activate() {
        Properties props = System.getProperties();
        if (!(props instanceof ThreadLocalProperties)) {
            System.setProperties(new ThreadLocalProperties(props));
            LOG.info("Activated thread-local system properties");
        }
    }

    private final ThreadLocal<Properties> localProperties = new ThreadLocal<Properties>() {
        @Override
        protected Properties initialValue() {
            return new Properties();
        }
    };

    public ThreadLocalProperties(Properties properties) {
        super(properties);
    }

    @Override
    public String getProperty(String key) {
        String localValue = localProperties.get().getProperty(key);
        return localValue == null ? super.getProperty(key) : localValue;
    }

    @Override
    public Object setProperty(String key, String value) {
        return localProperties.get().setProperty(key, value);
    }
}