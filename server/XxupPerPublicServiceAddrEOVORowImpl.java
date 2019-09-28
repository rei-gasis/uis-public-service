package xxup.oracle.apps.per.publicservice.server;

import oracle.apps.fnd.framework.server.OAViewRowImpl;

import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.RowID;
import oracle.jbo.server.AttributeDefImpl;

import xxup.oracle.apps.per.publicservice.schema.server.XxupPerPublicServiceAddrEOImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class XxupPerPublicServiceAddrEOVORowImpl extends OAViewRowImpl {
    public static final int SEQUENCENO = 0;
    public static final int LINENUMBER = 1;
    public static final int ADDRESS = 2;
    public static final int ATTRIBUTE1 = 3;
    public static final int ATTRIBUTE2 = 4;
    public static final int ATTRIBUTE4 = 5;
    public static final int ATTRIBUTE5 = 6;
    public static final int LASTUPDATEDATE = 7;
    public static final int LASTUPDATEDBY = 8;
    public static final int LASTUPDATELOGIN = 9;
    public static final int CREATEDBY = 10;
    public static final int CREATIONDATE = 11;
    public static final int ROWID = 12;

    /**This is the default constructor (do not remove)
     */
    public XxupPerPublicServiceAddrEOVORowImpl() {
    }

    /**Gets XxupPerPublicServiceAddrEO entity object.
     */
    public XxupPerPublicServiceAddrEOImpl getXxupPerPublicServiceAddrEO() {
        return (XxupPerPublicServiceAddrEOImpl)getEntity(0);
    }

    /**Gets the attribute value for SEQUENCE_NO using the alias name SequenceNo
     */
    public Number getSequenceNo() {
        return (Number) getAttributeInternal(SEQUENCENO);
    }

    /**Sets <code>value</code> as attribute value for SEQUENCE_NO using the alias name SequenceNo
     */
    public void setSequenceNo(Number value) {
        setAttributeInternal(SEQUENCENO, value);
    }

    /**Gets the attribute value for LINE_NUMBER using the alias name LineNumber
     */
    public Number getLineNumber() {
        return (Number) getAttributeInternal(LINENUMBER);
    }

    /**Sets <code>value</code> as attribute value for LINE_NUMBER using the alias name LineNumber
     */
    public void setLineNumber(Number value) {
        setAttributeInternal(LINENUMBER, value);
    }

    /**Gets the attribute value for ADDRESS using the alias name Address
     */
    public String getAddress() {
        return (String) getAttributeInternal(ADDRESS);
    }

    /**Sets <code>value</code> as attribute value for ADDRESS using the alias name Address
     */
    public void setAddress(String value) {
        setAttributeInternal(ADDRESS, value);
    }

    /**Gets the attribute value for ATTRIBUTE1 using the alias name Attribute1
     */
    public String getAttribute1() {
        return (String) getAttributeInternal(ATTRIBUTE1);
    }

    /**Sets <code>value</code> as attribute value for ATTRIBUTE1 using the alias name Attribute1
     */
    public void setAttribute1(String value) {
        setAttributeInternal(ATTRIBUTE1, value);
    }

    /**Gets the attribute value for ATTRIBUTE2 using the alias name Attribute2
     */
    public String getAttribute2() {
        return (String) getAttributeInternal(ATTRIBUTE2);
    }

    /**Sets <code>value</code> as attribute value for ATTRIBUTE2 using the alias name Attribute2
     */
    public void setAttribute2(String value) {
        setAttributeInternal(ATTRIBUTE2, value);
    }

    /**Gets the attribute value for ATTRIBUTE4 using the alias name Attribute4
     */
    public String getAttribute4() {
        return (String) getAttributeInternal(ATTRIBUTE4);
    }

    /**Sets <code>value</code> as attribute value for ATTRIBUTE4 using the alias name Attribute4
     */
    public void setAttribute4(String value) {
        setAttributeInternal(ATTRIBUTE4, value);
    }

    /**Gets the attribute value for ATTRIBUTE5 using the alias name Attribute5
     */
    public String getAttribute5() {
        return (String) getAttributeInternal(ATTRIBUTE5);
    }

    /**Sets <code>value</code> as attribute value for ATTRIBUTE5 using the alias name Attribute5
     */
    public void setAttribute5(String value) {
        setAttributeInternal(ATTRIBUTE5, value);
    }

    /**Gets the attribute value for LAST_UPDATE_DATE using the alias name LastUpdateDate
     */
    public Date getLastUpdateDate() {
        return (Date) getAttributeInternal(LASTUPDATEDATE);
    }

    /**Sets <code>value</code> as attribute value for LAST_UPDATE_DATE using the alias name LastUpdateDate
     */
    public void setLastUpdateDate(Date value) {
        setAttributeInternal(LASTUPDATEDATE, value);
    }

    /**Gets the attribute value for LAST_UPDATED_BY using the alias name LastUpdatedBy
     */
    public Number getLastUpdatedBy() {
        return (Number) getAttributeInternal(LASTUPDATEDBY);
    }

    /**Sets <code>value</code> as attribute value for LAST_UPDATED_BY using the alias name LastUpdatedBy
     */
    public void setLastUpdatedBy(Number value) {
        setAttributeInternal(LASTUPDATEDBY, value);
    }

    /**Gets the attribute value for LAST_UPDATE_LOGIN using the alias name LastUpdateLogin
     */
    public Number getLastUpdateLogin() {
        return (Number) getAttributeInternal(LASTUPDATELOGIN);
    }

    /**Sets <code>value</code> as attribute value for LAST_UPDATE_LOGIN using the alias name LastUpdateLogin
     */
    public void setLastUpdateLogin(Number value) {
        setAttributeInternal(LASTUPDATELOGIN, value);
    }

    /**Gets the attribute value for CREATED_BY using the alias name CreatedBy
     */
    public Number getCreatedBy() {
        return (Number) getAttributeInternal(CREATEDBY);
    }

    /**Sets <code>value</code> as attribute value for CREATED_BY using the alias name CreatedBy
     */
    public void setCreatedBy(Number value) {
        setAttributeInternal(CREATEDBY, value);
    }

    /**Gets the attribute value for CREATION_DATE using the alias name CreationDate
     */
    public Date getCreationDate() {
        return (Date) getAttributeInternal(CREATIONDATE);
    }

    /**Sets <code>value</code> as attribute value for CREATION_DATE using the alias name CreationDate
     */
    public void setCreationDate(Date value) {
        setAttributeInternal(CREATIONDATE, value);
    }

    /**Gets the attribute value for ROWID using the alias name RowID
     */
    public RowID getRowID() {
        return (RowID) getAttributeInternal(ROWID);
    }

    /**getAttrInvokeAccessor: generated method. Do not modify.
     */
    protected Object getAttrInvokeAccessor(int index, 
                                           AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case SEQUENCENO:
            return getSequenceNo();
        case LINENUMBER:
            return getLineNumber();
        case ADDRESS:
            return getAddress();
        case ATTRIBUTE1:
            return getAttribute1();
        case ATTRIBUTE2:
            return getAttribute2();
        case ATTRIBUTE4:
            return getAttribute4();
        case ATTRIBUTE5:
            return getAttribute5();
        case LASTUPDATEDATE:
            return getLastUpdateDate();
        case LASTUPDATEDBY:
            return getLastUpdatedBy();
        case LASTUPDATELOGIN:
            return getLastUpdateLogin();
        case CREATEDBY:
            return getCreatedBy();
        case CREATIONDATE:
            return getCreationDate();
        case ROWID:
            return getRowID();
        default:
            return super.getAttrInvokeAccessor(index, attrDef);
        }
    }

    /**setAttrInvokeAccessor: generated method. Do not modify.
     */
    protected void setAttrInvokeAccessor(int index, Object value, 
                                         AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case SEQUENCENO:
            setSequenceNo((Number)value);
            return;
        case LINENUMBER:
            setLineNumber((Number)value);
            return;
        case ADDRESS:
            setAddress((String)value);
            return;
        case ATTRIBUTE1:
            setAttribute1((String)value);
            return;
        case ATTRIBUTE2:
            setAttribute2((String)value);
            return;
        case ATTRIBUTE4:
            setAttribute4((String)value);
            return;
        case ATTRIBUTE5:
            setAttribute5((String)value);
            return;
        case LASTUPDATEDATE:
            setLastUpdateDate((Date)value);
            return;
        case LASTUPDATEDBY:
            setLastUpdatedBy((Number)value);
            return;
        case LASTUPDATELOGIN:
            setLastUpdateLogin((Number)value);
            return;
        case CREATEDBY:
            setCreatedBy((Number)value);
            return;
        case CREATIONDATE:
            setCreationDate((Date)value);
            return;
        default:
            super.setAttrInvokeAccessor(index, value, attrDef);
            return;
        }
    }
}