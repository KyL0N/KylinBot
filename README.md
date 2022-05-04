# KylinBot

[![](https://img.shields.io/badge/blog-Kylin-blue)](http://kyl1n.top/) 
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

[comment]: <> (中 / English [![]&#40;https://img.shields.io/badge/%E8%AF%AD%E8%A8%80-%E4%B8%AD%E6%96%87-green&#41;]&#40;https://github.com/KyL0N/KylinBot/blob/main/README.zh-CN.md&#41; [![]&#40;https://img.shields.io/badge/Language-English-green&#41;]&#40;https://github.com/KyL0N/KylinBot/blob/main/README.md&#41;)

[中](https://github.com/KyL0N/KylinBot/blob/main/README.zh-CN.md) / [English](https://github.com/KyL0N/KylinBot/blob/main/README.md)


This project is  based on [simple-robot](https://github.com/ForteScarlet/simpler-robot) which framework use [mirai component ](https://github.com/ForteScarlet/simpler-robot/tree/dev/component/component-mirai)to [Mirai](https://github.com/mamoe/mirai) 's demo project

## Project In WIP

Oh well, cuz personal reseaons, I'm not wanna to spending time on this project, but maybe sometimes I will come back and rebuild what I love and what I eager.

## Install

- fork or clone code on locally，use IDE to construct

**Warning**: if you clone or fork this project means you have read this document, please make sure you have already read it over

```shell
git clone https://github.com/KyL0N/KylinBot.git
```

## Usage

- configuration
open [src/main/](src/main/java/main/resources) and create a directory named simbot-bots, create file`yourBot1.bot`into the  directory, put below in it


```yaml
code=yourQQcode
password=yourQQpasswd
```


open [src/main/resources](src/main/java/main/resources) create a file name `KylinBot.properties` 
put in as followed

```properties
#MySQL
MysqlUrl=jdbc:mysql://xxx.xxx:3306/xxx?useSSL=false #your Mysql Database url
MysqlUser=xxx                                       #Username
MysqlPassword=xxx                                   #pwd
#osuClient
oauthID=114514                                      #your osu client id
redirectUrl=https://xxx.xxx                         #recall uri
oauthToken=xxxxxxxxxxxxx                            #client key
api=https://osu.ppy.sh/api/v2/
#tillerino
#https://ppaddict.tillerino.org/
tillerinoURL=https://api.tillerino.org
TillerinoBotKey=xxxxxxxxxx                          #in https://ppaddict.tillerino.org/ sign up a user and get key
```


**Please Make Sure Your QQ And Passwd Wouldn't Leak, If Your Private Information was Leaked And Make Property Loss Due To This Project, I Am Not Responsible For It**

- Before Running

  **Put your bot in some test groups instead of some large groups**

- run

  run main function in[KylinBotApplication](src/main/java/main/java/main/java/top/kylinbot/demo/KylinBotApplication.java)

## TODO
1. - [ ] Complete Basic Func
2. - [x] What tiles to cut in Japanese Mahjong
3. - [ ] Android Client
4. - [ ] JPA to reconstruct project

## Contributing

PRs accepted.

[comment]: <> (## Acknowledgements)

[comment]: <> (Thanks to [JetBrains]&#40;https://www.jetbrains.com/?from=mirai&#41; for allocating free open-source licences for IDEs such as [IntelliJ IDEA]&#40;https://www.jetbrains.com/idea/?from=mirai&#41;.  )

[comment]: <> ([<img src=".github/jetbrains-variant-3.png" width="200"/>]&#40;https://www.jetbrains.com/?from=mirai&#41;)


## License

**Apache 2.0 License**
The project has signed the Apache 2.0 license, please refer to LICENSE.txt for details

