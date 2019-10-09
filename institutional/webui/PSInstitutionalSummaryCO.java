/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.publicservice.institutional.webui;

import java.io.Serializable;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

/**
 * Controller for ...
 */
public class PSInstitutionalSummaryCO extends OAControllerImpl {
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


        String access_level = "";
        Serializable[] params = new String[1];

        OAApplicationModule am = 
            (OAApplicationModule)pageContext.getApplicationModule(webBean);


        try {

            Connection conn = 
                pageContext.getApplicationModule(webBean).getOADBTransaction().getJdbcConnection();

            String Query = 
                "SELECT FND_PROFILE.value('XXUP_PER_PS_SUMMARY_ACCESS_LEVEL') access_level FROM DUAL";

            PreparedStatement stmt = conn.prepareStatement(Query);
            // stmt.setString(1, project_id);  
            for (ResultSet resultset = stmt.executeQuery(); resultset.next(); 
            ) {
                // pageContext.writeDiagnostics(this, "Query Executed", 1);  
                access_level = resultset.getString("access_level");
                // params[0] = access_level;
                params[0] = access_level;
                pageContext.writeDiagnostics(this, 
                                             "Query Executed" + access_level, 
                                             1);

                System.out.println(resultset.getString("access_level"));
            }

            am.invokeMethod("showSummaryVO", params);

        } catch (Exception exception) {
            throw new OAException("Error in Getting Access Level" + exception, 
                                  OAException.ERROR);
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
    }

}
