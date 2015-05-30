package Testing.Predefined;

import Testing.Customized.Testing_Predictor;
import Training.Predefined.Training_VertexValue;
import com.aliyun.odps.graph.ComputeContext;
import com.aliyun.odps.graph.Vertex;
import com.aliyun.odps.io.DoubleWritable;
import com.aliyun.odps.io.NullWritable;
import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.WritableRecord;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class Testing_Vertex extends
		Vertex<Text, Training_VertexValue, NullWritable, NullWritable> {
	@Override
	public void compute(
			ComputeContext<Text, Training_VertexValue, NullWritable, NullWritable> context,
			Iterable<NullWritable> values) throws IOException {

		Training_VertexValue value = this.getValue();

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		Iterable<WritableRecord> map_table = context.readResourceTable(context.getConfiguration().get("map_table"));

		for (WritableRecord val : map_table) {
			map.put(val.get(0).toString(), Integer.parseInt(val.get(1).toString()));
		}

		String model = "";
		Iterable<WritableRecord> model_table = context.readResourceTable(context.getConfiguration().get("model_table"));
		for (WritableRecord val : model_table) {
			model += val.get(0).toString();
		}

		List<String> auxilary_data = new LinkedList<String>();
		Iterable<WritableRecord> auxi_table = context.readResourceTable(context.getConfiguration().get("auxi_table"));

		for (WritableRecord val : auxi_table) {
			auxilary_data.add(val.get(0).toString());
		}

		System.out.println(value.data_list.size());
		List<Testing_Data> data_list = Testing_Predictor.predict(auxilary_data,value.data_list,map,model);
		for (int i = 0; i < data_list.size(); i++) {
			Testing_Data d = data_list.get(i);
			context.write(new Text(d.user_id), new Text(d.merchant_id), new DoubleWritable(d.value));
		}
	}
}
