# KylinBot

[![](https://img.shields.io/badge/blog-Kylin-blue)](http://kyl1n.top/) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


[comment]: <> (中 / English [![]&#40;https://img.shields.io/badge/%E8%AF%AD%E8%A8%80-%E4%B8%AD%E6%96%87-green&#41;]&#40;https://github.com/KyL0N/KylinBot/blob/main/README.zh-CN.md&#41; [![]&#40;https://img.shields.io/badge/Language-English-green&#41;]&#40;https://github.com/KyL0N/KylinBot/blob/main/README.md&#41;)

[中](https://github.com/KyL0N/KylinBot/blob/main/README.zh-CN.md) / [English](https://github.com/KyL0N/KylinBot/blob/main/README.md)

## 项目介绍 

这是基于 [simple-robot](https://github.com/ForteScarlet/simpler-robot)  框架使用 [mirai组件](https://github.com/ForteScarlet/simpler-robot/tree/dev/component/component-mirai) 对接 [Mirai](https://github.com/mamoe/mirai) 的 Demo 项目

## Install

- fork 或者 clone 此项目到你的本地，并使用 IDE 工具打开并构建它

**如果你 Clone 或者 Fork 了这个项目, 就代表你已经阅读完整个 Readme ，请保证你已经阅读完**

```shell
git clone https://github.com/KyL0N/KylinBot.git
```

## Usage

- 配置文件

打开文件夹  [src/main/resources](src/main/java/main/resources)  并创建一个名为 simbot-bots 的文件夹，在文件夹中创建 `yourBot1.bot` 文件，填入


```yaml
code=QQ号
password=QQ密码
```

打开文件夹  [src/main/resources](src/main/java/main/resources)  再创建一个文件名为 `KylinBot.properties` 的文件
填入

```properties
#MySQL
MysqlUrl=jdbc:mysql://xxx.xxx:3306/xxx?useSSL=false 此处填入数据库地址
MysqlUser=xxx                                       用户名
MysqlPassword=xxx                                   密码
#osuClient
oauthID=114514                                      在osu.ppy.sh处创建的客户端ID
redirectUrl=https://xxx.xxx                         应用回调链接
oauthToken=xxxxxxxxxxxxx                            客户端密钥
api=https://osu.ppy.sh/api/v2/
#tillerino
#https://ppaddict.tillerino.org/
tillerinoURL=https://api.tillerino.org
TillerinoBotKey=xxxxxxxxxx                          在https://ppaddict.tillerino.org/所创建的用户的密钥
```

**请务必确认 QQ 号 QQ 密码不会泄露，因此项目造成的个人信息泄露以及财产损失，本人概不负责**

- 运行之前

  **将你的 bot 放在一些测试用的群而不是一些大型群**

- 运行

  执行 [KylinBotApplication](src/main/java/main/java/main/java/top/kylinbot/demo/KylinBotApplication.java)  中的 main 方法

## TODO
1. - [ ] 完成基本框架
2. - [x] 日麻何切
3. - [ ] 安卓端
4. - [ ] 使用JPA完成数据库上的重构

## Contributing

PRs accepted.

[comment]: <> (## 鸣谢)

[comment]: <> (特别感谢 [JetBrains]&#40;https://www.jetbrains.com/?from=mirai&#41; 为开源项目提供免费的 [IntelliJ IDEA]&#40;https://www.jetbrains.com/idea/?from=mirai&#41; 等 IDE 的授权  )

[comment]: <> ([<img src=".github/jetbrains-variant-3.png" width="200"/>]&#40;https://www.jetbrains.com/?from=mirai&#41;)

## License

**Apache 2.0 License**
该项目签署了 Apache 2.0 授权许可，详情请参阅 LICENSE.txt



