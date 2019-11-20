8390		
EXEC fnd_global.APPS_INITIALIZE(8390,51996,200299);

SELECT fnd_global.employee_id
FROM DUAL;

SELECT *
FROM xxup_per_ps_inst_obj_cat main
WHERE sequence_no = '276'
;
WHERE sequence_no = 8;  


SELECT lower(object_name)
FROM all_objects
WHERE 1=1
AND LOWER(object_name) LIKE '%xxup%ps%inst%'
--AND object_type = 'TABLE'
;




WITH profile_v AS (SELECT user_id, 'UNIT' access_level
                   FROM fnd_user 
                   WHERE user_name IN (SELECT * 
                                       FROM TABLE(xxup_ps_inst_wf_pkg.get_users_per_unit(fnd_global.user_id))
                                       )
                   UNION ALL
                   SELECT user_id , 'CU' access_level
                   FROM fnd_user 
                   WHERE user_name IN (SELECT * 
                                       FROM TABLE(xxup_ps_inst_wf_pkg.get_users_per_cu(fnd_global.user_id))
                                       )
                   UNION ALL
                   SELECT user_id, 'ALL' access_level
                   FROM fnd_user
)
SELECT main.sequence_no
      ,main.constituent_university cu
      ,office.office
      ,office.tel_no
      ,office.email_address
      ,main.sequence_no
      ,main.project_name
      ,main.project_leader
      ,mem.full_name mem_full_name
      ,mem.position mem_position
      ,mem.organization mem_organization
      ,mem.project_role mem_project_role
      ,non_mem.full_name non_mem_full_name
      ,non_mem.position non_mem_position
      ,non_mem.organization non_mem_organization
      ,non_mem.project_role non_mem_project_role
      ,to_char(main.implementation_start_date, 'Mon DD, YYYY') implementation_start_date
      ,to_char(main.implementation_end_date, 'Mon DD, YYYY') implementation_end_date
      ,status
      ,NVL2(duration, duration || ' ' || duration_unit, '') duration
      ,main.implementation_frequency
      ,obj_cat.objective_category
      ,obj_cat.specifics
      ,toa.type_of_activity
      ,act.activity
      ,del_mode.delivery_mode
      ,subj.subject_interest
      ,main.funding_agency
      ,main.cost_of_participation
      ,main.unit_of_beneficiary 
      ,part_org.organization_name partner_org
      ,part_org.location partner_org_loc
      ,main.no_of_beneficiary
      ,benef_type.type_of_beneficiary
      ,main.no_of_beneficiary
      ,main.male_benef_count
      ,main.female_benef_count
      ,cou.country
      ,addr.address
      ,main.post_act_eval_rating
      ,main.remarks
--      ,profile_v.*
FROM xxup.xxup_per_ps_institutional main
LEFT JOIN (SELECT LISTAGG(objective_category, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) objective_category
                           ,specifics
                           ,sequence_no
                           ,line_number
            FROM xxup.xxup_per_ps_inst_obj_cat obj
             ) obj_cat
    ON obj_cat.sequence_no = main.sequence_no
    AND obj_cat.line_number = 1 -- get one line only
LEFT JOIN (SELECT LISTAGG(office, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) office
                 ,LISTAGG(tel_no, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) tel_no
                 ,LISTAGG(email_address, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) email_address
                    ,sequence_no
                    ,line_number
       FROM xxup.xxup_per_ps_inst_office office
       ) office
    ON office.sequence_no = main.sequence_no
    AND office.line_number = 1
LEFT JOIN (SELECT LISTAGG(full_name, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) full_name
                 ,LISTAGG(position, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) position
                 ,LISTAGG(organization, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) organization
                 ,LISTAGG(project_role, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) project_role
                 ,sequence_no
                 ,line_number
       FROM xxup.xxup_per_ps_inst_members mem
       WHERE attribute1 IS NULL --exclude non-members
       ) mem
    ON mem.sequence_no = main.sequence_no
    AND mem.line_number = 1
LEFT JOIN (SELECT LISTAGG(full_name, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) full_name
                 ,LISTAGG(position, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) position
                 ,LISTAGG(organization, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) organization
                 ,LISTAGG(project_role, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) project_role
                 ,sequence_no
                 ,line_number
       FROM xxup.xxup_per_ps_inst_members non_mem
       WHERE attribute1 = 'Non-UP' --non-members
       ) non_mem
    ON non_mem.sequence_no = main.sequence_no
    AND non_mem.line_number = 1
LEFT JOIN (SELECT LISTAGG(attribute1, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) type_of_activity
                    ,sequence_no
                    ,line_number
           FROM xxup.xxup_per_ps_inst_toa toa
           ) toa
    ON toa.sequence_no = main.sequence_no
    AND toa.line_number = 1
LEFT JOIN (SELECT LISTAGG(activity, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) activity
                    ,sequence_no
                    ,line_number
           FROM xxup.xxup_per_ps_inst_activities act
           ) act
    ON act.sequence_no = main.sequence_no
    AND act.line_number = 1
LEFT JOIN (SELECT LISTAGG(delivery_mode, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) delivery_mode
                    ,sequence_no
                    ,line_number
           FROM xxup.xxup_per_ps_inst_delivery_mode del_mode
           ) del_mode
    ON del_mode.sequence_no = main.sequence_no
    AND del_mode.line_number = 1
LEFT JOIN (SELECT LISTAGG(attribute1, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) subject_interest
                    ,sequence_no
                    ,line_number
           FROM xxup.xxup_per_ps_inst_subj subj
           ) subj
    ON subj.sequence_no = main.sequence_no
    AND subj.line_number = 1  
LEFT JOIN (SELECT LISTAGG(organization_name, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) organization_name
                 ,LISTAGG(location, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) location
                    ,sequence_no
                    ,line_number
           FROM xxup.xxup_per_ps_inst_part_org_tr part_org
           ) part_org
    ON part_org.sequence_no = main.sequence_no
    AND part_org.line_number = 1  
LEFT JOIN (SELECT LISTAGG(type_of_beneficiary, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) type_of_beneficiary
                    ,sequence_no
                    ,line_number
           FROM xxup.xxup_per_ps_inst_benef_type benef_type
           ) benef_type
    ON benef_type.sequence_no = main.sequence_no
    AND benef_type.line_number = 1  
LEFT JOIN (SELECT LISTAGG(country, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) country
                    ,sequence_no
                    ,line_number
           FROM xxup.xxup_per_ps_inst_countries cou
           ) cou
    ON cou.sequence_no = main.sequence_no
    AND cou.line_number = 1  
LEFT JOIN (SELECT LISTAGG(address, chr(10)) WITHIN GROUP(ORDER BY line_number) OVER(PARTITION BY sequence_no) address
                    ,sequence_no
                    ,line_number
           FROM xxup.xxup_per_ps_inst_addr addr
           ) addr
    ON addr.sequence_no = main.sequence_no
    AND addr.line_number = 1
INNER JOIN profile_v
    ON profile_v.access_level = FND_PROFILE.value('XXUP_PER_PS_SUMMARY_ACCESS_LEVEL')
    AND profile_v.user_id = main.created_by
WHERE (implementation_start_date >= &P_START_DATE AND --required and has default value
        NVL(implementation_end_date, to_date('01-JAN-0001','DD-MON-YYYY')) <= NVL(&P_END_DATE,to_date('31-DEC-4712','DD-MON-YYYY'))
    )
AND main.status = NVL(&P_STATUS, main.status)
AND subj.subject_interest LIKE '%' || NVL(&P_SUBJECT_INTEREST, subj.subject_interest) || '%'
AND obj_cat.objective_category LIKE '%' || NVL(&P_OBJ_CAT, obj_cat.objective_category) || '%'
AND benef_type.type_of_beneficiary LIKE '%' || NVL(&P_BENEF_TYPE, benef_type.type_of_beneficiary) || '%'

;
        
AND 
;

SELECT FND_PROFILE.value('XXUP_PER_PS_SUMMARY_ACCESS_LEVEL') access_level FROM DUAL;

   

SELECT NVL(,'')
FROM DUAL;

DEFINE P_START_DATE = to_date('01-JAN-1908','DD-MON-YYYY');
DEFINE P_END_DATE = to_date('01-JAN-2019','DD-MON-YYYY');
DEFINE P_STATUS = to_char('Ongoing');
DEFINE P_SUBJECT_INTEREST = NULL;
DEFINE P_OBJ_CAT = NULL;
DEFINE P_BENEF_TYPE = null;

--
-- to_char('Human Kinetics');
-- to_char('To increase');



--to_char('Agri-forestry');


SELECT *
FROM xxup_per_ps_inst_addr;


SELECT FND_PROFILE.value('XXUP_PER_PS_SUMMARY_ACCESS_LEVEL') access_level FROM DUAL ;



