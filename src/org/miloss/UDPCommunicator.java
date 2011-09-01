package org.miloss;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class UDPCommunicator implements Communicator {

   private final List<SoundBiteListener> listeners = new CopyOnWriteArrayList<SoundBiteListener>();
   private final InetSocketAddress address;
   private final DatagramSocket socket;

   public UDPCommunicator(InetSocketAddress address) throws Exception {
       socket = new DatagramSocket(address.getPort());
       this.address = address;
       Thread listenerThread = new Thread("UDP listener") {

           public void run() {
               byte[] buffer = new byte[65536];
               DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

               try {
                   socket.receive(receivePacket);
                   // FIXME: Check we did not receive our own packet.
                   // Create a new buffer with the actual data, as the length is likely to be smaller.
                   byte[] received = new byte[receivePacket.getLength()];
                   System.arraycopy(receivePacket.getData(), 0, received, 0, receivePacket.getLength());
                   SoundBite soundBite = new SoundBite(0, received);
                   for (SoundBiteListener listener : listeners) {
                       listener.soundBiteReceived(soundBite);
                   }
               } catch (IOException ioe) {
                 // FIXME: log this
               }
           }

       };
       listenerThread.setDaemon(true);
       listenerThread.start();
   }

   public void send(SoundBite soundBite) throws IOException {
       // FIXME: Need to break into multiple DatagramPackets if payload is bigger than 64K
       // FIXME: Need to send along audio format metadata.
       byte[] payload = soundBite.data;
       DatagramPacket packet = new DatagramPacket(payload, payload.length, address);
       socket.send(packet);
   }

   public void addSoundBiteListener(SoundBiteListener listener) {
       listeners.add(listener);
   }

   public void removeSoundBiteListener(SoundBiteListener listener) {
       listeners.remove(listener);
   }
 
}
