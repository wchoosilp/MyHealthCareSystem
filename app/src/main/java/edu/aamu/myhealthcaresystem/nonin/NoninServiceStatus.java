package edu.aamu.myhealthcaresystem.nonin;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.androidplot.xy.SimpleXYSeries;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoninServiceStatus extends Thread {
    private static final boolean status = true;
    public static final int MAX_DATA_POINTS = 300;
    private static final String TAG = "NONIN_WRIST0X2";
    private UUID MY_UUID;
    NoninTCPSender TCPout;
    private String address;
    private BluetoothSocket bluetoothSocket;
    byte[] buffer;
    List<Byte> dataBuffer;
    private BluetoothAdapter bluetoothAdapter;
    public InputStream inputStream;
    public OutputStream outputStream;
    public SimpleXYSeries plenthSeries;
    List<Byte> remainingData;
    private boolean running;
    private boolean streamingsent;
    private boolean pause;
    private boolean trying;
    public boolean connected;
    public int dataPointCounter;
    public int oxygen;
    private int timeout;
    public int pulse;
    int data;

    public NoninServiceStatus(String mac, int timeout, NoninTCPSender TCPout) {
        bluetoothAdapter = null;
        bluetoothSocket = null;
        outputStream = null;
        inputStream = null;
        connected = false;
        trying = false;
        running = status;
        pause = status;
        streamingsent = false;
        MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        dataBuffer = new ArrayList();
        remainingData = new ArrayList();
        address = mac;
        timeout = timeout;
        buffer = new byte[MAX_DATA_POINTS];
        plenthSeries = new SimpleXYSeries("NoninWaveform");
        for (int i = 0; i < MAX_DATA_POINTS; i++) {
            plenthSeries.addLast(Integer.valueOf(i), Integer.valueOf(100));
        }
        dataPointCounter = MAX_DATA_POINTS;
        running = status;
        connected = false;
        trying = false;
        pause = status;
        streamingsent = false;
        TCPout = TCPout;
    }

    public void run() {
        while (running) {
            if (!(connected || trying || pause)) {
                connected = Connect();
            }
            if (connected && !streamingsent) {
                SetMode();
            } else if (connected) {
                ProcessData();
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    sleep((long) timeout);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private void SetMode() {
        ByteBuffer packet = ByteBuffer.allocate(1000);
        packet.put(NoninPacketSize.START_OF_PACKET);
        packet.put(NoninPacketSize.OP_CODE);
        packet.put((byte) 2);
        packet.put((byte) 2);
        packet.put((byte) 2);
        packet.put(NoninPacketSize.ETX);
        try {
            outputStream.write(packet.array(), 0, packet.position());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            data = inputStream.read(buffer, 0, MAX_DATA_POINTS);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        this.streamingsent = status;
    }

    private boolean Connect() {
        trying = status;
        boolean ok = status;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            ok = false;
        }
/*        if (!this.bluetoothAdapter.isEnabled()) {
            ok = false;
        }*/
        if (ok) {
            try {
                bluetoothSocket = bluetoothAdapter.getRemoteDevice(address).createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                ok = false;
            }
            try {
                bluetoothSocket.connect();
            } catch (IOException e2) {
                ok = false;
                e2.printStackTrace();
                try {
                    bluetoothSocket.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
            try {
                outputStream = bluetoothSocket.getOutputStream();
                inputStream = bluetoothSocket.getInputStream();
            } catch (IOException e3) {
                ok = false;
                e3.printStackTrace();
            }
        }
        if (ok) {
            Log.e(TAG, "+++ YOU ARE NOW CONNECTED WITH NONIN WRIST0X2 BLUETOOTH DEVICE +++");
        } else {
            Log.e(TAG, "+++ UNABLE TO CONNECT WITH NONIN WRIST0X2 BLUETOOTH DEVICE +++");
        }
        this.trying = false;
        return ok;
    }

    private void DisConnect() {
        Log.e(TAG, "+++ YOU ARE NOW DISCONNECTING NONIN WRIST0X2 BLUETOOTH DEVICE +++");
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e22) {
                e22.printStackTrace();
            }
        }
        connected = false;
        Log.e(TAG, "+++ NONIN WRIST0X2 BLUETOOTH DEVICE HAS DISCONNECTED +++");
    }

    public void Stop() {
        running = false;
        DisConnect();
    }

    public void ProcessData() {
        try {
            data = inputStream.read(buffer, 0, MAX_DATA_POINTS);
            for (int i = 0; i < data; i++) {
                dataBuffer.add(new Byte(buffer[i]));
                SensorData();
            }
        } catch (IOException e) {
            DisConnect();
            e.printStackTrace();
        }
    }

    public void SensorData() {
        if (remainingData != null) {
            for (Byte b : remainingData) {
                dataBuffer.add(b);
            }
        }
        List<NoninPacketSize> packets = createPackets(createFrames(dataBuffer));
        int indexUnusedByte2 = -1;
        for (NoninPacketSize pkt : packets) {
            if (indexUnusedByte2 < pkt.getLastUnusedByte()) {
                indexUnusedByte2 = pkt.getLastUnusedByte();
            }
        }
        if (indexUnusedByte2 > 0) {
            dataBuffer.subList(0, indexUnusedByte2).clear();
        }
        byte[] newRemainingData = new byte[dataBuffer.size()];
        for (int i = 0; i < dataBuffer.size(); i++) {
            newRemainingData[i] = ((Byte) dataBuffer.get(i)).byteValue();
        }
        if (packets.size() > 0) {
            pulse = ((NoninPacketSize) packets.get(packets.size() - 1)).getPluseRate();
            oxygen = ((NoninPacketSize) packets.get(packets.size() - 1)).getOxygenLevel();
            for (int j = 0; j < packets.size(); j++) {
                int[] temp = ((NoninPacketSize) packets.get(j)).getPlethysmographic();
                for (int valueOf : temp) {
                    plenthSeries.addLast(Integer.valueOf(dataPointCounter), Integer.valueOf(valueOf));
                    dataPointCounter++;
                    if (dataPointCounter > MAX_DATA_POINTS) {
                        plenthSeries.removeFirst();
                    }
                }
            }
        }
    }

    private List<NoninFrameSize> createFrames(List<Byte> bytes) {
        List<NoninFrameSize> frames = new ArrayList();
        if (bytes.size() >= 10) {
            int i = 0;
            while (i < bytes.size() - 5) {
                int exclusiveEndByte = i + 5;
                if (((Byte) bytes.get(i)).byteValue() == (byte) 1 && ((Byte) bytes.get(exclusiveEndByte)).byteValue() == (byte) 1) {
                    try {
                        NoninFrameSize frame = new NoninFrameSize(bytes.subList(i, exclusiveEndByte), exclusiveEndByte);
                        if (frame != null) {
                            frames.add(frame);
                            i = exclusiveEndByte - 1;
                        }
                    } catch (IOException e) {
                        Log.w(TAG, e.getMessage());
                    }
                }
                i++;
            }
        }
        return frames;
    }

    private List<NoninPacketSize> createPackets(List<NoninFrameSize> frames) {
        List<NoninPacketSize> packets = new ArrayList();
        if (frames.size() >= 50) {
            int i = 0;
            while (i < frames.size() - 25) {
                int exclusiveEndFrame = i + 25;
                if (((NoninFrameSize) frames.get(i)).isFirstFrame() && ((NoninFrameSize) frames.get(exclusiveEndFrame)).isFirstFrame()) {
                    try {
                        NoninPacketSize packet = new NoninPacketSize(frames.subList(i, exclusiveEndFrame));
                        if (packet != null) {
                            packets.add(packet);
                            i = exclusiveEndFrame - 1;
                        }
                    } catch (IOException e) {
                        Log.w(TAG, e.getMessage());
                    }
                }
                i++;
            }
        }
        return packets;
    }

    public void Pause() {
        pause = status;
        DisConnect();
    }

    public void UnPause() {
        pause = false;
    }
}
