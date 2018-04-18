/*
 * Fiberhome SDN Platform
 * Copyright (c)  Fiberhome Technologies.
 * 88,YouKeYuan Road,Hongshan District.,Wuhan,P.R.China,
 * Wuhan Research Institute of Post &amp; Telecommunication.
 * All rights reserved.
 */
package org.onosproject.dhcp.channel;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.onlab.packet.DHCP;
import org.onlab.packet.Ip4Address;
import org.onosproject.dhcp.impl.DhcpManager;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * 创建人: hwpeng5896.
 * 创建时间 2017-04-27 下午6:59
 * 功能描述:.
 **/

public class DhcpMessageHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    DhcpManager.onlyDhcpPacketProcessor processor;

    String client;
    boolean flags = false;
    String mac;



    public DhcpMessageHandler(DhcpManager.onlyDhcpPacketProcessor processor)
    {
        this.processor = processor;
    }
    @Override
    protected void channelRead0(io.netty.channel.ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {


        DatagramSocket socket = new DatagramSocket();
        Channel ch = channelHandlerContext.channel();
        ByteBuf buf = (ByteBuf) datagramPacket.copy().content();
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
//        System.out.println("dhcphander receive message");
        DHCP message = new DHCP();
        message = (DHCP)message.deserialize(req,0 , req.length);
        if(message.getClientIPAddress() != 0){
            client = Ip4Address.valueOf(message.getClientIPAddress()).toString();
            System.out.println("IP： " + client.toString());
            message = processor.onlyProcessDhcpPacket(message);
            System.out.println("message: " + message.toString());
            if(message == null){
                System.out.println("error ! null");
                return;
            }
        }else{
            client = Ip4Address.valueOf("255.255.255.255").toString();
            System.out.println("IP： " + client.toString());
            message = processor.onlyProcessDhcpPacket(message);
            System.out.println("message: " + message.toString());
            if(message == null){
                System.out.println("error ! null");
                return;
            }
            message.setFlags((short)(-32768));
        }
//        if(message.getFlags() != 0){
//            if(message.getClientIPAddress() != 0){
//                client = Ip4Address.valueOf(message.getClientIPAddress()).toString();
//                System.out.println("IP： " + client.toString());
//                message = processor.onlyProcessDhcpPacket(message);
//            }else{
//                client = Ip4Address.valueOf("255.255.255.255").toString();
//                System.out.println("IP： " + client.toString());
//                message = processor.onlyProcessDhcpPacket(message);
//                message.setFlags((short)(-32768));
//            }
//
////            System.out.println("----------------receive dhcp message-------------");
////            System.out.println("处理前： " + message.toString());
//        }else{
//            flags = true;
//            int id = message.getTransactionId();
//            message = processor.onlyProcessDhcpPacket(message);
//            if(message == null){
//                System.out.println("process after the message is null" + id);
//                return;
//            }
//            client = Ip4Address.valueOf(message.getYourIPAddress()).toString();
//            mac = MacAddress.valueOf(message.getClientHardwareAddress()).toString();
//            addArp(client,mac);
//            System.out.println("mac: " + mac);
//
////            System.out.println("short :" + ((short)(-32768)));
//        }
        // System.out.println("处理后的DHCP报文： " + message.toString());
        channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(message.serialize()), new InetSocketAddress(client,68))).sync();
        System.out.println("fa chu packet!");
//        if(flags){
//            deleteArp(client);
//            flags = false;
//        }
//        if(DHCPPacketType.getType(message.getOption(DHCP.DHCPOptionCode.OptionCode_MessageType).getData()[0]) == DHCPPacketType.DHCPACK){
//            notifyNetconf(Ip4Address.valueOf(message.getYourIPAddress()).toString());
//        }
    }


    private void addArp(String ip, String mac){
        StringBuffer sb = new StringBuffer("sudo arp -s ");
        sb = sb.append(ip);
        sb.append(" ");
        sb.append(mac);
        try {
            Process process = Runtime.getRuntime().exec(sb.toString());
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                System.out.println("添加执行出错。。。");
            }else{
                System.out.println("成功添加。。。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void deleteArp(String ip){
        StringBuffer sb = new StringBuffer("sudo arp -d ");
        sb = sb.append(ip);
        try {
            Process process = Runtime.getRuntime().exec(sb.toString());
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                System.out.println("删除执行出错。。。");
            }else{
                System.out.println("删除成功。。。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    private void notifyNetconf(String ip){
//        ArrayList devices = new ArrayList();
//        NotifyNetconf notifyNetconf = new NotifyNetconf();
//        Device device = new Device();
//        device.setUsername("root");
//        device.setPassword("root");
//        device.setIp(ip);
//        device.setPort(830);
//        devices.add(device);
//        notifyNetconf.setDevices(devices);
//        Map response = GetJsonStringAndNotify.sendPost(GetJsonStringAndNotify.getJson(notifyNetconf).toString());
//        System.out.println("response: " + (String)response.get("Content"));
//    }
}
