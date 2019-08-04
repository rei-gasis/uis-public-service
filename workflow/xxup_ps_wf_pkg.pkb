create or replace PACKAGE BODY xxup_ps_wf_pkg
IS


    PROCEDURE set_owner_details(p_seq_no       IN  VARCHAR2
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
        FROM xxup.xxup_per_ps_header_tr psh
            ,per_all_assignments_f paaf
            ,per_all_people_f papf
        WHERE psh.position_id = paaf.position_id
        AND paaf.person_id = papf.person_id
        AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
        AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
        AND papf.person_id = (SELECT employee_id
                              FROM fnd_user fu
                              WHERE fu.user_id = psh.created_by)
        AND psh.sequence_no = p_seq_no;


    EXCEPTION
      WHEN no_data_found THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - data not found!');
      WHEN too_many_rows THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - too many row fetched!');
      WHEN OTHERS THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - ' || SUBSTR(SQLERRM,0,200));

    END set_owner_details;

    PROCEDURE init_wf(p_sequence_no IN VARCHAR2)
    IS
    lv_item_type VARCHAR2(100) := 'XXUPPSWF';
    lv_item_key VARCHAR2(100) := p_sequence_no;
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


        UPDATE xxup.xxup_per_ps_header_tr
        SET item_key = lv_item_key
        WHERE sequence_no = p_sequence_no;

        UPDATE xxup.xxup_per_ps_action_history hist
        SET action_date = SYSDATE
           ,type = 'Individual'
        WHERE sequence_no = p_sequence_no
        AND action = 'Submit'; --employee submission


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
                FROM xxup.xxup_per_ps_action_history
                WHERE sequence_no = lv_seq_no;

                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'TOTAL_APPROVER_COUNT'
                                        ,ln_total_approver_count);


            EXCEPTION 
                WHEN OTHERS THEN
                    NULL;
            END;


            --get project name
            BEGIN
                SELECT project_name
                INTO lv_project_name
                FROM xxup.xxup_per_ps_header_tr psh
                WHERE sequence_no = lv_seq_no;
            EXCEPTION
                WHEN no_data_found THEN
                    raise_application_error(-20101, 'Encountered error on Setting attributes - Project name not found!');
                WHEN too_many_rows THEN
                    raise_application_error(-20101, 'Encountered error on Setting attributes - too many Project name fetched!');
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Setting attributes - ' || SUBSTR(SQLERRM,0,200));
            END;


            --get approver name (fullname)
            BEGIN
                SELECT (SELECT full_name
                        FROM per_all_people_f papf
                        WHERE SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
                        AND papf.person_id = paaf.supervisor_id) sup_name
                  INTO lv_approver_name
                  FROM per_all_assignments_f paaf
                 WHERE paaf.assignment_type = 'E'
                   AND TRUNC(SYSDATE) BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                   AND paaf.assignment_status_type_id = 1
                   AND paaf.assignment_id = (SELECT assignment_id
                                             FROM xxup.xxup_per_ps_header_tr psh
                                                 ,per_all_assignments_f paaf
                                             WHERE psh.position_id = paaf.position_id
                                             AND paaf.person_id = fnd_global.employee_id
                                             AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                                             AND psh.sequence_no = lv_seq_no);

            EXCEPTION
                WHEN no_data_found THEN
                    raise_application_error(-20101, 'Encountered error on Setting attributes - Approver name not found!');
                WHEN too_many_rows THEN
                    raise_application_error(-20101, 'Encountered error on Setting attributes - too many Approver name fetched!');
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Setting attributes - ' || SUBSTR(SQLERRM,0,200));
            END;




            /*Set 1st approver notification attributes*/
            BEGIN

                BEGIN
                    SELECT user_name
                    INTO lv_appr_user_name
                    FROM fnd_user
                    WHERE employee_id = (SELECT to_employee_id
                                         FROM xxup.xxup_per_ps_action_history
                                         WHERE approver_no = 1
                                         AND sequence_no = lv_seq_no);

                    wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'APPROVER'
                                        ,lv_appr_user_name);

                EXCEPTION
                WHEN no_data_found THEN
                    raise_application_error(-20101, 'Encountered error on Setting attributes - Approver user name not found!');
                WHEN too_many_rows THEN
                    raise_application_error(-20101, 'Encountered error on Setting attributes - too many Approver user name fetched!');
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Setting attributes - ' || SUBSTR(SQLERRM,0,200));
                END;        







                /*Set employee details*/
                set_owner_details(p_seq_no       => lv_seq_no
                                 ,p_emp_id       => ln_emp_id
                                 ,p_emp_name     => lv_emp_name
                                 ,p_emp_pos_name => lv_emp_pos_name
                                 ,p_emp_org_name => lv_emp_org_name
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

                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'APPR_BODY_RN'
                                        ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_DETAILS_RN&pSequenceNo='
                                      || lv_seq_no
                                        );

                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'ATTACHMENT_RN'
                                        ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_ATTACHMENT_RN&pSequenceNo='
                                        || lv_seq_no
                                        );


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'#HISTORY'
                                        ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_HIST_RN&pSequenceNo='
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



                wf_engine.setitemattrtext(itemtype
                                         ,l_itemkey
                                         ,'FROM'
                                         ,fnd_global.user_name
                                        );


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'FYI_TITLE'
                                        ,'Public Service - '
                                        || lv_project_name
                                        || ' has been submitted for approval of '
                                        || lv_approver_name
                                        );


                wf_engine.setitemattrtext(itemtype
                                         ,l_itemkey
                                         ,'FYI_BODY_RN'
                                         ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_DETAILS_RN&pSequenceNo=' 
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
          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20101, 'Encountered error on Setting attributes' || SUBSTR(SQLERRM,0,200));

          END;
        END IF;

    EXCEPTION
      WHEN OTHERS THEN
          raise_application_error(-20101, 'Encountered error on Setting attributes' || SUBSTR(SQLERRM,0,200));
    END set_attributes;

    PROCEDURE init_approvers(p_sequence_no VARCHAR2)
    IS
        lv_sup_name per_all_people_f.full_name%TYPE;
        ln_sup_person_id per_all_people_f.person_id%TYPE;
        ln_sup_assignment_id per_all_assignments_f.assignment_id%TYPE;
        lv_sup_pos_name per_all_positions.name%TYPE;
        lv_sup_org_name hr_all_organization_units.name%TYPE;

        is_principal_unit VARCHAR2(1) := 'N';

        lv_emp_name per_all_people_f.full_name%TYPE;
        ln_emp_id per_all_people_f.person_id%TYPE;
        lv_emp_pos_name per_all_positions.name%TYPE;
        lv_emp_org_name hr_all_organization_units.name%TYPE;
        ln_emp_asg_id per_all_assignments_f.assignment_id%TYPE;


        lv_seq_no VARCHAR2(28);


        ln_from_id per_all_people_f.person_id%TYPE;
        lv_from_name per_all_people_f.full_name%TYPE;
        lv_from_pos_name per_all_positions.name%TYPE;
        lv_from_org_name hr_all_organization_units.name%TYPE;


        ln_to_id per_all_people_f.person_id%TYPE;
        lv_to_name per_all_people_f.full_name%TYPE;
        lv_to_pos_name per_all_positions.name%TYPE;
        lv_to_org_name hr_all_organization_units.name%TYPE;

        ln_total_approver_count NUMBER;                    
        ln_approver_ctr NUMBER := 0;
        lv_appr_user_name fnd_user.user_name%TYPE;

        ln_ps_recipient_id per_people_f.person_id%TYPE;

        lv_exist VARCHAR2(1) := 'N';
    BEGIN

        /*prevent duplicate inserts*/

        BEGIN 

          SELECT 'Y'
          INTO lv_exist
          FROM xxup.xxup_per_ps_action_history
          WHERE ROWNUM = 1
          AND type = 'Individual'
          AND sequence_no = p_sequence_no;

        EXCEPTION
          WHEN OTHERS THEN
            NULL; 
        END ;

        IF lv_exist = 'Y' THEN
          RETURN;
        END IF;

        BEGIN

        /*Set employee details*/
        set_owner_details(p_seq_no       => p_sequence_no
                         ,p_emp_id       => ln_emp_id
                         ,p_emp_name     => lv_emp_name
                         ,p_emp_pos_name => lv_emp_pos_name
                         ,p_emp_org_name => lv_emp_org_name
        );



        EXCEPTION
            WHEN OTHERS THEN
                NULL;
        END;

            --1. get first approver and insert into history
            --2. iterate approver counter
            --3. insert to action history table
            BEGIN

                BEGIN
                  SELECT supervisor_id
                        ,supervisor_assignment_id
                        ,(SELECT full_name
                          FROM per_all_people_f papf
                          WHERE SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
                          AND papf.person_id = paaf.supervisor_id) sup_name
                        ,(SELECT ppd.segment1
                          FROM per_position_definitions ppd
                              ,per_all_positions pap
                              ,per_all_assignments_f sup_asg
                           WHERE ppd.position_definition_id = pap.position_definition_id
                           AND   pap.position_id = sup_asg.position_id
                           AND   SYSDATE BETWEEN sup_asg.effective_start_date AND sup_asg.effective_end_date
                           AND   sup_asg.assignment_id = paaf.supervisor_assignment_id) POSITION_NAME  
                        ,(SELECT haou.name
                          FROM per_position_definitions ppd
                              ,per_all_positions pap
                              ,per_all_assignments_f sup_asg
                              ,hr_all_organization_units haou
                           WHERE ppd.position_definition_id = pap.position_definition_id
                           AND   pap.position_id = sup_asg.position_id
                           AND   SYSDATE BETWEEN sup_asg.effective_start_date AND sup_asg.effective_end_date
                           AND   sup_asg.assignment_id = paaf.supervisor_assignment_id
                           AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
                    INTO ln_sup_person_id
                        ,ln_sup_assignment_id
                        ,lv_sup_name
                        ,lv_sup_pos_name
                        ,lv_sup_org_name
                    FROM per_all_assignments_f paaf
                   WHERE paaf.assignment_type = 'E'
                     AND TRUNC(SYSDATE) BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                     AND paaf.assignment_status_type_id = 1
                     AND paaf.assignment_id = (SELECT assignment_id
                                               FROM xxup.xxup_per_ps_header_tr psh
                                                   ,per_all_assignments_f paaf
                                               WHERE psh.position_id = paaf.position_id
                                               AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                                               AND paaf.person_id = ln_emp_id
                                               AND psh.sequence_no = p_sequence_no);

                EXCEPTION
                  WHEN no_data_found THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - First approver not found!');
                  WHEN too_many_rows THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - too many approver, consult your System Administrator');
                  WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - ' || SUBSTR(SQLERRM,0,200));
                END;

                ln_approver_ctr := ln_approver_ctr + 1;




                INSERT INTO xxup.xxup_per_ps_action_history(sequence_no 
                                                        ,approver_no 
                                                        ,from_employee_id 
                                                        ,from_employee_name 
                                                        ,from_position_name 
                                                        ,from_org_name 
                                                        ,to_employee_id 
                                                        ,to_employee_name
                                                        ,to_position_name
                                                        ,to_org_name
                                                        ,action
                                                        ,type
                                                        )
                                                VALUES(p_sequence_no
                                                      ,ln_approver_ctr
                                                      ,ln_emp_id
                                                      ,lv_emp_name
                                                      ,lv_emp_pos_name
                                                      ,lv_emp_org_name
                                                      ,ln_sup_person_id
                                                      ,lv_sup_name
                                                      ,lv_sup_pos_name
                                                      ,lv_sup_org_name
                                                      ,'Pending'
                                                      ,'Individual'
                                                      );

            EXCEPTION
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - creating record history: ' || SUBSTR(SQLERRM,0,200));
            END;






            /*Loop to supervisor hierarchy until principal unit is found*/
            BEGIN

            is_principal_unit := xxup_hrms_utilities_pkg.is_principal_unit_v2(ln_sup_assignment_id);

            WHILE is_principal_unit <> 'Y' LOOP

                ln_from_id := ln_sup_person_id;
                lv_from_name := lv_sup_name;
                lv_from_pos_name := lv_sup_pos_name;
                lv_from_org_name := lv_sup_org_name;

                ln_to_id := null;
                lv_to_name := null;
                lv_to_pos_name := null;
                lv_to_org_name := null;


                BEGIN
                  BEGIN
                      SELECT supervisor_id,
                         supervisor_assignment_id,
                        (SELECT full_name
                        FROM per_all_people_f papf
                        WHERE SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
                        AND papf.person_id = paaf.supervisor_id
                        ) pos_name,
                        (SELECT ppd.segment1
                          FROM per_position_definitions ppd,
                            per_all_positions pap,
                            per_all_assignments_f sup_asg
                          WHERE ppd.position_definition_id = pap.position_definition_id
                          AND pap.position_id              = sup_asg.position_id
                          AND SYSDATE BETWEEN sup_asg.effective_start_date AND sup_asg.effective_end_date
                          AND sup_asg.assignment_id = paaf.supervisor_assignment_id
                        ) position_name ,
                        (SELECT haou.name
                          FROM per_position_definitions ppd ,
                               per_all_positions pap ,
                               per_all_assignments_f sup_asg ,
                               hr_all_organization_units haou
                        WHERE ppd.position_definition_id = pap.position_definition_id
                        AND pap.position_id              = sup_asg.position_id
                        AND SYSDATE BETWEEN sup_asg.effective_start_date AND sup_asg.effective_end_date
                        AND sup_asg.assignment_id = paaf.supervisor_assignment_id
                        AND haou.organization_id  = ppd.segment2
                        ) ORGANIZATION_NAME
                    INTO ln_sup_person_id,ln_sup_assignment_id,lv_sup_name,lv_sup_pos_name,lv_sup_org_name
                    FROM per_all_assignments_f paaf
                    WHERE paaf.person_id     = ln_sup_person_id
                    AND paaf.assignment_id   = ln_sup_assignment_id
                    AND paaf.assignment_type = 'E'
                    AND TRUNC(SYSDATE) BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                    AND paaf.assignment_status_type_id = 1;
                  EXCEPTION
                      WHEN no_data_found THEN
                          raise_application_error(-20101, 'Encountered error on Initializing approvers - Approver data not found!');
                      WHEN too_many_rows THEN
                          raise_application_error(-20101, 'Encountered error on Initializing approvers - too many Approver data fetched!');
                      WHEN OTHERS THEN
                          raise_application_error(-20101, 'Encountered error on Initializing approvers - ' || SUBSTR(SQLERRM,0,200));
                  END;

                    is_principal_unit := xxup_hrms_utilities_pkg.is_principal_unit_v2(ln_sup_assignment_id);

                    ln_approver_ctr := ln_approver_ctr + 1;


                    ln_to_id := ln_sup_person_id;
                    lv_to_name := lv_sup_name;
                    lv_to_pos_name := lv_sup_pos_name;
                    lv_to_org_name := lv_sup_org_name;


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
                        'Individual'
                    );


                EXCEPTION
                    WHEN OTHERS THEN
                        EXIT;
                END;
            END LOOP;

            /*Exiting the loop, store details for PS Recipient row*/
            ln_from_id := ln_sup_person_id;
            lv_from_name := lv_sup_name;
            lv_from_pos_name := lv_sup_pos_name;
            lv_from_org_name := lv_sup_org_name;

        END;



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

            --get PS recipient
            BEGIN 
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

            EXCEPTION
                WHEN no_data_found THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - PS Recipient data not found!');
                WHEN too_many_rows THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - too many PS Recipient data fetched!');
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - ' || SUBSTR(SQLERRM,0,200));
            END;



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
                        'Individual'
                    );

        EXCEPTION 
            WHEN OTHERS THEN
                raise_application_error(-20101, 'Encountered error on Initializing approvers - ' || SUBSTR(SQLERRM,0,200));
        END;


    END init_approvers;


    PROCEDURE resubmit(p_sequence_no VARCHAR2)
    IS

      ln_nid NUMBER;
      lv_project_name xxup_per_ps_header_tr.project_name%TYPE;
      lv_owner wf_notifications.recipient_role%TYPE;


      lv_emp_name per_all_people_f.full_name%TYPE;
      ln_emp_id per_all_people_f.person_id%TYPE;
      lv_emp_pos_name per_all_positions.name%TYPE;
      lv_emp_org_name hr_all_organization_units.name%TYPE;



      ln_approver_counter NUMBER;
      ln_update_ctr NUMBER;
      ln_total_approver_count NUMBER;

      lv_error VARCHAR2(2000);
    BEGIN


      --get notification id

      BEGIN
      SELECT notification_id
            ,recipient_role
      INTO ln_nid
          ,lv_owner
      FROM wf_notifications
      WHERE message_type LIKE 'XXUPPSWF'
      AND message_name LIKE 'RFC_MSG'
      AND INSTR(context, ':' || p_sequence_no || ':') > 1 --added colons to get exact itemkey
      AND status = 'OPEN'; 

      EXCEPTION
        WHEN OTHERS THEN
              raise_application_error(-20101, 'Error on Resubmit - getting notification: ' || SQLERRM);
      END;

      BEGIN
      set_owner_details(p_seq_no       => p_sequence_no
                       ,p_emp_id       => ln_emp_id
                       ,p_emp_name     => lv_emp_name
                       ,p_emp_pos_name => lv_emp_pos_name
                       ,p_emp_org_name => lv_emp_org_name
      );
      EXCEPTION
        WHEN OTHERS THEN
            raise_application_error(-20101, 'Error on Setting initiator details: ' || SQLERRM);
      END;


      IF ln_nid IS NOT NULL THEN


          lv_project_name := wf_engine.getitemattrtext('XXUPPSWF'
                                                      ,p_sequence_no
                                                      ,'PROJECT_NAME');



          ln_approver_counter := wf_engine.getitemattrnumber('XXUPPSWF'
                                                            ,p_sequence_no
                                                           ,'APPROVER_COUNTER');

          IF lv_project_name IS NOT NULL THEN
            wf_engine.setitemattrtext('XXUPPSWF'
                                   ,p_sequence_no
                                   ,'TITLE'
                                   ,'Public Service - ' 
                                   || lv_project_name
                                   || ' has been corrected by '
                                   || lv_emp_name ||
                                      ' and requires your approval'
                                    );
          ELSE
            raise_application_error(-20101, 'Error on Resubmit - Project name not set!');
          END IF;                             



          wf_notification.setattrtext(nid    => ln_nid
                                     ,aname  => 'RESULT'
                                     ,avalue => 'RESUBMIT');

          BEGIN
            wf_notification.respond(nid       =>  ln_nid
                                   ,responder =>  lv_owner);

          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20101, 'Error on Resubmit - respond: ' || SUBSTR(SQLERRM,0,200));
          END;


          BEGIN
            UPDATE xxup.xxup_per_ps_action_history
            SET action = 'Resubmit'
               ,action_date = SYSDATE
            WHERE sequence_no = p_sequence_no
            AND type = 'Individual'
            AND approver_no = ln_approver_counter;

            COMMIT;

          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20101, 'Error on Resubmit - update: ' || SQLERRM);
          END;


          /*1. get all pending approvers and increment their approver no/sequence by 1  
          2. insert record for the approver (pending approval)
          */

          BEGIN
            UPDATE xxup.xxup_per_ps_action_history
            SET approver_no = approver_no + 1
            WHERE action = 'Pending'
            AND type = 'Individual'
            AND sequence_no = p_sequence_no;  

          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20101, 'Error on Resubmit - update: ' || SQLERRM);
          END;

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
                  ,'Individual'
            FROM xxup.xxup_per_ps_action_history
            WHERE sequence_no = p_sequence_no
            AND approver_no = ln_approver_counter - 1; 

          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20101, 'error on insert: ' || SQLERRM);
          END;


          ln_approver_counter := ln_approver_counter + 1;

          wf_engine.setitemattrnumber('XXUPPSWF'
                                     ,p_sequence_no
                                     ,'APPROVER_COUNTER'
                                     ,ln_approver_counter);

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

        raise_application_error(-20101, 'Encountered error on Resubmit' || SUBSTR(SQLERRM,0,200));

    END resubmit; 





    PROCEDURE update_status(itemtype  IN VARCHAR2
                            ,l_itemkey IN VARCHAR2
                            ,actid     IN VARCHAR2
                            ,funcmode  IN VARCHAR2
                            ,resultout OUT VARCHAR2)
    IS

    lv_seq_no VARCHAR2(100);

    lv_appr_user_name fnd_user.user_name%TYPE;
    lv_project_name xxup_per_ps_header_tr.project_name%TYPE;

    lv_ntf_result VARCHAR2(30);
    ln_nid NUMBER := wf_engine.context_nid;

    ln_approver_counter NUMBER;
    ln_total_approver_count NUMBER;

    lv_approver_name VARCHAR2(150);
    lv_approver_emp_id per_all_people_f.person_id%TYPE;

    ln_update_ctr NUMBER; --update approver no on RFC

    lv_emp_name per_all_people_f.full_name%TYPE;
    ln_emp_id per_all_people_f.person_id%TYPE;
    lv_emp_pos_name per_all_positions.name%TYPE;
    lv_emp_org_name hr_all_organization_units.name%TYPE;


    BEGIN
    /*
        BEGIN
            SELECT 

        EXCEPTION 
            WHEN OTHERS THEN
                NULL;
        END;
    */

        lv_ntf_result := wf_notification.getattrtext(ln_nid, 'RESULT');

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

        IF lv_seq_no IS NULL OR lv_project_name IS NULL THEN
          raise_application_error(-20101, 'Encountered error on Update status - sequence no/project name missing');
        END IF;



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
                raise_application_error(-20101, 'Encountered error on Update status: ' || SUBSTR(SQLERRM,0,200));
        END;

        IF funcmode = 'RESPOND' THEN

                --get approver details
                BEGIN
                    SELECT user_name
                            ,(SELECT full_name
                              FROM per_all_people_f papf
                              WHERE person_id = employee_id
                              AND SYSDATE BETWEEN effective_start_date AND effective_end_date)
                      INTO lv_appr_user_name
                          ,lv_approver_name
                      FROM fnd_user
                      WHERE employee_id = (SELECT to_employee_id
                                           FROM xxup.xxup_per_ps_action_history pah
                                           WHERE sequence_no = lv_seq_no
                                           AND approver_no = ln_approver_counter);
                EXCEPTION
                    WHEN no_data_found THEN
                        raise_application_error(-20101, 'Encountered error on Update status - Approver data not found!');
                    WHEN too_many_rows THEN
                        raise_application_error(-20101, 'Encountered error on Update status - too many Approver data fetched!');
                    WHEN OTHERS THEN
                        raise_application_error(-20101, 'Encountered error on Update status - ' || SUBSTR(SQLERRM,0,200));
                END;



                SELECT COUNT(1)
                INTO ln_total_approver_count
                FROM xxup.xxup_per_ps_action_history
                WHERE sequence_no = lv_seq_no
                AND type = 'Individual';


                IF lv_ntf_result = 'RFC' THEN
                    BEGIN

                      wf_engine.setitemattrtext(itemtype
                                            ,l_itemkey
                                            ,'RFC_URL'
                                            ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_REQ&urlParam=RFC' ||
                                            --'&pNid=' || ln_nid ||
                                            '&pSequenceNo=' || lv_seq_no
                                            );            

                      wf_engine.setitemattrtext(itemtype
                                            ,l_itemkey
                                            ,'TITLE'
                                            ,'Public Service - '||  
                                            lv_project_name|| 
                                            ' has been returned for correction by '|| 
                                            lv_approver_name
                                             );



                      UPDATE xxup.xxup_per_ps_action_history
                            SET action = 'Return for correction'
                               ,action_date = SYSDATE
                               ,note = wf_engine.context_user_comment
                            WHERE sequence_no = lv_seq_no
                            AND type = 'Individual'
                            AND approver_no = ln_approver_counter;

                      COMMIT;


                      /*1. get all pending approvers and increment their approver no/sequence by 1    
                        2. insert record for the owner (pending resubmission)
                      */


                      UPDATE xxup.xxup_per_ps_action_history
                      SET approver_no = approver_no + 1
                      WHERE action = 'Pending'
                      AND type = 'Individual'
                      AND sequence_no = lv_seq_no;


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
                            ,ln_emp_id
                            ,lv_emp_name
                            ,lv_emp_pos_name
                            ,lv_emp_org_name
                            ,'Pending'
                            ,'Individual'
                      FROM xxup.xxup_per_ps_action_history
                      WHERE sequence_no = lv_seq_no
                      AND approver_no = ln_approver_counter;
                  EXCEPTION
                    WHEN OTHERS THEN
                      raise_application_error(-20101, 'Encountered error on Update status - return for correction: ' || SUBSTR(SQLERRM,0,200));
                  END;
                END IF;




                IF ln_approver_counter = ln_total_approver_count THEN


                      IF lv_ntf_result = 'APPROVE' THEN

                          wf_engine.setitemattrtext(itemtype
                                    ,l_itemkey
                                    ,'FYI_TITLE'
                                    ,'Public Service - '
                                    || lv_project_name
                                    || ' has been approved by '
                                    || lv_approver_name
                                    || ' and has been completed'
                                    );

                          -- UPDATE xxup.xxup_per_public_service_header
                          -- SET request_status = 'APPROVED'
                          -- WHERE sequence_no = lv_seq_no;


                         INSERT INTO xxup_per_public_service_header
                         (
                         sequence_no              
                        ,position_id              
                        ,project_name             
                        ,primary_role             
                        ,request_date             
                        ,responded_date           
                        ,start_date               
                        ,end_date                 
                        ,duration_hours           
                        ,type_of_activity         
                        ,source_of_fund           
                        ,cost_of_participation    
                        ,partner_org_or_inst      
                        ,beneficiary_category     
                        ,unit_of_beneficiary      
                        ,no_of_beneficiary        
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
                        ,project_type             
                        ,type_of_beneficiary      
                         )
                         SELECT sequence_no              
                                ,position_id              
                                ,project_name             
                                ,primary_role             
                                ,request_date             
                                ,responded_date           
                                ,start_date               
                                ,end_date                 
                                ,duration_hours           
                                ,type_of_activity         
                                ,source_of_fund           
                                ,cost_of_participation    
                                ,partner_org_or_inst      
                                ,beneficiary_category     
                                ,unit_of_beneficiary      
                                ,no_of_beneficiary        
                                ,country                  
                                ,post_act_eval_rating     
                                ,remarks                  
                                ,attribute1               
                                ,attribute2               
                                ,attribute3               
                                ,attribute4               
                                ,attribute5               
                                ,SYSDATE         
                                ,fnd_global.user_id          
                                ,null        
                                ,fnd_global.user_id               
                                ,SYSDATE            
                                ,project_type             
                                ,type_of_beneficiary
                          FROM xxup_per_ps_header_tr
                          WHERE sequence_no = lv_seq_no;


                          UPDATE xxup.xxup_per_ps_action_history
                          SET action = 'Approved'
                             ,action_date = SYSDATE
                             ,note = wf_engine.context_user_comment
                          WHERE sequence_no = lv_seq_no
                          AND type = 'Individual'
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

                          -- UPDATE xxup.xxup_per_public_service_header
                          -- SET request_status = 'Rejected by ' || lv_approver_name
                          -- WHERE sequence_no = lv_seq_no;

                          UPDATE xxup.xxup_per_ps_action_history
                          SET action = 'Rejected'
                             ,action_date = SYSDATE
                             ,note = wf_engine.context_user_comment
                          WHERE sequence_no = lv_seq_no
                          AND type = 'Individual'
                          AND approver_no = ln_approver_counter;

                      END IF;
                END IF;


          IF ln_approver_counter < ln_total_approver_count THEN

            IF lv_ntf_result = 'APPROVE' THEN

                BEGIN
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

                  UPDATE xxup.xxup_per_ps_action_history
                  SET action = 'Approved'
                     ,action_date = SYSDATE
                     ,note = wf_engine.context_user_comment
                  WHERE sequence_no = lv_seq_no
                  AND type = 'Individual'
                  AND approver_no = ln_approver_counter;
                EXCEPTION
                  WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Update status - approved: ' || SUBSTR(SQLERRM,0,200));
                END;

             ELSIF lv_ntf_result = 'REJECT' THEN

                BEGIN

                  wf_engine.setitemattrtext(itemtype
                                           ,l_itemkey
                                           ,'FYI_TITLE'
                                           ,'Public Service - '
                                           || lv_project_name
                                           || ' has been rejected by '
                                           || lv_approver_name
                                           );
                  UPDATE xxup.xxup_per_ps_action_history
                  SET action = 'Rejected'
                     ,action_date = SYSDATE
                     ,note = wf_engine.context_user_comment
                  WHERE sequence_no = lv_seq_no
                  AND type = 'Individual'
                  AND approver_no = ln_approver_counter;

                EXCEPTION
                  WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Update status - rejected: ' || SUBSTR(SQLERRM,0,200));
                END;
            END IF;



            ln_approver_counter := ln_approver_counter + 1;


            BEGIN

                SELECT user_name
                      ,(SELECT full_name
                        FROM per_all_people_f papf
                        WHERE person_id = employee_id)
                INTO lv_appr_user_name
                    ,lv_approver_name
                FROM fnd_user
                WHERE employee_id = (SELECT to_employee_id
                                     FROM xxup.xxup_per_ps_action_history pah
                                     WHERE sequence_no = lv_seq_no
                                     AND type = 'Individual'
                                     AND approver_no = ln_approver_counter);



            EXCEPTION 
                WHEN OTHERS THEN
                  raise_application_error(-20101, 'Encountered error on Update status - get Approver details: ' || SUBSTR(SQLERRM,0,200));
            END;


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


                  IF lv_ntf_result IN ('APPROVE', 'REJECT') THEN

                    wf_engine.setitemattrtext(itemtype
                                             ,l_itemkey
                                             ,'APPROVER_NAME'
                                             ,lv_approver_name);


                    wf_engine.setitemattrtext(itemtype
                                       ,l_itemkey
                                       ,'APPROVER'
                                       ,lv_appr_user_name);                 

                  END IF;
                 EXCEPTION 
                    WHEN OTHERS THEN
                      raise_application_error(-20101, 'Encountered error on Update status - set attributes: ' || SUBSTR(SQLERRM,0,200));
                END;


            END IF;

          END IF;

        END IF;

    END update_status;

END xxup_ps_wf_pkg;