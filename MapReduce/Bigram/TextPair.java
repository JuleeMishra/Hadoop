package com.biGram;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class TextPair implements WritableComparable<TextPair> {
	Text first;
	Text second;
	
	public void set(Text first, Text second){
		this.first=first;
		this.second=second;
	}
	TextPair(){
		set(new Text(), new Text());
	}
	TextPair(String first, String second){
		set(new Text(first), new Text(second));
	}
	TextPair(Text first, Text second){
		set(new Text(first), new Text(second));
	}
	@Override
	public void readFields(DataInput arg0) throws IOException {
		first.readFields(arg0);
		second.readFields(arg0);
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		first.write(arg0);
		second.write(arg0);
	}


	@Override
	public int compareTo(TextPair tp) {
		int cmp = first.compareTo(tp.first);
		if(cmp!=0){
			return cmp;
		}
		return second.compareTo(tp.second);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TextPair other = (TextPair) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "first + '\t' + second" ;
	}
	
	
}