package security.storage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;

// TODO. 执行任何URL请求先必须先验证(特别是用户端提供的链接)
// Avoid processing user-supplied URLs as much as possible.
// Server-Side Request Forgery (SSRF) risks
public class ImageUploadService {

    private static final String ALLOWED_PROTOCOL = "https://";
    private static final List<String> ALLOWED_HOSTS = List.of("secure-code.com", "test.io");

    public static String sendRequest(String inputUrl) throws Exception {
        URL url = new URL(inputUrl);

        // Check scheme + host against allowed values
        if (!ALLOWED_PROTOCOL.equals(url.getProtocol()) || !ALLOWED_HOSTS.contains(url.getHost())) {
            throw new Exception("URL is not within allowed protocol or host");
        }
        // Resolve the IP address
        InetAddress address = InetAddress.getByName(url.getHost());
        if (address.isAnyLocalAddress()) {
            throw new Exception("URL points to a local IP address: " + address.getHostAddress());
        }

        // Connect to the host
        HttpURLConnection con = (HttpURLConnection) new URL(inputUrl.replace(url.getHost(), address.getHostAddress())).openConnection();
        // block redirects
        con.setInstanceFollowRedirects(false);
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        if (status >= 300 && status <= 399) {
            throw new Exception("URL is a redirect: " + status);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public static String getData(String url, String userId) throws Exception {
        String data = sendRequest(url + "/getData?userId=" + userId);
        return data;
    }
}