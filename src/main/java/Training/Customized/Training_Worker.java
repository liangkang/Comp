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

//example worker

public class Training_Worker {
    public List<String> auxiliary_data;
    public FlexCompRowMatrix X=null;
    public DenseVector y = null;
    public String string_from_master;
    public int dim;
    public int num;
    
    public FlexCompRowMatrix VW=null;
    
    public final static int factorSize=10;
    
    private static double safeLog(double input)
    {
    	if(input < 1.0e-29)
    		input = 1.0e-29;
    	return Math.log(input);
    }
    
    private static double safeExp(double input)
    {
    	if(input < 1.0e-29)
    		input=1.0e-29;
    	
    	if(input > 1.0e29)
    		input=1.0e29;
    	
    	return Math.exp(input);
    }

    //reformat the local data to assure global consistent
    public Training_Worker(List<FE_Data_Processed> data_list, HashMap<String, Integer> featureMap){

        dim = featureMap.size();
        num = data_list.size();
        string_from_master="";

        X = new FlexCompRowMatrix(num,dim);
        y = new DenseVector(num);


        for(int i=0;i<num;i++){
            FE_Data_Processed d = data_list.get(i);

            if(d.label.equals("1"))
                y.set(i,1.0);
            else
                y.set(i,-1.0);
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
    
    public FlexCompRowMatrix str2matrix(String input_str)
    {
    	String []strs=input_str.split(",");
    	FlexCompRowMatrix matrix=new FlexCompRowMatrix(factorSize+1, strs.length/(factorSize+1) );
    	for (int i = 0; i < matrix.numRows(); i++) {
    		for (int j = 0; j < matrix.numColumns(); j++) {
    			matrix.set(i,j,Double.parseDouble(strs[i*matrix.numColumns()+j]));
    		}
			
		}
    	
    	return matrix;
    }

    String matrix2str(FlexCompRowMatrix matrix)
    {
    	String str ="";
    	double t;
    	for (int i = 0; i < matrix.numRows(); i++) {
			for (int j = 0; j < matrix.numColumns(); j++) {
				String temStr="";
				t=matrix.get(i, j);
				if (i==matrix.numRows()-1 && j==matrix.numColumns()-1) {
					temStr=String.valueOf(t);
				}
				else {
					temStr=String.valueOf(t)+",";
				}
				str+=temStr;
			}
		}
    	
    	return str;
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

    SparseVector multiply(SparseVector a,SparseVector b)
    {

    	SparseVector result=new SparseVector(a.size());
    	result.zero();
    	for (int i :a.getIndex()) {
			result.set(i, a.get(i)*b.get(i));
		}
    	
    	return result;
    }
    
    Double sumSparseVector(SparseVector a)
    {
    	Double su=0.0;
    	for (Double b:a.getData()) {
			su+=b;
		}
    	return su;
    }
    
    public String compute_gradient_FM(FlexCompRowMatrix ws){
    	FlexCompRowMatrix g=new FlexCompRowMatrix(ws.numRows(),ws.numColumns());
    	g.zero();
    	
    	double obj=0.0;
    
    	double l;
    	double t;
		double a;
    	SparseVector sv;
    	for (int i = 0; i < X.numRows(); i++) {
			
    		sv=X.getRow(i);
    		l=y.get(i);
    		
    		
    		

    		DenseVector sums = new DenseVector(factorSize);
    		DenseVector sum_sqaures = new DenseVector(factorSize);
    		sums.zero();
    		sum_sqaures.zero();
    		double mvalue=sv.dot(ws.getRow(factorSize));
    		for (int j = 0; j < factorSize; j++) {
    			SparseVector tem_sums = multiply(sv, ws.getRow(j));
    			SparseVector tem_sum_squares = multiply(tem_sums, tem_sums);
    			Double d_sum=sumSparseVector(tem_sums);
    			Double d_sum_s=sumSparseVector(tem_sum_squares);
    			sums.set(j,d_sum);
    			sum_sqaures.set(j,d_sum_s);
    			mvalue+=(d_sum*d_sum-d_sum_s)*0.5;
    			
			}
    		    		
    		
     		
    		t=safeExp(-l*mvalue);
    		a=t/(1+t);   		
    		
     		   		   		
    		SparseVector temg = g.getRow(factorSize);
    		temg.add(-l*a,sv);
    		g.setRow(factorSize, temg);

    		for (int j = 0; j < factorSize; j++) {
				for (int k:ws.getRow(j).getIndex()) {
					g.set(j, k, g.get(j, k) - l*a *(sums.get(j)-sv.get(k)*ws.getRow(j).get(k))*sv.get(k) );
				}
			}
    		
    		obj+=safeLog(1+t);
		}
    	
    	return matrix2str(g)+":"+String.valueOf(obj);
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
            t = safeExp(-l * sv.dot(w));
            //gradient -(x*y*exp(-w*x*y))/(exp(-w*x*y) + 1)
            a = t / (1 + t);
            g.add(-l * a, sv);
            //error -log(1/(exp(-w*x*y) + 1))
            obj += safeLog(1 + t);
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