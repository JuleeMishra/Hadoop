package com.invertedIndex;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class invertedIndex extends Configured implements Tool{

	@Override
	public int run(String[] args) throws Exception {

		if(args.length!=2){
			System.err.println("Invalid Command");
			System.err.println("Usage: <input path> <output path>");
			return -1;
		}
		Job job = Job.getInstance(getConf());
		job.setJobName("InvertedIndex-Job");
		job.setJarByClass(invertedIndex.class);

		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setMapperClass(IIndexMap.class);
		job.setCombinerClass(IIndexReduce.class);
		job.setReducerClass(IIndexReduce.class);
		
		Path inputFilePath = new Path(args[0]);
		Path outputFilePath = new Path(args[1]);
		
		FileInputFormat.addInputPath(job, inputFilePath);
		FileOutputFormat.setOutputPath(job, outputFilePath);
		
		FileInputFormat.setInputDirRecursive(job, true);
				
		return job.waitForCompletion(true)?0:1;
	}
	
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new invertedIndex(), args);
		System.exit(exitCode);
		
	}

}
