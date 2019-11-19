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
import java.util.concurrent.CountDownLatch;

public class udpServer2 {


    @Test
    public void test() throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(1);
        final InetAddress multicastGroup = InetAddress.getByName("230.0.0.1");
        final NetworkInterface multicastInterface = findMulticastEnabledIPv4Interface();
        final Collection<Connection> servers = new ArrayList<>();
        Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
        LoopResources resources = LoopResources.create("test");
        Connection server =
                UdpServer.create()
                        .option(ChannelOption.SO_REUSEADDR, true)
                        .addressSupplier(() -> new InetSocketAddress(6666))
                        .runOn(resources, InternetProtocolFamily.IPv4)
                        .handle((in, out) -> {
                            in.receive().asString().subscribe(System.out::println);
                            Flux.<NetworkInterface>generate(s -> {
                                if (ifaces.hasMoreElements()) {
                                    s.next(ifaces.nextElement());
                                }
                                else {
                                    s.complete();
                                }
                            }).flatMap(iface -> {
                                if (isMulticastEnabledIPv4Interface(iface)) {
                                    return in.join(multicastGroup,
                                            iface);
                                }
                                return Flux.empty();
                            }).subscribe();
                            return Flux.never();
                        })
                        .wiretap(true)
                        .bind()
                        .block(Duration.ofSeconds(30));
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
