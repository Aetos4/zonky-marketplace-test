package cz.aetos.zonky.zonkyapiconnector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "cz.aetos.zonky.zonkyapiconnector")
public class App {

    private static final Log logger = LogFactory.getLog(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
