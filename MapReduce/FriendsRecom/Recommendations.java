package recom;

/**
 * The Main class for the Recommendations MapReduce program
 * It sets up and runs 2 MR jobs chained together (output of 1 is input to the other)
 */


import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Recommendations extends Configured implements Tool{
    private static final String OUTPUT_PATH = "/test/intermediate_output"; //path to store intermediate output from 1st map reduce job 
   
	@Override
	public int run(String[] arg0) throws Exception {
		
        FileSystem fs = FileSystem.get(getConf());//FileSystem objects are able to access HDFS. 
        if(fs.exists(new Path(OUTPUT_PATH))){ //checks whether the output path exists
            fs.delete(new Path(OUTPUT_PATH),true);//if exist, it delete the file.
        }

        Job job1 = Job.getInstance(getConf());
        job1.setJobName("FriendRecom-MR1");
        job1.setJarByClass(Recommendations.class);
        job1.setOutputKeyClass(TextPair.class);
        job1.setOutputValueClass(IntWritable.class);

        job1.setMapOutputKeyClass(TextPair.class);
        job1.setMapOutputValueClass(IntWritable.class);

        job1.setMapperClass(MapReco1.class);
        job1.setReducerClass(ReduceReco1.class);

        FileInputFormat.addInputPath(job1, new Path(arg0[0]));
        FileOutputFormat.setOutputPath(job1, new Path(OUTPUT_PATH));
        job1.waitForCompletion(true); //Needs to modify return
        // -------- Second Map1 Reduce1 Job -------
        // For each user, find top recommendations
        Job job2 = Job.getInstance(getConf());
        job2.setJobName("FriendRecom-MR2");
        job2.setJarByClass(Recommendations.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(Text.class);

        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(TextIntPair.class);

        job2.setMapperClass(MapReco2.class);
        job2.setReducerClass(ReduceReco2.class);

        FileInputFormat.addInputPath(job2, new Path(OUTPUT_PATH));
        FileOutputFormat.setOutputPath(job2, new Path(arg0[1]));
        job2.waitForCompletion(true);
		
	}
	
	 public static void main(String[] args) throws Exception {
	       int exitCode = ToolRunner.run(new Recommendations(), args);
	       System.exit(exitCode);
	       
	    }

}
