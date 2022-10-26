package com.ferox.fs;

import com.ferox.io.RSBuffer;
import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;

public class AreaDefinition implements Definition {

    public int id;

    private int spriteId = -1;
    private int anInt1967 = -1;
    private String name;
    private int anInt1959;
    private int anInt1968 = 0;
    private int[] anIntArray1982;
    private String aString1970;
    private int[] anIntArray1981;
    private int anInt1980;
    private byte[] aByteArray1979;
    private String[] aStringArray1969 = new String[5];

    private Map<Integer, Object> areas = new HashMap<>();


    public AreaDefinition(int id, byte[] data) {
        this.id = id;

        if (data != null && data.length > 0)
            decode(new RSBuffer(Unpooled.wrappedBuffer(data)));

    }

    void decode(RSBuffer buffer) {
        while (true) {
            int op = buffer.readUByte();
            if (op == 0)
                break;
            decode(buffer, op);
        }
    }

    void decode(RSBuffer buffer, int code) {
        if (code == 1) {
            spriteId = buffer.readLEInt();
        } else if (code == 2) {
            anInt1967 = buffer.readLEInt();
        } else if (code == 3) {
            name = buffer.readString();
        } else if (code == 4) {
            anInt1959 = buffer.readUByte();
        } else if (code == 5) {
            buffer.readUByte();
        } else if (code == 6) {
            anInt1968 = buffer.readByte();
        } else if (code == 7) {
            int flags = buffer.readByte();
            if ((flags & 0x1) == 0) {
            }
            if ((flags & 0x2) == 2) {
            }
        } else if (code == 8) {
            buffer.readByte();
        } else if (code >= 10 && code <= 14) {
            aStringArray1969[code - 10] = buffer.readString();
        } else if (code == 15) {
            int size = buffer.readByte();
            anIntArray1982 = new int[size * 2];

            for (int i = 0; i < size * 2; ++i) {
                anIntArray1982[i] = buffer.readShort();
            }

            buffer.readInt();
            int size2 = buffer.readByte() & 0xFF;
            anIntArray1981 = new int[size2];

            for (int i = 0; i < anIntArray1981.length; ++i) {
                anIntArray1981[i] = buffer.readInt();
            }

            aByteArray1979 = new byte[size];

            for (int i = 0; i < size; ++i) {
                aByteArray1979[i] = buffer.readByte();
            }
        } else if (code == 17) {
            aString1970 = buffer.readString();
        } else if (code == 18) {
            buffer.readLEInt();
        } else if (code == 19) {
            anInt1980 = buffer.readShort();
        } else if (code == 21) {
            buffer.readInt();
        } else if (code == 22) {
            buffer.readInt();
        } else if (code == 23) {
            buffer.readByte();
            buffer.readByte();
            buffer.readByte();
        } else if (code == 24) {
            buffer.readShort();
            buffer.readShort();
        } else if (code == 25) {
            buffer.readLEInt();
        } else if (code == 28) {
            buffer.readByte();
        } else if (code == 29) {
            buffer.readByte();
        } else if (code == 30) {
            buffer.readByte();
        }
    }

    public int getId() { return id; }
    public int getSpriteId() { return spriteId; }
    public String defaultString() { return name; }

    public String getString(int key) { return areas.getOrDefault(key, defaultString()).toString(); }

    public Map<Integer, Object> areas() {
        return areas;
    }

    @Override
    public String toString() {
        return "AreaDefinition {" +
            "\n\tid=" + id +
            ",\n\tspriteId=" + spriteId +
            ",\n\tname='" + name + '\'' +
            "\n}\n";

    }

}
