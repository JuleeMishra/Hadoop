package com.join;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class TextIntPair implements WritableComparable<TextIntPair> {
	Text first;
	IntWritable second;
	
	public void set(Text first, IntWritable second){
		this.first = first;
		this.second = second;
	}
	public TextIntPair(){
		set(new Text(), new IntWritable());
	}
	public TextIntPair(String first, Integer second){
		set(new Text(first), new IntWritable(second));
	}
	public TextIntPair(Text first, IntWritable second){
		set(first, second);
	}
	
	@Override
	public void write(DataOutput out) throws IOException {
		first.write(out);
		second.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		first.readFields(in);
		second.readFields(in);
	}

	@Override
	public int compareTo(TextIntPair arg0) {
		return 0;
	}

}
