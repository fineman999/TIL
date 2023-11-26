package io.chan.springcoresecurity.config;

import io.chan.springcoresecurity.repository.AccessIpRepository;
import io.chan.springcoresecurity.repository.ResourcesRepository;
import io.chan.springcoresecurity.security.service.SecurityResourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AppConfig {

    @Bean
    public SecurityResourceService securityResourceService(
            ResourcesRepository resourcesRepository,
            AccessIpRepository accessIpRepository
    ) {
        return new SecurityResourceService(resourcesRepository, accessIpRepository);
    }
}
