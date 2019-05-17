map-reduce code Notes

Error: Hadoop: No Such Method Exception
olution: Your mapper and reducer classes need to be defined static, otherwise the compile creates a constructor with a single argument (the parent MaxTemperature class). Hence there now is not a default constructor.

Error: classNotFoundException: input/sample.txt output
Solution: Run the job including packge with class name 
     i.e  hadoop jar wordcount.jar com.hadoop.WordCount input output
		  hadoop jar jarName.jar packagename.MainClassName inputPath outputPath
		  
Error: java.io.IOException: Type mismatch in key from map: expected org.apache.hadoop.io.Text, received org.apache.hadoop.io.LongWritable
Solution : 

Error: java.lang.ClassCastException: org.apache.hadoop.io.LongWritable cannot be cast to org.apache.hadoop.io.IntWritable 
Solution: Converted IntWritable to LongWritable for Mapper ByteOffset