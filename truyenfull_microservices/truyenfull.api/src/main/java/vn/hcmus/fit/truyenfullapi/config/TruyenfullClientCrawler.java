package vn.hcmus.fit.truyenfullapi.config;

import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;
import org.springframework.stereotype.Component;
import vn.hcmus.fit.truyenfull.lib.TruyenFullCrawlerService;

/**
 * Created by Asus on 11/14/2019.
 */
@Component
public class TruyenfullClientCrawler extends TruyenFullCrawlerService.Client {
    public TruyenfullClientCrawler(TProtocolFactory tProtocolFactory) throws TTransportException {
        super(tProtocolFactory.getProtocol(new THttpClient("http://localhost:8081/truyenfull")));
    }
}
