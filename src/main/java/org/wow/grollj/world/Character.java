package org.wow.grollj.world;

import org.wow.grollj.world.packet.movement.Position;

public class Character {
    int guid;
    String name;
    int lvl;
    int zone;
    int map;

    Position pos;


    public Character(int guid, String name, int lvl, int zone, int map, float x, float y, float z) {
        this.guid = guid;
        this.name = name;
        this.lvl = lvl;
        this.zone = zone;
        this.map = map;
        pos=new Position(x,y,z);
    }

    public int getGuid() {
        return guid;
    }

    public void setGuid(int guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}
