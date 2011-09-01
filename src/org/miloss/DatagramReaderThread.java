/* Copyright 2011 Chuck Atkins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.miloss;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * The input loop for reading from a datagram socket
 */
public class DatagramReaderThread implements Runnable {
	
	DatagramSocket mSource;
	OutputStream mDest;
	
	public DatagramReaderThread(DatagramSocket source, OutputStream dest) {
		mSource = source;
		mDest = dest;
	}
	
	public void run() {
		try
		{
			int len = mSource.getReceiveBufferSize();
			byte[] buf = new byte[len];
			DatagramPacket packet = new DatagramPacket(buf, len);
		
			while( true ) {
				mSource.receive(packet);
				mDest.write(packet.getData(), packet.getOffset(), packet.getLength());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
