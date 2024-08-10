数据库密码加解密中间件，避免数据库密码明文显示导致密码泄露，安全、高效。

数据库密码配置问题
一、现状
目前应用与数据库建立连接使用的密码一般都是配置在应用的配置文件中，有些项目配置的是密码明文、有些是做了加密。
数据源配置 
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
    。。。。。
    <property name="password">
        <!--<value>${jdbc.druid.password}</value>-->
        <bean class="com.yujian.sso.common.support.EncryptDBPasswordFactory">
            <property name="password" value="${jdbc.druid.password}"/>
        </bean>
    </property>
   。。。。。
</bean>
jdbc.properties 
jdbc.druid.url=jdbc:mysql://db.yj.com:3306/auth?useUnicode=true&characterEncoding=UTF-8
jdbc.druid.user=devauth
jdbc.druid.password=2d4f769686700917fed7032cca7f8d21
......
虽然做了加密，但是意义不大，因为加解密的算法和key都在代码中，大家随时可以解密成明文。
可以如此轻易的获取我们线上数据库的密码明文，有很大的安全隐患，也不符合安全规范。
二、解决办法
经和dba、运维沟通，决定采用如下方式来缩小能够获取到数据库密码明文的人的范围。
加解密算法不变，加解密的key存储在/root/目录下的一个文件中，应用有权限读取该文件，开发没有权限读该文件。且兼容现有已加密密码。
我们称此方式为“使用privateKey方式”，称以前加解密方式为“默认方式”。
使用过程为：
1.运维在跑应用的机器上/root/下加入存放key的文件；
2.dba或运维使用我们提供的脚本生成“密码密文”，可以选择使用privateKey方式或者使用默认方式，然后将生成的密文配置在应用的配置文件中或者diamond上。
 
过程与原来基本一致，不同在于“密码密文”只能由dba或者运维生成，且使用privateKey方式生成的密文以“@”开头，使用默认方式生成的密文与原来方式生成的一样。
本地运行，因本地没有"/root/xxx"来存放加密秘钥，所以本地跑需要使用默认加密方式，即不带“@”符号的密文。
相应的用来解密的代码也要有些变化，应用系统需要做如下配置：
1.增加maven依赖：
数据库密码解密maven依赖 
<dependency>
<groupId>com.yujian.middleware</groupId>
<artifactId>middleware-commons</artifactId>
<version>1.0.1</version>
</dependency>
2.修改

<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
   。。。。。
   <property name="password">
                  <bean class="com.yujian.middleware.commons.support.WdDBPasswordFactory">    <!--  修改为新的类  -->
                            <property name="password" value="${jdbc.druid.password}"/>
                    </bean>
     </property>
。。。。。
</bean>
 
spring-boot应用，参考下面配置
DruidDataSource datasource = new DruidDataSource();
datasource.setUrl("jdbc:mysql://" + dbUrl + "?useUnicode=true&characterEncoding=utf-8");
datasource.setUsername(username);
YjDBPasswordFactory yjDBPasswordFactory = new YjDBPasswordFactory();
yjDBPasswordFactory.setPassword(password);
datasource.setPassword(yjDBPasswordFactory.getObject());
datasource.setDriverClassName("com.mysql.jdbc.Driver");
