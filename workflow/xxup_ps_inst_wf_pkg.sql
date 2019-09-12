CREATE OR REPLACE PACKAGE BODY xxup_ps_inst_wf_pkg
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

--            BEGIN
--                SELECT 'Y'
--                INTO lv_exist
--                FROM xxup.xxup_per_ps_action_history
--                WHERE item_key = lv_item_key
--                AND sequence_no = p_seq_no
--                AND type = 'Institutional'
--                AND ROWNUM = 1;
--                
--                raise e_exists;
--                
--             EXCEPTION 
--                WHEN OTHERS THEN
--                    raise_application_error(-20101, 'Unable to create item key for this transaction');
--             END;

            RETURN lv_item_key;

        ELSIF p_action = 'Update' THEN
            /*Get last update transaction's item key*/

            BEGIN
                SELECT item_key
                INTO lv_last_update_item_key
                FROM xxup_per_ps_action_history
                WHERE sequence_no = p_seq_no
                AND type = 'Institutional'
                AND ROWNUM = 1;

                SELECT SUBSTR(lv_last_update_item_key,1,1)
                INTO lv_action_prefix
                FROM DUAL;

                IF lv_action_prefix = 'U' THEN
                    /*increment update item key*/
                    SELECT MAX(SUBSTR(lv_last_update_item_key,-1,1))
                    INTO ln_last_update_ctr
                    FROM xxup_per_ps_action_history
                    WHERE sequence_no = p_seq_no
                    AND type = 'Institutional'
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


    PROCEDURE set_owner_details(p_itemkey      IN  VARCHAR2
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
        WHERE paaf.person_id = papf.person_id
        AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
        AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
        AND primary_flag = 'Y'
        AND pptr.item_key = p_itemkey;


    END set_owner_details;


    PROCEDURE init_wf(p_sequence_no IN VARCHAR2
                     ,p_item_key    IN VARCHAR2)
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
                                 ,p_sequence_no
                                 );

        wf_engine.startprocess(lv_item_type, lv_item_key);


--        UPDATE xxup.xxup_per_ps_header_tr
--        SET item_key = lv_item_key
--        WHERE sequence_no = p_sequence_no;

--        UPDATE xxup.xxup_per_ps_action_history hist
--        SET action_date = SYSDATE
--        WHERE item_key = lv_item_key
--        AND action = 'Submit'
--        AND type = 'Institutional'; --employee submission


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
                WHERE sequence_no = lv_seq_no
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
                WHERE sequence_no = lv_seq_no;
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
                                    AND sequence_no = lv_seq_no
                                    AND action = 'Pending');



            BEGIN

                BEGIN
                    SELECT user_name
                    INTO lv_appr_user_name
                    FROM fnd_user
                    WHERE employee_id = (SELECT to_employee_id
                                        FROM xxup.xxup_per_ps_action_history
                                        WHERE type = 'Institutional'
                                        AND sequence_no = lv_seq_no
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

                set_owner_details(p_seq_no       => lv_seq_no
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
                                        ,'APPROVER'
                                        ,lv_appr_user_name);


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'TITLE'
                                        ,'Public Service Institutional - '
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
                                        );

                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'ATTACHMENT_RN'
                                        ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_INST_ATTACHMENT_RN&pSequenceNo='
                                        || lv_seq_no
                                        );


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'#HISTORY'
                                        ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_INST_HIST_RN&pSequenceNo='
                                        || lv_seq_no
                                        );                        


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'APPROVER_COUNTER'
                                        ,1);


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
                                    ,'Public Service Institutional - '
                                    || lv_project_name
                                    || ' has been submitted for approval of '
                                    || lv_approver_name
                                    );


            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'FYI_BODY_RN'
                                     ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_INST_DETAILS_RNnull=' 
                                    || lv_seq_no
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
    BEGIN

        /*create item key*/

          lv_item_key := create_item_key(p_seq_no        => p_seq_no
                                        ,p_action        => p_action
                         );

          p_item_key :=  lv_item_key; 

          SELECT 'Y'
          INTO lv_exist
          FROM xxup.xxup_per_ps_action_history
          WHERE ROWNUM = 1
          AND type = 'Institutional'
          AND item_key = lv_item_key;
--          AND sequence_no = p_seq_no;

        EXCEPTION
          WHEN OTHERS THEN
            NULL; 
        END ;

        IF lv_exist = 'Y' THEN
--          RETURN;
            DELETE
              FROM xxup.xxup_per_ps_action_history
              WHERE type = 'Institutional'
              AND item_key = lv_item_key;

        END IF;

        
        BEGIN

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
                        p_sequence_no,
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

            SELECT apps.xxup_hrms_utilities_pkg.get_up_approver(lv_emp_org_name,
                                                                'UP_HRD_APPROVAL_ROLES',
                                                                'Public Service Recipient',
                                                                trunc(sysdate)) hr
             INTO ln_ps_recipient_id
             FROM DUAL;

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
             AND papf.person_id = ln_ps_recipient_id;

             INSERT INTO xxup.xxup_per_ps_action_history (
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
                        p_sequence_no,
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
            WHEN OTHERS THEN

                raise_application_error(-20101, 'error on getting notification: ' || SQLERRM);
        END;



    END init_approvers;


    PROCEDURE resubmit(p_sequence_no VARCHAR2)
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
    BEGIN

      /*append inst*/
      lv_seq_no := 'INST-' || p_sequence_no;

      --get notification id

      BEGIN
      SELECT notification_id
            ,recipient_role
      INTO ln_nid
          ,lv_owner
      FROM wf_notifications
      WHERE message_type LIKE 'XXUPPIWF'
      AND message_name LIKE 'RFC_MSG'
      AND INSTR(context, ':' || lv_seq_no || ':') > 1 --added colons to get exact itemkey
      AND status = 'OPEN'; 

      EXCEPTION
        WHEN OTHERS THEN
              raise_application_error(-20101, 'error on getting notification: ' || SQLERRM);
      END;

      BEGIN

      set_owner_details(p_seq_no         => p_sequence_no
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
                                                      ,lv_seq_no
                                                      ,'PROJECT_NAME');



          ln_approver_counter := wf_engine.getitemattrnumber('XXUPPIWF'
                                                            ,lv_seq_no
                                                           ,'APPROVER_COUNTER');

          wf_engine.setitemattrtext('XXUPPIWF'
                                   ,lv_seq_no
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
              raise_application_error(-20105, 'error on get and set attributes: ' || SQLERRM);
          END;


          BEGIN

          UPDATE xxup.xxup_per_ps_action_history hist
          SET action = 'Resubmit'
             ,action_date = SYSDATE
          WHERE sequence_no = p_sequence_no
          AND type = 'Institutional'
          AND approver_no = (SELECT MAX(approver_no)
                             FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
                             WHERE hist_1.sequence_no = hist.sequence_no
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
                                                      type
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
          FROM xxup.xxup_per_ps_action_history hist
          WHERE sequence_no = p_sequence_no
          AND type = 'Institutional'
          AND approver_no = (SELECT MAX(approver_no)
                             FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
                             WHERE hist_1.sequence_no = hist.sequence_no
                             AND type = 'Institutional'
                             AND action = 'Return for correction'); /*get latest return for correction*/

          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20101, 'error on insert: ' || SQLERRM);
          END;


--          ln_approver_counter := ln_approver_counter + 1;

--          wf_engine.setitemattrnumber('XXUPPIWF'
--                                     ,lv_seq_no
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

    BEGIN


        lv_ntf_result := wf_notification.getattrtext(ln_nid, 'RESULT');


        --raise_application_error(-20111, lv_ntf_result);


        ln_approver_counter := wf_engine.getitemattrnumber(itemtype
                                                         ,l_itemkey
                                                         ,'APPROVER_COUNTER');






        /*
        ln_total_approver_count := wf_engine.getitemattrnumber(itemtype
                                                         ,l_itemkey
                                                         ,'TOTAL_APPROVER_COUNT');
        */


        lv_seq_no := wf_engine.getitemattrtext(itemtype
                                             ,l_itemkey
                                             ,'SEQUENCE_NO');


        lv_project_name := wf_engine.getitemattrtext(itemtype
                                             ,l_itemkey
                                             ,'PROJECT_NAME');

        BEGIN

          SELECT MAX(approver_no)
          INTO ln_cur_approver_no
          FROM xxup.xxup_per_ps_action_history
          WHERE sequence_no = lv_seq_no
          AND action = 'Pending';

        EXCEPTION
          WHEN OTHERS THEN
            raise_application_error(-20101, 'Error getting latest approver');
        END;

        BEGIN

        /*Set employee details*/
        set_owner_details(p_seq_no       => lv_seq_no
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
                                           AND sequence_no = lv_seq_no
--                                           AND approver_no = ln_approver_counter
                                           AND action = 'Pending');

        EXCEPTION
          WHEN OTHERS THEN
            raise_application_error(-20101, 'Error getting latest approver user name');
        END;


        BEGIN

          SELECT COUNT(1)
          INTO ln_total_approver_count
          FROM xxup.xxup_per_ps_action_history
          WHERE sequence_no = lv_seq_no
          AND action = 'Pending';

        EXCEPTION
          WHEN OTHERS THEN
            raise_application_error(-20101, 'Error getting total approver count');
        END;

--       raise_application_error(-20101, 'total approver counter: ' || ln_approver_counter);


        IF funcmode = 'RESPOND' THEN


                IF lv_ntf_result = 'RFC' THEN

                    wf_engine.setitemattrtext(itemtype
                                          ,l_itemkey
                                          ,'RFC_URL'
                                          ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_INST_REQnull=RFC' ||
                                          'null=' || lv_seq_no
                                          );            

                    wf_engine.setitemattrtext(itemtype
                                          ,l_itemkey
                                          ,'TITLE'
                                          ,'Public Service Institutional - ' ||  
                                          lv_project_name|| 
                                          ' has been returned for correction by '|| 
                                          lv_approver_name
                                           );



                    UPDATE xxup.xxup_per_ps_action_history hist
                    SET action = 'Return for correction'
                       ,action_date = SYSDATE
                       ,note = wf_engine.context_user_comment
                    WHERE sequence_no = lv_seq_no
                    AND type = 'Institutional' 
                    AND approver_no = (SELECT MAX(approver_no)
                                         FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
                                         WHERE hist_1.sequence_no = hist.sequence_no
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
                                                                type
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
                    FROM xxup.xxup_per_ps_action_history hist
                    WHERE sequence_no = lv_seq_no
                    AND type = 'Institutional'
                    AND approver_no = (SELECT MAX(approver_no)
                                       FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
                                       WHERE hist_1.sequence_no = hist.sequence_no
                                       AND type = 'Institutional'
                                       AND action IN ('Resubmit', 'Pending', 'Submit')); /*get latest employee resubmission*/


                  EXCEPTION
                    WHEN OTHERS THEN
                      lv_error := SQLERRM;
                      raise_application_error(-20105, 'error inserting action history record: ' || lv_error);
                  END;
                END IF;







                IF ln_approver_counter = ln_total_approver_count THEN


                      IF lv_ntf_result = 'APPROVE' THEN

                      BEGIN

                          wf_engine.setitemattrtext(itemtype
                                    ,l_itemkey
                                    ,'FYI_TITLE'
                                    ,'Public Service Institutional - '
                                    || lv_project_name
                                    || ' has been approved by '
                                    || lv_approver_name
                                    || ' and has been completed'
                                    );

                          UPDATE xxup.xxup_per_ps_Institutional_tr
                          SET request_status = 'Approved'
                          WHERE sequence_no = lv_seq_no;


                          UPDATE xxup.xxup_per_ps_action_history
                          SET action = 'Approved'
                             ,action_date = SYSDATE
                             ,note = wf_engine.context_user_comment
                          WHERE sequence_no = lv_seq_no
                          AND type = 'Institutional'
                          AND approver_no = ln_cur_approver_no;


                          INSERT INTO xxup.xxup_per_ps_Institutional
                          (
                            sequence_no
                           ,constituent_university
                           ,project_name
                           ,project_leader
                           ,implementation_start_date
                           ,implementation_end_date
                           ,status
                           ,duration_hours
                           ,implementation_frequency
                           ,type_of_activity
                           ,funding_agency
                           ,cost_of_participation
                           ,unit_of_beneficiary
                           ,no_of_beneficiary
                           ,male_benef_count
                           ,female_benef_count
                           ,country
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
                                 ,duration_hours
                                 ,implementation_frequency
                                 ,type_of_activity
                                 ,funding_agency
                                 ,cost_of_participation
                                 ,unit_of_beneficiary
                                 ,no_of_beneficiary
                                 ,male_benef_count
                                 ,female_benef_count
                                 ,country
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
                          FROM xxup.xxup_per_ps_Institutional_tr
                          WHERE sequence_no = lv_seq_no;




                        EXCEPTION
                          WHEN OTHERS THEN
                            lv_error := SQLERRM;
                            raise_application_error(-20101, lv_error);
                        END;



                      ELSIF lv_ntf_result = 'REJECT' THEN

                          wf_engine.setitemattrtext(itemtype
                                    ,l_itemkey
                                    ,'FYI_TITLE'
                                    ,'Public Service Institutional- '
                                    || lv_project_name
                                    || ' has been rejected by '
                                    || lv_approver_name
                                    );

                          UPDATE xxup.xxup_per_ps_Institutional_tr
                          SET request_status = 'Rejected by ' || lv_approver_name
                          WHERE sequence_no = lv_seq_no;

                          UPDATE xxup.xxup_per_ps_action_history
                          SET action = 'Rejected'
                             ,action_date = SYSDATE
                             ,note = wf_engine.context_user_comment
                          WHERE sequence_no = lv_seq_no
                          AND approver_no = ln_cur_approver_no
                          AND type = 'Institutional';

                          DELETE FROM xxup.xxup_per_ps_action_history
                          WHERE sequence_no = lv_seq_no
                          AND type = 'Institutional';

                          DELETE FROM xxup.xxup_per_ps_inst_addr
                          WHERE sequence_no = lv_seq_no;

                          DELETE FROM xxup.xxup_per_ps_inst_benef_org
                          WHERE sequence_no = lv_seq_no;

                          DELETE FROM xxup.xxup_per_ps_inst_benef_type
                          WHERE sequence_no = lv_seq_no;

                          DELETE FROM xxup.xxup_per_ps_inst_delivery_mode
                          WHERE sequence_no = lv_seq_no;

                          DELETE FROM xxup.xxup_per_ps_inst_members
                          WHERE sequence_no = lv_seq_no;

                          DELETE FROM xxup.xxup_per_ps_inst_obj_cat
                          WHERE sequence_no = lv_seq_no;

                          DELETE FROM xxup.xxup_per_ps_inst_office
                          WHERE sequence_no = lv_seq_no;

                          DELETE FROM xxup.xxup_per_ps_inst_partner_org
                          WHERE sequence_no = lv_seq_no;

                          DELETE FROM xxup.xxup_per_ps_inst_subj
                          WHERE sequence_no = lv_seq_no;

                      END IF;
                END IF;


          IF ln_approver_counter < ln_total_approver_count THEN

            IF lv_ntf_result = 'APPROVE' THEN


                wf_engine.setitemattrtext(itemtype
                                         ,l_itemkey
                                         ,'FYI_TITLE'
                                         ,'Public Service - '
                                         || lv_project_name
                                         || ' has been approved by '
                                         || lv_approver_name
                                         || ' and is completed'
                                          );

                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'TITLE'
                                        ,'Public Service - '
                                      || lv_project_name
                                      || ' submitted by '
                                      || lv_emp_name
                                      || ' needs your Approval'
                                         );

                UPDATE XXUP.XXUP_PER_PS_ACTION_HISTORY
                SET action = 'Approved'
                   ,action_date = SYSDATE
                   ,note = wf_engine.context_user_comment
                WHERE sequence_no = lv_seq_no
                AND approver_no = ln_approver_counter;

             ELSIF lv_ntf_result = 'REJECT' THEN

                wf_engine.setitemattrtext(itemtype
                                         ,l_itemkey
                                         ,'FYI_TITLE'
                                         ,'Public Service - '
                                         || lv_project_name
                                         || ' has been rejected by '
                                         || lv_approver_name
                                         );

                UPDATE XXUP.XXUP_PER_PS_ACTION_HISTORY
                SET action = 'Rejected'
                   ,action_date = SYSDATE
                   ,note = wf_engine.context_user_comment
                WHERE sequence_no = lv_seq_no
                AND type = 'Institutional'
                AND approver_no = ln_approver_counter;
            END IF;


--            
--            Ln_Approver_Counter := Ln_Approver_Counter + 1;
--            
--            
--            BEGIN
--            
--                
--                SELECT user_name
--                      ,(SELECT full_name
--                        FROM per_all_people_f papf
--                        WHERE person_id = employee_id)
--                INTO lv_appr_user_name
--                    ,lv_approver_name
--                FROM fnd_user
--                WHERE employee_id = (SELECT to_employee_id
--                                     FROM xxup.xxup_per_ps_action_history pah
--                                     WHERE sequence_no = lv_seq_no
--                                     AND type = 'Institutional'
--                                     AND approver_no = ln_approver_counter
--                                     AND action = 'Pending');
--                
--                
--            
--             EXCEPTION
--              WHEN OTHERS THEN
--                lv_error := SQLERRM;
--                raise_application_error(-20101, 'error getting next approver: ' || lv_error);
--             END;
--             



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

                raise_application_error(-20102, 'set attribute error');

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


                raise_application_error(-20102, 'set attribute error');


                IF lv_ntf_result IN ('APPROVE', 'REJECT') THEN

                  wf_engine.setitemattrtext(itemtype
                                           ,l_itemkey
                                           ,'APPROVER_NAME'
                                           ,lv_approver_name);


                  wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'APPROVER'
                                     ,lv_appr_user_name);

                  raise_application_error(-20102, 'set attribute approver error');




                END IF;


                EXCEPTION
              WHEN OTHERS THEN
                lv_error := SQLERRM;
                raise_application_error(-20101, 'setting attributes: ' || lv_error);
             END;
            END IF;



          END IF;


        ELSIF funcmode = 'FORWARD' THEN

            --get forwarded employee person id (l_new_approver_id
            wf_directory.getroleorigsysinfo(wf_engine.context_new_role,
                                            l_origsys,
                                            l_new_approver_id
                                           );


            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'TITLE'
                                     ,'Public Service Institutional - '
                                     || lv_project_name
                                     || ' has been reassigned to you by '
                                     || lv_approver_name
                                     || ' for approval'
                                     );


            UPDATE xxup.xxup_per_ps_action_history
            SET action = 'Reassign'
                ,action_date = SYSDATE
                ,note = wf_engine.context_user_comment
            WHERE sequence_no = lv_seq_no
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
                                                         type
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
             FROM xxup.xxup_per_ps_action_history
             WHERE sequence_no = lv_seq_no
             AND approver_no = ln_cur_approver_no
             AND type = 'Institutional';         


--            ln_approver_counter := ln_approver_counter + 1;
--            
--            
--            wf_engine.setitemattrnumber(itemtype
--                                        ,l_itemkey
--                                        ,'APPROVER_COUNTER'
--                                        ,ln_approver_counter);




        END IF;




        EXCEPTION
          WHEN OTHERS THEN
--            lv_error := SQLERRM;
--            raise_application_error(-20101, 'last exception' ||  lv_error);

          NULL;
    END update_status;

END xxup_ps_inst_wf_pkg;