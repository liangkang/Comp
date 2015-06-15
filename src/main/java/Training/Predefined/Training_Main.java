package Training.Predefined;


import Feature_Extraction.Predefined.FE_Partitioner;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.graph.GraphJob;
import org.apache.commons.cli.*;

import java.io.IOException;


public class Training_Main {

	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException,
			ParseException {

		GraphJob job = new GraphJob();
		job.setGraphLoaderClass(Training_Loader.class);
		job.setVertexClass(Training_Vertex.class);
		job.setPartitionerClass(FE_Partitioner.class);
		job.setLoadingVertexResolverClass(Training_VertexResolver.class);
		job.setMaxIteration(Integer.parseInt(args[6]));
		job.setNumWorkers(Integer.parseInt(args[0]));
		job.set("odps.graph.load.checkpoint.limit.time", "60000000");
		job.set("odps.graph.worker.cpu","300");
		job.set("odps.graph.checkpoint.superstep.frequency","0");
		job.set("odps.graph.job.priority","0");
		job.setInt("number_of_vertices", Integer.parseInt(args[1]));
		TableInfo ti = new TableInfo();
		ti.setTableName(args[2]);
		job.addInput(ti);
		job.set("map_table",args[3]);
		job.set("auxi_table",args[4]);
		ti = new TableInfo();
		ti.setTableName(args[5]);
		job.addOutput(ti);
		job.run();

	}


}
