package withbeth.me.helloboot.profiletest.highschool;

import withbeth.me.helloboot.profiletest.Food;
import withbeth.me.helloboot.profiletest.FoodProviderService;

import java.util.List;

public class FoodProviderServiceImpl implements FoodProviderService {
    @Override
    public List<Food> provideLunchSet() {
        return List.of(
            new Food("Coke"),
            new Food("Hamburger"),
            new Food("Fried Chicken")
        );
    }
}
