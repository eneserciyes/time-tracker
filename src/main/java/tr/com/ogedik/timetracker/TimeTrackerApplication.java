package tr.com.ogedik.timetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("tr.com.ogedik.scrumier.proxy.clients")
public class TimeTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeTrackerApplication.class, args);
    }
}
