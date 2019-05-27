package com.groupBy;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/****************************************************************************************************
 * Data Input:																							*
 * SYMBOL,SERIES,OPEN,HIGH,LOW,CLOSE,LAST,PREVCLOSE,TOTTRDQTY,TOTTRDVAL,TIMESTAMP,TOTALTRADES,ISIN, *
 * 20MICRONS,EQ,32.5,32.5,31.7,31.9,31.8,32.1,91832,2938573.55,19-SEP-2014,511,INE144J01027,        *
 * 3IINFOTECH,EQ,8.9,8.9,8.45,8.55,8.55,8.65,1109153,9569097.6,19-SEP-2014,2921,INE748C01020,       *
 *																			                        *
 * SQL equivalent Query:																			*
 * select SYMBOL, Max(OPEN) where SERIES = "EQ" group by SYMBOL having Max(OPEN)>20;                *
 * The Mapper class for an SQL group   by query.                                                       *
 * The Grouping key becomes the key for map output                                                  *
 ****************************************************************************************************/
public class GroupMap extends Mapper<LongWritable,Text,Text,DoubleWritable>{
	public void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException{
		String line = value.toString();
		String[] columns = line.split(",");
		if(columns[0]=="SYMBOL"){
			return;
		} 
		if(columns[1].equals("EQ")){
			Double open = Double.parseDouble(columns[2]);
			context.write(new Text(columns[0]), new DoubleWritable(open));
		}
	}
}
