package withbeth.me.helloboot.profiletest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import withbeth.me.helloboot.profiletest.kindergarten.FoodProviderServiceImpl;

@Profile("kindergarten")
@Configuration
public class KindergartenConfig {
    @Bean
    public FoodProviderService foodProviderService() {
        return new FoodProviderServiceImpl();
    }
}
