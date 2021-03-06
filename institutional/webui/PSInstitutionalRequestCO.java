/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.publicservice.institutional.webui;

import com.sun.java.util.collections.ArrayList;
import com.sun.java.util.collections.List;

import java.io.Serializable;

import oracle.apps.fnd.common.MessageToken;
import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.TransactionUnitHelper;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

import oracle.jbo.Row;


/**
 * Controller for ...
 */
public class PSInstitutionalRequestCO extends OAControllerImpl {
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

        OAApplicationModule am = 
            (OAApplicationModule)pageContext.getApplicationModule(webBean);


        String backUsed = pageContext.getParameter("backUsed");

        if("yes".equals(backUsed)){
            return; 
        }


        // String pItemKey = pageContext.getParameter("pItemKey");
        

//        return;

        String actionFromURL = pageContext.getParameter("urlParam");

        String sequenceNo = pageContext.getParameter("pSequenceNo");

        //        System.out.println(actionFromURL);

        Serializable[] params = { sequenceNo };


        if ("Create".equals(actionFromURL) || actionFromURL == null) {
             am.invokeMethod("initVOForNewRequest");
            
//test rfc
//String pItemKey = "U-INST-240-7";
//Serializable[] updatePSParams = { pItemKey };
//am.invokeMethod("updatePS", updatePSParams);

            //            OAViewObject mainVO = (OAViewObject) am.findViewObject("XxupPerPSInstTrEOVO1");
            //             mainVO.setWhereClauseParams(null);
            //             mainVO.setWhereClause("item_key = :1");
            //             mainVO.setWhereClauseParam(0, "INST-96");
            //             mainVO.executeQuery();
            //             
            //             Row row = mainVO.getCurrentRow();
            //             
            //             for(Object obj : mainVO.get
            //            
            //             if(row.getAttribute("ItemKey") != null){
            //                 String paramItemKey = row.getAttribute("ItemKey").toString();     
            //            
            //                 Serializable[] updatePSParams = { paramItemKey };
            //                 am.invokeMethod("updatePS", updatePSParams);
            //             }

        } else if ("Update".equals(actionFromURL)) {

            Serializable[] initTranRecordParams = { sequenceNo };
            am.invokeMethod("initTranRecord", initTranRecordParams);

            
            OAViewObject mainVO = (OAViewObject) am.findViewObject("XxupPerPSInstTrEOVO1");

            mainVO.reset();
            Row mainRow = mainVO.next();

            /*check country if have value to show addressRN*/
            OAViewObject couVO = (OAViewObject) am.findViewObject("XxupPerPSInstCountriesTrEOVO1");

            if(couVO.getRowCount() >= 1){
                couVO.reset();
                while(couVO.hasNext()){
                    Row couRow = couVO.next();    

                    if("Philippines".equals(couRow.getAttribute("Country"))){
                        mainRow.setAttribute("RenderAddress", true);   
                        break;
                    }else{
                        mainRow.setAttribute("RenderAddress", false);
                    }
                    
                }
                
            }else{
                mainRow.setAttribute("RenderAddress", false);
            }


            /*check subj others is checked then show subj others*/
            OAViewObject subjVO = (OAViewObject) am.findViewObject("XxupPerPSInstSubjEOVO1");

            if(subjVO.getRowCount() >= 1){
                subjVO.reset();
                while(subjVO.hasNext()){
                    Row subjRow = subjVO.next();    

                    if("Others".equals(subjRow.getAttribute("Attribute5"))){
                            String strSubjAreaOthers = "";
                        if(subjRow.getAttribute("Attribute1") != null){
                            strSubjAreaOthers = subjRow.getAttribute("Attribute1").toString();    
                        }
                        

                        mainRow.setAttribute("RenderSubjAreaOthers", true);   
                        mainRow.setAttribute("SubjAreaOthers", strSubjAreaOthers);   
                        break;

                    }else{
                        mainRow.setAttribute("RenderSubjAreaOthers", false);
                    }
                    
                }
                
            }else{
                mainRow.setAttribute("RenderSubjAreaOthers", false);
            }

            /*check delmode others*/
            OAViewObject delModeVO = (OAViewObject) am.findViewObject("XxupPerPSInstDeliveryModeEOVO1");

            if(delModeVO.getRowCount() >= 1){
                delModeVO.reset();
                while(delModeVO.hasNext()){
                    Row delModeRow = delModeVO.next();    

                    if("Others".equals(delModeRow.getAttribute("DeliveryMode"))){
                        String strDelModeOthers = "";
                        if(delModeRow.getAttribute("Attribute1") != null){
                            strDelModeOthers = delModeRow.getAttribute("Attribute1").toString();    
                        }
                        

                        mainRow.setAttribute("RenderDelModeOthers", true);   
                        mainRow.setAttribute("DelModeOthers", strDelModeOthers);   

                        break;
                    }else{
                        mainRow.setAttribute("RenderDelModeOthers", false);
                    }
                    
                }
                
            }else{
                mainRow.setAttribute("RenderDelModeOthers", false);
            }


            /*check act others*/
            OAViewObject actVO = (OAViewObject) am.findViewObject("XxupPerPSInstActivitiesEOVO1");

            if(actVO.getRowCount() >= 1){
                actVO.reset();
                while(actVO.hasNext()){
                    Row actRow = actVO.next();    

                    if("Others".equals(actRow.getAttribute("Activity"))){
                        String strActOthers = "";
                        if(actRow.getAttribute("Attribute1") != null){
                            strActOthers = actRow.getAttribute("Attribute1").toString();    
                        }
                        

                        mainRow.setAttribute("RenderActivityOthers", true);   
                        mainRow.setAttribute("ActivityOthers", strActOthers);   
                        break;



                    }else{
                        mainRow.setAttribute("RenderActivityOthers", false);
                    }
                    
                }
                
            }else{
                mainRow.setAttribute("RenderActivityOthers", false);
            }




        // } 
        // else if ("Back".equals(actionFromURL)) {

            // OAViewObject mainVO = 
            //     (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");

            // if (mainVO != null) {
            //     mainVO.reset();
            //     Row row = mainVO.next();
            //     // Row row = mainVO.getCurrentRow();

            //     System.out.println("rowcount: " + mainVO.getRowCount());

            //     String paramItemKey = "";
            //     if (row.getAttribute("ItemKey") != null) {
            //         paramItemKey = 
            //             row.getAttribute("ItemKey").toString();
            //     }

            //     // String paramItemKey = row.getAttribute("ItemKey") != null ? currRow.getAttribute("SequenceNo").toString() : "";

            //     Serializable[] updatePSParams = { paramItemKey };
            //     am.invokeMethod("updatePS", updatePSParams);
                

            // }
        }else if("RFC".equals(actionFromURL)){
            String pItemKey = pageContext.getParameter("pItemKey");
            Serializable[] updatePSParams = { pItemKey };
            am.invokeMethod("updatePS", updatePSParams);

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


        OAApplicationModule am = 
            (OAApplicationModule)pageContext.getApplicationModule(webBean);


        String actionParam = pageContext.getParameter("urlParam");
        String error = "";


        List errMsg = new ArrayList();


        if (pageContext.getParameter("Next") != null) {

            String strSequenceNo = "";
            String strProjectName = "";
            Row row = null;


            /*Validate required fields*/
            OAViewObject mainVo = 
                (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");

            if (mainVo != null) {
                row = mainVo.getCurrentRow();

                if (row.getAttribute("SequenceNo") != null) {
                    strSequenceNo = row.getAttribute("SequenceNo").toString();
                }


                if (row.getAttribute("ConstituentUniversity") == null) {
                    errMsg.add(new OAException("Constituent University is required", 
                                               OAException.ERROR));
                }

                if (row.getAttribute("ProjectName") != null) {
                    strProjectName = 
                            row.getAttribute("ProjectName").toString();
                } else {
                    errMsg.add(new OAException("Project Name is required", 
                                               OAException.ERROR));
                }


                if (row.getAttribute("ProjectLeader") == null) {
                    errMsg.add(new OAException("Project Leader is required", 
                                               OAException.ERROR));
                }

                OAViewObject membersVo = 
                    (OAViewObject)am.findViewObject("XxupPerPSInstMembersTrEOVO1");

                int memberCount = 0;
                if (membersVo != null) {
                    for (Row rowi: membersVo.getAllRowsInRange()) {
                        if (rowi.getAttribute("FullName") != null) {
                            memberCount++;
                        }
                    }
                }

                if (memberCount <= 0) {
                    errMsg.add(new OAException("Please fill at least one Team Member", 
                                               OAException.ERROR));
                }

                if (row.getAttribute("ImplementationStartDate") == null) {
                    errMsg.add(new OAException("Start Date of implementation is required", 
                                               OAException.ERROR));
                }


                if (row.getAttribute("Duration") == null) {
                    errMsg.add(new OAException("Duration is required", 
                                               OAException.ERROR));
                }

                if (row.getAttribute("ImplementationFrequency") == null) {
                    errMsg.add(new OAException("Frequency of Implementation is required", 
                                               OAException.ERROR));
                }

                OAViewObject catVo = 
                    (OAViewObject)am.findViewObject("XxupPerPSInstObjCatTrEOVO1");

                /*Validate if objective category is empty*/
                int objCatCount = 0;
                if (catVo != null) {
                    for (Row rowi: catVo.getAllRowsInRange()) {

                        //String objCat = rowi.getAttribute("Attribute1").toString();

                        if (rowi.getAttribute("ObjectiveCategory") != null) {
                            objCatCount++;
                            break;
                        }

                    }

                    //            System.out.println(objCatCount);

                }

                if (objCatCount <= 0) {
                    //error += "Please fill at least one Objective Category" + "<br/>";
                    errMsg.add(new OAException("Please fill at least one Objective Category", 
                                               OAException.ERROR));

                }

                // if (row.getAttribute("TypeOfActivity") == null) {
                //     errMsg.add(new OAException("Type of Activity is required", 
                //                                OAException.ERROR));
                // }


                /*Validate if subject area of interest is empty*/
                OAViewObject subjVo = 
                    (OAViewObject)am.findViewObject("XxupPerPSInstSubjTrEOVO1");


                Row selectedRows[] = null;

                int subjCount = 0;
                if (subjVo != null) {
                    selectedRows = subjVo.getFilteredRows("Selected", "Y");
                }

                if (selectedRows.length <= 0) {
                    errMsg.add(new OAException("Please select at least one Subject Area of Interest", 
                                               OAException.ERROR));
                }


                if (row.getAttribute("CostOfParticipation") == null) {
                    errMsg.add(new OAException("Cost of Participation is required", 
                                               OAException.ERROR));
                }

                /*Validate if type of beneficiary is empty*/
                OAViewObject benefTypeVO = 
                    (OAViewObject)am.findViewObject("XxupPerPSInstBenefTypeTrEOVO1");


                Row selectedBenefTypeRow[] = null;

                int benefTypeCount = 0;
                if (benefTypeVO != null) {
                    selectedBenefTypeRow = 
                            benefTypeVO.getFilteredRows("Selected", "Y");
                }

                if (selectedBenefTypeRow.length <= 0) {
                    errMsg.add(new OAException("Please select at least one Type of Beneficiary", 
                                               OAException.ERROR));
                }


                if (row.getAttribute("NoOfBeneficiary") == null) {
                    errMsg.add(new OAException("No of Beneficiary is required", 
                                               OAException.ERROR));
                }

                /*Validate if type of activities is empty*/
                OAViewObject toaVO = 
                    (OAViewObject)am.findViewObject("XxupPerPSInstToaTrEOVO1");


                int toaCount = 0;

                if (toaVO != null) {
                    for (Row rowi: toaVO.getAllRowsInRange()) {

                        if (rowi.getAttribute("TypeOfActivity") != null) {
                            toaCount++;
                            break;
                        }
                    }


                }

                if (toaCount <= 0) {
                    //error += "Please fill at least one Objective Category" + "<br/>";
                    errMsg.add(new OAException("Please fill at least one Type of Activity", 
                                               OAException.ERROR));
                }


                /*Validate if countries is empty*/
                OAViewObject couVO = 
                    (OAViewObject)am.findViewObject("XxupPerPSInstCountriesTrEOVO1");


                int couCount = 0;

                if (couVO != null) {
                    for (Row rowi: couVO.getAllRowsInRange()) {

                        if (rowi.getAttribute("Country") != null) {
                            couCount++;
                            break;
                        }
                    }

                    //System.out.println(objCatCount);

                }

                if (couCount <= 0) {
                    //error += "Please fill at least one Objective Category" + "<br/>";
                    errMsg.add(new OAException("Please fill at least one Country", 
                                               OAException.ERROR));
                }

                if (errMsg.size() >= 1) {
                    MessageToken[] tokens = 
                    { new MessageToken("ERROR_MSG", error) };
                    OAException.raiseBundledOAException((List)errMsg);

                    throw new OAException("XXUP", "UP_HR_PS_FORM_VALID_MSG", 
                                          tokens, OAException.ERROR, null);
                }


                //init approver list/approval history

                String itemKey = "";
                try {

                    String assignmentId = "";

                    String actionFromURL = 
                        pageContext.getParameter("urlParam");
                    String sequenceNo = "";


                    Serializable[] initApproversParams = new String[3];

                    OAViewObject mainVO = 
                        (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");
                    row = null;

                    if (mainVO != null) {
                        System.out.println("rowcount:" + mainVO.getRowCount());

                        mainVO.reset();
                        row = mainVO.next();


                        //                    System.out.println("PublicServiceRequestCO > SubjAreaOthers: " + row.getAttribute("SubjAreaOthers").toString());

                        //Item key will be initialize on creation of approver list
                        //Only be created if not yet initialized
                        if (row.getAttribute("ItemKey") == null) {
                            if (row.getAttribute("AssignmentId") != null) {
                                assignmentId = 
                                        row.getAttribute("AssignmentId").toString();
                                sequenceNo = 
                                        row.getAttribute("SequenceNo").toString();
                                initApproversParams[0] = assignmentId;
                                initApproversParams[1] = sequenceNo;
                                initApproversParams[2] = actionFromURL;

                                // System.out.println("1assignmentId:" + assignmentId);
                                // System.out.println("SequenceNo:" + row.getAttribute("SequenceNo").toString());
                            }

                            System.out.println("assignmentId: " + 
                                               assignmentId);


                            itemKey = 
                                    (String)am.invokeMethod("initApprovers", initApproversParams);

                            System.out.println("assignmentId: " + 
                                               assignmentId);
                            System.out.println("sequenceNo: " + sequenceNo);
                            System.out.println("actionFromURL: " + 
                                               actionFromURL);
                            System.out.println("itemKey: " + itemKey);

                            row.setAttribute("ItemKey", itemKey);

                        } else {
                            if (mainVO != null) {
                                row = mainVO.getCurrentRow();

                                if (row.getAttribute("ItemKey") != null) {
                                    itemKey = row.getAttribute("ItemKey").toString();
                                }

                            }

                        }
                    }


                } catch (Exception ex) {
                    throw new OAException("Unable to set approver list: " + 
                                          ex);
                }


                //store child tables

                am.invokeMethod("saveDetails");
                am.invokeMethod("commitTransaction");


                pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/publicservice/institutional/webui/PSInstitutionalReviewPG&pSequenceNo=" + 
                                               strSequenceNo + "&urlParam=" + 
                                               actionParam + "&pItemKey=" + 
//                                                "RFC" + "&pItemKey=" + 
                                               itemKey, null, 
                                               OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                               null, null, true, 
                                               OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);

            }

        } else if (pageContext.getParameter("Cancel") != null) {

            TransactionUnitHelper.endTransactionUnit(pageContext, 
                                                     "PSCreateTxn");

            pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/publicservice/institutional/webui/PSInstitutionalSummaryPG", 
                                           null, 
                                           OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                           null, null, false, 
                                           OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);
        }

    }
}
