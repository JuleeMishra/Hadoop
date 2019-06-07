package secondarySort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
/*
 * Compositekey class produce the natural orderdering(Asc). But since we need 3rd field i.e "total" in descending
 * order, we write WritableComparator class.
 * 
 * NOTE: Implementing the Composite Key(Compositekey) and Sort Comparator(FullKeyComparator) is enough to sort on 
 * multiple fields when using only one reducer.
 * */
public class FullKeyComparator extends WritableComparator{
	public FullKeyComparator(){
		super(Compositekey.class,true);
	}
	@SuppressWarnings("rawtypes")
	public int compare( WritableComparable w1, WritableComparable w2){
		Compositekey key1 = (Compositekey)w1;
		Compositekey key2 = (Compositekey)w2;
		int cmpState = key1.state.toLowerCase().compareTo(key2.state.toLowerCase());
		if(cmpState!=0){
			return cmpState;
		}else{
			int cmpCity = key1.city.toLowerCase().compareTo(key2.city.toLowerCase());
			if(cmpCity!=0){
				return cmpCity;
			}else{
				return -1*Float.compare(key1.total, key2.total);
			}
		}
	}
}
