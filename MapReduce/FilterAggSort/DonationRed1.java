package filterAggSort;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
/*
 * INPUT: 
 * upper_donar_city, {total1,total2,...}
 * NEW YORK, {12.50,8.00,12.50,22.50,2.00,11.50}
 * HOUSTON      12.00	
 * CHICAGO		8.75			
 * 
 * Output: 
 * sum(total), upper_donar_city
 * 8.75			CHICAGO	
 * 12.00		HOUSTON
 * 69.00		NEW YORK	
 * */
public class DonationRed1 extends Reducer<Text, DoubleWritable, DoubleWritable, Text> {
	public void reduce(Text city, Iterable<DoubleWritable> total, Context context) throws IOException, InterruptedException{

		Double sum_total=0.0;
		StringTokenizer st = new StringTokenizer(total.toString());
		while(st.hasMoreTokens()){
			sum_total = sum_total+Double.parseDouble(st.nextToken());
		}
		context.write(new DoubleWritable(sum_total), city);
	}
}
