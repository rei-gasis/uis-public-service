/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.publicservice.institutional.webui;

import java.io.Serializable;

import java.util.Dictionary;

import oracle.apps.fnd.common.MessageToken;
import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.webui.OAAttachmentAPIHelper;
import oracle.apps.fnd.framework.webui.OAAttachmentUtils;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OADialogPage;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.TransactionUnitHelper;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

import oracle.apps.fnd.framework.webui.beans.form.OASubmitButtonBean;
import oracle.apps.fnd.framework.webui.beans.layout.OAAttachmentTableBean;
import oracle.apps.fnd.framework.webui.beans.layout.OAHeaderBean;
import oracle.apps.fnd.framework.webui.beans.message.OAMessageAttachmentLinkBean;
import oracle.apps.fnd.framework.webui.beans.message.OAMessageCheckBoxBean;
import oracle.apps.fnd.framework.webui.beans.nav.OAPageButtonBarBean;

import oracle.cabo.ui.action.ClientAction;

import oracle.jbo.Row;


/**
 * Controller for ...
 */
public class PSInstitutionalReviewCO extends OAControllerImpl {
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

        String sequenceNo = pageContext.getParameter("pSequenceNo");
        String actionFromURL = pageContext.getParameter("urlParam");
        String pItemKey = "";


        OAApplicationModule am = pageContext.getApplicationModule(webBean);

        OAViewObject mainVO = 
            (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");

        //        if(mainVO != null){
        //            pItemKey = mainVO.getCurrentRow().getAttribute("ItemKey").toString();
        //            System.out.println("ReviewCO > itemKey: " + pItemKey);
        //            // pItemKey = "INDIV-317";
        //        }

         // pItemKey = "INST-233";

        //        Serializable[] initApproversParams = new String[2];
        Serializable[] reviewPSParams = { pItemKey };


        am.invokeMethod("reviewPS", reviewPSParams);


        /*Handle RFC
               * change button
               * */
        OAWebBean rootWB = pageContext.getRootWebBean();

        OAPageButtonBarBean pageBtnBar = 
            (OAPageButtonBarBean)rootWB.findChildRecursive("PageButtonRN");
        OASubmitButtonBean submitBtn, cancelBtn, BackBtn;

        OAHeaderBean attachmentRN = 
            (OAHeaderBean)rootWB.findChildRecursive("AttachmentHRN");

        //        OAMessageCheckBoxBean checkbox =  (OAMessageCheckBoxBean) rootWB.findChildRecursive("SelectedMode");


        String actionParam = pageContext.getParameter("urlParam");

        if ("RFC".equals(actionParam)) {
            submitBtn = 
                    (OASubmitButtonBean)pageBtnBar.findChildRecursive("Submit");
            submitBtn.setText("Update");
        } else if ("View".equals(actionParam)) {
            submitBtn = 
                    (OASubmitButtonBean)pageBtnBar.findChildRecursive("Submit");
            cancelBtn = 
                    (OASubmitButtonBean)pageBtnBar.findChildRecursive("Cancel");

            submitBtn.setRendered(false);
            cancelBtn.setRendered(false);


            /*Show attachment read only*/
            if (attachmentRN != null) {

                attachmentRN.setRendered(false);

                OAHeaderBean attachmentReviewRN = 
                    (OAHeaderBean)this.createWebBean(pageContext, 
                                                     "/xxup/oracle/apps/per/publicservice/institutional/webui/PSInstitutionalAttachmentReviewRN", 
                                                     "AttachmentReviewRN", 
                                                     true);
                attachmentReviewRN.setApplicationModuleDefinitionName("xxup.oracle.apps.per.publicservice.institutional.server.PSInstitutionalAM");
                webBean.addIndexedChild(1, attachmentReviewRN);
            }


            //            OAMessageAttachmentLinkBean attachmentLink = 
            //                (OAMessageAttachmentLinkBean)rootWB.findChildRecursive("PSInstitutionalAttachment");
            //
            //            if (attachmentLink != null) {
            //                Dictionary[] entityMaps = attachmentLink.getEntityMappings();
            //                entityMaps[0].remove("insertAllowed");
            //                entityMaps[0].put("insertAllowed", false);
            //                entityMaps[0].put("updatetAllowed", false);
            //                entityMaps[0].put("deleteAllowed", false);
            //            }
            //

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

                //ignore actions from attachment functions
        if(("oaAddAttachment".equals(pageContext.getParameter(EVENT_PARAM))) ||
           ("oaGotoAttachments".equals(pageContext.getParameter(EVENT_PARAM))) ||
           ("oaUpdateAttachment".equals(pageContext.getParameter(EVENT_PARAM))) ||
           ("oaDeleteAttachment".equals(pageContext.getParameter(EVENT_PARAM))) ||
           ("oaViewAttachment".equals(pageContext.getParameter(EVENT_PARAM))) ||
           ("AddInlineAttachment".equals(pageContext.getParameter(EVENT_PARAM))) ||
           ("DeleteInlineAttachment".equals(pageContext.getParameter(EVENT_PARAM)))
          ){
            return;
        } 


        OAApplicationModule am = 
            (OAApplicationModule)pageContext.getApplicationModule(webBean);


        String sequenceNo = pageContext.getParameter("pSequenceNo");
        String actionParam = pageContext.getParameter("urlParam");
        String viewFrom = pageContext.getParameter("viewFrom");

        Serializable[] params = { sequenceNo };


//        pageContext.writeDiagnostics(this, "PSInstitutionalReviewCO:" + pageContext.getParameter(EVENT_PARAM), 1);
//
        // System.out.println("PSInstitutionalReviewCO:" + pageContext.getParameter(EVENT_PARAM));
//        pageContext.writeDiagnostics(this, "PSInstitutionalReviewCO:" + pageContext.getParameter(EVENT_PARAM), 1);
        
        
        


        if (pageContext.getParameter("Back") != null) {

            if ("View".equals(actionParam)) {

                TransactionUnitHelper.endTransactionUnit(pageContext, 
                                                         "PSCreateTxn");

                if ("individual".equals(viewFrom)) {

                    pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/publicservice/webui/PublicServiceSummaryPG", 
                                                   null, 
                                                   OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                                   null, null, false, 
                                                   OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);
                } else if ("institutional".equals(viewFrom)) {


                    pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/publicservice/institutional/webui/PSInstitutionalSummaryPG", 
                                                   null, 
                                                   OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                                   null, null, false, 
                                                   OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);
                }

            } else {

                am.invokeMethod("returnNonMemberVO");
                //                am.invokeMethod("returnNonMemberVO", null);

                TransactionUnitHelper.endTransactionUnit(pageContext, 
                                                         "PSCreateTxn");

                pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/publicservice/institutional/webui/PSInstitutionalRequestPG" + 
                                               "&pSequenceNo=" + sequenceNo + 
                                               "&urlParam=" + actionParam +
                                               "&backUsed=" + "yes",
                                               null, 
                                               OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                               null, null, true, 
                                               OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);
            }
            //am.invokeMethod("removeRecord");

            /*}else if(pageContext.getParameter("Add") != null) {*/


            //restrict resubmit if add attachment button is clicked
            /*}else if("oaViewAttachment".equals(pageContext.getParameter(EVENT_PARAM))){
                        throw new OAException("view");

                */
        } else if (pageContext.getParameter("DialogOk") != null) {

            pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/publicservice/institutional/webui/PSInstitutionalSummaryPG", 
                                           null, 
                                           OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                           null, null, false, 
                                           OAWebBeanConstants.ADD_BREAD_CRUMB_NO);

        } else if ("RFC".equals(actionParam)) {
            
            OAViewObject vo = (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");
            String pItemKey = pageContext.getParameter("pItemKey");
            Serializable[] reviewPSParams = { pItemKey };
            am.invokeMethod("reviewPS", reviewPSParams);
            
            
            vo.reset();
            Row row = vo.next();
//            Row row = vo.getCurrentRow();

            if(vo.getRowCount() == 1){
                String projName = row.getAttribute("ProjectName").toString();
                
                System.out.println("projName: " + projName);


                MessageToken[] tokens = { new MessageToken("PROJ_NAME", projName) };

                         
                OAException resubmitMessage = 
                    new OAException("XXUP", "UP_HR_PS_RESUBMIT_MSG", tokens, 
                                    OAException.INFORMATION, null);
                    
                OADialogPage dialogPage = 
                new OADialogPage(OAException.INFORMATION, resubmitMessage, 
                                 null, "", null);


                dialogPage.setOkButtonToPost(true);
                dialogPage.setOkButtonLabel("Ok");
                dialogPage.setOkButtonItemName("DialogOk");

                dialogPage.setPostToCallingPage(true);

                String itemKey = pageContext.getParameter("pItemKey");
                Serializable[] resubmitParams = { itemKey };
                am.invokeMethod("resubmitPS", resubmitParams);


                pageContext.redirectToDialogPage(dialogPage);
                
                
                
            }

            


            //OADialogPage dialogPage = new OADialogPage(()  


            //pageContext.forwardImmediately("OA.jsp?page=/oracle/apps/fnd/framework/navigate/webui/NewHomePG",
            


        } else if (pageContext.getParameter("Submit") != null) {

            OAViewObject vo = 
                (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");

            // vo.executeQuery();
            //            Row row = vo.getCurrentRow();
            vo.reset();
            Row row = vo.next();

            String pSequenceNo = row.getAttribute("SequenceNo").toString();
            String pItemKey = row.getAttribute("ItemKey").toString();

            Serializable[] initWFParams = { pSequenceNo, pItemKey };

            /*start workflow*/
            am.invokeMethod("initWF", initWFParams);


            String projName = row.getAttribute("ProjectName").toString();
            MessageToken[] tokens = 
            { new MessageToken("PROJ_NAME", projName) };

            OAException confirmMessage = 
                new OAException("XXUP", "UP_HR_PS_CREATE_PUB_SER_CONF", tokens, 
                                OAException.INFORMATION, null);

            OADialogPage dialogPage = 
                new OADialogPage(OAException.INFORMATION, confirmMessage, null, 
                                 "", null);


            dialogPage.setOkButtonToPost(true);
            dialogPage.setOkButtonLabel("Ok");
            dialogPage.setOkButtonItemName("DialogOk");

            dialogPage.setPostToCallingPage(true);


            pageContext.redirectToDialogPage(dialogPage);

            //pageContext.putDialogMessage(confirmMessage);

            //pageContext.forwardImmediately("OA.jsp?page=/oracle/apps/fnd/framework/navigate/webui/NewHomePG",


        } else if (pageContext.getParameter("Cancel") != null) {
            am.invokeMethod("rollbackPS", null);

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


