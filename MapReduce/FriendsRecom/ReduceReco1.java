package recom;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/********************************************************************************************************************
* Input:                                                                                                            *
* (Jacob	Harry)			{1,1,1}                                                                                 *
* (Jacob	Joe)			{1}	  --> they are not friends with each other but have at least one friend in common   *
* (Harry	Jacob)			{1,1} --> they are not friends with each other but have at least 2 friend in common     *
* (Harry	Joe)			{1}	  --> they are not friends with each other but have at least one friend in common   *
* (Joe	Jacob)			{1}	  --> they are not friends with each other but have at least one friend in common       *
* (Joe	Harry)			{1}	  --> they are not friends with each other but have at least one friend in common       *
* (Oscar	Jacob)			{0,0} --> Already friends with each other                                               *
* (Oscar	Harry)			{0}	  --> Already friends with each other                                               *
* (Oscar	Joe)			{0}   --> Already friends with each other                                               *
*                                                                                                                   *
* Output:                                                                                                           *
* (Jacob	Harry)			3   ---> they have 3 friends in common                                                  *
* (Jacob	Joe)			1	---> they have 1 friends in common                                                  *
* (Harry	Jacob)			2 	---> they have 2 friends in common                                                  *
* (Harry	Joe)			1                                                                                       *
* (Joe	Jacob)			1                                                                                           *
* (Joe	Harry)			1                                                                                           *
*********************************************************************************************************************/                                                                                                                  
public  class ReduceReco1 extends Reducer<TextPair, IntWritable, TextPair, IntWritable> {
    public void reduce(TextPair key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0, prod = 1;
        for (IntWritable val : values) {
            sum += val.get();
            prod *= val.get();
        }
        if( prod!=0 ) //if they are not friends with each other and this pair of user can be recommended for friendship.
            context.write(key, new IntWritable(sum));
    }
}