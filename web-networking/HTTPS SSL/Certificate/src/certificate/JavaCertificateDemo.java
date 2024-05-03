package certificate;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class JavaCertificateDemo {

    public static void main(String[] args) throws Exception {
        // 提供的certificate的文件必须是有效的签名文件
        InputStream inputStream2 = new FileInputStream("xxx/certificate/test.crt");
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        X509Certificate signingCert = (X509Certificate) factory.generateCertificate(inputStream2);
        System.out.println(signingCert.toString());

        // Create a CertPath Instance
        InputStream certificateInputStream = new FileInputStream("my-x509-certificate-chain.crt");
        CertPath certPath = factory.generateCertPath(certificateInputStream);
    }

    public X509Certificate loadCertificateFromFile(String file) throws Exception {
        if(file == null) {
            return null;
        }
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        try(FileInputStream is = new FileInputStream(file)) {
            return (X509Certificate) fact.generateCertificate(is);
        }
    }

    protected X509Certificate generateCertFromDER(byte[] certBytes) throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
    }
}
