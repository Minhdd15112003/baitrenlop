package ph25260.fpoly.client.config;

import java.net.URISyntaxException;

import io.socket.client.IO;

public class SocketConfig {
       public io.socket.client.Socket mSocket;
        {
            try {
                mSocket = IO.socket(ApiConfig.BASE_URL);
            }catch (URISyntaxException e){
                throw new RuntimeException(e);
            }
        }
}
