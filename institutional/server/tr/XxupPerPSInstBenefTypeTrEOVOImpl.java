package xxup.oracle.apps.per.publicservice.institutional.server.tr;

import oracle.apps.fnd.framework.server.OAViewObjectImpl;

import oracle.jbo.Row;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class XxupPerPSInstBenefTypeTrEOVOImpl extends OAViewObjectImpl {
    /**This is the default constructor (do not remove)
     */
    public XxupPerPSInstBenefTypeTrEOVOImpl() {
    }
    
    public void initNewRecord() {
        setMaxFetchSize(0);
        executeQuery();
        Row row = createRow();
        insertRow(row);
        row.setNewRowState(Row.STATUS_INITIALIZED);
    }

    public void initTranPS(String itemKey) {
        setWhereClauseParams(null);
        setWhereClause("item_key = :1");
        setWhereClauseParam(0, itemKey);
        executeQuery();
    }
}
