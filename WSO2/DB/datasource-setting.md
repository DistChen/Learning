# wso2数据源指定
此处以oracle数据库为例，将wso2数据源指定为外部oracle数据库
## 数据库创建
1. 创建oracle用户
2. 为用户授予connect、resource权限
3. 找到wso2_home/dbscripts下的oracle.sql脚本在pl/sql中执行，完成数据库建立
![image](https://github.com/DistX/Learning/tree/master/WSO2/DB/resources/建库脚本路径.jpg)

## 添加oracle连接用jar包
1. jar包添加路径：wso2_home/repository/components/lib
![image](https://github.com/DistX/Learning/tree/master/WSO2/DB/resources/jar包存放路径.jpg)

## 修改配置文件
1. 配置文件路径:wso2_home\repository\conf\datasources\master-datasources.xml
![image](https://github.com/DistX/Learning/tree/master/WSO2/DB/resources/数据库配置文件路径.jpg)

2. 修改前文件内容
！[image](https://github.com/DistX/Learning/tree/master/WSO2/DB/resources/修改前文件.jpg)

3. 修改后文件内容
![image](https://github.com/DistX/Learning/tree/master/WSO2/DB/resources/修改后文件.jpg) 

通过上述配置，我们就能将wso2使用的数据库由内置的h2数据库指定为我们自己创建的数据库了。

