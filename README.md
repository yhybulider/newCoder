# newCoder
和小伙伴的Java+spring+redis+kafka 

## 前言

这是一个功能齐全的博客系统框架的一个spring项目；

1. 前期目标借鉴大致框架去实现一些东西
2. 后期实现了大概框架，就加进去自己修改的东西，体现自己的特点。

### 要实现的功能和特点

有讨论发帖，看关注人数、点赞数、曾经发过的帖子。修改密码。头像-涉及到云服务。系统通知，发私信，消息队列、多线程知识、全文搜索、同步数据、管理员才能使用的一些管理-统计用户数量、用户行为、还有登录注册功能。



## 目录文件夹介绍
static-静态资源



## 技术和工具介绍

技术架构-spring boot

spring、spring mvc mybatis--ssm组合

Redis、 kafka、 Elasticsearch

Spring security、spring actuator

数据库：mysql、redis

版本控制工具：git

应用服务器：apache tomacat

集成开发工具：idea

数据库工具：Navicat



### redis整合使用
- redis整合spring使用
#### 公共的
工具类、配置文件都处理好
配置类--redisConfg--
	返回的是rediisTemplate类型的--访问数据库--创建连接工厂--redisConnectionFactory
	在配置类中还有设置序列化方式，在传播的过程中以什么形式去传播-setKeySerializer
	有序列化成json和string格式的
	后续哪里需要redis，就将redistemplate注入

redisKeyutil.java（下面的功能都有涉及到）
redisTemplate的使用
opsForValue方法
##### 功能模块

1.查询点赞功能
重构点赞-用户为key，记录点赞数量
调用increment，decement（加，减）
redis--sessioncallback-一个事务内建立连接，同一个连接内完成多个命令的进行
页面点赞-like页面-找到controller方法-到service方法（页面是onclick就会触发）--discuss.js有响应的function-保持和controller方法的参数一致
关注、取消关注
统计用户的关注数和粉丝数
开发也是从RedisKeyUtil里面开始写


2.关注、取消关注
统计用户的粉丝、关注数
如何关注人？

涉及到的文件
- RedisKeyUtil
- FollowSerivice.java
- UserController.java
- FollowController.java
- profle.html
- profle.js

3.查询某人对实体的点赞数量

 涉及到的文件
  + LikeService.java
  + LikeController.java
  + discuss.js
  + usercontroller.java -- 查看个人主页
  + index.html
  + profile.html
  
 4.查询某个用户获得的赞

涉及到的文件
- FollowService.java
- followController.java
- profilehtml-找到主页，进去点击关注按钮
- profile.js


5.关注列表、粉丝列表
  *业务层和表现层*
查询用户、支持**分页**
处理查询关注的人、查询粉丝的请求
编写查询关注的人、查询粉丝的请求

涉及到的文件
- FollowService.java(查询关注、粉丝)
    （map数据包装）--（支持分页，函数参数设置好）--reverseRange范围查询
    （list数据包装map）--返回结果，再返回给表现层，处理表现层
- FollowController.java
    requestMapping--path要根据用户id去变化
    先注入service,调用findUserByid(userid)-去获得对象，将查询数据返回给页面
    调用上面的service方法，查询followees的传入分页参数，userid等，上面可以知道返回的是list
                 


6.优化登录模块
使用redis存储验证码  
使用redis存储登录凭证
使用redis缓存用户信息
这些数据访问效率高或者存储时间短

涉及到的文件
- LoginController.java

需要修改的地方

