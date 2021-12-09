# KylinBot

[![](https://img.shields.io/badge/blog-Kylin-blue)](http://kyl1n.top/) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


[comment]: <> (中 / English [![]&#40;https://img.shields.io/badge/%E8%AF%AD%E8%A8%80-%E4%B8%AD%E6%96%87-green&#41;]&#40;https://github.com/KyL0N/KylinBot/blob/main/README.zh-CN.md&#41; [![]&#40;https://img.shields.io/badge/Language-English-green&#41;]&#40;https://github.com/KyL0N/KylinBot/blob/main/README.md&#41;)

[中](https://github.com/KyL0N/KylinBot/blob/main/README.zh-CN.md) / [English](https://github.com/KyL0N/KylinBot/blob/main/README.md)


这是基于 [simple-robot](https://github.com/ForteScarlet/simpler-robot)  框架使用 [mirai组件](https://github.com/ForteScarlet/simpler-robot/tree/dev/component/component-mirai) 对接 [Mirai](https://github.com/mamoe/mirai) 的 Demo 项目

## Install

- fork 或者 clone 此项目到你的本地，并使用 IDE 工具打开并构建它

**如果你 Clone 或者 Fork 了这个项目, 就代表你已经阅读完整个 Readme ，请保证你已经阅读完**

```shell
git clone https://github.com/KyL0N/KylinBot.git
```

## Usage

- 配置文件

打开文件夹  [src/main/](src/main/resources)  并创建一个名为 simbot-bots 的文件夹，在文件夹中创建 `yourBot1.bot` 文件，填入


```yaml
code=QQ号
password=QQ密码
```

**请务必确认 QQ 号 QQ 密码不会泄露，因此项目造成的个人信息泄露以及财产损失，本人概不负责**

- 运行之前

  **将你的 bot 放在一些测试用的群而不是一些大型群**

- 运行

  执行 [KylinBotApplication](src/main/java/top/kylinbot/demo/KylinBotApplication.java)  中的 main 方法

## TODO
1. - [ ] 完成基本框架
2. - [x] 日麻何切
3. - [ ] 安卓端
4. - [ ] 使用JPA完成数据库上的重构

## Contributing

PRs accepted.

## 鸣谢

特别感谢 [JetBrains](https://www.jetbrains.com/?from=kylinbot) 为开源项目提供免费的 [IntelliJ IDEA](https://www.jetbrains.com/idea/?from=mirai) 等 IDE 的授权  
[<img src=".github/jetbrains-variant-3.png" width="200"/>](https://www.jetbrains.com/?from=kylinbot)

## License

**Apache 2.0 License**


