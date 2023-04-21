
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.course.Course;
import acme.entities.sessions.TutorialSession;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantTutorialRepository extends AbstractRepository {

	@Query("SELECT tutorial FROM Tutorial tutorial WHERE tutorial.assitant.userAccount.id = :id")
	Collection<Tutorial> findTutorialsByAssistantId(Integer id);

	@Query("SELECT course FROM Course course")
	Collection<Tutorial> findAllTutorials();

	@Query("SELECT tutorial FROM Tutorial tutorial WHERE tutorial.id = :id")
	Tutorial findTutorialById(Integer id);

	@Query("SELECT session FROM TutorialSession session WHERE session.tutorial = :tutorial")
	Collection<TutorialSession> findSessionsByTutorial(Tutorial tutorial);

	@Query("SELECT course FROM Course course")
	Collection<Course> findAllCourses();

	@Query("SELECT assistant FROM Assistant assistant WHERE assistant.id = :id")
	Assistant findAssistantById(Integer id);

	@Query("SELECT course FROM Course course WHERE course.id = :id")
	Course findCourseById(Integer id);

	@Query("SELECT tutorial FROM Tutorial tutorial WHERE tutorial.code = :code")
	Tutorial findTutorialByCode(String code);

}
