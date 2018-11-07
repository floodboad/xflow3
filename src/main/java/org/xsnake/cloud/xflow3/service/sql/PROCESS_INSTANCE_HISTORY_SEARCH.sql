select 
p.PROCESS_INSTANCE_ID,
p.PROCESS_CODE,
p.VERSION,
p.BUSINESS_KEY,
p.NAME,
p.STATUS,
p.PARENT_ID,
p.START_DATE,
p.CREATOR_ID,
p.CREATOR_NAME,
p.CREATOR_TYPE,
p.PARENT_ACTIVITY_ID,
p.FORK_STATUS,
p.BUSINESS_TYPE,
p.BUSINESS_URL,
h.operator_id,
h.operator_name,
h.operator_type
from xflow_process_instance_history h,
xflow_process_instance p,
XFLOW_PROCESS_INSTANCE_RECORD r 
where h.record_id = r.record_id 
and p.process_instance_id = r.process_instance_id
<#if operatorId??>
	and h.operator_id = :operatorId
	and h.operator_type = :operatorType
</#if>
<#if creatorId??>
	and p.CREATOR_ID = :creatorId
	and p.CREATOR_TYPE = :creatorType
</#if>
<#if searchKey?? && searchKey != ''>
    and p.NAME like :searchKey 
</#if>