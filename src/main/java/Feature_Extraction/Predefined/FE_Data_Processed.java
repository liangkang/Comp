package Feature_Extraction.Predefined;

import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by yice.zwl on 2015/5/7.
 */
public class FE_Data_Processed implements Writable {
    public String user_id;
    public String merchant_id;
    public String features;
    public String label;

    public void readFields(DataInput in) throws IOException {
        Text text = new Text();
        text.readFields(in);
        user_id = text.toString();

        text = new Text();
        text.readFields(in);
        merchant_id = text.toString();

        text = new Text();
        text.readFields(in);
        features = text.toString();


        text = new Text();
        text.readFields(in);
        label = text.toString();

    }

    public void write(DataOutput out) throws IOException {
        (new Text(user_id)).write(out);
        (new Text(merchant_id)).write(out);
        (new Text(features)).write(out);
        (new Text(label)).write(out);
    }
}
