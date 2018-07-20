/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.publicservice.institutional.webui;

import java.util.ArrayList;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;
import oracle.apps.fnd.framework.webui.beans.table.OATableBean;

import oracle.jbo.Row;

/**
 * Controller for ...
 */
public class PSInstitutionalTeamMembersCO extends OAControllerImpl
{
  public static final String RCS_ID="$Header$";
  public static final boolean RCS_ID_RECORDED =
        VersionInfo.recordClassVersion(RCS_ID, "%packagename%");

  /**
   * Layout and page setup logic for a region.
   * @param pageContext the current OA page context
   * @param webBean the web bean corresponding to the region
   */
  public void processRequest(OAPageContext pageContext, OAWebBean webBean)
  {
    super.processRequest(pageContext, webBean);
    
    
  }

  /**
   * Procedure to handle form submissions for form elements in
   * a region.
   * @param pageContext the current OA page context
   * @param webBean the web bean corresponding to the region
   */
  public void processFormRequest(OAPageContext pageContext, OAWebBean webBean)
  {
    super.processFormRequest(pageContext, webBean);
    
//    OATableBean membersTable = (OATableBean) webBean.findChildRecursive("TeamMembersAT");
//    System.out.println(membersTable.getName(pageContext));
//    
    OAApplicationModule am = (OAApplicationModule) pageContext.getApplicationModule(webBean);
    OAViewObject vo = (OAViewObject) am.findViewObject("XxupPerPSInstMembersEOVO1");
    
    
    
    ArrayList<Row> rows = new ArrayList<Row>();
//    Row[] rows;
    

    if("addRows".equals(pageContext.getParameter(EVENT_PARAM))){
        
        for(Row row : vo.getAllRowsInRange()){
            String fullName = String.valueOf(row.getAttribute("FullName"));
            String position = String.valueOf(row.getAttribute("Position"));
            String organization = String.valueOf(row.getAttribute("Organization"));
            
            System.out.println(fullName);
            System.out.println(position);
            System.out.println(organization);
            
            
            rows.add(row);
            
            
        }
        
        
        
        
    }
//    
    
  }

}
