package com.pocolifo.robobase.dashboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
    public String fileContents(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/public/" + fileName)))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
        
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        }
    }
}
