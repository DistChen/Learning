/**
 * Company: Dist
 * Date：2016/11/11
 * Author: ChenYanping
 * Desc：
 */
package distchen.cloud.consumer;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumerService {
    @Autowired
    RestTemplate template;

    @HystrixCommand(fallbackMethod = "errorCallback")
    public String add(int x,int y){
        return template.getForEntity("http://DIST-SERVICE/add?x="+x+"&y="+y,String.class).getBody();
    }

    public String errorCallback(Integer [] args){
        return "-9999";
    }
}
