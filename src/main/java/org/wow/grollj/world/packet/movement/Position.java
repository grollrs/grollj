package org.wow.grollj.world.packet.movement;

import java.nio.ByteBuffer;

public class Position {
    float x;
    float y;
    float z;
    float o;

    public Position(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(float x, float y, float z, float o) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.o = o;
    }

    public void writeToByteBuffer(ByteBuffer bb){
        bb.putFloat(x);
        bb.putFloat(y);
        bb.putFloat(z);
        bb.putFloat(o);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getO() {
        return o;
    }

    public void setO(float o) {
        this.o = o;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", o=" + o +
                '}';
    }
}
