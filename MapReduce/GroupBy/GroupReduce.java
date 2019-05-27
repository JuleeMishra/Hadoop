package com.groupBy;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
/* *******************************************************************
* Data Input:                                                        *
* SYMBOL,    {OPEN}                                                  *
* 20MICRONS, {32.5,8.9,...}                                          *
* 3IINFOTECH,{16.2,3.7,...}                                          *
*                                                                    *
* Output:                                                            *
* SYMBOL,    max{OPEN}>20                                            *
**********************************************************************/
public class GroupReduce extends Reducer<Text,DoubleWritable,Text,DoubleWritable>{
	public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException{
		
		Double maxValue = Double.MIN_VALUE;
		for(DoubleWritable value:values){
			maxValue = Math.max(maxValue, value.get());
		}
		if(maxValue>20){
			context.write(key, new DoubleWritable(maxValue));
		}
		
	}
}
