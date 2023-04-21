
package acme.features.assistant.tutorialSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.sessions.TutorialSession;
import acme.entities.sessions.sessionType;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionShowService extends AbstractService<Assistant, TutorialSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		final boolean status;
		int masterId;
		Tutorial tutorial;
		TutorialSession tutorialSession;
		Assistant assistant;

		masterId = super.getRequest().getData("id", int.class);
		tutorialSession = this.repository.findOneTutorialSessionById(masterId);
		tutorial = tutorialSession == null ? null : tutorialSession.getTutorial();
		assistant = tutorial == null ? null : tutorial.getAssitant();
		status = tutorial != null && tutorialSession != null && super.getRequest().getPrincipal().hasRole(assistant) && //
			tutorial.getAssitant().getId() == super.getRequest().getPrincipal().getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TutorialSession object;
		int tutorialSessionId;

		tutorialSessionId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTutorialSessionById(tutorialSessionId);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final TutorialSession object) {
		assert object != null;

		Tuple tuple;
		SelectChoices choices;
		Tutorial tutorial;
		boolean draftMode;

		tutorial = object.getTutorial();
		draftMode = tutorial.isDraftMode();
		choices = SelectChoices.from(sessionType.class, object.getSessionType());
		tuple = super.unbind(object, "title", "abstract$", "sessionType", "startSession", "finishSession", "furtherInformation");
		tuple.put("masterId", super.getRequest().getData("id", int.class));
		tuple.put("sessionType", choices);
		tuple.put("tutorial", tutorial);
		tuple.put("draftMode", draftMode);
		super.getResponse().setData(tuple);
	}

}
