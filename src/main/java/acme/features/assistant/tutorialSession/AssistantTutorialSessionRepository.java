
package acme.features.assistant.tutorialSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.sessions.TutorialSession;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AssistantTutorialSessionRepository extends AbstractRepository {

	@Query("select t from Tutorial t where t.id = :id")
	Tutorial findOneTutorialById(int id);

	@Query("select ts from TutorialSession ts where ts.tutorial.id = :tutorialId")
	Collection<TutorialSession> findManySessionsByTutorialId(int tutorialId);

	@Query("select ts from TutorialSession ts where ts.id = :id")
	TutorialSession findOneTutorialSessionById(int id);

	@Query("select a.tutorial from TutorialSession a where a.id = :tutorialSessionId")
	Tutorial findOneTutorialByTutorialSessionId(int tutorialSessionId);

}
