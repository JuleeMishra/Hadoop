package filterAggSort;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class DescDoubleComparator extends WritableComparator{
	
	public DescDoubleComparator(){
		super(DoubleWritable.class,true);
	}
	@SuppressWarnings("rawtypes")
	public int compare( WritableComparable w1, WritableComparable w2){
		DoubleWritable key1 = (DoubleWritable) w1;
		DoubleWritable key2 = (DoubleWritable) w2;
		return -1*key1.compareTo(key2); //reverse the natural order(ASC)
		
	}
}
