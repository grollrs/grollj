package org.wow.grollj.world;

public class World {

    private Character character;

    private ServerTime serverTime;

    public ServerTime getServerTime() {
        return serverTime;
    }

    public void setServerTime(ServerTime serverTime) {
        this.serverTime = serverTime;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
}
