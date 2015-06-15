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
	HashMap<String,HashSet<String>> map_all_m3;
	HashMap<String,HashSet<Date>> map_all_d;
	HashMap<String,Integer> map_db_c;
	public  userStat(String user) {
		this.user = user;
	}
}

//LR特征体系

public class FE_Extractor {
    //perform feature extraction in this class
    //you need to implement mapping() and reformat(), respectively
    //reformat() convert data of format 2 to features you need
    //mapping() returns feature mapping (i.e. feature name and its corresponding dimension index)
	static int numMerchant = 4995;
	static int numBrand = 8443;
	static int numCat = 1511;

	// old map

    public static HashMap<String,Integer> mapping()
    {
        HashMap<String,Integer> map = new HashMap<String, Integer>();
        //the dimensions from 0 to 4995 is the indicator of merchant
        for(int i=0;i<4995;i++)
            map.put(Integer.toString(i+1),i);
        int count = numMerchant;
        //the dimension starting from 4995 is the behavior on top 5 similar merchants
        for(int i=0;i<numBrand;i++)
        	map.put("bid|"+i, count+i);
        count += numBrand;
        for(int i=0;i<numCat;i++)
        	map.put("cid|"+i, count+i);
        count += numCat;
        //用户购买过的cat,brand交merchant有的cat,brand dummy
//        for(int i=0;i<numBrand;i++)
//        	map.put("umb|"+i, count+i);
//        count += numBrand;
        
//        for(int i=0;i<numCat;i++)
//        	map.put("umc|"+i, count+i);
//        count += numCat;
        
        //UB weight
//        for(int i=0;i<numBrand;i++)
//        	map.put("umBw|"+i, count+i);
//        count += numBrand;
        
        //用户有过行为的mid,brand_id,cat_id dummy
//        for(int i=0;i<numMerchant;i++)
//        	map.put("u|mid|b|"+Integer.toString(i+1), count+i);
//        count += numMerchant;
//        for(int i=0;i<numBrand;i++)
//        	map.put("u|bid|b|"+i, count+i);
//        count += numBrand;
//        for(int i=0;i<numCat;i++)
//        	map.put("u|cid|b|"+i,count+i);
//        count += numCat;
        //um feature index
        for(int rank=0;rank<=5;rank++)
            for(int month=5;month<=11;month++)
                for(int action=0;action<=3;action++)
                	for(int stat_type=0;stat_type<=5;stat_type++)
                	{
                		map.put(rank+"|"+month+"|"+action+"|"+stat_type,count);
                		count++;
                	}
        for(int rank=0;rank<=5;rank++)
        	for(int action=0;action<=3;action++)
        		for(int stat_type=0;stat_type<=7;stat_type++)
        		{
        			map.put(rank+"|"+"0"+"|"+action+"|"+stat_type, count);
        			count++;
        		}
        // sim u,merchant feature
//        for(int rank=1;rank<6;rank++)
//            for(int month=5;month<=11;month++)
//                for(int action=0;action<=3;action++) {
//                    map.put(rank+"|"+month+"|"+action,count);
//                    count++;
//                }
        
        // double11 feature
//        for(int rank=0;rank<=0;rank++)
//        	for(int action=0;action<=3;action++)
//        		for(int st=0;st<=1;st++){
//        			if(action!=2){
//        				map.put(rank+"|db11|"+action+"|"+st, count);
//        				count++;
//        			}
//        		}
        
        //some feature * age * gender
//        for(int rank=0;rank<6;rank++)
//        	for(int age=0;age<=8;age++)
//        		for(int gender=0;gender<=2;gender++)
//        			for(int action=0;action<=3;action++)
//        				for(int stat_type=0;stat_type<=6;stat_type++){
//        					map.put("ag|"+rank+"|"+age+"|"+gender+"|"+"0"+"|"+action+"|"+stat_type, count);
//        					count++;
//        				}
        //log type
//        int [] umalldayLogType = {2,4,5,6,7};
//        int [] ummonthLogType = {2,4,5};
//        for(int rank=0;rank<=5;rank++)
//            for(int month=5;month<=11;month++)
//                for(int action=0;action<=3;action++)
//                	for(int st : ummonthLogType){
//                		map.put(rank+"|"+month+"|"+action+"|"+st+"l",count);
//                		count++;
//                	}
//        for(int rank=0;rank<=5;rank++)
//        	for(int action=0;action<=3;action++)
//        		for(int st : umalldayLogType)
//        		{
//        			map.put(rank+"|"+"0"+"|"+action+"|"+st+"l", count);
//        			count++;
//        		}
        
        
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
    		for(int stat_type=7;stat_type<=15;stat_type++)
    		{
    			map.put("u"+"0"+"|"+action+"|"+stat_type, count);
    			count++;
    		}
        //double11
//        for(int action=0;action<=3;action++)
//        	for(int st=0;st<=1;st++){
//        		map.put("u|db11|"+action+"|"+st, count);
//        		count++;
//        	}
//        int [] userAlldayLogType = {2,4,6,7,8,11,12,13,15};
//        int [] userMonthLogType = {2,4};
//        for(int month=5;month<=11;month++)
//        	for(int action=0;action<=3;action++)
//        		for(int st : userMonthLogType){
//        			map.put("u"+month+"|"+action+"|"+st+"l",count);
//        			count++;
//        		}
//        for(int action=0;action<=3;action++)
//    		for(int st : userAlldayLogType)
//    		{
//    			map.put("u"+"0"+"|"+action+"|"+st+"l", count);
//    			count++;
//    		}
        
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
//        for (int age=1;age<=8;age++)
//        	for(int gender=0;gender<=1;gender++){
//        		map.put("u|a|g|"+age+"|"+gender, count);
//        		count++;
//        	}
        //用户age*gender*mid index
//        for (int age=0;age<=8;age++)
//        	for(int gender=0;gender<=2;gender++)
//        		for(int m=1;m<=4995;m++)
//        		{
//        			map.put("u|a|g|m|"+age+"|"+gender+"|"+m, count);
//        			count++;
//        		}
        
        //用户some feature*age*gender
//        for(int action=0;action<=3;action++)
//        	for(int age=0;age<=8;age++)
//        		for(int gender=0;gender<=2;gender++)
//        			for(int stat_type=0;stat_type<=15;stat_type++){
//        				if(stat_type==6&&action==2){
//        					continue;
//        				}
//        				map.put("u|ag|"+age+"|"+gender+"|"+"0"+"|"+action+"|"+stat_type, count);
//        				count++;
//        			}
        
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
        map.put("m"+"0"+"|"+"0|"+"9", count);
        count++;
        map.put("m"+"0"+"|"+"1|"+"9", count);
        count++;
        map.put("m"+"0"+"|"+"3|"+"9", count);
        count++;
        for (int action=0;action<=3;action++)
        	for (int stat_type=10;stat_type<=15;stat_type++){
        		map.put("m"+"0"+"|"+action+"|"+stat_type, count);
        		count++;
        	}
        
//        for (int st=18;st<=23;st++){
//        	map.put("m0|"+st, count);
//        	count++;
//        }
//        map.put("mbn", count);
//        count++;
        
        return map;
    }

 
    
    public static String dummyCoding4Count(int x, String key, int minL, int maxR) {
		String feat = "";
		for(int i=minL;i<=maxR;i++){
			if(x>=i)
				feat += key + i + ":" + "1" + ","; 
		}
		return feat;
	}
    

    // um特征提取
    public static String activity_log2feature(int rank,String merchant_id, String age, String gender,String activitiy_log, HashMap<String,String> brandid2inx, HashMap<String,String> catid2inx){
        String feature="";
        String [] strs = activitiy_log.split("#");
        if(age.equals("-999"))
        	age = "0";
        if(gender.equals("-999"))
        	gender = "2";
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
        // double11|act: count
        HashMap<String,Integer> map_db_c = new HashMap<String, Integer>();
        for(int i=0;i<strs.length;i++){
            String []strs2 = strs[i].split(":");
            if(strs2.length==5) {
                int month = Integer.parseInt(strs2[3].substring(0, 2));
                int day = Integer.parseInt(strs2[3].substring(2,4));
                String brand = strs2[2];
                String cat = strs2[1];
                if(strs2[4].equals("2")){
                	feature += "bid"+"|"+brandid2inx.get(brand)+":"+1+",";
                	feature += "cid"+"|"+catid2inx.get(cat)+":"+1+",";
                }
                if (month==11&&day>11)
                	day = 11;
                String item = strs2[0];
                // 双11当天|行为 数量
                if (month==11&&day==11){
                	if (map_db_c.containsKey("db11|"+strs2[4])){
                		map_db_c.put("db11|"+strs2[4], map_db_c.get("db11|"+strs2[4])+1);
                	} else{
                		map_db_c.put("db11|"+strs2[4], 1);
                	}
                }
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
            // *age*gender
//            feature += "ag|"+rank+"|"+age+"|"+gender+"|"+entry.getKey()+"|0"+":"+Math.log((double)entry.getValue()+1)+",";
        }
        for(Map.Entry<String,HashSet<Date>> entry: map_all_dat.entrySet()){
        	// 整个时间段行为天数
            feature += rank+"|"+entry.getKey()+"|1"+":"+Math.log((double)entry.getValue().size()+1)+",";
            // *age*gender
//            feature += "ag|"+rank+"|"+age+"|"+gender+"|"+entry.getKey()+"|1"+":"+Math.log((double)entry.getValue().size()+1)+",";
            // 整个时间段行为数/行为天数
            feature += rank+"|"+entry.getKey()+"|2"+":"+map_all_c.get(entry.getKey())/(double)entry.getValue().size()+",";
            // *age*gender
//            feature += "ag|"+rank+"|"+age+"|"+gender+"|"+entry.getKey()+"|2"+":"+map_all_c.get(entry.getKey())/(double)entry.getValue().size()+",";
            // 整个时间段行为数/行为天数log
//            feature += rank+"|"+entry.getKey()+"|2l"+":"+Math.log(map_all_c.get(entry.getKey())/(double)entry.getValue().size()+1)+",";
        }
        for(Map.Entry<String, HashSet<String>> entry: map_all_i.entrySet()){
        	// 整个时间段行为产品数
        	feature += rank+"|"+entry.getKey()+"|3"+":"+Math.log((double)entry.getValue().size()+1)+",";
        	// *age*gender
//        	feature += "ag|"+rank+"|"+age+"|"+gender+"|"+entry.getKey()+"|3"+":"+Math.log((double)entry.getValue().size()+1)+",";
        	// 整个时间段行为产品数/行为数
        	feature += rank+"|"+entry.getKey()+"|4"+":"+(double)entry.getValue().size()/map_all_c.get(entry.getKey())+",";
        	// *age*gender
//        	feature += "ag|"+rank+"|"+age+"|"+gender+"|"+entry.getKey()+"|4"+":"+(double)entry.getValue().size()/map_all_c.get(entry.getKey())+",";
        	// 整个时间段行为产品数/行为数log
//        	feature += rank+"|"+entry.getKey()+"|4l"+":"+Math.log((double)entry.getValue().size()/map_all_c.get(entry.getKey())+1)+",";
        }
        for(Map.Entry<String, HashSet<Date>> entry: map_all_dat.entrySet()){
        	// 整个时间段最近行为时间差倒数
        	feature += rank+"|"+entry.getKey()+"|5"+":"+1/((double)(last_day.getTime()/86400000-Collections.max(entry.getValue()).getTime()/86400000))+",";
        	// *age*gender
//        	feature += "ag|"+rank+"|"+age+"|"+gender+"|"+entry.getKey()+"|5"+":"+1/((double)(last_day.getTime()/86400000-Collections.max(entry.getValue()).getTime()/86400000))+",";
        	// 整个时间段最近行为时间差倒数log
//        	feature += rank+"|"+entry.getKey()+"|5l"+":"+Math.log(1/((double)(last_day.getTime()/86400000-Collections.max(entry.getValue()).getTime()/86400000))+1)+",";
        	// 整个时间段最远行为时间差倒数
        	feature += rank+"|"+entry.getKey()+"|6"+":"+1/((double)(last_day.getTime()/86400000-Collections.min(entry.getValue()).getTime()/86400000))+",";
        	// *age*gender
//        	feature += "ag|"+rank+"|"+age+"|"+gender+"|"+entry.getKey()+"|6"+":"+1/((double)(last_day.getTime()/86400000-Collections.min(entry.getValue()).getTime()/86400000))+",";
        	// 整个时间段最远行为时间差倒数log
//        	feature += rank+"|"+entry.getKey()+"|6l"+":"+Math.log(1/((double)(last_day.getTime()/86400000-Collections.min(entry.getValue()).getTime()/86400000))+1)+",";
        	// 整个时间段第一次行为与最后一次行为的时间差
        	if(entry.getValue().size()>1){
        		feature += rank+"|"+entry.getKey() + "|7" + ":" +(double)(Collections.max(entry.getValue()).getTime()/86400000-Collections.min(entry.getValue()).getTime()/86400000)+",";
        		// 整个时间段第一次行为与最后一次行为的时间差log
//        		feature += rank+"|"+entry.getKey() + "|7l" + ":" +Math.log((double)(Collections.max(entry.getValue()).getTime()/86400000-Collections.min(entry.getValue()).getTime()/86400000)+1)+",";
        	}
        }
//        for(Map.Entry<String, Integer> entry: map_db_c.entrySet()){
//        	String [] strsk = entry.getKey().split("\\|");
//        	if(!strsk[1].equals("2")){
//        		// 双11当天|行为 数量log
//        		feature += rank+"|"+entry.getKey()+"|0"+":"+Math.log((double)entry.getValue()+1)+",";
//        		// 双11当天|行为 占比       	
//        		feature += rank+"|"+entry.getKey()+"|1"+":"+(double)entry.getValue()/(double)map_all_c.get("0|"+strsk[1])+",";
//        	}
//        }
        for(Map.Entry<String,Integer> entry: map.entrySet()){
        	// 每个月行为数
            feature += rank+"|"+entry.getKey()+"|0"+":"+Math.log((double)entry.getValue()+1)+",";
            // 每个月行为数占比
            String [] strsk = entry.getKey().split("\\|");
            feature += rank+"|"+entry.getKey()+"|5"+":"+(double)entry.getValue()/(double)map_all_c.get("0"+"|"+strsk[1])+",";
            // 每个月行为数占比log
//            feature += rank+"|"+entry.getKey()+"|5l"+":"+Math.log((double)entry.getValue()/(double)map_all_c.get("0"+"|"+strsk[1])+1)+",";
            // 615 11月点击/加购/收藏 / 双十一购买
         
        }
        for(Map.Entry<String,HashSet<Integer>> entry: map_d.entrySet()){
        	// 每个月行为天数
            feature += rank+"|"+entry.getKey()+"|1"+":"+Math.log((double)entry.getValue().size()+1)+",";
            // 每个月行为数/行为天数
            feature += rank+"|"+entry.getKey()+"|2"+":"+map.get(entry.getKey())/(double)entry.getValue().size()+",";
            // 每个月行为数/行为天数log
//            feature += rank+"|"+entry.getKey()+"|2l"+":"+Math.log(map.get(entry.getKey())/(double)entry.getValue().size()+1)+",";
        }
        for(Map.Entry<String, HashSet<String>> entry: map_i.entrySet()){
        	// 每个月行为产品数
        	feature += rank+"|"+entry.getKey()+"|3"+":"+Math.log((double)entry.getValue().size()+1)+",";
        	// 每个月行为产品数/行为数
        	feature += rank+"|"+entry.getKey()+"|4"+":"+(double)entry.getValue().size()/map.get(entry.getKey())+",";
        	// 每个月行为产品数/行为数log
//        	feature += rank+"|"+entry.getKey()+"|4l"+":"+Math.log((double)entry.getValue().size()/map.get(entry.getKey())+1)+",";
        }
        
        feature += merchant_id+":"+"1";
        
        
        return feature;
    }
    
    // 用户特征提取
    public static HashMap<String,String> extractUserFeature(List<FE_Data_Raw> data_raw_list, HashMap<String,String> brandid2inx, HashMap<String,String> catid2inx) {
		int num = data_raw_list.size();
		Date last_day = new Date(2014,11,12);
		HashMap<String,userStat> map_user = new HashMap<String, userStat>();
		HashMap<String,String> user_mbc_map = new HashMap<String, String>();
		HashMap<String,String> user_feature_map = new HashMap<String, String>();
		HashMap<String,String> user_age = new HashMap<String, String>();
		HashMap<String,String> user_gender = new HashMap<String, String>();
		FE_Data_Raw d;
		userStat us;
		for(int i=0;i<num;i++){
			d = data_raw_list.get(i);
			String age = d.age_range;
			String gender = d.gender;
			if(age.equals("-999"))
				age = "0";
			if(gender.equals("-999"))
				gender = "2";
			if (!user_age.containsKey(d.user_id))
				user_age.put(d.user_id, age);
			if (!user_gender.containsKey(d.user_id))
				user_gender.put(d.user_id, gender);
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
//				us.map_db_c = new HashMap<String, Integer>();
				us.map_all_m2 = new HashMap<String, HashSet<String>>();
				us.map_all_m3 = new HashMap<String, HashSet<String>>();
				
				//map_user.put(d.user_id, us);
			}
			if(!user_mbc_map.containsKey(d.user_id)){
				user_mbc_map.put(d.user_id, "");
			}
			String [] strs = d.activity_log.split("#");
//			String mbc_id = "u|mid|b"+"|" + d.merchant_id +":"+"1"+",";
			String mbc_id = "";
			for(int k=0;k<strs.length;k++){
	            String []strs2 = strs[k].split(":");
	            if (strs2.length==5){
	            	int month = Integer.parseInt(strs2[3].substring(0, 2));
	                int day = Integer.parseInt(strs2[3].substring(2,4));
	                String brand = strs2[2];
	                String cat = strs2[1];
	                if (month==11&&day>11)
	                	day = 11;
	                if(strs2[4].equals("2")){
	                	mbc_id += "u|bid|b"+"|"+brandid2inx.get(brand)+":"+1+",";
	                	mbc_id += "u|cid|b"+"|"+catid2inx.get(cat)+":"+1+",";
	                }
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
	                // 用户整个时间段|行为 merchant+month
	                if(us.map_all_m3.containsKey("0"+"|"+strs2[4])){
	                	HashSet<String> set_m3 = us.map_all_m3.get("0"+"|"+strs2[4]);
	                	set_m3.add(month+","+d.merchant_id);
	                	us.map_all_m3.put("0"+"|"+strs2[4], set_m3);
	                } else{
	                	HashSet<String> set_m3 = new HashSet<String>();
	                	set_m3.add(month+","+d.merchant_id);
	                	us.map_all_m3.put("0"+"|"+strs2[4], set_m3);
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
	                // 用户双11|行为 计数
//	                if(month==11&&day==11){
//	                	if(us.map_db_c.containsKey("db11"+"|"+strs2[4])){
//	                		us.map_db_c.put("db11"+"|"+strs2[4], us.map_db_c.get("db11"+"|"+strs2[4])+1);
//	                	} else{
//	                		us.map_db_c.put("db11"+"|"+strs2[4], 1);
//	                	}
//	                }
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
			user_mbc_map.put(d.user_id,user_mbc_map.get(d.user_id)+mbc_id);
			map_user.put(d.user_id, us);
		}
		
		String user;
		userStat us1;
		String feature;
		
		for(Map.Entry<String, userStat> entry: map_user.entrySet()){
			user = entry.getKey();
			us1 = entry.getValue();
			String age = user_age.get(user);
			String gender = user_gender.get(user);
			String mbc_id = user_mbc_map.get(user);
			feature = "";
			for(Map.Entry<String,Integer> entry2: us1.map_all_c.entrySet()){
	        	// 整个时间段行为数
	            feature += "u"+entry2.getKey()+"|0"+":"+Math.log((double)entry2.getValue()+1)+",";
	            // *age*gender
//	            feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|0"+":"+Math.log((double)entry2.getValue()+1)+",";
	            // 购买可能性：购买数/行为数
	            String [] strsk = entry2.getKey().split("\\|");
	            if (!strsk[1].equals("2")&&us1.map_all_c.containsKey("0|2")){
	            	feature += "u"+entry2.getKey()+"|6"+":"+(double)us1.map_all_c.get("0|2")/(double)entry2.getValue()+",";
	            	// *age*gender
//	            	feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|6"+":"+(double)us1.map_all_c.get("0|2")/(double)entry2.getValue()+",";
	            	// 购买可能性：购买数/行为数log
//	            	feature += "u"+entry2.getKey()+"|6l"+":"+Math.log((double)us1.map_all_c.get("0|2")/(double)entry2.getValue()+1)+",";
	            }
	        }
			for(Map.Entry<String,HashSet<Date>> entry2: us1.map_all_d.entrySet()){
	        	// 整个时间段行为天数
	            feature += "u"+entry2.getKey()+"|1"+":"+Math.log((double)entry2.getValue().size()+1)+",";
	            // *age*gender
//	            feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|1"+":"+Math.log((double)entry2.getValue().size()+1)+",";
	            // 整个时间段行为数/行为天数
	            feature += "u"+entry2.getKey()+"|2"+":"+us1.map_all_c.get(entry2.getKey())/(double)entry2.getValue().size()+",";
	            // *age*gender
//	            feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|2"+":"+us1.map_all_c.get(entry2.getKey())/(double)entry2.getValue().size()+",";
	            // 整个时间段行为数/行为天数log
//	            feature += "u"+entry2.getKey()+"|2l"+":"+Math.log(us1.map_all_c.get(entry2.getKey())/(double)entry2.getValue().size()+1)+",";
	            // 整个时间段最近时间差倒数
	            feature += "u"+entry2.getKey()+"|4"+":"+1/((double)(last_day.getTime()/86400000-Collections.max(entry2.getValue()).getTime()/86400000))+",";
	            // *age*gender
//	            feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|4"+":"+1/((double)(last_day.getTime()/86400000-Collections.max(entry2.getValue()).getTime()/86400000))+",";
	            // 整个时间段最近时间差倒数log
//	            feature += "u"+entry2.getKey()+"|4l"+":"+Math.log(1/((double)(last_day.getTime()/86400000-Collections.max(entry2.getValue()).getTime()/86400000))+1)+",";
	            // 整个时间段最远时间差倒数
	            feature += "u"+entry2.getKey()+"|8"+":"+1/(double)(last_day.getTime()/86400000-Collections.min(entry2.getValue()).getTime()/86400000)+",";
	            // *age*gender
//	            feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|8"+":"+1/(double)(last_day.getTime()/86400000-Collections.min(entry2.getValue()).getTime()/86400000)+",";
	            // 整个时间段最远时间差倒数log
//	            feature += "u"+entry2.getKey()+"|8l"+":"+Math.log(1/(double)(last_day.getTime()/86400000-Collections.min(entry2.getValue()).getTime()/86400000)+1)+",";
	            // 整个时间段最近-最远时间差
	            //feature += "u"+entry2.getKey()+"|9"+":"+1/(double)(Collections.max(entry2.getValue()).getTime()/86400000-Collections.min(entry2.getValue()).getTime()/86400000)+",";
	        }
			for(Map.Entry<String,HashSet<String>> entry2: us1.map_all_m.entrySet()){
				String [] strsk = entry2.getKey().split("\\|");
				if(strsk[1].equals("2")){
					for (String m : entry2.getValue()){
						mbc_id += "u|mid|b"+"|"+m+":"+"1"+",";
					}
				}
	        	// 整个时间段行为m数
	            feature += "u"+entry2.getKey()+"|3"+":"+Math.log((double)entry2.getValue().size()+1)+",";
	            // *age*gender
//	            feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|3"+":"+Math.log((double)entry2.getValue().size()+1)+",";
	            // 整个时间段行为m数/行为数
	            feature += "u"+entry2.getKey()+"|7"+":"+(double)entry2.getValue().size()/(double)us1.map_all_c.get(entry2.getKey())+",";
	            // *age*gender
//	            feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|7"+":"+(double)entry2.getValue().size()/(double)us1.map_all_c.get(entry2.getKey())+",";
	            // 整个时间段行为m数/行为数log
//	            feature += "u"+entry2.getKey()+"|7l"+":"+Math.log((double)entry2.getValue().size()/(double)us1.map_all_c.get(entry2.getKey())+1)+",";
	        }
			for(Map.Entry<String,HashSet<String>> entry2: us1.map_all_m2.entrySet()){
	        	// 整个时间段行为天数>=2 m数
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
				int c2 = 0;
				int c5 = 0;
				for(Map.Entry<String, Integer> entry3: mcount.entrySet()){
					if(entry3.getValue()>1)
						c2++;
					if(entry3.getValue()>4)
						c5++;
				}
				if(c2>0){
					feature += "u"+entry2.getKey()+"|5"+":"+Math.log(c2+1)+",";
					// *age*gender
//					feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|5"+":"+Math.log(c2+1)+",";
					//整个时间段行为天数大于1的m占比
					feature += "u"+entry2.getKey()+"|11"+":"+c2/(double)us1.map_all_m.get(entry2.getKey()).size()+",";
					// *age*gender
//					feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|11"+":"+c2/(double)us1.map_all_m.get(entry2.getKey()).size()+",";
					//整个时间段行为天数大于1的m占比log
//					feature += "u"+entry2.getKey()+"|11l"+":"+Math.log(c2/(double)us1.map_all_m.get(entry2.getKey()).size()+1)+",";
				}
				if(c5>0){
					feature += "u"+entry2.getKey()+"|10"+":"+Math.log(c5+1)+",";
					// *age*gender
//					feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|10"+":"+Math.log(c5+1)+",";
					//整个时间段行为天数大于4的m占比
					feature += "u"+entry2.getKey()+"|12"+":"+c2/(double)us1.map_all_m.get(entry2.getKey()).size()+",";
					// *age*gender
//					feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|12"+":"+c2/(double)us1.map_all_m.get(entry2.getKey()).size()+",";
					//整个时间段行为天数大于4的m占比log
//					feature += "u"+entry2.getKey()+"|12l"+":"+Math.log(c2/(double)us1.map_all_m.get(entry2.getKey()).size()+1)+",";
				}
	        }
			for(Map.Entry<String,HashSet<String>> entry2: us1.map_all_m3.entrySet()){
	        	// 整个时间段行为月数>=2 m数
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
				int c2 = 0;
				int c3 = 0;
				for(Map.Entry<String, Integer> entry3: mcount.entrySet()){
					if(entry3.getValue()>1)
						c2++;
					if(entry3.getValue()>1)
						c3++;
				}
				if(c2>0){
					feature += "u"+entry2.getKey()+"|9"+":"+Math.log(c2+1)+",";
					// *age*gender
//					feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|9"+":"+Math.log(c2+1)+",";
					//整个时间段行为月数大于2的m占比
					feature += "u"+entry2.getKey()+"|13"+":"+c2/(double)us1.map_all_m.get(entry2.getKey()).size()+",";
					//*age*gender
//					feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|13"+":"+c2/(double)us1.map_all_m.get(entry2.getKey()).size()+",";
					//整个时间段行为月数大于2的m占比log
//					feature += "u"+entry2.getKey()+"|13l"+":"+Math.log(c2/(double)us1.map_all_m.get(entry2.getKey()).size()+1)+",";
				}
				if(c3>0){
					feature += "u"+entry2.getKey()+"|14"+":"+Math.log(c3+1)+",";
					// *age*gender
//					feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|14"+":"+Math.log(c3+1)+",";
					//整个时间段行为月数大于3的m占比
					feature += "u"+entry2.getKey()+"|15"+":"+c3/(double)us1.map_all_m.get(entry2.getKey()).size()+",";
					//*age*gender
//					feature += "u"+"|ag|"+age+"|"+gender+"|"+entry2.getKey()+"|15"+":"+c3/(double)us1.map_all_m.get(entry2.getKey()).size()+",";
					//整个时间段行为月数大于3的m占比log
//					feature += "u"+entry2.getKey()+"|15l"+":"+Math.log(c3/(double)us1.map_all_m.get(entry2.getKey()).size()+1)+",";
				}
	        }
//			for(Map.Entry<String,Integer> entry2: us1.map_db_c.entrySet()){
//				// 双11行为数
//				feature += "u|"+entry2.getKey()+"|0"+":"+Math.log((double)entry2.getValue()+1)+",";
//				// 双11行为数占比
//				String [] strsk = entry2.getKey().split("\\|");
//				feature += "u|"+entry2.getKey()+"|1"+":"+(double)entry2.getValue()/(double)us1.map_all_c.get("0"+"|"+strsk[1])+",";
//			}
			for(Map.Entry<String,Integer> entry2: us1.map_u_c.entrySet()){
				// 每个月行为数
				feature += "u"+entry2.getKey()+"|0"+":"+Math.log((double)entry2.getValue()+1)+",";
				// 每个月行为数占比
				String [] strsk = entry2.getKey().split("\\|");
				feature += "u"+entry2.getKey()+"|4"+":"+(double)entry2.getValue()/(double)us1.map_all_c.get("0"+"|"+strsk[1])+",";
				// 每个月行为数占比log
//				feature += "u"+entry2.getKey()+"|4l"+":"+Math.log((double)entry2.getValue()/(double)us1.map_all_c.get("0"+"|"+strsk[1])+1)+",";
			}
			for(Map.Entry<String, HashSet<Integer>> entry2: us1.map_u_d.entrySet()){
				// 每个月行为天数
				feature += "u"+entry2.getKey()+"|1"+":"+Math.log((double)entry2.getValue().size()+1)+",";
				// 每个月行为数/行为天数
				feature += "u"+entry2.getKey()+"|2"+":"+us1.map_u_c.get(entry2.getKey())/(double)entry2.getValue().size()+",";
				// 每个月行为数/行为天数log
//				feature += "u"+entry2.getKey()+"|2l"+":"+Math.log(us1.map_u_c.get(entry2.getKey())/(double)entry2.getValue().size()+1)+",";
			}
			for(Map.Entry<String,HashSet<String>> entry2: us1.map_u_m.entrySet()){
	        	// 每个月行为m数
	            feature += "u"+entry2.getKey()+"|3"+":"+Math.log((double)entry2.getValue().size()+1)+",";
	        }
			
			if(feature!=null&&!feature.equals("")){
				//System.out.println(feature.length());
				feature = feature.substring(0,feature.length()-1);
			}
			
			user_feature_map.put(user,feature);
		}
		return user_feature_map;
	}
    
    public static String activity_log2simfeature(int rank,String merchant_id, String activitiy_log){
        String feature="";
        String [] strs = activitiy_log.split("#");
        HashMap<String,Integer> map = new HashMap<String, Integer>();
        for(int i=0;i<strs.length;i++){
            String []strs2 = strs[i].split(":");
            if(strs2.length==5) {
                int month = Integer.parseInt(strs2[3].substring(0, 2));
                if (map.containsKey(month + "|" + strs2[4])) {
                    map.put(month + "|" + strs2[4], map.get(month + "|" + strs2[4]) + 1);
                } else {
                    map.put(month + "|" + strs2[4], 1);
                }
            }
        }
        for(Map.Entry<String,Integer> entry: map.entrySet()){
            feature += rank+"|"+entry.getKey()+":"+Math.log((double)entry.getValue()+1)+",";
        }
        feature += merchant_id+":"+"1";
        return feature;
    }
    
    //Stat cat of user buy
    public static HashMap<String,HashSet<String>> statUserBuyCat(List<FE_Data_Raw> data_raw_list, HashMap<String,String> catid2inx) {
    	HashMap<String,HashSet<String>> user_cat = new HashMap<String, HashSet<String>>();
    	HashMap<String,HashSet<String>> user_cat2 = new HashMap<String, HashSet<String>>();
    	HashMap<String,HashSet<String>> user_cat2sp = new HashMap<String, HashSet<String>>();
    	int num = data_raw_list.size();
    	FE_Data_Raw d;
    	for(int i=0;i<num;i++){
    		d = data_raw_list.get(i);
    		String [] strs = d.activity_log.split("#");
    		if(!user_cat.containsKey(d.user_id)){
    			HashSet<String> cat_set = new HashSet<String>();
    			user_cat.put(d.user_id, cat_set);
    		}
    		if(!user_cat2.containsKey(d.user_id)){
    			HashSet<String> cat_set2 = new HashSet<String>();
    			user_cat2.put(d.user_id, cat_set2);
    		}
    		HashSet<String> cat_set = user_cat.get(d.user_id);
    		HashSet<String> cat_set2 = user_cat2.get(d.user_id);
    		for(int k=0;k<strs.length;k++){
    			String []strs2 = strs[k].split(":");
	            if (strs2.length==5){
	            	if(strs2[4].equals("2")){
	            		cat_set.add(catid2inx.get(strs2[1]));
	            		cat_set2.add(catid2inx.get(strs2[1])+","+strs2[3]);
	            	}
	            }
    		}
    		user_cat.put(d.user_id, cat_set);
    		user_cat2.put(d.user_id, cat_set2);
    	}
    	for(Map.Entry<String,HashSet<String>> entry: user_cat2.entrySet()){
    		HashMap<String,Integer> ccount = new HashMap<String, Integer>();
    		for(String catdat : entry.getValue()){
    			String cat = catdat.split(",")[0];
    			if(ccount.containsKey(cat)){
    				ccount.put(cat, ccount.get(cat)+1);
    			}else{
    				ccount.put(cat, 1);
    			}
    		}
    		HashSet<String> cats = new HashSet<String>();
    		for(Map.Entry<String,Integer> entry2: ccount.entrySet()){
    			if(entry2.getValue()>1)
    				cats.add(entry2.getKey());
    		}
    		user_cat2sp.put(entry.getKey(), cats);
    	}
//    	return user_cat;
    	return user_cat2sp;
	}
    //Stat brand of user buy
    public static HashMap<String,HashSet<String>> statUserBuyBrand(List<FE_Data_Raw> data_raw_list, HashMap<String,String> brandid2inx) {
    	HashMap<String,HashSet<String>> user_brand = new HashMap<String, HashSet<String>>();
    	//购买天数超过一天的brand stat
    	HashMap<String,HashSet<String>> user_brand2 = new HashMap<String, HashSet<String>>();
    	HashMap<String,HashSet<String>> user_brand2sp = new HashMap<String, HashSet<String>>();
    	int num = data_raw_list.size();
    	FE_Data_Raw d;
    	for(int i=0;i<num;i++){
    		d = data_raw_list.get(i);
    		String [] strs = d.activity_log.split("#");
    		if(!user_brand.containsKey(d.user_id)){
    			HashSet<String> brand_set = new HashSet<String>();
    			user_brand.put(d.user_id, brand_set);
    		}
    		if(!user_brand2.containsKey(d.user_id)){
    			HashSet<String> brand_set2 = new HashSet<String>();
    			user_brand2.put(d.user_id, brand_set2);
    		}
    		HashSet<String> brand_set = user_brand.get(d.user_id);
    		HashSet<String> brand_set2 = user_brand2.get(d.user_id);
    		for(int k=0;k<strs.length;k++){
    			String []strs2 = strs[k].split(":");
    			
	            if (strs2.length==5){
	            	if(strs2[4].equals("2")){
	            		brand_set.add(brandid2inx.get(strs2[2]));
	            		brand_set2.add(brandid2inx.get(strs2[2])+","+strs2[3]);
	            	}
	            }
    		}
    		user_brand.put(d.user_id, brand_set);
    		user_brand2.put(d.user_id, brand_set2);
    	}
    	for(Map.Entry<String,HashSet<String>> entry: user_brand2.entrySet()){
    		HashMap<String,Integer> bcount = new HashMap<String, Integer>();
    		for(String branddat : entry.getValue()){
    			String brand = branddat.split(",")[0];
    			if(bcount.containsKey(brand)){
    				bcount.put(brand, bcount.get(brand)+1);
    			}else{
    				bcount.put(brand, 1);
    			}
    		}
    		HashSet<String> brands = new HashSet<String>();
    		for(Map.Entry<String,Integer> entry2: bcount.entrySet()){
    			if(entry2.getValue()>1)
    				brands.add(entry2.getKey());
    		}
    		user_brand2sp.put(entry.getKey(), brands);
    	}
//    	return user_brand;
    	return user_brand2sp;
	}
    
    //user buy cat & merchant's cat
    public static String ucatUmcat(String user, String merchant, HashMap<String,HashSet<String>> user_cat,HashMap<String,HashSet<String>> merchant_cat) {
		String feature = "";
		HashSet<String> union = new HashSet<String>();
		union.addAll(user_cat.get(user));
		union.retainAll(merchant_cat.get(merchant));
		for(String cat : union){
			feature += "umc|" + cat +":"+"1"+ ",";
		}
		if(!feature.equals(""))
			feature = feature.substring(0,feature.length()-1);
		return feature;
	}
    //user buy brand & merchant's brand
    public static String ubrandUmbrand(String user, String merchant, HashMap<String,HashSet<String>> user_brand,HashMap<String,HashSet<String>> merchant_brand) {
		String feature = "";
		HashSet<String> union = new HashSet<String>();
		union.addAll(user_brand.get(user));
		union.retainAll(merchant_brand.get(merchant));
		for(String brand : union){
			feature += "umb|" + brand +":"+ "1" + ",";
		}
		if(!feature.equals(""))
			feature = feature.substring(0,feature.length()-1);
		return feature;
	}
    //user buy brand & merchant's brand
    public static String ubrandUmbrand(String user, String merchant, HashMap<String,HashSet<String>> user_brand,HashMap<String,HashSet<String>> merchant_brand,HashMap<String,HashMap<String,Double>> ub_weight_map) {
		String feature = "";
		HashSet<String> union = new HashSet<String>();
		union.addAll(user_brand.get(user));
		union.retainAll(merchant_brand.get(merchant));
		HashMap<String,Double> bw = ub_weight_map.get(user);
		for(String brand : union){
			feature += "umb|" + brand +":"+ bw.get(brand) + ",";
		}
		if(!feature.equals(""))
			feature = feature.substring(0,feature.length()-1);
		return feature;
	}
    
    //UB weight
    public static HashMap<String,HashMap<String,Double>> statUBweight(List<FE_Data_Raw> data_raw_list, HashMap<String,String> brandid2inx) {
		HashMap<String,HashMap<String,Double>> ub_weight_map = new HashMap<String, HashMap<String,Double>>();
		int num = data_raw_list.size();
    	FE_Data_Raw d;
    	Date firstDate = new Date(2014,5,11);
    	for(int i=0;i<num;i++){
    		d = data_raw_list.get(i);
    		String [] strs = d.activity_log.split("#");
    		if(!ub_weight_map.containsKey(d.user_id)){
    			HashMap<String, Double> bw = new HashMap<String, Double>();
    			ub_weight_map.put(d.user_id, bw);
    		}
    		HashMap<String, Double> bw = ub_weight_map.get(d.user_id);
    		for(int k=0;k<strs.length;k++){
    			String []strs2 = strs[k].split(":");
    			if(strs2.length==5){
    				int month = Integer.parseInt(strs2[3].substring(0, 2));
    				int day = Integer.parseInt(strs2[3].substring(2,4));
    				if (month==11&&day>11)
    					day = 11;
    				Date dat = new Date(2014,month,day);
    				int beh_w;
    				if(strs2[4].equals("0"))
    					beh_w = 1;
    				else if(strs2[4].equals("1")||strs2[4].equals("3"))
    					beh_w = 5;
    				else
    					beh_w = 10;
    				int time_w = (int)(dat.getTime()/86400000 - firstDate.getTime()/86400000)/7+1;
    				String bid = brandid2inx.get(strs2[2]);
    				if(bw.containsKey(bid)){
    					bw.put(bid, bw.get(bid)+(double)beh_w*time_w);
    				}else{
    					bw.put(bid, (double)beh_w*time_w);
    				}
    			}
    		}
    	}
    	for(Map.Entry<String, HashMap<String,Double>> entry : ub_weight_map.entrySet()){
    		String user = entry.getKey();
    		HashMap<String,Double> bw = entry.getValue();
    		for(Map.Entry<String, Double> entry2 : entry.getValue().entrySet()){
    			String bid = entry2.getKey();
    			bw.put(bid, Math.log(entry2.getValue()));
    		}
    		ub_weight_map.put(user, bw);
    	}
		return ub_weight_map;
	}
    //UB weight dummy
    public static String dummyUBweight(FE_Data_Raw data_raw_list, HashMap<String,HashMap<String,Double>> ub_weight_map,HashMap<String,String> brandid2inx) {
		String feat = "";
		String [] strs = data_raw_list.activity_log.split("#");
		HashMap<String, Double> bw = ub_weight_map.get(data_raw_list.user_id);
		HashSet<String> umb = new HashSet<String>();
		for(int k=0;k<strs.length;k++){
			String []strs2 = strs[k].split(":");
			if(strs2.length==5){
				String bid = brandid2inx.get(strs2[2]);
				umb.add(bid);
			}
		}
		for(String bid : umb){
			feat += "umBw|" + bid + ":" + bw.get(bid)+",";
		}
		if(!feat.equals(""))
			feat = feat.substring(0,feat.length()-1);
		return feat;
	}

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
//        int n_merchant = 4995;
        for(int i=0;i<numMerchant;i++){
            String [] strs = auxilary_data.get(i).split(",");
            List<String> list =  new ArrayList<String>(num_similar_merchants);

            for (int j = 0; j < 5; j++)
                list.add(j, strs[j + 1]);

            similarity_merchants.put(strs[0],list);
        }
        
        
        //load brandId2inx
        HashMap<String,String> brandId2inx = new HashMap<String, String>();
        for(int i=numMerchant;i<numMerchant+numBrand;i++){
        	String [] strs = auxilary_data.get(i).split(",");
        	brandId2inx.put(strs[0], strs[1]);
        }
        //load catId2inx
        HashMap<String,String> catId2inx = new HashMap<String, String>();
        for(int i=numMerchant+numBrand;i<numMerchant+numBrand+numCat;i++){
        	String[] strs = auxilary_data.get(i).split(",");
        	catId2inx.put(strs[0], strs[1]);
        }
        //load merchant cat
        HashMap<String,HashSet<String>> merchant_cat = new HashMap<String, HashSet<String>>();
        for(int i=numMerchant+numBrand+numCat;i<numMerchant*2+numBrand+numCat;i++){
        	String [] strs = auxilary_data.get(i).split("@");
        	String [] strs2 = strs[1].split(",");
        	HashSet<String> cat_set = new HashSet<String>();
        	for(String st : strs2){
        		cat_set.add(catId2inx.get(st));
        	}
        	merchant_cat.put(strs[0], cat_set);
        }
        //load merchant brand
        HashMap<String,HashSet<String>> merchant_brand = new HashMap<String, HashSet<String>>();
        for(int i=numMerchant*2+numBrand+numCat;i<numMerchant*3+numBrand+numCat;i++){
        	String [] strs = auxilary_data.get(i).split("@");
        	String [] strs2 = strs[1].split(",");
        	HashSet<String> brand_set = new HashSet<String>();
        	for(String st : strs2){
        		brand_set.add(brandId2inx.get(st));
        	}
        	merchant_brand.put(strs[0], brand_set);
        }
        
        //load merchant feature
        HashMap<String,String> merchant_feature = new HashMap<String, String>();
        for(int i=numMerchant*3+numBrand+numCat;i<numMerchant*4+numBrand+numCat;i++){
        	String [] strs = auxilary_data.get(i).split("@");
        	merchant_feature.put(strs[0], strs[1]);
        }
//      System.out.println(merchant_feature.size());
        
        //extract user feature
//        HashMap<String,String> uf_map = extractUserFeature4dummy(data_raw_list);
        HashMap<String,String> uf_map = extractUserFeature(data_raw_list,brandId2inx,catId2inx);
        //stat user buy cat
//        HashMap<String,HashSet<String>> user_cat = statUserBuyCat(data_raw_list, catId2inx);
        //stat user buy brand
//        HashMap<String,HashSet<String>> user_brand = statUserBuyBrand(data_raw_list, brandId2inx);
        //stat UB weight
//        HashMap<String, HashMap<String,Double>> ub_weight_map = statUBweight(data_raw_list, brandId2inx);
        
        //the feature is defined by behavior log on itself and  top 5 similar merchants
        for(int i=0;i<num;i++){
            d = data_raw_list.get(i);
            if(!d.label.equals("-1")&&!d.activity_log.equals("")){
                FE_Data_Processed d2 = new FE_Data_Processed();
                d2.user_id = d.user_id;
                d2.merchant_id = d.merchant_id;
//                d2.features = activity_log2feature4dummy(0,d.merchant_id,d.activity_log,brandId2inx,catId2inx);
                d2.features = activity_log2feature(0,d.merchant_id,d.age_range,d.gender,d.activity_log,brandId2inx,catId2inx);
//                String ucmc = ucatUmcat(d.user_id, d.merchant_id, user_cat, merchant_cat);
//                String ubmb = ubrandUmbrand(d.user_id, d.merchant_id, user_brand, merchant_brand);
                //use ub weight
//                String ubmb = ubrandUmbrand(d.user_id, d.merchant_id, user_brand, merchant_brand);
//                if(!ucmc.equals(""))
//                	d2.features += "," + ucmc;
//                if(!ubmb.equals(""))
//                	d2.features += "," + ubmb;
//                String ubw = dummyUBweight(d,ub_weight_map,brandId2inx);
//                if(!ubw.equals(""))
//                	d2.features += "," + ubw;
                d2.label = d.label;
                List<String> list = similarity_merchants.get(d.merchant_id);
                for(int j=0;j<5;j++){
                    if(map.containsKey(d2.user_id+"|"+list.get(j))) {
//                        d2.features += "," + activity_log2feature4dummy(j+1,list.get(j),map.get(d2.user_id+"|"+list.get(j)),brandId2inx,catId2inx);
//                        d2.features += "," + activity_log2simfeature(j+1,list.get(j),map.get(d2.user_id+"|"+list.get(j)));
                        d2.features += "," + activity_log2feature(j+1,list.get(j),d.age_range,d.gender,map.get(d2.user_id+"|"+list.get(j)),brandId2inx,catId2inx);
                    }
                }
                d2.features += "," + uf_map.get(d.user_id);
                // age
                if (d.age_range==null||d.age_range.equals("-999")){
                	d2.features += "," + "u|age|" + "0" + ":" + "1";
                }else{
                	d2.features += "," + "u|age|" + d.age_range + ":" + "1";
                }
                // gender
                if (d.gender==null||d.gender.equals("-999")){
                	d2.features += "," + "u|gender|" + "2" + ":" + "1";
                }else{
                	d2.features += "," + "u|gender|" + d.gender + ":" + "1";
                }
                // age*gender
//                if (d.age_range.equals("-999")&&d.gender.equals("-999")){
//                	d2.features += "," + "u|a|g|m|" + "0|2|"+d.merchant_id+ ":" + "1";
//                } else if(d.age_range.equals("-999")){
//                	d2.features += "," + "u|a|g|m|" + "0|" + d.gender +"|"+d.merchant_id + ":" + "1";
//                } else if(d.gender.equals("-999")){
//                	d2.features += "," + "u|a|g|m|" + d.age_range + "|2|" +d.merchant_id + ":" + "1";
//                } else{
//                	d2.features += "," + "u|a|g|m|" + d.age_range +"|" + d.gender +"|"+d.merchant_id+ ":" + "1";
//                }
//                d2.features += "," + "u|a|g|" + age +"|" + gender + ":" + "1";
                
                //add merchant feature
                d2.features += "," + merchant_feature.get(d.merchant_id);
                //#brand in merchant
//                d2.features += "," + "mbn:" +Math.log(merchant_brand.get(d.merchant_id).size()+1);
                data_list.add(d2);
            }
        }
        System.out.println(data_list.size());
        return data_list;
    }
}


//GBDT 特征体系
/*
public class FE_Extractor {
    //perform feature extraction in this class
    //you need to implement mapping() and reformat(), respectively
    //reformat() convert data of format 2 to features you need
    //mapping() returns feature mapping (i.e. feature name and its corresponding dimension index)
	
	
    public static HashMap<String,Integer> mapping()
    {
        HashMap<String,Integer> map = new HashMap<String, Integer>();
        int count = 0;
        //um feature index
        for(int action=0;action<=3;action++)
        	for(int stat_type=0;stat_type<=7;stat_type++)
        	{
        		map.put("0"+"|"+action+"|"+stat_type, count);
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
    		for(int stat_type=7;stat_type<=15;stat_type++)
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
        map.put("m"+"0"+"|"+"0|"+"9", count);
        count++;
        map.put("m"+"0"+"|"+"1|"+"9", count);
        count++;
        map.put("m"+"0"+"|"+"3|"+"9", count);
        count++;
        for (int action=0;action<=3;action++)
        	for (int stat_type=10;stat_type<=17;stat_type++){
        		map.put("m"+"0"+"|"+action+"|"+stat_type, count);
        		count++;
        	}
        for (int st=18;st<=23;st++){
        	map.put("m0|"+st, count);
        	count++;
        }
             
        return map;
    }

    // um特征提取
    public static String activity_log2feature(String merchant_id, String activitiy_log){
        String feature="";
        String [] strs = activitiy_log.split("#");
//        if(strs.length==1)
//        	System.out.println("act_log is empty");
        Date last_day = new Date(2014,11,12);
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
            feature += entry.getKey()+"|0"+":"+Math.log((double)entry.getValue()+1)+",";
        }
        for(Map.Entry<String,HashSet<Date>> entry: map_all_dat.entrySet()){
        	// 整个时间段行为天数
            feature += entry.getKey()+"|1"+":"+Math.log((double)entry.getValue().size()+1)+",";
            // 整个时间段行为数/行为天数
            feature += entry.getKey()+"|2"+":"+map_all_c.get(entry.getKey())/(double)entry.getValue().size()+",";
        }
        for(Map.Entry<String, HashSet<String>> entry: map_all_i.entrySet()){
        	// 整个时间段行为产品数
        	feature += entry.getKey()+"|3"+":"+Math.log((double)entry.getValue().size()+1)+",";
        	// 整个时间段行为产品数/行为数
        	feature += entry.getKey()+"|4"+":"+(double)entry.getValue().size()/map_all_c.get(entry.getKey())+",";
        }
        for(Map.Entry<String, HashSet<Date>> entry: map_all_dat.entrySet()){
        	// 整个时间段最近行为时间差倒数
        	feature += entry.getKey()+"|5"+":"+1/((double)(last_day.getTime()/86400000-Collections.max(entry.getValue()).getTime()/86400000))+",";
        	// 整个时间段最远行为时间差倒数
        	feature += entry.getKey()+"|6"+":"+1/((double)(last_day.getTime()/86400000-Collections.min(entry.getValue()).getTime()/86400000))+",";
        	// 整个时间段第一次行为与最后一次行为的时间差
        	if(entry.getValue().size()>1)
        		feature += entry.getKey() + "|7" + ":" +(double)(Collections.max(entry.getValue()).getTime()/86400000-Collections.min(entry.getValue()).getTime()/86400000)+",";
        }
        if(feature!=null&&!feature.equals("")){
			//System.out.println(feature.length());
			feature = feature.substring(0,feature.length()-2);
		}
        
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
				us.map_all_m3 = new HashMap<String, HashSet<String>>();
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
	                // 用户整个时间段|行为 merchant+month
	                if(us.map_all_m3.containsKey("0"+"|"+strs2[4])){
	                	HashSet<String> set_m3 = us.map_all_m3.get("0"+"|"+strs2[4]);
	                	set_m3.add(month+","+d.merchant_id);
	                	us.map_all_m3.put("0"+"|"+strs2[4], set_m3);
	                } else{
	                	HashSet<String> set_m3 = new HashSet<String>();
	                	set_m3.add(month+","+d.merchant_id);
	                	us.map_all_m3.put("0"+"|"+strs2[4], set_m3);
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
				int c2 = 0;
				int c5 = 0;
				for(Map.Entry<String, Integer> entry3: mcount.entrySet()){
					if(entry3.getValue()>1)
						c2++;
					if(entry3.getValue()>4)
						c5++;
				}
				if(c2>0){
					feature += "u"+entry2.getKey()+"|5"+":"+c2+",";
					//整个时间段行为天数大于1的m占比
					feature += "u"+entry2.getKey()+"|11"+":"+c2/(double)us1.map_all_m.get(entry2.getKey()).size()+",";
				}
				if(c5>0){
					feature += "u"+entry2.getKey()+"|10"+":"+c5+",";
					//整个时间段行为天数大于4的m占比
					feature += "u"+entry2.getKey()+"|12"+":"+c2/(double)us1.map_all_m.get(entry2.getKey()).size()+",";
				}
	        }
			for(Map.Entry<String,HashSet<String>> entry2: us1.map_all_m3.entrySet()){
	        	// 整个时间段行为月数>=2 m数
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
				int c2 = 0;
				int c3 = 0;
				for(Map.Entry<String, Integer> entry3: mcount.entrySet()){
					if(entry3.getValue()>1)
						c2++;
					if(entry3.getValue()>1)
						c3++;
				}
				if(c2>0){
					feature += "u"+entry2.getKey()+"|9"+":"+c2+",";
					//整个时间段行为月数大于2的m占比
					feature += "u"+entry2.getKey()+"|13"+":"+c2/(double)us1.map_all_m.get(entry2.getKey()).size()+",";
				}
				if(c3>0){
					feature += "u"+entry2.getKey()+"|14"+":"+c3+",";
					//整个时间段行为月数大于3的m占比
					feature += "u"+entry2.getKey()+"|15"+":"+c3/(double)us1.map_all_m.get(entry2.getKey()).size()+",";
				}
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
//        HashMap<String,List<String>> similarity_merchants = new HashMap<String, List<String>>();
//        int num_similar_merchants = 5;
        int n_merchant = 4995;
//        for(int i=0;i<n_merchant;i++){
//            String [] strs = auxilary_data.get(i).split(",");
//            List<String> list =  new ArrayList<String>(num_similar_merchants);
//
//            for (int j = 0; j < 5; j++)
//                list.add(j, strs[j + 1]);
//
//            similarity_merchants.put(strs[0],list);
//        }
        //load merchant feature
        HashMap<String,String> merchant_feature = new HashMap<String, String>();
        for(int i=0;i<n_merchant;i++){
        	String [] strs = auxilary_data.get(i).split("@");
        	merchant_feature.put(strs[0], strs[1]);
        }
//        System.out.println(merchant_feature.size());
        
        //extract user feature
        HashMap<String,String> uf_map = extractUserFeature(data_raw_list);

        //the feature is defined by behavior log on itself and  top 5 similar merchants
        for(int i=0;i<num;i++){
            d = data_raw_list.get(i);
            if(!d.label.equals("-1")&&!d.activity_log.equals("")){
                FE_Data_Processed d2 = new FE_Data_Processed();
                d2.user_id = d.user_id;
                d2.merchant_id = d.merchant_id;
                d2.features = activity_log2feature(d.merchant_id,d.activity_log);
                d2.label = d.label;
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
*/