/*
 * Fiberhome SDN Platform
 * Copyright (c)  Fiberhome Technologies.
 * 88,YouKeYuan Road,Hongshan District.,Wuhan,P.R.China,
 * Wuhan Research Institute of Post &amp; Telecommunication. 
 *
 * All rights reserved.
 */
package org.onosproject.dhcp.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人: ubuntu
 * 创建时间 2017-05-05 下午3:23
 * 功能描述:
 **/

public class NotifyNetconf{

    public List<Device> devices = new ArrayList<>();

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "NotifyNetconf{" +
                "devices=" + devices +
                '}';
    }


}
