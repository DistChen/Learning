## distprocedure-1.1.5.RELEASE 

> 请先配置[公司私服](https://github.com/DistX/Learning/wiki/%E5%85%AC%E5%8F%B8nexus%E4%BD%BF%E7%94%A8)。

```xml
<dependency>
    <groupId>dist.xdata.product</groupId>
    <artifactId>distprocedure</artifactId>
    <version>1.1.5.RELEASE</version>
</dependency>
```

### 更新内容

- 数据源
- ProcedureModel 获取方式
- Clob、Blob支持
- 结果映射

### 具体说明

#### 1、数据源

之前独立使用数据源是为了脱离spring之后也能用，但是现在看来没这个必要。所以在新版本中的配置文件移除了数据源的配置内容，distprocedure会直接使用spring配置的数据源，不再需要手动配置。**删除features.xml中的如下配置**即可：

```xml
<datasource src="dataSource.properties">
    <driver>${driverClassName}</driver>
    <url>${url}</url>
    <username>${username}</username>
    <password>${password}</password>
</datasource> 
```

#### 2、ProcedureModel

之前版本的设计中，考虑到每个控制器都会有一个单独的储存过程配置文件，所以会在控制器类中 加入一个如下属性，用来存储对应的存储过程模型ProcedureModel：
```JAVA
private Map<String,ProcedureModel> features;

public Map<String, ProcedureModel> getFeatures() {
    return features;
}

public void setFeatures(Map<String, ProcedureModel> features) {
    this.features = features;
}
```

而且要在spring配置文件中给控制器指定如下属性：
```XML
<property name="features" value="features.xml" />
```

为了将features.xml文件的内容转为Map，在spring中还配置了属性编辑器，如下所示：
```
<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer"
          p:customEditors-ref="customPropertyEditor" />

<util:map id="customPropertyEditor">
    <entry key="java.util.Map" value="dist.common.procedure.define.ProcedureRepositoryEditor" />
</util:map>
```

现在，**上面说的三部分内容可以全部删除**了！！！！！

features.xml的加载全部交由监听类来完成，在容器启动时即可完成加载，所以只需要在web.xml中如下配置即可：
```xml
<context-param>
    <param-name>ProcedureFiles</param-name>
    <param-value>features.xml</param-value>
</context-param>
<listener>
    <listener-class>dist.common.procedure.define.listener.ProcedureListener</listener-class>
</listener>
```

容器启动时能自动加载储存过程配置文件。而我们使用Junit测试时，没有启动容器，就无法自动加载配置文件。为了方便测试使用，在`ProcedureFile`类中新增了两个接口用于手动加载存储过程配置文件，如下：

```java
/**
 * 加载多个存储过程配置文件
 * @param fileNames
 */
public static void loadProcedureModels(String [] fileNames);

/**
 * 加载一个储存过程配置文件
 * @param fileName
 */
public static void loadProcedureModels(String fileName);
```

加载后的ProcedureModel存于`ProcedureRepository`类中，通过此类的`getProcedure(String id)`方法即可取出`ProcedureModel`。


#### 3、Clob、Blob 支持
之前的版本无法对Clob、Blob两种数据类型传入传出，也没法将两种类型的字段映射到结果类。此版本中：传入Clob类型的参数时，请传String类型数据给distprocedure,传入Blob类型的参数时，请传字节数组(byte[])数据给distprocedure。distprocedure会自动转换这样的参数为Clob、Blob 类型。如：
```java
ProcedureModel model = ProcedureRepository.getProcedure("testPro");
String clobStr = "很长的字符串";
byte [] blobBytes = "blob测试数据".getBytes();
ProcedureCaller.call(model,clobstr,blobBytes)
```
distprocedure 会自动将上面的`clobstr`和`blobBytes`封装成`Clob`、`CBlob`，然后传递给相应的储存过程。

#### 4、结果映射
之前将游标里的数据映射成vo类时，需要保持属性和字段的一一对应，任何一方多一个或者少一个都会导致自动映射失败。新版本中已没有此限制，做到了只要能映射就映射，不能映射就忽略，任何一方多字段或者属性都不会导致映射失败。

#### 示例

##### feature.xml 配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<procedures xmlns="http://www.dist.com.cn"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://www.dist.com.cn distprocedure.xsd">

   <procedure id="testPro">
        <proName>prc_test</proName>
        <parameters>
            <parameter name="p_str" type="in" dataType="varchar"/>
            <parameter name="p_num" type="in" dataType="number" />
            <parameter name="p_strDate" type="in" dataType="date" format="yyyy-MM-dd" />
            <parameter name="p_date" type="in" dataType="date" />
            <parameter name="p_inclob" type="in" dataType="clob" />
            <parameter name="p_inblob" type="in" dataType="blob" />
            <parameter name="p_info" type="out" dataType="varchar" />
            <parameter name="p_outclob" type="out" dataType="clob" />
            <parameter name="p_outblob" type="out" dataType="blob" />
            <parameter name="p_cursor" type="out" dataType="cursor" vo="dist.dgp.model.Person" />
        </parameters>
    </procedure>
</procedures>
```

##### 测试的储存过程
```sql
create or replace procedure prc_test(
       p_str in varchar2,
       p_num in number,
       p_strDate in date,
       p_date in date,
       p_inclob in clob,
       p_inblob in blob,
       p_info out varchar2,
       p_outclob out clob,
       p_outblob out blob,
       p_cursor out sys_refcursor
)
is
begin
  p_info :='字符串为：'||p_str||',数字为：'||p_num||',日期为：'||to_char(p_strDate,'yyyy-mm-dd')||','||to_char(p_date,'yyyy-mm-dd')||',Clob长度为：'||length(p_inclob);
  select p_inblob into p_outblob from dual;
  select 'clob->hello world' into p_outclob from dual;
  open p_cursor for select * from(
        -- other字段在person中无对应属性
       select 1 id,sysdate age,'CUG' school, '张三' name,'11' other from dual
       union
       select 2 id,sysdate age,'HUST' school, '李四' name,'22' other  from dual
  );
end prc_test;

```

##### dist.dgp.model.Person
```java
public class Person {

    private int id;
    private Date age;
    private String school;
    private String name;
    private String desc; // 储存过程不会返回此字段

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getAge() {
        return age;
    }
    public void setAge(Date age) {
        this.age = age;
    }
    public String getSchool() {
        return school;
    }
    public void setSchool(String school) {
        this.school = school;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
```

##### 测试代码
```
public class DistProcedureTest {

    @Before
    public void init(){
        // 手动加载配置文件
    	ProcedureFile.loadProcedureModels("features.xml");
    }
    
    @Test
    public void testDistProcedure() throws Exception{
    	ApplicationContextUtil.loadContext();
    	byte [] blobBytes = "传入的blob参数：hello".getBytes();
    	StringBuilder clobStr=new StringBuilder();
        for (int i=0;i<300000;i++){
            clobStr.append("1");
        }
        ProcedureModel model = ProcedureRepository.getProcedure("testPro");
        Map<String,Object> obj=(Map<String,Object>)ProcedureCaller.call(model
                ,"dataType配置为varchar"
                ,100
                ,"2010-08-22",
                new Date()
                ,clobStr.toString() // Clob
                ,blobBytes); //Blob
    	 
        System.out.println(obj.get("p_info")+"\n");
    
        CLOB demo = (CLOB)obj.get("p_outclob"); // 传出的 Clob
        System.out.println(demo.getSubString(1, (int) demo.length()) +"\n");
    
        BLOB blob = (BLOB)obj.get("p_outblob"); // 传出的 Blob
        System.out.println(new String(blob.getBytes(1,(int)blob.length()))+"\n");
    
        Object result=obj.get("p_cursor");
        for (Person person:(List<Person>)result){
    	    System.out.println(person.getName()+","
                            +person.getSchool()+","
                            +person.getAge()+","
                            +person.getId());
        }
    }
}
```