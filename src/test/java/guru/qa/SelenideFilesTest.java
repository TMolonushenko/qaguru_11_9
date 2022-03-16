package guru.qa;

import com.codeborne.pdftest.assertj.Assertions;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.nio.charset.StandardCharsets.UTF_8;

public class SelenideFilesTest {
    @Test
    void selenideDownloadTest() throws Exception {
        open("https://github.com/junit-team/junit5/blob/main/README.md");

        // Assertions.assertThrows(FileNotFoundException.class, () -> $("#raw-url").download());


        //Стандартный Assert для проверки исключений вместо try/cath
        //   Assertions.assertThrows(FileNotFoundException.class, () -> $("#raw-url").download());
        /* {
            File downloaddedFile = $("#raw-url").download();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/


// если возникло исключение то значит либо вы неправильно написали тест, либо не работает приложение. вы должны сразу об этом узнать поэтому try/cath не подходит
        // об этом исключении может подумать сам junit для этого надо продублировать чать сигнатуры  throws FileNotFoundException к себе в тест
        File downloaddedFile = $("#raw-url").download();
        try (InputStream is = new FileInputStream(downloaddedFile)) {
            //  is.readAllBytes();
            Assertions.assertThat(new String(is.readAllBytes(), UTF_8)).contains("This repository is the home of the next generation of JUnit");
        }

// этот код раблтает так  же как блок try
        //String readString = Files.readString(downloaddedFile.toPath(), UTF_8);
    }

    @Test
    void uploadSelenideTest() {
        Selenide.open("https://the-internet.herokuapp.com/upload");
        $("input[type='file']").
       //         uploadFile(new File("C:\\Users\\user\\IdeaProjects\\qaguru_11_9\\src\\test\\resources\\files\\1.txt"));//плохая практика
                uploadFromClasspath ("files\\1.txt");
        $("#file-submit").click();
        $("div.example").shouldHave(text("File Uploaded!"));
        Selenide.$("#uploaded-files").shouldHave(text("1.txt"));

       /* @Test
        void uploadSelenideTest() {
            Selenide.open("https://the-internet.herokuapp.com/upload");
            Selenide.$("input[type='file']")
//                .uploadFile(new File("/Users/dmitriituchs/IdeaProjects/qa_guru/qa_guru_11_9_files/src/test/resources/files/1.txt")); // bad practice!
                    .uploadFromClasspath("files/1.txt");
            Selenide.$("#file-submit").click();
            Selenide.$("div.example").shouldHave(Condition.text("File Uploaded!"));
            Selenide.$("#uploaded-files").shouldHave(Condition.text("1.txt"));*/
    }
}
