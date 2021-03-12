package com.minexd.api.balancing

import io.undertow.Handlers
import io.undertow.Undertow
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient
import java.net.URI


fun main() {
    APIBalancing().start()
}

class APIBalancing {

    lateinit var server: Undertow

    fun start() {
        val proxyClient = LoadBalancingProxyClient()
            .setConnectionsPerThread(20)

        val hosts = listOf("http://localhost:4567")
        for (host in hosts) {
            proxyClient.addHost(URI(host))
        }

        server = Undertow.builder()
            .addHttpListener(8080, "localhost")
            .setHandler(Handlers.proxyHandler(proxyClient)).build()

        object : Thread("Undertow-Thread") {
            override fun run() {
                server.start()
            }
        }.start()
    }

}