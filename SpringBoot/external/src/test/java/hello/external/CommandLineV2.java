package hello.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.List;

@Slf4j
public class CommandLineV2 {
    public static void main(String[] args) {
        for (String arg : args) {
            log.info("arg {}", arg);
        }

        ApplicationArguments appArgs = new DefaultApplicationArguments(args);
        log.info("SourceArgs={}", List.of(appArgs.getSourceArgs()));
        log.info("NonOptionsArgs={}", appArgs.getNonOptionArgs());
        log.info("OptionsNames={}", appArgs.getOptionNames());

        for (String optionName : appArgs.getOptionNames()) {
            log.info("option arg {}={}", optionName, appArgs.getOptionValues(optionName));
        }

        List<String> url = appArgs.getOptionValues("url");
        List<String> username = appArgs.getOptionValues("username");
        List<String> password = appArgs.getOptionValues("password");
        log.info("url={}", url);
        log.info("username={}", username);
        log.info("password={}", password);

    }
}
