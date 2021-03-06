package xxup.oracle.apps.per.publicservice.institutional.server;

import oracle.apps.fnd.framework.server.OAViewRowImpl;

import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class XxupPerPSInstOtherMembersVORowImpl extends OAViewRowImpl {
    public static final int FULLNAME = 0;
    public static final int POSITION = 1;
    public static final int ORGANIZATION = 2;
    public static final int PROJECTROLE = 3;
    public static final int MEMBERTYPE = 4;
    public static final int SEQUENCENO = 5;
    public static final int ATTRIBUTE1 = 6;
    public static final int ATTRIBUTE2 = 7;
    public static final int ATTRIBUTE3 = 8;
    public static final int ATTRIBUTE4 = 9;
    public static final int ATTRIBUTE5 = 10;

    /**This is the default constructor (do not remove)
     */
    public XxupPerPSInstOtherMembersVORowImpl() {
    }

    /**Gets the attribute value for the calculated attribute FullName
     */
    public String getFullName() {
        return (String) getAttributeInternal(FULLNAME);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute FullName
     */
    public void setFullName(String value) {
        setAttributeInternal(FULLNAME, value);
    }

    /**Gets the attribute value for the calculated attribute Position
     */
    public String getPosition() {
        return (String) getAttributeInternal(POSITION);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute Position
     */
    public void setPosition(String value) {
        setAttributeInternal(POSITION, value);
    }

    /**Gets the attribute value for the calculated attribute Organization
     */
    public String getOrganization() {
        return (String) getAttributeInternal(ORGANIZATION);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute Organization
     */
    public void setOrganization(String value) {
        setAttributeInternal(ORGANIZATION, value);
    }

    /**Gets the attribute value for the calculated attribute ProjectRole
     */
    public String getProjectRole() {
        return (String) getAttributeInternal(PROJECTROLE);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute ProjectRole
     */
    public void setProjectRole(String value) {
        setAttributeInternal(PROJECTROLE, value);
    }

    /**Gets the attribute value for the calculated attribute MemberType
     */
    public String getMemberType() {
        return (String) getAttributeInternal(MEMBERTYPE);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute MemberType
     */
    public void setMemberType(String value) {
        setAttributeInternal(MEMBERTYPE, value);
    }

    /**Gets the attribute value for the calculated attribute SequenceNo
     */
    public Number getSequenceNo() {
        return (Number) getAttributeInternal(SEQUENCENO);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute SequenceNo
     */
    public void setSequenceNo(Number value) {
        setAttributeInternal(SEQUENCENO, value);
    }

    /**getAttrInvokeAccessor: generated method. Do not modify.
     */
    protected Object getAttrInvokeAccessor(int index, 
                                           AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case FULLNAME:
            return getFullName();
        case POSITION:
            return getPosition();
        case ORGANIZATION:
            return getOrganization();
        case PROJECTROLE:
            return getProjectRole();
        case MEMBERTYPE:
            return getMemberType();
        case SEQUENCENO:
            return getSequenceNo();
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
        default:
            return super.getAttrInvokeAccessor(index, attrDef);
        }
    }

    /**setAttrInvokeAccessor: generated method. Do not modify.
     */
    protected void setAttrInvokeAccessor(int index, Object value, 
                                         AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case FULLNAME:
            setFullName((String)value);
            return;
        case POSITION:
            setPosition((String)value);
            return;
        case ORGANIZATION:
            setOrganization((String)value);
            return;
        case PROJECTROLE:
            setProjectRole((String)value);
            return;
        case MEMBERTYPE:
            setMemberType((String)value);
            return;
        case SEQUENCENO:
            setSequenceNo((Number)value);
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
        default:
            super.setAttrInvokeAccessor(index, value, attrDef);
            return;
        }
    }

    /**Gets the attribute value for the calculated attribute Attribute1
     */
    public String getAttribute1() {
        return (String) getAttributeInternal(ATTRIBUTE1);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute Attribute1
     */
    public void setAttribute1(String value) {
        setAttributeInternal(ATTRIBUTE1, value);
    }

    /**Gets the attribute value for the calculated attribute Attribute2
     */
    public String getAttribute2() {
        return (String) getAttributeInternal(ATTRIBUTE2);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute Attribute2
     */
    public void setAttribute2(String value) {
        setAttributeInternal(ATTRIBUTE2, value);
    }

    /**Gets the attribute value for the calculated attribute Attribute3
     */
    public String getAttribute3() {
        return (String) getAttributeInternal(ATTRIBUTE3);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute Attribute3
     */
    public void setAttribute3(String value) {
        setAttributeInternal(ATTRIBUTE3, value);
    }

    /**Gets the attribute value for the calculated attribute Attribute4
     */
    public String getAttribute4() {
        return (String) getAttributeInternal(ATTRIBUTE4);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute Attribute4
     */
    public void setAttribute4(String value) {
        setAttributeInternal(ATTRIBUTE4, value);
    }

    /**Gets the attribute value for the calculated attribute Attribute5
     */
    public String getAttribute5() {
        return (String) getAttributeInternal(ATTRIBUTE5);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute Attribute5
     */
    public void setAttribute5(String value) {
        setAttributeInternal(ATTRIBUTE5, value);
    }
}
