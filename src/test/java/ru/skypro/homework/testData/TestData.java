package ru.skypro.homework.testData;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDTO;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.dto.security.Login;
import ru.skypro.homework.dto.security.Register;
import ru.skypro.homework.dto.users.UpdateUser;
import ru.skypro.homework.entity.*;
import ru.skypro.homework.enums.Role;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class TestData {

    public static CreateOrUpdateAd randomTestDataCreateOrUpdateAd() {
        CreateOrUpdateAd adDTO = new CreateOrUpdateAd();
        Faker faker = new Faker();
        adDTO.setPrice(Math.toIntExact(faker.number().randomNumber()));
        adDTO.setTitle(faker.lorem().sentence());
        adDTO.setDescription(faker.lorem().paragraph());
        return adDTO;
    }

    public static Ad randomTestDataAd() {
        Ad ad = new Ad();
        Faker faker = new Faker();
        ad.setPk(Math.toIntExact(faker.number().randomNumber()));
        ad.setImage(generateRandomImage());
        ad.setTitle(faker.lorem().sentence());
        return ad;
    }

    public static ExtendedAd randomTestDataExtendedAd() {
        ExtendedAd extendedAd = new ExtendedAd();
        Faker faker = new Faker();
        extendedAd.setPk(Math.toIntExact(faker.number().randomNumber()));
        extendedAd.setImage(faker.file().fileName());
        extendedAd.setTitle(faker.lorem().sentence());
        extendedAd.setDescription(faker.lorem().sentence());
        extendedAd.setEmail(faker.internet().emailAddress());
        extendedAd.setPhone(faker.phoneNumber().phoneNumber());
        extendedAd.setPrice(faker.number().randomDigit());
        extendedAd.setAuthorFirstName(faker.name().firstName());
        extendedAd.setAuthorLastName(faker.name().lastName());
        return extendedAd;
    }
    public static AdDTO randomTestDataAdDTO() {
        AdDTO adDTO = new AdDTO();
        Faker faker = new Faker();
        adDTO.setPk(Math.toIntExact(faker.number().randomNumber()));
        adDTO.setImage(faker.file().fileName());
        adDTO.setTitle(faker.lorem().sentence());
        adDTO.setPrice(faker.number().randomDigit());
        return adDTO;
    }

    public static Image generateRandomImage() {
        Faker faker = new Faker();
        String imageUrl = faker.avatar().image();
        Image image = new Image();
        image.setPath(imageUrl);
        image.setId((int) faker.number().randomNumber());
        return image;
    }

    public static User randomTestDataUser() {
        Faker faker = new Faker();
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setRole(Role.USER);
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setPhone(faker.phoneNumber().cellPhone());
        user.setComments(new ArrayList<>());
        user.setAds(new ArrayList<>());
        return user;
    }


    public static UpdateUser randomTestDataUpdateUser() {
        Faker faker = new Faker();
        UpdateUser user = new UpdateUser();
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setPhone(faker.phoneNumber().cellPhone());
        return user;
    }
    public static Login randomTestLogin() {
        Faker faker = new Faker();
        Login login = new Login();
        login.setPassword(faker.internet().password());
        login.setUsername(faker.internet().emailAddress());
        return login;
    }

    public static Register randomTestRegister() {
        Faker faker = new Faker();
        Register register = new Register();
        register.setPassword(faker.internet().password());
        register.setUsername(faker.internet().emailAddress());
        register.setFirstName(faker.name().firstName());
        register.setLastName(faker.name().lastName());
        register.setPhone(faker.phoneNumber().phoneNumber());
        register.setRole(Role.USER);
        return register;
    }


    public static MultipartFile createMockEmptyMultipartFile() {
        return Mockito.mock(MultipartFile.class);
    }

    public static Comment generateRandomComment() {
        Faker faker = new Faker();
        Comment comment = new Comment();
        comment.setText(faker.lorem().sentence());
        comment.setPk((int) faker.random().nextLong());
        return comment;
    }

    public static CreateOrUpdateComment generateRandomCommentDTO() {
        Faker faker = new Faker();
        CreateOrUpdateComment comment = new CreateOrUpdateComment();
        comment.setText(faker.lorem().sentence());
        return comment;
    }

    public static UserAuthentication generateRandomUserAuthentication() {
        Faker faker = new Faker();
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setPasswordHash(faker.internet().password());
        return userAuthentication;
    }

}