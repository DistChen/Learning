/**
 * Company: Dist
 * Date：2016/12/26
 * Author: ChenYanping
 * Desc：
 */
package dist.xdata.sample.wso2.custome.mediator;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.config.xml.AbstractMediatorSerializer;

public class DistLogMediatorSerializer extends AbstractMediatorSerializer {
    @Override
    protected OMElement serializeSpecificMediator(Mediator mediator) {
        System.out.println(mediator);
        return null;
    }

    @Override
    public String getMediatorClassName() {
        return null;
    }
}
