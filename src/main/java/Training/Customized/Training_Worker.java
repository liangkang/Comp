package Training.Customized;

import Feature_Extraction.Predefined.FE_Data_Processed;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yice.zwl on 2015/5/20.
 */
public class Training_Worker {
    public List<String> auxiliary_data;
    public FlexCompRowMatrix X=null;
    public DenseVector y = null;
    public String string_from_master;
    public int dim;
    public int num;
    public double lambda;

    //reformat the local data to assure global consistent
    public Training_Worker(List<FE_Data_Processed> data_list, HashMap<String, Integer> featureMap){
        lambda = 1e-3;
        dim = featureMap.size();
        num = data_list.size();
        string_from_master="";

        X = new FlexCompRowMatrix(num,dim);
        y = new DenseVector(num);


        for(int i=0;i<num;i++){
            FE_Data_Processed d = data_list.get(i);

            if(d.label.equals("1"))
                y.set(i,1);
            else
                y.set(i,-1);
            String [] strs = d.features.split(",");
            SparseVector sv = new SparseVector(dim,strs.length);
            for(String str:strs){
                String[] strs2 = str.split(":");
                int j = featureMap.get(strs2[0]);
                sv.set(j, Double.parseDouble(strs2[1]));
            }
            X.setRow(i,sv);
        }
    }

    public DenseVector str2vec(String input_str){
        String []strs = input_str.split(",");
        DenseVector vec = new DenseVector(strs.length);
        for(int i=0;i<strs.length;i++)
            vec.set(i,Double.parseDouble(strs[i]));

        return vec;
    }

    public  String vec2str(DenseVector vec){
        String str = "";
        double t=0;
        for(int i=0;i<vec.size()-1;i++) {
            t = vec.get(i);
            str += String.valueOf(t) + ",";
        }
        t = vec.get(vec.size()-1);
        str+=String.valueOf(t);
        return str;
    }

    public  String compute_gradient_LR(DenseVector w) {

        double t, a;
        double l;

        int num = X.numRows();

        SparseVector sv;
        DenseVector g = new DenseVector(X.numColumns());
        g.zero();
        double obj = 0;
        for (int i = 0; i < num; i++) {
            sv = X.getRow(i);
            l = y.get(i);
            t = Math.exp(-l * sv.dot(w));
            a = t / (1 + t);
            g.add(-l * a, sv);
            obj += Math.log(1 + t);
        }


        return vec2str(g)+":"+String.valueOf(obj);
    }

    public String GradientDescent_worker(){

        DenseVector w;
        if(!string_from_master.equals(""))
            w = str2vec(string_from_master);
        else
            w = new DenseVector(dim);

        return compute_gradient_LR(w);
    }



    public  String run(){
        //convert the string to matrix at the first iteration
        return GradientDescent_worker();
    }
}
