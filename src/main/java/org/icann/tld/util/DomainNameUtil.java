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
package org.icann.tld.util;

import org.apache.commons.lang.StringUtils;

/**
 * A utility class to manipulate domain names
 *
 * @author Simon Raveh
 * @version 1.0.0
 */
public class DomainNameUtil {

    /**
     * Find the top level domain of a given domain name.
     *
     * @param domainName a fully qualified domain name
     * @return return the top level domain for this domain name
     */
    public String getTopLevelDomain(String domainName) {

        if(StringUtils.isBlank(domainName)){
            throw new IllegalArgumentException("Domain name can not be null or empty ");
        }

        String[] labels = domainName.split("\\.");
        if (labels.length > 1) {
            return labels[labels.length - 1];
        }
        return labels[0];
    }

    /**
     * <p>Strip the leading "." from a domain name. </p>
     * <pre>
     * DomainNameUtil.stripLeadingDot(.abc.com)  = abc.com
     * DomainNameUtil.stripLeadingDot(.abc)  = abc
     * DomainNameUtil.stripLeadingDot(abc)  = abc
     *
     *
     * @param domainName the domain name to strip the dot from, may be null
     * @return the domain name without the leading dot
     */
    public String stripLeadingDot(String domainName) {
        if(domainName == null){
            return domainName;
        }

        if (domainName.startsWith(".") && domainName.length() > 1) {
            return domainName.substring(1, domainName.length());
        }
        return domainName;
    }

    /**
     * <p>Strip the trailing "." from a domain name. </p>
     * <pre>
     * DomainNameUtil.stripLeadingDot(.abc.com.)  = .abc.com
     * DomainNameUtil.stripLeadingDot(abc.)  = abc
     * DomainNameUtil.stripLeadingDot(abc)  = abc
     *
     *
     * @param domainName the domain name to strip the  trailing dot from, may be null
     * @return the domain name without the trailing dot
     */
    public String stripTrailingDot(String domainName) {
        if (domainName.endsWith(".")) {
            return domainName.substring(0, domainName.lastIndexOf("."));
        }
        return domainName;
    }
}
