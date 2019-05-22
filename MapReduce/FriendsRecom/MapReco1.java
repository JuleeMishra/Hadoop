package recom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*******************************************************************************
* Input:                                                                       *
* Oscar		Jacob, Harry, Joe                                                  *
* (It means Oscar is friend with Jacob, Harry, Joe)                            *
*                                                                              *
* Output:                                                                      *
* (Jacob	Harry)			1                                                  *
* (Jacob	Joe)			1                                                  *
* (Harry	Jacob)			1                                                  *
* (Harry	Joe)			1                                                  *
* (Joe	Jacob)			1                                                      *
* (Joe	Harry)			1                                                      *
* (Oscar	Jacob)			0                                                  *
* (Oscar	Harry)			0                                                  *
* (Oscar	Joe)			0                                                  *
* 1--> Means they are not friends with each other                              *
* 0--> Means they are friends with each other                                  *
********************************************************************************/
public class MapReco1 extends Mapper<LongWritable, Text, TextPair, IntWritable>{
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		int index = line.indexOf('\t');
		String user_id = line.substring(0, index);
		StringTokenizer st = new StringTokenizer(line.substring(index+1),","); //Jacob, Harry, Joe 
		List<String> friends_id = new ArrayList<>();
		
		while(st.hasMoreTokens()){
			friends_id.add(st.nextToken());
		}
		
		for(int i=0; i<=friends_id.size(); i++){
			context.write(new TextPair(new Text(user_id),new Text(friends_id.get(i))), new IntWritable(0));
		}
		int length = friends_id.size();
		for(int i = 0; i < length; i++) {
			for(int j=0; j<length; j++){
				if(i!=j){
					context.write(new TextPair(new Text(friends_id.get(i)),new Text(friends_id.get(j))), new IntWritable(1));					
					}
				}
			}
		}
}
