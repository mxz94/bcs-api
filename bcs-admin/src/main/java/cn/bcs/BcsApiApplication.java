package cn.bcs;

import cn.bcs.framework.config.MyBatisPlusSqlLogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BcsApiApplication {
    public static void main(String[] args) throws UnknownHostException {


        ConfigurableApplicationContext application = SpringApplication.run(BcsApiApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        //String path = env.getProperty("server.servlet.context-path");
        String swaggerEnable = env.getProperty("swagger.enable");
        if ("true".equals(swaggerEnable)) {
            MyBatisPlusSqlLogInterceptor.startPrintSQL();
        }
        try {
            Runtime.getRuntime().exec("cmd /c start http://localhost:" + port);
        } catch (IOException e) {
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port  + "/doc.html\n\t" +
                "Doc: \t\thttp://" + ip + ":" + port  + "/doc.html\n" +
                "-----------------------------------------------------------\n\t" +
                "Application is running!");
    }
}
