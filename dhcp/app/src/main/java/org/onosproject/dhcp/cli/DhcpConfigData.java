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
package org.onosproject.dhcp.cli;

import org.apache.karaf.shell.commands.Command;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.core.CoreService;
import org.onosproject.dhcp.impl.DhcpConfig;
import org.onosproject.net.config.NetworkConfigRegistry;

/**
 * 输出dhcp配置相关的参数
 */
@Command(scope = "onos", name = "dhcp-data",
        description = "Lists all the default lease parameters offered by the DHCP Server")
public class DhcpConfigData extends AbstractShellCommand {

    @Override
    protected void execute() {
        CoreService coreService = AbstractShellCommand.get(CoreService.class);
        NetworkConfigRegistry networkConfigService = AbstractShellCommand.get(NetworkConfigRegistry.class);
        DhcpConfig cfg = networkConfigService.getConfig(coreService.getAppId("org.onosproject.dhcp"),DhcpConfig.class);
        System.out.println("cfg.ip() : " + cfg.ip());
        System.out.println("cfg.mac() : " + cfg.mac());
        System.out.println("cfg.startIp() : " + cfg.startIp());
        System.out.println("cfg.endIp() : " + cfg.endIp());
        System.out.println("cfg.broadcastAddress() : " + cfg.broadcastAddress());
        System.out.println("cfg.domainServer() : " + cfg.domainServer());
        System.out.println("cfg.subnetMask() : " + cfg.subnetMask());
//        JavaBeanTest testBean = new JavaBeanTest();
//        testBean.sendPost();


//        ArrayList devices = new ArrayList();
//        NotifyNetconf notifyNetconf = new NotifyNetconf();
//        Device device = new Device();
//        device.setUsername("admin");
//        device.setPassword("admin");
//        device.setIp("10.190.23.210");
//        device.setPort(17830);
//        devices.add(device);
//        notifyNetconf.setDevices(devices);
//        Map response = GetJsonStringAndNotify.sendPost(GetJsonStringAndNotify.getJson(notifyNetconf).toString());
//        System.out.println("response: " + (String)response.get("Content"));

    }
}
