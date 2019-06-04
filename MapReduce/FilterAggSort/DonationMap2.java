package filterAggSort;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
/*
 * Output: 
 * upper_donar_city, sum(total)
 * NEW YORK			 69.00	
 * HOUSTON			 12.00		
 * CHICAGO           8.75  
 * 
 * OutPut: 
 * NEW YORK		12.50
 * NEW YORK		8.00
 * NEW YORK		12.50
 * NEW YORK		22.50
 * NEW YORK		2.00
 * NEW YORK		11.50
 * HOUSTON		12.00
 * CHICAGO		8.75
 * */
public class DonationMap2 extends Mapper<DoubleWritable, Text, DoubleWritable, Text>{
	public void map(DoubleWritable sum, Text city, Context context) throws IOException, InterruptedException{
		
			context.write(sum, city);
	}
}
