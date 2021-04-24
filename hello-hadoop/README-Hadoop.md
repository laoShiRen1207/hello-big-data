# 大数据

## 大数据4大特点

1. `Volume `(海量数据存储)
2. `Velocity`(高速)
3. `Variety`(多样性)
4. `Value`(低价值密度 - 数据清洗)

平台

`Hadoop`,`Flume`,`Kafka`,`HBase`,`Spark`的等框架的平台搭建，集群性能监控，集群新能调优。

数据仓库

`ETL`数据清洗，数据分析，数仓建模

实时指标的分析性能调优，数据挖掘。

# Hadoop

[Hadoop 官网](http://hadoop.apache.org/)

## 1 概念

### 1.1 Hadoop 是什么？

`Hadoop`是一个由`Apache`基金会所开发的**分布式系统基础架构**。主要解决海量数据的**存储**和**分析计算**问题。

`Hadoop`通常是指一个更广泛的概念--**`Hadoop`生态圈**

![Hadoop生态圈](.\static\image\hadoop_0001.jpg)

### 1.2 Hadoop 的优势(4高)

1. 高可靠性:`Hadoop`底层维护了多个数据副本，所以即使`Hadoop`某个计算元素或存储出现故障，也不会导致数据丢失。
2. 高扩展性:在集群分配任务数据，可方便的扩展数以千计的节点。
3. 高效性:在`MapReduce`的西厢下`Hadoop`是并行工作的加快任务处理速度。
4. 高容错性:能够自动的将失败的任务重新分配。 

### 1.3 Hadoop 的组成

![](.\static\image\hadoop_0002.png)

#### 1.3.1 HDFS 架构概述

`Hadoop Distributed File System`简称`HDFS`，是一个分布式文件系统。

1. `NameNode`(`nn`): 存储文件的元数据如文件名，文件目录接口，文件属性，以及每个文件的**块列表**和**块所在的`DataNode`**等。
2. `DataNode`(`dn`): 在本地文件系统**存储文件块数据**，以及**块数据的校验和**。
3. `Secondary NameNode`(`2nn`): 每隔一段时间对`NameNode`数据据备份。

#### 1.3.2 Yarn架构概述

`Yet Another Resource Negotiator` 简称`Yarn `是`Hadoop`的资源管理器(主要管理`CPU`和内存)。

1. `ResouceManager`(`RM`): 整个集群资源的管理者。
2. `NodeManager`(`NM`): 单节点服务器管理者。
3. `ApplicationMaster`(`AM`): 单个任务运行的老大。
4. `Container`: 容器，相当于一台独立的服务器，里面封装了任务运行所需要的资源。

![](.\static\image\hadoop_0003.png)

#### 1.3.3 MapReduce架构概述

`MapReduce`将计算分为两个阶段`Map`和`Reduce`。

1. `Map`阶段: 并行处理输入数据。
2. `Reduce`阶段: 对`Map`结果进行汇总。

### 1.4 大数据技术生态体系

![](.\static\image\hadoop_0004.png)

推荐系统: 用户搜索/购买记录到日志，`Flume`采集对应的日志交给`Kafka`做缓冲，然后交给`Flink`做实时计算，计算完成存储成文件/数据库 推荐业务读取计算完成的结果返回给前端。

## 2 生产集群搭建

### 2.1 准备工作

正常安装`centos7.5`最小版

修改`ip`

~~~shell
vi /etc/sysconf/network-scripts/ipcfg-ens33
~~~

![image-20210424150001545](.\static\image\image-20210424150001545.png)

修改`host`

~~~shell
vi /etc/hostname
~~~

应用重启网络服务，如果报错就重启虚拟机`reboot`

~~~shell
systemctl restart network
~~~

安装`epel-release`

~~~shell
yum install -y epel-release
# 还需要安装 net-tools
yum install -y net-tools
# vim 编辑器
yum install -y vim 
~~~

关闭防火墙

~~~shell
systemctl stop firewalld
systemctl disable firewalld.service
~~~

安装`jdk`，解压配置

~~~shell
tar -zxvf jdk***.tar.gz
~~~

 进入`/etc/profile.d`，创建一个`shell`脚本，然后执行`source  /etc/profile`。

~~~shell
# Java_Home jdk 8
export JAVA_HOME=/root/module/jdk1.8.0_152
export PATH=$PATH:$JAVA_HOME/bin
~~~

检查`java`安装

~~~shell
java -version

## 打印 
java version "1.8.0_152"
Java(TM) SE Runtime Environment (build 1.8.0_152-b16)
Java HotSpot(TM) 64-Bit Server VM (build 25.152-b16, mixed mode)
~~~

#### 2.1.1 安装Hadoop 3.1.3

解压缩

~~~shell
tar -zxvf hadoop-3.1.3.tar.gz 
~~~

 进入`/etc/profile.d`，创建一个`shell`脚本，然后执行`source  /etc/profile`。

~~~shell
cat /etc/profile.d/my_hadoop_env.sh 
#Hadoop hadoop 3.1.3

export HADOOP_HOME=/root/module/hadoop-3.1.3

export PATH=$PATH:$HADOOP_HOME/bin
export PATH=$PATH:$HADOOP_HOME/sbin
~~~

检查`hadoop`安装

~~~shell
hadoop
# 打印
Usage: hadoop [OPTIONS] SUBCOMMAND [SUBCOMMAND OPTIONS]
 or    hadoop [OPTIONS] CLASSNAME [CLASSNAME OPTIONS]
  where CLASSNAME is a user-provided Java class

  OPTIONS is none or any of:

buildpaths                       attempt to add class files from build tree
--config dir                     Hadoop config directory
--debug                          turn on shell script debug mode
--help                           usage information
hostnames list[,of,host,names]   hosts to use in slave mode
hosts filename                   list of hosts to use in slave mode
loglevel level                   set the log4j level for this command
workers                          turn on worker mode

  SUBCOMMAND is one of:


    Admin Commands:

daemonlog     get/set the log level for each daemon

    Client Commands:

archive       create a Hadoop archive
checknative   check native Hadoop and compression libraries availability
classpath     prints the class path needed to get the Hadoop jar and the required libraries
conftest      validate configuration XML files
credential    interact with credential providers
distch        distributed metadata changer
distcp        copy file or directories recursively
dtutil        operations related to delegation tokens
envvars       display computed Hadoop environment variables
fs            run a generic filesystem user client
gridmix       submit a mix of synthetic job, modeling a profiled from production load
jar <jar>     run a jar file. NOTE: please use "yarn jar" to launch YARN applications, not this command.
jnipath       prints the java.library.path
kdiag         Diagnose Kerberos Problems
kerbname      show auth_to_local principal conversion
key           manage keys via the KeyProvider
rumenfolder   scale a rumen input trace
rumentrace    convert logs into a rumen trace
s3guard       manage metadata on S3
trace         view and modify Hadoop tracing settings
version       print the version

    Daemon Commands:

kms           run KMS, the Key Management Server

SUBCOMMAND may print help when invoked w/o parameters or with -h.
~~~

以上就算安装成功。

#### 2.1.2 hadoop 目录内容

~~~shell
cd hadoop-3.1.3
[root@hadoop1207 hadoop-3.1.3]# ll
总用量 180
drwxr-xr-x. 2 1000 1000    183 9月  12 2019 bin
drwxr-xr-x. 3 1000 1000     20 9月  12 2019 etc
drwxr-xr-x. 2 1000 1000    106 9月  12 2019 include
drwxr-xr-x. 3 1000 1000     20 9月  12 2019 lib
drwxr-xr-x. 4 1000 1000   4096 9月  12 2019 libexec
-rw-rw-r--. 1 1000 1000 147145 9月   4 2019 LICENSE.txt
-rw-rw-r--. 1 1000 1000  21867 9月   4 2019 NOTICE.txt
-rw-rw-r--. 1 1000 1000   1366 9月   4 2019 README.txt
drwxr-xr-x. 3 1000 1000   4096 9月  12 2019 sbin
drwxr-xr-x. 4 1000 1000     31 9月  12 2019 share
~~~

**bin 目录** 一些命令的文件目录

~~~shell
cd bin/
[root@hadoop1207 bin]# ll
总用量 996
-rwxr-xr-x. 1 1000 1000 441936 9月  12 2019 container-executor
-rwxr-xr-x. 1 1000 1000   8707 9月  12 2019 hadoop
-rwxr-xr-x. 1 1000 1000  11265 9月  12 2019 hadoop.cmd
-rwxr-xr-x. 1 1000 1000  11026 9月  12 2019 hdfs  			# 和资源存储相关的命令
-rwxr-xr-x. 1 1000 1000   8081 9月  12 2019 hdfs.cmd
-rwxr-xr-x. 1 1000 1000   6237 9月  12 2019 mapred			# 和计算相关的命令
-rwxr-xr-x. 1 1000 1000   6311 9月  12 2019 mapred.cmd
-rwxr-xr-x. 1 1000 1000 483728 9月  12 2019 test-container-executor
-rwxr-xr-x. 1 1000 1000  11888 9月  12 2019 yarn				# 和资源调度相关的命令
-rwxr-xr-x. 1 1000 1000  12840 9月  12 2019 yarn.cmd
~~~

**etc/hadoop 目录** 存储一些配置文件，配置`hdfs`,`mapre `,`yarn`

**sbin 目录** 存储一些启动脚本

~~~shell
cd sbin/
[root@hadoop1207 sbin]# ll
总用量 108
-rwxr-xr-x. 1 1000 1000 2756 9月  12 2019 distribute-exclude.sh
drwxr-xr-x. 4 1000 1000   36 9月  12 2019 FederationStateStore
-rwxr-xr-x. 1 1000 1000 1983 9月  12 2019 hadoop-daemon.sh			# 单节点服务器
-rwxr-xr-x. 1 1000 1000 2522 9月  12 2019 hadoop-daemons.sh
-rwxr-xr-x. 1 1000 1000 1542 9月  12 2019 httpfs.sh
-rwxr-xr-x. 1 1000 1000 1500 9月  12 2019 kms.sh
-rwxr-xr-x. 1 1000 1000 1841 9月  12 2019 mr-jobhistory-daemon.sh	# 启动历史服务器
-rwxr-xr-x. 1 1000 1000 2086 9月  12 2019 refresh-namenodes.sh
-rwxr-xr-x. 1 1000 1000 1779 9月  12 2019 start-all.cmd
-rwxr-xr-x. 1 1000 1000 2221 9月  12 2019 start-all.sh
-rwxr-xr-x. 1 1000 1000 1880 9月  12 2019 start-balancer.sh
-rwxr-xr-x. 1 1000 1000 1401 9月  12 2019 start-dfs.cmd
-rwxr-xr-x. 1 1000 1000 5170 9月  12 2019 start-dfs.sh  				# hdfs集群的启动命令
-rwxr-xr-x. 1 1000 1000 1793 9月  12 2019 start-secure-dns.sh
-rwxr-xr-x. 1 1000 1000 1571 9月  12 2019 start-yarn.cmd
-rwxr-xr-x. 1 1000 1000 3342 9月  12 2019 start-yarn.sh				# 资源调度器命令
-rwxr-xr-x. 1 1000 1000 1770 9月  12 2019 stop-all.cmd
-rwxr-xr-x. 1 1000 1000 2166 9月  12 2019 stop-all.sh
-rwxr-xr-x. 1 1000 1000 1783 9月  12 2019 stop-balancer.sh
-rwxr-xr-x. 1 1000 1000 1455 9月  12 2019 stop-dfs.cmd
-rwxr-xr-x. 1 1000 1000 3898 9月  12 2019 stop-dfs.sh
-rwxr-xr-x. 1 1000 1000 1756 9月  12 2019 stop-secure-dns.sh
-rwxr-xr-x. 1 1000 1000 1642 9月  12 2019 stop-yarn.cmd
-rwxr-xr-x. 1 1000 1000 3083 9月  12 2019 stop-yarn.sh
-rwxr-xr-x. 1 1000 1000 1982 9月  12 2019 workers.sh
-rwxr-xr-x. 1 1000 1000 1814 9月  12 2019 yarn-daemon.sh
-rwxr-xr-x. 1 1000 1000 2328 9月  12 2019 yarn-daemons.sh
~~~

### 2.2 本地模式

不借助`hdfs`将文件存储再服务器内部。

创建一个文件，随便写一些内容，统计每个单词出现频率。

~~~shell
cat laoshireninput/word.txt
laoshiren
xiangdehua
laoshiren
zhoujielun
~~~

执行计算，必须指定一个输入路径，一个输出路径

~~~shell
hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.1.3.jar wordcount laoshireninput/ ./laoshirenoutput

cd laoshirenoutput/
[root@hadoop1207 laoshirenoutput]# ll
总用量 4
-rw-r--r--. 1 root root 38 4月  25 01:18 part-r-00000			# 真正的数据
-rw-r--r--. 1 root root  0 4月  25 01:18 _SUCCESS				# 表示标记并没有数据

cat part-r-00000 
laoshiren	2
xiangdehua	1
zhoujielun	1
~~~

### 2.3 完全分布式集群

在创建2个完全一致的虚拟机，可以不装`JDK`和`hadoop`，等后期使用`scp`拷贝过去。

![](.\static\image\image-20210424181408221.png)

拷贝`JDK`和`Hadoop`

~~~shell
scp -r jdk1.8.0_152/ root@192.168.8.202:/root/module
# scp 安全拷贝
# -r 递归
# 本地文件
# 用户@主机:对应路径

scp -r hadoop-3.1.3/ root@192.168.8.202:/root/module
~~~

`rsync`同步