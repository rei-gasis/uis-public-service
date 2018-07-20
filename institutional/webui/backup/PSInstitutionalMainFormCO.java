/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.publicservice.institutional.webui;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
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


        if (pageContext.isLovEvent()) {

            String lovInputId = pageContext.getLovInputSourceId();

            if ("Country".equals(lovInputId)) {
                OAViewObject vo = 
                    (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");

                Row row = vo.getCurrentRow();
                if (row.getAttribute("Country") != null && 
                    "Philippines".equals(row.getAttribute("Country").toString())) {
                    row.setAttribute("RenderAddress", true);
                } else {
                    row.setAttribute("RenderAddress", false);
                }
                //if(row.getAttribute("Country") == null || "".equals(row.getAttribute("Country").toString()) || !"Philippines".equals(row.getAttribute("Country").toString())){
            }
        }

        /*Show Beneficiary group when Unit of beneficiary is Organization*/

        if ("RenderOrganization".equals(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM))) {
            //System.out.println(pageContext.getParameter("pUnitOfBeneficiary").toString());

            OAViewObject vo = 
                (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");

            Row row = vo.getCurrentRow();

            if ("Organization".equals(row.getAttribute("UnitOfBeneficiary").toString())) {
                //System.out.println(row.getAttribute("UnitOfBeneficiary").toString());
                row.setAttribute("RenderOrgRN", true);
            } else {
                row.setAttribute("RenderOrgRN", false);
            }


        }


        if ("RenderDelModeOthers".equals(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM))) {

            OAViewObject mainVO = 
                (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");


            String eventRowSourceParam = 
                pageContext.getParameter(EVENT_SOURCE_ROW_REFERENCE);
            Row row = am.findRowByRef(eventRowSourceParam);

            //            System.out.println(eventRowSourceParam);

            String selectedMode = row.getAttribute("DeliveryMode").toString();
            String isSelected = row.getAttribute("Selected").toString();

            Row mainRow = mainVO.getCurrentRow();


            if ("Y".equals(isSelected) && "Others".equals(selectedMode)) {

                mainRow.setAttribute("RenderDelModeOthers", Boolean.TRUE);

            } else if ("N".equals(isSelected) && "Others".equals(selectedMode)) {
                mainRow.setAttribute("RenderDelModeOthers", Boolean.FALSE);
            }

        }
        
        
        if ("RenderActivityOthers".equals(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM))) {

            OAViewObject mainVO = 
                (OAViewObject)am.findViewObject("XxupPerPSInstTrEOVO1");


            String eventRowSourceParam = 
                pageContext.getParameter(EVENT_SOURCE_ROW_REFERENCE);
            Row row = am.findRowByRef(eventRowSourceParam);

            //            System.out.println(eventRowSourceParam);

            String selectedActivity = row.getAttribute("Activity").toString();
            String isSelected = row.getAttribute("Selected").toString();

            Row mainRow = mainVO.getCurrentRow();


            if ("Y".equals(isSelected) && "Others".equals(selectedActivity)) {

                mainRow.setAttribute("RenderActivityOthers", Boolean.TRUE);

            } else if ("N".equals(isSelected) && "Others".equals(selectedActivity)) {
                mainRow.setAttribute("RenderActivityOthers", Boolean.FALSE);
            }

        }
    }


}
