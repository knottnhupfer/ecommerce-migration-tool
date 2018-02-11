package org.smooth.systems.ec.magento19.db.component;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
@DataJpaTest
@EnableAutoConfiguration
@EnableJpaRepositories("org.smooth.systems.ec.magento19.db")
@EntityScan("org.smooth.systems.ec.magento19.db")
public class ClientMagento18DatabaseConfiguration {
}
