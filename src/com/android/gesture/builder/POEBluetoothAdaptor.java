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
	String Socket_connection_status;
	String Sending_status;
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
	
	public void send(int[] toArduino) throws IOException 
	{
		
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
		/*
		if (Build.VERSION.SDK_INT >= 10){
			try {
				final Method  m = connected_device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
				mSocket = (BluetoothSocket) m.invoke(connected_device, uuid);
	          } catch (Exception e) {
	  			mSocket = connected_device.createRfcommSocketToServiceRecord(uuid);
	          }
			
		}
		else {
			mSocket = connected_device.createRfcommSocketToServiceRecord(uuid);
		}
		*/
		mSocket = connected_device.createRfcommSocketToServiceRecord(uuid);
		try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mSocket.connect();
        	Socket_connection_status = "Sending made";

        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
        	Socket_connection_status = "Sending not made";
          /*
        	try {
                mSocket.close();
            	Socket_connection_status = "Sending was not closed";	
            } catch (IOException closeException) { }
            //return;*/
        }
		try
		{
        	mSocket.connect();
        	Socket_connection_status = "Sending made";
        } catch (Exception e){
        	//Socket_connection_status = "Sending is not made";
        	Log.d("bluetooth", "...Error data send: " + e.getMessage() + "...");   
        }
        
        mOutputStream = mSocket.getOutputStream();
        mInputStream = mSocket.getInputStream();
		// Send toArduino array to serial port
        byte[] bytetoArduino = new byte[toArduino.length];
        for (int i = 0; i < toArduino.length; i++) {
        	bytetoArduino[i] = (byte) toArduino[i];
        }
        bluetooth.cancelDiscovery();
        //Socket_connection_status = "wow";
        //mOutputStream.write(2);
        try {
        	mOutputStream.write(bytetoArduino,0,bytetoArduino.length);
        	Socket_connection_status = "wow";
        }
        
        catch (IOException e) 
        {
        	Socket_connection_status = "T.T";
            Log.d("bluetooth", "...Error data send: " + e.getMessage() + "...");     
        }
        closeBT();
    }
	public void closeBT() throws IOException
    {
        mOutputStream.close();
        mInputStream.close();
        mSocket.close();
	}
}

