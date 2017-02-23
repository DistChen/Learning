package dist.dgp.test;

import dist.common.procedure.define.ProcedureFile;
import dist.dgp.model.Person;
import dist.dgp.controller.QueryStatCtl;
import dist.dgp.util.ApplicationContextUtil;
import oracle.sql.BLOB;
import oracle.sql.CLOB;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dist on 14-12-23.
 */
public class AppTest {

    private static Logger log=Logger.getLogger(AppTest.class);

    @Before
    public void init(){
        // 手动加载配置文件
        ProcedureFile.loadProcedureModels("distfeatures.xml");
    }

    @Test
    public void testGetCount() throws Exception {

        QueryStatCtl ctl=(QueryStatCtl) ApplicationContextUtil.getBean("QueryStatCtl");

        StringBuilder sb=new StringBuilder();
        sb.append("clob:");
        for (int i=0;i<300;i++){
            sb.append("1");
        }

        Map<String,Object> obj=(Map<String,Object>)ctl.testPro("testPro",
                                                                "dataType配置为varchar",
                                                                100,
                                                                "2010-08-22",
                                                               new Date(),sb.toString(),"blob:hello".getBytes());

        System.out.println(obj.get("p_info")+"\n");

        CLOB demo = (CLOB)obj.get("p_outclob");
        System.out.println(demo.getSubString(1, (int) demo.length()) +"\n");

        BLOB blob = (BLOB)obj.get("p_outblob");
        System.out.println(new String(blob.getBytes(1,(int)blob.length()),"UTF-8")+"\n");

        Object result=obj.get("p_cursor");
        if(result instanceof String){
            log.info(obj.get("p_cursor").toString());
        }else{
            for (Person person:(List<Person>)result){
                log.info(person.getName()+"   "+person.getSchool()+"    "+person.getAge().toLocaleString()+"   "+person.getId());
            }
        }
    }

}
