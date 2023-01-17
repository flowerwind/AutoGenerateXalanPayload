# AutoGenerateXalanPayload

### 简介

这是一款根据cve-2022-34169漏洞延伸出的一个针对jdk xalan溢出漏洞的payload自动化生成工具。由于发现不同版本的Jdk所需要的溢出payload不同，所以这个自动化的生成工具诞生了，只需要用不同版本的JDK运行这个工程就可以生成出不同的xslt利用文件。

### 使用说明

复制select.bak.xslt为select.xslt，修改自己要执行的命令

![image-20230117143738105](images/image-20230117143738105.png)

然后运行java -jar AutoGenerateXalanPayload.jar即可，过程中又一些错误抛出，忽略即可

![image-20230117144434703](images/image-20230117144434703.png)

### 改造思路

后面会出文章

### JDK版本对比

| jdk版本   | xsls类型 |
| --------- | -------- |
| Jdk-8u301 | A        |
| Jdk-8u202 | A        |
| Jdk-8u162 | A        |
| Jdk8u152  | A        |
| Jdk8u151  | B        |
| Jdk8u144  | B        |
| Jdk8u131  | B        |
| Jdk8u121  | C        |
| Jdk8u111  | C        |
| Jdk8u102  | D        |
| Jdk8u101  | D        |
| Jdk8u91   | D        |
| Jdk8u60   | D        |
| Jdk8u20   | D        |
| Jdk7u40   | D        |
| Jdk7u21   | D        |
| Jdk7u10   | D        |
| Jdk7u05   | D        |
| Jdk7u04   | D        |
| Jdk7u03   | E        |
| Idk7u02   | E        |
| Jdk7u0    | E        |
| Jdk6u45   | E        |
| Jdk6u20   | E        |
| Jdk6u17   | F        |
| Jdk6u15   | F        |
| Jdk6u10   | F        |
| Jdk6u0    | F        |

### 参考

https://blog.noah.360.net/xalan-j-integer-truncation-reproduce-cve-2022-34169/
