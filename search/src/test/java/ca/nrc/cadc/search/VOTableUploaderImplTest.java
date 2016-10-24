/*
 ************************************************************************
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 *
 * (c) 2013.                         (c) 2013.
 * National Research Council            Conseil national de recherches
 * Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
 * All rights reserved                  Tous droits reserves
 *
 * NRC disclaims any warranties         Le CNRC denie toute garantie
 * expressed, implied, or statu-        enoncee, implicite ou legale,
 * tory, of any kind with respect       de quelque nature que se soit,
 * to the software, including           concernant le logiciel, y com-
 * without limitation any war-          pris sans restriction toute
 * ranty of merchantability or          garantie de valeur marchande
 * fitness for a particular pur-        ou de pertinence pour un usage
 * pose.  NRC shall not be liable       particulier.  Le CNRC ne
 * in any event for any damages,        pourra en aucun cas etre tenu
 * whether direct or indirect,          responsable de tout dommage,
 * special or general, consequen-       direct ou indirect, particul-
 * tial or incidental, arising          ier ou general, accessoire ou
 * from the use of the software.        fortuit, resultant de l'utili-
 *                                      sation du logiciel.
 *
 *
 * @author jenkinsd
 * 11/14/13 - 3:00 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */
package ca.nrc.cadc.search;


import ca.nrc.cadc.AbstractUnitTest;
import ca.nrc.cadc.auth.AuthMethod;
import ca.nrc.cadc.net.HttpUpload;
import ca.nrc.cadc.reg.Standards;
import ca.nrc.cadc.reg.client.RegistryClient;
import ca.nrc.cadc.search.VOTableUploaderImpl;
import ca.nrc.cadc.uws.web.InlineContentException;
import org.junit.Test;

import java.net.URI;
import java.net.URL;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class VOTableUploaderImplTest
        extends AbstractUnitTest<VOTableUploaderImpl>
{
    final HttpUpload mockHTTPUpload = createMock(HttpUpload.class);
    final RegistryClient mockRegistryClient = createMock(RegistryClient.class);

    @Test
    public void uploadGood() throws Exception
    {
        // Test with alternate ports.
        final URL testURL =
                new URL("https://mysite.com:3443/secure/tap/upload");
        final URL dataHTTPURL = new URL("http://mysite.com:3080/data");

        setTestSubject(new VOTableUploaderImpl(mockHTTPUpload,
                                               mockRegistryClient)
        {
            /**
             * Execute the upload over HTTPS.
             *
             * @param upload The upload Runnable class.
             */
            @Override
            protected void secureUpload(Runnable upload)
            {
                upload.run();
            }
        });

        mockHTTPUpload.setContentType("text/xml");
        expectLastCall().once();

        mockHTTPUpload.run();
        expectLastCall().once();

        expect(mockRegistryClient.getServiceURL(
            URI.create(VOTableUploaderImpl.DATA_URI),
            Standards.DATA_10,
            AuthMethod.COOKIE)).andReturn(dataHTTPURL).once();

        expect(mockHTTPUpload.getURL()).andReturn(testURL).once();
        expect(mockHTTPUpload.getThrowable()).andReturn(null).once();

        replay(mockHTTPUpload, mockRegistryClient);

        final URL expectedReturnURL1 =
                new URL("http://mysite.com:3080/secure/tap/upload");
        final URL returnURL1 = getTestSubject().upload();

        assertEquals("Return URL should match.", expectedReturnURL1,
                     returnURL1);

        verify(mockHTTPUpload, mockRegistryClient);
    }

    @Test
    public void uploadException() throws Exception
    {
        setTestSubject(new VOTableUploaderImpl(mockHTTPUpload,
                                               mockRegistryClient)
        {
            /**
             * Execute the upload over HTTPS.
             *
             * @param upload The upload Runnable class.
             */
            @Override
            protected void secureUpload(Runnable upload)
            {
                upload.run();
            }
        });

        mockHTTPUpload.setContentType("text/xml");
        expectLastCall().once();

        mockHTTPUpload.run();
        expectLastCall().once();

        expect(mockHTTPUpload.getThrowable()).andReturn(
                new Exception()).once();

        replay(mockHTTPUpload, mockRegistryClient);

        try
        {
            getTestSubject().upload();
            fail("Should throw exception.");
        }
        catch (InlineContentException e)
        {
            // Good!
        }

        verify(mockHTTPUpload, mockRegistryClient);
    }
}
