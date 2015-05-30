package Feature_Extraction.Predefined;

import com.aliyun.odps.graph.Vertex;
import com.aliyun.odps.graph.VertexChanges;
import com.aliyun.odps.graph.VertexResolver;
import com.aliyun.odps.io.NullWritable;
import com.aliyun.odps.io.Text;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FE_VertexResolver extends
		VertexResolver<Text, FE_VertexValue, NullWritable, NullWritable> {

	@Override
	public Vertex<Text, FE_VertexValue, NullWritable, NullWritable> resolve(
			Text vertexId,
			Vertex<Text, FE_VertexValue, NullWritable, NullWritable> vertex,
			VertexChanges<Text, FE_VertexValue, NullWritable, NullWritable> vertexChanges,
			boolean hasMessages) throws IOException {
		if (vertexChanges.getAddedVertexList() != null
				&& vertexChanges.getAddedVertexList().size() > 0) {
			int Size = vertexChanges.getAddedVertexList().size();
			FE_VertexValue value = new FE_VertexValue();
			value.recordList = new LinkedList<FE_Data_Raw>();
			if (vertex == null) {
				vertex = new FE_Vertex();
				vertex.setId(vertexId);
			}

			for (int i = 0; i < Size; i++) {

				List<FE_Data_Raw> recordList = (vertexChanges.getAddedVertexList()
						.get(i).getValue()).recordList;
				// combine data from conflicted vertices
				for (int j = 0; j < recordList.size(); j++) {
					value.recordList.add(recordList.get(j));
				}
			}
			vertex.setValue(value);
			System.out.println("ID"+ vertexId+"  "+value.recordList.size());
		}
		return vertex;
	}
}
