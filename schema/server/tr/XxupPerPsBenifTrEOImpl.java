package xxup.oracle.apps.per.publicservice.schema.server.tr;

import oracle.apps.fnd.framework.server.OAEntityDefImpl;
import oracle.apps.fnd.framework.server.OAEntityImpl;

import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.RowID;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityDefImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class XxupPerPsBenifTrEOImpl extends OAEntityImpl {
    public static final int SEQUENCENO = 0;
    public static final int LINENUMBER = 1;
    public static final int BENEFICIARYORGANIZATION = 2;
    public static final int CONTACTDETAILS = 3;
    public static final int ATTRIBUTE1 = 4;
    public static final int ATTRIBUTE2 = 5;
    public static final int ATTRIBUTE3 = 6;
    public static final int ATTRIBUTE4 = 7;
    public static final int ATTRIBUTE5 = 8;
    public static final int LASTUPDATEDATE = 9;
    public static final int LASTUPDATEDBY = 10;
    public static final int LASTUPDATELOGIN = 11;
    public static final int CREATEDBY = 12;
    public static final int CREATIONDATE = 13;
    public static final int ITEMKEY = 14;
    public static final int ROWID = 15;
    private static OAEntityDefImpl mDefinitionObject;

    /**This is the default constructor (do not remove)
     */
    public XxupPerPsBenifTrEOImpl() {
    }

    /**Retrieves the definition object for this instance class.
     */
    public static synchronized EntityDefImpl getDefinitionObject() {
        if (mDefinitionObject == null) {
            mDefinitionObject = 
                    (OAEntityDefImpl)EntityDefImpl.findDefObject("xxup.oracle.apps.per.publicservice.schema.server.tr.XxupPerPsBenifTrEO");
        }
        return mDefinitionObject;
    }

    /**Gets the attribute value for SequenceNo, using the alias name SequenceNo
     */
    public Number getSequenceNo() {
        return (Number)getAttributeInternal(SEQUENCENO);
    }

    /**Sets <code>value</code> as the attribute value for SequenceNo
     */
    public void setSequenceNo(Number value) {
        setAttributeInternal(SEQUENCENO, value);
    }

    /**Gets the attribute value for LineNumber, using the alias name LineNumber
     */
    public Number getLineNumber() {
        return (Number)getAttributeInternal(LINENUMBER);
    }

    /**Sets <code>value</code> as the attribute value for LineNumber
     */
    public void setLineNumber(Number value) {
        setAttributeInternal(LINENUMBER, value);
    }

    /**Gets the attribute value for BeneficiaryOrganization, using the alias name BeneficiaryOrganization
     */
    public String getBeneficiaryOrganization() {
        return (String)getAttributeInternal(BENEFICIARYORGANIZATION);
    }

    /**Sets <code>value</code> as the attribute value for BeneficiaryOrganization
     */
    public void setBeneficiaryOrganization(String value) {
        setAttributeInternal(BENEFICIARYORGANIZATION, value);
    }

    /**Gets the attribute value for ContactDetails, using the alias name ContactDetails
     */
    public String getContactDetails() {
        return (String)getAttributeInternal(CONTACTDETAILS);
    }

    /**Sets <code>value</code> as the attribute value for ContactDetails
     */
    public void setContactDetails(String value) {
        setAttributeInternal(CONTACTDETAILS, value);
    }

    /**Gets the attribute value for Attribute1, using the alias name Attribute1
     */
    public String getAttribute1() {
        return (String)getAttributeInternal(ATTRIBUTE1);
    }

    /**Sets <code>value</code> as the attribute value for Attribute1
     */
    public void setAttribute1(String value) {
        setAttributeInternal(ATTRIBUTE1, value);
    }

    /**Gets the attribute value for Attribute2, using the alias name Attribute2
     */
    public String getAttribute2() {
        return (String)getAttributeInternal(ATTRIBUTE2);
    }

    /**Sets <code>value</code> as the attribute value for Attribute2
     */
    public void setAttribute2(String value) {
        setAttributeInternal(ATTRIBUTE2, value);
    }

    /**Gets the attribute value for Attribute3, using the alias name Attribute3
     */
    public String getAttribute3() {
        return (String)getAttributeInternal(ATTRIBUTE3);
    }

    /**Sets <code>value</code> as the attribute value for Attribute3
     */
    public void setAttribute3(String value) {
        setAttributeInternal(ATTRIBUTE3, value);
    }

    /**Gets the attribute value for Attribute4, using the alias name Attribute4
     */
    public String getAttribute4() {
        return (String)getAttributeInternal(ATTRIBUTE4);
    }

    /**Sets <code>value</code> as the attribute value for Attribute4
     */
    public void setAttribute4(String value) {
        setAttributeInternal(ATTRIBUTE4, value);
    }

    /**Gets the attribute value for Attribute5, using the alias name Attribute5
     */
    public String getAttribute5() {
        return (String)getAttributeInternal(ATTRIBUTE5);
    }

    /**Sets <code>value</code> as the attribute value for Attribute5
     */
    public void setAttribute5(String value) {
        setAttributeInternal(ATTRIBUTE5, value);
    }

    /**Gets the attribute value for LastUpdateDate, using the alias name LastUpdateDate
     */
    public Date getLastUpdateDate() {
        return (Date)getAttributeInternal(LASTUPDATEDATE);
    }

    /**Sets <code>value</code> as the attribute value for LastUpdateDate
     */
    public void setLastUpdateDate(Date value) {
        setAttributeInternal(LASTUPDATEDATE, value);
    }

    /**Gets the attribute value for LastUpdatedBy, using the alias name LastUpdatedBy
     */
    public Number getLastUpdatedBy() {
        return (Number)getAttributeInternal(LASTUPDATEDBY);
    }

    /**Sets <code>value</code> as the attribute value for LastUpdatedBy
     */
    public void setLastUpdatedBy(Number value) {
        setAttributeInternal(LASTUPDATEDBY, value);
    }

    /**Gets the attribute value for LastUpdateLogin, using the alias name LastUpdateLogin
     */
    public Number getLastUpdateLogin() {
        return (Number)getAttributeInternal(LASTUPDATELOGIN);
    }

    /**Sets <code>value</code> as the attribute value for LastUpdateLogin
     */
    public void setLastUpdateLogin(Number value) {
        setAttributeInternal(LASTUPDATELOGIN, value);
    }

    /**Gets the attribute value for CreatedBy, using the alias name CreatedBy
     */
    public Number getCreatedBy() {
        return (Number)getAttributeInternal(CREATEDBY);
    }

    /**Sets <code>value</code> as the attribute value for CreatedBy
     */
    public void setCreatedBy(Number value) {
        setAttributeInternal(CREATEDBY, value);
    }

    /**Gets the attribute value for CreationDate, using the alias name CreationDate
     */
    public Date getCreationDate() {
        return (Date)getAttributeInternal(CREATIONDATE);
    }

    /**Sets <code>value</code> as the attribute value for CreationDate
     */
    public void setCreationDate(Date value) {
        setAttributeInternal(CREATIONDATE, value);
    }

    /**Gets the attribute value for ItemKey, using the alias name ItemKey
     */
    public String getItemKey() {
        return (String)getAttributeInternal(ITEMKEY);
    }

    /**Sets <code>value</code> as the attribute value for ItemKey
     */
    public void setItemKey(String value) {
        setAttributeInternal(ITEMKEY, value);
    }

    /**Gets the attribute value for RowID, using the alias name RowID
     */
    public RowID getRowID() {
        return (RowID)getAttributeInternal(ROWID);
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
        case BENEFICIARYORGANIZATION:
            return getBeneficiaryOrganization();
        case CONTACTDETAILS:
            return getContactDetails();
        case ATTRIBUTE1:
            return getAttribute1();
        case ATTRIBUTE2:
            return getAttribute2();
        case ATTRIBUTE3:
            return getAttribute3();
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
        case ITEMKEY:
            return getItemKey();
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
        case BENEFICIARYORGANIZATION:
            setBeneficiaryOrganization((String)value);
            return;
        case CONTACTDETAILS:
            setContactDetails((String)value);
            return;
        case ATTRIBUTE1:
            setAttribute1((String)value);
            return;
        case ATTRIBUTE2:
            setAttribute2((String)value);
            return;
        case ATTRIBUTE3:
            setAttribute3((String)value);
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
        case ITEMKEY:
            setItemKey((String)value);
            return;
        default:
            super.setAttrInvokeAccessor(index, value, attrDef);
            return;
        }
    }
}
