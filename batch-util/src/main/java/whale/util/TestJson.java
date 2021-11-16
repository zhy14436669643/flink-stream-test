package whale.util;

import com.alibaba.fastjson.JSONObject;

import java.io.*;

public class TestJson {
    public static void main(String[] args) {
        try {
            String inPath = args[0];
            String outPath = args[1];
            System.out.println(inPath+"\n"+outPath);
            FileReader fr = new FileReader(inPath);
            BufferedReader br = new BufferedReader(fr);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outPath, false)));
            String line;
            String sink;
            while ((line = br.readLine()) != null) {
                JSONObject jsonObject = JSONObject.parseObject(line);
                String member_code = jsonObject.getString("member_code");
                jsonObject.put("member_code","test-" + member_code);
                sink = jsonObject.toJSONString()+"\n";
                out.write(sink);
//                System.out.println(sink);
            }
            fr.close();
            br.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
