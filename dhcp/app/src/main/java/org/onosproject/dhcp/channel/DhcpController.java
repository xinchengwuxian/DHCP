/*
 * Copyright 2015-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.dhcp.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.onosproject.dhcp.impl.DhcpManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import static org.onlab.util.Tools.groupedThreads;

/**
 * The main controller class. Handles all setup and network listeners -
 * Distributed ownership control of pcc through IControllerRegistryService
 */
public class DhcpController {


    public DhcpController(DhcpManager.onlyDhcpPacketProcessor processor) {
        this.processor = processor;

    }

    private static final Logger log = LoggerFactory.getLogger(DhcpController.class);

//    private static final PcepFactory FACTORY1 = PcepFactories.getFactory(PcepVersion.PCEP_1);
    private DhcpManager.onlyDhcpPacketProcessor processor;
    private ChannelGroup cg;
    private boolean runEnable = false;
    private Thread runThread = new DhcpRunThread();

    // Configuration options
    private int udport = 67;
    private int workerThreads = 10;

    // Start time of the controller
    private long systemStartTime;


    private NioDatagramChannelFactory execFactory;

    // Perf. related configuration
    private static final int SEND_BUFFER_SIZE = 4 * 1024 * 1024;
    /**
     * To get system start time.
     *
     * @return system start time in milliseconds
     */
    public long getSystemStartTime() {
        return (this.systemStartTime);
    }

    /**
     * Tell controller that we're ready to accept pcc connections.
     */
    public void run() {

        try {
//            InetSocketAddress addr = new InetSocketAddress(udport);
//            ConnectionlessBootstrap bootstrap = createConnectionlessBootStrap();
////            channel.getConfig().setReuseAddress(true);
////            channel.getConfig().setBroadcast(true);
////            channel.getConfig().setReceiveBufferSize(1024*1024);
////            channel.getConfig().setSendBufferSize(1024*1024);
//            bootstrap.setOption("receiveBufferSize", 1048576);
//            bootstrap.setOption("sendBufferSize", 1048576);
//            bootstrap.setOption("broadcast",true);
//            bootstrap.setOption("reuseAddress", true);
//            //定义一个dhcpFactory
//            ChannelPipelineFactory dhcpFactory  = new DhcpPipelineFactory(processor);
//            bootstrap.setPipelineFactory(dhcpFactory);
//            cg = new DefaultChannelGroup();
//            cg.add(bootstrap.bind(addr));
//            System.out.println("bind success!"+ addr.getAddress().toString() + "/" +addr.getPort());

            InetSocketAddress addr = new InetSocketAddress(udport);
            System.out.println("addr : " + addr.getAddress().toString());
              Bootstrap b = new Bootstrap();
                           EventLoopGroup group = new NioEventLoopGroup();
                           b.group(group)
                                   .option(ChannelOption.SO_BROADCAST, true)
                                   .channel(NioDatagramChannel.class)
                                   .handler(new DhcpMessageHandler(processor));
                           // 服务端监听在9999端口
                            b.bind(addr).sync().channel().closeFuture().await();
            System.out.println("bind success!"+ addr.getAddress().toString() + "/" +addr.getPort());

        } catch (Exception e) {
            System.out.println("exception:"+e.toString() + e.getCause());
            throw new RuntimeException(e);
       }
    }

    /**
     * Creates server boot strap.
     *
     * @return ServerBootStrap
     */
    private ConnectionlessBootstrap createConnectionlessBootStrap() {
        if (workerThreads == 0) {
            execFactory = new NioDatagramChannelFactory(
                    Executors.newCachedThreadPool(groupedThreads("onos/pcep", "worker-%d")));
            return new ConnectionlessBootstrap(execFactory);
        } else {
            execFactory = new NioDatagramChannelFactory(
                    Executors.newCachedThreadPool(groupedThreads("onos/pcep", "worker-%d")), workerThreads);
            return new ConnectionlessBootstrap(execFactory);
        }
    }

    /**
     * Initialize internal data structures.
     */
    public void init() {
        // These data structures are initialized here because other
        // module's startUp() might be called before ours
        this.systemStartTime = System.currentTimeMillis();
    }

    public Map<String, Long> getMemory() {
        Map<String, Long> m = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        m.put("total", runtime.totalMemory());
        m.put("free", runtime.freeMemory());
        return m;
    }

    public Long getUptime() {
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        return rb.getUptime();
    }

//    /**
//     * Creates instance of Pcep client.
//     *
//     * @param pccId pcc identifier
//     * @param sessionID session id
//     * @param pv pcep version
//     * @param pktStats pcep packet statistics
//     * @return instance of PcepClient
//     */
//    protected PcepClientDriver getPcepClientInstance(PccId pccId, int sessionID, PcepVersion pv,
//            PcepPacketStats pktStats) {
//        PcepClientDriver pcepClientDriver = new PcepClientImpl();
//        pcepClientDriver.init(pccId, pv, pktStats);
//        pcepClientDriver.setAgent(agent);
//        return pcepClientDriver;
//    }

    /**
     * Starts the pcep controller.
     *
     */
    public void start() {
        System.out.println("dhcp controller Started");
//        this.agent = ag;
        this.init();
        runThread.start();
    }


    public void setRunEnable(boolean enable){
        this.runEnable = enable;
    }
    /**
     * Stops the pcep controller.
     */
    public void stop() {
        log.info("Stopped");
        execFactory.shutdown();
        runThread.stop();
    }

    private class DhcpRunThread extends Thread{

        @Override
        public void run() {
            try {
                InetSocketAddress addr = new InetSocketAddress(udport);
                System.out.println("addr : " + addr.getAddress().toString());
                Bootstrap b = new Bootstrap();
                EventLoopGroup group = new NioEventLoopGroup();
                b.group(group)
                        .option(ChannelOption.SO_BROADCAST, true)
                        .channel(NioDatagramChannel.class)
                        .handler(new DhcpMessageHandler(processor));
                // 服务端监听在9999端口
                b.bind(addr).sync().channel().closeFuture().await();
                System.out.println("bind success!"+ addr.getAddress().toString() + "/" +addr.getPort());

            } catch (Exception e) {
                System.out.println("exception:"+e.toString() + e.getCause());
                throw new RuntimeException(e);
            }
        }
    }
}
