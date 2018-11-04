--查找可以驳回的流程节点
select r.* , h.operator_id,h.operator_name,h.operator_type ,h.suggestion ,h.operate_date,h.task_type 
from XFLOW_PROCESS_INSTANCE_RECORD r ,xflow_process_instance_history h 
where r.record_id = h.record_id and task_type = 'normalTask' 
and operate_type = 'COMPLETE' 
and r.PROCESS_INSTANCE_ID = :processInstanceId 
and r.record_id = :recordId
order by r.sn desc
