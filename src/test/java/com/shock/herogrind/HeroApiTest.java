package com.shock.herogrind;

import io.karatelabs.junit6.Karate;
import org.junit.jupiter.api.DynamicNode;

class HeroApiTest {
    @Karate.Test
    Iterable<DynamicNode> heroApi() {
        return Karate.run(
                "classpath:karate/hero/get-hero.feature",
                "classpath:karate/hero/get-heroes.feature"
                );
    }
}
