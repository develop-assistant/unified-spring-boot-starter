<p align="center">
  <a href="https://idea360.cn">
   <img alt="idea360-Logo" src="https://raw.githubusercontent.com/qidian360/oss/master/images/idea360.cn.png">
  </a>
</p>

<p align="center">
  为简化开发工作、提高生产率而生
</p>

<p align="center">
  <a href="https://search.maven.org/search?q=g:cn.idea360%20a:unified-spring-boot-starter">
    <img alt="maven" src="https://img.shields.io/maven-central/v/cn.idea360/unified-spring-boot-starter.svg?style=flat-square">
  </a>

  <a href="https://github.com/qidian360/unified-spring-boot-starter">
    <img alt="maven" src="https://img.shields.io/github/forks/qidian360/unified-spring-boot-starter">
  </a>

  <a href="https://github.com/qidian360/unified-spring-boot-starter">
    <img alt="maven" src="https://img.shields.io/github/stars/qidian360/unified-spring-boot-starter">
  </a>

  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>

  <a href="https://idea360.cn">
    <img alt="code style" src="https://img.shields.io/badge/%E5%BD%93%E6%88%91%E9%81%87%E4%B8%8A%E4%BD%A0-idea360.cn-brightgreen">
  </a>
</p>

## 它能够做什么

1. 统一返回格式, 正确结果code为0
2. 统一异常处理, 自定义 `RuntimeException` 需要继承自 `BizException`, 否则视为 `Throwable` 处理。
3. 统一参数校验, 在实体中添加校验注解即可。无需在方法参数中加@Valid和@Validated注解


## 快速使用

#### 1. maven添加依赖

```xml
<dependency>
  <groupId>cn.idea360</groupId>
  <artifactId>unified-spring-boot-starter</artifactId>
  <version>x.x.x</version>
</dependency>
```

#### 2. 统一返回结果示例

VO定义

```java
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {

    @NotBlank(message = "帐号不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
```

测试接口

```java
@GetMapping("/user-info")
public User userInfo() {
    return User.builder().username("当我遇上你").password("123456").build();
}
```

返回结果
```json
{
    "msg": "OK",
    "code": 0,
    "data": {
        "username": "当我遇上你",
        "password": "123456"
    }
}
```
#### 3. 全局异常示例

测试接口

```java
@GetMapping("/user-ex")
public User exception() {
    return Collections.singletonList(User.builder().username("当我遇上你").password("123456").build()).get(1);
}
```

日志输出
```text
2022-08-07 15:46:50.609 ERROR 3954 --- [nio-8080-exec-1] c.i.u.e.UnifiedExceptionHandler          : 兜底异常: java.lang.IndexOutOfBoundsException: Index: 1, Size: 1
	at java.base/java.util.Collections$SingletonList.get(Collections.java:4849)
	at cn.idea360.log.web.LogController.exception(LogController.java:42)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:105)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:878)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:792)
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040)
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:626)
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:733)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542)
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143)
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:374)
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:888)
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1597)
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.base/java.lang.Thread.run(Thread.java:834)
```

接口返回

```json
{
    "msg": "OK",
    "code": 0,
    "data": {
        "msg": "Index: 1, Size: 1",
        "code": -1,
        "data": null
    }
}
```

#### 4. 参数校验示例

测试接口

```java
@PostMapping("/add-user")
public boolean addUser(@RequestBody User user) {
    return true;
}
```

请求体

```json
{
  "username": "当我遇上你"
}
```

返回结果

```json
{
    "msg": "OK",
    "code": 0,
    "data": {
        "msg": "password: 密码不能为空",
        "code": -1,
        "data": null
    }
}
```


## 联系我

* [Email](idea360@foxmail.com): idea360@foxmail.com
* [Blog](https://idea360.cn): idea360.cn

---
欢迎大家关注微信公众号. 您的支持是我写作的最大动力

![当我遇上你](https://raw.githubusercontent.com/qidian360/oss/master/images/wechat-qr-code-300px.png "当我遇上你")
