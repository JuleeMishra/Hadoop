package com.biGram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

public class TestBigramCount{
	MapDriver<LongWritable, Text, TextPair, IntWritable> mapDriver;
	ReduceDriver<TextPair, IntWritable,Text, IntWritable> reduceDriver;
	MapReduceDriver<LongWritable, Text, TextPair, IntWritable,Text, IntWritable> mapReduceDriver;
	@Before
	public void setUp(){
		MyMap1 mapper = new MyMap1();
		mapDriver = new MapDriver<LongWritable, Text, TextPair, IntWritable>();
		mapDriver.setMapper(mapper);
		
		MyReduce1 reducer = new MyReduce1();
		reduceDriver = new ReduceDriver<TextPair, IntWritable,Text, IntWritable>();
		reduceDriver.setReducer(reducer);
		
		mapReduceDriver = new MapReduceDriver<LongWritable, Text, TextPair, IntWritable,Text, IntWritable>();
		mapReduceDriver.setMapper(mapper);
		mapReduceDriver.setReducer(reducer);
	}
	

	@Test
	public void testMapper() throws IOException{
		mapDriver.withInput(new LongWritable(1),new Text("this is the bigram this is the test"));
		mapDriver.withOutput(new TextPair("this","is"),new IntWritable(1));
		mapDriver.withOutput(new TextPair("is","the"),new IntWritable(1));
		mapDriver.withOutput(new TextPair("the","bigram"),new IntWritable(1));
		mapDriver.withOutput(new TextPair("bigram","this"),new IntWritable(1));
		mapDriver.withOutput(new TextPair("this","is"),new IntWritable(1));
		mapDriver.withOutput(new TextPair("is","the"),new IntWritable(1));
		mapDriver.withOutput(new TextPair("the","test"),new IntWritable(1));
		mapDriver.runTest();
	}
	@Test
	public void testReducer() throws IOException{
		List<IntWritable> value = new ArrayList<IntWritable>();
		value.add(new IntWritable(1));
		value.add(new IntWritable(1));
		reduceDriver.withInput(new TextPair("this","is"),value);
		reduceDriver.withOutput(new Text("this"+'\t'+"is"),new IntWritable(2));
		reduceDriver.runTest();
	}
	@Test
	public void testMapperReducer() throws IOException{
		List<IntWritable> value = new ArrayList<IntWritable>();
		value.add(new IntWritable(1));
		value.add(new IntWritable(1));
		mapReduceDriver.addInput(new LongWritable(1),new Text("this is the bigram this is the test"));
		mapReduceDriver.addOutput(new Text("bigram"+'\t'+"this"),new IntWritable(1));
		mapReduceDriver.addOutput(new Text("is"+'\t'+"the"),new IntWritable(2));
		mapReduceDriver.addOutput(new Text("the"+'\t'+"bigram"),new IntWritable(1));
		mapReduceDriver.addOutput(new Text("the"+'\t'+"test"),new IntWritable(1));
		mapReduceDriver.addOutput(new Text("this"+'\t'+"is"),new IntWritable(2));
		mapReduceDriver.runTest();
	}
	
}
