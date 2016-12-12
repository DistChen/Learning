/**
 * Company: Dist
 * Date：2016/11/11
 * Author: ChenYanping
 * Desc：
 */
package distchen.cloud.consumer;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class ErrorCallback implements IConsumerController{
    @Override
    public Integer add(@RequestParam(value = "x") Integer x, @RequestParam(value = "y") Integer y) {
        return -9999;
    }
}
