package stone.test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import stone.TemperatureController;

import javax.swing.text.html.Option;
import java.util.Optional;

public class TemperatureTest {

    @DataProvider
    public static Object [][] datas(){
        return new Object[][]{
                {"江苏","苏州","苏州","23"},
                {"江苏","广州","广州","21"}, //江苏-南京
                {"aa","bb","cc","null"}  //运行3次
        };
    }

    /**
     * 测试获取温度
     * @param province
     * @param city
     * @param county
     * @param response
     * @throws Exception
     */
    @Test(dataProvider="datas")
    public static void test(String province, String city, String county,String response) throws Exception {
        System.out.println("province:"+province+",city:"+city+",county:"+county+",response:"+response);
        //TemperatureController controller = new TemperatureController();
        //Optional<Integer> resp = controller.getTemperature(province,city,county);
        try{
            TemperatureController controller = new TemperatureController();
            Optional<Integer> temp = controller.getTemperature(province,city,county);
           if(temp.isPresent()) {
               Assert.assertEquals(temp, Optional.of(Integer.parseInt(response)));
           }else {
               Assert.assertEquals(temp, Optional.<Integer>empty());
           }

        }catch (Exception e){
            e.printStackTrace();
        }
        //if(resp==null) throw new RuntimeException("温度获取异常");

    }



}
