package app.conf;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

/**
 * Created by tmolloy on 17/10/2015.
 */

/**
 * Security Config for Tomcat
 */
public class SecureTomcatConfiguration {

    /**
     * Adds a new ssl connector
     * @return TomcatEmbeddedServletContainerFactory
     * @throws FileNotFoundException
     */
    @Bean
    public EmbeddedServletContainerFactory servletContainer () throws FileNotFoundException {
        TomcatEmbeddedServletContainerFactory f = new TomcatEmbeddedServletContainerFactory();
        f.addAdditionalTomcatConnectors(createSslConnector());
        return f;
    }


    /**
     * Creates a secure ssl connector setting port, ssl key, and keystore password
     * @return
     * @throws FileNotFoundException
     */
    private Connector createSslConnector () throws FileNotFoundException {
        Connector connector = new Connector(Http11NioProtocol.class.getName());
        Http11NioProtocol protocol = (Http11NioProtocol)connector.getProtocolHandler();

        connector.setPort(8443);
        connector.setSecure(true);
        connector.setScheme("https");

        protocol.setSSLEnabled(true);
        protocol.setKeyAlias("springboot");
        protocol.setKeystorePass("password");
        protocol.setKeystoreFile(ResourceUtils
                                    .getFile("src/main/resource/tomcat.keystore")
                                    .getAbsolutePath());
        protocol.setSslProtocol("TLS");
        return connector;
    }
}
