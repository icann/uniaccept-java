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

import org.apache.commons.io.*;
import org.icann.tld.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.*;

import java.io.*;
import java.net.*;

/**
 * A class that lets you verify the existence
 * of a domain
 *
 * @author Simon Raveh
 * @version 1.0
 */
public class TldVerify {

    private static final Logger logger = LoggerFactory.getLogger(TldVerify.class);

    /**
     * Default Cache file name
     */
    public static final String TLD_CACHE_FILE = "tlds-alpha-by-domain.txt";

    private static final String TLDS_BY_DOMAIN_TXT = "http://data.iana.org/TLD/tlds-alpha-by-domain.txt";

    private TldCache cache;
    private static final String MD5_URL = "http://data.iana.org/TLD/tlds-alpha-by-domain.txt.md5";

    /**
     * Verifies a top-level domain exists.
     * This methods takes a single argument, which can either be a
     * domain name, or a TLD - and verifies its validity using the DNS
     * protocol.
     *
     * @param domainName a domain name or a TLD
     * @return <code>true</code> if the top-level domain exist <code>false</code>
     */
    public boolean verifyTld(String domainName) {
        try {
            domainName = new DomainNameUtil().getTopLevelDomain(domainName);
            Lookup lookup = new Lookup(domainName + ".", Type.SOA);
            Record[] records = lookup.run();
            if (records == null) {
                return false;
            }
        } catch (TextParseException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Verifies a top-level domain exists against a fixed database.
     * This methods takes a single argument, which can either be a
     * domain name, or a TLD and verified for validity against a fixed database that
     * has been previously obtained with the {@link #refreshTldDB()} method.
     *
     * @param domainName the domain name or a TLD to validate
     * @return <code>true</code> if the top-leve domain exist <code>false</code>
     * @throws TLDVerifyException
     */

    public boolean verifyTldOffline(String domainName) throws TLDVerifyException {
        String topLevelDomain = new DomainNameUtil().getTopLevelDomain(domainName);
        if (cache != null) {
            return cache.exist(topLevelDomain);
        } else {
            refreshTldDB(TLD_CACHE_FILE);
            return verifyTldOffline(topLevelDomain, TLD_CACHE_FILE);
        }
    }

    /**
     * Verifies a top-level domain exists against a fixed database.
     * The first argument supplies is either a domain name, or a TLD,
     * which is verified for validity against a fixed database that
     * has been previously obtained with the {@link #refreshTldDB()} method.
     * The second argument is the filename where the cache is stored.
     * The file format should be the same as  the one IANA publish at (http://data.iana.org/TLD/tlds-alpha-by-domain.txt).
     * The version in the first line will be check against the current cache version to prevent loading of old data.
     *
     * @param domainName    The domain name or a TLD to validate
     * @param cacheFilePath The full path to where the cache is stored.
     * @return <code>true</code> if the top-leve domain exist <code>false</code>
     * @throws TLDVerifyException
     */
    public boolean verifyTldOffline(String domainName, String cacheFilePath) throws TLDVerifyException {
        try {
            String topLevelDomain = new DomainNameUtil().getTopLevelDomain(domainName);
            readTld(new FileReader(new File(cacheFilePath)));
            return cache.exist(topLevelDomain);
        } catch (IOException e) {
            throw new TLDVerifyException(e);
        }
    }

    /**
     * Updates the copy of the fixed database of valid top-level
     * domains.
     * Downloads the official list of valid TLDs from the IANA website,
     * and performs consistency checking to ensure it was downloaded
     * correctly. Store the data in the default location.
     *
     * @throws TLDVerifyException
     */
    public void refreshTldDB() throws TLDVerifyException {
        refreshTldDB(TLD_CACHE_FILE);
    }

    /**
     * Updates the copy of the fixed database of valid top-level
     * domains.
     * Downloads the official list of valid TLDs from the IANA website,
     * and performs consistency checking to ensure it was downloaded
     * correctly. The parameter is the filename to store the cache in.
     *
     * @param cacheStoreFileName The full path to the file to store the cache in.
     * @throws TLDVerifyException
     */
    public void refreshTldDB(String cacheStoreFileName) throws TLDVerifyException {
        try {
            String outputCacheName = TLD_CACHE_FILE;
            URL url = new URL(TLDS_BY_DOMAIN_TXT);
            File tempFile = new File("temp_md5 " + System.currentTimeMillis());
            FileUtils.copyURLToFile(url, tempFile);
            String digest = getDigestInfo(new URL(MD5_URL));
            String digest1 = new FileBasedMD5Generator().createDigest(tempFile);

            if (!digest.equals(digest1)) {
                throw new TLDVerifyException("Could not download TLD data from IANA web site");
            }

            readTld(new FileReader(tempFile));

            FileUtils.forceDeleteOnExit(tempFile);

            if (cacheStoreFileName != null) {
                outputCacheName = cacheStoreFileName;
            }

            writeTlds(outputCacheName);
        } catch (MalformedURLException e) {
            throw new TLDVerifyException("Malformed URL " + TLDS_BY_DOMAIN_TXT, e);
        } catch (IOException e) {
            throw new TLDVerifyException(e.getMessage(), e);
        }

    }

    private String getDigestInfo(URL url) throws IOException {
        InputStream inputStream = url.openStream();
        BufferedReader buff = new BufferedReader(new InputStreamReader(inputStream));
        String header = buff.readLine();
        return header.trim().substring(0, 32);
    }


    private void readTld(Reader reader) throws TLDVerifyException {
        try {
            BufferedReader buff = new BufferedReader(reader);
            String line;
            String header = buff.readLine();
            long version = parseVersion(header);
            TldCache newCache = new TldCache(version, header);
            if (cache != null && cache.getVersion() >= version) {
                return;
            }

            while ((line = buff.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                newCache.addTld(line.trim());
            }
            cache = newCache;
        } catch (Exception e) {
            throw new TLDVerifyException(e);
        }
    }

    private long parseVersion(String line) {
        return new VersionParser().parse(line);
    }

    private void writeTlds(String filePath) throws TLDVerifyException {
        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter(filePath));
            cache.print(writer);
        } catch (IOException e) {
            throw new TLDVerifyException(e);
        }
    }


}
