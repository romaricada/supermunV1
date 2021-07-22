package bf.gov.anptic.fileshandler;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HandlerConstant {
    private static final Logger log = LoggerFactory.getLogger(HandlerConstant.class);
    public final static String ETOILES = "nombre d'étoiles";
    public final static String POINTS = "points";
    public final static String GLOBAL = "globale";


    public static void buildResponse(XSSFWorkbook workbook, HttpServletResponse response, File file) {
        try (OutputStream os = new FileOutputStream(file)) {
            workbook.write(os);
            FileSystemResource fsr = new FileSystemResource(file);
            response.setContentType("application/vnd.ms.excel; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            response.setHeader("filename", file.getName());
            StreamUtils.copy(fsr.getInputStream(), response.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void buildResponseCsv(HttpServletResponse response, File file) {
            FileSystemResource fsr = new FileSystemResource(file);
            response.setContentType("text/csv; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            response.setHeader("filename", file.getName());
        try {
            StreamUtils.copy(fsr.getInputStream(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
