package com.iot.container.udp;

import io.netty.channel.ChannelOption;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.util.NetUtil;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.netty.Connection;
import reactor.netty.resources.LoopResources;
import reactor.netty.udp.UdpServer;

import java.net.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class udpClient1 {


    @Test
    public void test() throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(1);

        final InetAddress multicastGroup = InetAddress.getByName("230.0.0.1");
        MulticastSocket multicast = new MulticastSocket();
        final NetworkInterface multicastInterface = findMulticastEnabledIPv4Interface();

        Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
        NetworkInterface networkInterface = null;
        while (ifaces.hasMoreElements()){
            networkInterface = ifaces.nextElement();
            if(isMulticastEnabledIPv4Interface(networkInterface))
                break;
        }
        multicast.joinGroup(new InetSocketAddress(multicastGroup, 6666), multicastInterface);
        String msg = "我来测试一下数据";
        multicast.send(new DatagramPacket(msg.getBytes(),
                msg.getBytes().length,
                multicastGroup,
                6666));
        multicast.close();
        countDownLatch.await();
    }


    private boolean isMulticastEnabledIPv4Interface(NetworkInterface iface) {
        try {
            if (!iface.supportsMulticast() || !iface.isUp()) {
                return false;
            }
        }
        catch (SocketException se) {
            return false;
        }

        for (Enumeration<InetAddress> i = iface.getInetAddresses();
             i.hasMoreElements(); ) {
            InetAddress address = i.nextElement();
            if (address.getClass() == Inet4Address.class) {
                return true;
            }
        }

        return false;
    }

    private NetworkInterface findMulticastEnabledIPv4Interface() throws SocketException {
        if (isMulticastEnabledIPv4Interface(NetUtil.LOOPBACK_IF)) {
            return NetUtil.LOOPBACK_IF;
        }

        for (Enumeration<NetworkInterface> ifaces =
             NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
            NetworkInterface iface = ifaces.nextElement();
            if (isMulticastEnabledIPv4Interface(iface)) {
                return iface;
            }
        }

        throw new UnsupportedOperationException(
                "This test requires a multicast enabled IPv4 network interface, but " + "none" + " " + "were found");
    }

}
