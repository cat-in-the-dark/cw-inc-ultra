package com.catinthedark.cw_inc.lib;

/**
 * Created by over on 08.11.14.
 */
public abstract class AbstractSystemDef {
    protected final Port[] ports;
    protected final Updater[] updaters;
    protected final boolean isQueueBlocking;
    public final int masterDelay;

    private AbstractSystemDef(Updater[] updaters, Port[] ports, int masterDelay, boolean isQueueBlocking) {
        this.updaters = updaters;
        this.ports = ports;
        this.masterDelay = masterDelay;
        this.isQueueBlocking = isQueueBlocking;
    }

    /**
     * create update-only system without any inbound ports
     *
     * @param updaters    - list of updaters
     * @param masterDelay - system step delay
     */
    public AbstractSystemDef(Updater[] updaters, int masterDelay) {
        this(updaters, new Port[]{}, masterDelay, false);
    }

    /**
     * create updatable system with inbound ports.
     * Warn: ports queue is non blocking! System will poll every port at every step
     *
     * @param updaters    - list of updaters
     * @param ports       - list of inbound ports
     * @param masterDelay - system step delay
     */
    public AbstractSystemDef(Updater[] updaters, Port[] ports, int masterDelay) {
        this(updaters, ports, masterDelay, false);
    }

    /**
     * Create fully even-driven system.
     * System will be blocked until any message inbox
     *
     * @param ports - list of inbound ports
     */
    public AbstractSystemDef(Port[] ports) {
        this(new Updater[]{}, ports, 0, true);
    }


    void update() throws InterruptedException {
        for (Updater updater : updaters)
            updater.fn.doLogic(0, 0);
        for (Port port : ports)
            port.dispatch(isQueueBlocking);
    }

    void start() {
        System.out.println("Starting system: " + this.toString());
    }
}