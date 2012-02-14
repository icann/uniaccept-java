package org.icann.tld;

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

import junit.framework.TestCase;

import java.io.*;

import org.apache.commons.io.*;

public class TldVerifyTest extends TestCase {

    public void testRefreshTldDB() throws Exception {
        new TldVerify().refreshTldDB("testDB.txt");
        File file = new File("testDB.txt");
        assertTrue(file.exists());
        String s = FileUtils.readFileToString(file, null);
        assertTrue(s.indexOf("AERO")> -1);
    }

    public void testVerifyTldOffline() throws Exception {
        TldVerify tldVerify = new TldVerify();
        assertTrue(tldVerify.verifyTldOffline("AERO."));
        assertTrue(tldVerify.verifyTldOffline("AERO", "testDB.txt"));
        assertFalse(tldVerify.verifyTldOffline("sss"));
        assertTrue(tldVerify.verifyTldOffline(".AERO."));
    }

    public void testVerifyTld() throws Exception {
        TldVerify tldVerify = new TldVerify();
        assertTrue(tldVerify.verifyTld("AERO."));
        assertTrue(tldVerify.verifyTld("AERO"));
        assertTrue(tldVerify.verifyTld("ICANN.ORG"));
        assertTrue(tldVerify.verifyTld("icann.org"));
        assertFalse(tldVerify.verifyTld("SS"));
    }


}