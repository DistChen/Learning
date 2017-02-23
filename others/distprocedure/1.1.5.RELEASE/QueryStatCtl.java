package dist.dgp.controller;

import dist.common.procedure.define.ProcedureCaller;
import dist.common.procedure.define.ProcedureRepository;

import java.util.Date;
import java.util.Map;

/**
 * Created by dist on 14-12-23.
 */
public class QueryStatCtl {


    public Object testPro(String featureName,
                          String str,
                          Integer num,
                          String strDate,
                          Date date,
                          String clobstr,
                          byte[] blob){
        return (Map)ProcedureCaller.call(ProcedureRepository.getProcedure(featureName),str,num,strDate,date,clobstr,blob);
    }

}
