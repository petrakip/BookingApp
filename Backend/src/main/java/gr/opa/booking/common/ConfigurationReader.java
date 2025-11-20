package gr.opa.booking.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class ConfigurationReader {
    private Configuration configuration;
    public ConfigurationReader() {
        initialize();
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    private void initialize() {
        InputStream in = getClass().getResourceAsStream("/configuration.json");
        ObjectMapper mapper = new ObjectMapper();

        try {
            this.configuration = mapper.readValue(in, Configuration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
