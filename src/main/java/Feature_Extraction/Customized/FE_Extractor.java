package Feature_Extraction.Customized;

import Feature_Extraction.Predefined.FE_Data_Processed;
import Feature_Extraction.Predefined.FE_Data_Raw;

import java.util.*;

/**
 * Created by yice.zwl on 2015/5/7.
 */
class userStat{
	// 用户特征统计类
	String user;
	HashMap<String,Integer> map_u_c;
	HashMap<String,HashSet<String>> map_u_m;
	HashMap<String,HashSet<Integer>> map_u_d;
	HashMap<String,Integer> map_all_c;
	HashMap<String,HashSet<String>> map_all_m;
	HashMap<String,HashSet<String>> map_all_m2;
	HashMap<String,HashSet<Date>> map_all_d;
	
	public  userStat(String user) {
		this.user = user;
	}
}
public class FE_Extractor {
    //perform feature extraction in this class
    //you need to implement mapping() and reformat(), respectively
    //reformat() convert data of format 2 to features you need
    //mapping() returns feature mapping (i.e. feature name and its corresponding dimension index)
	
	
    public static HashMap<String,Integer> mapping()
    {
        HashMap<String,Integer> map = new HashMap<String, Integer>();
        //the dimensions from 0 to 4995 is the indicator of merchant
        for(int i=0;i<4995;i++)
            map.put(Integer.toString(i+1),i);
        int count = 4995;
        //the dimension starting from 4995 is the behavior on top 5 similar merchants
        //um feature index
        for(int rank=0;rank<6;rank++)
            for(int month=5;month<=11;month++)
                for(int action=0;action<=3;action++)
                	for(int stat_type=0;stat_type<=5;stat_type++)
                	{
                		map.put(rank+"|"+month+"|"+action+"|"+stat_type,count);
                		count++;
                	}
        for(int rank=0;rank<6;rank++)
        	for(int action=0;action<=3;action++)
        		for(int stat_type=0;stat_type<=6;stat_type++)
        		{
        			map.put(rank+"|"+"0"+"|"+action+"|"+stat_type, count);
        			count++;
        		}
        //user feature index
        for(int month=5;month<=11;month++)
        	for(int action=0;action<=3;action++)
        		for(int stat_type=0;stat_type<=4;stat_type++){
        			map.put("u"+month+"|"+action+"|"+stat_type,count);
        			count++;
        		}
        for(int action=0;action<=3;action++)
    		for(int stat_type=0;stat_type<=5;stat_type++)
    		{
    			map.put("u"+"0"+"|"+action+"|"+stat_type, count);
    			count++;
    		}
        for(int action=0;action<=3;action++)
    		for(int stat_type=7;stat_type<=8;stat_type++)
    		{
    			map.put("u"+"0"+"|"+action+"|"+stat_type, count);
    			count++;
    		}
        //用户购买可能性feature index
        map.put("u"+"0"+"|"+"0"+"|"+"6", count);
		count++;
		map.put("u"+"0"+"|"+"1"+"|"+"6", count);
		count++;
		map.put("u"+"0"+"|"+"3"+"|"+"6", count);
		count++;
		//用户age&gender index
        for (int age=0;age<=8;age++){
        	map.put("u|age|"+age, count);
        	count++;
        }
        for (int gender=0;gender<=2;gender++){
        	map.put("u|gender|"+gender, count);
        	count++;
        }
        //merchant特征
        for (int action=0;action<=3;action++)
        	for(int stat_type=0;stat_type<=6;stat_type++){
        		map.put("m"+"0"+"|"+action+"|"+stat_type, count);
        		count++;
        	}
        map.put("m"+"0"+"|"+"7", count);
        count++;
        map.put("m"+"0"+"|"+"8", count);
        count++;
        for (int month=5;month<=11;month++)
        	for (int action=0;action<=3;action++)
        		for (int stat_type=0;stat_type<=5;stat_type++){
        			map.put("m"+month+"|"+action+"|"+stat_type, count);
        			count++;
        		}
        return map;
    }

    // um特征提取
    public static String activity_log2feature(int rank,String merchant_id, String activitiy_log){
        String feature="";
        String [] strs = activitiy_log.split("#");
        Date last_day = new Date(2014,11,12);
        // month|act: act count
        HashMap<String,Integer> map = new HashMap<String, Integer>();
        // month|act: day set
        HashMap<String,HashSet<Integer>> map_d = new HashMap<String, HashSet<Integer>>();
        // month|act: item set
        HashMap<String,HashSet<String>> map_i = new HashMap<String, HashSet<String>>();
        // ALLtime|act: act count
        HashMap<String,Integer> map_all_c = new HashMap<String, Integer>();
        // ALLtime|act: act date
        HashMap<String,HashSet<Date>> map_all_dat = new HashMap<String, HashSet<Date>>();
        // ALLtime|act: item set
        HashMap<String,HashSet<String>> map_all_i = new HashMap<String, HashSet<String>>();
        for(int i=0;i<strs.length;i++){
            String []strs2 = strs[i].split(":");
            if(strs2.length==5) {
                int month = Integer.parseInt(strs2[3].substring(0, 2));
                int day = Integer.parseInt(strs2[3].substring(2,4));
                if (month==11&&day>11)
                	day = 11;
                String item = strs2[0];
                // 统计每个月|行为 数量
                if (map.containsKey(month + "|" + strs2[4])) {
                    map.put(month + "|" + strs2[4], map.get(month + "|" + strs2[4]) + 1);
                } else {
                    map.put(month + "|" + strs2[4], 1);
                }
                // 统计每个月|行为 天set
                if (map_d.containsKey(month + "|" +strs2[4])){
                	HashSet<Integer> set_d = map_d.get(month + "|" +strs2[4]);
                	set_d.add(day);
                	map_d.put(month + "|" +strs2[4], set_d);
                } else{
                	HashSet<Integer> set_d = new HashSet<Integer>();
                	set_d.add(day);
                	map_d.put(month + "|" +strs2[4], set_d);
                }
                // 统计每个月|行为 产品set
                if (map_i.containsKey(month + "|" + strs2[4])){
                	HashSet<String> set_i = map_i.get(month + "|" +strs2[4]);
                	set_i.add(item);
                	map_i.put(month + "|" + strs2[4], set_i);
                } else{
                	HashSet<String> set_i = new HashSet<String>();
                	set_i.add(item);
                	map_i.put(month + "|" + strs2[4], set_i);
                }
                // 统计整个时间段|行为 数量
                if (map_all_c.containsKey("0" + "|" + strs2[4])) {
                	map_all_c.put("0" + "|" + strs2[4], map_all_c.get("0" + "|" + strs2[4]) + 1);
                } else {
                	map_all_c.put("0" + "|" + strs2[4], 1);
                }
                // 统计整个时间段|行为 产品set
                if (map_all_i.containsKey("0" + "|" + strs2[4])){
                	HashSet<String> set_i = map_all_i.get("0" + "|" +strs2[4]);
                	set_i.add(item);
                	map_all_i.put("0" + "|" + strs2[4], set_i);
                } else{
                	HashSet<String> set_i = new HashSet<String>();
                	set_i.add(item);
                	map_all_i.put("0" + "|" + strs2[4], set_i);
                }
                // 统计整个时间段|行为 date
                if (map_all_dat.containsKey("0" + "|" + strs2[4])){
                	HashSet<Date> set_dat = map_all_dat.get("0" + "|" + strs2[4]);
                	Date dat = new Date(2014,month,day);
                	set_dat.add(dat);
                	map_all_dat.put("0" + "|" +strs2[4],set_dat);
                } else{
                	HashSet<Date> set_dat = new HashSet<Date>();
                	Date dat = new Date(2014,month,day);
                	set_dat.add(dat);
                	map_all_dat.put("0" + "|" +strs2[4],set_dat);
                }
            }
        }
        for(Map.Entry<String,Integer> entry: map_all_c.entrySet()){
        	// 整个时间段行为数
            feature += rank+"|"+entry.getKey()+"|0"+":"+Math.log((double)entry.getValue()+1)+",";
        }
        for(Map.Entry<String,HashSet<Date>> entry: map_all_dat.entrySet()){
        	// 整个时间段行为天数
            feature += rank+"|"+entry.getKey()+"|1"+":"+Math.log((double)entry.getValue().size()+1)+",";
            // 整个时间段行为数/行为天数
            feature += rank+"|"+entry.getKey()+"|2"+":"+map_all_c.get(entry.getKey())/(double)entry.getValue().size()+",";
        }
        for(Map.Entry<String, HashSet<String>> entry: map_all_i.entrySet()){
        	// 整个时间段行为产品数
        	feature += rank+"|"+entry.getKey()+"|3"+":"+Math.log((double)entry.getValue().size()+1)+",";
        	// 整个时间段行为产品数/行为数
        	feature += rank+"|"+entry.getKey()+"|4"+":"+(double)entry.getValue().size()/map_all_c.get(entry.getKey())+",";
        }
        for(Map.Entry<String, HashSet<Date>> entry: map_all_dat.entrySet()){
        	// 整个时间段最近行为时间差倒数
        	feature += rank+"|"+entry.getKey()+"|5"+":"+1/((double)(last_day.getTime()/86400000-Collections.max(entry.getValue()).getTime()/86400000))+",";
        	// 整个时间段最远行为时间差倒数
        	feature += rank+"|"+entry.getKey()+"|6"+":"+1/((double)(last_day.getTime()/86400000-Collections.max(entry.getValue()).getTime()/86400000))+",";
        }
        for(Map.Entry<String,Integer> entry: map.entrySet()){
        	// 行为数
            feature += rank+"|"+entry.getKey()+"|0"+":"+Math.log((double)entry.getValue()+1)+",";
            // 行为数占比
            String [] strsk = entry.getKey().split("\\|");
            
            feature += rank+"|"+entry.getKey()+"|5"+":"+(double)entry.getValue()/(double)map_all_c.get("0"+"|"+strsk[1])+",";
        }
        for(Map.Entry<String,HashSet<Integer>> entry: map_d.entrySet()){
        	// 行为天数
            feature += rank+"|"+entry.getKey()+"|1"+":"+Math.log((double)entry.getValue().size()+1)+",";
            // 行为数/行为天数
            feature += rank+"|"+entry.getKey()+"|2"+":"+map.get(entry.getKey())/(double)entry.getValue().size()+",";
        }
        for(Map.Entry<String, HashSet<String>> entry: map_i.entrySet()){
        	// 行为产品数
        	feature += rank+"|"+entry.getKey()+"|3"+":"+Math.log((double)entry.getValue().size()+1)+",";
        	// 行为产品数/行为数
        	feature += rank+"|"+entry.getKey()+"|4"+":"+(double)entry.getValue().size()/map.get(entry.getKey())+",";
        }
        
        feature += merchant_id+":"+"1";
        
        
        return feature;
    }
    
    // 用户特征提取
    public static HashMap<String,String> extractUserFeature(List<FE_Data_Raw> data_raw_list) {
		int num = data_raw_list.size();
		Date last_day = new Date(2014,11,12);
		HashMap<String,userStat> map_user = new HashMap<String, userStat>();
		HashMap<String,String> user_feature_map = new HashMap<String, String>();
		FE_Data_Raw d;
		userStat us;
		for(int i=0;i<num;i++){
			d = data_raw_list.get(i);
			if (map_user.containsKey(d.user_id)){
				us = map_user.get(d.user_id);
			} else{
				us = new userStat(d.user_id);
				us.map_all_c = new HashMap<String, Integer>();
				us.map_all_d = new HashMap<String, HashSet<Date>>();
				us.map_all_m = new HashMap<String, HashSet<String>>();
				us.map_u_c = new HashMap<String, Integer>();
				us.map_u_d = new HashMap<String, HashSet<Integer>>();
				us.map_u_m = new HashMap<String, HashSet<String>>();
				us.map_all_m2 = new HashMap<String, HashSet<String>>();
				//map_user.put(d.user_id, us);
			}
			String [] strs = d.activity_log.split("#");
			for(int k=0;k<strs.length;k++){
	            String []strs2 = strs[k].split(":");
	            if (strs2.length==5){
	            	int month = Integer.parseInt(strs2[3].substring(0, 2));
	                int day = Integer.parseInt(strs2[3].substring(2,4));
	                if (month==11&&day>11)
	                	day = 11;
	                //String item = strs2[0];
	                // 用户整个时间段|行为 计数
	                if(us.map_all_c.containsKey("0"+"|"+strs2[4])){
	                	us.map_all_c.put("0"+"|"+strs2[4], us.map_all_c.get("0"+"|"+strs2[4])+1);
	                } else{
	                	us.map_all_c.put("0"+"|"+strs2[4], 1);
	                }
	                // 用户整个时间段|行为 merchant set
	                if(us.map_all_m.containsKey("0"+"|"+strs2[4])){
	                	HashSet<String> set_m = us.map_all_m.get("0"+"|"+strs2[4]);
//	                	if(set_m.contains(d.merchant_id)){
//	                		if(us.map_all_m2.containsKey("0"+"|"+strs2[4])){
//	                			HashSet<String> set_m2 = us.map_all_m2.get("0"+"|"+strs2[4]);
//	                			set_m2.add(d.merchant_id);
//	                			us.map_all_m2.put("0"+"|"+strs2[4], set_m2);
//	                		}else{
//	                			HashSet<String> set_m2 = new HashSet<String>();
//	                			set_m2.add(d.merchant_id);
//	                			us.map_all_m2.put("0"+"|"+strs2[4], set_m2);
//	                		}
//	                	}
	                	set_m.add(d.merchant_id);
	                	us.map_all_m.put("0"+"|"+strs2[4], set_m);
	                } else{
	                	HashSet<String> set_m = new HashSet<String>();
	                	set_m.add(d.merchant_id);
	                	us.map_all_m.put("0"+"|"+strs2[4], set_m);
	                }
	                // 用户整个时间段|行为 merchant+month+day
	                if(us.map_all_m2.containsKey("0"+"|"+strs2[4])){
	                	HashSet<String> set_m2 = us.map_all_m2.get("0"+"|"+strs2[4]);
	                	set_m2.add(month+day+","+d.merchant_id);
	                	us.map_all_m2.put("0"+"|"+strs2[4], set_m2);
	                } else{
	                	HashSet<String> set_m2 = new HashSet<String>();
	                	set_m2.add(month+day+","+d.merchant_id);
	                	us.map_all_m2.put("0"+"|"+strs2[4], set_m2);
	                }
	                // 用户整个时间段|行为 date set
	                if(us.map_all_d.containsKey("0"+"|"+strs2[4])){
	                	HashSet<Date> set_d = us.map_all_d.get("0"+"|"+strs2[4]);
	                	set_d.add(new Date(2014,month,day));
	                	us.map_all_d.put("0"+"|"+strs2[4], set_d);
	                } else{
	                	HashSet<Date> set_d = new HashSet<Date>();
	                	set_d.add(new Date(2014,month,day));
	                	us.map_all_d.put("0"+"|"+strs2[4], set_d);
	                }
	                // 用户每个月|行为 计数
	                if(us.map_u_c.containsKey(month+"|"+strs2[4])){
	                	us.map_u_c.put(month+"|"+strs2[4], us.map_u_c.get(month+"|"+strs2[4])+1);
	                } else{
	                	us.map_u_c.put(month+"|"+strs2[4], 1);
	                }
	                // 用户每个月|行为 merchant set
	                if(us.map_u_m.containsKey(month+"|"+strs2[4])){
	                	HashSet<String> set_m = us.map_u_m.get(month+"|"+strs2[4]);
	                	set_m.add(d.merchant_id);
	                	us.map_u_m.put(month+"|"+strs2[4], set_m);
	                } else{
	                	HashSet<String> set_m = new HashSet<String>();
	                	set_m.add(d.merchant_id);
	                	us.map_u_m.put(month+"|"+strs2[4], set_m);
	                }
	                // 用户每个月|行为 date set
	                if(us.map_u_d.containsKey(month+"|"+strs2[4])){
	                	HashSet<Integer> set_d = us.map_u_d.get(month+"|"+strs2[4]);
	                	set_d.add(day);
	                	us.map_u_d.put(month+"|"+strs2[4], set_d);
	                } else{
	                	HashSet<Integer> set_d = new HashSet<Integer>();
	                	set_d.add(day);
	                	us.map_u_d.put(month+"|"+strs2[4], set_d);
	                }
	            }
			}
			map_user.put(d.user_id, us);
		}
		
		String user;
		userStat us1;
		String feature;
		
		for(Map.Entry<String, userStat> entry: map_user.entrySet()){
			user = entry.getKey();
			us1 = entry.getValue();
			feature = "";
			for(Map.Entry<String,Integer> entry2: us1.map_all_c.entrySet()){
	        	// 整个时间段行为数
	            feature += "u"+entry2.getKey()+"|0"+":"+Math.log((double)entry2.getValue()+1)+",";
	            // 购买可能性：购买数/行为数
	            String [] strsk = entry2.getKey().split("\\|");
	            if (!strsk[1].equals("2")&&us1.map_all_c.containsKey("0|2")){
	            	feature += "u"+entry2.getKey()+"|6"+":"+(double)us1.map_all_c.get("0|2")/(double)entry2.getValue()+",";
	            }
	        }
			for(Map.Entry<String,HashSet<Date>> entry2: us1.map_all_d.entrySet()){
	        	// 整个时间段行为天数
	            feature += "u"+entry2.getKey()+"|1"+":"+Math.log((double)entry2.getValue().size()+1)+",";
	            // 整个时间段行为数/行为天数
	            feature += "u"+entry2.getKey()+"|2"+":"+us1.map_all_c.get(entry2.getKey())/(double)entry2.getValue().size()+",";
	            // 整个时间段最近时间差倒数
	            feature += "u"+entry2.getKey()+"|4"+":"+1/((double)(last_day.getTime()/86400000-Collections.max(entry2.getValue()).getTime()/86400000))+",";
	            // 整个时间段最远时间差倒数
	            feature += "u"+entry2.getKey()+"|8"+":"+1/(double)(last_day.getTime()/86400000-Collections.min(entry2.getValue()).getTime()/86400000)+",";
	            // 整个时间段最近-最远时间差
	            //feature += "u"+entry2.getKey()+"|9"+":"+1/(double)(Collections.max(entry2.getValue()).getTime()/86400000-Collections.min(entry2.getValue()).getTime()/86400000)+",";
	        }
			for(Map.Entry<String,HashSet<String>> entry2: us1.map_all_m.entrySet()){
	        	// 整个时间段行为m数
	            feature += "u"+entry2.getKey()+"|3"+":"+Math.log((double)entry2.getValue().size()+1)+",";
	            // 整个时间段行为m数/行为数
	            feature += "u"+entry2.getKey()+"|7"+":"+(double)entry2.getValue().size()/(double)us1.map_all_c.get(entry2.getKey())+",";
	        }
			for(Map.Entry<String,HashSet<String>> entry2: us1.map_all_m2.entrySet()){
	        	// 整个时间段重复行为m数
//	            feature += "u"+entry2.getKey()+"|5"+":"+Math.log((double)entry2.getValue().size()+1)+",";
				HashMap<String,Integer> mcount = new HashMap<String, Integer>();
				for(String str : entry2.getValue()){
					String [] str2 = str.split(",");
					if(mcount.containsKey(str2[1])){
						mcount.put(str2[1], mcount.get(str2[1])+1);
					}else{
						mcount.put(str2[1],1);
					}
				}
				int c = 0;
				for(Map.Entry<String, Integer> entry3: mcount.entrySet()){
					if(entry3.getValue()>1)
						c++;
				}
				if(c>0)
					feature += "u"+entry2.getKey()+"|5"+":"+c+",";
	        }
			for(Map.Entry<String,Integer> entry2: us1.map_u_c.entrySet()){
				// 每个月行为数
				feature += "u"+entry2.getKey()+"|0"+":"+Math.log((double)entry2.getValue()+1)+",";
				// 每个月行为数占比
				String [] strsk = entry2.getKey().split("\\|");
				feature += "u"+entry2.getKey()+"|4"+":"+(double)entry2.getValue()/(double)us1.map_all_c.get("0"+"|"+strsk[1])+",";
			}
			for(Map.Entry<String, HashSet<Integer>> entry2: us1.map_u_d.entrySet()){
				// 每个月行为天数
				feature += "u"+entry2.getKey()+"|1"+":"+Math.log((double)entry2.getValue().size()+1)+",";
				// 每个月行为数/行为天数
				feature += "u"+entry2.getKey()+"|2"+":"+us1.map_u_c.get(entry2.getKey())/(double)entry2.getValue().size()+",";
			}
			for(Map.Entry<String,HashSet<String>> entry2: us1.map_u_m.entrySet()){
	        	// 每个月行为m数
	            feature += "u"+entry2.getKey()+"|3"+":"+Math.log((double)entry2.getValue().size()+1)+",";
	        }
			
			if(feature!=null&&!feature.equals("")){
				//System.out.println(feature.length());
				feature = feature.substring(0,feature.length()-2);
			}
			
			user_feature_map.put(user,feature);
		}
		return user_feature_map;
	}
    
    //merchant特征提取
//    public static HashMap<String,String> extractMerchantFeature(List<FE_Data_Raw> data_raw_list){
//    	int num = data_raw_list.size();
//		Date last_day = new Date(2014,11,12);
//		HashMap<String,String> merchant_feature_map = new HashMap<String, String>();
//		
//		return merchant_feature_map;
//    }
    

    //convert the data of format 2 to your desire format,
    //data_raw_list the list of raw data
    //auxiliary_data is any auxiliary data you needed, which is stored in table "auxi_table"
    public static List<FE_Data_Processed> reformat(List<FE_Data_Raw> data_raw_list, List<String> auxilary_data){
        List<FE_Data_Processed> data_list = new LinkedList<FE_Data_Processed>();

        HashMap<String, String> map = new HashMap<String, String>();
        int num = data_raw_list.size();
        FE_Data_Raw d;
        for(int i=0;i<num;i++){
            d = data_raw_list.get(i);
            map.put(d.user_id+"|"+d.merchant_id,d.activity_log);
        }

        //in this example, the auxiliary data is top 5 similar merchants of each merchant
        HashMap<String,List<String>> similarity_merchants = new HashMap<String, List<String>>();
        int num_similar_merchants = 5;
        int n_merchant = 4995;
        for(int i=0;i<n_merchant;i++){
            String [] strs = auxilary_data.get(i).split(",");
            List<String> list =  new ArrayList<String>(num_similar_merchants);

            for (int j = 0; j < 5; j++)
                list.add(j, strs[j + 1]);

            similarity_merchants.put(strs[0],list);
        }
        //load merchant feature
        HashMap<String,String> merchant_feature = new HashMap<String, String>();
        for(int i=n_merchant;i<2*n_merchant;i++){
        	String [] strs = auxilary_data.get(i).split("@");
        	merchant_feature.put(strs[0], strs[1]);
        }
//        System.out.println(merchant_feature.size());
        
        //extract user feature
        HashMap<String,String> uf_map = extractUserFeature(data_raw_list);

        //the feature is defined by behavior log on itself and  top 5 similar merchants
        for(int i=0;i<num;i++){
            d = data_raw_list.get(i);
            if(!d.label.equals("-1")){
                FE_Data_Processed d2 = new FE_Data_Processed();
                d2.user_id = d.user_id;
                d2.merchant_id = d.merchant_id;
                d2.features = activity_log2feature(0,d.merchant_id,d.activity_log);
                d2.label = d.label;
                List<String> list = similarity_merchants.get(d.merchant_id);
                for(int j=0;j<5;j++){
                    if(map.containsKey(d2.user_id+"|"+list.get(j))) {
                        d2.features += "," + activity_log2feature(j+1,list.get(j),map.get(d2.user_id+"|"+list.get(j)));
                    }
                }
                d2.features += "," + uf_map.get(d.user_id);
                // age
                if (d.age_range==null||d.age_range.equals("-999")){
                	d2.features += "," + "u|age|" + "0" + ":" + "1";
                }else{
//                	if(Integer.parseInt(d.age_range)<0||Integer.parseInt(d.age_range)>8){
//                		System.out.println(d.age_range);
//                	}
                	d2.features += "," + "u|age|" + d.age_range + ":" + "1";
                }
                // gender
                if (d.gender==null||d.gender.equals("-999")){
                	d2.features += "," + "u|gender|" + "2" + ":" + "1";
                }else{
                	d2.features += "," + "u|gender|" + d.gender + ":" + "1";
                }
                //add merchant feature
                d2.features += "," + merchant_feature.get(d.merchant_id);
                data_list.add(d2);
            }
        }
        System.out.println(data_list.size());
        return data_list;
    }
}
