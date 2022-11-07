Universal Acceptance of TLDs
----------------------------

As ICANN has introduced new generic Top Level Domains into the DNS
root, problems have been experienced with end users receiving errors
from applications which try to sense the validity of a domain name.
Traditionally all top-level domains have either been two octets long
(for country-code top level domains like .UK and .DE), or three octets
long (for generic top level domains like .COM and .INT). However, with
the introduction of new domains such as .INFO and .TRAVEL this premise
no longer applies.

This project provides three small utilities:

verifytld(domain):

    Verifies a top-level domain exists.

    This function takes a single argument, which can either be a
    domain name, or a TLD - and verifies its validity using the DNS
    protocol.

verifytldoffline(domain[, filename]):

    Verifies a top-level domain exists against a fixed database.

    The first argument supplies is either a domain name, or a TLD,
    which is verified for validity against a fixed database that
    has been previously obtained with the refreshtlddb() function.
    The optional second argument is the filename where the cache
    is stored.

refreshtlddb([filename]):

    Updates the copy of the fixed database of valid top-level
    domains.

    Downloads the official list of valid TLDs from the IANA website,
    and performs consistency checking to ensure it was downloaded
    correctly. The optional argument is the filename to store
    the cache in.

    NOTE: This function should not be called on every request or
    invocation, it is designed to store a cached version of the file,
    which can be referenced in future queries. There is no need to
    update this file more often than once per day.

The [ua-code-samples](https://github.com/icann/ua-code-samples) repo
contains a number of code samples related to IDNs, that is, to domains
such as [טעסט.	](https://web.archive.org/web/20111002054014/http://%D7%91%D7%B2%D6%B7%D7%A9%D7%A4%D6%BC%D7%99%D7%9C.%D7%98%D7%A2%D7%A1%D7%98/%D7%94%D7%95%D7%99%D7%A4%D6%BC%D7%98_%D7%96%D7%B2%D6%B7%D7%98) (a test TLD using yiddish).

Finally, in certain applications - such as those without reliable
connectivity to perform live DNS requests, or for those for
performance reasons can not conduct such requests for each test, the
possibility exists to verify against
[IANA'a list of TLDs](https://data.iana.org/TLD/tlds-alpha-by-domain.txt). Many
users have come to use Mozilla's
[Public Suffix List](https://publicsuffix.org) for
similar purposes. Note that the list changes frequently, so you need
to update it every month, and that it contains domains such as .co.uk,
not just .uk.
