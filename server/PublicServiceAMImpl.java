package xxup.oracle.apps.per.publicservice.server;

import java.sql.CallableStatement;
import java.sql.Connection;

import java.util.Arrays;

import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.server.OAApplicationModuleImpl;
import oracle.apps.fnd.framework.server.OADBTransaction;
import oracle.apps.fnd.framework.server.OAViewRowImpl;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.Transaction;
import oracle.jbo.server.ViewLinkImpl;

import xxup.oracle.apps.per.publicservice.lov.server.PerPSActivityTypeVOImpl;
import xxup.oracle.apps.per.publicservice.lov.server.PerPSAddressVOImpl;
import xxup.oracle.apps.per.publicservice.lov.server.PerPSBeneficiaryCategoryVOImpl;
import xxup.oracle.apps.per.publicservice.lov.server.PerPSBeneficiaryTypeVOImpl;
import xxup.oracle.apps.per.publicservice.lov.server.PerPSCountryVOImpl;
import xxup.oracle.apps.per.publicservice.lov.server.PerPSProjectTypeVOImpl;
import xxup.oracle.apps.per.publicservice.lov.server.PerPSSubjectAreaInterestVOImpl;
import xxup.oracle.apps.per.publicservice.server.XxupPerPublicServiceBenifEOVOImpl;
import xxup.oracle.apps.per.publicservice.server.XxupPerPublicServiceCatEOVOImpl;
import xxup.oracle.apps.per.publicservice.server.XxupPerPublicServiceSubjEOVOImpl;


// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class PublicServiceAMImpl extends OAApplicationModuleImpl {
    /**This is the default constructor (do not remove)
     */
    public PublicServiceAMImpl() {
    }

    /**Container's getter for XxupPerPublicServiceBenifEOVO1
     */
    public XxupPerPublicServiceBenifEOVOImpl getXxupPerPublicServiceBenifEOVO1() {
        return (XxupPerPublicServiceBenifEOVOImpl)findViewObject("XxupPerPublicServiceBenifEOVO1");
    }

    /**Container's getter for XxupPerPublicServiceCatEOVO1
     */
    public XxupPerPublicServiceCatEOVOImpl getXxupPerPublicServiceCatEOVO1() {
        return (XxupPerPublicServiceCatEOVOImpl)findViewObject("XxupPerPublicServiceCatEOVO1");
    }


    /**Container's getter for XxupPerPublicServiceSubjEOVO1
     */
    public XxupPerPublicServiceSubjEOVOImpl getXxupPerPublicServiceSubjEOVO1() {
        return (XxupPerPublicServiceSubjEOVOImpl)findViewObject("XxupPerPublicServiceSubjEOVO1");
    }

    /**Sample main for debugging Business Components code using the tester.
     */
    public static void main(String[] args) { /* package name */
        /* Configuration Name */launchTester("xxup.oracle.apps.per.publicservice.server", 
                                             "PublicServiceAMLocal");
    }


    public void initApprovers(String sequenceNo) {
        try{
            
            
            Connection conn = getOADBTransaction().getJdbcConnection();
            CallableStatement stmt = conn.prepareCall("{call xxup_ps_wf_pkg.init_approvers(?)}");
            
            stmt.setString(1, sequenceNo);
            stmt.execute();
            stmt.close();
            
        }catch(Exception ex){
            throw new OAException("Error occured initializing approvers table " + ex);
        }
    }


    public void initWF(String sequenceNo) {
        try{
            
            
            Connection conn = getOADBTransaction().getJdbcConnection();
            CallableStatement stmt = conn.prepareCall("{call xxup_ps_wf_pkg.init_wf(?)}");
            
            
            stmt.setString(1, sequenceNo);
            stmt.execute();
            stmt.close();
            
        }catch(Exception ex){
            throw new OAException("Error occured calling workflow " + ex);
        }
    }
    
    public void resubmitPS(String sequenceNo) {
        try{
            
            
            Connection conn = getOADBTransaction().getJdbcConnection();
            CallableStatement stmt = conn.prepareCall("{call xxup_ps_wf_pkg.resubmit(?)}");
            
            
            stmt.setString(1, sequenceNo);
            stmt.execute();
            stmt.close();
            
        }catch(Exception ex){
            throw new OAException("Error occured calling workflow " + ex);
        }
    }
    

    public void commitTransaction() {


        getTransaction().commit();
    }


    public void rollbackPS() {
        Transaction txn = getTransaction();
        if (txn.isDirty()) {
            txn.rollback();
        }
    }

    public void initVOForNewRequest() {
        // XxupPerPublicServiceHeaderEOVOImpl pshVO = 
        //     getXxupPerPublicServiceHeaderEOVO1();
        // pshVO.initNewVO();

        XxupPerPSHeaderTrEOVOImpl pshVO = 
            getXxupPerPSHeaderTrEOVO1();
        pshVO.initNewRecord();

        
        XxupPerPublicServiceCatEOVOImpl ppscVO = 
            getXxupPerPublicServiceCatEOVO1();
        ppscVO.initNewRecord();


        XxupPerPublicServiceBenifEOVOImpl ppsbVO =
            getXxupPerPublicServiceBenifEOVO1();
        ppsbVO.initNewRecord();
        
        
        XxupPerPublicServiceAddrEOVOImpl addrVO =
            getXxupPerPublicServiceAddrEOVO1();
            addrVO.initNewRecord();
        
        
        
        

        //
        LoadNewSubjectAreaOfInterestInTable();
        // testLoad("2502");

    }
    
    
    public void setAttachments(String paramSequenceNo) {
        try {
            
            // XxupPerPublicServiceHeaderEOVOImpl pshVO = 
            //     getXxupPerPublicServiceHeaderEOVO1();
            // pshVO.initExistingPS(paramSequenceNo);
            
            XxupPerPSHeaderTrEOVOImpl pshVO = 
                getXxupPerPSHeaderTrEOVO1();
            pshVO.initExistingPS(paramSequenceNo);
            
            /*
            PerPSAttachmentsVOImpl attVO = 
                getPerPSAttachmentsVO1();
            
            
            attVO.getAttachments(paramSequenceNo);*/
            
        }catch(Exception ex){
            OAException.wrapperException(ex);
        }
    }
    
    
    public void setApproversTable(String paramSequenceNo) {
        try {
            
            PerPSActionHistoryVOImpl ahVO = 
                getPerPSActionHistoryVO1();
            
            
            
            ahVO.initApprovers(paramSequenceNo);
            
        }catch(Exception ex){
            OAException.wrapperException(ex);
        }
    }
    
    
    public void reviewPS(String paramSequenceNo) {
        
    
        try {
            
            // XxupPerPublicServiceHeaderEOVOImpl pshVO = 
            //     getXxupPerPublicServiceHeaderEOVO1();
            // pshVO.initExistingPS(paramSequenceNo);

            XxupPerPSHeaderTrEOVOImpl pshVO = 
                getXxupPerPSHeaderTrEOVO1();
            pshVO.initExistingPS(paramSequenceNo);
            
            
            PerPSActionHistoryVOImpl ahVO = 
                getPerPSActionHistoryVO1();
            ahVO.initApprovers(paramSequenceNo);
            
            
            XxupPerPublicServiceCatEOVOImpl ppscVO = 
                getXxupPerPublicServiceCatEOVO1();
            ppscVO.initExistingPS(paramSequenceNo);
            
            XxupPerPublicServiceSubjEOVOImpl ppssVO = 
                getXxupPerPublicServiceSubjEOVO1();
            ppssVO.initExistingPS(paramSequenceNo);

            XxupPerPublicServiceBenifEOVOImpl ppsbVO = 
                getXxupPerPublicServiceBenifEOVO1();
            ppsbVO.initExistingPS(paramSequenceNo);
                        
            XxupPerPublicServiceAddrEOVOImpl addrVO = 
                getXxupPerPublicServiceAddrEOVO1();
            addrVO.initExistingPS(paramSequenceNo);    
            
            
            PerPSAttachmentsVOImpl attVO = 
                getPerPSAttachmentsVO1();
                
            attVO.getAttachments(paramSequenceNo);
            
            
        }catch(Exception ex){
            throw OAException.wrapperException(ex);
        }
    }

    public void updatePS(String paramSequenceNo) {
        try {
            
            // XxupPerPublicServiceHeaderEOVOImpl ppshvo = 
            //     getXxupPerPublicServiceHeaderEOVO1();
            // ppshvo.initExistingPS(paramSequenceNo);
            
            XxupPerPSHeaderTrEOVOImpl pshVO = 
                getXxupPerPSHeaderTrEOVO1();
            pshVO.initExistingPS(paramSequenceNo);
            

            XxupPerPublicServiceCatEOVOImpl ppscVO = 
                getXxupPerPublicServiceCatEOVO1();
            ppscVO.initExistingPS(paramSequenceNo);
            
            if(ppscVO.getRowCount() <= 0){
                ppscVO.initNewRecord();
            }

            XxupPerPublicServiceBenifEOVOImpl ppsbVO = 
                getXxupPerPublicServiceBenifEOVO1();
            ppsbVO.initExistingPS(paramSequenceNo);
            
            if(ppsbVO.getRowCount() <= 0){
                ppsbVO.initNewRecord();
            }
            
            XxupPerPublicServiceSubjEOVOImpl ppssVO = 
                getXxupPerPublicServiceSubjEOVO1();
            ppssVO.initExistingPS(paramSequenceNo);
            
            if(ppssVO.getRowCount() <= 0){
                LoadNewSubjectAreaOfInterestInTable();
            }else if (ppssVO.getRowCount() >= 1){
                LoadExistSubjectAreaOfInterestTable(paramSequenceNo);
            }
            
            
            
            
            XxupPerPublicServiceAddrEOVOImpl addrVO = 
                getXxupPerPublicServiceAddrEOVO1();
            addrVO.initExistingPS(paramSequenceNo);    
            
            if(addrVO.getRowCount() <= 0){
                addrVO.initNewRecord();
            }
            

        } catch (Exception ex) {
            throw OAException.wrapperException(ex);
        }

    }
    
    /*
    public void removeRecord(){
        XxupPerPublicServiceHeaderEOVOImpl vo = 
            getXxupPerPublicServiceHeaderEOVO1();

        String sequenceNo = 
            vo.getCurrentRow().getAttribute("SequenceNo").toString();
        //System.out.println(sequenceNo);

        Row headerRow = (OAViewRowImpl)vo.getCurrentRow();
        
        headerRow.remove();
        
        
        if (headerRow != null) {
            
        }
        
    }*/

    public void saveDetails() {


        /*Object Category*/
        // XxupPerPublicServiceHeaderEOVOImpl vo = 
        //     getXxupPerPublicServiceHeaderEOVO1();

        XxupPerPSHeaderTrEOVOImpl vo =
            getXxupPerPSHeaderTrEOVO1();

        String sequenceNo = 
            vo.getCurrentRow().getAttribute("SequenceNo").toString();
        //System.out.println(sequenceNo);

        Row headerRow = (OAViewRowImpl)vo.getCurrentRow();


        if (headerRow != null) {


            XxupPerPublicServiceCatEOVOImpl ppscVO = 
                getXxupPerPublicServiceCatEOVO1();

            Integer lineNumber = 1;
            //System.out.println("total count" + lineNumber); 

            for (Row rowi: ppscVO.getAllRowsInRange()) {
                String objCatDisplay = "";
                if (rowi.getAttribute("Attribute1") != null) {
                    objCatDisplay = rowi.getAttribute("Attribute1").toString();
                }


                if (objCatDisplay != "") {
                    rowi.setAttribute("SequenceNo", sequenceNo);
                    rowi.setAttribute("LineNumber", lineNumber);


                    lineNumber += 1;
                } else if (objCatDisplay == "") {
                    rowi.remove();
                }


            }
            
            //reset counter
            lineNumber = 1;

            
            /*Beneficiary Org*/
            XxupPerPublicServiceBenifEOVOImpl ppsbVO =
                getXxupPerPublicServiceBenifEOVO1();
            
            for (Row rowi: ppsbVO.getAllRowsInRange()) {
                String OrgName = "";
                if (rowi.getAttribute("BeneficiaryOrganization") != null) {
                    OrgName = rowi.getAttribute("BeneficiaryOrganization").toString();
                }
                
                if (OrgName != "") {
                    rowi.setAttribute("SequenceNo", sequenceNo);
                    rowi.setAttribute("LineNumber", lineNumber);


                    lineNumber += 1;
                    
                } else if (OrgName == "") {
                    rowi.remove();
                }
            
                

            }

            //reset counter
            lineNumber = 1;
    
            
            /*Subject area of interest*/
            
            XxupPerPublicServiceSubjEOVOImpl ppssVO =
                getXxupPerPublicServiceSubjEOVO1();
            
            
            
            Row selectedRows [] = ppssVO.getFilteredRows("Selected", "Y");
            Row deselectedRows [] = ppssVO.getFilteredRows("Selected", null);
            
            
            
            for (Row rowi: selectedRows) {

                rowi.setAttribute("SequenceNo", sequenceNo);
                rowi.setAttribute("LineNumber", lineNumber);

                lineNumber += 1;

            }


            


            for (Row rowi: ppssVO.getAllRowsInRange()) {
                //System.out.println(rowi.getAttribute("Attribute1").toString());
                if(rowi.getAttribute("Selected") == null) {
                    rowi.remove();
                }
            }
            
            lineNumber = 1;
            
            /*Address*/
            XxupPerPublicServiceAddrEOVOImpl addrVO = 
                getXxupPerPublicServiceAddrEOVO1();


            for (Row rowi: addrVO.getAllRowsInRange()) {
                String address = "";
                if (rowi.getAttribute("Address") != null) {
                    address = rowi.getAttribute("Address").toString();
                }


                if (address != "") {
                    rowi.setAttribute("SequenceNo", sequenceNo);
                    rowi.setAttribute("LineNumber", lineNumber);


                    lineNumber += 1;
                } else if (address == "") {
                    rowi.remove();
                }
            }
        }

    }

    // Use when we don't have AO

    public String getSequenceValue() {

        OADBTransaction tr = getOADBTransaction();
        return tr.getSequenceValue("XXUP.XXUP_PER_PUBLIC_SERVICE_SEQ").toString();

    }

    /**Container's getter for PerPSSubjectAreaInterestVO1
     */
    public PerPSSubjectAreaInterestVOImpl getPerPSSubjectAreaInterestVO1() {
        return (PerPSSubjectAreaInterestVOImpl)findViewObject("PerPSSubjectAreaInterestVO1");
    }


    public void LoadNewSubjectAreaOfInterestInTable() {

        XxupPerPublicServiceSubjEOVOImpl ppssVO = 
            getXxupPerPublicServiceSubjEOVO1();
        ppssVO.setMaxFetchSize(0);
        ppssVO.executeQuery();

        //
        if (ppssVO.getRowCount() < 1) {
            PerPSSubjectAreaInterestVOImpl ppsaVO = 
                getPerPSSubjectAreaInterestVO1();

            ppsaVO.executeQuery();
            Integer line = ppsaVO.getRowCount();
            Row newRowForSubArea = null;
            Row row = null;
            for (row = (OAViewRowImpl)ppsaVO.first(); row != null; 
                 row = (OAViewRowImpl)ppsaVO.next()) {
                newRowForSubArea = ppssVO.createRow();
                newRowForSubArea.setAttribute("SubjectAreaInterest", 
                                              row.getAttribute("SubjectAreaInterestId"));
                newRowForSubArea.setAttribute("Attribute1", 
                                              row.getAttribute("SubjectAreaInterestDisplay"));
                ppssVO.insertRow(newRowForSubArea);
                //line = line - 1;
            }
        }
        //
    }


    public void LoadExistSubjectAreaOfInterestTable(String sequenceNo) {

        XxupPerPublicServiceSubjEOVOImpl existVO = 
            getXxupPerPublicServiceSubjEOVO1();
            
        
        existVO.initExistingPS(sequenceNo);
        
        //if (ppssVO.getRowCount() < 1) {
        PerPSSubjectAreaInterestVOImpl ppsaVO = 
            getPerPSSubjectAreaInterestVO1();

        ppsaVO.executeQuery();
        Integer line = ppsaVO.getRowCount();
        Row newRowForSubArea = null;
        Row row = null;
        
        
        //Row[] rowExists = existVO.getAllRowsInRange();
        
        
        /**/
        RowSetIterator rs = existVO.createRowSetIterator(null);
        String[] arrExistSubj = new String[rs.getRowCount()];
        rs.reset();
        
        
        int ctr = 0;
        while(rs.hasNext()){
            Row r = rs.next();
            arrExistSubj[ctr] = r.getAttribute("Attribute1").toString();
            ctr++;
            //System.out.println(r.getAttribute("Attribute1").toString());
        }
        
        
        rs.closeRowSetIterator();
        
        for (row = (OAViewRowImpl)ppsaVO.first(); row != null; 
             row = (OAViewRowImpl)ppsaVO.next()) {
             
             
            String subjArea = row.getAttribute("SubjectAreaInterestDisplay").toString();
            //Set<String> alreadyExist = new HashSet<String>());
            
            
            if(!Arrays.asList(arrExistSubj).contains(subjArea)){
                newRowForSubArea = existVO.createRow();
                newRowForSubArea.setAttribute("SubjectAreaInterest", 
                                              row.getAttribute("SubjectAreaInterestId"));
                newRowForSubArea.setAttribute("Attribute1", 
                                              row.getAttribute("SubjectAreaInterestDisplay"));
                existVO.insertRow(newRowForSubArea);
                
            }
             
            
            //line = line - 1;
        }
        
        
        existVO.setOrderByClause("Attribute1");
        
        
        
//        
//        Row [] distinctRows = new Row[allRows.size()];
//        
//        int ctr = 0;    
//        
//        
//        for(Row rowi : allRows){
//            if(alreadyPresent.add(rowi))
//                distinctRows[ctr++] = rowi;
//        }
//        
//        
//        setXxup
        
        
        //Set<Row> distinctRow = new HashSet<>(Arrays.asList(existVO.getAllRowsInRange()));
        
        
        

    }
    



    


    public void showSummaryVO() {
        XxupPerPSApprovedVOImpl vo = 
            getXxupPerPSApprovedVO1();

        if (vo != null) {
            vo.showSummaryVO();
        }
        
        
    }

    /**Container's getter for PerPSBeneficiaryCategoryVO1
     */
    public PerPSBeneficiaryCategoryVOImpl getPerPSBeneficiaryCategoryVO1() {
        return (PerPSBeneficiaryCategoryVOImpl)findViewObject("PerPSBeneficiaryCategoryVO1");
    }

    /**Container's getter for PerPSProjectTypeVO1
     */
    public PerPSProjectTypeVOImpl getPerPSProjectTypeVO1() {
        return (PerPSProjectTypeVOImpl)findViewObject("PerPSProjectTypeVO1");
    }


    /**Container's getter for PerPSBeneficiaryTypeVO1
     */
    public PerPSBeneficiaryTypeVOImpl getPerPSBeneficiaryTypeVO1() {
        return (PerPSBeneficiaryTypeVOImpl)findViewObject("PerPSBeneficiaryTypeVO1");
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

    /**Container's getter for XxupPerPublicServiceAddrEOVO1
     */
    public XxupPerPublicServiceAddrEOVOImpl getXxupPerPublicServiceAddrEOVO1() {
        return (XxupPerPublicServiceAddrEOVOImpl)findViewObject("XxupPerPublicServiceAddrEOVO1");
    }

    /**Container's getter for PerPSActionHistoryVO1
     */
    public PerPSActionHistoryVOImpl getPerPSActionHistoryVO1() {
        return (PerPSActionHistoryVOImpl)findViewObject("PerPSActionHistoryVO1");
    }

    /**Container's getter for PerPSAttachmentsVO1
     */
    public PerPSAttachmentsVOImpl getPerPSAttachmentsVO1() {
        return (PerPSAttachmentsVOImpl)findViewObject("PerPSAttachmentsVO1");
    }


    /**Container's getter for PerPSActivityTypeVO1
     */
    public PerPSActivityTypeVOImpl getPerPSActivityTypeVO1() {
        return (PerPSActivityTypeVOImpl)findViewObject("PerPSActivityTypeVO1");
    }

    /**Container's getter for XxupPerPSApprovedVO1
     */
    public XxupPerPSApprovedVOImpl getXxupPerPSApprovedVO1() {
        return (XxupPerPSApprovedVOImpl)findViewObject("XxupPerPSApprovedVO1");
    }

    /**Container's getter for XxupPerPSHeaderTrEOVO1
     */
    public XxupPerPSHeaderTrEOVOImpl getXxupPerPSHeaderTrEOVO1() {
        return (XxupPerPSHeaderTrEOVOImpl)findViewObject("XxupPerPSHeaderTrEOVO1");
    }
}
