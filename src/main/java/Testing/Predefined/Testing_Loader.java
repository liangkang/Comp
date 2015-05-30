package Testing.Predefined;

import Feature_Extraction.Predefined.FE_Data_Processed;
import Training.Predefined.Training_VertexValue;
import com.aliyun.odps.conf.Configuration;
import com.aliyun.odps.graph.GraphLoader;
import com.aliyun.odps.graph.MutationContext;
import com.aliyun.odps.io.LongWritable;
import com.aliyun.odps.io.NullWritable;
import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.WritableRecord;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class Testing_Loader extends
		GraphLoader<Text, Training_VertexValue, NullWritable, NullWritable> {

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
			MutationContext<Text, Training_VertexValue,NullWritable, NullWritable> context)
			throws IOException {

		Testing_Vertex vertex = new Testing_Vertex();
		Training_VertexValue value = new Training_VertexValue();
		FE_Data_Processed data = new FE_Data_Processed();

//		System.out.println(count);
		data.user_id 	 = record.get("user_id").toString();
		data.merchant_id = record.get("merchant_id").toString();


		if(record.get("feature")!=null)
			data.features	 = record.get("feature").toString();
		else
			data.features = "-999";
		if(record.get("label")!= null) {
			data.label = record.get("label").toString();
		}
		else
			data.label	=	"-999";

		Random r = new Random();
		Integer vID = Math.abs(r.nextInt()%context.getConfiguration().getInt("number_of_vertices", 10));
		vertex.setId(new Text(vID.toString()));


		if(!data.features.equals("-999")) {
			value.data_list = new LinkedList<FE_Data_Processed>();
			value.data_list.add(data);
			vertex.setValue(value);
			context.addVertexRequest(vertex);
		}

	}
}
