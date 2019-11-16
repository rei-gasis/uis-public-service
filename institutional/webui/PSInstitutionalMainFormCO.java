/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.publicservice.institutional.webui;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;


import oracle.apps.fnd.framework.webui.beans.form.OACheckBoxBean;

import oracle.apps.fnd.framework.webui.beans.message.OAMessageCheckBoxBean;

import oracle.cabo.ui.action.FirePartialAction;

import oracle.jbo.Row;


/**
 * Controller for ...
 */
public class PSInstitutionalMainFormCO extends OAControllerImpl {
    public static final String RCS_ID = "$Header$";
    public static final boolean RCS_ID_RECORDED = 
        VersionInfo.recordClassVersion(RCS_ID, "%packagename%");

    /**
     * Layout and page setup logic for a region.
     * @param pageContext the current OA page context
     * @param webBean the web bean corresponding to the region
     */
    public void processRequest(OAPageContext pageContext, OAWebBean webBean) {
        super.processRequest(pageContext, webBean);

        //    FirePartialAction pprRenderOthers = new FirePartialAction();
        //    pprRenderOthers.setEvent("RenderDelModeOthers");
        //    pprRenderOthers.setUnvalidated(false);
        //    
        //    OAWebBean rootWB = pageContext.getRootWebBean();
        //    OAMessageCheckBoxBean chkboxSelectedDelMode = (OAMessageCheckBoxBean) rootWB.findChildRecursive("SelectedMode");
        //    
        //    chkboxSelectedDelMode.setPrimaryClientAction(pprRenderOthers);

        String actionFromURL = pageContext.getParameter("urlParam");
        
        


        OAApplicationModule am = 
            (OAApplicationModule)pageContext.getApplicationModule(webBean);

        Connection conn = 
            pageContext.getApplicationModule(webBean).getOADBTransaction().getJdbcConnection();



        /*on init, hide some fields*/
        OAViewObject mainVO = 
            (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");

        mainVO.reset();
        Row mRow = mainVO.next();

        //set sequenceNo 
        String sequenceNo = "";
        try {
            // System.out.println(row.getAttribute("PositionId").toString());
            sequenceNo = String.format("%010d", Integer.parseInt(mRow.getAttribute("SequenceNo").toString()));

            mRow.setAttribute("SequenceNoDisplay", sequenceNo);
            // System.out.println("SequenceNoDisplay" + sequenceNo);
        } catch (Exception ex) {
            throw new OAException("Setting sequenceNo: " + ex);
        }

        //exit for UPDATE when setting VO attr
        if("Update".equals(actionFromURL)){

            return;
        }


        try {
            if("".equals(mRow.getAttribute("ItemKey"))){
                mRow.setAttribute("RenderAddress", false);
                mRow.setAttribute("RenderOrgRN", false);
                mRow.setAttribute("RenderSubjAreaOthers", false);
                mRow.setAttribute("RenderActivityOthers", false);
                mRow.setAttribute("RenderDelModeOthers", false);

            }

            
        } catch (Exception ex) {
            //throws null pointer exception when rendering Organization from ReviewPG to RequestPG
            // throw new OAException("Exception " + ex);
        }


        //set assignmentID onInit
        String assignmentId = "";
        try {
            // System.out.println(row.getAttribute("PositionId").toString());
            String Query = 
                "SELECT assignment_id " + "FROM per_all_assignments_f paaf " + 
                "WHERE SYSDATE BETWEEN effective_start_date AND effective_end_date " + 
                "AND person_id = fnd_global.employee_id " + 
                "AND primary_flag = 'Y'";


            PreparedStatement stmt = conn.prepareStatement(Query);
            // stmt.setString(1, row.getAttribute("PositionId").toString());
            // stmt.setString(1, project_id);  
            for (ResultSet resultset = stmt.executeQuery(); resultset.next(); 
            ) {

                assignmentId = resultset.getString("assignment_id");

            }

            mRow.setAttribute("AssignmentId", assignmentId);
            // System.out.println("AssignmentId" + assignmentId);
        } catch (Exception ex) {
            throw new OAException("Exception" + ex);
        }

        

    }

    /**
     * Procedure to handle form submissions for form elements in
     * a region.
     * @param pageContext the current OA page context
     * @param webBean the web bean corresponding to the region
     */
    public void processFormRequest(OAPageContext pageContext, 
                                   OAWebBean webBean) {
        super.processFormRequest(pageContext, webBean);

        OAApplicationModule am = pageContext.getApplicationModule(webBean);

        //
        //        String eventParam = 
        //            pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM);
        //        System.out.println(eventParam);
        //
        //        String anotherParam = pageContext.getParameter(EVENT_PARAM);
        //        System.out.println(anotherParam);
        //
        //
        //        String anotherParam1 = pageContext.getParameter("event");
        //        System.out.println(anotherParam1);

        OAViewObject mainVO = 
            (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");


        if (pageContext.isLovEvent()) {
            String lovInputId = pageContext.getLovInputSourceId();
            Row row = mainVO.getCurrentRow();

            if ("Country".equals(lovInputId)) {
                OAViewObject couVO = 
                    (OAViewObject)am.findViewObject("XxupPerPSInstCountriesTrEOVO1");


                String showAddressRN = 
                    (String)am.invokeMethod("checkPhilippines", null);

                if ("Y".equals(showAddressRN))
                    row.setAttribute("RenderAddress", true);
                else
                    row.setAttribute("RenderAddress", false);
                //if(row.getAttribute("Country") == null || "".equals(row.getAttribute("Country").toString()) || !"Philippines".equals(row.getAttribute("Country").toString())){
            }
        }

        /*Show Beneficiary group when Unit of beneficiary is Organization*/

        if ("RenderOrganization".equals(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM))) {
            //System.out.println(pageContext.getParameter("pUnitOfBeneficiary").toString());


            Row row = mainVO.getCurrentRow();

            if ("Organization".equals(row.getAttribute("UnitOfBeneficiary").toString())) {
                //System.out.println(row.getAttribute("UnitOfBeneficiary").toString());
                row.setAttribute("RenderOrgRN", true);
            } else {
                row.setAttribute("RenderOrgRN", false);
            }


        }


        if ("RenderDelModeOthers".equals(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM))) {

            //            OAViewObject mainVO = 
            //                (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");


            String eventRowSourceParam = 
                pageContext.getParameter(EVENT_SOURCE_ROW_REFERENCE);
            Row row = am.findRowByRef(eventRowSourceParam);

            //            System.out.println(eventRowSourceParam);

            String selectedMode = row.getAttribute("DeliveryMode").toString();
            String isSelected = row.getAttribute("Selected").toString();

            Row mainRow = mainVO.getCurrentRow();


            if ("Y".equals(isSelected) && "Others".equals(selectedMode)) {

                mainRow.setAttribute("RenderDelModeOthers", Boolean.TRUE);

            } else if ("N".equals(isSelected) && 
                       "Others".equals(selectedMode)) {
                mainRow.setAttribute("RenderDelModeOthers", Boolean.FALSE);
            }

        }


        if ("RenderActivityOthers".equals(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM))) {

            //            OAViewObject mainVO = 
            //                (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");


            String eventRowSourceParam = 
                pageContext.getParameter(EVENT_SOURCE_ROW_REFERENCE);
            Row row = am.findRowByRef(eventRowSourceParam);

            //            System.out.println(eventRowSourceParam);

            String selectedActivity = row.getAttribute("Activity").toString();
            String isSelected = row.getAttribute("Selected").toString();

            Row mainRow = mainVO.getCurrentRow();


            if ("Y".equals(isSelected) && "Others".equals(selectedActivity)) {

                mainRow.setAttribute("RenderActivityOthers", Boolean.TRUE);

            } else if ("N".equals(isSelected) && 
                       "Others".equals(selectedActivity)) {
                mainRow.setAttribute("RenderActivityOthers", Boolean.FALSE);
            }

        }


        if ("RenderSubjAreaOthers".equals(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM))) {

            String eventRowSourceParam = 
                pageContext.getParameter(EVENT_SOURCE_ROW_REFERENCE);
            Row row = am.findRowByRef(eventRowSourceParam);

            //            System.out.println(eventRowSourceParam);

            String selectedSubj = row.getAttribute("Attribute1").toString();
            String isSelected = row.getAttribute("Selected").toString();

            Row mainRow = mainVO.getCurrentRow();


            if ("Y".equals(isSelected) && "Others".equals(selectedSubj)) {

                mainRow.setAttribute("RenderSubjAreaOthers", Boolean.TRUE);

            } else if ("N".equals(isSelected) && 
                       "Others".equals(selectedSubj)) {
                mainRow.setAttribute("RenderSubjAreaOthers", Boolean.FALSE);
            }

        }
    }


}
