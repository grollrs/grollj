package org.wow.grollj.world;

public class ServerTime {

    int lastReceivedServerTime;
    int lastLocalTime;
    int timeDiff;

    public ServerTime(int lastReceivedServerTime, int lastLocalTime, int timeDiff) {
        this.lastReceivedServerTime = lastReceivedServerTime;
        this.lastLocalTime = lastLocalTime;
        this.timeDiff = timeDiff;
    }

    public int getCurrentServerTime(){
        int curTime = (int) (System.currentTimeMillis()/1000);
        int lastTime = getLastLocalTime();
        int localDiff = curTime-lastTime;
        int oldServerTime = getLastReceivedServerTime();
        return oldServerTime+localDiff;
    }

    public int getLastReceivedServerTime() {
        return lastReceivedServerTime;
    }

    public void setLastReceivedServerTime(int lastReceivedServerTime) {
        this.lastReceivedServerTime = lastReceivedServerTime;
    }

    public int getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(int timeDiff) {
        this.timeDiff = timeDiff;
    }

    public int getLastLocalTime() {
        return lastLocalTime;
    }

    public void setLastLocalTime(int lastLocalTime) {
        this.lastLocalTime = lastLocalTime;
    }

    @Override
    public String toString() {
        return "ServerTime{" +
                "lastReceivedServerTime=" + lastReceivedServerTime +
                ", timeDiff=" + timeDiff +
                '}';
    }
}
