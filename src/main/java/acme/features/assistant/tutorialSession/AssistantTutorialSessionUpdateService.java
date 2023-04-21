
package acme.features.assistant.tutorialSession;

import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.sessions.TutorialSession;
import acme.entities.sessions.sessionType;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionUpdateService extends AbstractService<Assistant, TutorialSession> {

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
		boolean status;
		int sessionId;
		Tutorial tutorial;

		sessionId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findOneTutorialByTutorialSessionId(sessionId);
		status = tutorial != null && tutorial.isDraftMode() && super.getRequest().getPrincipal().hasRole(tutorial.getAssitant());

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
	public void bind(final TutorialSession object) {
		assert object != null;
		super.bind(object, "title", "abstract$", "sessionType", "startSession", "finishSession", "furtherInformation");
	}

	@Override
	public void validate(final TutorialSession object) {
		assert object != null;
		Date actualDate;
		final boolean validStartDate;
		final boolean minDuration;
		final boolean maxDuration;
		actualDate = MomentHelper.getCurrentMoment();

	}

	@Override
	public void perform(final TutorialSession object) {
		assert object != null;

		Tutorial tutorial;
		double totalHours;
		double sessionHours;
		double validFormatSessionHours;
		Duration sessionDuration;
		String formattedSessionHours;

		tutorial = object.getTutorial();
		totalHours = 0.;
		sessionDuration = MomentHelper.computeDuration(object.getStartSession(), object.getFinishSession());
		sessionHours = sessionDuration.getSeconds() / 3600.;
		formattedSessionHours = String.format("%.2f", sessionHours);
		validFormatSessionHours = Double.parseDouble(formattedSessionHours);

		totalHours = validFormatSessionHours + tutorial.getEstimatedTime();

		tutorial.setEstimatedTime(totalHours);

		this.repository.save(tutorial);
		this.repository.save(object);
	}

	@Override
	public void unbind(final TutorialSession object) {
		assert object != null;

		Tuple tuple;
		SelectChoices choices;
		Tutorial tutorial;

		choices = SelectChoices.from(sessionType.class, object.getSessionType());
		tutorial = object.getTutorial();

		tuple = super.unbind(object, "title", "abstract$", "sessionType", "startSession", "finishSession", "furtherInformation");
		tuple.put("masterId", tutorial.getId());
		tuple.put("draftMode", tutorial.isDraftMode());
		tuple.put("sessionType", choices);

		super.getResponse().setData(tuple);
	}

}
