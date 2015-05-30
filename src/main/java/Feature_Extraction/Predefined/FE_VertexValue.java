package Feature_Extraction.Predefined;

import com.aliyun.odps.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FE_VertexValue implements Writable {

	public List<FE_Data_Raw> recordList = null;
	public List<FE_Data_Processed> data =null;


	public void readFields(DataInput in) throws IOException {
		int num = in.readInt();
		recordList = new LinkedList<FE_Data_Raw>();
		for(int i=0;i<num;i++){
			FE_Data_Raw d = new FE_Data_Raw();
			d.readFields(in);
			recordList.add(d);
		}

	}

	public void write(DataOutput out) throws IOException {

		int num = recordList.size();
		out.writeInt(num);
		for(int i=0;i<num;i++){
			FE_Data_Raw d = recordList.get(i);
			d.write(out);
		}
	}


}
