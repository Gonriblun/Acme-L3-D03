/*
 * EmployerJobPublishService.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.lecturer.lecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.lecture.Lecture;
import acme.entities.lecture.LectureType;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLecturePublishService extends AbstractService<Lecturer, Lecture> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Lecture object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findLectureById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Lecture object) {
		assert object != null;

		super.bind(object, "title", "abstractt", "estimatedLearningTime", "body", "lectureType", "furtherInformation");
	}

	@Override
	public void validate(final Lecture object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("draftMode")) {
			final boolean draftMode = object.isDraftMode();
			super.state(draftMode, "draftMode", "ya está publicada");
		}
	}

	@Override
	public void perform(final Lecture object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		Tuple tuple;

		tuple = super.unbind(object, "title", "abstractt", "estimatedLearningTime", "body", "draftMode", "lectureType", "furtherInformation");

		final SelectChoices choices = SelectChoices.from(LectureType.class, object.getLectureType());
		tuple.put("lectureType", choices.getSelected().getKey());
		tuple.put("lecturesType", choices);
		super.getResponse().setData(tuple);
	}

}
