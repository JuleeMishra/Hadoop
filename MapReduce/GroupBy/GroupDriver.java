package com.groupBy;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class GroupDriver extends Configured implements Tool{

	@Override
	public int run(String[] args) throws Exception {
		if(args.length!=2){
			System.err.println("Input/output argument is missing");
		}
		Job job = Job.getInstance(getConf());
		job.setJobName("ByGroup-Job");
		job.setMapperClass(GroupMap.class);
		job.setReducerClass(GroupReduce.class);
		job.setJarByClass(GroupDriver.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);
		
		return job.waitForCompletion(true)?0:1;
	}
	
	public static void main(String[] args) throws Exception{
		int exitCode = ToolRunner.run(new GroupDriver(), args);
		System.exit(exitCode);
	}

}
