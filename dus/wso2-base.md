### 添加私服
```xml
<repository>
    <id>DistNexus</id>
    <url>http://58.246.138.178:22280/nexus/content/groups/public/</url>
</repository>
```
关于私服的使用请参考[公司nexus使用-Wiki](https://github.com/DistX/Learning/wiki/%E5%85%AC%E5%8F%B8nexus%E4%BD%BF%E7%94%A8)
### 添加依赖
```xml
<dependency>
    <groupId>dist.xdata.product</groupId>
    <artifactId>wso2-base</artifactId>
    <version>version</version>
</dependency>
```
### 使用场景示例：登录
```java
String server_url = "https://localhost:9443/services/";
LoginServiceClient loginClient = new LoginServiceClient(server_url);
sessionCookie = loginClient.login("admin", "admin");
``` 
### 使用场景示例：添加代理服务
```java
ProxyServiceAdminClient proxyClient = new ProxyServiceAdminClient(server_url,sessionCookie) ;
String result = proxyClient.addProxyService("java_demo", targetEndpoint);
Assert.assertEquals("添加代理服务失败", "successful", result);
```