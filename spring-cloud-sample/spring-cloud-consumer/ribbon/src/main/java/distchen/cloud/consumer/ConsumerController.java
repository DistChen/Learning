/**
 * Company: Dist
 * Date：2016/11/2
 * Author: ChenYanping
 * Desc：
 */
package distchen.cloud.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/add/{x}/{y}", method = RequestMethod.GET)
    public String add(@PathVariable Integer x,
                      @PathVariable Integer y) {
        return restTemplate.getForEntity("http://DIST-SERVICE/add?x="+x+"&y="+y, String.class).getBody();
    }
    /*@Autowired
    ConsumerService service;

    @RequestMapping(value = "/add/{x}/{y}")
    public String add(@PathVariable Integer x,@PathVariable Integer y){
        return this.service.add(x,y);
    }*/
}
