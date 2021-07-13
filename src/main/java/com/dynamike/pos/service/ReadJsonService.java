package com.dynamike.pos.service;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.*;

@Service
public class ReadJsonService {

    private final static Logger LOG = (Logger) LoggerFactory.getLogger(ReadJsonService.class);

    public String readJsonFile(@NotNull String fileName) {
        StringBuilder sb = new StringBuilder();
        try (InputStream path = this.getClass().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(path))){
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                sb.append(line);
            }
        } catch (Exception e) {
            LOG.error("Read json file failed: ", e);
        }
        return sb.toString();
    }
}
