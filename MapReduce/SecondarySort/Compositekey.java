package secondarySort;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Compositekey implements WritableComparable<Compositekey>{
	String state;
	String city;
	float total;
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(state);
		out.writeUTF(city);
		out.writeFloat(total);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		state=in.readUTF();
		city=in.readUTF();
		total=in.readFloat();
	}

	@Override
	public int compareTo(Compositekey ck) {
		int cmpState = this.state.toLowerCase().compareTo(ck.state.toLowerCase());
		if(cmpState!=0){
			return cmpState;
		}else{
			int cmpCity = this.city.toLowerCase().compareTo(ck.city.toLowerCase());
			if(cmpCity!=0){
				return cmpCity;
			}else{
					return Float.compare(this.total, ck.total);
				}
			}
		}
	public void set(String state,String city,float total){
		this.state = (state==null)?"":state; //if state==null then "" else state
		this.city = (city==null)?"":city;
		this.total=total;
	}
}
