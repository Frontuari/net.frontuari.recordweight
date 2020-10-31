DELETE FROM  AD_WF_Node WHERE AD_Workflow_ID=1000003 AND IsActive ='N';
DELETE FROM  AD_WF_Node WHERE AD_Workflow_ID=1000003 AND EntityTYpe='FTU';
ALTER TABLE FTU_RecordWeight DROP COLUMN IF EXISTS  IsSkipSensors;
ALTER TABLE FTU_RecordWeight DROP COLUMN IF EXISTS   LineDescription;
-- ALTER TABLE FTU_RecordWeight DROP COLUMN IF EXISTS  FTU_Driver_ID;
-- ALTER TABLE FTU_RecordWeight DROP COLUMN IF EXISTS  FTU_Vehicle_ID;
-- ALTER TABLE FTU_RecordWeight  DROP COLUMN IF EXISTS  TrailerPlate;
ALTER TABLE FTU_WeightScale  DROP COLUMN IF EXISTS  FTU_CommProtocolConfig_ID;

DELETE 
-- SELECT Name, AD_Field_UU
FROM  AD_Field
WHERE AD_Field_UU IN(
	'c915f6ed-38d5-4521-b27d-7ace95308b0a' --FTU_CommProtocolConfig_ID  
	-- ,'e02c0203-c182-4ab7-baad-67573b4e63c0' -- TrailerPlate 
	,'cfd2a22d-93d2-4d80-985c-cec261bed8b1' --Typology 
	,'0d4a8a83-bee0-4481-84a5-747f7d9c2a77' --Typology 
	,'4ec854b5-d116-42bb-b930-7d90c95b7277' --Typology 
	-- ,'9dcb4968-eabb-4ec1-a161-effa96bad441' -- FTU_Vehicle_ID 
	,'31b4b33a-1b7e-43d3-b567-484e587ac45d' --isskipsensors 
	,'d472fea2-590f-4a1e-b7f3-04a6fc9566cc' --LineDescription 
	-- ,'06f7da55-024b-4c4b-9967-015a48772c60' -- FTU_Driver_ID 
	-- , '28ed1e3d-c445-4e5d-9fed-5744b88b1619' --M_Shipper_ID 
	);

DELETE 
-- SELECT ColumnName, AD_Column_ID, AD_Column_UU
FROM AD_Column 
WHERE AD_Column_UU IN(
	'63d3a04b-bead-4da5-bcd5-cf7f865e23e7' --FTU_CommProtocolConfig_ID
	-- ,'ce2c586e-13ff-4785-a514-a5c4dfee4973' --TrailerPlate
	,'3133ec15-96b3-4c25-a0bc-c21ba8e7cfcf' --HRS_Typology_ID
	-- ,'67b08e12-1739-4edc-ad76-479dcecd2205'/*FTU_Vehicle_ID*/
	,'9d8164a5-8635-464f-8a8f-89d9d20c4671' --isskipsensors
	,'1fc5ac86-e25a-492d-98c2-59fc4513a040' --LineDescription
	-- ,'dcfed5b0-57cd-4898-9511-aaf9bf2bb7aa' -- FTU_Driver_ID
	-- , '60c46b03-1045-45a9-9370-e5e7696a7653' --M_Shipper_ID
);



DELETE
-- SELECT ColumnName, AD_Element_ID, AD_Element_UU
FROM  AD_Element 
WHERE AD_Element_UU IN(
	'd703060e-5b86-44ab-bf89-4101a0fc3ba6'--FTU_CommProtocolConfig_ID
	,'8b263182-4ee9-4c97-91a2-27f32f4d5682' --HRS_Typology_ID
	, '014bfe49-dcbf-41d2-a5d0-e70d4660fc7d' -- isskipsensors
);

UPDATE AD_WF_Node SET AD_Client_ID = 1000000 WHERE  AD_WF_Node_UU='62c5fbc9-2c4b-4bd1-a7c5-170d87e66011';

CREATE TABLE FTU_Weight (
	FTU_WeightScale_ID NUMERIC(10) NOT NULL,
	Time_Read TIMESTAMP NOT NULL,
	Value NUMERIC(10,2) NOT NULL,
	IsActive CHAR(1) NOT NULL,
	Name VARCHAR (60) NOT NULL,
	CONSTRAINT FTU_Weight_PKey PRIMARY KEY (FTU_WeightScale_ID),
	CONSTRAINT FTU_Weight_FKey FOREIGN KEY (FTU_WeightScale_ID) REFERENCES FTU_WeightScale(FTU_WeightScale_ID)
);

/*
UPDATE AD_Workflow SET AD_Client_ID = 0 WHERE AD_Workflow_ID=1000003;
UPDATE  AD_WF_Node SET AD_Client_ID = 0 WHERE AD_Workflow_ID=1000003 AND AD_WF_Node_ID<>1000047;
UPDATE  AD_WF_NodeNext SET AD_Client_ID = 0 
WHERE EXISTS(
	SELECT 1 
	FROM AD_WF_Node
	WHERE
		AD_Workflow_ID=1000003
		AND AD_WF_NodeNext.AD_WF_Node_ID = AD_WF_Node.AD_WF_Node_ID AND AD_WF_Node_ID<>1000047);

	
UPDATE  AD_WF_NodeNext SET AD_Client_ID = 1000000 WHERE  AD_WF_NodeNext_ID=1000033
	
UPDATE AD_WF_NextCondition SET AD_WF_NodeNext_ID=1000034 WHERE AD_WF_NextCondition_ID =1000004


 AD_WF_NextCondition
 AD_WF_NextCondition_ID

 UPDATE  AD_WF_NextCondition SET AD_Client_ID = 1000000 
 WHERE AD_WF_NextCondition_ID=1000000
 
 
 SELECT AD_Column_ID,ColumnName FROM AD_Column WHERE ColumnName LIKE 'FTU_RW_A%'
 
 
 SELECT TableName FROM AD_Table WHERE AD_Table_ID = 1000039
 */
 
