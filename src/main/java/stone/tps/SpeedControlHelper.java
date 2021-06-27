package stone.tps;

/**
 * 单机TPS限流功能类
 *
 */
public class SpeedControlHelper {

    /**
     * 判断是否被限流
     *
     * @param taskType
     * @return false限流
     */
    public static boolean speedControl(String taskType) {
        int tpsThreshod = SpeedControlConstant.serverTps;// tps阀值
        int currentTps = currentTps(taskType);// 当前tps
        if (tpsThreshod <= currentTps) {
            return false;
        }
        return true;
    }

    /**
     * 获取当前任务类型tps
     *
     * @param taskType
     * @return
     */
    public static int currentTps(String taskType) {
        TaskStateSingleton taskStateSingleton = TaskStateSingleton.getInstance();
        TaskStateDO currentTask = taskStateSingleton.getTaskStateDO(taskType);
        if (currentTask == null) {
            // 这里有并发问题，但是统计tps不需要特别精准。添加了并发控制反而会影响性能
            currentTask = new TaskStateDO();
            taskStateSingleton.putTaskStateDO(taskType, currentTask);
        }
        return currentTask.calcuTps();
    }

    /**
     * 设置当前任务类型执行时间
     *
     * @param taskType
     * @param time
     */
    public static void setExecTime(String taskType, long time) {
        TaskStateSingleton taskStateSingleton = TaskStateSingleton.getInstance();
        TaskStateDO currentTask = taskStateSingleton.getTaskStateDO(taskType);
        if (currentTask == null) {
            // logger..
            return;
        }
        currentTask.state(time);
    }
}