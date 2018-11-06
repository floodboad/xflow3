select * from (
<#list participantList as participant>
	<#if participant_index gt 0>
		union
	</#if>
	SELECT * FROM XFLOW_PROCESS_INSTANCE_TASK_V WHERE STATUS = 'RUNNING' and participant_id = :participantId_${participant_index} and participant_type = :participantType_${participant_index}
</#list>
) order by create_date desc , sn 