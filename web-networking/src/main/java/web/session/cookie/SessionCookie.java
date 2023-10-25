package web.session.cookie;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.URI;
import java.util.List;

// Session: Context 用于在clients和servers直接交换上下文信息
// Cookie : The state information 用于创建和维护会话的状态信息称为cookie
// 1. Cookie的信息可以存储在浏览器的缓存
// 2. Cookie的数据可以在重新访问网页时完成身份认证(在Cookie有效时间之内，该时间可以设置)
public class SessionCookie implements CookieStore, Runnable {

    // CookiePolicy: 预定义是否接受cookie的policy
    // 1. CookiePolicy.ACCEPT_ORIGINAL_SERVER only accepts cookies from the original server.
    // 2. CookiePolicy.ACCEPT_ALL accepts all cookies.
    // 3. CookiePolicy.ACCEPT_NONE accepts no cookies

    // CookieStore: an interface that represents a storage area for cookies
    // 1. CookieManager adds the cookies to the CookieStore for every HTTP response
    // 2. CookieManager retrieves cookies from the CookieStore for every HTTP request
    CookieStore store;

    // 创建时先读取之前存储的cookies
    // During runtime, cookies are stored and retrieved from memory.
    // 结束时，将cookies写入到持久层的存储中
    public SessionCookie() {
        store = new CookieManager().getCookieStore();
        // read in cookies from persistent storage, and add them store
        // add a shutdown hook to write out the in memory cookies
        Runtime.getRuntime().addShutdownHook(new Thread(this));
    }

    @Override
    public void run() {
        // write cookies in store to persistent storage
    }

    @Override
    public void add(URI uri, java.net.HttpCookie cookie) {
        store.add(uri, cookie);
    }

    @Override
    public List<java.net.HttpCookie> get(URI uri) {
        return store.get(uri);
    }

    @Override
    public List<java.net.HttpCookie> getCookies() {
        return store.getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return store.getURIs();
    }

    @Override
    public boolean remove(URI uri, java.net.HttpCookie cookie) {
        return store.remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return store.removeAll();
    }
}
