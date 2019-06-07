package secondarySort;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;
/***********************************************************************************
 * We set this class as below in Driver class
 * job.setPartitionerClass(NaturalKeyPartitioner.class).
 * 
 * **********************************************************************************/
public class NaturalKeyPartitioner extends Partitioner<Compositekey, Text>{

	@Override
	public int getPartition(Compositekey key, Text val, int numPartitions) {
		// TODO Auto-generated method stub
		return Math.abs(key.state.hashCode() & Integer.MAX_VALUE)%numPartitions;
	}

}
