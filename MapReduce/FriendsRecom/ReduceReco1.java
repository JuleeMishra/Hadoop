package recom;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


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