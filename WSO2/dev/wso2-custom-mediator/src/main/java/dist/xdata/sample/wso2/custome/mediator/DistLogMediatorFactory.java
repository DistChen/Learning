/**
 * Company: Dist
 * Date：2016/12/26
 * Author: ChenYanping
 * Desc：
 */
package dist.xdata.sample.wso2.custome.mediator;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseException;
import org.apache.synapse.config.xml.AbstractMediatorFactory;
import org.apache.synapse.config.xml.XMLConfigConstants;

import javax.xml.namespace.QName;
import java.util.Properties;

public class DistLogMediatorFactory extends AbstractMediatorFactory {
    static final QName company = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "company");

    @Override
    protected Mediator createSpecificMediator(OMElement omElement, Properties properties) {
        DistLogMediator mediator = new DistLogMediator();
        processTraceState(mediator,omElement);
        OMElement _default = omElement.getFirstChildWithName(company);
        if (_default!=null){
            mediator.setCompany(_default.getText());
        }else{
            throw new SynapseException("default company element missing");
        }
        return mediator;
    }

    @Override
    public QName getTagQName() {
        return null;
    }
}
