package edu.aamu.myhealthcaresystem.nonin;

import java.io.IOException;
import java.util.List;

class NoninFrameSize {
    public static final int FRAME_SIZE = 5;
    public static final int START_BYTE = 1;
    private static final int SNSA_BIT = 3;
    private static final int SNSD_BIT = 6;
    private static final int SYNC_BIT = 0;
    private int endByteIndex;
    private byte plethysmographic;
    private byte status;
    private byte value;

    NoninFrameSize(List<Byte> byteArray, int endIndex) throws IOException {

        if (byteArray.size() != FRAME_SIZE) {
            throw new IOException("Incorrect Number of Bytes in Frame");
        }

        byte[] frame = new byte[FRAME_SIZE];

        for (int j = 0; j < FRAME_SIZE; j += START_BYTE) {
            frame[j] = ((Byte) byteArray.get(j)).byteValue();
        }

        if (frame[0] != (byte) 1) {
            throw new IOException("Frame does not begin with Start Byte!");
        } else if ((frame[START_BYTE] & 128) != 128) {
            throw new IOException("Status Byte does not have Bit 7 set to 1!");
        } else if ((frame[4] & 255) != (((((frame[0] + frame[START_BYTE]) + frame[2]) + frame[SNSA_BIT]) % 256) & 255)) {
            throw new IOException("Checksum Failure!");
        } else {
            status = frame[START_BYTE];
            plethysmographic = frame[2];
            value = frame[SNSA_BIT];
            endByteIndex = endIndex;
        }
    }

    private boolean isBitSet(byte value, int position) {

        if (((value >> position) & START_BYTE) == START_BYTE) {
            return true;
        }
        return false;
    }

    boolean isFirstFrame() {
        return isBitSet(status, 0);
    }

    boolean sensorConnected() {
        return !isBitSet(status, SNSD_BIT);
    }

    boolean unusableData() {
        return isBitSet(status, SNSA_BIT);
    }

    byte getFrameValue() {
        return value;
    }

    byte getPlethysmographic() {
        return plethysmographic;
    }

    int getEndByteIndex() {
        return endByteIndex;
    }
}
