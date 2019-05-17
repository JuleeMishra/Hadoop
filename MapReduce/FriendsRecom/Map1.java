package Recom;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/* for each line of input, generates user pairs that have common friend, or are already friends */
/*
Input: 
Navdeep		Vitthal, Janani, Pratik 
(It means Navdeep is friend with Vitthal, Janani, Pratik)

Output:     
Vitthal		Janani		1
Vitthal		Pratik		1
Janani		Pratik		1
Janani		Vitthal		1
Pratik		Vitthal		1
Pratik		Janani		1
Navdeep		Janani		0
Navdeep		Vitthal		0
Navdeep		Pratik		0
1--> Means they are not friends with each other
0--> Means they are friends with each other
*/
public class Map1 extends Mapper<LongWritable, Text, TextPair, IntWritable> {

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        int index = line.indexOf('\t'); //if '/t' not found, it returns -1 
        if(index == -1)
            return;
        String user_id = line.substring(0, index); //substring from index no 0 until index of '\t'
        List<String> friends_id = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(line.substring(index+1), ",");
        while(tokenizer.hasMoreTokens()) {
            friends_id.add(tokenizer.nextToken());
        }

        int length = friends_id.size(), i,j;
        for(i = 0; i < length; i++) {
            context.write(new TextPair(user_id , friends_id.get(i)), new IntWritable(0));
        }
        for(i = 0; i < length; i++) {
            for(j = 0; j < length; j ++) {
                if(j == i)
                    continue;
                context.write(new TextPair(friends_id.get(i),friends_id.get(j)), new IntWritable(1));
            }
        }
    }
}