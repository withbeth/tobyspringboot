package withbeth.me.helloboot.profiletest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("highschool")
@ContextConfiguration(classes = {KindergartenConfig.class, HighSchoolConfig.class})
class ProfileTest {

    @Autowired FoodProviderService foodProviderService;

    @DisplayName("컨텍스트 직접 생성하지 말고, 정해진 프로파일의 서비스를 이용가능해야 한다")
    @Test
    void useProfileWithOutManuallyCreatingAppContext() {
        System.out.println(foodProviderService.provideLunchSet());
    }

    @Disabled
    @DisplayName("정해진 프로파일의 서비스를 이용해야 한다")
    @Test
    void useProfileFoodProviderService() {
        GenericApplicationContext ac = new AnnotationConfigApplicationContext(KindergartenConfig.class, HighSchoolConfig.class);
        ac.getEnvironment().setDefaultProfiles("kindergarten");

        // Profile
        System.out.println(Arrays.toString(ac.getEnvironment().getActiveProfiles()));
        for (String beanDefinitionName : ac.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }

        FoodProviderService service = ac.getBean(FoodProviderService.class);
        System.out.println(service.getClass());
        System.out.println(service.provideLunchSet());
    }
}
