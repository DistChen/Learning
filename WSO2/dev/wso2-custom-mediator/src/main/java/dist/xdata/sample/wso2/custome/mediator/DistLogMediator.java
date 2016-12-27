/**
 * Company: Dist
 * Date：2016/12/26
 * Author: ChenYanping
 * Desc：
 */
package dist.xdata.sample.wso2.custome.mediator;

import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.mediators.AbstractMediator;

public class DistLogMediator extends AbstractMediator implements ManagedLifecycle {

    private String company;

    @Override
    public void init(SynapseEnvironment synapseEnvironment) {
        System.out.print("save 的时候调用了");
    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean mediate(MessageContext messageContext) {
        if (company.equals("dist")){
            return true;
        }else{
            return false;
        }
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
