/**
 * Company: Dist
 * Date：2016/11/2
 * Author: ChenYanping
 * Desc：
 */
package distchen.cloud.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConsumerController {

    @Autowired
    IConsumerController client;

    @RequestMapping(value = "/add/{x}/{y}",method = RequestMethod.GET)
    public Integer add(@PathVariable Integer x,@PathVariable Integer y){
        return this.client.add(x,y);
    }
}
