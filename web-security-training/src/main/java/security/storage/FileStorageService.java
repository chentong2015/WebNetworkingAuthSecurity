package security.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class FileStorageService {

    private final Path rootLocation;
    private final Set<IpAddressMatcher> denyAddressMatchers;
    private static final Set<String> allowedProtocols = Set.of("http", "https");

    public FileStorageService(final String location,
                              @Value("${storage.host.ipdenylist}") final String[] ipDenyList) {
        rootLocation = Paths.get(location);
        denyAddressMatchers = Arrays.stream(ipDenyList).map(IpAddressMatcher::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    public void verifyUrl(final URL url) throws MalformedURLException {
        final String protocol = url.getProtocol();
        if (!allowedProtocols.contains(protocol)) {
            throw new MalformedURLException("Forbidden protocol in given URL: " + protocol);
        }

        final String host = url.getHost();
        final InetAddress address;
        try {
            address = InetAddress.getByName(host);
        } catch (final UnknownHostException e) {
            throw new MalformedURLException("Unknown host in given URL: " + host);
        }
        if (denyAddressMatchers.stream().anyMatch(m -> m.matches(address.getHostAddress()))) {
            throw new MalformedURLException("Denied host in given URL: " + host);
        }
        if (address.isAnyLocalAddress() || address.isLoopbackAddress()) {
            throw new MalformedURLException("Given URL hosts resolved to local machine: " + host);
        }
    }

    public boolean store(final URL url, final String path) throws Exception {
        verifyUrl(url);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);

        try (final BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
            final File file = File.createTempFile("filestorage-tmp", ".tmp");
            final String name = file.getName();
            final String originalFileName = file.getName();
            final String contentType = "image/jpeg";
            final byte[] content = inputStream.readAllBytes();
            // Save file data to system
        } catch (final Exception e) {
            throw new Exception("Failed to store the uploaded proof.", e);
        }
        return true;
    }

    public boolean store(final MultipartFile file, final String path) throws Exception {
        try {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file.");
            }
            final Path destinationFile = rootLocation.resolve(path).normalize().toAbsolutePath();
            ImageIO.write(ImageIO.read(file.getInputStream()), "jpeg", destinationFile.toFile());
        } catch (final IOException e) {
            throw new Exception("Failed to store the uploaded proof.", e);
        }
        return true;
    }
}
