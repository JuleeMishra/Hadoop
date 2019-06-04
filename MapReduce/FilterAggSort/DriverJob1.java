package filterAggSort;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class DriverJob1  extends Configured  implements Tool{
	private static final String OUTPUT_PATH = "/test/intermediate_output"; //path to store intermediate output from 1st map reduce job

	@Override
	public int run(String[] args) throws Exception {
		FileSystem fs = FileSystem.get(getConf());//FileSystem objects are able to access HDFS. 
        if(fs.exists(new Path(OUTPUT_PATH))){ //checks whether the output path exists
            fs.delete(new Path(OUTPUT_PATH),true);//if exist, it delete the file.
        }
		if(args.length!=2){
			System.err.println("Please pass 2 arguments");
		}
		 
		Job job1= Job.getInstance(getConf());
		job1.setJarByClass(DriverJob1.class);
		job1.setJobName("FilterAggregation-job");

		//Mapper Configuration
		job1.setMapperClass(DonationMap1.class);
		job1.setInputFormatClass(TextInputFormat.class);
		job1.setMapOutputKeyClass(LongWritable.class);
		job1.setMapOutputValueClass(DoubleWritable.class);
	
		//Reducer Configurations
		//Since we are doing an aggregation task here, using our Reducer as a Combiner by calling  job.setCombinerClass(DonationRed1.class) improves performance.*/
		job1.setReducerClass(DonationRed1.class);
		job1.setCombinerClass(DonationRed1.class);
		job1.setNumReduceTasks(1);
		
		job1.setOutputKeyClass(DoubleWritable.class);
		job1.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job1, new Path(args[0]));
		FileOutputFormat.setOutputPath(job1, new Path(OUTPUT_PATH));
		job1.waitForCompletion(true);
		
		Job job2= Job.getInstance(getConf());
		job2.setJarByClass(DriverJob1.class);
		// The mapper which transforms (K:V) => (Double(V):K)
		job2.setMapperClass(DonationMap2.class);
		job2.setInputFormatClass(TextInputFormat.class);
		job2.setMapOutputKeyClass(DoubleWritable.class);
		job2.setMapOutputValueClass(Text.class);
		// Use default Reducer which simply transforms (K:V1,V2) => (K:V1), (K:V2)
		job2.setReducerClass(Reducer.class);
		job2.setNumReduceTasks(1);
		// Sort with descending Double order
		job2.setSortComparatorClass(DescDoubleComparator.class);
		job2.setJarByClass(DriverJob1.class);
		job2.setJobName("Sorting-job");
		
		FileInputFormat.addInputPath(job2, new Path(OUTPUT_PATH));
		FileOutputFormat.setOutputPath(job2, new Path(args[1]));
		
		return job2.waitForCompletion(true)?0:1;
	}
	
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new DriverJob1(), args);
		System.exit(exitCode);
	}

}
