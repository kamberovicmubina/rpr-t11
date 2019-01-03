package ba.unsa.etf.rpr;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;


public class GradoviReport extends JFrame {
    public void showReport(Connection conn, Drzava drzava) throws JRException {
        String reportSrcFile;
        String reportsDir = getClass().getResource("/").getFile();
        reportSrcFile = getClass().getResource("/gradovi.jrxml").getFile();

        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        // Fields for resources path
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("reportsDirPath", reportsDir);
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list.add(parameters);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(700, 500);
        this.setVisible(true);
    }

    public void showReport2 (Connection conn, Drzava drzava) throws JRException {
        String reportSrcFile;
        String reportsDir = getClass().getResource("/").getFile();
        reportSrcFile = getClass().getResource("/gradovi2.jrxml").getFile();
        HashMap<String, Object> parameters = new HashMap<>();
        String param = String.valueOf(drzava.getId());
        parameters.put("reportsDirPath", reportsDir);
        parameters.put("param",param);
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(700, 500);
        this.setVisible(true);
    }

    public void saveAs (String format,Connection connection) throws IOException, JRException {
        String reportSrcFile = getClass().getResource("/gradovi.jrxml").getFile();
        String reportsDir = getClass().getResource("/").getFile();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("reportsDirPath", reportsDir);

        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, connection);
        File file = new File(format);
        OutputStream output = new FileOutputStream(file);

        if (format.contains(".pdf"))
            JasperExportManager.exportReportToPdfStream(print, output);
        else if (format.contains(".docx")) {
            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
            exporter.exportReport();
        } else if (format.contains(".xslx")) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(true);
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        }
        output.close();



    }



}
