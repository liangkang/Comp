package Training.Customized;

import no.uib.cipr.matrix.DenseVector;

import java.util.List;

/**
 * Created by yice.zwl on 2015/5/20.
 */
public class Training_Master {


    public List<String> auxiliary_data=null;
    public String w_old="";
    public List<String> string_from_workers=null;

    public DenseVector x0,x1,y;
    double t0,t1;

    public  DenseVector str2vec(String input_str){
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
            t = (Math.round(vec.get(i)*10000000)/10000000.0);
            str += String.valueOf(t) + ",";
        }
        t = (Math.round(vec.get(vec.size()-1)*10000000)/10000000.0);
        str+=String.valueOf(t);
        return str;
    }

    public void GradientDescent_master()
    {

        DenseVector w_new;
        if(w_old.equals("")) {
            w_new = new DenseVector(string_from_workers.get(0).split(",").length);
            w_new.zero();
        }else
            w_new = str2vec(w_old);
        double step_size = Double.parseDouble(auxiliary_data.get(0));
        double obj = 0;
        for(int i=0;i<string_from_workers.size();i++) {
            String [] strs= string_from_workers.get(i).split(":");
            obj += Double.parseDouble(strs[1]);
            w_new.add(-step_size, str2vec(strs[0]));
        }

        System.out.println(obj);
        w_old = vec2str(w_new);
    }

    public void FISTA_master()
    {
        String[] strings = auxiliary_data.get(0).split(",");
        double step_size = Double.parseDouble(strings[0]);
        double lambda = Double.parseDouble(strings[1]);

        if(w_old.equals("")) {
            x0 = new DenseVector(string_from_workers.get(0).split(",").length);
            x0.zero();
            x1 = new DenseVector(x0);
            y = new DenseVector(x0);
            t0=1;
            t1=1;
        }
        double obj = 0;
        x1.set(y);
        for(int i=0;i<string_from_workers.size();i++) {
            String [] strs= string_from_workers.get(i).split(":");
            obj += Double.parseDouble(strs[1]);
            x1.add(-step_size, str2vec(strs[0]));
        }
        x1.add(-step_size*lambda,x0);
        obj+=lambda/2*x0.dot(x0);
        t1 = (1+Math.sqrt(1+4*t0*t0))/2;
        y.set(1+(t0-1)/t1,x1);
        y.add((1 - t0) / t1, x0);
        w_old = vec2str(x1);
        x0.set(x1);
        t0=t1;
        System.out.println(obj);

    }

    public  void run() {
        FISTA_master();
    }
}
