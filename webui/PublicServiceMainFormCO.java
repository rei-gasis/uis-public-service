/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.publicservice.webui;

import java.io.Serializable;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

import oracle.jbo.Row;

/**
 * Controller for ...
 */
public class PublicServiceMainFormCO extends OAControllerImpl {
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
        OAApplicationModule am = pageContext.getApplicationModule(webBean);

        
        
        
        
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
        
        
        
        /*Show address when Country is Philippines*/
        if(pageContext.isLovEvent()){
            
            String lovInputId = pageContext.getLovInputSourceId();
            
            if("Country".equals(lovInputId)){
                OAViewObject vo = (OAViewObject) am.findViewObject("XxupPerPublicServiceHeaderEOVO1");
                
                Row row = vo.getCurrentRow();
                if(row.getAttribute("Country")!=null && "Philippines".equals(row.getAttribute("Country").toString())){
                    row.setAttribute("RenderAddress", true);
                }else {
                    row.setAttribute("RenderAddress", false);
                }
            }
            
        }
        
        
            
        
        
        
        
        
        /*Show Beneficiary group when Unit of beneficiary is Organization*/
        if("RenderOrganization".equals(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM))){
            //System.out.println(pageContext.getParameter("pUnitOfBeneficiary").toString());
            
             OAViewObject vo = (OAViewObject) am.findViewObject("XxupPerPublicServiceHeaderEOVO1");
             
             Row row = vo.getCurrentRow();
             
             if("Organization".equals(row.getAttribute("UnitOfBeneficiary").toString())){
                 //System.out.println(row.getAttribute("UnitOfBeneficiary").toString());
                  row.setAttribute("RenderOrgRN",true);
             }else{
                 row.setAttribute("RenderOrgRN",false);
             }   
                 
             
            
            
        }
        

    }
        
        
        
        
        
        
    

}
