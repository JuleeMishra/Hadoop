package com.invertedIndex;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/*****************************************************************************
* Input:																	 *
* 1st File: index.docs                                                       *
* An inverted index lists documents per word instead of words per document   *
*                                                                            *
* 2nd File: abc.docs                                                         *
* An inverted index lists documents per word instead of words per document   *
* 																			 *
* Output:                                                                    *
* An 			index.docs                                                   *
* inverted 	index.docs                                                       *
* figure		abc.docs                                                     *
* An			xyz.docs                                                     *
* resources	abc.docs, ft.txt,..,..                                           *
* An 			abc.docs                                                     *
******************************************************************************/
public class IIndexMap extends Mapper<LongWritable, Text, Text, Text>{
	private Text word = new Text();
	private Text fileName = new Text();
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		
		FileSplit currentSplit = ((FileSplit) context.getInputSplit());
		String fileNameStr = currentSplit.getPath().getName();
		fileName = new Text(fileNameStr);
		
		String line = value.toString();
		StringTokenizer st = new StringTokenizer(line);
		
		while(st.hasMoreTokens()){
			word.set(st.nextToken());
			context.write(word, fileName);
			
		}
	}
}
