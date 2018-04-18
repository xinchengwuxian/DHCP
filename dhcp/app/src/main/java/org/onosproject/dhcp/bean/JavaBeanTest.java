/*
 * Fiberhome SDN Platform
 * Copyright (c)  Fiberhome Technologies.
 * 88,YouKeYuan Road,Hongshan District.,Wuhan,P.R.China,
 * Wuhan Research Institute of Post &amp; Telecommunication. 
 *
 * All rights reserved.
 */
package org.onosproject.dhcp.bean;


import org.onosproject.dhcp.utils.JSONParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人: ubuntu
 * 创建时间 2017-05-05 下午3:36
 * 功能描述:
 **/

public class JavaBeanTest {
    public StringBuffer netconfHeader = new StringBuffer("{\"apps\":{\"org.onosproject.netconf\":");
    public StringBuffer needSend = new StringBuffer();
    public List<Device> devices = new ArrayList<Device>();

    public void sendPost() {
        NotifyNetconf notifyNetconf = new NotifyNetconf();
        Device device = new Device();
        device.setUsername("admin");
        device.setPassword("admin");
        device.setIp("10.190.23.210");
        device.setPort(17830);

        devices.add(device);
        notifyNetconf.setDevices(devices);
        String  bean = JSONParser.returnJsonString(notifyNetconf);
        System.out.println("输出bean: " + bean);
        needSend.append(netconfHeader);
        needSend.append(bean);
        needSend.append("}}");
        System.out.println("输出需要发送的bean: " + needSend);
    }
}