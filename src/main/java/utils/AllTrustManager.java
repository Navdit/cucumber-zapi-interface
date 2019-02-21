package utils;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * @Name           : AllTrustManager
 * @Description    : Implements Method, to create an All TrustManager Store. This is to bypass any certification check.
 *
 * @version 1.0 20th Feb 2019
 * @author Navdit Sharma
 */
public class AllTrustManager {

    /**
     * @Name                 : installAllTrustingManager
     * @Description          : Return an all Trust Manager. This is not recommended approach, as it opens up security
     *                         issues.
     *
     * @return               : TrustManager which can be added to HTTPURLConnection.
     *
     * @version 1.0 20th Feb 2019
     * @author Navdit Sharma
     */
    public TrustManager[] installAllTrustingManager() {

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        return trustAllCerts;
    }
}
