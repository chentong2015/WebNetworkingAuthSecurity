import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class UrlEncoderDecoder {


    private void testEncoderDecoder() throws UnsupportedEncodingException {
        String URL = "http://www.example.com/test<>";
        String encoderURL = URLEncoder.encode(URL, "UTF-8");
        String decoderURL = URLDecoder.decode(URL, "UTF-8");
    }
}
