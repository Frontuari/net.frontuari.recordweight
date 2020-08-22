--select name from ad_field where ad_tab_id=1000039 and  ad_column_id= 1000398;

delete from AD_Process where AD_Process_ID=1000027;
delete from AD_Workflow where AD_Workflow_ID=1000002;
delete from ad_field where  ad_tab_id= 1000046 and ad_column_id= 1001025;
delete from ad_field where  ad_tab_id= 1000046 and ad_column_id= 1001024;

--select name from ad_tab where ad_tab_id= 1000046 ;
--select name from ad_field where ad_tab_id= 1000046 and ad_column_id= 1001024;


--select Name  from AD_Menu where AD_Menu_ID=1000030;


delete from AD_Menu where AD_Menu_ID=1000030;
delete from AD_Field where  AD_Tab_ID=1000040;
delete from AD_Tab where  AD_Tab_ID=1000040;
delete from AD_Menu where AD_Window_ID=1000018;
delete from AD_Window where AD_Window_ID=1000018;
delete from AD_Column where  AD_Table_ID=1000019;
delete from AD_Table where AD_Table_ID=1000019;

delete from AD_Field where ad_column_id=1000391;
delete from AD_Column where AD_Element_ID=1000097;
delete from AD_Element where AD_Element_ID=1000097;

delete from  AD_Field where  AD_Field_ID=1000959;
delete from  AD_Column where AD_Column_ID=1000596;
delete from AD_Field where ad_column_id=1000956;



delete from AD_Field where AD_Column_ID=1000990;

delete from AD_Column where AD_Element_ID=1000135;
delete from  AD_Element where AD_Element_ID=1000135;
delete from  AD_Field where AD_Field_ID=1001494;

delete from AD_Column where AD_Column_ID=1002442;
delete from  AD_Element where AD_Element_ID=1000285;

drop table hrs_analysis;




