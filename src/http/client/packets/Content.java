/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http.client.packets;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author p1609594
 */
public class Content {
    List<Byte> bytes;

    public Content() {
        bytes = new ArrayList<>();
    }
    
    public void addByte(Byte b) {
        bytes.add(b);
    }
    
    public byte[] getContent() {
        byte[] bs = new byte[bytes.size()];
        int i = 0;
        for (byte b : bytes) {
            bs[i++] = b;
        }
        return bs;
    }
}
