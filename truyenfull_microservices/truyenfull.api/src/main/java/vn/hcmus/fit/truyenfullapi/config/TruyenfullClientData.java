package vn.hcmus.fit.truyenfullapi.config;

import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;
import org.springframework.stereotype.Component;
import vn.hcmus.fit.truyenfull.lib_data.TruyenFullDataService;

/**
 * Created by Asus on 11/14/2019.
 */
@Component
public class TruyenfullClientData extends TruyenFullDataService.Client {
    public TruyenfullClientData(TProtocolFactory tProtocolFactory) throws TTransportException {
        super(tProtocolFactory.getProtocol(new THttpClient("http://localhost:8082/truyenfulldata")));
    }
}
