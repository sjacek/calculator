package pl.sjacek.calculator.model;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by jacek on 21.05.17.
 */
@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = { "id" })
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String expression;

    @NotNull
    private Date datetime;
}
