package secondarySort;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
/*
 * It will group all sorted partition data by natural key ('state') because we used our NaturalKeyComparator
 * as the grouping comparator. 
 * Compositekey:
 * [state=”AZ“, city=”Phoenix”, total=10.00]
 * [state=”TX“, city=”Dallas“, total=7.00]
 * [state=”TX“, city=”Dallas“, total=5.00]
 * [state=”TX“, city=”Houston”, total=10.00]
 * 
 * Grouping based on "state":
 * reduce(AZ, [value1,value2,...])
 * reduce(TX, [value1,value2,value3,value4,...])
 * */
public class NaturalKeyComparator extends WritableComparator{
	
		public NaturalKeyComparator() {
			super(Compositekey.class, true);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public int compare(WritableComparable wc1, WritableComparable wc2) {

			Compositekey key1 = (Compositekey) wc1;
			Compositekey key2 = (Compositekey) wc2;
			return key1.state.compareTo(key2.state);

		}

}