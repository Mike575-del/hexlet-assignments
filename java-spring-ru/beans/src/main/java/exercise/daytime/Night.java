package exercise.daytime;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


public class Night implements Daytime {
    private String name = "night";

    public String getName() {
        return name;
    }

    // BEGIN
    @PostConstruct
    public void init(){
        System.out.println("The " + this.name + " was started!");
    }

    @PreDestroy
    public void cleanup(){
        System.out.println("The " + this.name + " is over!");
    }
    // END

}
