//recrea
/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.publicservice.webui;

import com.sun.java.util.collections.List;
import com.sun.java.util.collections.ArrayList;

import java.io.Serializable;

//import java.util.ArrayList;

import oracle.apps.fnd.common.MessageToken;
import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.server.OAViewRowImpl;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.TransactionUnitHelper;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

import oracle.apps.fnd.framework.webui.beans.message.OAMessageLovInputBean;

import oracle.apps.fnd.framework.webui.beans.message.OAMessageTextInputBean;
import oracle.apps.fnd.framework.webui.beans.nav.OAPageButtonBarBean;

import oracle.jbo.Row;

/**
 * Controller for ...
 */
public class PublicServiceRequestCO extends OAControllerImpl {
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
        
        /*
        OAMessageLovInputBean countryLov = (OAMessageLovInputBean) webBean.findChildRecursive("Country");
        countryLov.set
        countryLov.setValue(pageContext, "Philippines");
        */
        
         OAApplicationModule am = 
             (OAApplicationModule)pageContext.getApplicationModule(webBean);
             
         String actionFromURL = pageContext.getParameter("urlParam");

         String sequenceNo = 
             pageContext.getParameter("pSequenceNo");

         //String sequenceNo = "1950";
         
         if ("Create".equals(actionFromURL) || actionFromURL == null) {
             am.invokeMethod("initVOForNewRequest");

         } else if ("Update".equals(actionFromURL) 
                || "RFC".equals(actionFromURL) 
                || "Back".equals(actionFromURL) 
                || actionFromURL == null
                ) {
             
             Serializable[] params = { sequenceNo };
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
            //OAMessageTextInputBean  = 
            //OAMessageLovInputBean asgPosLov = (OAMessageLovInputBean)webBean.findChildRecursive("PositionName");
            //OAMessageLovInputBean asgPosLov = (OAMessageLovInputBean)webBean.findChildRecursive("ProjectName");
            /*OAMessageLovInputBean asgPosLov = (OAMessageLovInputBean)webBean.findChildRecursive("PositionName");
            OAMessageLovInputBean asgPosLov = (OAMessageLovInputBean)webBean.findChildRecursive("PositionName");
            
            //System.out.println(asgPosLov.get);
            
            if("".equals(asgPosLov.getText())){
                errMsg.add(new OAException("Assignment Position field is required",OAException.ERROR));
            }
            
            */
            
             /*Validate required fields*/
            OAViewObject mainVo = 
                (OAViewObject)am.findViewObject("XxupPerPublicServiceHeaderEOVO1");
                
            if (mainVo != null) {
                row = mainVo.getCurrentRow();
                
                if(row.getAttribute("SequenceNo") != null){
                    strSequenceNo = row.getAttribute("SequenceNo").toString();
                }
            
                
                if(row.getAttribute("PositionName") == null) {
                    errMsg.add(new OAException("Assignment Position is required",OAException.ERROR));
                }
                
                
                if(row.getAttribute("ProjectName") != null){
                    strProjectName =row.getAttribute("ProjectName").toString();
                }else {
                    errMsg.add(new OAException("Project Name is required",OAException.ERROR));
                }
                

            
            
                
                
                if(row.getAttribute("PrimaryRole") == null) {
                    errMsg.add(new OAException("Primary Role is required",OAException.ERROR));
                }
                
                
                if(row.getAttribute("StartDate") == null) {
                    errMsg.add(new OAException("Start Date is required",OAException.ERROR));
                }
                
                if(row.getAttribute("TypeOfActivityDisplay") == null) {
                    errMsg.add(new OAException("Type of Activity is required",OAException.ERROR));
                }
                
                if(row.getAttribute("SourceOfFund") == null) {
                    errMsg.add(new OAException("Source of Fund is required",OAException.ERROR));
                }
                
                if(row.getAttribute("TypeOfBeneficiary") == null) {
                    errMsg.add(new OAException("Type of Beneficiary is required",OAException.ERROR));
                }
                
                if(row.getAttribute("NoOfBeneficiary") == null) {
                    errMsg.add(new OAException("No of Beneficiary is required",OAException.ERROR));
                }
                
                if(row.getAttribute("Country") == null) {
                    errMsg.add(new OAException("Country is required",OAException.ERROR));
                }
                
                
                
            }
            
            
            row = null;
            
            OAViewObject catVo = 
                (OAViewObject)am.findViewObject("XxupPerPublicServiceCatEOVO1");
            
            /*Validate if objective category is empty*/
            int objCatCount = 0;
            if (catVo != null) {
                for (Row rowi: catVo.getAllRowsInRange()) {
                    
                    //String objCat = rowi.getAttribute("Attribute1").toString();
                    
                    if (rowi.getAttribute("Attribute1") != null) {
                        objCatCount++;
                    }
                
                
                
                
                }
                
            //System.out.println(objCatCount);
           
            }
            
            if(objCatCount <= 0){
               //error += "Please fill at least one Objective Category" + "<br/>";
               errMsg.add(new OAException("Please fill at least one Objective Category",OAException.ERROR));
               
            }
            
            
            
            
            
            /*Validate if subject area of interest is empty*/
            OAViewObject subjVo = 
                (OAViewObject)am.findViewObject("XxupPerPublicServiceSubjEOVO1");
            
            
            Row selectedRows [] = null;
            
            /*Validate if objective category is empty*/
            int subjCount = 0;
            if (subjVo != null) {
                selectedRows = subjVo.getFilteredRows("Selected", "Y");
                
            }
            
            if(selectedRows.length<=0) {
               //error += "Please select at least one Subject Area of Interest" + "<br>";
                errMsg.add(new OAException("Please select at least one Subject Area of Interest",OAException.ERROR));
            }    




            



             
             
            if(errMsg.size()>=1){
                //MessageToken[] tokens ={ new MessageToken("ERROR_MSG",  error ) };
                OAException.raiseBundledOAException((List)errMsg);
                //throw new OAException("XXUP", "UP_HR_PS_FORM_VALID_MSG", tokens, OAException.ERROR, null);
            }
             
            
             //store child tables
            am.invokeMethod("saveDetails");
            am.invokeMethod("commitTransaction");
            
             
            
            pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/publicservice/webui/PublicServiceReviewPG&pSequenceNo=" + strSequenceNo + "&urlParam=" + actionParam, 
                                           null, 
                                           OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                           null, null, true, 
                                           OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);
                                           
            
        } else if (pageContext.getParameter("Cancel") != null) {
            am.invokeMethod("rollbackPS", null);

            /*TransactionUnitHelper.endTransactionUnit(pageContext, 
                                                     "PSCreateTxn");
            am.invokeMethod("showSummaryVO");
            
            pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/publicservice/webui/PublicServiceSummaryPG", 
                                           null, 
                                           OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                           null, null, false, 
                                           OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);
             */
             pageContext.setForwardURL("OA.jsp?page=/xxup/oracle/apps/per/publicservice/webui/PublicServiceSummaryPG",
                                        null,
                                        OAWebBeanConstants.KEEP_MENU_CONTEXT,
                                        null,
                                        null,
                                        false,
                                        OAWebBeanConstants.ADD_BREAD_CRUMB_NO,
                                        OAWebBeanConstants.IGNORE_MESSAGES);
        }


    }

}
