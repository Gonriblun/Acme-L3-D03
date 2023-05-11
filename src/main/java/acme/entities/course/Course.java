
package acme.entities.course;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.lecture.Lecture;
import acme.entities.lecture.LectureType;
import acme.framework.data.AbstractEntity;
import acme.roles.Lecturer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Course extends AbstractEntity {

	//	Serialisation identifier ----------------------------
	protected static final long	serialVersionUID	= 1L;

	//	Attributes ----------------------------------------------
	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "[A-Z]{1,3}[0-9]{3}")
	protected String			code;

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			courseAbstract;

	@PositiveOrZero
	protected Double			retailPrice;

	@URL
	protected String			link;

	@NotNull
	@ManyToOne(optional = false)
	protected Lecturer			lecturer;

	protected boolean			draftMode;


	public CourseType calculateCourseType(final Collection<Lecture> lectures) {
		Map<LectureType, Long> modeLectureType;
		Long handsOnLectures;
		Long theoricalLectures;

		modeLectureType = lectures.stream().map(Lecture::getLectureType).collect(Collectors.groupingBy(type -> type, Collectors.counting()));
		handsOnLectures = modeLectureType.get(LectureType.HANDS_ON);
		theoricalLectures = modeLectureType.get(LectureType.THEORICAL);

		if (modeLectureType.isEmpty() || modeLectureType == null || handsOnLectures == null || theoricalLectures == null)
			return null;
		if (handsOnLectures.equals(theoricalLectures))
			return CourseType.BALANCED;
		if (theoricalLectures > handsOnLectures)
			return CourseType.THEORY_COURSE;

		return CourseType.HANDS_ON_COURSE;
	}
}
