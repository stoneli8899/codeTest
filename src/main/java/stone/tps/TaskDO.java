package stone.tps;


import stone.TemperatureController;

/**
 * 任务
 *
 */
public class TaskDO implements Runnable {
    private String taskType;

    public TaskDO(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskType() {
        return taskType;
    }

    public void run() {
        try {
            long startTime = System.currentTimeMillis();

            TemperatureController controller = new TemperatureController();
            controller.getTemperature("江苏","苏州","苏州");

            long endTime = System.currentTimeMillis();
            SpeedControlHelper.setExecTime(taskType, endTime - startTime);
        } catch (Exception e) {
            // logger..
        }
    }

}