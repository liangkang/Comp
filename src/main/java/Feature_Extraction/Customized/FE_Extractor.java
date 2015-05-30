package Feature_Extraction.Customized;

import Feature_Extraction.Predefined.FE_Data_Processed;
import Feature_Extraction.Predefined.FE_Data_Raw;

import java.util.*;

/**
 * Created by yice.zwl on 2015/5/7.
 */
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
        for(int rank=0;rank<6;rank++)
            for(int month=5;month<=11;month++)
                for(int action=0;action<=3;action++) {
                    map.put(rank+"|"+month+"|"+action,count);
                    count++;
                }
        return map;
    }

    public static String activity_log2feature(int rank,String merchant_id, String activitiy_log){
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

    //convert the data of format 2 to your desire format,
    //data_raw_list the list of raw data
    //auxiliary_data is any auxilary data you needed, which is stored in table "auxi_table"
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
        for(int i=0;i<auxilary_data.size();i++){
            String [] strs = auxilary_data.get(i).split(",");
            List<String> list =  new ArrayList<String>(num_similar_merchants);

            for (int j = 0; j < 5; j++)
                list.add(j, strs[j + 1]);

            similarity_merchants.put(strs[0],list);
        }

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
                data_list.add(d2);
            }
        }
        System.out.println(data_list.size());
        return data_list;
    }
}
