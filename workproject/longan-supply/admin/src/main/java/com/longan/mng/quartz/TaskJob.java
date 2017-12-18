package com.longan.mng.quartz;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.core.TaskService;
import com.longan.biz.dataobject.Task;
import com.longan.biz.domain.Result;

public class TaskJob {
	protected final Logger logger = LoggerFactory.getLogger(TaskJob.class);

	@Resource
	private TaskService taskService;

	public void submit() {
		logger.warn("task Job is start");
		Result<List<Task>> result = taskService.queryTaskListUnDone();
		if (!result.isSuccess()) {
			logger.error("TaskJob queryTaskListUnDone error " + result.getResultMsg());
			return;
		}

		List<Task> TaskList = result.getModule();
		if (TaskList != null) {
			for (Task task : TaskList) {
				logger.warn("submitTask  id : " + task.getId());
				taskService.commitTask(task);
			}
		}
	}
}
