package org.jarvis.phmart.view.polymer;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;
import org.jarvis.phmart.model.jasper.ReceiptField;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterName;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created: kim chheng
 * Date: 18-Jun-2019 Tue
 * Time: 9:02 PM
 */
public class Dummy {

    public static void main(String[] args) throws JRException, IOException {

        DecimalFormat df = new DecimalFormat("$0.00");
        ReceiptField field = new ReceiptField();
        field.setName("Vital");
        field.setQty(String.valueOf(10));
        field.setPrice(df.format(2.5f));
        field.setDisc("10%");
        field.setTotal(df.format(2.5f));
        List<ReceiptField> list = new ArrayList<>();
        list.add(field);
        field=new ReceiptField();
        field.setName("Dasani");
        field.setQty(String.valueOf(10));
        field.setPrice(df.format(2.5f));
        field.setDisc("10%");
        field.setTotal(df.format(2.5f));
        list.add(field);
        //Resource resource = new ClassPathResource("receipt/good-day-mart.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\KimChheng\\ireport\\good-day-mart.jrxml");
        //JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        Map<String, Object> param = new HashMap<>();
        param.put("cashier", "Admin");
        param.put("exchangeRate", "$1=4100");
        param.put("customer", "General");
        param.put("date", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        param.put("grandQty", String.valueOf(5));
        param.put("grandTotal", df.format(10.5f));
        param.put("cashReceiveUSD", df.format(10.5f));
        param.put("cashReceiveKH", "1000៛");
        param.put("changeKH", "0៛");
        param.put("changeUSD", df.format(2.0f));
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource);
        JasperViewer.viewReport(jasperPrint, false);
       // printReportToPrinter(jasperPrint);

        // JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream("/tmp/jasper-report.pdf"));
        // JasperExportManager.exportReportToPdfFile(jp, "jasper-report.pdf");
        //JasperExportManager.exportReportToHtmlFile(jasperPrint, "/tmp/jasper-report.html");
        //JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream("/tmp/report.pdf"));

        /*try {
            InputStream jrxmlInput = new FileInputStream("/opt/good-day-mart/good-day-mart.jrxml");
            JasperDesign design = JRXmlLoader.load(jrxmlInput);
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            //logger.info("Report compiled");
            //String data = invokeService();
            //logger.info("data = "+ data);
            // It is possible to generate Jasper reports from a JSON source.
            //JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(data.getBytes()));
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource);
            //logger.info("JasperPrint" + jasperPrint);

            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
            pdfExporter.exportReport();
            IOUtils.copy(new ByteArrayInputStream(pdfReportStream.toByteArray()), new FileOutputStream("/tmp/report.pdf"));

            //response.setContentType("application/pdf");
            //response.setHeader("Content-Length", String.valueOf(pdfReportStream.size()));
            //response.addHeader("Content-Disposition", "inline; filename=jasper.pdf;");

            //OutputStream responseOutputStream = response.getOutputStream();
            //responseOutputStream.write(pdfReportStream.toByteArray());
            //responseOutputStream.close();
            pdfReportStream.close();
            //logger.info("Completed Successfully: ");
        } catch (Exception e) {
            //logger.info("Error: ", e);
        }*/
    }

    private static void printReportToPrinter(JasperPrint jasperPrint) throws JRException {

//Get the printers names
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

//Lets set the printer name based on the registered printers driver name (you can see the printer names in the services variable at debugging)
        String selectedPrinter = "POS-80C";
// String selectedPrinter = "\\\\S-BPPRINT\\HP Color LaserJet 4700"; // examlpe to network shared printer

        System.out.println("Number of print services: " + services.length);
        PrintService selectedService = null;

//Set the printing settings
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printRequestAttributeSet.add(MediaSizeName.ISO_A4);
        printRequestAttributeSet.add(new Copies(1));
        if (jasperPrint.getOrientationValue() == OrientationEnum.LANDSCAPE) {
            printRequestAttributeSet.add(OrientationRequested.LANDSCAPE);
        } else {
            printRequestAttributeSet.add(OrientationRequested.PORTRAIT);
        }
        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        printServiceAttributeSet.add(new PrinterName(selectedPrinter, null));

        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
        configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
        configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
        configuration.setDisplayPageDialog(false);
        configuration.setDisplayPrintDialog(false);

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setConfiguration(configuration);

//Iterate through available printer, and once matched with our <selectedPrinter>, go ahead and print!
        if (services != null && services.length != 0) {
            for (PrintService service : services) {
                String existingPrinter = service.getName();
                if (existingPrinter.equals(selectedPrinter)) {
                    selectedService = service;
                    break;
                }
            }
        }
        if (selectedService != null) {
            try {
                //Lets the printer do its magic!
                exporter.exportReport();
            } catch (Exception e) {
                System.out.println("JasperReport Error: " + e.getMessage());
            }
        } else {
            System.out.println("JasperReport Error: Printer not found!");
        }
    }
}
