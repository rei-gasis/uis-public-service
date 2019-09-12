/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.publicservice.webui;

import java.io.Serializable;

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

import oracle.jbo.Row;

import oracle.apps.fnd.framework.OAFwkConstants;

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

        
        /*on init, hide some fields*/
        OAViewObject mainVO = (OAViewObject) am.findViewObject("XxupPerPSHeaderTrEOVO1");
        Row mRow = mainVO.getCurrentRow();


        try{
            mRow.setAttribute("RenderAddress", false);
            mRow.setAttribute("RenderOrgRN", false);
            mRow.setAttribute("RenderSubjAreaOthers", false);
        }catch(Exception ex){
            //throws null pointer exception when rendering Organization from ReviewPG to RequestPG
            // throw new OAException("Exception " + ex);
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

        Connection conn = pageContext.getApplicationModule(webBean).getOADBTransaction().getJdbcConnection();  
    
        OAViewObject mainVO = (OAViewObject) am.findViewObject("XxupPerPSHeaderTrEOVO1");


        
        /*Show address when Country is Philippines*/
        if(pageContext.isLovEvent()){
            
            String lovInputId = pageContext.getLovInputSourceId();
            OAViewObject vo = (OAViewObject) am.findViewObject("XxupPerPSHeaderTrEOVO1");
            // Row row = vo.getCurrentRow();
            vo.reset();
            Row row = vo.next();
            
            // if("Country".equals(lovInputId)){
                
            //     if(row.getAttribute("Country")!=null && "Philippines".equals(row.getAttribute("Country").toString())){
            //         row.setAttribute("RenderAddress", true);
            //     }else {
            //         row.setAttribute("RenderAddress", false);
            //     }
            // }else 
            if("PositionName".equals(lovInputId)){
                // if("".equals(row.getAttribute("PositionName"))){
                //     return;
                // }

                String assignmentId = "";

                try{
                    // System.out.println(row.getAttribute("PositionId").toString());
                    String Query = "SELECT assignment_id " +
                               "FROM per_all_assignments_f paaf " +
                               "WHERE SYSDATE BETWEEN effective_start_date AND effective_end_date " +
                               "AND person_id = fnd_global.employee_id " +
                               "AND position_id = ?";

                    PreparedStatement stmt = conn.prepareStatement(Query);  
                    stmt.setString(1, row.getAttribute("PositionId").toString());
                    // stmt.setString(1, project_id);  
                    for(ResultSet resultset = stmt.executeQuery(); resultset.next();)  
                    {  

                        assignmentId = resultset.getString("assignment_id");

                        // System.out.println("assignment_id: " + resultset.getString("assignment_id"));
                    }

                    row.setAttribute("AssignmentId", assignmentId);
                }catch(Exception ex){
                    // throw new OAException("Exception" + ex);
                    
                }
                
            }
            else if("Country".equals(lovInputId)){
                OAViewObject couVO = (OAViewObject)am.findViewObject("XxupPerPSCountriesTrEOVO1");

                

                String showAddressRN = (String) am.invokeMethod("checkPhilippines", null);

                if("Y".equals(showAddressRN))
                    row.setAttribute("RenderAddress", true);
                else
                    row.setAttribute("RenderAddress", false);
                // Serializable[] checkCountryParams = {  };


                // Row countryRow = couVO.getCurrentRow();
                
                // if(couVO != null){
                //     if(countryRow.getAttribute("Country")!=null && "Philippines".equals(countryRow.getAttribute("Country").toString())){
                //         row.setAttribute("RenderAddress", true);
                //     }else {
                //        row.setAttribute("RenderAddress", false);
                //     }
                // }



            }
        }
        
        
        
        
        
        
        
        
        /*Show Beneficiary group when Unit of beneficiary is Organization*/
        if("RenderOrganization".equals(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM))){
            //System.out.println(pageContext.getParameter("pUnitOfBeneficiary").toString());
            
             

             if(pageContext.isLoggingEnabled(OAFwkConstants.STATEMENT))  
             {  
                if(mainVO != null)
                    pageContext.writeDiagnostics(this, "PS Main Transaction view - found" ,OAFwkConstants.STATEMENT);    
                else
                    pageContext.writeDiagnostics(this, "PS Main Transaction view - NOT found" ,OAFwkConstants.STATEMENT);    
             }  

             
             Row row = mainVO.getCurrentRow();
             
             if("Organization".equals(row.getAttribute("UnitOfBeneficiary").toString())){
                 //System.out.println(row.getAttribute("UnitOfBeneficiary").toString());
                  row.setAttribute("RenderOrgRN",true);
             }else{
                 row.setAttribute("RenderOrgRN",false);
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

            } else if ("N".equals(isSelected) && "Others".equals(selectedSubj)) {
                mainRow.setAttribute("RenderSubjAreaOthers", Boolean.FALSE);
            }

        }
        

    }
        
        
        
        
        
        
    

}
