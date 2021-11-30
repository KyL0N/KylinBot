# KylinBot

[![](https://img.shields.io/badge/blog-Kylin-blue)](http://kyl1n.top/) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


English Readme: [![](https://img.shields.io/badge/Language-English-green)](https://github.com/KyL0N/KylinBot/blob/main/README.md)


这是基于[simple-robot](https://github.com/ForteScarlet/simpler-robot) 框架使用[mirai组件](https://github.com/ForteScarlet/simpler-robot/tree/dev/component/component-mirai)对接[Mirai](https://github.com/mamoe/mirai) 的Demo项目。

## Install

- fork或者clone此项目到你的本地，并使用IDE工具打开并构建它。

**如果你Clone或者Fork了这个项目, 就代表你已经阅读完整个Readme, 请保证你已经阅读完**

```shell
git clone https://github.com/KyL0N/KylinBot.git
```

## Usage

- 配置文件
打开文件夹 [src/main/](src/main/resources/) 并创建一个名为simbot-bots的文件夹, 在文件夹中创建`yourBot1.bot`文件, 填入


```yaml
code=QQ号
password=QQ密码
```

**请务必确认QQ号QQ密码不会泄露，因此项目造成的个人信息泄露以及财产损失, 本人概不负责**

- 运行之前

  **将你的bot放在一些测试用的群而不是一些大型群**

- 运行

  执行[SimbotExampleApplication](src/main/java/top/kylinbot/demo/SimbotExampleApplication.java) 中的main方法

## TODO
1. - [ ] 完成基本框架
2. - [ ] 日麻何切
3. - [ ] 安卓端

## Contributing

PRs accepted.

## License

**Apache 2.0 License**


