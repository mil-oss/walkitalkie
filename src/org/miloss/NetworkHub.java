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

/**
 * 
 */

package org.miloss;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.LocalSocket;
import android.net.wifi.WifiManager;

/**
 * 
 *
 */
public class NetworkHub {
	DatagramSocket mNet;
	LocalSocket mLocal;

	Thread mNet2Local;
	Thread mLocal2Net;

	/**
	 * Initialize a UDP communication path accessible via a file descriptor
	 * 
	 * @param ctx
	 *            The Application context
	 * @param port
	 *            The UDP port to talk over
	 */
	public NetworkHub(Context ctx, int port) throws IOException {
		mNet = new DatagramSocket(port, getWifiBroadcastAddress(ctx));
		mLocal = new LocalSocket();
		
		new BufferedOutputStream(mLocal.getOutputStream());
		
		// Launch the socket IO threads
		mLocal2Net = new Thread(
			new DatagramWriterThread(mNet, 
				new BufferedInputStream(mLocal.getInputStream())));
		
		mLocal2Net.start();
		
		mNet2Local = new Thread(
			new DatagramReaderThread(mNet, 
				new BufferedOutputStream(mLocal.getOutputStream())));
		mNet2Local.start();
	}

	/**
	 * Retrieve the file descriptor that can be used to read and write to the
	 * network connection
	 * 
	 * @return
	 */
	public FileDescriptor getFileDescriptor() {
		return mLocal.getFileDescriptor();
	}

	/**
	 * Shamelessly taken from
	 * http://code.google.com/p/boxeeremote/wiki/AndroidUDP
	 * 
	 * @return
	 * @throws IOException
	 */
	protected InetAddress getWifiBroadcastAddress(Context ctx)
			throws IOException {
		WifiManager wifi = (WifiManager) ctx
				.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}
}
