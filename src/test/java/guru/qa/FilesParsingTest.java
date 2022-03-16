package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import guru.qa.domain.Student;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class FilesParsingTest {
    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void parseTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File pdfDownload = $(byText("PDF download")).download();
        PDF pdf = new PDF(pdfDownload);
        assertThat(pdf.author).contains("Marc Philipp");
    }

    @Test
    void parseXlsTest() throws Exception {
        open("http://romashka2008.ru/price");
        File xlsDownload = $(".site-main__inner a[href*='prajs_ot']").download();
        XLS xls = new XLS(xlsDownload);
        assertThat(xls.excel
                .getSheetAt(0) //первая таблица
                .getRow(11)//12 строчка
                .getCell(1)//2 столбец
                .getStringCellValue().contains("Сахалинская обл. Южно-Сахалинск")); //достаем значение и проверяем по тексту
    }

    @Test
    void parseCsvTest() throws Exception {
        //ClassLoader classLoader = getClass().getClassLoader();

        //если надо вызвать внутри статического метода, например BeforeAll используем это
        // ClassLoader classLoader = FilesParsingTest.class().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("files/Machine_readable_file_bdc_sf_2021_q4.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            List<String[]> content = reader.readAll();
            assertThat(content.get(0)).contains("Series_reference", "Period", "Data_value", "Suppressed", "STATUS", "UNITS", "Magnitude", "Subject", "Group", "Series_title_1", "Series_title_2", "Series_title_3", "Series_title_4", "Series_title_5");
        }
    }

    @Test
    void paerseZipTest() throws Exception {
        try (InputStream is = classLoader.getResourceAsStream("files/business-financial-data-december-2021-quarter-csv.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                assertThat(entry.getName()).isEqualTo("Machine_readable_file_bdc_sf_2021_q4.csv");
            }

        }

    }

    @Test
    void jsonCommonTest() throws Exception {

        Gson gson = new Gson();
        try (InputStream is = classLoader.getResourceAsStream("files/simple.json")) {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            assertThat(jsonObject.get("name").getAsString()).isEqualTo("Tatyana");
            assertThat(jsonObject.get("address").getAsJsonObject().get("street").getAsString()).isEqualTo("Oktyabrskaya");
        }
    }

    @Test
    void jsonTypeTest() throws Exception {

        Gson gson = new Gson();
        try (InputStream is = classLoader.getResourceAsStream("files/simple.json")) {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Student jsonObject = gson.fromJson(json, Student.class);
            assertThat(jsonObject.name).isEqualTo("Tatyana");
            assertThat(jsonObject.address.street).isEqualTo("Oktyabrskaya");
        }
    }
}