/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.reports;

import com.saake.invoicer.entity.CustomerVehicle;
import com.saake.invoicer.entity.Invoice;
import com.saake.invoicer.entity.InvoiceItems;
import com.saake.invoicer.model.InvoiceItemsData;
import com.saake.invoicer.model.InvoiceReportData;
import com.saake.invoicer.util.JsfUtil;
import com.saake.invoicer.util.Utils;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.naming.InitialContext;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jn
 */
public class ReportHelper {
    private static final Log log = LogFactory.getLog(ReportHelper.class);

    public static List<InvoiceReportData> buildDataListForInvoiceReport(Invoice inv) {
    
        List<InvoiceReportData> dataList = new ArrayList<>();
        if(inv != null){
            InvoiceReportData dat = new InvoiceReportData();
            
            dat.setAmount(inv.getAmount());
            dat.setDiscount(inv.getDiscount());
            dat.setInvoiceDate(inv.getInvoiceDate());
            dat.setInvoiceNumber(Utils.notBlank(inv.getInvoiceNum())? inv.getInvoiceId() + " - " +inv.getInvoiceNum(): inv.getInvoiceId().toString());            
            dat.setInvoiceItems(convertInvoiceItems(inv.getInvoiceItemsAsList()));
            dat.setAddressLine1(inv.getCustomerId().getAddressLine1());
            dat.setAddressLine2(inv.getCustomerId().getAddressLine2());
            dat.setCity(inv.getCustomerId().getCity());
            dat.setCompanyName(inv.getCustomerId().getCompanyName());
            dat.setEmail(inv.getCustomerId().getEmail());
            dat.setFirstName(inv.getCustomerId().getFirstName());
            dat.setLastName(inv.getCustomerId().getLastName());
            dat.setStateProvince(inv.getCustomerId().getStateProvince());
            dat.setMobileNum(inv.getCustomerId().getMobileNum());            
            
            CustomerVehicle veh = inv.getCustomerId().getCustomerVehicles().get(0);
            dat.setMake(veh.getMake());            
            dat.setMileage(veh.getMileage());            
            dat.setModel(veh.getModel());            
            dat.setVin(veh.getVin());            
            dat.setYear(veh.getYear());            
            
            dataList.add(dat);
        }
        return dataList;
    }
        
    private static void streamPdf(Invoice inv, Boolean download) throws IOException {
         byte[] pdfByteArray = generatePdfFromJasperTemplate(buildDataListForInvoiceReport(inv), "saakeInvoice.jasper");

        if (pdfByteArray != null && pdfByteArray.length > 0) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                baos.write(pdfByteArray);
                ExternalContext ec = JsfUtil.getExternalContext();
                ec.responseReset();
                ec.setResponseContentType("application/pdf");
                ec.setResponseContentLength(baos.size());
                if(download){
                    ec.setResponseHeader("Content-Disposition", "attachment; filename=saake-invoice.pdf");
                }
                ec.setResponseHeader("Expires", "0");
                ec.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                ec.setResponseHeader("Pragma", "private");

                baos.writeTo(ec.getResponseOutputStream());
                ec.getResponseOutputStream().flush();
            } finally {
                JsfUtil.getFacesContext().responseComplete();

                baos.close();
            }
        }
    }
    
    public static void downloadPDF(Invoice inv) throws IOException {
        streamPdf(inv, Boolean.TRUE);
    }
    
    public static <T> byte[] generatePdfFromJasperTemplate(List<T> dataList, String template) {
        byte[] pdfByteArray = null;
        try{
            if (Utils.notEmpty(dataList) && Utils.notBlank(template)) {
                try {
                    
                    Map<String, Object> parameters = new HashMap<String, Object>();
                    parameters.put(JRFillParameter.REPORT_FILE_RESOLVER, fileResolver);
                    
                    JasperPrint jasperPrint = fillJasperTemplate(template, "JAVABEAN", dataList, parameters);

                    if (jasperPrint != null) {
                        pdfByteArray = generatePdfBytesFromJasperTemplate(jasperPrint);
                    }
                } catch (Exception e) {
                    throw new Exception("Error exporting pdf", e);
                }
            } else {
                throw new Exception("No data to generate pdf!");
            }
        }catch (Exception e){
            log.error("",e);
            System.out.println(e.getMessage());
            JsfUtil.addErrorMessage("Error exporting pdf for template:"+template);
        }

        return pdfByteArray;
    }
    
    public static <T> JasperPrint fillJasperTemplate(String templateName, String dataSrc, List<T> dataList,
                                                               Map<String, Object> params) throws Exception{
        InputStream is;
        JasperReport jReport = null;
        JasperPrint jPrint = null;

        if (Utils.isEmpty(dataList)) {
            throw new Exception("No data to fill");
        }
        
        if(Utils.notBlank(templateName)){
            try{
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/saake/invoicer/reports/"+templateName);

                if (is != null) {
                    jReport = (JasperReport) JRLoader.loadObject(is);
                }

                if (jReport != null) {
                    
                    if ( "JDBC".equalsIgnoreCase(dataSrc)){
                        Connection con = ((DataSource) (new InitialContext().lookup(""))).getConnection();
                        jPrint = JasperFillManager.fillReport(jReport, params, con);
                    }
                    else
                    if ( "JAVABEAN".equalsIgnoreCase(dataSrc)){
                        JRBeanCollectionDataSource jrDataSource = new JRBeanCollectionDataSource(dataList);
                        jPrint = JasperFillManager.fillReport(jReport, params, jrDataSource);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());

                throw new Exception("Error generating JR Template:"+templateName,e);
            }
        }
        else{
            throw new Exception("No template to fill");
        }

        return jPrint;
    }
     
     public static byte[] generatePdfBytesFromJasperTemplate(JasperPrint jPrint){
        byte[] pdfByteArray = new byte[0];

        try {
            pdfByteArray = JasperExportManager.exportReportToPdf(jPrint);
//            jrHtmlExp.exportReport();
        } catch (JRException e) {
            log.error("Error generating PDF from Jasper Template:"+ jPrint.getName(),e);
        }

        return pdfByteArray;

    }
     
    public static void printJasperReport(JasperPrint jasperPrint) throws Exception {
        
        try {

            PrinterJob job = PrinterJob.getPrinterJob();
            /* Create an array of PrintServices */
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

            PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
            job.defaultPage(pf);
            int selectedService = 0;


            String theUserPrinterName = "\\\\office1\\printer1";
            AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName(theUserPrinterName, null));
            services = PrintServiceLookup.lookupPrintServices(null, attrSet);
                
            job.setPrintService(services[selectedService]);
            
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            printRequestAttributeSet.add(MediaSizeName.NA_LETTER);
            printRequestAttributeSet.add(new Copies(1));

            JRPrintServiceExporter exporter = new JRPrintServiceExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            /* We set the selected service and pass it as a paramenter */
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, services[selectedService]);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, services[selectedService].getAttributes());
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
            exporter.exportReport();
        } catch (Exception e) {
           throw new Exception("Error processing printing",e);
//            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.TRUE);
//            try {
//                exporter.exportReport();
//            } catch (JRException e2) {
//                e2.printStackTrace();
//            }
        }
    }
    
    public static void printInvoice(Invoice inv) throws Exception{
        try {
            JasperPrint jasperPrint = 
                    fillJasperTemplate("saakeInvoice.jasper", "JAVABEAN", buildDataListForInvoiceReport(inv), null);

            if (jasperPrint != null) {
                printJasperReport(jasperPrint);
            }
        } catch (Exception e) {
            throw new Exception("Error exporting pdf", e);
        }
    }

    public static void viewPDF(Invoice inv) throws IOException {
        streamPdf(inv, Boolean.FALSE);
    }

    private static List<InvoiceItemsData> convertInvoiceItems(List<InvoiceItems> invoiceItems) {
        List<InvoiceItemsData> list = new ArrayList<>();
        
        for(InvoiceItems invItem : invoiceItems){
            InvoiceItemsData itData = new InvoiceItemsData();
            
            itData.setAmount(invItem.getAmount());
            itData.setDescription(invItem.getItem().getDescription());
            itData.setQuantity(invItem.getQuantity());
            itData.setUnitCost(invItem.getItem().getUnitCost());
            
            list.add(itData);
        }
        
        return list;
    }
    
    private static FileResolver fileResolver = new FileResolver() {
        @Override
        public File resolveFile(String fileName) {
            URI uri;
            try {
                uri = new URI(this.getClass().getResource(fileName).getPath());
                return new File(uri.getPath());
            } catch (URISyntaxException e) {
                log.error("",e);
                return null;
            }
        }
    };
}
