/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.publicservice.webui;

import com.sun.java.util.collections.HashMap;


import java.util.Dictionary;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.beans.OAImageBean;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;
import oracle.apps.fnd.framework.webui.beans.nav.OALinkBean;

import oracle.apps.fnd.framework.webui.beans.table.OAAdvancedTableBean;

import oracle.jbo.Row;

/**
 * Controller for ...
 */
public class PublicServiceSummaryCO extends OAControllerImpl {
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
        
        OAApplicationModule am = (OAApplicationModule) pageContext.getApplicationModule(webBean);
        
        am.invokeMethod("showSummaryVO");
        
        
        /*set Destination URI of View Image for Individual and Institutional*/
        
//         OAWebBean rootWB = pageContext.getRootWebBean();
//         OAAdvancedTableBean summaryTable = (OAAdvancedTableBean) rootWB.findChildRecursive("SummaryAT");
//         
//         Dictionary[] viewImages = summaryTable.get
////         OAImageBean viewDetailsImg = (OAImageBean) rootWB.findChildRecursive("ViewDetails");
//        
//        OAViewObject approvedPSVO = (OAViewObject) am.findViewObject("XxupPerPSApprovedVO1");
//        
//        if(approvedPSVO != null){
//            String strType = "";
//            
//            for(Row rowi = approvedPSVO.first(); rowi != null; rowi = approvedPSVO.next()){
////                System.out.println(rowi.getAttribute("SequenceNo").toString());
////                System.out.println(rowi.getAttribute("Type").toString());
//                 if(rowi.getAttribute("Type") != null){
//                     strType = rowi.getAttribute("Type").toString();
//                     
////                     System.out.println(strType);
//                     if("individual".equals(strType)){
//                         viewDetailsImg.setDestination("");
//                     }else if("institutional".equals(strType)){
//                         viewDetailsImg.setDestination("");
//                     }
//
//                }
//            
//            }   
//        
//    
                
            
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
            
    }

}
