# 标准微服务模块说明
- 使用springboot作为项目启动器
- 使用dubbo作为微服务框架
- 使用apollo作为配置服务框架
- 依赖vsim-parent做公共库版本号管理
- 使用logback打日志
- 依赖dubbo-demo-interface二方库(演示正确使用二方库)

## 默认配置（务必修改）
```
spring.application.name=vsim2-template
dubbo.application.name=${spring.application.name}
dubbo.scan.base-packages=com.simo.vsim.service
demo.service.version=1.0.0
zookeeper.port = 2181
dubbo.protocol.name=dubbo
## Random port
dubbo.protocol.port=-1
dubbo.registry.file = ${user.home}/dubbo-cache/${spring.application.name}/dubbo.cache

## 在Apollo配置中心上配置
## Dubbo Registry
#dubbo.registry.address=zookeeper://192.168.14.41:${zookeeper.port}
## configcenter
#dubbo.configcenter.address=zookeeper://192.168.14.41:${zookeeper.port}
## Dubbo MetaData
#dubbo.metadata-report.address=zookeeper://192.168.14.41:${zookeeper.port}

# Apollo应用全局唯一的身份标识
app.id=vsim
# Apollo Meta Server 地址
apollo.meta=http://192.168.14.44:8080
#apollo.meta=http://192.168.1.61:8080
# 自定义本地配置文件缓存路径
apollo.cacheDir=./config
# 设置在应用启动阶段就加载 Apollo 配置
apollo.bootstrap.enabled=true
# 将 Apollo 配置加载提到初始化日志系统之前，需要托管日志配置时开启
apollo.bootstrap.eagerLoad.enabled = true
# 注入 application namespace
#apollo.bootstrap.namespaces = application
#关闭springValue自动注入
#apollo.autoUpdateInjectedSpringProperties=false
#如果用docker做apollo的server注意这个issue https://github.com/ctripcorp/apollo/issues/1481
#-Denv=local进入本地开发模式
```
