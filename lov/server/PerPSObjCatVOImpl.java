package xxup.oracle.apps.per.publicservice.lov.server;

import oracle.apps.fnd.framework.server.OAViewObjectImpl;

// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class PerPSObjCatVOImpl extends OAViewObjectImpl {
    /**This is the default constructor (do not remove)
     */
    public PerPSObjCatVOImpl() {
    }

    public void showList(){
    	setWhereClause("1=1");  
        executeQuery();
    }
}
