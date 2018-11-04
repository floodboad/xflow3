delete from XFLOW_PROCESS_INSTANCE_TASK t
where t.record_id = (select r.record_id from XFLOW_PROCESS_INSTANCE_RECORD r where r.record_Id = t.RECORD_ID and r.PROCESS_INSTANCE_ID = :processInstanceId) 