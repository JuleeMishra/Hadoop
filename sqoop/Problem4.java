In this problem, we will focus on conversion between different file formats using spark or hive. This is a very import examination topic. I recommend that you master the data file conversion techniques and understand the limitations. You should have an alternate method of accomplishing a solution to the problem in case your primary method fails for any unknown reason. For example, if saving the result as a text file with snappy compression fails while using spark then you should be able to accomplish the same thing using Hive. In this blog\video I am going to walk you through some scenarios that cover alternative ways of dealing with same problem.    

Problem 4:
PART 1:
Import orders table from mysql as text file to the destination /user/cloudera/problem5/text. Fields should be terminated by a tab character ("\t") character and lines should be terminated by new line character ("\n"). 

cloudera@quickstart ~]$ sqoop import \
> --connect jdbc:mysql://localhost/retail_db \
> --username root \
> --password cloudera \
> --table orders \
> --as-textfile \
> --fields-terminated-by '\t' \
> --target-dir '/user/cloudera/problem5/text' \
> --m 1

Validation:
[cloudera@quickstart ~]$ hadoop fs -ls /user/cloudera/problem5/text
Found 2 items
-rw-r--r--   1 cloudera cloudera          0 2018-04-02 12:52 /user/cloudera/problem5/text/_SUCCESS
-rw-r--r--   1 cloudera cloudera    2999944 2018-04-02 12:52 /user/cloudera/problem5/text/part-m-00000
[cloudera@quickstart ~]$ hadoop fs -tail /user/cloudera/problem5/text/part-m-00000
68861	2014-06-13 00:00:00.0	3031	PENDING_PAYMENT
68862	2014-06-15 00:00:00.0	7326	PROCESSING
68863	2014-06-16 00:00:00.0	3361	CLOSED
68864	2014-06-18 00:00:00.0	9634	ON_HOLD
68865	2014-06-19 00:00:00.0	4567	SUSPECTED_FRAUD
68866	2014-06-20 00:00:00.0	3890	PENDING_PAYMENT
68867	2014-06-23 00:00:00.0	869	CANCELED

PART 2:
Import orders table from mysql into hdfs to the destination /user/cloudera/problem5/avro. File should be stored as avro file.
[cloudera@quickstart ~]$ sqoop import \
> --connect jdbc:mysql://localhost/retail_db \
> --username root \
> --password cloudera \
> --table orders \
> --as-avrodatafile \
> --target-dir '/user/cloudera/problem5/avro' \
> --m 1;

Validation:
[cloudera@quickstart ~]$ hadoop fs -ls /user/cloudera/problem5/avro
Found 2 items
-rw-r--r--   1 cloudera cloudera          0 2018-04-02 13:05 /user/cloudera/problem5/avro/_SUCCESS
-rw-r--r--   1 cloudera cloudera    1779793 2018-04-02 13:05 /user/cloudera/problem5/avro/part-m-00000.avro

PART 3:
Import orders table from mysql  into hdfs  to folders /user/cloudera/problem5/parquet. File should be stored as parquet file.
[cloudera@quickstart ~]$ sqoop import \
 --connect jdbc:mysql://localhost/retail_db \
 --username root \
 --password cloudera \
 --table orders \
 --as-parquetfile \
 --target-dir '/user/cloudera/problem5/parquet' \
 --m 1;
VALIDATION:
[cloudera@quickstart ~]$ hadoop fs -ls /user/cloudera/problem5/parquet
Found 3 items
drwxr-xr-x   - cloudera cloudera          0 2018-04-02 13:20 /user/cloudera/problem5/parquet/.metadata
drwxr-xr-x   - cloudera cloudera          0 2018-04-02 13:20 /user/cloudera/problem5/parquet/.signals
-rw-r--r--   1 cloudera cloudera     488256 2018-04-02 13:20 /user/cloudera/problem5/parquet/ada216d7-e858-4e36-b0c1-0d4822762a1b.parquet

PART 4:
Transform/Convert data-files at /user/cloudera/problem5/avro and store the converted file at the following locations and file formats

>>>> save the data to hdfs using snappy compression as parquet file at /user/cloudera/problem5/parquet-snappy-compress

NOTE: databricks allow sqlContext to read avro file. Spark 1.6 doesn't support avro read, so we need to import avro through databricks.
scala> import com.databricks.spark.avro._;
import com.databricks.spark.avro._
scala> val dataFile = sqlContext.read.avro("/user/cloudera/problem5/avro")
datafile: org.apache.spark.sql.DataFrame = [order_id: int, order_date: bigint, order_customer_id: int, order_status: string]

NOTE: convert this datafile into parquet file using snappy compression. In order to perform compression, we need to setup a configuration as below
scala> sqlContext.setConf("spark.sql.parquet.compression.codec", "snappy");

NOTE: to get only one file, repartition this file into a single file.
scala> dataFile.repartition(1).write.parquet("/user/cloudera/problem5/parquet-snappy-compress")
VALIDATION:
[cloudera@quickstart ~]$ hadoop fs -ls /user/cloudera/problem5/
Found 4 items
drwxr-xr-x   - cloudera cloudera          0 2018-04-02 13:05 /user/cloudera/problem5/avro
drwxr-xr-x   - cloudera cloudera          0 2018-04-02 13:20 /user/cloudera/problem5/parquet
drwxr-xr-x   - cloudera cloudera          0 2018-04-02 14:38 /user/cloudera/problem5/parquet-snappy-compress
drwxr-xr-x   - cloudera cloudera          0 2018-04-02 12:52 /user/cloudera/problem5/text

>>>> save the data to hdfs using gzip compression as text file at /user/cloudera/problem5/text-gzip-compress
convert avro file to text file format and store it as gzip format
scala> import com.databricks.spark.avro._;
import com.databricks.spark.avro._
scala> val dataFile = sqlContext.read.avro("/user/cloudera/problem5/avro")
scala> val dataFile1 = dataFile.map(x => (x(0)+"\t"+x(1)+"\t"+x(2)+"\t"+x(3)+"\t"))
dataFile1: org.apache.spark.rdd.RDD[String] = MapPartitionsRDD[30] at map at <console>:30
1	1374735600000	11599	CLOSED	
2	1374735600000	256	PENDING_PAYMENT	
3	1374735600000	12111	COMPLETE	


scala> dataFile1.saveAsTextFile("/user/cloudera/problem5/text-gzip-compress", classOf[org.apache.hadoop.io.compress.GzipCodec])
VALIDATIONS:
[cloudera@quickstart ~]$ hadoop fs -ls -h /user/cloudera/problem5/text-gzip-compress
Found 2 items
-rw-r--r--   1 cloudera cloudera          0 2018-04-02 15:13 /user/cloudera/problem5/text-gzip-compress/_SUCCESS
-rw-r--r--   1 cloudera cloudera    452.9 K 2018-04-02 15:13 /user/cloudera/problem5/text-gzip-compress/part-00000.gz

>>>> save the data to hdfs using no compression as sequence file at /user/cloudera/problem5/sequence
save the avro dataFile as sequence file without using compression. Sequence file is key-value file  and both key and value will be serializable in nature..
By default text data in spark is serializable in nature.
scala> import com.databricks.spark.avro._;
import com.databricks.spark.avro._
scala> val dataFile = sqlContext.read.avro("/user/cloudera/problem5/avro")
scala> dataFile.map(x => (x(0),x(0)+"\t"+x(1)+"\t"+x(2)+"\t"+x(3)))
res13: org.apache.spark.rdd.RDD[(Any, String)] = MapPartitionsRDD[43] at map at <console>:31
scala> dataFile.map(x => (x(0).toString,x(0)+"\t"+x(1)+"\t"+x(2)+"\t"+x(3)))
res14: org.apache.spark.rdd.RDD[(String, String)] = MapPartitionsRDD[44] at map at <console>:31

scala> dataFile1.saveAsSequenceFile("/user/cloudera/problem5/sequence")
VALIDATIONS:
[cloudera@quickstart ~]$ hadoop fs -ls -h /user/cloudera/problem5/sequence
Found 2 items
-rw-r--r--   1 cloudera cloudera          0 2018-04-02 15:32 /user/cloudera/problem5/sequence/_SUCCESS
-rw-r--r--   1 cloudera cloudera      3.3 M 2018-04-02 15:32 /user/cloudera/problem5/sequence/part-00000
NOTE: size of the sequence file is 3.3 M which is way more than actual AVRO file(1.7 M). This is because sequence files are stored as key,value pair.

>>>> save the data to hdfs using snappy compression as text file at /user/cloudera/problem5/text-snappy-compress
scala> import com.databricks.spark.avro._;
import com.databricks.spark.avro._
scala> val dataFile = sqlContext.read.avro("/user/cloudera/problem5/avro")
scala> val dataFile1 = dataFile.map(x => (x(0)+"\t"+x(1)+"\t"+x(2)+"\t"+x(3)+"\t"))
scala> dataFile1.saveAsTextFile("/user/cloudera/problem5/text-snappy-compress", classOf[org.apache.hadoop.io.compress.SnappyCodec])
18/04/02 16:05:50 ERROR executor.Executor: Exception in task 0.0 in stage 11.0 (TID 11)
java.lang.RuntimeException: native snappy library not available: this version of libhadoop was built without snappy support.

NOTE: build of cloudera doesn't support snappy compression, so instead of using spark, we perform snappy compression with HIVE
a) since we don't have to perform any transformation, we can use order table data in hive and save as snappy compression using HIVE.
[cloudera@quickstart ~]$ sqoop import 
--connect jdbc:mysql://localhost/retail_db 
--username root 
--password cloudera 
--table orders 
--as-textfile 
--compression-codec 'org.apache.hadoop.io.compress.SnappyCodec' 
--target-dir '/user/cloudera/problem5/text-snappy-compress' --m 1;
b) In case of snappy compression if we need some transformation over data, in that case, we can create a hive table and point this table to location where your compressed data is stored and do transformation ------> check it

Transform/Convert data-files at /user/cloudera/problem5/parquet-snappy-compress and store the converted file at the following locations and file formats

PART: 5
>>>>save the data to hdfs without compression as parquet file at /user/cloudera/problem5/parquet-no-compress
=> read the compressed avro datafile and store it as uncomressed paraquet file. (NOTE: there is no difference between reading a compressed and uncompressed datafile in spark)

scala> sqlContext.setConf("spark.sql.parquet.compression.codec","uncompressed");
>>>read the compressed parquet file
scala> val parquetDataFile = sqlContext.read.parquet("/user/cloudera/problem5/parquet-snappy-compress")
parquetDataFile: org.apache.spark.sql.DataFrame = [order_id: int, order_date: bigint, order_customer_id: int, order_status: string]
>>>after reading save the read datafile as uncompressed paraquet file.
scala> parquetDataFile.write.parquet("/user/cloudera/problem5/parquet-no-compress")
18/04/02 17:44:22 WARN parquet.CorruptStatistics: Ignoring statistics because created_by is null or empty! See PARQUET-251 and PARQUET-297


>>>>>save the data to hdfs using snappy compression as avro file at /user/cloudera/problem5/avro-snappy
scala> sqlContext.setConf("spark.sql.avro.compression.codec","snappy")
scala> parquetDataFile.write.avro("/user/cloudera/problem5/avro-snappy")

PART 6:Transform/Convert data-files at /user/cloudera/problem5/avro-snappy and store the converted file at the following locations and file formats

>>>>save the data to hdfs using no compression as json file at /user/cloudera/problem5/json-no-compress
scala> val dataFile = sqlContext.read.avro("/user/cloudera/problem5/avro-snappy")
dataFile: org.apache.spark.sql.DataFrame = [order_id: int, order_date: bigint, order_customer_id: int, order_status: string]
scala> sqlContext.setConf("spark.sql.json.compression.codec", "uncompressed")
NOTE: you can save the data in json format using any of the two way below:
scala> dataFile.write.json("/user/cloudera/problem5/json-no-compress")
scala> dataFile.toJson.saveAsTextFile("/user/cloudera/problem5/json-no-compress")  --> since json is plain text format, this methos is preffered


>>>>save the data to hdfs using gzip compression as json file at /user/cloudera/problem5/json-gzip
scala> dataFile.saveAsTextFile("/user/cloudera/problem5/json-gzip", classOf[org.apache.hadoop.io.compress.GzipCodec])

PART 7: Transform/Convert data-files at  /user/cloudera/problem5/json-gzip and store the converted file at the following locations and file formats
>>>>>>>>save the data to as comma separated text using gzip compression at   /user/cloudera/problem5/csv-gzip
transform the compressed json file to compressed textfile
a)read the compressed json file
scala> val jsonDataFile = sqlContext.read.avro("/user/cloudera/problem5/json-gzip")
b)convert the jsonDataFile into a comma separated to convert it in text format then compress it as gzip
jsonDataFile.map(x => (x(0)+","+x(1)+","+x(2)+","+x(3)+","+x(4))).saveAsTextFile("/user/cloudera/problem5/csv-gzip", classOf[org.apache.hadoop.io.compress.GzipCodec)

PART 8:Using spark access data at /user/cloudera/problem5/sequence and stored it back to hdfs using no compression as ORC file to HDFS to destination /user/cloudera/problem5/orc 
NOTE: to read sequence file you need to understand class of key and value.
reading sequence file through sc context takes 3 parameters.
sc.sequenceFile("path of the sequenceFile", class of Key, Class of Value)
>>understanding class of key
[cloudera@quickstart ~]$ hadoop fs -copyToLocal /user/cloudera/problem5/sequence/part-m-00000
>>get 1st 300 caracter of sequence file using cut command
[cloudera@quickstart ~]$ cut -c-300 part-m-00000
here both key and value are of text class
so,
scala> val seqDataFile = sc.sequenceFile("/user/cloudera/problem5/sequence", classOf[org.apache.hadoop.io.Text], classOf[org.apache.hadoop.io.Tesxt])


 
