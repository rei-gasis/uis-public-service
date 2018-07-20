package xxup.oracle.apps.per.publicservice.institutional.server;

import oracle.apps.fnd.framework.server.OAViewObjectImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class XxupPerPSInstDeliveryModeEOVOImpl extends OAViewObjectImpl {
    /**This is the default constructor (do not remove)
     */
    public XxupPerPSInstDeliveryModeEOVOImpl() {
    }
    
    
    public void initNewRecord(){
        setMaxFetchSize(0);
        setOrderByClause("DELIVERY_MODE");
        executeQuery();
    }
    
    public void initExistingPS(String sequenceNumber) {
        setWhereClauseParams(null);
        setWhereClause("sequence_no = :1");
        setWhereClauseParam(0, sequenceNumber);
        setOrderByClause("DELIVERY_MODE");  
        executeQuery();
    }
}
