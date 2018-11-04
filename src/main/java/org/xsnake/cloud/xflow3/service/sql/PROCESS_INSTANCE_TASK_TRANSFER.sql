
 update XFLOW_PROCESS_INSTANCE_TASK set 
 PARTICIPANT_ID = :participantId ,
 PARTICIPANT_NAME = :participantName , 
 PARTICIPANT_TYPE = :participantType
 where TASK_ID = :taskId