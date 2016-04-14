package javaserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Headers {
    public static final String SEPARATOR = ": ";
    public static final List<String> KNOWN_HEADERS = new ArrayList<>(Arrays.asList("If-Match", "Range", "Authorization", "Host", "Connection", "User-Agent", "Accept-Encoding", "Content-Length"));
}
