package Testing.Customized;

import Feature_Extraction.Predefined.FE_Data_Processed;
import Testing.Predefined.Testing_Data;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.sparse.SparseVector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yice.zwl on 2015/5/7.
 */
public class Testing_Predictor {

    public static DenseVector str2vec(String input_str){
        String []strs = input_str.split(",");
        DenseVector vec = new DenseVector(strs.length);
        //System.out.println(strs.length);
        for(int i=0;i<strs.length;i++)
            vec.set(i,Double.parseDouble(strs[i]));

        return vec;
    }
    
    public static double sigmoid(double x) {
    	double a = x;
    	if (x>19)
    		a = 19;
    	if (x<-19)
    		a = -19;
		return 1.0 / (1 + Math.exp(a)); 
	}

    public static List<Testing_Data> predict(List<String> auxi_data,List<FE_Data_Processed> data_list,HashMap<String,Integer> feature_map,String model){
        List<Testing_Data> prediction_list = new LinkedList<Testing_Data>();
        DenseVector vec = str2vec(model);

        for(int i=0;i<data_list.size();i++){
            FE_Data_Processed d = data_list.get(i);
            String [] strs = d.features.split(",");
            SparseVector sv = new SparseVector(feature_map.size(),strs.length);
            for(String str:strs){
                String[] strs2 = str.split(":");
                int j = feature_map.get(strs2[0]);
                sv.set(j, Double.parseDouble(strs2[1]));
            }
            prediction_list.add(new Testing_Data(d.user_id, d.merchant_id, -sv.dot(vec)));
        }

        return prediction_list;
    }
}
