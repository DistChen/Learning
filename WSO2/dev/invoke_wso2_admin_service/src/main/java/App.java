import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.proxyadmin.stub.ProxyServiceAdminProxyAdminException;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaData;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaDataWrapper;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Company: Dist
 * Date：2016/12/16
 * Author: ChenYanping
 * Desc：
 */

public class App {
    static String server_url = "https://localhost:9443";
    static String trustStore = "wso2carbon.jks";
    static String sessionCookie;
    static LoginAdminServiceClient loginClient;
    static ServiceAdminClient serviceClient;
    static ProxyServiceAdminClient proxyClient;

    public static void main(String args[]) throws RemoteException, ProxyServiceAdminProxyAdminException, LoginAuthenticationExceptionException {

        System.setProperty("javax.net.ssl.trustStore",App.class.getResource("/").getPath()+"/"+trustStore);

        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

        sessionCookie = login("admin","admin");

        if (sessionCookie != null){

            serviceClient = new ServiceAdminClient(server_url, sessionCookie);
            proxyClient = new ProxyServiceAdminClient(server_url,sessionCookie);

            listProxyService().forEach(data -> System.out.println(data.getName()));

            System.out.printf("添加服务：%s \n", addProxy("java_demo", "http://192.168.1.188:6080/arcgis/rest/services/DemoBJ/MapServer"));

            System.out.printf("删除服务：%s \n", deleteProxyService("java_demo"));

            System.out.printf("添加服务：%s \n", addProxy("java_demo2", "http://192.168.1.188:6080/arcgis/rest/services/DemoBJ/MapServer"));

            logout();
        }
    }

    private static List<ServiceMetaData> listProxyService() throws RemoteException {
        ServiceMetaDataWrapper serviceList = serviceClient.listServices();
        return Stream.of(serviceList.getServices())
                .filter(data -> data.getServiceType().equals("proxy"))
                .collect(Collectors.toList());
    }

    private static String addProxy(String proxyName,String targetEndpoint){
        try {
            return proxyClient.addProxyService(proxyName,targetEndpoint);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static String deleteProxyService(String proxyServiceName){
        try {
            return proxyClient.deleteProxy(proxyServiceName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String login(String userName,String password){
        try {
            loginClient = new LoginAdminServiceClient(server_url);
            return loginClient.authenticate(userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void logout(){
        try {
            loginClient.logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
