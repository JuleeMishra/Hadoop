package com.join;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * Assume file as table. Write mapper for 1st table(joinNames.csv)
 * Header of the file:
 * SYMBOL,NAME
 * 
 * Input:			 Output:
 * RIL,Reliance ---> <<RIL,0>,<Reliance>>
 * 
 * */
public class JoinMap1 extends Mapper<LongWritable, Text, TextIntPair, Text>{
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		
		String line = value.toString();
		String[] values = line.split(",");
		//skip the header of the file
		if(values[0].equals("SYMBOL")){
			return;
		}
		TextIntPair keyOut = new TextIntPair(new Text(values[0]),new IntWritable(0));
		context.write(keyOut, new Text(value));
	}
}
