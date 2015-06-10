package Training.Customized;

import no.uib.cipr.matrix.DenseVector;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yice.zwl on 2015/5/20.
 */

// example Master

public class Training_Master {

	static enum masterStatuses {
		firstTime, arjimoChecking
	}

	final static int the_m = 10; // lbfgs use m as the size for storage limit
	final static double init_a = 0.01; // the initial step size
	final static double c2 = 0.99; // the parameter for line tracking
	final static double ro = 0.2; // the parameter to shrink step size every
									// check

	public List<String> auxiliary_data = null;
	public String w_old = "";
	public List<String> string_from_workers = null;

	public DenseVector x0, x1, y;
	double t0, t1;

	public DenseVector str2vec(String input_str) {
		String[] strs = input_str.split(",");
		DenseVector vec = new DenseVector(strs.length);
		for (int i = 0; i < strs.length; i++)
			vec.set(i, Double.parseDouble(strs[i]));

		return vec;
	}

	public String vec2str(DenseVector vec) {
		String str = "";
		double t = 0;
		for (int i = 0; i < vec.size() - 1; i++) {
			t = (Math.round(vec.get(i) * 10000000) / 10000000.0);
			str += String.valueOf(t) + ",";
		}
		t = (Math.round(vec.get(vec.size() - 1) * 10000000) / 10000000.0);
		str += String.valueOf(t);
		return str;
	}

	LinkedList<DenseVector> the_y = new LinkedList<DenseVector>();
	LinkedList<DenseVector> the_s = new LinkedList<DenseVector>();

	masterStatuses myStatus = masterStatuses.firstTime;

	double a = 0.0; // the current step size
	double arjimo_m = 0.0; // the parameter to descript p*g
	int arjimo_n = 0; // the times of back tracing check
	double f = 0.0; // the current error: collected from all workders
	double f_current = 0.0;
	DenseVector w_current = null;

	DenseVector p = null;
	DenseVector w = null;
    DenseVector g = null;
	
	DenseVector calculateG(DenseVector temp_g) // the function calculate g from its
											// storage and a new g
	{
		DenseVector temp_p = new DenseVector(temp_g.size());
		temp_p.zero();
		temp_p.add(-1, temp_g);

		int L = the_y.size();
		DenseVector alpha = new DenseVector(L);
		assert (the_y.size() == the_s.size());

		Iterator<DenseVector> rit_y = the_y.descendingIterator();
		Iterator<DenseVector> rit_s = the_s.descendingIterator();
		int i = the_y.size() - 1;
		while (rit_y.hasNext()) {
			DenseVector cur_y = rit_y.next();
			DenseVector cur_s = rit_s.next();
			alpha.set(i, cur_s.dot(temp_p) / cur_s.dot(cur_y));
			temp_p.add(-1.0 * alpha.get(i), cur_y);
			i--;
		}
		if (L > 0) {
			DenseVector cur_y = the_y.getLast();
			DenseVector cur_s = the_s.getLast();
			temp_p.scale(cur_y.dot(cur_s) / cur_y.dot(cur_y));
		}
		assert (the_y.size() == the_s.size());
		Iterator<DenseVector> it_y = the_y.iterator();
		Iterator<DenseVector> it_s = the_s.iterator();

		i = 0;
		while (it_y.hasNext()) {

			DenseVector cur_y = it_y.next();
			DenseVector cur_s = it_s.next();
			double beta = cur_y.dot(temp_p) / cur_s.dot(cur_y);
			temp_p.add(alpha.get(i) - beta, cur_s);
			i++;

		}

		return temp_p;
	}

	public void GradientDescent_master() {
		/*
		 * DenseVector w_new; if(w_old.equals("")) { w_new = new
		 * DenseVector(string_from_workers.get(0).split(",").length);
		 * w_new.zero(); }else w_new = str2vec(w_old); double step_size =
		 * Double.parseDouble(auxiliary_data.get(0)); double obj = 0; for(int
		 * i=0;i<string_from_workers.size();i++) { String [] strs=
		 * string_from_workers.get(i).split(":"); obj +=
		 * Double.parseDouble(strs[1]); w_new.add(-step_size, str2vec(strs[0]));
		 * }
		 * 
		 * System.out.println(obj); w_old = vec2str(w_new);
		 */
		DenseVector temp_w;
		if (w_old.equals("")) {
			temp_w = new DenseVector(
					string_from_workers.get(0).split(",").length);
			temp_w.zero();
		} else
			temp_w = str2vec(w_old);
		int the_dimension = temp_w.size();
		DenseVector temp_g = new DenseVector(the_dimension);
		temp_g.zero();
		double temp_f = 0.0;
		for (int i = 0; i < string_from_workers.size(); i++) {
			String[] strs = string_from_workers.get(i).split(":");
			temp_f += Double.parseDouble(strs[1]);
			temp_g.add(1.0, str2vec(strs[0]));
		}

		if (myStatus == masterStatuses.firstTime) {
			g = new DenseVector(the_dimension);
			g.zero();
			g.add(temp_g);
			p = calculateG(g); // p is the direction of lbfgs
			
			a = init_a;
			w_current = new DenseVector(the_dimension);
			w_current.zero();
			w_current.add(temp_w);
			w_current.add(a, p);
			
			arjimo_m = -p.dot(temp_g);
			f = temp_f;
			w = new DenseVector(the_dimension);
			w.zero();
			w.add(temp_w);
			myStatus=masterStatuses.arjimoChecking;
		}

		if (myStatus == masterStatuses.arjimoChecking) {

			f_current = temp_f;
			if (f_current <= f + c2 * a * arjimo_m) {
				DenseVector s_new = new DenseVector(the_dimension);
				s_new.zero();
				s_new.add(w_current);
				s_new.add(-1, w);
				DenseVector y_new = new DenseVector(the_dimension);
				y_new.zero();
				y_new.add(temp_g);
				y_new.add(-1,g);
				
				the_s.push(s_new);
				the_y.push(y_new);
				
				if(the_y.size()>the_m)
				{
					the_y.pop();
					the_s.pop();
				}
				w.zero();
				w.add(temp_w);
				f=f_current;
				g.zero();
				g.add(temp_g);
				
				p=calculateG(g);
				a=init_a;
				
				w_current.zero();
				w_current.add(temp_w);
				w_current.add(a, p);
				
				arjimo_m = -p.dot(temp_g);
				
			} else {
				a = a * ro;
				w_current.zero();
				w_current.add(temp_w);
				w_current.add(a, p);
			}

		}
		System.out.println(f);
		w_old = vec2str(w_current);
//		System.out.println(w_old);
		
		// System.out.println(current_f);

	}

	public void FISTA_master() {
		String[] strings = auxiliary_data.get(0).split(",");
		double step_size = Double.parseDouble(strings[0]);
		double lambda = Double.parseDouble(strings[1]);

		if (w_old.equals("")) {
			x0 = new DenseVector(string_from_workers.get(0).split(",").length);
			x0.zero();
			x1 = new DenseVector(x0);
			y = new DenseVector(x0);
			t0 = 1;
			t1 = 1;
		}
		double obj = 0;
		x1.set(y);
		for (int i = 0; i < string_from_workers.size(); i++) {
			String[] strs = string_from_workers.get(i).split(":");
			obj += Double.parseDouble(strs[1]);
			x1.add(-step_size, str2vec(strs[0]));
		}
		x1.add(-step_size * lambda, x0);
		obj += lambda / 2 * x0.dot(x0);
		t1 = (1 + Math.sqrt(1 + 4 * t0 * t0)) / 2;
		y.set(1 + (t0 - 1) / t1, x1);
		y.add((1 - t0) / t1, x0);
		w_old = vec2str(x1);
		x0.set(x1);
		t0 = t1;
		System.out.println(obj);

	}

	public void run() {
//		FISTA_master();
		GradientDescent_master();
	}
}
