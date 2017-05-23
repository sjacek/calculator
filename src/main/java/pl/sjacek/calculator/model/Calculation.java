package pl.sjacek.calculator.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    private String expression;
}
