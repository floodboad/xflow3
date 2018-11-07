INSERT INTO XFLOW_PROCESS_INSTANCE 
(PROCESS_INSTANCE_ID,PROCESS_CODE,VERSION,BUSINESS_KEY,NAME,STATUS,
PARENT_ID,START_DATE,CREATOR_ID,CREATOR_NAME,
CREATOR_TYPE,PARENT_ACTIVITY_ID,BUSINESS_FORM,FORK_STATUS,BUSINESS_TYPE,BUSINESS_URL ) 
VALUES (
:processInstanceId,:processCode,:version,:businessKey,:name,:status,:parentId,:startDate,:creatorId,:creatorName,
:creatorType,:parentActivityId,:businessForm,'0',:businessType,:businessUrl
)