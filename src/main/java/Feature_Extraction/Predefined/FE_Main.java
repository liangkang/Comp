package Feature_Extraction.Predefined;


import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.graph.GraphJob;
import org.apache.commons.cli.*;

import java.io.IOException;


public class FE_Main {

	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException,
			ParseException {
		GraphJob job = new GraphJob();
		job.setGraphLoaderClass(FE_Loader.class);
		job.setVertexClass(FE_Vertex.class);
		job.setPartitionerClass(FE_Partitioner.class);
		job.setLoadingVertexResolverClass(FE_VertexResolver.class);
		job.setNumWorkers(Integer.parseInt(args[0]));
		job.setInt("number_of_vertices", Integer.parseInt(args[1]));
		job.set("odps.graph.load.checkpoint.limit.time", "60000000");
		job.set("odps.graph.worker.cpu","300");
		job.set("odps.graph.checkpoint.superstep.frequency","0");
		job.set("odps.graph.job.priority","0");
		job.setMaxIteration(1);
		job.addInput(TableInfo.builder().projectName(args[2]).tableName(args[3]).build());
		job.addOutput(TableInfo.builder().tableName(args[3] + "_feature").label("feature").build());
		job.addOutput(TableInfo.builder().tableName(args[3] + "_map").label("map").build());
		job.set("auxi_table",args[4]);
		job.run();

	}


}
