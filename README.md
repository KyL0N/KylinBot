# KylinBot

[![](https://img.shields.io/badge/blog-Kylin-blue)](http://kyl1n.top/) 
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

中 / English [![](https://img.shields.io/badge/%E8%AF%AD%E8%A8%80-%E4%B8%AD%E6%96%87-green)](https://github.com/KyL0N/KylinBot/blob/main/README.zh-CN.md) [![](https://img.shields.io/badge/Language-English-green)](https://github.com/KyL0N/KylinBot/blob/main/README.md)

This project is  based on [simple-robot](https://github.com/ForteScarlet/simpler-robot) which framework use [mirai component ](https://github.com/ForteScarlet/simpler-robot/tree/dev/component/component-mirai)to [Mirai](https://github.com/mamoe/mirai) 's demo project



## Install

- fork or clone code on locally，use IDE to construct

**Warning**: if you clone or fork this project means you have read this document, please make sure you have already read it over

```shell
git clone https://github.com/KyL0N/KylinBot.git
```

## Usage

- configuration
open [src/main/](src/main/resources) and create a directory named simbot-bots, create file`yourBot1.bot`into the  directory, put below in it


```yaml
code=yourQQcode
password=yourQQpasswd
```

**Please Make Sure Your QQ And Passwd Wouldn't Leak, If Your Private Information was Leaked And Make Property Loss Due To This Project, I Am Not Responsible For It**

- Before Running

  **Put your bot in some test groups instead of some large groups**

- run

  run main function in[KylinBotApplication](src/main/java/top/kylinbot/demo/KylinBotApplication.java)

## TODO
1. - [ ] Complete Basic Func
2. - [x] What tiles to cut in Japanese Mahjong
3. - [ ] Android Client
4. - [ ] JPA to reconstruct project

## Contributing

PRs accepted.

## License

**Apache 2.0 License**
