package vn.hcmus.fit.truyenfull.crawler.config;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.hcmus.fit.truyenfull.crawler.controller.CrawlerController;
import vn.hcmus.fit.truyenfull.lib_crawler.TruyenFullCrawlerService;

/**
 * Created by Asus on 11/14/2019.
 */
@Configuration
public class ThriftServer {

    @Bean
    public TProtocolFactory tProtocolFactory() {
        return new TBinaryProtocol.Factory();
    }

    @Bean
    public ServletRegistrationBean thriftBookServlet(TProtocolFactory protocolFactory, CrawlerController controller) {
        TServlet tServlet = new TServlet(new TruyenFullCrawlerService.Processor<>(controller), protocolFactory);

        return new ServletRegistrationBean(tServlet, "/truyenfull");
    }
}
