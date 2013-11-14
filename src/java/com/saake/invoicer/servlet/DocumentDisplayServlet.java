/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.servlet;

import com.saake.invoicer.controller.InvoiceController;
import com.saake.invoicer.entity.Invoice;
import com.saake.invoicer.model.InvoiceReportData;
import com.saake.invoicer.reports.ReportHelper;
import com.saake.invoicer.util.Utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jn
 */
public class DocumentDisplayServlet extends HttpServlet {
    private static final Log log = LogFactory.getLog(DocumentDisplayServlet.class);
    private static final long serialVersionUID = -4479218862735017202L;

    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        this.doPost(httpServletRequest, httpServletResponse);
    }

    @Override
    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        OutputStream out = null;

        try {
            if (httpServletRequest.getParameter("generateInvoicePdf") != null &&
                httpServletRequest.getParameter("generateInvoicePdf").equalsIgnoreCase("Y")) {

                Object obj = httpServletRequest.getSession(false).getAttribute("invoice");

                Invoice inv = null;

                if(obj != null){
                    inv = (Invoice)obj;
                }

                if(inv != null){

                    List<InvoiceReportData> dataList = ReportHelper.buildDataListForInvoiceReport(inv);

                    respondWithPdf(
                            httpServletResponse, ReportHelper.generatePdfFromJasperTemplate(dataList, "saakeInvoice.jasper"));
                }
                else{
                    throw new Exception("No data found to generate report!");
                }
            }
        } catch (Exception e) {
            log.error("",e);
            httpServletResponse.getWriter().print("<h2>Error: " + e.getMessage() + " </h2>");
        } finally {
            Utils.closeOutputStream(out);
            //FacesUtil.getHttpSession().setAttribute("elmEquipMeterData", null);
        }
    }

    private String getExtension(String type) {
        String extension = "pdf";
        if (type == null || type.equalsIgnoreCase("PDF")) {
            extension = "pdf";
        } else if (type.equalsIgnoreCase("RTF")) {
            extension = "rtf";
        } else if (type.equalsIgnoreCase("DOC")) {
            extension = "doc";
        } else if (type.equalsIgnoreCase("DOT")) {
            extension = "doc";
        } else if (type.equalsIgnoreCase("XLS")) {
            extension = "xls";
        } else if (type.equalsIgnoreCase("PPT")) {
            extension = "ppt";
        } else if (type.equalsIgnoreCase("GIF")) {
            extension = "gif";
        } else if (type.equalsIgnoreCase("JPG")) {
            extension = "jpg";
        } else if (type.equalsIgnoreCase("PNG")) {
            extension = "png";
        } else if (type.equalsIgnoreCase("BMP")) {
            extension = "bmp";
        } else if (type.equalsIgnoreCase("TIF")) {
            extension = "tiff";
        }

        return extension;
    }


    private String getContentType(String type) {
        String contType = "application/pdf";
        if (type == null || type.equalsIgnoreCase("PDF")) {
            contType = "application/pdf";
        } else if (type.equalsIgnoreCase("RTF")) {
            contType = "application/rtf";
        } else if (type.equalsIgnoreCase("DOC")) {
            contType = "application/msword";
        } else if (type.equalsIgnoreCase("DOT")) {
            contType = "application/msword";
        } else if (type.equalsIgnoreCase("XLS")) {
            contType = "application/vnd.ms-excel";
        } else if (type.equalsIgnoreCase("PPT")) {
            contType = "application/vnd.ms-powerpoint";
        } else if (type.equalsIgnoreCase("GIF")) {
            contType = "image/gif";
        } else if (type.equalsIgnoreCase("JPG")) {
            contType = "image/jpg";
        } else if (type.equalsIgnoreCase("PNG")) {
            contType = "image/png";
        } else if (type.equalsIgnoreCase("BMP")) {
            contType = "image/bmp";
        } else if (type.equalsIgnoreCase("TIF")) {
            contType = "image/tiff";
        }

        return contType;
    }


    private void setHeaderMime(HttpServletResponse httpServletResponse, String mime, String fileName) {
        if ("excel".equalsIgnoreCase(mime)) {
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.setHeader("Cache-Control", "private"); //HTTP 1.1
            httpServletResponse.setHeader("Pragma", "private"); //HTTP 1.0
            httpServletResponse.setDateHeader("Expires", 0);//to prevents caching at the proxy server
            httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + fileName);
        } else if ("pdf".equalsIgnoreCase(mime)) {
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setHeader("Cache-Control", "private"); //HTTP 1.1
            httpServletResponse.setHeader("Pragma", "private"); //HTTP 1.0
            httpServletResponse.setDateHeader("Expires", 0);//to prevents caching at the proxy server
            httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + fileName);
        }
    }

    public void respondWithSpreadSheet(HttpServletResponse httpServletResponse, Object handler, String docName) throws Exception {
        File file = null;
        FileOutputStream fos = null;

        try {

            if (httpServletResponse == null) {
                throw new Exception("Http Response is null");
            }

            if (handler == null) {
                throw new Exception("No handler found to get data for spreadsheet");
            }

            if (docName == null) {
                docName = "connectReportTemp.xls";
            }

            //String filePath = JfwConfig.getInstance().getProperty("temp.upload.file.path");
            String filePath = "C:/";
            String fileName = System.currentTimeMillis() + docName;
            file = new File(filePath);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    throw new Exception("Error creating directory for temp document generation");
                }
            }

            file = new File(filePath, fileName);
            fos = new FileOutputStream(file);

            Method m = handler.getClass().getDeclaredMethod("exportDataToSpreadSheet");
//            HSSFWorkbook wb = (HSSFWorkbook) m.invoke(handler);
//
//            wb.write(fos);
//
//            httpServletResponse.setContentType("application/vnd.ms-excel");
//            httpServletResponse.setContentLength((int) file.length());
//            httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + docName);
//            httpServletResponse.getOutputStream().write(FileUtils.getBytesFromFile(file.getPath()));

            httpServletResponse.flushBuffer();

        } catch (Exception e) {
            throw new Exception("Error while exporting Excel", e);
        }

        finally {
            Utils.closeOutputStream(fos);
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    public void respondWithPdf(HttpServletResponse response, byte[] pdfByteArray) throws Exception {

        try {
            if (pdfByteArray.length > 0) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    baos.write(pdfByteArray);

                    response.reset();
                    response.setContentType("application/pdf");
                    response.setHeader("Expires", "0");
                    response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                    response.setHeader("Pragma", "private");
                    response.setHeader("Content-disposition", "attachment;filename=SaakeInvoice.pdf");
                    response.setContentLength(baos.size());

//                    out.write(pdfByteArray, 0, pdfByteArray.length);
                    baos.writeTo(response.getOutputStream());
                    response.getOutputStream().flush();
                } finally {
                    baos.close();
                }
            } else {
                throw new Exception("No data to generate pdf!");
            }

        } catch (Exception e) {
            throw new Exception("Error while generating pdf", e);
        }
    }
}
