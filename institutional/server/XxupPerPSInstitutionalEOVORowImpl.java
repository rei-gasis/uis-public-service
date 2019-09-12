package xxup.oracle.apps.per.publicservice.institutional.server;

import oracle.apps.fnd.framework.server.OAViewRowImpl;

import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;

import xxup.oracle.apps.per.publicservice.institutional.schema.server.XxupPerPsInstitutionalEOImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class XxupPerPSInstitutionalEOVORowImpl extends OAViewRowImpl {
    public static final int SEQUENCENO = 0;
    public static final int CONSTITUENTUNIVERSITY = 1;
    public static final int PROJECTNAME = 2;
    public static final int IMPLEMENTATIONSTARTDATE = 3;
    public static final int IMPLEMENTATIONENDDATE = 4;
    public static final int DURATIONHOURS = 5;
    public static final int IMPLEMENTATIONFREQUENCY = 6;
    public static final int FUNDINGAGENCY = 7;
    public static final int COSTOFPARTICIPATION = 8;
    public static final int UNITOFBENEFICIARY = 9;
    public static final int NOOFBENEFICIARY = 10;
    public static final int POSTACTEVALRATING = 11;
    public static final int REMARKS = 12;
    public static final int ATTRIBUTE1 = 13;
    public static final int ATTRIBUTE2 = 14;
    public static final int ATTRIBUTE3 = 15;
    public static final int ATTRIBUTE4 = 16;
    public static final int ATTRIBUTE5 = 17;
    public static final int LASTUPDATEDATE = 18;
    public static final int LASTUPDATEDBY = 19;
    public static final int LASTUPDATELOGIN = 20;
    public static final int CREATEDBY = 21;
    public static final int CREATIONDATE = 22;
    public static final int STATUS = 23;
    public static final int MALEBENEFCOUNT = 24;
    public static final int FEMALEBENEFCOUNT = 25;
    public static final int PROJECTLEADER = 26;
    public static final int PROJECTLEADERDISPLAY = 27;

    /**This is the default constructor (do not remove)
     */
    public XxupPerPSInstitutionalEOVORowImpl() {
    }

    /**Gets XxupPerPsInstitutionalEO entity object.
     */
    public XxupPerPsInstitutionalEOImpl getXxupPerPsInstitutionalEO() {
        return (XxupPerPsInstitutionalEOImpl)getEntity(0);
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

    /**Gets the attribute value for CONSTITUENT_UNIVERSITY using the alias name ConstituentUniversity
     */
    public String getConstituentUniversity() {
        return (String) getAttributeInternal(CONSTITUENTUNIVERSITY);
    }

    /**Sets <code>value</code> as attribute value for CONSTITUENT_UNIVERSITY using the alias name ConstituentUniversity
     */
    public void setConstituentUniversity(String value) {
        setAttributeInternal(CONSTITUENTUNIVERSITY, value);
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

    /**Gets the attribute value for IMPLEMENTATION_START_DATE using the alias name ImplementationStartDate
     */
    public Date getImplementationStartDate() {
        return (Date) getAttributeInternal(IMPLEMENTATIONSTARTDATE);
    }

    /**Sets <code>value</code> as attribute value for IMPLEMENTATION_START_DATE using the alias name ImplementationStartDate
     */
    public void setImplementationStartDate(Date value) {
        setAttributeInternal(IMPLEMENTATIONSTARTDATE, value);
    }

    /**Gets the attribute value for IMPLEMENTATION_END_DATE using the alias name ImplementationEndDate
     */
    public Date getImplementationEndDate() {
        return (Date) getAttributeInternal(IMPLEMENTATIONENDDATE);
    }

    /**Sets <code>value</code> as attribute value for IMPLEMENTATION_END_DATE using the alias name ImplementationEndDate
     */
    public void setImplementationEndDate(Date value) {
        setAttributeInternal(IMPLEMENTATIONENDDATE, value);
    }

    /**Gets the attribute value for DURATION_HOURS using the alias name DurationHours
     */
    public String getDurationHours() {
        return (String) getAttributeInternal(DURATIONHOURS);
    }

    /**Sets <code>value</code> as attribute value for DURATION_HOURS using the alias name DurationHours
     */
    public void setDurationHours(String value) {
        setAttributeInternal(DURATIONHOURS, value);
    }

    /**Gets the attribute value for IMPLEMENTATION_FREQUENCY using the alias name ImplementationFrequency
     */
    public String getImplementationFrequency() {
        return (String) getAttributeInternal(IMPLEMENTATIONFREQUENCY);
    }

    /**Sets <code>value</code> as attribute value for IMPLEMENTATION_FREQUENCY using the alias name ImplementationFrequency
     */
    public void setImplementationFrequency(String value) {
        setAttributeInternal(IMPLEMENTATIONFREQUENCY, value);
    }


    /**Gets the attribute value for FUNDING_AGENCY using the alias name FundingAgency
     */
    public String getFundingAgency() {
        return (String) getAttributeInternal(FUNDINGAGENCY);
    }

    /**Sets <code>value</code> as attribute value for FUNDING_AGENCY using the alias name FundingAgency
     */
    public void setFundingAgency(String value) {
        setAttributeInternal(FUNDINGAGENCY, value);
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

    /**Gets the attribute value for STATUS using the alias name Status
     */
    public String getStatus() {
        return (String) getAttributeInternal(STATUS);
    }

    /**Sets <code>value</code> as attribute value for STATUS using the alias name Status
     */
    public void setStatus(String value) {
        setAttributeInternal(STATUS, value);
    }

    /**Gets the attribute value for MALE_BENEF_COUNT using the alias name MaleBenefCount
     */
    public Number getMaleBenefCount() {
        return (Number) getAttributeInternal(MALEBENEFCOUNT);
    }

    /**Sets <code>value</code> as attribute value for MALE_BENEF_COUNT using the alias name MaleBenefCount
     */
    public void setMaleBenefCount(Number value) {
        setAttributeInternal(MALEBENEFCOUNT, value);
    }

    /**Gets the attribute value for FEMALE_BENEF_COUNT using the alias name FemaleBenefCount
     */
    public Number getFemaleBenefCount() {
        return (Number) getAttributeInternal(FEMALEBENEFCOUNT);
    }

    /**Sets <code>value</code> as attribute value for FEMALE_BENEF_COUNT using the alias name FemaleBenefCount
     */
    public void setFemaleBenefCount(Number value) {
        setAttributeInternal(FEMALEBENEFCOUNT, value);
    }

    /**Gets the attribute value for PROJECT_LEADER using the alias name ProjectLeader
     */
    public Number getProjectLeader() {
        return (Number) getAttributeInternal(PROJECTLEADER);
    }

    /**Sets <code>value</code> as attribute value for PROJECT_LEADER using the alias name ProjectLeader
     */
    public void setProjectLeader(Number value) {
        setAttributeInternal(PROJECTLEADER, value);
    }

    /**getAttrInvokeAccessor: generated method. Do not modify.
     */
    protected Object getAttrInvokeAccessor(int index, 
                                           AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case SEQUENCENO:
            return getSequenceNo();
        case CONSTITUENTUNIVERSITY:
            return getConstituentUniversity();
        case PROJECTNAME:
            return getProjectName();
        case IMPLEMENTATIONSTARTDATE:
            return getImplementationStartDate();
        case IMPLEMENTATIONENDDATE:
            return getImplementationEndDate();
        case DURATIONHOURS:
            return getDurationHours();
        case IMPLEMENTATIONFREQUENCY:
            return getImplementationFrequency();
        case FUNDINGAGENCY:
            return getFundingAgency();
        case COSTOFPARTICIPATION:
            return getCostOfParticipation();
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
        case STATUS:
            return getStatus();
        case MALEBENEFCOUNT:
            return getMaleBenefCount();
        case FEMALEBENEFCOUNT:
            return getFemaleBenefCount();
        case PROJECTLEADER:
            return getProjectLeader();
        case PROJECTLEADERDISPLAY:
            return getProjectLeaderDisplay();
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
        case CONSTITUENTUNIVERSITY:
            setConstituentUniversity((String)value);
            return;
        case PROJECTNAME:
            setProjectName((String)value);
            return;
        case IMPLEMENTATIONSTARTDATE:
            setImplementationStartDate((Date)value);
            return;
        case IMPLEMENTATIONENDDATE:
            setImplementationEndDate((Date)value);
            return;
        case DURATIONHOURS:
            setDurationHours((String)value);
            return;
        case IMPLEMENTATIONFREQUENCY:
            setImplementationFrequency((String)value);
            return;
        case FUNDINGAGENCY:
            setFundingAgency((String)value);
            return;
        case COSTOFPARTICIPATION:
            setCostOfParticipation((String)value);
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
        case STATUS:
            setStatus((String)value);
            return;
        case MALEBENEFCOUNT:
            setMaleBenefCount((Number)value);
            return;
        case FEMALEBENEFCOUNT:
            setFemaleBenefCount((Number)value);
            return;
        case PROJECTLEADER:
            setProjectLeader((Number)value);
            return;
        case PROJECTLEADERDISPLAY:
            setProjectLeaderDisplay((String)value);
            return;
        default:
            super.setAttrInvokeAccessor(index, value, attrDef);
            return;
        }
    }

    /**Gets the attribute value for the calculated attribute ProjectLeaderDisplay
     */
    public String getProjectLeaderDisplay() {
        return (String) getAttributeInternal(PROJECTLEADERDISPLAY);
    }

    /**Sets <code>value</code> as the attribute value for the calculated attribute ProjectLeaderDisplay
     */
    public void setProjectLeaderDisplay(String value) {
        setAttributeInternal(PROJECTLEADERDISPLAY, value);
    }
}
