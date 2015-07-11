# Storage Performance Benchmark Tools

This is a performance bencmark framework for various distributed storage 
systems, particularly 
[HDFS-like](http://hadoop.apache.org/docs/r1.2.1/hdfs_design.html) systems 
(e.g., [Tachyon](http://tachyonp-roject.org).

## Building

The framework is built using Apache Maven. To build the framework, simply run:

```
mvn clean package
```

## Usage

To run the benchmark, use the `storage-perf` script provided in the `bin/` 
directory.

It's usage is as follows:

```
usage: storage-perf
 -b <arg>   The benchmark to run. To run all benchmarks for all classes,
            specify "all"
 -d <arg>   Path to data. (REQUIRED)
 -r <arg>   Path where the results will be stored.
```

## Benchmarking different storage-systems

Different storage frameworks may require tweaking certain configurations for the
benchmark to work. In particular, the benchmark script should be able to locate
the Hadoop core-site.xml and hdfs-site.xml files, and these files should be
properly configured to handle your filesystem of choice. The benchmark scripts
look for these configuration files in ${HADOOP_HOME}/conf, but you can change
this by modifying [this](conf/storage-env.sh) file. 

Additionally, the scripts should also be locate the jars containing the 
implementations of these fileystems. The maven build will automatically include
the implementation for HDFS 2.0.0-cdh4.3.0, but this can be changed by modifying
the [pom.xml](pom.xml) file. Additionally, the jars are searched for by the
benchmark scripts in $HADOOP_HOME/lib, but this can also be modified by 
modifying [this](conf/storage-env.sh) file.

I'll describe the configurations required for three of storage systems here:

### Local File System

Files on the local filesystem are represented as their absolute path pre-pended
with the "file://" prefix. Additionally, the core-site.xml file should contain
the following property:

```xml
<property>
    <name>fs.file.impl</name>
    <value>org.apache.hadoop.fs.LocalFileSystem</value>
    <description>The FileSystem for file: uris.</description>
</property>
```

### Hadoop Distributed File System

Files on HDFS are typically represented by "hdfs://" followed by the IP and port
of the namenode host. Additionally, the core-site.xml file should contain the
following property:

```
<property>
    <name>fs.hdfs.impl</name>
    <value>org.apache.hadoop.hdfs.DistributedFileSystem</value>
    <description>The FileSystem for hdfs: uris.</description>
</property>
```

### Tachyon File System

Tachyon files have URIs that are similar to files on HDFS; they are represented
by the "tachyon://" prefix, followed by the IP and port of the Tachyon Master, 
which is followed by the path of the file on Tachyon FS. The core-site.xml file
should contain the following property:

```
<property>
    <name>fs.tachyon.impl</name>
    <value>tachyon.hadoop.TFS</value>
    <description>The FileSystem for tachyon: uris.</description>
</property>
```
