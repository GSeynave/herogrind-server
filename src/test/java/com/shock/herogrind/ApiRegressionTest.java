package com.shock.herogrind;

import io.karatelabs.junit6.Karate;
import org.junit.jupiter.api.DynamicNode;

class ApiRegressionTest {
    @Karate.Test
    Iterable<DynamicNode> heroApi() {
        return Karate.run(
                "classpath:karate/hero",
                "classpath:karate/party"
        );
    }
}
