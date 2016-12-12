/**
 * Company: Dist
 * Date：2016/11/11
 * Author: ChenYanping
 * Desc：
 */
package distchen.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringCloudApplication
public class ApiGatewayApp {
    public static void main(String [] args){
        SpringApplication.run(ApiGatewayApp.class,args);
    }

    @Bean
    public ServiceFilter serviceFilter(){
        return new ServiceFilter();
    }
}
