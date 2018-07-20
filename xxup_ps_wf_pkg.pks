create or replace PACKAGE xxup_ps_wf_pkg
IS
    PROCEDURE init_wf(p_sequence_no IN VARCHAR2);

    PROCEDURE set_attributes(itemtype  IN VARCHAR2
                            ,l_itemkey IN VARCHAR2
                            ,actid     IN VARCHAR2
                            ,funcmode  IN VARCHAR2
                            ,resultout OUT VARCHAR2);
    PROCEDURE init_approvers(p_sequence_no VARCHAR2);

    PROCEDURE update_status(itemtype  IN VARCHAR2
                            ,l_itemkey IN VARCHAR2
                            ,actid     IN VARCHAR2
                            ,funcmode  IN VARCHAR2
                            ,resultout OUT VARCHAR2);

    PROCEDURE resubmit(p_sequence_no VARCHAR2);
END xxup_ps_wf_pkg;