### 添加私服
```xml
<repository>
    <id>DistNexus</id>
    <url>http://58.246.138.178:22280/nexus/content/groups/public/</url>
</repository>
```
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
LoginServiceClient loginClient = new LoginServiceClient(server_url);
sessionCookie = loginClient.login("admin", "admin");
``` 