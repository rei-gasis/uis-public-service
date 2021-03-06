package xxup.oracle.apps.per.publicservice.server;

import oracle.apps.fnd.framework.server.OAViewRowImpl;

import oracle.jbo.RowIterator;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;

import xxup.oracle.apps.per.publicservice.schema.server.XxupPerPublicServiceHeaderEOImpl;


// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class XxupPerPublicServiceHeaderEOVORowImpl extends OAViewRowImpl {
    public static final int SEQUENCENO = 0;
    public static final int REQUESTSTATUS = 21;
    public static final int TYPEOFACTIVITYDISPLAY = 30;
    public static final int POSITIONID = 1;
    public static final int PROJECTNAME = 2;
    public static final int PRIMARYROLE = 3;
    public static final int PROJECTTYPE = 4;
    public static final int REQUESTDATE = 5;
    public static final int RESPONDEDDATE = 6;
    public static final int STARTDATE = 7;
    public static final int ENDDATE = 8;
    public static final int DURATIONHOURS = 9;
    public static final int SOURCEOFFUND = 10;
    public static final int COSTOFPARTICIPATION = 11;
    public static final int PARTNERORGORINST = 12;
    public static final int BENEFICIARYCATEGORY = 13;
    public static final int UNITOFBENEFICIARY = 14;
    public static final int NOOFBENEFICIARY = 15;
    public static final int POSTACTEVALRATING = 16;
    public static final int REMARKS = 17;
    public static final int ATTRIBUTE1 = 18;
    public static final int ATTRIBUTE2 = 19;
    public static final int ATTRIBUTE3 = 20;
    public static final int ATTRIBUTE4 = 21;
    public static final int ATTRIBUTE5 = 22;
    public static final int LASTUPDATEDATE = 23;
    public static final int LASTUPDATEDBY = 24;
    public static final int LASTUPDATELOGIN = 25;
    public static final int CREATEDBY = 26;
    public static final int CREATIONDATE = 27;
    public static final int POSITIONNAME = 28;
    public static final int SHOWHIDE = 29;
    public static final int RENDERADDRESS = 30;
    public static final int RENDERORGRN = 31;
    public static final int XXUPPERPUBLICSERVICECATEOVO = 32;
    public static final int XXUPPERPUBLICSERVICESUBJEOVO = 33;

    /**This is the default constructor (do not remove)
     */
    public XxupPerPublicServiceHeaderEOVORowImpl() {
    }

    /**Gets XxupPerPublicServiceHeaderEO entity object.
     */
    public XxupPerPublicServiceHeaderEOImpl getXxupPerPublicServiceHeaderEO() {
        return (XxupPerPublicServiceHeaderEOImpl)getEntity(0);
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

    /**Gets the attribute value for POSITION_ID using the alias name PositionId
     */
    public Number getPositionId() {
        return (Number) getAttributeInternal(POSITIONID);
    }

    /**Sets <code>value</code> as attribute value for POSITION_ID using the alias name PositionId
     */
    public void setPositionId(Number value) {
        setAttributeInternal(POSITIONID, value);
    }

    /**Gets the attribute value for PROJECT_NAME using the alias name ProjectName
     */
    public String getProjectName() {
        return (String) getAttributeInternal(PROJECTNAME);
    }

    /**Sets <code>value</code> as attribute value for PROJECT_NAME using the alias name ProjectName
     */
    public void setProjectName(String value) {
        setAttributeInternal(PROJECTNAME, value);
    }

    /**Gets the attribute value for PRIMARY_ROLE using the alias name PrimaryRole
     */
    public String getPrimaryRole() {
        return (String) getAttributeInternal(PRIMARYROLE);
    }

    /**Sets <code>value</code> as attribute value for PRIMARY_ROLE using the alias name PrimaryRole
     */
    public void setPrimaryRole(String value) {
        setAttributeInternal(PRIMARYROLE, value);
    }

    /**Gets the attribute value for PROJECT_TYPE using the alias name ProjectType
     */
    public String getProjectType() {
        return (String) getAttributeInternal(PROJECTTYPE);
    }

    /**Sets <code>value</code> as attribute value for PROJECT_TYPE using the alias name ProjectType
     */
    public void setProjectType(String value) {
        setAttributeInternal(PROJECTTYPE, value);
    }

    /**Gets the attribute value for REQUEST_DATE using the alias name RequestDate
     */
    public Date getRequestDate() {
        return (Date) getAttributeInternal(REQUESTDATE);
    }

    /**Sets <code>value</code> as attribute value for REQUEST_DATE using the alias name RequestDate
     */
    public void setRequestDate(Date value) {
        setAttributeInternal(REQUESTDATE, value);
    }

    /**Gets the attribute value for RESPONDED_DATE using the alias name RespondedDate
     */
    public Date getRespondedDate() {
        return (Date) getAttributeInternal(RESPONDEDDATE);
    }

    /**Sets <code>value</code> as attribute value for RESPONDED_DATE using the alias name RespondedDate
     */
    public void setRespondedDate(Date value) {
        setAttributeInternal(RESPONDEDDATE, value);
    }

    /**Gets the attribute value for START_DATE using the alias name StartDate
     */
    public Date getStartDate() {
        return (Date) getAttributeInternal(STARTDATE);
    }

    /**Sets <code>value</code> as attribute value for START_DATE using the alias name StartDate
     */
    public void setStartDate(Date value) {
        setAttributeInternal(STARTDATE, value);
    }

    /**Gets the attribute value for END_DATE using the alias name EndDate
     */
    public Date getEndDate() {
        return (Date) getAttributeInternal(ENDDATE);
    }

    /**Sets <code>value</code> as attribute value for END_DATE using the alias name EndDate
     */
    public void setEndDate(Date value) {
        setAttributeInternal(ENDDATE, value);
    }

    /**Gets the attribute value for DURATION_HOURS using the alias name DurationHours
     */
    public Number getDurationHours() {
        return (Number) getAttributeInternal(DURATIONHOURS);
    }

    /**Sets <code>value</code> as attribute value for DURATION_HOURS using the alias name DurationHours
     */
    public void setDurationHours(Number value) {
        setAttributeInternal(DURATIONHOURS, value);
    }


    /**Gets the attribute value for SOURCE_OF_FUND using the alias name SourceOfFund
     */
    public String getSourceOfFund() {
        return (String) getAttributeInternal(SOURCEOFFUND);
    }

    /**Sets <code>value</code> as attribute value for SOURCE_OF_FUND using the alias name SourceOfFund
     */
    public void setSourceOfFund(String value) {
        setAttributeInternal(SOURCEOFFUND, value);
    }

    /**Gets the attribute value for COST_OF_PARTICIPATION using the alias name CostOfParticipation
     */
    public String getCostOfParticipation() {
        return (String) getAttributeInternal(COSTOFPARTICIPATION);
    }

    /**Sets <code>value</code> as attribute value for COST_OF_PARTICIPATION using the alias name CostOfParticipation
     */
    public void setCostOfParticipation(String value) {
        setAttributeInternal(COSTOFPARTICIPATION, value);
    }

    /**Gets the attribute value for PARTNER_ORG_OR_INST using the alias name PartnerOrgOrInst
     */
    public String getPartnerOrgOrInst() {
        return (String) getAttributeInternal(PARTNERORGORINST);
    }

    /**Sets <code>value</code> as attribute value for PARTNER_ORG_OR_INST using the alias name PartnerOrgOrInst
     */
    public void setPartnerOrgOrInst(String value) {
        setAttributeInternal(PARTNERORGORINST, value);
    }

    /**Gets the attribute value for BENEFICIARY_CATEGORY using the alias name BeneficiaryCategory
     */
    public String getBeneficiaryCategory() {
        return (String) getAttributeInternal(BENEFICIARYCATEGORY);
    }

    /**Sets <code>value</code> as attribute value for BENEFICIARY_CATEGORY using the alias name BeneficiaryCategory
     */
    public void setBeneficiaryCategory(String value) {
        setAttributeInternal(BENEFICIARYCATEGORY, value);
    }


    /**Gets the attribute value for UNIT_OF_BENEFICIARY using the alias name UnitOfBeneficiary
     */
    public String getUnitOfBeneficiary() {
        return (String) getAttributeInternal(UNITOFBENEFICIARY);
    }

    /**Sets <code>value</code> as attribute value for UNIT_OF_BENEFICIARY using the alias name UnitOfBeneficiary
     */
    public void setUnitOfBeneficiary(String value) {
        setAttributeInternal(UNITOFBENEFICIARY, value);
    }

    /**Gets the attribute value for NO_OF_BENEFICIARY using the alias name NoOfBeneficiary
     */
    public Number getNoOfBeneficiary() {
        return (Number) getAttributeInternal(NOOFBENEFICIARY);
    }

    /**Sets <code>value</code> as attribute value for NO_OF_BENEFICIARY using the alias name NoOfBeneficiary
     */
    public void setNoOfBeneficiary(Number value) {
        setAttributeInternal(NOOFBENEFICIARY, value);
    }


    /**Gets the attribute value for POST_ACT_EVAL_RATING using the alias name PostActEvalRating
     */
    public String getPostActEvalRating() {
        return (String) getAttributeInternal(POSTACTEVALRATING);
    }

    /**Sets <code>value</code> as attribute value for POST_ACT_EVAL_RATING using the alias name PostActEvalRating
     */
    public void setPostActEvalRating(String value) {
        setAttributeInternal(POSTACTEVALRATING, value);
    }

    /**Gets the attribute value for REMARKS using the alias name Remarks
     */
    public String getRemarks() {
        return (String) getAttributeInternal(REMARKS);
    }

    /**Sets <code>value</code> as attribute value for REMARKS using the alias name Remarks
     */
    public void setRemarks(String value) {
        setAttributeInternal(REMARKS, value);
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

    /**Gets the attribute value for ATTRIBUTE3 using the alias name Attribute3
     */
    public String getAttribute3() {
        return (String) getAttributeInternal(ATTRIBUTE3);
    }

    /**Sets <code>value</code> as attribute value for ATTRIBUTE3 using the alias name Attribute3
     */
    public void setAttribute3(String value) {
        setAttributeInternal(ATTRIBUTE3, value);
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

    /**Gets the attribute value for the calculated attribute PositionName
     */
    public String getPositionName() {
        return (String) getAttributeInternal(POSITIONNAME);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute PositionName
     */
    public void setPositionName(String value) {
        setAttributeInternal(POSITIONNAME, value);
    }


    /**Gets the attribute value for the calculated attribute ShowHide
     */
    public String getShowHide() {
        return (String) getAttributeInternal(SHOWHIDE);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute ShowHide
     */
    public void setShowHide(String value) {
        //setAttributeInternal(SHOWHIDE, value);
        populateAttribute(SHOWHIDE, value);
    }

    /**Gets the attribute value for the calculated attribute RenderAddress
     */
    public Boolean getRenderAddress() {
        return (Boolean) getAttributeInternal(RENDERADDRESS);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute RenderAddress
     */
    public void setRenderAddress(Boolean value) {
        setAttributeInternal(RENDERADDRESS, value);
    }

    /**Gets the associated <code>RowIterator</code> using master-detail link XxupPerPublicServiceCatEOVO
     */
    public RowIterator getXxupPerPublicServiceCatEOVO() {
        return (RowIterator)getAttributeInternal(XXUPPERPUBLICSERVICECATEOVO);
    }

    /**Gets the associated <code>RowIterator</code> using master-detail link XxupPerPublicServiceSubjEOVO
     */
    public RowIterator getXxupPerPublicServiceSubjEOVO() {
        return (RowIterator)getAttributeInternal(XXUPPERPUBLICSERVICESUBJEOVO);
    }

    /**getAttrInvokeAccessor: generated method. Do not modify.
     */
    protected Object getAttrInvokeAccessor(int index, 
                                           AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case SEQUENCENO:
            return getSequenceNo();
        case POSITIONID:
            return getPositionId();
        case PROJECTNAME:
            return getProjectName();
        case PRIMARYROLE:
            return getPrimaryRole();
        case PROJECTTYPE:
            return getProjectType();
        case REQUESTDATE:
            return getRequestDate();
        case RESPONDEDDATE:
            return getRespondedDate();
        case STARTDATE:
            return getStartDate();
        case ENDDATE:
            return getEndDate();
        case DURATIONHOURS:
            return getDurationHours();
        case SOURCEOFFUND:
            return getSourceOfFund();
        case COSTOFPARTICIPATION:
            return getCostOfParticipation();
        case PARTNERORGORINST:
            return getPartnerOrgOrInst();
        case BENEFICIARYCATEGORY:
            return getBeneficiaryCategory();
        case UNITOFBENEFICIARY:
            return getUnitOfBeneficiary();
        case NOOFBENEFICIARY:
            return getNoOfBeneficiary();
        case POSTACTEVALRATING:
            return getPostActEvalRating();
        case REMARKS:
            return getRemarks();
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
        case POSITIONNAME:
            return getPositionName();
        case SHOWHIDE:
            return getShowHide();
        case RENDERADDRESS:
            return getRenderAddress();
        case RENDERORGRN:
            return getRenderOrgRN();
        case XXUPPERPUBLICSERVICECATEOVO:
            return getXxupPerPublicServiceCatEOVO();
        case XXUPPERPUBLICSERVICESUBJEOVO:
            return getXxupPerPublicServiceSubjEOVO();
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
        case POSITIONID:
            setPositionId((Number)value);
            return;
        case PROJECTNAME:
            setProjectName((String)value);
            return;
        case PRIMARYROLE:
            setPrimaryRole((String)value);
            return;
        case PROJECTTYPE:
            setProjectType((String)value);
            return;
        case REQUESTDATE:
            setRequestDate((Date)value);
            return;
        case RESPONDEDDATE:
            setRespondedDate((Date)value);
            return;
        case STARTDATE:
            setStartDate((Date)value);
            return;
        case ENDDATE:
            setEndDate((Date)value);
            return;
        case DURATIONHOURS:
            setDurationHours((Number)value);
            return;
        case SOURCEOFFUND:
            setSourceOfFund((String)value);
            return;
        case COSTOFPARTICIPATION:
            setCostOfParticipation((String)value);
            return;
        case PARTNERORGORINST:
            setPartnerOrgOrInst((String)value);
            return;
        case BENEFICIARYCATEGORY:
            setBeneficiaryCategory((String)value);
            return;
        case UNITOFBENEFICIARY:
            setUnitOfBeneficiary((String)value);
            return;
        case NOOFBENEFICIARY:
            setNoOfBeneficiary((Number)value);
            return;
        case POSTACTEVALRATING:
            setPostActEvalRating((String)value);
            return;
        case REMARKS:
            setRemarks((String)value);
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
        case POSITIONNAME:
            setPositionName((String)value);
            return;
        case SHOWHIDE:
            setShowHide((String)value);
            return;
        case RENDERADDRESS:
            setRenderAddress((Boolean)value);
            return;
        case RENDERORGRN:
            setRenderOrgRN((Boolean)value);
            return;
        default:
            super.setAttrInvokeAccessor(index, value, attrDef);
            return;
        }
    }

    /**Gets the attribute value for the calculated attribute TypeOfActivityDisplay
     */
    public String getTypeOfActivityDisplay() {
        return (String) getAttributeInternal(TYPEOFACTIVITYDISPLAY);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute TypeOfActivityDisplay
     */
    public void setTypeOfActivityDisplay(String value) {
        setAttributeInternal(TYPEOFACTIVITYDISPLAY, value);
    }


    /**Gets the attribute value for the calculated attribute RenderOrgRN
     */
    public Boolean getRenderOrgRN() {
        return (Boolean) getAttributeInternal(RENDERORGRN);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute RenderOrgRN
     */
    public void setRenderOrgRN(Boolean value) {
        setAttributeInternal(RENDERORGRN, value);
    }
}
