/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http.client.packets;

import java.util.List;

/**
 *
 * @author p1609594
 */
public class Header {

    private String header;

    public Header(List<Byte> bytes) {
        header = "";
        for (Byte b : bytes) {
            header += (char) (byte) b;
        }
        
    }

    public String getHeader() {
        return header;
    }

    public String getContentType() {
        String[] lines = header.split("\n");
        for (String s : lines) {
            if (s.matches("Content-Type: .*")) {
                return s.split(" ")[1];
            }
        }
        return null;
    }
    
    public int getCode() {
        return Integer.valueOf(header.split(" ")[1]);
    }
}
