package xxup.oracle.apps.per.publicservice.institutional.server;

import oracle.apps.fnd.framework.server.OAViewObjectImpl;

import oracle.jbo.Row;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class XxupPerPSInstOtherMembersVOImpl extends OAViewObjectImpl {
    /**This is the default constructor (do not remove)
     */
    public XxupPerPSInstOtherMembersVOImpl() {
    }
    
    public void initNewRecord() {
        setMaxFetchSize(0);
        executeQuery();
        Row row = createRow();
        
        insertRow(row);
        
        row.setNewRowState(Row.STATUS_INITIALIZED);
        
    }
    
    
    public void initExistingPS(String sequenceNo){
        setWhereClause(null);
        setWhereClause("sequence_no = :1");
        setWhereClauseParam(0, sequenceNo);
        
        executeQuery();
        
    
    }
}
