create or replace PACKAGE BODY xxup_ps_inst_wf_pkg
IS

    FUNCTION create_item_key(p_seq_no        IN VARCHAR2
                            ,p_action        IN VARCHAR2                
                            )
    RETURN VARCHAR2
    IS
        lv_item_key wf_notifications.item_key%TYPE;

        lv_exist VARCHAR2(1) := 'N';
        e_exists EXCEPTION;

        lv_last_update_item_key wf_notifications.item_key%TYPE;
        ln_last_update_ctr VARCHAR2(1000);
        lv_action_prefix VARCHAR2(1) := '';

    BEGIN
        IF p_action = 'Create' THEN

            lv_item_key := 'INST-' || p_seq_no;

            RETURN lv_item_key;

        ELSIF p_action = 'Update' THEN
            /*Get last update transaction's item key*/

            BEGIN
                SELECT item_key
                INTO lv_last_update_item_key
                FROM (
                    SELECT item_key
    --                FROM xxup_per_ps_action_history
                    FROM xxup.xxup_per_ps_institutional_tr
                    WHERE sequence_no = p_seq_no
                    ORDER BY last_update_date DESC
                )
                WHERE ROWNUM = 1;

                SELECT SUBSTR(lv_last_update_item_key,1,1)
                INTO lv_action_prefix
                FROM DUAL;

                IF lv_action_prefix = 'U' THEN
                    /*increment update item key*/
                    SELECT MAX(SUBSTR(lv_last_update_item_key,-1,1))
                    INTO ln_last_update_ctr
--                    FROM xxup_per_ps_action_history
                    FROM xxup.xxup_per_ps_institutional_tr
                    WHERE sequence_no = p_seq_no
--                    AND type = 'Institutional'
                    AND ROWNUM = 1;


                    RETURN 'U-INST-' || p_seq_no || '-' || to_char(ln_last_update_ctr + 1);
                ELSE --no last update transaction
                    RETURN 'U-INST-' || p_seq_no || '-1';
                END IF;

            EXCEPTION 
                WHEN OTHERS THEN
                    --no last update 
                    NULL;
--                    raise_application_error(-20101, 'Unable to create item key for this transaction');
             END;

             RETURN 'U-INST-' || p_seq_no || '-1';


--            lv_item_key := 'INST-' || lv_item_key || p_seq_no;
        END IF;

    EXCEPTION
--        WHEN e_exists THEN
--            raise_application_error(-20101, 'Unable to create item key for this transaction');
        WHEN OTHERS THEN
            raise_application_error(-20101, 'Unable to create item key for this transaction');
    END create_item_key;

    PROCEDURE set_owner_details(p_assignment_id IN NUMBER
                               ,p_emp_id       OUT NUMBER
                               ,p_emp_name     OUT VARCHAR2
                               ,p_emp_pos_name OUT VARCHAR2
                               ,p_emp_org_name OUT VARCHAR2
                               )
    IS   

    BEGIN
        SELECT  papf.full_name
              ,papf.person_id
              ,(SELECT ppd.segment1
                FROM per_position_definitions ppd
                    ,per_all_positions pap
                 WHERE pap.position_id = paaf.position_id
                 AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
              ,(SELECT haou.name
                FROM per_position_definitions ppd
                    ,per_all_positions pap
                    ,hr_all_organization_units haou
                 WHERE pap.position_id = paaf.position_id
                 AND   ppd.position_definition_id = pap.position_definition_id
                 AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
        INTO p_emp_name
            ,p_emp_id
            ,p_emp_pos_name
            ,p_emp_org_name
        FROM per_all_assignments_f paaf
            ,per_all_people_f papf
        WHERE paaf.person_id = papf.person_id
        AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
        AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
        AND paaf.assignment_id = p_assignment_id;


    EXCEPTION
      WHEN no_data_found THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - data not found!');
      WHEN too_many_rows THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - too many row fetched!');
      WHEN OTHERS THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - ' || SUBSTR(SQLERRM,0,200));
    END;


    PROCEDURE set_owner_details(p_item_key      IN  VARCHAR2
                               ,p_emp_id       OUT NUMBER
                               ,p_emp_name     OUT VARCHAR2
                               ,p_emp_pos_name OUT VARCHAR2
                               ,p_emp_org_name OUT VARCHAR2
                               )
    IS    

    BEGIN
      SELECT  papf.full_name
              ,papf.person_id
              ,(SELECT ppd.segment1
                FROM per_position_definitions ppd
                    ,per_all_positions pap
                 WHERE pap.position_id = paaf.position_id
                 AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
              ,(SELECT haou.name
                FROM per_position_definitions ppd
                    ,per_all_positions pap
                    ,hr_all_organization_units haou
                 WHERE pap.position_id = paaf.position_id
                 AND   ppd.position_definition_id = pap.position_definition_id
                 AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
      INTO p_emp_name
          ,p_emp_id
          ,p_emp_pos_name
          ,p_emp_org_name
        FROM per_all_assignments_f paaf
            ,per_all_people_f papf
            ,xxup.xxup_per_ps_Institutional_tr pptr            
            ,fnd_user fu
        WHERE paaf.person_id = papf.person_id
        AND papf.person_id = fu.employee_id
        AND pptr.created_by = fu.user_id
        AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
        AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
        AND primary_flag = 'Y'
        AND pptr.item_key = p_item_key;

    EXCEPTION
        WHEN no_data_found THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - data not found!');
        WHEN too_many_rows THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - too many row fetched!');
        WHEN OTHERS THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - ' || SUBSTR(SQLERRM,0,200));

    END set_owner_details;


    PROCEDURE init_wf(p_seq_no IN VARCHAR2
                     ,p_item_key  IN VARCHAR2)
    IS
    lv_item_type VARCHAR2(100) := 'XXUPPIWF';
    lv_item_key VARCHAR2(100) := p_item_key;
    lv_process_name VARCHAR2(140) := 'XXUP_PS_PRC'; 

    BEGIN 

        wf_engine.createprocess(lv_item_type
                               ,lv_item_key
                               ,lv_process_name
                               );

        wf_engine.setitemowner(lv_item_type, lv_item_key, fnd_global.user_name);

        wf_engine.setitemattrtext(lv_item_type
                                 ,lv_item_key
                                 ,'OWNER'
                                 ,fnd_global.user_name
                                 );

        wf_engine.setitemattrtext(lv_item_type
                                 ,lv_item_key
                                 ,'SEQUENCE_NO'
                                 ,p_seq_no
                                 );

        wf_engine.startprocess(lv_item_type, lv_item_key);


--        UPDATE xxup.xxup_per_ps_header_tr
--        SET item_key = lv_item_key
--        WHERE sequence_no = p_sequence_no;

        UPDATE xxup.xxup_per_ps_action_history hist
        SET action_date = SYSDATE
        WHERE item_key = lv_item_key
        AND action = 'Submit'
        AND type = 'Institutional'; --employee submission


        COMMIT;

    EXCEPTION
      WHEN OTHERS THEN
        raise_application_error(-20101, 'Encountered error on init_wf - ' || SUBSTR(SQLERRM,0,200));

    END init_wf;


    PROCEDURE set_attributes(itemtype  IN VARCHAR2
                            ,l_itemkey IN VARCHAR2
                            ,actid     IN VARCHAR2
                            ,funcmode  IN VARCHAR2
                            ,resultout OUT VARCHAR2)
    IS
        lv_fyi_title VARCHAR2(1000);
        lv_owner VARCHAR2(100);
        lv_seq_no VARCHAR2(100);

        lv_project_name VARCHAR2(150);

        lv_approver_name VARCHAR2(150);


        lv_sup_name VARCHAR2(100);
        ln_sup_person_id NUMBER;
        ln_sup_assignment_id NUMBER;
        lv_sup_pos_name VARCHAR2(150);

        is_principal_unit VARCHAR2(1) := 'N';

        lv_emp_name VARCHAR2(100);
        ln_emp_id NUMBER;
        lv_emp_pos_name VARCHAR2(150);
        lv_emp_org_name VARCHAR2(150);



        --lv_owner VARCHAR2(100);
        --lv_seq_no VARCHAR2(100);


        /*ln_from_id NUMBER;
        lv_from_name VARCHAR2(100);
        lv_from_pos_name VARCHAR2(150);


        ln_to_id NUMBER;
        lv_to_name VARCHAR2(100);
        lv_to_pos_name VARCHAR2(150);*/


        ln_total_approver_count NUMBER;                    
        ln_approver_ctr NUMBER := 0;
        lv_appr_user_name VARCHAR2(100);

        lv_error VARCHAR2(2000);


    BEGIN


        --for test



        --wf_engine.setitemowner(itemtype, l_itemkey, fnd_global.user_name);


        --for test
        /*wf_engine.setitemattrtext(itemtype
                                 ,l_itemkey
                                 ,'OWNER'
                                 ,fnd_global.user_name
                                 );

        */


        lv_owner := wf_engine.getitemattrtext(itemtype
                                            ,l_itemkey
                                            ,'OWNER'
                                            );


        lv_seq_no := wf_engine.getitemattrtext(itemtype
                                            ,l_itemkey
                                            ,'SEQUENCE_NO'
                                            );

        IF funcmode = 'RUN' THEN

            BEGIN
                SELECT COUNT(1)
                INTO ln_total_approver_count
                FROM xxup.xxup_per_ps_action_history psah
                WHERE item_key = l_itemkey
                AND type = 'Institutional'
                AND action = 'Pending';

                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'TOTAL_APPROVER_COUNT'
                                        ,ln_total_approver_count);

            EXCEPTION 
                WHEN OTHERS THEN
                      NULL;

            END;



            BEGIN
                SELECT project_name
                INTO lv_project_name
                FROM xxup.xxup_per_ps_Institutional_tr pptr
                WHERE item_key = l_itemkey;
            EXCEPTION
                WHEN OTHERS THEN
                    NULL;
            END;

            /*Set 1st approver notification attributes*/

            SELECT full_name
            INTO lv_approver_name
            FROM hr.per_all_people_f papf
            WHERE SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
              AND person_type_id = 1126
              AND papf.person_id = (SELECT to_employee_id
                                    FROM xxup.xxup_per_ps_action_history
                                    WHERE type = 'Institutional'
                                    AND action = 'Pending' --since only 1 approver
--                                    AND approver_no = 1
                                    AND item_key = l_itemkey);


            BEGIN

                BEGIN
                    SELECT user_name
                    INTO lv_appr_user_name
                    FROM fnd_user
                    WHERE employee_id = (SELECT to_employee_id
                                        FROM xxup.xxup_per_ps_action_history
                                        WHERE type = 'Institutional'
                                        AND item_key = l_itemkey
                                        AND action = 'Pending');

                EXCEPTION
                    WHEN OTHERS THEN
                        NULL;
                END;        


--                BEGIN
--                    SELECT project_name
--                    INTO lv_project_name
--                    FROM xxup.xxup_per_ps_Institutional_tr pptr
--                    WHERE sequence_no = lv_seq_no;
--                EXCEPTION
--                    WHEN OTHERS THEN
--                        NULL;
--                END;

                BEGIN 



                /*Set employee details*/

                set_owner_details(p_item_key     => l_itemkey
                                 ,p_emp_id       => ln_emp_id
                                 ,p_emp_name     => lv_emp_name
                                 ,p_emp_pos_name => lv_emp_pos_name
                                 ,p_emp_org_name => lv_emp_org_name
                );




                EXCEPTION
                    WHEN OTHERS THEN
                        NULL;
                END;

                wf_engine.setitemattrtext(itemtype
                                         ,l_itemkey
                                         ,'FROM'
                                         ,fnd_global.user_name
                                        );


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'APPROVER'
                                        ,lv_appr_user_name);


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'TITLE'
                                        ,'Institutional Public Service - '
                                      || lv_project_name
                                      || ' submitted by '
                                      || lv_emp_name    
                                      || ' needs your Approval'
                                         );

                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'APPR_BODY_RN'
                                        ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_INST_DETAILS_RN&pSequenceNo='
                                      || lv_seq_no
                                      || '&urlParam=Approval'
                                        || '&pItemKey='
                                        || l_itemkey
                                        );

                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'ATTACHMENT_RN'
                                        ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_INST_ATTACHMENT_RN&pSequenceNo='
                                        || lv_seq_no
                                        || '&pItemKey='
                                        || l_itemkey
                                        );


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'#HISTORY'
                                        ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_INST_HIST_RN&pSequenceNo='
                                        || lv_seq_no
                                        || '&pItemKey='
                                        || l_itemkey
                                        );                        


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'APPROVER_COUNTER'
                                        ,2); --1 is reserved for submitter


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'APPROVER_NAME'
                                        ,lv_approver_name);


            END;


            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'FROM'
                                     ,fnd_global.user_name
                                    );


            wf_engine.setitemattrtext(itemtype
                                    ,l_itemkey
                                    ,'FYI_TITLE'
                                    ,'Institutional Public Service - '
                                    || lv_project_name
                                    || ' has been submitted for approval of '
                                    || lv_approver_name
                                    );


            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'FYI_BODY_RN'
                                     ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_INST_DETAILS_RN&pSequenceNo=' 
                                    || lv_seq_no
                                    || '&pItemKey='
                                    || l_itemkey
                                    );

            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'PROJECT_NAME'
                                     ,lv_project_name
                                    );


            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'OWNER_NAME'
                                     ,lv_emp_name
                                    );


        END IF;


        COMMIT;

    EXCEPTION
      WHEN OTHERS THEN
        lv_error := SQLERRM;

        raise_application_error(-20101, 'error setting up attributes: ' || SQLERRM);

        COMMIT;

    END set_attributes;

    PROCEDURE init_approvers(p_assignment_id IN VARCHAR2
                            ,p_seq_no        IN VARCHAR2
                            ,p_action        IN VARCHAR2
                            ,p_item_key      OUT VARCHAR2)
    IS
        lv_sup_name VARCHAR2(100);
        ln_sup_person_id NUMBER;
        ln_sup_assignment_id NUMBER;
        lv_sup_pos_name VARCHAR2(150);
        lv_sup_org_name VARCHAR2(150);

        is_principal_unit VARCHAR2(1) := 'N';

        lv_emp_name VARCHAR2(100);
        ln_emp_id NUMBER;
        lv_emp_pos_name VARCHAR2(150);
        lv_emp_org_name VARCHAR2(150);
        ln_emp_asg_id NUMBER;   


        --lv_owner VARCHAR2(100);
        lv_seq_no VARCHAR2(100);


        ln_from_id NUMBER;
        lv_from_name VARCHAR2(100);
        lv_from_pos_name VARCHAR2(150);
        lv_from_org_name VARCHAR2(150);


        ln_to_id NUMBER;
        lv_to_name VARCHAR2(100);
        lv_to_pos_name VARCHAR2(150);
        lv_to_org_name VARCHAR2(150);

        ln_total_approver_count NUMBER;                    
        ln_approver_ctr NUMBER := 0;
        lv_appr_user_name VARCHAR2(100);

        ln_ps_recipient_id NUMBER;

        lv_exist VARCHAR2(1) := 'N';

        lv_error VARCHAR2(1000) := '';

        lv_item_key wf_notifications.item_key%TYPE;


        lv_ps_recipient_org_name hr_all_organization_units.name%TYPE := '';
    BEGIN

        /*create item key*/

          lv_item_key := create_item_key(p_seq_no        => p_seq_no
                                        ,p_action        => p_action
                         );

          p_item_key :=  lv_item_key; 
        BEGIN
          SELECT 'Y'
          INTO lv_exist
          FROM xxup.xxup_per_ps_action_history
          WHERE ROWNUM = 1
          AND type = 'Institutional'
          AND item_key = lv_item_key;
--          AND sequence_no = p_seq_no;

            IF lv_exist = 'Y' THEN
    --          RETURN;
                DELETE
                  FROM xxup.xxup_per_ps_action_history
                  WHERE type = 'Institutional'
                  AND item_key = lv_item_key;

            END IF;
        EXCEPTION
            WHEN no_data_found THEN
                NULL;
        END;






        BEGIN

--            lv_seq_no := wf_engine.getitemattrtext('XXUPPIWF'
--                                                    ,lv_item_key
--                                                    ,'SEQUENCE_NO');

            /*Set employee details*/
            set_owner_details(p_assignment_id  => p_assignment_id
                             ,p_emp_id       => ln_emp_id
                             ,p_emp_name     => lv_emp_name
                             ,p_emp_pos_name => lv_emp_pos_name
                             ,p_emp_org_name => lv_emp_org_name
            );


            /*Submit employee action line*/
            ln_approver_ctr := ln_approver_ctr + 1;

            INSERT INTO xxup.xxup_per_ps_action_history (
                            sequence_no,
                            approver_no,
                            to_employee_id,
                            to_employee_name,
                            to_position_name,
                            to_org_name,
                            action,
                            type,
                            item_key
                        ) VALUES (
                            lv_seq_no,
                            ln_approver_ctr,
                            ln_emp_id,
                            lv_emp_name,
                            lv_emp_pos_name,
                            lv_emp_org_name,
                            'Submit',
                            'Institutional',  
                            lv_item_key
                        );



          EXCEPTION
              WHEN OTHERS THEN
                  NULL;

          END;

         ln_from_id := ln_emp_id;
         lv_from_name := lv_emp_name;
         lv_from_pos_name := lv_emp_pos_name;
         lv_from_org_name := lv_emp_org_name;


        /*Get PS Recipient approver*/
        BEGIN


            /*--get org name
            SELECT organization_id
            INTO ln_emp_asg_id
            FROM hr.per_all_assignments_f paaf
            WHERE SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
              AND primary_flag = 'Y'
              AND person_id = ln_emp_id;

            */

            ln_approver_ctr := ln_approver_ctr + 1;


            BEGIN
                SELECT name
                INTO lv_ps_recipient_org_name
                FROM hr_all_organization_units haou
                    ,hr_soft_coding_keyflex hsck
                    ,per_all_assignments_f paaf
                WHERE haou.organization_id = to_number(hsck.segment1)
                AND paaf.soft_coding_keyflex_id = hsck.soft_coding_keyflex_id
                AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                AND paaf.assignment_id = p_assignment_id;
            EXCEPTION
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - PS recipient not found!');
            END;






            SELECT apps.xxup_hrms_utilities_pkg.get_up_approver(lv_ps_recipient_org_name,
                                                                'UP_HRD_APPROVAL_ROLES',
                                                                'Public Service Recipient',
                                                                trunc(sysdate)) hr
             INTO ln_ps_recipient_id
             FROM DUAL;



             /*Additional check if the recipient is the same as creator*/
            IF ln_ps_recipient_id = ln_emp_id
            THEN
                RETURN;
            END IF;
            BEGIN
             SELECT papf.full_name
                   ,papf.person_id
                   ,(SELECT ppd.segment1
                     FROM per_position_definitions ppd
                         ,per_all_positions pap
                     WHERE pap.position_id = paaf.position_id
                     AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
                  ,(SELECT haou.name
                    FROM per_position_definitions ppd
                        ,per_all_positions pap
                        ,hr_all_organization_units haou
                     WHERE pap.position_id = paaf.position_id
                     AND   ppd.position_definition_id = pap.position_definition_id
                     AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
             INTO lv_to_name
                 ,ln_to_id
                 ,lv_to_pos_name
                 ,lv_to_org_name
             FROM per_all_assignments_f paaf
                 ,per_all_people_f papf
                 ,per_position_extra_info ppei
             WHERE paaf.person_id = papf.person_id
             AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
             AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
             AND paaf.position_id = ppei.position_id
             AND information_type = 'UP_POSITION_INFO'
             AND NVL(ppei.poei_information8,'No') = 'Yes' --is PS recipient
             AND papf.person_id = ln_ps_recipient_id;
            EXCEPTION
                WHEN no_data_found THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - PS Recipient not properly setup');
                WHEN too_many_rows THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - Too many PS Recipient defined');
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - Unable to retrieve PS Recipient data');
            END;


             INSERT INTO xxup.xxup_per_ps_action_history (
                        item_key,
                        sequence_no,
                        approver_no,
                        from_employee_id,
                        from_employee_name,
                        from_position_name,
                        from_org_name,
                        to_employee_id,
                        to_employee_name,
                        to_position_name,
                        to_org_name,
                        action,
                        type                        
                    ) VALUES (
                        lv_item_key,
                        lv_seq_no,
                        ln_approver_ctr,
                        ln_from_id,
                        lv_from_name,
                        lv_from_pos_name,
                        lv_from_org_name,
                        ln_to_id,
                        lv_to_name,
                        lv_to_pos_name,
                        lv_to_org_name,
                        'Pending',
                        'Institutional'
                    );

              COMMIT;


        EXCEPTION 
            WHEN no_data_found THEN
              raise_application_error(-20101, 'Encountered error on Initializing approvers - PS recipient not found!');
            WHEN too_many_rows THEN
              raise_application_error(-20101, 'Encountered error on Initializing approvers - too many approver, consult your System Administrator');
            WHEN OTHERS THEN
              raise_application_error(-20101, 'Encountered error on Initializing approvers - ' || SUBSTR(SQLERRM,0,200));
        END;


    END init_approvers;


    PROCEDURE resubmit(p_item_key VARCHAR2)
    IS

      lv_seq_no VARCHAR2(100);
      ln_nid NUMBER;
      lv_project_name VARCHAR2(200);
      lv_owner VARCHAR2(100);


      lv_emp_name VARCHAR2(100);
      ln_emp_id NUMBER;
      lv_emp_pos_name VARCHAR2(150);
      lv_emp_org_name VARCHAR2(150);


      ln_approver_counter NUMBER;
      ln_update_ctr NUMBER;
      ln_total_approver_count NUMBER;

      lv_error VARCHAR2(2000);

      lv_item_key wf_notifications.item_key%TYPE;
    BEGIN
        lv_item_key := p_item_key;
      /*append inst*/
      -- lv_seq_no := 'INST-' || p_sequence_no;

      --get notification id

      BEGIN
      SELECT notification_id
            ,recipient_role
      INTO ln_nid
          ,lv_owner
      FROM wf_notifications
      WHERE message_type LIKE 'XXUPPIWF'
      AND message_name LIKE 'RFC_MSG'
      AND INSTR(context, ':' || lv_item_key || ':') > 1 --added colons to get exact itemkey
      AND status = 'OPEN'; 

      EXCEPTION
        WHEN OTHERS THEN
              raise_application_error(-20101, 'error on getting notification: ' || SQLERRM);
      END;

      BEGIN

      set_owner_details(p_item_key     => lv_item_key
                       ,p_emp_id       => ln_emp_id
                       ,p_emp_name     => lv_emp_name
                       ,p_emp_pos_name => lv_emp_pos_name
                       ,p_emp_org_name => lv_emp_org_name
      );
      EXCEPTION
        WHEN OTHERS THEN
            raise_application_error(-20101, 'error on setting owner details: ' || SQLERRM);
      END;


      IF ln_nid IS NOT NULL THEN


          lv_project_name := wf_engine.getitemattrtext('XXUPPIWF'
                                                      ,lv_item_key
                                                      ,'PROJECT_NAME');



          ln_approver_counter := wf_engine.getitemattrnumber('XXUPPIWF'
                                                            ,lv_item_key
                                                           ,'APPROVER_COUNTER');

          wf_engine.setitemattrtext('XXUPPIWF'
                                   ,lv_item_key
                                   ,'TITLE'
                                   ,'Public Service - ' 
                                   || lv_project_name
                                   || ' has been corrected by '
                                   || lv_emp_name ||
                                      ' and requires your approval'
                                    );


          wf_notification.setattrtext(nid    => ln_nid
                                     ,aname  => 'RESULT'
                                     ,avalue => 'RESUBMIT');

          BEGIN
          wf_notification.respond(nid       =>  ln_nid
                                 ,responder =>  lv_owner);

          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20105, 'Error encountered on Resubmit:  get and set attributes: ' || SQLERRM);
          END;


          BEGIN

          UPDATE xxup.xxup_per_ps_action_history hist
          SET action = 'Resubmit'
             ,action_date = SYSDATE
          WHERE item_key = lv_item_key
          AND type = 'Institutional'
          AND approver_no = (SELECT MAX(approver_no)
                             FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
                             WHERE hist_1.item_key = hist.item_key
                             AND type = 'Institutional'
                             AND action = 'Pending'); /*get line number of employee*/

          COMMIT;

          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20101, 'error on update resubmit: ' || SQLERRM);
          END;


          /*1. get all pending approvers and increment their approver no/sequence by 1  
          2. insert record for the approver (pending approval)
          */

--          BEGIN
--          UPDATE xxup.xxup_per_ps_action_history
--          SET approver_no = approver_no + 1
--          WHERE action = 'Pending'
--          AND type = 'Institutional'
--          AND sequence_no = p_sequence_no;  
--          
--          EXCEPTION
--            WHEN OTHERS THEN
--              raise_application_error(-20101, 'error on updating approver no: ' || SQLERRM);
--          END;

          BEGIN
              INSERT INTO xxup.xxup_per_ps_action_history(sequence_no,
                                                          approver_no,
                                                          from_employee_id,
                                                          from_employee_name,
                                                          from_position_name,
                                                          from_org_name,
                                                          to_employee_id,
                                                          to_employee_name,
                                                          to_position_name,
                                                          to_org_name,
                                                          action,
                                                          type,
                                                          item_key
                                                         )
              SELECT sequence_no
                    ,to_number(approver_no) + 2
                    ,ln_emp_id
                    ,lv_emp_name
                    ,lv_emp_pos_name
                    ,lv_emp_org_name
                    ,to_employee_id
                    ,to_employee_name
                    ,to_position_name
                    ,to_org_name
                    ,'Pending'
                    ,'Institutional'
                    ,lv_item_key
              FROM xxup.xxup_per_ps_action_history hist
              WHERE item_key = lv_item_key
              AND type = 'Institutional'
              AND approver_no = (SELECT MAX(approver_no)
                                 FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
                                 WHERE hist_1.item_key = hist.item_key
                                 AND type = 'Institutional'
                                 AND action = 'Return for correction'); /*get latest return for correction*/

          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20101, 'Error on resubmit -> insert: ' || SQLERRM);
          END;


--          ln_approver_counter := ln_approver_counter + 1;
--
--          wf_engine.setitemattrnumber('XXUPPIWF'
--                                     ,lv_item_key
--                                     ,'APPROVER_COUNTER'
--                                     ,ln_approver_counter);

          /*                               
          SELECT COUNT(1)
          INTO ln_total_approver_count
          FROM xxup.xxup_per_ps_action_history
          WHERE sequence_no = p_sequence_no;

          wf_engine.setitemattrnumber('XXUPPSWF'
                                     ,p_sequence_no
                                     ,'TOTAL_APPROVER_COUNT'
                                     ,ln_total_approver_count);   
          */


          COMMIT;


      END IF;



    EXCEPTION 
      WHEN OTHERS THEN 

        raise_application_error(-20100, 'resubmit error: ' || SQLERRM);

        /*
        lv_error := SQLERRM;

        UPDATE XXUP.XXUP_PER_PUBLIC_SERVICE_HEADER
        SET request_status = lv_error
        WHERE sequence_no = p_sequence_no;

        COMMIT;
        */


    END resubmit; 





    PROCEDURE update_status(itemtype  IN VARCHAR2
                            ,l_itemkey IN VARCHAR2
                            ,actid     IN VARCHAR2
                            ,funcmode  IN VARCHAR2
                            ,resultout OUT VARCHAR2)
    IS

    lv_seq_no VARCHAR2(100);

    lv_appr_user_name VARCHAR2(100);
    lv_project_name VARCHAR2(150);

    lv_ntf_result VARCHAR2(30);
    ln_nid NUMBER := wf_engine.context_nid;

    ln_approver_counter NUMBER;
    ln_total_approver_count NUMBER;
    lv_completed_approval VARCHAR2(1) := 'N';

    ln_cur_approver_no NUMBER;

    lv_approver_name VARCHAR2(150);
    lv_approver_emp_id NUMBER;

    ln_update_ctr NUMBER; --update approver no on RFC


    lv_emp_name VARCHAR2(100);
    ln_emp_id NUMBER;
    lv_emp_pos_name VARCHAR2(150);
    lv_emp_org_name VARCHAR2(150);

    ln_to_id NUMBER;
    lv_to_name VARCHAR2(100);
    lv_to_pos_name VARCHAR2(150);
    lv_to_org_name VARCHAR2(150);

    l_new_approver_id         wf_roles.orig_system_id%TYPE;
    l_origsys                  wf_roles.orig_system%TYPE;

    lv_error VARCHAR2(1000);


    lv_action_prefix VARCHAR2(1) := 'C';



    BEGIN




--        INSERT INTO xxup_ps_test_tbl
--        VALUES('funcmode: ' || funcmode);
--        
--        COMMIT;
--        

--       raise_application_error(-20101, 'total approver counter: ' || ln_approver_counter);


        IF funcmode = 'RESPOND' THEN

            lv_ntf_result := wf_notification.getattrtext(ln_nid, 'RESULT');


            ln_approver_counter := wf_engine.getitemattrnumber(itemtype
                                                             ,l_itemkey
                                                             ,'APPROVER_COUNTER');



            BEGIN 
                SELECT sequence_no
                INTO lv_seq_no
                FROM xxup.xxup_per_ps_institutional_tr
                WHERE item_key = l_itemkey;

            EXCEPTION
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Error getting Sequence no');
            END;


            lv_project_name := wf_engine.getitemattrtext(itemtype
                                                 ,l_itemkey
                                                 ,'PROJECT_NAME');


            BEGIN

              SELECT MAX(approver_no)
              INTO ln_cur_approver_no
              FROM xxup.xxup_per_ps_action_history
              WHERE item_key = l_itemkey
              AND type = 'Institutional'
              AND action = 'Pending';

            EXCEPTION
              WHEN OTHERS THEN
                raise_application_error(-20101, 'Error getting latest approver');
            END;

    --        INSERT INTO xxup_ps_test_tbl
    --        VALUES('ln_cur_approver_no: ' || ln_cur_approver_no);
    --        
    --        COMMIT;




            BEGIN

            /*Set employee details*/
            set_owner_details(p_item_key     => l_itemkey
                             ,p_emp_id       => ln_emp_id
                             ,p_emp_name     => lv_emp_name
                             ,p_emp_pos_name => lv_emp_pos_name
                             ,p_emp_org_name => lv_emp_org_name
            );


            EXCEPTION
                WHEN OTHERS THEN
                    lv_error := SQLERRM;
                    raise_application_error(-20105, 'error getting employee details: ' || lv_error);
            END;


    --        INSERT INTO xxup_ps_test_tbl
    --        VALUES('ln_emp_id: ' || ln_emp_id);
    --        
    --        COMMIT;


            BEGIN

                SELECT user_name
                     ,(SELECT full_name
                       FROM per_all_people_f papf
                       WHERE person_id = employee_id
                       AND SYSDATE BETWEEN effective_start_date AND effective_end_date
                       AND person_type_id = 1126)
                INTO lv_appr_user_name
                    ,lv_approver_name
                FROM fnd_user
                WHERE employee_id = (SELECT to_employee_id
                                                   FROM xxup.xxup_per_ps_action_history pah
                                                   WHERE type = 'Institutional'
                                                   AND item_key = l_itemkey
        --                                           AND approver_no = ln_approver_counter
                                                   AND action = 'Pending');

            EXCEPTION
              WHEN OTHERS THEN
                raise_application_error(-20101, 'Error getting latest approver user name');
            END;


            BEGIN

              SELECT COUNT(DISTINCT to_employee_name)
              INTO ln_total_approver_count
              FROM xxup.xxup_per_ps_action_history
              WHERE item_key = l_itemkey
              AND type = 'Institutional'
              AND action IN ('Pending','Return for correction','Reassign');

            EXCEPTION
              WHEN OTHERS THEN
                raise_application_error(-20101, 'Error getting total approver count');
            END;


                IF lv_ntf_result = 'RFC' THEN

                    wf_engine.setitemattrtext(itemtype
                                          ,l_itemkey
                                          ,'RFC_URL'
                                          ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_INST_REQ&urlParam=RFC' ||
                                          '&pSequenceNo=' || lv_seq_no ||
                                          '&pItemKey=' || l_itemkey
                                          );            

                    wf_engine.setitemattrtext(itemtype
                                          ,l_itemkey
                                          ,'TITLE'
                                          ,'Institutional Public Service - ' ||  
                                          lv_project_name|| 
                                          ' has been returned for correction by '|| 
                                          lv_approver_name
                                           );

                    wf_engine.setitemattrtext(itemtype
                                          ,l_itemkey
                                          ,'FROM'
                                          ,lv_appr_user_name);



                    UPDATE xxup.xxup_per_ps_action_history hist
                    SET action = 'Return for correction'
                       ,action_date = SYSDATE
                       ,note = wf_engine.context_user_comment
                    WHERE item_key = l_itemkey
                    AND type = 'Institutional' 
                    AND approver_no = (SELECT MAX(approver_no)
                                         FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
                                         WHERE hist_1.item_key = hist.item_key
                                         AND type = 'Institutional'
                                         AND action = 'Pending');




                    /*1. get all pending approvers and increment their approver no/sequence by 1  
                      2. insert record for the owner (pending resubmission)
                    */


--                    UPDATE xxup.xxup_per_ps_action_history
--                    SET approver_no = approver_no + 1
--                    WHERE action = 'Pending'
--                    AND type = 'Institutional'
--                    AND sequence_no = lv_seq_no;

                    BEGIN 
                    INSERT INTO xxup.xxup_per_ps_action_history(sequence_no,
                                                                approver_no,
                                                                from_employee_id,
                                                                from_employee_name,
                                                                from_position_name,
                                                                from_org_name,
                                                                to_employee_id,
                                                                to_employee_name,
                                                                to_position_name,
                                                                to_org_name,
                                                                action,
                                                                type,
                                                                item_key
                                                               )
                    SELECT sequence_no
                          ,ln_cur_approver_no + 1
                          ,to_employee_id
                          ,to_employee_name
                          ,to_position_name
                          ,to_org_name
                          ,ln_emp_id
                          ,lv_emp_name
                          ,lv_emp_pos_name
                          ,lv_emp_org_name
                          ,'Pending'
                          ,'Institutional'
                          ,l_itemkey
                    FROM xxup.xxup_per_ps_action_history hist
                    WHERE item_key = l_itemkey
                    AND type = 'Institutional'
                    AND approver_no = (SELECT MAX(approver_no)
                                       FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
                                       WHERE hist_1.item_key = hist.item_key
                                       AND type = 'Institutional'
                                       AND action IN ('Resubmit', 'Pending', 'Submit')); /*get latest employee resubmission*/


                  EXCEPTION
                    WHEN OTHERS THEN
                      lv_error := SQLERRM;
                      raise_application_error(-20105, 'error inserting action history record: ' || lv_error);
                  END;

                ELSIF lv_ntf_result = 'APPROVE' THEN
                    INSERT INTO test_tbl
                    VALUES('approved');

                    wf_engine.setitemattrtext(itemtype
                                             ,l_itemkey
                                             ,'FYI_TITLE'
                                             ,'Institutional Public Service- '
                                             || lv_project_name
                                             || ' has been approved by '
                                             || lv_approver_name
                                            );

                    wf_engine.setitemattrtext(itemtype
                                            ,l_itemkey
                                            ,'TITLE'
                                            ,'Institutional Public Service- '
                                          || lv_project_name
                                          || ' submitted by '
                                          || lv_emp_name
                                          || ' needs your Approval'
                                             );

                    UPDATE XXUP.XXUP_PER_PS_ACTION_HISTORY
                    SET action = 'Approved'
                       ,action_date = SYSDATE
                       ,note = wf_engine.context_user_comment
                    WHERE item_key = l_itemkey
                    AND approver_no = ln_approver_counter;
                ELSIF lv_ntf_result = 'REJECT' THEN
--                    INSERT INTO test_tbl
--                    VALUES('rejected');

                    wf_engine.setitemattrtext(itemtype
                                             ,l_itemkey
                                             ,'FYI_TITLE'
                                             ,'Institutional Public Service- '
                                             || lv_project_name
                                             || ' has been rejected by '
                                             || lv_approver_name
                                             );

                    UPDATE XXUP.XXUP_PER_PS_ACTION_HISTORY
                    SET action = 'Rejected'
                       ,action_date = SYSDATE
                       ,note = wf_engine.context_user_comment
                    WHERE item_key = l_itemkey
                    AND type = 'Institutional'
                    AND approver_no = ln_approver_counter;


                    DELETE FROM xxup.xxup_per_ps_action_history
                    WHERE item_key = l_itemkey
                    AND type = 'Institutional'
                    AND approver_no > ln_approver_counter;

                ELSIF funcmode = 'FORWARD' THEN

                    --get forwarded employee person id (l_new_approver_id
                    wf_directory.getroleorigsysinfo(wf_engine.context_new_role,
                                                    l_origsys,
                                                    l_new_approver_id
                                                   );


                    wf_engine.setitemattrtext(itemtype
                                             ,l_itemkey
                                             ,'TITLE'
                                             ,'Institutional Public Service - '
                                             || lv_project_name
                                             || ' has been reassigned to you by '
                                             || lv_approver_name
                                             || ' for approval'
                                             );


                    UPDATE xxup.xxup_per_ps_action_history
                    SET action = 'Reassign'
                        ,action_date = SYSDATE
                        ,note = wf_engine.context_user_comment
                    WHERE item_key = l_itemkey
                    AND type = 'Institutional'
                    AND approver_no = ln_cur_approver_no;

        --            COMMIT;


                     /*1. get all pending approvers and increment their approver no/sequence by 1 
                       2. insert record for the owner (pending pending approval to new approver)  */

        --             UPDATE xxup.xxup_per_ps_action_history
        --             SET approver_no = approver_no + 1
        --             WHERE action = 'Pending'
        --             AND sequence_no = lv_seq_no
        --             AND type = 'Institutional';



                     /*Get details of reassigned employee*/
                     BEGIN

                       SELECT papf.full_name
                             ,papf.person_id
                             ,(SELECT ppd.segment1
                               FROM per_position_definitions ppd
                                   ,per_all_positions pap
                               WHERE pap.position_id = paaf.position_id
                               AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
                            ,(SELECT haou.name
                              FROM per_position_definitions ppd
                                  ,per_all_positions pap
                                  ,hr_all_organization_units haou
                               WHERE pap.position_id = paaf.position_id
                               AND   ppd.position_definition_id = pap.position_definition_id
                               AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
                       INTO lv_to_name
                           ,ln_to_id
                           ,lv_to_pos_name
                           ,lv_to_org_name
                       FROM per_all_assignments_f paaf
                           ,per_all_people_f papf
                       WHERE paaf.person_id = papf.person_id
                       AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                       AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
                       AND primary_flag = 'Y'
                       AND papf.person_id = l_new_approver_id;

                     EXCEPTION
                        WHEN OTHERS THEN
                          lv_error := SQLERRM;
                          raise_application_error(-20105, 'Error on getting details of reassigned employee: ' || lv_error);
                     END;


                     INSERT INTO xxup.xxup_per_ps_action_history(sequence_no,
                                                                 approver_no,
                                                                 from_employee_id,
                                                                 from_employee_name,
                                                                 from_position_name,
                                                                 from_org_name,
                                                                 to_employee_id,
                                                                 to_employee_name,
                                                                 to_position_name,
                                                                 to_org_name,
                                                                 action,
                                                                 type,
                                                                 item_key

                                                                 )
                     SELECT sequence_no
                           ,to_number(approver_no) + 1
                           ,to_employee_id
                           ,to_employee_name
                           ,to_position_name
                           ,to_org_name
                           ,ln_to_id
                           ,lv_to_name
                           ,lv_to_pos_name
                           ,lv_to_org_name
                           ,'Pending'
                           ,'Institutional'
                           ,l_itemkey
                     FROM xxup.xxup_per_ps_action_history
                     WHERE item_key = l_itemkey
                     AND approver_no = ln_cur_approver_no
                     AND type = 'Institutional';         


        --            ln_approver_counter := ln_approver_counter + 1;
        ----            
        ----            
        --            wf_engine.setitemattrnumber(itemtype
        --                                        ,l_itemkey
        --                                        ,'APPROVER_COUNTER'
        --                                        ,ln_approver_counter);




                END IF;



                --exit workflow and complete, if no 'Pending' approval remaining
                  BEGIN
                      SELECT 'N'
                      INTO lv_completed_approval
                      FROM xxup.xxup_per_ps_action_history
                      WHERE action IN ('Pending')
                      AND item_key = l_itemkey
                      AND type = 'Institutional'
                      AND ROWNUM = 1;
                  EXCEPTION
                      WHEN no_data_found THEN
                          lv_completed_approval := 'Y';
                       WHEN OTHERS THEN
                          lv_completed_approval := 'N';

--                          INSERT INTO test_tbl
--                          VALUES('3.5');

                  END;





                  IF lv_completed_approval = 'Y' THEN

                    INSERT INTO test_tbl
                    VALUES('completed approval');


--                IF ln_approver_counter = ln_total_approver_count THEN
--                    INSERT INTO xxup_ps_test_tbl
--                    VALUES('equal');


--                    COMMIT;

                      IF lv_ntf_result = 'APPROVE' THEN

                        INSERT INTO test_tbl
                        VALUES('approvad');


                        BEGIN
                            SELECT SUBSTR(l_itemkey,1,1)
                            INTO lv_action_prefix
                            FROM DUAL;
                        EXCEPTION
                            WHEN OTHERS THEN
                                raise_application_error(-20101, 'Unable to get Transaction action');
                        END;

--                        INSERT INTO xxup_ps_test_tbl
--                        VALUES('lv_action_prefix: ' || lv_action_prefix);


                        BEGIN

                            IF lv_action_prefix = 'U' THEN

                                    UPDATE xxup.xxup_per_ps_institutional main
                                    SET (sequence_no                     
                                        ,constituent_university          
                                        ,project_name                    
                                        ,implementation_start_date       
                                        ,implementation_end_date         
                                        ,duration
                                        ,duration_unit
                                        ,implementation_frequency        
                                        ,funding_agency                  
                                        ,cost_of_participation           
                                        ,unit_of_beneficiary             
                                        ,no_of_beneficiary               
                                        ,post_act_eval_rating            
                                        ,remarks                         
                                        ,attribute1                      
                                        ,attribute2                      
                                        ,attribute3                      
                                        ,attribute4                      
                                        ,attribute5                      
                                        ,last_update_date                
                                        ,last_updated_by                 
                                        ,last_update_login               
                                        ,created_by                      
                                        ,creation_date                   
                                        ,status                          
                                        ,male_benef_count                
                                        ,female_benef_count              
                                        ,project_leader           
                                    ) = (SELECT sequence_no                     
                                            ,constituent_university          
                                            ,project_name                    
                                            ,implementation_start_date       
                                            ,implementation_end_date         
                                            ,duration
                                            ,duration_unit
                                            ,implementation_frequency        
                                            ,funding_agency                  
                                            ,cost_of_participation           
                                            ,unit_of_beneficiary             
                                            ,no_of_beneficiary               
                                            ,post_act_eval_rating            
                                            ,remarks                         
                                            ,attribute1                      
                                            ,attribute2                      
                                            ,attribute3                      
                                            ,attribute4                      
                                            ,attribute5                      
                                            ,last_update_date                
                                            ,last_updated_by                 
                                            ,last_update_login               
                                            ,created_by                      
                                            ,creation_date                   
                                            ,status                          
                                            ,male_benef_count                
                                            ,female_benef_count              
                                            ,project_leader           
                                      FROM xxup_per_ps_institutional_tr tr
                                      WHERE tr.item_key = l_itemkey)
                                    WHERE main.sequence_no = lv_seq_no;



                                 ELSE --create

--                                    INSERT INTO xxup_ps_test_tbl
--                                    VALUES('creating record');

                                     INSERT INTO xxup.xxup_per_ps_institutional
                                      (
                                        sequence_no
                                       ,constituent_university
                                       ,project_name
                                       ,project_leader
                                       ,implementation_start_date
                                       ,implementation_end_date
                                       ,status
                                       ,duration
                                       ,duration_unit
                                       ,implementation_frequency
                                       ,funding_agency
                                       ,cost_of_participation
                                       ,unit_of_beneficiary
                                       ,no_of_beneficiary
                                       ,male_benef_count
                                       ,female_benef_count
                                       ,post_act_eval_rating
                                       ,remarks
                                       ,attribute1
                                       ,attribute2
                                       ,attribute3
                                       ,attribute4
                                       ,attribute5
                                       ,last_update_date
                                       ,last_updated_by
                                       ,last_update_login
                                       ,created_by
                                       ,creation_date
                                      )
                                       SELECT sequence_no
                                             ,constituent_university
                                             ,project_name
                                             ,project_leader
                                             ,implementation_start_date
                                             ,implementation_end_date
                                             ,status
                                             ,duration
                                             ,duration_unit
                                             ,implementation_frequency
                                             ,funding_agency
                                             ,cost_of_participation
                                             ,unit_of_beneficiary
                                             ,no_of_beneficiary
                                             ,male_benef_count
                                             ,female_benef_count
                                             ,post_act_eval_rating
                                             ,remarks
                                             ,attribute1
                                             ,attribute2
                                             ,attribute3
                                             ,attribute4
                                             ,attribute5
                                             ,last_update_date
                                             ,last_updated_by
                                             ,last_update_login
                                             ,created_by
                                             ,creation_date
                                      FROM xxup.xxup_per_ps_institutional_tr
                                      WHERE item_key = l_itemkey;

                                END IF;

                                /*create child records*/

                                    /*Activities */
                                    DELETE FROM xxup.xxup_per_ps_inst_activities
                                    WHERE sequence_no = lv_seq_no;

                                    INSERT INTO xxup.xxup_per_ps_inst_activities(sequence_no
                                                                            ,line_number
                                                                            ,activity
                                                                            ,selected
                                                                            ,attribute1               
                                                                            ,attribute2               
                                                                            ,attribute3               
                                                                            ,attribute4               
                                                                            ,attribute5               
                                                                            ,last_update_date         
                                                                            ,last_updated_by          
                                                                            ,last_update_login        
                                                                            ,created_by               
                                                                            ,creation_date     
                                                                            )
                                      SELECT sequence_no
                                            ,line_number
                                            ,activity
                                            ,selected
                                            ,attribute1               
                                            ,attribute2               
                                            ,attribute3               
                                            ,attribute4               
                                            ,attribute5               
                                            ,last_update_date         
                                            ,last_updated_by          
                                            ,last_update_login        
                                            ,created_by               
                                            ,creation_date
                                     FROM xxup_per_ps_inst_act_tr
                                     WHERE item_key = l_itemkey;


                                     /*Address */
                                    DELETE FROM xxup.xxup_per_ps_inst_addr
                                    WHERE sequence_no = lv_seq_no;

                                    INSERT INTO xxup.xxup_per_ps_inst_addr(sequence_no
                                                                            ,line_number
                                                                            ,address
                                                                            ,attribute1               
                                                                            ,attribute2               
                                                                            ,attribute3               
                                                                            ,attribute4               
                                                                            ,attribute5               
                                                                            ,last_update_date         
                                                                            ,last_updated_by          
                                                                            ,last_update_login        
                                                                            ,created_by               
                                                                            ,creation_date     
                                                                            )
                                      SELECT sequence_no
                                            ,line_number
                                            ,address
                                            ,attribute1               
                                            ,attribute2               
                                            ,attribute3               
                                            ,attribute4               
                                            ,attribute5               
                                            ,last_update_date         
                                            ,last_updated_by          
                                            ,last_update_login        
                                            ,created_by               
                                            ,creation_date
                                     FROM xxup_per_ps_inst_addr_tr
                                     WHERE item_key = l_itemkey;

                                     /*Benef Org */
                                    DELETE FROM xxup.xxup_per_ps_inst_benef_org
                                    WHERE sequence_no = lv_seq_no;

                                    INSERT INTO xxup.xxup_per_ps_inst_benef_org(sequence_no
                                                                            ,line_number
                                                                            ,beneficiary_organization
                                                                            ,contact_details
                                                                            ,attribute1               
                                                                            ,attribute2               
                                                                            ,attribute3               
                                                                            ,attribute4               
                                                                            ,attribute5               
                                                                            ,last_update_date         
                                                                            ,last_updated_by          
                                                                            ,last_update_login        
                                                                            ,created_by               
                                                                            ,creation_date     
                                                                            )
                                      SELECT sequence_no
                                            ,line_number
                                            ,beneficiary_organization
                                            ,contact_details
                                            ,attribute1               
                                            ,attribute2               
                                            ,attribute3               
                                            ,attribute4               
                                            ,attribute5               
                                            ,last_update_date         
                                            ,last_updated_by          
                                            ,last_update_login        
                                            ,created_by               
                                            ,creation_date
                                     FROM xxup_per_ps_inst_benef_org_tr
                                     WHERE item_key = l_itemkey;

                                     /*Benef Type*/
                                    DELETE FROM xxup_per_ps_inst_benef_type
                                    WHERE sequence_no = lv_seq_no;

                                    INSERT INTO xxup_per_ps_inst_benef_type(sequence_no
                                                                            ,line_number
                                                                            ,type_of_beneficiary
                                                                            ,selected
                                                                            ,attribute1               
                                                                            ,attribute2               
                                                                            ,attribute3               
                                                                            ,attribute4               
                                                                            ,attribute5               
                                                                            ,last_update_date         
                                                                            ,last_updated_by          
                                                                            ,last_update_login        
                                                                            ,created_by               
                                                                            ,creation_date     
                                                                            )
                                      SELECT sequence_no
                                            ,line_number
                                            ,type_of_beneficiary
                                            ,selected
                                            ,attribute1               
                                            ,attribute2               
                                            ,attribute3               
                                            ,attribute4               
                                            ,attribute5               
                                            ,last_update_date         
                                            ,last_updated_by          
                                            ,last_update_login        
                                            ,created_by               
                                            ,creation_date
                                     FROM xxup_per_ps_inst_benef_type_tr
                                     WHERE item_key = l_itemkey;

                                     /*Countries*/
                                    DELETE FROM xxup_per_ps_inst_countries
                                    WHERE sequence_no = lv_seq_no;

                                    INSERT INTO xxup_per_ps_inst_countries(sequence_no
                                                                            ,line_number
                                                                            ,country
                                                                            ,attribute1               
                                                                            ,attribute2               
                                                                            ,attribute3               
                                                                            ,attribute4               
                                                                            ,attribute5               
                                                                            ,last_update_date         
                                                                            ,last_updated_by          
                                                                            ,last_update_login        
                                                                            ,created_by               
                                                                            ,creation_date     
                                                                            )
                                      SELECT sequence_no
                                            ,line_number
                                            ,country
                                            ,attribute1               
                                            ,attribute2               
                                            ,attribute3               
                                            ,attribute4               
                                            ,attribute5               
                                            ,last_update_date         
                                            ,last_updated_by          
                                            ,last_update_login        
                                            ,created_by               
                                            ,creation_date
                                     FROM xxup_per_ps_inst_countries_tr
                                     WHERE item_key = l_itemkey;

                                     /*Del Mode*/
                                    DELETE FROM xxup_per_ps_inst_delivery_mode
                                    WHERE sequence_no = lv_seq_no;

                                    INSERT INTO xxup_per_ps_inst_delivery_mode(sequence_no
                                                                            ,line_number
                                                                            ,delivery_mode
                                                                            ,selected
                                                                            ,attribute1               
                                                                            ,attribute2               
                                                                            ,attribute3               
                                                                            ,attribute4               
                                                                            ,attribute5               
                                                                            ,last_update_date         
                                                                            ,last_updated_by          
                                                                            ,last_update_login        
                                                                            ,created_by               
                                                                            ,creation_date     
                                                                            )
                                      SELECT sequence_no
                                            ,line_number
                                            ,delivery_mode
                                            ,selected
                                            ,attribute1               
                                            ,attribute2               
                                            ,attribute3               
                                            ,attribute4               
                                            ,attribute5               
                                            ,last_update_date         
                                            ,last_updated_by          
                                            ,last_update_login        
                                            ,created_by               
                                            ,creation_date
                                     FROM xxup_per_ps_inst_del_mode_tr
                                     WHERE item_key = l_itemkey;

                                     /*Members*/
                                    DELETE FROM xxup.xxup_per_ps_inst_members
                                    WHERE sequence_no = lv_seq_no;

                                    INSERT INTO xxup.xxup_per_ps_inst_members(sequence_no
                                                                            ,line_number
                                                                            ,full_name
                                                                            ,position
                                                                            ,organization
                                                                            ,attribute1               
                                                                            ,attribute2               
                                                                            ,attribute3               
                                                                            ,attribute4               
                                                                            ,attribute5               
                                                                            ,last_update_date         
                                                                            ,last_updated_by          
                                                                            ,last_update_login        
                                                                            ,created_by               
                                                                            ,creation_date     
                                                                            )
                                      SELECT sequence_no
                                            ,line_number
                                            ,full_name
                                            ,position
                                            ,organization
                                            ,attribute1               
                                            ,attribute2               
                                            ,attribute3               
                                            ,attribute4               
                                            ,attribute5               
                                            ,last_update_date         
                                            ,last_updated_by          
                                            ,last_update_login        
                                            ,created_by               
                                            ,creation_date
                                     FROM xxup_per_ps_inst_members_tr
                                     WHERE item_key = l_itemkey;

                                     /*Obj Cat*/
                                    DELETE FROM xxup.xxup_per_ps_inst_obj_cat
                                    WHERE sequence_no = lv_seq_no;

                                    INSERT INTO xxup.xxup_per_ps_inst_obj_cat(sequence_no
                                                                            ,line_number
                                                                            ,objective_category
                                                                            ,specifics
                                                                            ,attribute1               
                                                                            ,attribute2               
                                                                            ,attribute3               
                                                                            ,attribute4               
                                                                            ,attribute5               
                                                                            ,last_update_date         
                                                                            ,last_updated_by          
                                                                            ,last_update_login        
                                                                            ,created_by               
                                                                            ,creation_date     
                                                                            )
                                      SELECT sequence_no
                                            ,line_number
                                            ,objective_category
                                            ,specifics
                                            ,attribute1               
                                            ,attribute2               
                                            ,attribute3               
                                            ,attribute4               
                                            ,attribute5               
                                            ,last_update_date         
                                            ,last_updated_by          
                                            ,last_update_login        
                                            ,created_by               
                                            ,creation_date
                                     FROM xxup_per_ps_inst_obj_cat_tr
                                     WHERE item_key = l_itemkey;


                                     /*Office*/
                                    DELETE FROM xxup.xxup_per_ps_inst_office
                                    WHERE sequence_no = lv_seq_no;

                                    INSERT INTO xxup.xxup_per_ps_inst_office(sequence_no
                                                                            ,line_number
                                                                            ,office
                                                                            ,tel_no
                                                                            ,attribute1               
                                                                            ,attribute2               
                                                                            ,attribute3               
                                                                            ,attribute4               
                                                                            ,attribute5               
                                                                            ,last_update_date         
                                                                            ,last_updated_by          
                                                                            ,last_update_login        
                                                                            ,created_by               
                                                                            ,creation_date     
                                                                            )
                                      SELECT sequence_no
                                            ,line_number
                                            ,office
                                            ,tel_no
                                            ,attribute1               
                                            ,attribute2               
                                            ,attribute3               
                                            ,attribute4               
                                            ,attribute5               
                                            ,last_update_date         
                                            ,last_updated_by          
                                            ,last_update_login        
                                            ,created_by               
                                            ,creation_date
                                     FROM xxup_per_ps_inst_office_tr
                                     WHERE item_key = l_itemkey;

                                     /*Part Org */
                                    DELETE FROM xxup.xxup_per_ps_inst_part_org
                                    WHERE sequence_no = lv_seq_no;

                                    INSERT INTO xxup.xxup_per_ps_inst_part_org(sequence_no
                                                                            ,line_number
                                                                            ,organization_name
                                                                            ,location
                                                                            ,attribute1               
                                                                            ,attribute2               
                                                                            ,attribute3               
                                                                            ,attribute4               
                                                                            ,attribute5               
                                                                            ,last_update_date         
                                                                            ,last_updated_by          
                                                                            ,last_update_login        
                                                                            ,created_by               
                                                                            ,creation_date     
                                                                            )
                                      SELECT sequence_no
                                            ,line_number
                                            ,organization_name
                                            ,location
                                            ,attribute1               
                                            ,attribute2               
                                            ,attribute3               
                                            ,attribute4               
                                            ,attribute5               
                                            ,last_update_date         
                                            ,last_updated_by          
                                            ,last_update_login        
                                            ,created_by               
                                            ,creation_date
                                     FROM xxup_per_ps_inst_part_org_tr
                                     WHERE item_key = l_itemkey;


                                    /*Subject area */
                                    DELETE FROM xxup_per_ps_inst_subj
                                    WHERE sequence_no = lv_seq_no;

                                    INSERT INTO xxup_per_ps_inst_subj(sequence_no
                                                                            ,line_number
                                                                            ,subject_area_interest
                                                                            ,selected
                                                                            ,attribute1               
                                                                            ,attribute2               
                                                                            ,attribute3               
                                                                            ,attribute4               
                                                                            ,attribute5               
                                                                            ,last_update_date         
                                                                            ,last_updated_by          
                                                                            ,last_update_login        
                                                                            ,created_by               
                                                                            ,creation_date     
                                                                            )
                                      SELECT sequence_no
                                            ,line_number
                                            ,subject_area_interest
                                            ,selected
                                            ,attribute1               
                                            ,attribute2               
                                            ,attribute3               
                                            ,attribute4               
                                            ,attribute5               
                                            ,last_update_date         
                                            ,last_updated_by          
                                            ,last_update_login        
                                            ,created_by               
                                            ,creation_date
                                     FROM xxup_per_ps_inst_subj_tr
                                     WHERE item_key = l_itemkey;

                                    /*Type of activity */
                                    DELETE FROM xxup.xxup_per_ps_inst_toa
                                    WHERE sequence_no = lv_seq_no;

                                    INSERT INTO xxup.xxup_per_ps_inst_toa(sequence_no
                                                                            ,line_number
                                                                            ,type_of_activity
                                                                            ,attribute1               
                                                                            ,attribute2               
                                                                            ,attribute3               
                                                                            ,attribute4               
                                                                            ,attribute5               
                                                                            ,last_update_date         
                                                                            ,last_updated_by          
                                                                            ,last_update_login        
                                                                            ,created_by               
                                                                            ,creation_date     
                                                                            )
                                      SELECT sequence_no
                                            ,line_number
                                            ,type_of_activity
                                            ,attribute1               
                                            ,attribute2               
                                            ,attribute3               
                                            ,attribute4               
                                            ,attribute5               
                                            ,last_update_date         
                                            ,last_updated_by          
                                            ,last_update_login        
                                            ,created_by               
                                            ,creation_date
                                     FROM xxup.xxup_per_ps_inst_toa_tr
                                     WHERE item_key = l_itemkey;



                                UPDATE xxup.xxup_per_ps_institutional_tr
                                  SET request_status = 'APPROVED'
                                  WHERE item_key = l_itemkey;


                                  UPDATE xxup.xxup_per_ps_action_history
                                  SET action = 'Approved'
                                     ,action_date = SYSDATE
                                     ,note = wf_engine.context_user_comment
                                  WHERE item_key = l_itemkey
                                  AND type = 'Institutional'
                                  AND approver_no = ln_cur_approver_no; 



--                                INSERT INTO xxup_ps_test_tbl
--                                VALUES('done updating child tables');
--                                  COMMIT;

                        EXCEPTION
                            WHEN OTHERS THEN
                                ROLLBACK;
                                raise_application_error(-20101, 'Update status, Error creating records!');
                        END;

                        BEGIN

                         INSERT INTO test_tbl
                                VALUES('FYI_TITLE set');

                          wf_engine.setitemattrtext(itemtype
                                    ,l_itemkey
                                    ,'FYI_TITLE'
                                    ,'Institutional Public Service - '
                                    || lv_project_name
                                    || ' has been approved by '
                                    || lv_approver_name
                                    || ' and has been completed'
                                    );

                            --workflow done, exit proc
                            RETURN;

--                                INSERT INTO xxup_ps_test_tbl
--                                VALUES('FYI_TITLE set ');
                        EXCEPTION
                          WHEN OTHERS THEN
                            lv_error := SQLERRM;
                            raise_application_error(-20101, 'Update status: ' || lv_error);
                        END;



                      ELSIF lv_ntf_result = 'REJECT' THEN

--                                INSERT INTO xxup_ps_test_tbl
--                                VALUES('rejected!');

                          wf_engine.setitemattrtext(itemtype
                                    ,l_itemkey
                                    ,'FYI_TITLE'
                                    ,'Institutional Public Service- '
                                    || lv_project_name
                                    || ' has been rejected by '
                                    || lv_approver_name
                                    );

                          UPDATE xxup.xxup_per_ps_institutional_tr
                          SET request_status = 'Rejected by ' || lv_approver_name
                          WHERE item_key = l_itemkey;

                          UPDATE xxup.xxup_per_ps_action_history
                          SET action = 'Rejected'
                             ,action_date = SYSDATE
                             ,note = wf_engine.context_user_comment
                          WHERE item_key = l_itemkey
                          AND approver_no = ln_cur_approver_no
                          AND type = 'Institutional';

                      END IF;

            END IF; --completed approval

--          IF ln_approver_counter < ln_total_approver_count THEN



--            END IF;


            IF lv_ntf_result IN ('APPROVE', 'REJECT','RFC') THEN
            BEGIN
                wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'RESULT'
                                     ,lv_ntf_result);



                wf_engine.setitemattrnumber(itemtype
                                         ,l_itemkey
                                         ,'APPROVER_COUNTER'
                                         ,ln_approver_counter);


                /*                         
                SELECT COUNT(1)
                INTO ln_total_approver_count
                FROM xxup.xxup_per_ps_action_history
                WHERE sequence_no = lv_seq_no;

                wf_engine.setitemattrnumber(itemtype
                                         ,l_itemkey
                                         ,'TOTAL_APPROVER_COUNT'
                                         ,ln_total_approver_count);   
                */                        

                wf_engine.setitemattrtext(itemtype
                                         ,l_itemkey
                                         ,'FROM'
                                         ,lv_appr_user_name);




--                IF lv_ntf_result IN ('APPROVE', 'REJECT') THEN
--
--                  wf_engine.setitemattrtext(itemtype
--                                           ,l_itemkey
--                                           ,'APPROVER_NAME'
--                                           ,lv_approver_name);
--
--
--                  wf_engine.setitemattrtext(itemtype
--                                     ,l_itemkey
--                                     ,'APPROVER'
--                                     ,lv_appr_user_name);
--
--                  raise_application_error(-20102, 'set attribute approver error');
--
--
--
--
--                END IF;


                EXCEPTION
              WHEN OTHERS THEN
                lv_error := SQLERRM;
                raise_application_error(-20101, 'setting attributes: ' || lv_error);
             END;
            END IF;



          END IF;





        EXCEPTION
          WHEN OTHERS THEN
            lv_error := SQLERRM;
            raise_application_error(-20101, 'update status' ||  lv_error);
    END update_status;

END xxup_ps_inst_wf_pkg;