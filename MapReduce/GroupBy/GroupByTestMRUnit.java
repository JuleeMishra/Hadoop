package com.groupBy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

public class GroupByTestMRUnit {
	MapDriver<LongWritable,Text,Text,DoubleWritable> mapDriver;
	ReduceDriver<Text,DoubleWritable,Text,DoubleWritable> reduceDriver;
	MapReduceDriver<LongWritable,Text,Text,DoubleWritable,Text,DoubleWritable> mapReduceDriver;
	
	@Before
	public void setUp(){
		GroupMap mapper = new GroupMap();
		mapDriver = new MapDriver<LongWritable,Text,Text,DoubleWritable>();
		mapDriver.setMapper(mapper);
		
		GroupReduce reducer = new GroupReduce();
		reduceDriver = new ReduceDriver<Text,DoubleWritable,Text,DoubleWritable>();
		reduceDriver.setReducer(reducer);
	}
	@Test
	public void testMapper() throws IOException{
		mapDriver.withInput(new LongWritable(0),new Text("20MICRONS,EQ,32.5,32.5,31.7,31.9,31.8,32.1,91832,2938573.55,19-SEP-2014,511,INE144J01027"));
		mapDriver.withOutput(new Text("20MICRONS"), new DoubleWritable(32.5));
		mapDriver.runTest();
	}
	
	@Test
	public void testReducer() throws IOException{
		List<DoubleWritable> value = new ArrayList<DoubleWritable>();
		value.add(new DoubleWritable(23.5));
		value.add(new DoubleWritable(32.5));
		value.add(new DoubleWritable(43.0));
		reduceDriver.withInput(new Text("20MICRONS"),value);
		reduceDriver.withOutput(new Text("20MICRONS"), new DoubleWritable(43.0));
		reduceDriver.runTest();
	}
	
	@Test
	public void testMapReduce() throws IOException{
		List<DoubleWritable> value = new ArrayList<DoubleWritable>();
		value.add(new DoubleWritable(23.5));
		value.add(new DoubleWritable(32.5));
		value.add(new DoubleWritable(43.0));
		mapReduceDriver.addInput(new LongWritable(0),new Text("20MICRONS,EQ,32.5,32.5,31.7,31.9,31.8,32.1,91832,2938573.55,19-SEP-2014,511,INE144J01027"));
		mapReduceDriver.addOutput(new Text("20MICRONS"), new DoubleWritable(43.0));
		mapReduceDriver.runTest();
	}
	
	
}
