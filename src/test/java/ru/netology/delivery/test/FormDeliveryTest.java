package ru.netology.delivery.test;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.withText;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;
import static com.codeborne.selenide.Selectors.byText;
import java.util.Locale;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.delivery.data.DataGenerator.*;


public class FormDeliveryTest {


    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1280x1024";
        Configuration.browser = "chrome";
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var daysToAddForFirstMeeting = 5;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 10;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);
        Faker faker = new Faker(new Locale("ru"));

        $("[data-test-id=city] input").setValue(DataGenerator.generateCity());
        $("[data-test-id=date] input").sendKeys(DataGenerator
                .generateDate(daysToAddForFirstMeeting));
        $("[data-test-id=name] input").setValue(DataGenerator.
                generateName());
        $("[data-test-id=phone] input").setValue(DataGenerator.generatePhone());
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $("[data-test-id=success-notification]")
                .shouldHave(text("Успешно! Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] [placeholder='Дата встречи']").sendKeys(DataGenerator
                .generateDate(daysToAddForSecondMeeting));
        $(withText("Запланировать")).click();
        $(withText("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible).click();
        $(byText("Перепланировать")).click();
        $("[data-test-id=success-notification]").shouldBe(visible)
                .shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate));

    }
}
