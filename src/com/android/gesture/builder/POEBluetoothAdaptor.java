package com.android.gesture.builder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


public class POEBluetoothAdaptor {
	
	BluetoothAdapter bluetooth;
	String mStatus;
	String connection_status;
	String bluetooth_name;
	BluetoothDevice connected_device;
	BluetoothSocket mSocket;
	private InputStream mInputStream;
	private OutputStream mOutputStream;
	
	public POEBluetoothAdaptor() {
		bluetooth = BluetoothAdapter.getDefaultAdapter();
		
		if(bluetooth != null)
		{
			if (bluetooth.isEnabled()) {
			    String mydeviceaddress = bluetooth.getAddress();
			    String mydevicename = bluetooth.getName();
			    mStatus = mydevicename + " : " + mydeviceaddress;
			}
			else
			{
			    mStatus = "Bluetooth is not Enabled.";
			}
		}
	}
	
	public void connect() {
		// TO DO
		// 1. Find the available bluetooth device
		if (bluetooth.isEnabled()){
			Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
	
			for(BluetoothDevice bt : pairedDevices)
			{
				if (bt.getName().equals("SeeedBTSlave"))
				{
					bluetooth_name = bt.getName();
					connected_device = bt;
					break;
				}
			}
			 
			connection_status = bluetooth_name;
			
		}
		else
		{
			connection_status = "Bluetooth is not Enabled.";
		}
		// 2. Choose our Arduino bluetooth shield
		
		// 3. Initiate pairing
	}
	
	public void send(int[] toArduino) throws IOException {
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        if (connected_device != null) {
        	
        }
		mSocket = connected_device.createRfcommSocketToServiceRecord(uuid);
        mSocket.connect();
        mOutputStream = mSocket.getOutputStream();
        //mInputStream = mSocket.getInputStream();
		// Send toArduino array to serial port
        byte[] bytetoArduino = new byte[toArduino.length];
        for (int i = 0; i < toArduino.length; i++) {
        	bytetoArduino[i] = (byte) toArduino[i];
        }
        try {
        	mOutputStream.write(bytetoArduino);
        } catch (IOException e) {
            Log.d("bluetooth", "...Error data send: " + e.getMessage() + "...");     
          }
        
	}
}
