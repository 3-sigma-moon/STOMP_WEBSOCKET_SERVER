package local.sigma_labs.app.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Message {
    private UUID Id;
    private String Body;
    private User user;
    private Date sentAt;
}
