
package acme.entities.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.entities.course.Course;
import acme.framework.data.AbstractEntity;
import acme.roles.Auditor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Audit extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	@ManyToOne
	protected Auditor			auditor;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Course			course;

	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "[A-Z]{1,3}[0-9][0-9]{3}")
	protected String			code;

	@NotBlank
	@Length(max = 100)
	protected String			conclusion;

	@NotBlank
	@Length(max = 100)
	protected String			strongPoints;

	@NotBlank
	@Length(max = 100)
	protected String			weakPoints;

	protected boolean			published;

	/*
	 * public Mark calculateMark() {
	 * 
	 * final Map<Mark, Integer> markCounts = new HashMap<>();
	 * for (final AuditingRecord record : this.records) {
	 * 
	 * final Mark mark = record.getMark();
	 * if (markCounts.containsKey(mark))
	 * markCounts.put(mark, markCounts.get(mark) + 1);
	 * else
	 * markCounts.put(mark, 1);
	 * 
	 * }
	 * Mark mode = Mark.F_MINUS;
	 * 
	 * int maxCount = 0;
	 * for (final Map.Entry<Mark, Integer> entry : markCounts.entrySet())
	 * if (entry.getValue() > maxCount) {
	 * maxCount = entry.getValue();
	 * mode = entry.getKey();
	 * }
	 * 
	 * return mode;
	 * }
	 */
}
