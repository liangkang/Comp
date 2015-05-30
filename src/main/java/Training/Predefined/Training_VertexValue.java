package Training.Predefined;

import Feature_Extraction.Predefined.FE_Data_Processed;
import Training.Customized.Training_Master;
import Training.Customized.Training_Worker;
import com.aliyun.odps.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Training_VertexValue implements Writable {
	public List<FE_Data_Processed> data_list=new LinkedList<FE_Data_Processed>();
	public Training_Worker worker ;
	public Training_Master master = new Training_Master();
	public HashMap<String,Integer> map = null;

	public void readFields(DataInput in) throws IOException {
		int num = in.readInt();
		data_list = new LinkedList<FE_Data_Processed>();
		for(int i=0;i<num;i++){
			FE_Data_Processed d = new FE_Data_Processed();
			d.readFields(in);
			data_list.add(d);
		}
	}

	public void write(DataOutput out) throws IOException {
		int num = data_list.size();
		out.writeInt(num);
		for(int i=0;i<num;i++)
			data_list.get(i).write(out);
	}


}
