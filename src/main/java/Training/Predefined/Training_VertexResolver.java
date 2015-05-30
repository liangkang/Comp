package Training.Predefined;

import Feature_Extraction.Predefined.FE_Data_Processed;
import com.aliyun.odps.graph.Vertex;
import com.aliyun.odps.graph.VertexChanges;
import com.aliyun.odps.graph.VertexResolver;
import com.aliyun.odps.io.Text;

import java.io.IOException;
import java.util.List;

public class Training_VertexResolver extends
		VertexResolver<Text, Training_VertexValue, Text, Text> {

	@Override
	public Vertex<Text, Training_VertexValue, Text, Text> resolve(
			Text vertexId,
			Vertex<Text, Training_VertexValue, Text,Text> vertex,
			VertexChanges<Text, Training_VertexValue, Text, Text> vertexChanges,
			boolean hasMessages) throws IOException {
		if (vertexChanges.getAddedVertexList() != null
				&& vertexChanges.getAddedVertexList().size() > 0) {
			int Size = vertexChanges.getAddedVertexList().size();
			Training_VertexValue value = new Training_VertexValue();
			if (vertex == null) {
				vertex = new Training_Vertex();
				vertex.setId(vertexId);
			}


			for (int i = 0; i < Size; i++) {

				List<FE_Data_Processed> dataList = (vertexChanges.getAddedVertexList()
						.get(i).getValue()).data_list;
				// combine data from conflicted vertices
				for (FE_Data_Processed d: dataList) {
					value.data_list.add(d);
				}
			}
			vertex.setValue(value);
			System.out.println("ID"+ vertexId+"  "+value.data_list.size());
		}
		return vertex;
	}
}
