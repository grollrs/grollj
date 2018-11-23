package org.wow.grollj.world;

import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wow.grollj.auth.AuthSession;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

public class WorldSocketManager{

    private static WorldSocketManager instance = new WorldSocketManager();
    private WorldSocketManagerThread worker = new WorldSocketManagerThread();
    private boolean started = false;
    Logger logger = LogManager.getLogger(WorldSocketManager.class);


    private WorldSocketManager() {
    }

    public static WorldSocketManager getInstance() {
        return instance;
    }



    private LinkedBlockingQueue<AuthSession> authSessionQueue = new LinkedBlockingQueue<>();
    private EventLoopGroup workerGroup;

    public void init(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    public void start() {
        if(started){
            throw new IllegalStateException("WorldSocketManager has already been started");
        }
        else{
            started=true;
            worker.start();
        }
    }

    public void schedule(AuthSession session) {
        boolean result = authSessionQueue.offer(session);
        if(!result){
            logger.warn("Could not schedule connect");
        }
    }

    class WorldSocketManagerThread extends Thread{
        @Override
        public void run() {
            if(workerGroup==null){
                throw new IllegalStateException("WorldSocketManager was not initilaized");
            }
            while(!Thread.currentThread().isInterrupted()){
                AuthSession authSession = null;
                try {
                    authSession = authSessionQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Starting new WorldSocket");
                WorldSocket ws = new WorldSocket(workerGroup);
                WorldSession worldSession = new WorldSession(authSession);
                ChannelFuture future = ws.start(worldSession);
                //TODO save this somewhere

            }
        }
    }
}
