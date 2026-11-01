package software.ulpgc.katas.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StreamTextReader {
    public String read(InputStream input) throws IOException {
        return new String(input.readAllBytes(), StandardCharsets.UTF_8);
    }
}