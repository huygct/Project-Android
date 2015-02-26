package com.gct;

import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by thuynghi on 12/17/2014.
 */
public class Client {
    private Data serverData;
    public static String SERVERIP = "10.10.17.148";

    public static final int SERVERPORT = 2222;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    ObjectOutputStream  out;
    ObjectInputStream  in;

    /**
     * Constructor of the class. OnMessageReceived listens for the message
     * received from server
     */
    public Client(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by Client to the Server
     *
     * @param data text entered by client
     */
    public void sendMessage(Data data) {
        if (out != null) {
            try {
                String xxx = data.getId() + " - " + data.getMessage();
                Log.e("aa", xxx);
                out.writeObject(new Data(data.getId(), data.getMessage()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopClient() {
        mRun = false;
    }

    public void run() {
        mRun = true;

        try {
            InetAddress serverAddr = InetAddress.getByName("10.10.17.148");
            Log.e("serverAddr", serverAddr.toString());
            Log.e("TCP Client", "C: Connecting.........");

            // create a socket to make the connection with the server

            Socket socket = new Socket(serverAddr, SERVERPORT);
            Log.e("TCP Server IP", SERVERIP);

            try {
                // Send message to the server
                // OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                // out = new PrintWriter(new BufferedWriter(outputStreamWriter), true);
                out = new ObjectOutputStream(socket.getOutputStream());

                Log.e("TCP Client", "C: Sent.");
                Log.e("TCP Client", "C: Done.");

                // receive the message which the server sends back
//                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
//                in = new BufferedReader(inputStreamReader);
                in = new ObjectInputStream(socket.getInputStream());

                // in this while the client listens for the message sent by the server
                while (mRun) {
                    serverData = (Data) in.readObject();
                    if (serverData != null && mMessageListener != null) {
                        //Call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverData);
                    }
                    serverData = null;
                }
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverData + "'");
            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                // the soket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {
            Log.e("TCP", "S: Error", e);
        }
    }

    public interface OnMessageReceived {
        public void messageReceived(Data data);
    }
}
