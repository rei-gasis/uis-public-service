package xxup.oracle.apps.per.publicservice.server;

import oracle.apps.fnd.framework.server.OAViewObjectImpl;

import oracle.jbo.Row;


// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class XxupPerPublicServiceHeaderEOVOImpl extends OAViewObjectImpl {
    /**This is the default constructor (do not remove)
     */
    public XxupPerPublicServiceHeaderEOVOImpl() {
    }

    public void initNewVO() {
        setMaxFetchSize(0);
        Row row = createRow();
        insertRow(row);
        executeQuery();
    }

    public void initExistingPS(String sequenceNo) {
        setWhereClauseParams(null);
        setWhereClause("sequence_no = :1");
        setWhereClauseParam(0, sequenceNo);
        executeQuery();
    }   
    
    
    public void showSummaryVO(){
        setWhereClause(null);
        setWhereClauseParams(null);
        setOrderByClause(null);
        //setWhereClause("created_by = fnd_global.user_id");
        
        
        //String ownerId = "fnd_global.user_id";
        String requestStatus = "APPROVED";
        
        setWhereClause("created_by = fnd_global.user_id AND request_status = :1");
        
        
        //setWhereClauseParam(1, ownerId);
        setWhereClauseParam(0, requestStatus);
        
        setOrderByClause("sequence_no DESC");
        
        
        
        executeQuery();
    }
    
    
}
