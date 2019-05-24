package com.biGram;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/********************************************************************************************
 * INPUT:                                                                                   *
 * MapReduce is a framework originally developed at Google that allows for easy large scale *
 * distributed computing across a number of domains                                         *
 *                                                                                          *
 * OUTPUT:                                                                                  *
 * (MapReduce is),1                                                                         *
 * (is a),1                                                                                 *
 * (a framework),1                                                                          *
 * (framework originally),1                                                                 *
 * (originally developed),1                                                                 *
 * (is, a), 1                                                                               *
 *                                                                                          *
 *                                                                                          *
 * ******************************************************************************************/

public class MyMap1 extends Mapper<LongWritable, Text, TextPair, IntWritable>{
	public void map(LongWritable key, Text value,Context context) throws IOException, InterruptedException{
		String line = value.toString();
		String[] words = line.split(" ");
		int i; 
		for(i=0; i<=words.length-1; i++){
			if(i<words.length-1){
				context.write(new TextPair(new Text(words[i]),new Text(words[i+1])),new IntWritable(1));
			}
		}
	}
}













