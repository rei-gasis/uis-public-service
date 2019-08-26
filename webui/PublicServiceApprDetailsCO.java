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
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

import oracle.jbo.Row;

/**
 * Controller for ...
 */
public class PublicServiceApprDetailsCO extends OAControllerImpl
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
        

    String sequenceNo = pageContext.getParameter("pSequenceNo");
    String pItemKey = pageContext.getParameter("pItemKey");

    // String pItemKey = "INDIV-317";

    OAApplicationModule am = pageContext.getApplicationModule(webBean);

    // System.out.println("ApprDetailsCO > Itemkey: " + pItemKey);
    // Serializable[] initApproversParams = new String[2];
    Serializable[] reviewPSParams = { pItemKey };

    am.invokeMethod("reviewPS", reviewPSParams);


  
      
      /*OAViewObject vo = (OAViewObject)am.findViewObject("XxupPerPublicServiceHeaderEOVO1");

      
      String type = "";
      if(vo!=null){
      
        
        Row row = vo.getCurrentRow();
            
            if(row!=null)        {
                System.out.println("ya");
            }
        
        
        
        
      }
      
      
      pageContext.putDialogMessage(new OAException(type));*/
      
//      OAViewObject pshVO = (OAViewObject) am.findViewObject("XxupPerPSHeaderTrEOVO1");
//      pshVO.executeQuery();
      
    
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
  }

}
