关于 `字符`、`字符集`、`字符编码`、`ASCII`、`GB2312`、`GBK`、`Unicode`、`UTF-8`等名词，这是理解乱码问题的基本，在继续往下看之前，请先理解这些概念，大家可参考此篇文章，个人觉得值得一看。

> [http://www.cnblogs.com/skynet/archive/2011/05/03/2035105.html](http://www.cnblogs.com/skynet/archive/2011/05/03/2035105.html)

在这篇文章中，将这些名称解释的很清楚。但并没有对java编译和前后端编码、解码的场景进行分析，我这里就对这些场景进行补充。

### javac编译

javac 编译源文件时，如果不指定`encoding`参数，则使用操作系统的默认编码来读取源文件并编译，也就是GBK。(中文操作系统默认编码为GBK，命令行chcp可查看)：

#### GBK 编码的源文件
    
由于javac默认根据操作系统编码(GBK)读取源文件，因此在编译GBK编码的源文件时，不需要加上任何的参数，直接编译即可，如：

```
javac Demo.java
```

#### 非GBK编码的源文件

编译非GBK编码的源文件时，如果在编译时加上`encoding`参数指定对应编码，则不会有任何问题，如：
```
javac -encoding UTF-8 Demo.java
```

执行的结果也不会乱码。如果仍然按照默认编码来编译源文件，即：
```
javac Demo.java
```

会有两种结果：
    
- 编译成功

    示例：
    ```java
    public class Demo{
    	public static void main(String [] args){
    		System.out.println("数字");
    	}
    }
    ```
    结果：
    ```
    D:\>javac Demo.java

    D:\>java Demo
    鏁板瓧
    ```
    可以看到，即使编译成功，执行结果一样乱码。
    
- 编译不成功

    示例：
    ```java
    public class Demo{
    	public static void main(String [] args){
    		System.out.println("数值");
    	}
    }
    ```
    结果：
    ```
    D:\>javac Demo.java
    Demo.java:3: 错误: 编码GBK的不可映射字符
                    System.out.println("鏁板??");
                                          ^
    Demo.java:3: 错误: 编码GBK的不可映射字符
                    System.out.println("鏁板??");
                                           ^
    2 个错误
    ```
    可以看到，直接编译失败。

发现什么？差别只是字符`字`与`值`区别！！！这两个UTF-8编码的源文件，javac都是用GBK编译。那么在读取文件时，会分别将`字`与`值`读出来，读到的是什么样子呢，如下：
```java
//数字
public class Demo{
	public static void main(String [] args){
		System.out.println("鏁板瓧");
	}
}

//数值
public class Demo{
	public static void main(String [] args){
		System.out.println("鏁板€?);
	}
}
```
如上所示，细心的话可以发现，当字符是`数字`的时候，双引号仍然存在，整个源文件格式是正确的，所以可以被javac编译；而当字符是`数值`的时候，双引号都少了一个，整个源文件的格式都不正确了，所以才会导致编译失败。
    
因此，用GBK来编译UTF-8等非GBK编码的源文件时，编译成功与否是看运气的。如果乱码后，仍然符合java规范，则可以编译通过；否则，编译不会通过。
        
#### GBK 、非GBK编码的多个源文件

当编译的一个源文件中使用了另外一种编码格式的类时，也就是多个不同编码的源文件会被同时编译。同样默认使用GBK来编译这些源文件，通过encoding参数也可指定编码格式，编译是否成功，结果是否乱码，请参考上面两点。因此，可以推测：当同时编译不同编码的多个源文件时，即使编译成功，也必然会乱码(编译时只能指定一种编码，而源文件有多种编码)。值得注意的是，我们平常使用各种IDE(Eclipse等)来编译项目时，即使源文件编码五花八门，也不会乱码，为什么？是IDE智能处理了这些编译过程，根据文件的编码选择对应的编码来编译。

### web
web项目是乱码的集中地：请求-应答模式。由于客户端和服务器的编码环境不一致，所以导致很容易出现乱码问题。通过浏览器请求时，会有以下的几个场景涉及到中文编码的问题：

#### URL 中包含中文
url 中包含中文的有两部分：Path和Query String，如下所示：
```
http://127.0.0.1:8080/demo/我?name=我
```
上面这个url，在`Chrome`、`Firefox`浏览器中会自动转成如下的形式：
```
http://127.0.0.1:8080/demo/%E6%88%91?name=%E6%88%91
```
在`IE`浏览器中会自动转成如下的形式：
```
http://127.0.0.1:8080/demo/%E6%88%91?name=%CE%D2
```
可以看到不同的浏览器处理的方式不同：`Chrome`、`Firefox`、`IE`将Path中的`我`转成了`%E6%88%91`，`我`的UTF-8编码正是`E68891`。而对于Query String部分，`Chrome`、`Firefox`同样是转成了`%E6%88%91`，`IE`却转成了`%CE%D2`，这是`我`的GBK编码。

由此可得知：URL中Path部分的中文，浏览器均会转成UTF-8，而对于Query String 部分，`Chrome`、`Firefox`浏览器会转成UTF-8编码，`IE`会转成GBK编码(操作系统默认编码)。

那服务器如何解码？以Tomcat为例，如果不加任何的控制，Tomcat会默认转成`ISO-8859-1`，而浏览器传输过来的是Path部分是UTF-8，而Query String部分就不一定了，因浏览器而定。如果不加处理，毫无疑问会乱码，如下所示：
```java
request.getParameter("name");
```
如果不修改任何配置，代码层面上可通过如下的形式还原：
```java
new String(request.getParameter("name").getBytes("ISO-8859-1"),"UTF-8")
// new String(request.getParameter("name").getBytes("ISO-8859-1"),"GBK")
```
也可以通过修改Tomcat的配置来避免修改代码，打开server.xml文件，设置`URIEncoding`，如下：
```xml
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" URIEncoding="UTF-8" />
           
or

<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" URIEncoding="GBK" />
```
属性`URIEncoding`表明用何种方式来解码URL。通过上面的试验可得知，对于直接在浏览器中输入含中文的URL，不同的浏览器处理的方式有差异(还有很多其它的浏览器)，后台无法通过一个解码规则来应对。因此，在实际的开发中应该屏蔽掉直接输入含中文的URL来访问的场景。

#### 表单提交
在`Chrome`、`Firefox`、`IE`三种浏览器中，表单中的中文编码按照如下的规则来进行：
1. 如果form设置了accept-charset属性，则以此属性值为准；
2. 如果form未设置accept-charset属性，则以页面编码为准;
3. 如果form未设置，页面编码也未设置，则以操作系统编码为准。

- 示例1：页面编码为UTF-8，form设置accept-charset=GBK
    ```xml
  	<meta charset="UTF-8">
  	...
    <form accept-charset="GBK" action="http://127.0.0.1:8080" method="GET" >
		<input type="text" name="name" >
    </form>
    ```
    生成的URL为：
    ```
    http://127.0.0.1:8080/?name=%CE%D2
    ```
    > `CED2`为字符`我`的`GBK`编码。
    
- 示例2：页面编码为UTF-8，form未设置accept-charset
    ```xml
  	<meta charset="UTF-8">
  	...
    <form action="http://127.0.0.1:8080" method="GET" >
		<input type="text" name="name" >
    </form>
    ```
    生成的URL为：
    ```
    http://127.0.0.1:8080/?name=%E6%88%91
    ```
    > `E68891`为字符`我`的`UTF-8`编码。
    
- 示例3：页面无编码，form未设置
    ```xml
  	...
    <form action="http://127.0.0.1:8080" method="GET" >
		<input type="text" name="name" >
    </form>
    ```
    生成的URL为：
    ```
    http://127.0.0.1:8080/?name=%CE%D2
    ```
    > `CED2`为字符`我`的`GBK`编码。
    
不同的表单提交方式，不管是GET还是POST还是其它，前端编码的规则是一样的，也没有浏览器的差异(如果表单中的中文，不同的浏览器提交时编码还有差异，可以想象真是要搞死人了)，不过后端解码的规则不一样。

##### GET
GET 方式提交的数据会追加到URL后面，服务器端解析与上面一样，通过URIEncoding属性可设置。

##### POST
POST提交的数据并不是追加到URL之后，而是通过body发送，因此服务器无法通过设置URIEncoding来让Tomcat自动解码，需要手动设置(必须在第一次取值之前设置)，如下所示：
```java
request.setCharacterEncoding("UTF-8");
System.out.println(request.getParameter("name"));
```
当然，你大可以编写一个过滤器来干这样的事情，如下所示：
```java
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class CharacterEncodingFilter implements Filter {

    public void destroy() {}
 
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }
    public void init(FilterConfig filterConfig) throws ServletException {}
}
```
web.xml
```xml
<filter>
    <filter-name>characterEncoding</filter-name>
    <filter-class>CharacterEncodingFilter</filter-class>
</filter>
    <filter-mapping>
    <filter-name>characterEncoding</filter-name>
<url-pattern>/*</url-pattern>
</filter-mapping>
```

只要是通过body发送的数据，服务器的解码都要按照上面的方式进行。(设置setCharacterEncoding)

#### 网页链接含中文
同样是URL中含有中文，不过URL并不是直接在浏览器地址栏中输入，而是通过网页上的链接点击来访问，如下所示：
```html
<a href="http://127.0.0.1:8080/demo/我?name=我">test</a>
```
经过测试，在`Chrome`、`Firefox`、`IE`三种浏览器中，均按照如下规则来对中文编码：
1. 优先以页面设置的编码为准
2. 如果页面没有设置编码，则以操作系统编码为准(GBK)。

#### Ajax 请求
通过Ajax发送的数据，浏览器均以UTF-8的形式编码发送，不管页面编码是什么，后端解码按照表单POST提交的方式进行即可。

### 总结
编码的问题因使用的场景、浏览器均会产生差异，而后端解码要根据相应的前端编码来进行，所以前后端编码统一很重要。而且可以看到，只要是浏览器强势使用的编码(URL中的Path,Ajax发送body)，都是UTF-8(IE 对 Query String编码除外)，因此，前后端最好都是用UTF-8才是最合适的。
