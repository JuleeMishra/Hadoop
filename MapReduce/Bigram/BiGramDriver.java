package com.biGram;


import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class BiGramDriver extends Configured  implements Tool{

	@Override
	public int run(String[] args) throws Exception {
		if(args.length!=2){
			System.err.println("Number of arguments passed in command line is not 2");
			System.err.println("Check: BiGramDriver application");
			return -1;
		}
		Job job= Job.getInstance(getConf());
		job.setJobName("BiGramMapReduceJob");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setJarByClass(BiGramDriver.class);
		
		job.setMapperClass(MyMap1.class);
		//job.setMapOutputKeyClass(TextPair.class);//commented bcz output key and value class for mapper and reducer are same.
		//job.setMapOutputValueClass(IntWritable.class); 
		
		job.setReducerClass(MyReduce1.class);
		job.setOutputKeyClass(TextPair.class);
		job.setOutputValueClass(IntWritable.class);
		return job.waitForCompletion(true)?0:1;
	}
	
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new BiGramDriver(), args);
		System.exit(exitCode);
	}

}
