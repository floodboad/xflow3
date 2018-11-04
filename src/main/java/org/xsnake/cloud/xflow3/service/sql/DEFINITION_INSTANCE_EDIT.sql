UPDATE XFLOW_DEFINITION_INSTANCE SET 
REMARK = :remark , 
LAST_UPDATE_DATE = :lastUpdateDate,
XML = :xml, 
NAME = :name
WHERE CODE = :code 
AND VERSION = :version
AND STATUS = 'NEW'


