package Feature_Extraction.Predefined;

import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by yice.zwl on 2015/5/7.
 */
public class FE_Data_Raw implements Writable {
    public String user_id;
    public String merchant_id;
    public String age_range;
    public String gender;
    public String label;
    public String activity_log;


    public void readFields(DataInput in) throws IOException {
        Text text = new Text();
        text.readFields(in);
        user_id = text.toString();

        text = new Text();
        text.readFields(in);
        merchant_id = text.toString();

        text = new Text();
        text.readFields(in);
        age_range = text.toString();

        text = new Text();
        text.readFields(in);
        gender = text.toString();

        text = new Text();
        text.readFields(in);
        label = text.toString();

        text = new Text();
        text.readFields(in);
        activity_log = text.toString();
    }

    public void write(DataOutput out) throws IOException {
        (new Text(user_id)).write(out);
        (new Text(merchant_id)).write(out);
        (new Text(age_range)).write(out);
        (new Text(gender)).write(out);
        (new Text(label)).write(out);
        (new Text(activity_log)).write(out);
    }

}
