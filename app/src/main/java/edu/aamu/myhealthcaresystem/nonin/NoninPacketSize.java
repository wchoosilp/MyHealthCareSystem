package edu.aamu.myhealthcaresystem.nonin;

import android.os.Bundle;

import java.io.IOException;
import java.util.List;

class NoninPacketSize {
    static final String CONNECTED = "connected";
    static final String PLETHYSMOGRAPHIC = "plethysmographic";
    static final String USABLE = "usable";
    static final String PULSE = "pulse";
    static final String OXYGEN = "oxygen";
    public static byte ETX = (byte) 0;
    public static byte OP_CODE = (byte) 0;
    public static byte START_OF_PACKET = (byte) 0;
    static final int PACKET_SIZE = 25;
    private NoninFrameSize[] frames;

    static {
        START_OF_PACKET = (byte) 2;
        OP_CODE = (byte) 112;
        ETX = (byte) 3;
    }

    public NoninPacketSize(List<NoninFrameSize> pktFrames) throws IOException {

        if (pktFrames.size() != PACKET_SIZE) {
            throw new IOException("Packet has incorrect number of frames!");
        }

        frames = (NoninFrameSize[]) pktFrames.toArray(new NoninFrameSize[PACKET_SIZE]);

        if (frames[0].isFirstFrame()) {
            for (int i = 1; i < frames.length; i++) {
                if (frames[i].isFirstFrame()) {
                    throw new IOException("Got a first frame not in first position!");
                }
            }
            return;
        }
        throw new IOException("Packet's first frame is not a first frame!");
    }

    int getLastUnusedByte() {
        return frames[24].getEndByteIndex();
    }

    boolean sensorConnected() {

        for (NoninFrameSize frame : frames) {
            if (!frame.sensorConnected()) {
                return false;
            }
        }
        return true;
    }

    boolean unusableData() {

        for (NoninFrameSize frame : frames) {
            if (frame.unusableData()) {
                return true;
            }
        }
        return false;
    }

    int getPluseRate() {

        byte msb = frames[19].getFrameValue();
        return ((msb << 7) | frames[20].getFrameValue()) & 511;
    }

    int getExtendedPluseRate() {

        byte msb = frames[21].getFrameValue();
        return ((msb << 7) | frames[22].getFrameValue()) & 511;
    }

    int getOxygenLevel() {
        return frames[8].getFrameValue() & 127;
    }

    int getExtendedOxygenLevel() {
        return frames[16].getFrameValue() & 127;
    }

    int[] getPlethysmographic() {

        int[] readings = new int[PACKET_SIZE];
        for (int i = 0; i < PACKET_SIZE; i++) {
            readings[i] = frames[i].getPlethysmographic() & 255;
        }
        return readings;
    }

    Bundle getParsedDataBundle() {

        Bundle parsedPkt = new Bundle();
        parsedPkt.putBoolean(CONNECTED, sensorConnected());
        parsedPkt.putBoolean(USABLE, unusableData());
        parsedPkt.putInt(PULSE, getExtendedPluseRate());
        parsedPkt.putInt(OXYGEN, getExtendedOxygenLevel());
        parsedPkt.putIntArray(PLETHYSMOGRAPHIC, getPlethysmographic());
        return parsedPkt;
    }
}
