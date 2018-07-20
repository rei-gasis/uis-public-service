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

        String actionFromURL = pageContext.getParameter("urlParam");

        String sequenceNo = pageContext.getParameter("pSequenceNo");

        //        System.out.println(actionFromURL);

        Serializable[] params = { sequenceNo };

        if ("Create".equals(actionFromURL) || actionFromURL == null) {
            am.invokeMethod("initVOForNewRequest");

        } else if ("Update".equals(actionFromURL) || 
                   "RFC".equals(actionFromURL) || 
                   "Back".equals(actionFromURL) || actionFromURL == null) {


            am.invokeMethod("updatePS", params);


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
                    (OAViewObject)am.findViewObject("XxupPerPSInstMembersEOVO1");

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


                if (row.getAttribute("DurationHours") == null) {
                    errMsg.add(new OAException("Duration is required", 
                                               OAException.ERROR));
                }

                if (row.getAttribute("ImplementationFrequency") == null) {
                    errMsg.add(new OAException("Frequency of Implementation is required", 
                                               OAException.ERROR));
                }

                OAViewObject catVo = 
                    (OAViewObject)am.findViewObject("XxupPerPSInstObjCatEOVO1");

                /*Validate if objective category is empty*/
                int objCatCount = 0;
                if (catVo != null) {
                    for (Row rowi: catVo.getAllRowsInRange()) {

                        //String objCat = rowi.getAttribute("Attribute1").toString();

                        if (rowi.getAttribute("ObjectiveCategory") != null) {
                            objCatCount++;
                        }

                    }

                    //            System.out.println(objCatCount);

                }

                if (objCatCount <= 0) {
                    //error += "Please fill at least one Objective Category" + "<br/>";
                    errMsg.add(new OAException("Please fill at least one Objective Category", 
                                               OAException.ERROR));

                }

                if (row.getAttribute("TypeOfActivity") == null) {
                    errMsg.add(new OAException("Type of Activity is required", 
                                               OAException.ERROR));
                }


                /*Validate if subject area of interest is empty*/
                OAViewObject subjVo = 
                    (OAViewObject)am.findViewObject("XxupPerPSInstSubjEOVO1");


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
                    (OAViewObject)am.findViewObject("XxupPerPSInstBenefTypeEOVO1");


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


                if (errMsg.size() >= 1) {
                    MessageToken[] tokens = 
                    { new MessageToken("ERROR_MSG", error) };
                    OAException.raiseBundledOAException((List)errMsg);
                    throw new OAException("XXUP", "UP_HR_PS_FORM_VALID_MSG", 
                                          tokens, OAException.ERROR, null);
                }


                //store child tables

                am.invokeMethod("saveDetails");

                //                if("Create".equals(actionParam)){
                am.invokeMethod("commitTransaction");
                //                }


                pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/publicservice/institutional/webui/PSInstitutionalReviewPG&pSequenceNo=" + 
                                               strSequenceNo + "&urlParam=" + 
                                               actionParam, null, 
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
