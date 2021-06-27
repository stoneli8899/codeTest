package stone.tps;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 统计全量任务单例
 *
 */
public class TaskStateSingleton {
    private Map<String, TaskStateDO> taskStateMap = new HashMap<String, TaskStateDO>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private Lock read = lock.readLock();

    private Lock write = lock.writeLock();

    private TaskStateSingleton() {
    }

    // 获取实例
    public static TaskStateSingleton getInstance() {
        return SingletonHolder.taskStateSingleton;
    }

    private static class SingletonHolder {
        private static final TaskStateSingleton taskStateSingleton = new TaskStateSingleton();
    }

    public Map<String, TaskStateDO> getTaskStateMap() {
        try {
            read.lock();
            return taskStateMap;
        } finally {
            read.unlock();
        }
    }

    /**
     * 新增一个任务统计信息
     */
    public void putTaskStateDO(String taskType, TaskStateDO taskStateDO) {
        try {
            write.lock();
            taskStateMap.put(taskType, taskStateDO);
        } finally {
            write.unlock();
        }
    }

    /**
     * 查询一个任务统计信息
     */
    public TaskStateDO getTaskStateDO(String taskType) {
        try {
            read.lock();
            return taskStateMap.get(taskType);
        } finally {
            read.unlock();
        }
    }

}