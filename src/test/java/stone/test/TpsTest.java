package stone.test;

import stone.tps.SpeedControlHelper;
import stone.tps.TaskDO;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 测试类
 *
 * @author <a href="mailto:zq_dong@sina.cn">zhangqi.dzq</a>
 * @version 1.0
 * @since 2015年12月29日
 */
public class TpsTest {
    public static ThreadPoolExecutor executer = new ThreadPoolExecutor(4, 4, 4, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(500));

    public static void main(String[] args) throws Exception {
        while (true) {
            TaskDO task = new TaskDO("calcu_item_value");
            if (!SpeedControlHelper.speedControl(task.getTaskType())) {
                print("被限流..");
                continue;
            }
            executer.submit(task);
            print("添加任务成功");
            Thread.sleep(10);
        }
    }

    public static void print(Object obj) {
        System.out.print(obj.toString());
    }
}