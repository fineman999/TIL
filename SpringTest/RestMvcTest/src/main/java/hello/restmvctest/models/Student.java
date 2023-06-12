package hello.restmvctest.models;

import org.springframework.stereotype.Component;

@Component
public interface Student {

   public String studentInformation();

   public String getFullName();

}
