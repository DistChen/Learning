/**
 * Company: Dist
 * Date：2016/11/2
 * Author: ChenYanping
 * Desc：
 */
package distchen.cloud.consumer;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "dist-service",fallback = ErrorCallback.class)
public interface IConsumerController {

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    Integer add(@RequestParam(value = "x") Integer x,@RequestParam(value = "y") Integer y);

}
