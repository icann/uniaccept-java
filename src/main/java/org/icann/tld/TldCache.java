// Copyright 2006 ICANN. All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
//    1. Redistributions of source code must retain the above copyright notice, this list of conditions
//       and the following disclaimer.
//    2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
//       and the following disclaimer in the documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY ICANN ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE FREEBSD PROJECT OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY,
// OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
//  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
//  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
//  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
// The views and conclusions contained in the software and documentation are those of the authors
// and should not be interpreted as representing official policies,
// either expressed or implied, of ICANN.
package org.icann.tld;

import java.util.*;
import java.io.*;

/**
 * In memory cache that holds all the TLDs that were fetch using
 * {@link TldVerify#refreshTldDB}
 *
 * @author Simon Raveh
 * @version 1.0
 */
class TldCache {

    private List<String> tlds;
    private String header;
    private long version;

    public TldCache(long version, String header) {
        this.version = version;
        this.header = header;
        tlds = new ArrayList<String>();
    }

    /**
     * Return True if the TLD exist in the cache
     * @param tld - the top level domain to check
     * @return  <code>true</code> if the top level domain exist <code>false</code> otherwise
     */
    public boolean exist(String tld) {
        for (String name : tlds) {
            if(name.toUpperCase().equals(tld.toUpperCase())){
                return true;
            }
        }
        return false;
    }

    /**
     * Return a list of all TLDs currently in the cache
     * @return  a list of all TLDs
     */
    public List<String> tlds() {
        return Collections.unmodifiableList(tlds);
    }

    /**
     * Write out all TLDs and the header
     *
     * @param out The writer to use
     * @throws IOException
     */
    public void print(Writer out) throws IOException {
        out.write(header);
        out.write("\n");
        for (String tld : tlds) {
            out.write(tld);
            out.write("\n");
        }
        out.flush();
    }

    /**
     * Add new TLD to the cache.
     *
     * @param tld The top level domain to add
     */
    public void addTld(String tld) {
        tlds.add(tld);
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TldCache tldCache = (TldCache) o;

        if (version != tldCache.version) return false;
        if (header != null ? !header.equals(tldCache.header) : tldCache.header != null) return false;
        if (tlds != null ? !tlds.equals(tldCache.tlds) : tldCache.tlds != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (tlds != null ? tlds.hashCode() : 0);
        result = 31 * result + (header != null ? header.hashCode() : 0);
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }

    public long getVersion() {
        return version;
    }
}
