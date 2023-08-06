package local.sigma_labs.app.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    private UUID Id;
    private String name;
    private String SubscriptionPlan;
}
