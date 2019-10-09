package xxup.oracle.apps.per.publicservice.webui;

import java.io.Serializable;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;


public class PublicServiceViewDetailsCO extends OAControllerImpl {
    public static final String RCS_ID = "$Header$";
    public static final boolean RCS_ID_RECORDED = 
        VersionInfo.recordClassVersion(RCS_ID, "%packagename%");

    public PublicServiceViewDetailsCO() {
    }

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

        Serializable[] viewDetailsParam = { sequenceNo };


        am.invokeMethod("viewDetails", viewDetailsParam);


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


        String sequenceNo = pageContext.getParameter("pSequenceNo");
        // String itemKey = pageContext.getParameter("pItemKey");


        String actionParam = pageContext.getParameter("urlParam");

        Serializable[] params = { sequenceNo };


        if (pageContext.getParameter("Back") != null) {

            if ("View".equals(actionParam)) {

                pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/publicservice/webui/PublicServiceSummaryPG", 
                                               null, 
                                               OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                               null, null, false, 
                                               OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);
            }

        }
    }
}
