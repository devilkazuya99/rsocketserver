package ong.ternchow.rsocketdemo;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class Application {

    public static final int TCP_PORT = 8910;
    
    public static void main(String[] args) {
        log.info("Starting Server.");
        CloseableChannel channel = 
                RSocketServer.create(SocketAcceptor.with(new MyRSocket()))
                             // Enable Zero Copy
                             .payloadDecoder(PayloadDecoder.ZERO_COPY)
                             .bind(TcpServerTransport.create(TCP_PORT))
                             .block();
        if(channel != null) {
            channel.onClose().block();
        }
    }
    
    private static class MyRSocket implements RSocket {
        @Override
        public Mono<Payload> requestResponse(Payload payload) {
            return Mono.just(payload);
        }
    }
    
}
