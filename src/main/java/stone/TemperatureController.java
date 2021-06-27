package stone;


import javafx.beans.binding.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import stone.service.HttpClienService;
import stone.util.RetryTemplate;

import java.util.Date;
import java.util.Optional;

/**
    @author stone

     //The method signature is `public Optional<Integer> getTemperature(String province, String city, String county)`
     // If the location is invalid, return reasonable value.
     //Add reasonable retry mechanism, cause there might be connection exception when calling the weather API
     //The method need block invocation if the TPS(transactions per second) is more than 100.

     //JDK 1.8+ Project (Stream API is required)
     //Build with Maven.
     //Cover all above function requirements.
     //Unit Test (Including both Boundary tests and Exception tests. Success with Junit Assertations.
 */

public class TemperatureController {

    //TODO retry times
    //TODO calculate TPS
    //TODO Unit Test
    @Autowired
    private HttpClienService httpClienService;

    public static void main(String[] args) {
        try{
            TemperatureController controller = new TemperatureController();
            controller.getTemperature("江苏","苏州","苏州");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Optional<Integer> getTemperature(String province, String city, String county){
        try {
            Object ans = new RetryTemplate() {
                protected Object doBiz() throws Exception {
                   Integer temperature = callGetTemperatureFun(province, city, county);
                    return temperature;
                }
            }.setRetryTime(3).setSleepTime(1000).execute(); //When an exception occurs, retry again 2 times

            return Optional.ofNullable((Integer)ans);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    /**
     * getTemperature
     * @param province
     * @param city
     * @param county
     * @return
     */
    public Integer callGetTemperatureFun(String province, String city, String county) throws Exception{
        String[] urls = new String[]{"http://www.weather.com.cn/data/city3jdata/china.html",
                "http://www.weather.com.cn/data/city3jdata/provshi/",
                "http://www.weather.com.cn/data/city3jdata/station/",
                "http://www.weather.com.cn/data/sk/"};

            HttpClienService httpClienService = new HttpClienService();
            String provinceCode = httpClienService.getDataMap(urls[0], province);
            String cityCode = httpClienService.getDataMap(urls[1] + provinceCode + ".html", city);
            String countyCode = httpClienService.getDataMap(urls[2] + provinceCode + cityCode + ".html", county);
            String temperature = httpClienService.getTempData(urls[3] + provinceCode + cityCode + countyCode + ".html");
            System.out.println(temperature);
            Integer temp = null;
            if(temperature!=null) temp=Integer.parseInt(temperature);
            return temp;
    }


}
