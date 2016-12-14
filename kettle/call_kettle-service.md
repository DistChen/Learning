在Kettle中创建好的Transformation、Jobs除了可以利用Spoon工具来执行外，还可以有其它的执行方式，如下：

- 命令行调用
- java 调用
- 服务调用

这里就简单介绍下如何以服务的形式来访问、执行配置好的Transformation、Jobs。这里要利用Carte工具，我们使用kettle来搭建集群的时候肯定要用到此工具的。

### 配置文件
确保在`%Kettle_Home%/pwd`目录下存在这两个文件：

- carte-config-master-8080.xml
- kettle.pwd

![](http://www.chenyp.com/img/fadsfioi.png)

截图中的另外几个配置文件可不要(除非你要搭建集群)，`carte-config-master-8080.xml`的名称中的`8080`代表启动时的端口，可根据实际情况修改，对应的内容也要做出相应的修改：

```xml
<slave_config>
  <!-- 
     Document description...
     
     - masters: You can list the slave servers to which this slave has to report back to.
                If this is a master, we will contact the other masters to get a list of all the slaves in the cluster.

     - report_to_masters : send a message to the defined masters to let them know we exist (Y/N)

     - slaveserver : specify the slave server details of this carte instance.
                     IMPORTANT : the username and password specified here are used by the master instances to connect to this slave.

  --> 

  <slaveserver>
    <name>master1</name>
    <hostname>localhost</hostname>
    <port>8080</port>
    <master>Y</master>
  </slaveserver>
</slave_config>

```
`kettle.pwd`文件中存储的是服务器的用户名和密码(已加密)，内容也可以根据实际使用情况修改。

### 用户名/密码设置
服务器启动后，默认用户名为`cluster`，密码为`cluster`。如果你想修改用户名或密码，需要修改上述的`kettle.pwd`配置文件，默认的配置如下：
```
# Please note that the default password (cluster) is obfuscated using the Encr script provided in this release
# Passwords can also be entered in plain text as before
# 
cluster: OBF:1v8w1uh21z7k1ym71z7i1ugo1v9q 
```
`cluster`是用户名，`OBF:1v8w1uh21z7k1ym71z7i1ugo1v9q`是`cluster`加密后的值。使用    `Encr`工具可以用来生成密码，命令如下：
```
cd %Kettle_Home%
Encr -carte your_password
```
![](http://www.chenyp.com/img/YP5Z97PV1IITC.png)

### 启动服务
执行如下命令即可启动服务器：
```
Carte hostname port
```
命令中的`port`需要与[配置文件](#配置文件)设置的port对应，正常启动后如下所示：

![](http://www.chenyp.com/img/Q435YZ9.png)

访问[http://localhost:8080](http://localhost:8080/kettle/status/)，输入用户名/密码即可验证是否正常。

### 调用服务
启动成功后，就可以调用服务来执行一些操作了，这里以执行一个配置好的Transformation为例：
> [http://localhost:8080/kettle/executeTrans/?rep=kettle54_local&user=admin&pass=admin&trans=services/demo](http://cluster:cluster@127.0.0.1:8080/kettle/executeTrans/?rep=kettle54_local&user=admin&pass=admin&trans=services/demo)

这个转换放在了仓库`kettle54_local`中，仓库的用户名是`admin`，密码是`admin`，所处目录是`/services`，转换的名称是`demo`，如下所示：

![](http://www.chenyp.com/img/UZSEDXSE26NETBREH6.png)

调用后，返回结果如下:

![](http://www.chenyp.com/img/VQ0KV4QGQQUCFJ565K.png)

需要注意的是，如果在服务中想要得到响应结果，`输出`需要勾选此选项，`xml输出`等同理：

![](http://www.chenyp.com/img/XG5YV005_OVZNF21NI2.png)

更多API可参考：[http://help.pentaho.com/Documentation/5.4/0R0/070/020/020/020]( http://help.pentaho.com/Documentation/5.4/0R0/070/020/020/020)
