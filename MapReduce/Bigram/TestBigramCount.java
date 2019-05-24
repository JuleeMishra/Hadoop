package com.biGram;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;

public class TestBigramCount{
	MapDriver<LongWritable, Text, TextPair, IntWritable> mapDriver;
	@Before
	public void setUp(){
		MyMap1 mapper = new MyMap1();
		mapDriver = new MapDriver<LongWritable, Text, TextPair, IntWritable>();
		mapDriver.setMapper(mapper);
	}
	

	@Test
	public void testMapper(){
		mapDriver.withInput(new LongWritable(1),new Text("this is the bigram this is the test"));
		mapDriver.withOutput(new TextPair("bigram","this"),new IntWritable(1));
		mapDriver.withOutput(new TextPair("this","is"),new IntWritable(1));
		mapDriver.withOutput(new TextPair("is","the"),new IntWritable(1));
		mapDriver.withOutput(new TextPair("the","bigram"),new IntWritable(1));
		mapDriver.withOutput(new TextPair("bigram","this"),new IntWritable(1));
		mapDriver.withOutput(new TextPair("this","is"),new IntWritable(1));
		mapDriver.withOutput(new TextPair("is","the"),new IntWritable(1));
		mapDriver.withOutput(new TextPair("the","test"),new IntWritable(1));
	}
	
}