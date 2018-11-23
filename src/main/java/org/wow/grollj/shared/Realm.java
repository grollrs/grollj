package org.wow.grollj.shared;

public class Realm {
    private String addr;
    private String name;

    public Realm(String addr, String name) {
        this.addr = addr;
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public String getName() {
        return name;
    }
}
