import Processing.Processor;
import Processing.usableNetwork;
import Technical.Message;
import Technical.Packet;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test {

    /**check encryption*/

    void test0() {
        Message message = new Message(1, 1, new String("checkencryption"));

        Packet packet = new Packet((byte) 1, (long) 4, message);
        byte[] encodedPacket = packet.toPacket();
        try {
            Packet dcddPacket = new Packet(encodedPacket);
            byte[] dPacket = dcddPacket.toPacket();
            assertEquals(packet.getBSource(), dcddPacket.getBSource());
            assertEquals(packet.getWCRC16_1(), dcddPacket.getWCRC16_1());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    void test1() throws Exception {

        Message message = new Message(1, 1, "kpkklkklgkdlvaookra[nj");
        Packet packet = new Packet((byte) 1, (long) 1111, message);

        byte[] packBytes = packet.toPacket();
        Packet packetFirst = new Packet(packBytes);//first packet

        byte[] packFirstBytes = packetFirst.toPacket();//bytes
        assert (Arrays.equals(packBytes, packFirstBytes));
    }

    //check packet description

    void test2() throws Exception {

        Message message = new Message(1, 1, "descriptioneeee");

        byte[] messageToBytes = message.toPacketPart();//msg into bytes
        Packet packet = new Packet((byte) 1, (long) 444, message);
//
        byte[] packetToBytes = packet.toPacket();//packet into bytes

        Packet packet_decoded = new Packet(packetToBytes);
        Message message_decoded = packet_decoded.getBMsq();

        byte[] decoded_messageToBytes = message_decoded.toPacketPart();//decoded msg into bytes
        assert (Arrays.equals(messageToBytes, decoded_messageToBytes));
    }


        void finalTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(12);
        for (int i = 0; i < 24; i++) {
            executorService.submit(() -> {
                usableNetwork tcp = new usableNetwork();
                tcp.receiveMessage();
            });
        }
        try {
            executorService.shutdown();
            while (!executorService.awaitTermination(24L, TimeUnit.HOURS)) {
                System.out.println("Please wait.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Processor.shutdown();

    }
}
