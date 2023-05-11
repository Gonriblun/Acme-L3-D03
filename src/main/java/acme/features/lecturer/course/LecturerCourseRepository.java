
package acme.features.lecturer.course;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.course.Course;
import acme.entities.course.CourseLecture;
import acme.entities.lecture.Lecture;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Lecturer;

@Repository
public interface LecturerCourseRepository extends AbstractRepository {

	@Query("select c from Course c where c.lecturer.userAccount.id = :id")
	Collection<Course> findCoursesByLecturerId(int id);

	@Query("select c from Course c where c.id = :id")
	Course findCourseById(int id);

	@Query("select cl from CourseLecture cl where cl.course = :course")
	Collection<CourseLecture> findCourseLecturesByCourse(Course course);

	@Query("select l from Lecturer l where l.id = :id")
	Lecturer findOneLecturerById(int id);

	@Query("select l from Lecture l inner join CourseLecture cl on l = cl.lecture inner join Course c on cl.course = c where c.id = :id")
	Collection<Lecture> findLecturesByCourse(int id);

	@Query("select count(cl) > 0 from CourseLecture cl where cl.course.id = :courseId and cl.lecture.draftMode = true")
	boolean someLectureNotPublishedByCourseId(int courseId);

}
