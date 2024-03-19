package org.hlumak.connector;

import org.hlumak.dto.CSVDTO;

import java.io.*;

public class CSVConnector implements Connector<CSVDTO> {

    @Override
    public CSVDTO read(String path) {
        StringBuilder result = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return new CSVDTO(result.toString());
    }

    @Override
    public void write(String path, CSVDTO dto) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(dto.getParseString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
