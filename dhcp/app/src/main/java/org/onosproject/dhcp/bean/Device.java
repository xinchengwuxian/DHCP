/*
 * Fiberhome SDN Platform
 * Copyright (c)  Fiberhome Technologies.
 * 88,YouKeYuan Road,Hongshan District.,Wuhan,P.R.China,
 * Wuhan Research Institute of Post &amp; Telecommunication. 
 *
 * All rights reserved.
 */
package org.onosproject.dhcp.bean;

/**
 * 创建人: ubuntu
 * 创建时间 2017-05-05 下午5:20
 * 功能描述:
 **/

public class Device {
    public String username;
    public String password;
    public String ip;
    public int port;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "device{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}