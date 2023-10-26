import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UriUrl {

    private URI uri;

    /**
     * Scheme: db
     * Scheme-specific part: //username:password@myserver.com:5000/folder/phones?os=android
     * Authority: username:password@myserver.com:5000
     * User info: username:password 用户认证信息
     * Host: myserver.com 目标主机的名称，或者IPv4 & IPv6地址
     * Port: 5000
     * Path: /folder/phones 相对路径
     * Query: os=android 查询的信息
     * Fragment: samsung 目标资源的子集位置
     */
    private void testURI() {
        try {
            // URI 提供的位置可以是完全不存在的路径，不会报错 ==> 可能不是有效的
            uri = new URI("db://username:password@myserver.com:5000/folder/phones?os=android#samsung");
            System.out.println("Scheme: " + uri.getScheme());
            System.out.println("Scheme-specific part: " + uri.getSchemeSpecificPart());
            System.out.println("Authority: " + uri.getAuthority());
            System.out.println("User info: " + uri.getUserInfo());
            System.out.println("Host: " + uri.getHost());
            System.out.println("Port: " + uri.getPort());
            System.out.println("Path: " + uri.getPath());
            System.out.println("Query: " + uri.getQuery());
            System.out.println("Fragment: " + uri.getFragment());
        } catch (URISyntaxException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 1. URI is not absolute只提供相对路径的URI不足以定位和访问有效的资源
     * 2. Base URI包含公共的host information部分，在结合不同的URI组成绝对的路径 !!!
     * 3. 根据Base URI和Full URI可以解析出相对的路径 !!
     */
    private void testRelativeRUI() {
        try {
            URI baseURI = new URI("http://username:password@server.com:5000");
            URI relativeURI = new URI("/folder/phones?os=android#samsung");
            URI fullURI = baseURI.resolve(relativeURI);

            URI relativeURI2 = new URI("/catalogue/tvs?manufacturer=samsung");
            URI fullURI2 = baseURI.resolve(relativeURI2);

            URI relativeURI3 = baseURI.relativize(fullURI2); // => relativeURI2
        } catch (URISyntaxException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * URI -> URL, URL -> URI
     * uri中的"Scheme: db"并不是有效的URL中支持的Protocol组成部分, 转换出错
     * URL必须是绝对的路径，确定能够定位和访问到资源 !!
     */
    private void testURL() {
        try {
            URL url = uri.toURL();
            URI uri = url.toURI();
        } catch (MalformedURLException | URISyntaxException exception) {
            System.out.println("URI is not valid");
        }
    }
}
