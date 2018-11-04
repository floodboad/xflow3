--查找流程
select * from XFLOW_PROCESS where 1=1 
<#if searchKey??>
	and (PROCESS_CODE like :searchKey or PROCESS_NAME like :searchKey or DESCRIPTION like :searchKey) 
</#if>
<#if status??>
	and STATUS = :status
</#if>
