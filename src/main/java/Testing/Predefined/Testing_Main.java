package Testing.Predefined;


import Feature_Extraction.Predefined.FE_Partitioner;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.graph.GraphJob;
import org.apache.commons.cli.ParseException;

import java.io.IOException;


public class Testing_Main {

	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException,
			ParseException {
		GraphJob job = new GraphJob();
		job.setGraphLoaderClass(Testing_Loader.class);
		job.setVertexClass(Testing_Vertex.class);
		job.setPartitionerClass(FE_Partitioner.class);
		job.setLoadingVertexResolverClass(Testing_VertexResolver.class);
		job.setNumWorkers(Integer.parseInt(args[0]));
		job.setInt("number_of_vertices", Integer.parseInt(args[1]));
		job.setMaxIteration(1);
		job.set("odps.graph.load.checkpoint.limit.time", "60000000");
		job.set("odps.graph.worker.cpu","300");
		job.set("odps.graph.checkpoint.superstep.frequency","0");
		job.set("odps.graph.job.priority","0");
		job.set("odps.mapred.local.record.download.limit","10000");
		job.addInput(TableInfo.builder().tableName(args[2]).build());
		job.set("map_table", args[3]);
		job.set("model_table",args[4]);
		job.set("auxi_table",args[5]);
		job.addOutput(TableInfo.builder().tableName(args[6]).build());

		job.run();

	}


}
