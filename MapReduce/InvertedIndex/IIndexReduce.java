package com.invertedIndex;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/*****************************************************************************
* Input:                                                                     *
* An 			index.docs                                                   *
* inverted 	index.docs                                                       *
* figure		abc.docs                                                     *
* An			xyz.docs                                                     *
* resources	abc.docs, ft.txt,..,..                                           *
* An 			abc.docs                                                     *
*																			 *
* Output:                                                                    *
* An 			index.docs, xyz.docs, abc.docs                               *
* inverted 		index.docs                                                   *
* figure		abc.docs                                                     *
* resources	    abc.docs                                                     *
******************************************************************************/
public class IIndexReduce extends Reducer<Text, Text, Text, Text>{
	public void reduce(Text key, Iterable<Text> line, Context context) throws IOException, InterruptedException{
		
		StringBuilder sb = new StringBuilder();
		String[] values = line.toString().split(" ");
		for(String value:values){
			sb.append(value);
			if(line.iterator().hasNext()){
				sb.append(" | ");
			}
		}
		context.write(key, new Text(sb.toString()));
	}

}
