package com.shu.iot;

import com.shu.iot.server.UDPServer;
import org.junit.jupiter.api.Test;

public class UDPTest {
    @Test
    public void serverTest(){
        UDPServer udpServer = new UDPServer();

        udpServer.init(8082);
        udpServer.start();
    }

    public static void main(String[] args) {
        UDPServer udpServer = new UDPServer();

        udpServer.init(8082);
        udpServer.start();
    }
}
