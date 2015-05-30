package Feature_Extraction.Predefined;

import Feature_Extraction.Customized.FE_Extractor;
import com.aliyun.odps.graph.ComputeContext;
import com.aliyun.odps.graph.Vertex;
import com.aliyun.odps.io.LongWritable;
import com.aliyun.odps.io.NullWritable;
import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.WritableRecord;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class FE_Vertex extends
		Vertex<Text, FE_VertexValue, NullWritable, NullWritable> {
	@Override
	public void compute(
			ComputeContext<Text, FE_VertexValue, NullWritable, NullWritable> context,
			Iterable<NullWritable> values) throws IOException {

		List<String> auxilary_data = new LinkedList<String>();
		Iterable<WritableRecord> auxi_table = context.readResourceTable(context.getConfiguration().get("auxi_table"));

		for (WritableRecord val : auxi_table) {
			auxilary_data.add(val.get(0).toString());
		}

		FE_VertexValue value = this.getValue();
		List<FE_Data_Processed> data_list = FE_Extractor.reformat(value.recordList, auxilary_data);
		for(int i=0;i<data_list.size();i++){
			FE_Data_Processed d = data_list.get(i);
			context.write("feature",new Text(d.user_id),new Text(d.merchant_id),new Text(d.features),new Text(d.label));
		}
		if(this.getId().equals(new Text("0"))){
			HashMap<String,Integer> map = FE_Extractor.mapping();
			for(Map.Entry<String,Integer> entry:map.entrySet()){
				context.write("map",new Text(entry.getKey()),new LongWritable(entry.getValue()));
			}
		}
	}
}
