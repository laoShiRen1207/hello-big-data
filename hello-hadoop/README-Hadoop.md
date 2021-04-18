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

# 1. Hadoop

## 1.1 概念

### 1.1.1 Hadoop 是什么？

`Hadoop`是一个由`Apache`基金会所开发的**分布式系统基础架构**。主要解决海量数据的**存储**和**分析计算**问题。

`Hadoop`通常是指一个更广泛的概念--**`Hadoop`生态圈**

![Hadoop生态圈](.\static\image\hadoop_0001.jpg)

### 1.1.2 Hadoop 的优势(4高)

1. 高可靠性:`Hadoop`底层维护了多个数据副本，所以即使`Hadoop`某个计算元素或存储出现故障，也不会导致数据丢失。
2. 高扩展性:在集群分配任务数据，可方便的扩展数以千计的节点。
3. 高效性:在`MapReduce`的西厢下`Hadoop`是并行工作的加快任务处理速度。
4. 高容错性:能够自动的将失败的任务重新分配。 

### 1.1.3 Hadoop 的组成

![](.\static\image\hadoop_0002.png)

#### 1.1.3.1 HDFS 架构概述

`Hadoop Distributed File System`简称`HDFS`，是一个分布式文件系统。

1. `NameNode`(`nn`): 存储文件的元数据如文件名，文件目录接口，文件属性，以及每个文件的**块列表**和**块所在的`DataNode`**等。
2. `DataNode`(`dn`): 在本地文件系统**存储文件块数据**，以及**块数据的校验和**。
3. `Secondary NameNode`(`2nn`): 每隔一段时间对`NameNode`数据据备份。

#### 1.1.3.2 Yarn架构概述

`Yet Another Resource Negotiator` 简称`Yarn `是`Hadoop`的资源管理器(主要管理`CPU`和内存)。

1. `ResouceManager`(`RM`): 整个集群资源的管理者。
2. `NodeManager`(`NM`): 单节点服务器管理者。
3. `ApplicationMaster`(`AM`): 单个任务运行的老大。
4. `Container`: 容器，相当于一台独立的服务器，里面封装了任务运行所需要的资源。

![](.\static\image\hadoop_0003.png)

#### 1.1.3.3 MapReduce架构概述

`MapReduce`将计算分为两个阶段`Map`和`Reduce`。

1. `Map`阶段: 并行处理输入数据。
2. `Reduce`阶段: 对`Map`结果进行汇总。

### 1.1.4 大数据技术生态体系

![](.\static\image\hadoop_0004.png)

推荐系统: 用户搜索/购买记录到日志，`Flume`采集对应的日志交给`Kafka`做缓冲，然后交给`Flink`做实时计算，计算完成存储成文件/数据库 推荐业务读取计算完成的结果返回给前端。

## 1.2 生产集群搭建

### 1.2.1 本地模式

### 1.2.2 完全分布式集群



