package edu.aamu.myhealthcaresystem.nonin;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class NoninTCPSender extends Thread {
    private static final String TAG = "NONIN_WRIST0X2";
    DataInputStream inputStream;
    private String ipAddress;
    private boolean mRun;
    DataOutputStream outputStream;
    private boolean pause;
    private int port;
    private boolean running;

    public NoninTCPSender(int port, String ip) {
        this.ipAddress = ip;
        this.port = port;
        mRun = false;
        pause = true;
        running = false;
    }

    public void run() {
        running = true;
        pause = false;
        while (running) {
            while (!pause) {
                mRun = true;
                try {
                    InetAddress serverAddress = InetAddress.getByName(ipAddress);
                    Socket socket = new Socket(serverAddress, port);
                    try {
                        outputStream = new DataOutputStream(socket.getOutputStream());
                        inputStream = new DataInputStream(socket.getInputStream());
                        while (mRun) {
                            yield();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        outputStream.flush();
                        outputStream.close();
                        inputStream.close();
                        socket.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    sleep(3000);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
            try {
                sleep(3000);
            } catch (InterruptedException e32) {
                e32.printStackTrace();
            }
        }
        Log.d(TAG, "Closing Socket!");
    }

    public void Pause() {
        mRun = false;
        pause = true;
    }

    public void UnPause() {
        mRun = true;
        pause = false;
    }

    public void sendMessage(byte[] buffer, int off, int N) {
        if (outputStream != null) {
            try {
                outputStream.write(buffer, off, N);
            } catch (IOException e) {
                e.printStackTrace();
                mRun = false;
            }
        }
    }

    public void Stop() {
        Log.d(TAG, "Stopping Client Socket!");
        mRun = false;
        pause = true;
        running = false;
    }
}
