package stone.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClient {
    /*
     * 实现get类型接口的调用
     */
    public static String doGet(String url) throws Exception {
        //创建get对象
        HttpGet get = new HttpGet(url);
        //创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //提交请求
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(get);
            //获取状态码及响应数据
            //int status = response.getStatusLine().getStatusCode();
            //System.out.println("状态码为：" + status);
            String result = EntityUtils.toString(response.getEntity(),"utf-8");
            //System.out.println("响应数据为：" + result);
            return result;
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }finally {
            if (response != null) {
                response.close();
            }
            //相当于关闭浏览器
            httpclient.close();
        }
        return null;
    }

}
