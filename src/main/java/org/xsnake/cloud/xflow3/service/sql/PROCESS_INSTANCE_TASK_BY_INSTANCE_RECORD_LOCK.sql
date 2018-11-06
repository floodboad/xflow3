SELECT * FROM XFLOW_PROCESS_INSTANCE_TASK_V 
WHERE RECORD_ID = (select record_Id from XFLOW_PROCESS_INSTANCE_TASK where task_id = :taskId) 
and TASK_TYPE = 'normalTask' for update 