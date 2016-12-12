/**
 * Company: Dist
 * Date：2016/11/2
 * Author: ChenYanping
 * Desc：
 */
package distchen.eureka.provider;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ProviderApp {

    public static void main(String [] args){
        SpringApplication.run(ProviderApp.class,args);
    }

}