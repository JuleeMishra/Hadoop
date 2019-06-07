package secondarySort;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/* Input:
 * LongWritable, (id,     donor_state,		donar_city,		total)		
 * 0, 			 (id1,    NY,         		new york,    	12.50)
 * 
 * Output:
 *  key								  val
 * ((donor_state, donor_city, total), id)
 * */

public class SecSortMap1 extends Mapper<LongWritable, Text, Compositekey, Text>{
	public void map(LongWritable keyIn, Text valIn, Context context) throws IOException, InterruptedException{
		Compositekey compositekey = new Compositekey();
		String line = valIn.toString();
		String[] words = line.split(",");
		String state = words[1].toLowerCase();
		String city = words[2].toLowerCase();
		float total = Float.parseFloat(words[3]);
		if(!words[1].isEmpty()||!words[2].isEmpty()){
			compositekey.set(state,city,total);
			context.write(compositekey, new Text(words[0]));
		}
		
		
	}
}

/*
SELECT donation_id, donor_state, donor_city, total
FROM donations
WHERE donor_state IS NOT NULL AND donor_city IS NOT NULL
ORDER BY lower(donor_state) ASC, lower(donor_city) ASC, total DESC;
*/
