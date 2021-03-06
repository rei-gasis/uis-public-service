package xxup.oracle.apps.per.publicservice.lov.server;

import oracle.apps.fnd.framework.server.OAApplicationModuleImpl;


// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class PublicServiceLovAMImpl extends OAApplicationModuleImpl {
    /**This is the default constructor (do not remove)
     */
    public PublicServiceLovAMImpl() {
    }

    /**Container's getter for PerPositionsVO1
     */
    public PerPositionsVOImpl getPerPositionsVO1() {
        return (PerPositionsVOImpl)findViewObject("PerPositionsVO1");
    }

    /**Sample main for debugging Business Components code using the tester.
     */
    public static void main(String[] args) {
        launchTester("xxup.oracle.apps.per.publicservice.lov.server", /* package name */
      "PublicServiceLovAMLocal" /* Configuration Name */);
    }


    /**Container's getter for PerPSBeneficiaryCategoryVO1
     */
    public PerPSBeneficiaryCategoryVOImpl getPerPSBeneficiaryCategoryVO1() {
        return (PerPSBeneficiaryCategoryVOImpl)findViewObject("PerPSBeneficiaryCategoryVO1");
    }

    /**Container's getter for PerPSSubjectAreaInterestVO1
     */
    public PerPSSubjectAreaInterestVOImpl getPerPSSubjectAreaInterestVO1() {
        return (PerPSSubjectAreaInterestVOImpl)findViewObject("PerPSSubjectAreaInterestVO1");
    }

    /**Container's getter for PerPSProjectTypeVO1
     */
    public PerPSProjectTypeVOImpl getPerPSProjectTypeVO1() {
        return (PerPSProjectTypeVOImpl)findViewObject("PerPSProjectTypeVO1");
    }

    /**Container's getter for PerPSObjCatVO1
     */
    public PerPSObjCatVOImpl getPerPSObjCatVO1() {
        return (PerPSObjCatVOImpl)findViewObject("PerPSObjCatVO1");
    }

    /**Container's getter for PerPSBeneficiaryTypeVO1
     */
    public PerPSBeneficiaryTypeVOImpl getPerPSBeneficiaryTypeVO1() {
        return (PerPSBeneficiaryTypeVOImpl)findViewObject("PerPSBeneficiaryTypeVO1");
    }

    /**Container's getter for PerPSBeneficiaryUnitVO1
     */
    public PerPSBeneficiaryUnitVOImpl getPerPSBeneficiaryUnitVO1() {
        return (PerPSBeneficiaryUnitVOImpl)findViewObject("PerPSBeneficiaryUnitVO1");
    }

    /**Container's getter for PerPSCountryVO1
     */
    public PerPSCountryVOImpl getPerPSCountryVO1() {
        return (PerPSCountryVOImpl)findViewObject("PerPSCountryVO1");
    }

    /**Container's getter for PerPSAddressVO1
     */
    public PerPSAddressVOImpl getPerPSAddressVO1() {
        return (PerPSAddressVOImpl)findViewObject("PerPSAddressVO1");
    }
    
    
    public void initPosVO(String pPersonId){
        PerPositionsVOImpl posVO = getPerPositionsVO1();
        
        posVO.initPosVO(pPersonId);
        
        
    }


    public void showObjCatList(){
        PerPSObjCatVOImpl objCatVO =
          getPerPSObjCatVO1();

        objCatVO.showList();
    }

    /**Container's getter for PerPSActivityTypeVO1
     */
    public PerPSActivityTypeVOImpl getPerPSActivityTypeVO1() {
        return (PerPSActivityTypeVOImpl)findViewObject("PerPSActivityTypeVO1");
    }
}
