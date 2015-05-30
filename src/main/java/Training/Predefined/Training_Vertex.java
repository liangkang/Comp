package Training.Predefined;

import Training.Customized.Training_Worker;
import com.aliyun.odps.graph.ComputeContext;
import com.aliyun.odps.graph.Vertex;
import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.WritableRecord;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class Training_Vertex extends
		Vertex<Text, Training_VertexValue, Text, Text> {
	@Override
	public void compute(
			ComputeContext<Text, Training_VertexValue, Text, Text> context,
			Iterable<Text> values) throws IOException {
		Training_VertexValue value = this.getValue();
		Text id = this.getId();

		if(context.getSuperstep()%2==0) {
			if (value.map == null) {
				List<String> auxiliary_data = new LinkedList<String>();
				Iterable<WritableRecord> auxi_table = context.readResourceTable(context.getConfiguration().get("auxi_table"));

				for (WritableRecord val : auxi_table) {
					auxiliary_data.add(val.get(0).toString());
				}


				value.map = new HashMap<String, Integer>();
				Iterable<WritableRecord> map_table = context.readResourceTable(context.getConfiguration().get("map_table"));

				for (WritableRecord val : map_table) {
					value.map.put(val.get(0).toString(), Integer.parseInt(val.get(1).toString()));
				}

				value.worker = new Training_Worker(value.data_list, value.map);
				value.worker.auxiliary_data = auxiliary_data;
			}




			if(context.getSuperstep()==0)
				value.worker.string_from_master = "";
			else
				for (Text msg : values)
					value.worker.string_from_master=msg.toString();
			String result = value.worker.run();
			context.sendMessage(new Text("0"),new Text(result));
		}
		if(id.toString().equals("0")&&context.getSuperstep()%2==1){
			value.master.auxiliary_data = value.worker.auxiliary_data;
			List<String> list = new LinkedList<String>();
			for (Text msg : values) {
				list.add(msg.toString());
			}
			value.master.string_from_workers = list;
			value.master.run();
			if((context.getSuperstep()+1)>= context.getMaxIteration()){
				String result = value.master.w_old;
				int lower = 0;
				int upper = 0;
				while(lower<=result.length()){
					upper = lower + 10000;
					if(upper>=result.length()) {
						upper = result.length();
						context.write(new Text(result.substring(lower,upper)));
						break;
					}else
						context.write(new Text(result.substring(lower,upper)));
					lower = upper;
				}
			}

			for(int i=0;i<list.size();i++)
				context.sendMessage(new Text(String.valueOf(i)),new Text(value.master.w_old));
		}

	}
}
