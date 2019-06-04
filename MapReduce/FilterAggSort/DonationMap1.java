package filterAggSort;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*******************************************************************
 * SELECT SUM(total) as sumtotal, UPPER(donor_city) as city
 * FROM donations 
 * WHERE donor_is_teacher != 't'
 * GROUP BY UPPER(donor_city)
 * ORDER BY sumtotal DESC;
 * 
 * JOB1:	
 * Map1
 * input: "id,donar_city,total,donar_is_teacher"
 * 
 * Output: 
 * Upper(donar_city) , total 
 * NEW YORK		12.50
 * NEW YORK		8.00
 * NEW YORK		12.50
 * NEW YORK		22.50
 * NEW YORK		2.00
 * NEW YORK		11.50
 * HOUSTON		12.00
 * CHICAGO		8.75
 * 
 * 
 * 
 *******************************************************************/
public class DonationMap1 extends Mapper<LongWritable, Text, Text, DoubleWritable>{
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		String line = value.toString();
		String[] words = line.split(",");
		
			if(words[3]!=("t")){
				String keyOut = words[1]; //city
				Double valOut = Double.parseDouble(words[2].toUpperCase()); //total
				context.write(new Text(keyOut), new DoubleWritable(valOut));
			}
			
	}
}
