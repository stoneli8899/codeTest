package stone.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import stone.util.HttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@EnableRetry
public class HttpClienService {

    /**
     * getDataMap
     * @param url
     * @param keyWord
     * @return
     */
    @Retryable(value = Exception.class,maxAttempts = 3,backoff = @Backoff(multiplier = 1,value = 1000L,maxDelay = 1000L))
    public String getDataMap(String url,String keyWord) throws Exception{
        /* This is a case:
         //Get the province code of China
         //http://www.weather.com.cn/data/city3jdata/china.html
         //Get the city code of one certain province, ‘10119’ is ‘province code’
         //http://www.weather.com.cn/data/city3jdata/provshi/10119.html
         //Get the county code of one certain city,1011904’ is ‘province code + city code’
         //http://www.weather.com.cn/data/city3jdata/station/1011904.html
         //Get the weather of one certain county
         //http://www.weather.com.cn/data/sk/101190401.html
         //‘101190401’ is ‘province code + city code + county code.’
         */
        System.out.println("url:"+url+",keyWord:"+keyWord);
        //try {
            String resp = HttpClient.doGet(url);
            if(resp==null) throw new RuntimeException("网络异常");
            Map<String,String> mapObj = JSONObject.parseObject(resp, Map.class);
            if(mapObj==null) throw new RuntimeException("数据解析异常");
            //System.out.println(mapObj);
            Map<String, String> newMap = new HashMap<String, String>();
            mapObj.entrySet().stream().forEach(x->newMap.put(x.getValue(),x.getKey()));
            //System.out.println(newMap);
            String code= newMap.get(keyWord);
            //如果code获取不到，将使用最小的code来替代
            if(code==null){
                Optional<Map.Entry<String, String>> min = newMap.entrySet().stream().min(
                        (e1, e2)->Integer.compare(Integer.parseInt(e1.getValue()),Integer.parseInt(e2.getValue())));
                code= min.get().getValue();
            }
            System.out.println("code="+code);
            return code;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        //return null;
    }

    /**
     * getTempData
     * @param url
     * @return
     */
    @Retryable(maxAttempts = 3,backoff = @Backoff(multiplier = 1,value = 1000L,maxDelay = 1000L))
    public String getTempData(String url) throws Exception{
        System.out.println("url:"+url);
       try {
            String resp = HttpClient.doGet(url);
            if(resp==null) throw new RuntimeException("网络异常");
            Map<String,Map<String,String>> mapObj = JSONObject.parseObject(resp, Map.class);
            if(mapObj==null) throw new RuntimeException("数据解析异常");
            Map<String,String> infos = mapObj.get("weatherinfo");
            String temp= String.valueOf(infos.get("temp"));
            if(temp.indexOf(".")>0) temp=temp.substring(0,temp.indexOf("."));
            return temp;
        }catch (Exception e){
            //e.printStackTrace();
            throw new RuntimeException("网络调用解析异常");
        }
        //return null;
    }
}
