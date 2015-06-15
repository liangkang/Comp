package Feature_Extraction.Predefined;

import com.aliyun.odps.conf.Configuration;
import com.aliyun.odps.graph.GraphLoader;
import com.aliyun.odps.graph.MutationContext;
import com.aliyun.odps.io.LongWritable;
import com.aliyun.odps.io.NullWritable;
import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.WritableRecord;

import java.io.IOException;
import java.util.LinkedList;

public class FE_Loader extends
		GraphLoader<Text, FE_VertexValue, NullWritable, NullWritable> {

	int workerId = 0;


	@Override
	public void setup(Configuration conf, int workerId, com.aliyun.odps.data.TableInfo tableInfo)
			throws IOException {
		this.workerId = workerId;
	}

	@Override
	public void load(
			LongWritable recordNum,
			WritableRecord record,
			MutationContext<Text, FE_VertexValue, NullWritable, NullWritable> context)
			throws IOException {

		FE_Vertex vertex = new FE_Vertex();
		FE_VertexValue value = new FE_VertexValue();
		FE_Data_Raw data = new FE_Data_Raw();


		data.user_id 	 = record.get("user_id").toString();
		data.merchant_id = record.get("merchant_id").toString();


		if(record.get("age_range")!=null) {
			data.age_range = record.get("age_range").toString();
			try{
				int t_int = Integer.parseInt(data.age_range);
				if(t_int<0 || t_int>8)
					data.age_range = "-999";
			}catch(Exception e){
				data.age_range = "-999";
			}
		}else
			data.age_range = "-999";

		if(record.get("gender")!=null) {
			data.gender = record.get("gender").toString();
			try{
				int t_int = Integer.parseInt(data.gender);
				if(t_int<0 || t_int>2)
					data.gender = "-999";
			}catch(Exception e){
				data.gender = "-999";
			}
		}else
			data.gender = "-999";
		if(record.get("label")!= null) {
			data.label = record.get("label").toString();
			try{
				int t_int = Integer.parseInt(data.label);
				if(t_int<-1 || t_int>1)
					data.label = "-999";
			}catch(Exception e){
				data.label = "-999";
			}
		}
		else
			data.label	=	"-999";
		if(record.get("activity_log")!=null) {
			String log = record.get("activity_log").toString();
			String []strs=log.split("#");
			data.activity_log="";
			for(int j=0;j<strs.length;j++){
				String [] strs2=strs[j].split(":");
				try {
					Integer.parseInt(strs2[0]);
					Integer.parseInt(strs2[1]);
					Integer.parseInt(strs2[2]);
					Integer.parseInt(strs2[3]);
					Integer.parseInt(strs2[4]);
					data.activity_log+=strs[j];
					if(j<strs.length-1)
						data.activity_log+="#";
				}catch(Exception e){

				}
			}

		}else
			data.activity_log = "-999";

		
		Integer vID = Math.abs(Integer.parseInt(data.user_id)%context.getConfiguration().getInt("number_of_vertices",10));
		vertex.setId(new Text(vID.toString()));


		if(!data.activity_log.equals("-999")) {
			value.recordList = new LinkedList<FE_Data_Raw>();
			value.recordList.add(data);
			vertex.setValue(value);
			context.addVertexRequest(vertex);
		}
	}
}
