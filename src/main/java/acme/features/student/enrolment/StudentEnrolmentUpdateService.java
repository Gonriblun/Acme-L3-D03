//
// package acme.features.student.enrolment;
//
// import java.util.Collection;
// import java.util.Date;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
//
// import acme.entities.course.Course;
// import acme.entities.enrolments.Enrolment;
// import acme.framework.components.jsp.SelectChoices;
// import acme.framework.components.models.Tuple;
// import acme.framework.helpers.MomentHelper;
// import acme.framework.services.AbstractService;
// import acme.roles.Student;
//
// @Service
// public class StudentEnrolmentUpdateService extends AbstractService<Student, Enrolment> {
//
// // Internal state ---------------------------------------------------------
//
// @Autowired
// protected StudentEnrolmentRepository repository;
//
// // AbstractService interface ----------------------------------------------
//
//
// @Override
// public void check() {
// boolean status;
//
// status = super.getRequest().hasData("id", int.class);
//
// super.getResponse().setChecked(status);
// }
//
// @Override
// public void authorise() {
// boolean status;
// int masterId;
// Enrolment enrolment;
// Student student;
//
// masterId = super.getRequest().getData("id", int.class);
// enrolment = this.repository.findEnrolmentById(masterId);
// student = enrolment == null ? null : enrolment.getStudent();
// status = enrolment != null && !enrolment.getFinalised() && super.getRequest().getPrincipal().hasRole(student);
//
// super.getResponse().setAuthorised(status);
// }
//
// @Override
// public void load() {
// Enrolment object;
// int id;
//
// id = super.getRequest().getData("id", int.class);
// object = this.repository.findEnrolmentById(id);
//
// super.getBuffer().setData(object);
// }
//
// @Override
// public void bind(final Enrolment object) {
// assert object != null;
//
// final int LOWER_NIBBLE_START = 12;
//
// int courseId;
// Course course;
// final String ccNumber;
// final String ccLowerNibble;
//
// courseId = super.getRequest().getData("course", int.class);
// course = this.repository.findCourseById(courseId);
//
// ccNumber = super.getRequest().getData("ccNumber", String.class);
//
// super.bind(object, "code", "motivation", "goals", "ccHolder");
// object.setCourse(course);
// if (ccNumber.length() == 16) {
// ccLowerNibble = ccNumber.substring(LOWER_NIBBLE_START);
// object.setCcLowerNibble(ccLowerNibble);
// }
// }
//
// @Override
// public void validate(final Enrolment object) {
// assert object != null;
//
// Enrolment existing;
// final String ccNumber;
// final Date expiryDate;
// final String cvc;
//
// if (!super.getBuffer().getErrors().hasErrors("code")) {
// existing = this.repository.findEnrolmentByCode(object.getCode());
// super.state(existing == null || existing.equals(object), "code", "student.enrolment.form.error.code-duplicated");
// }
//
// if (!super.getBuffer().getErrors().hasErrors("ccNumber")) {
// ccNumber = super.getRequest().getData("ccNumber", String.class);
// if (!ccNumber.equals("")) {
// final String ccNumberRegex = "\\d{16}";
// super.state(ccNumber.matches(ccNumberRegex), "ccNumber", "student.enrolment.form.error.wrong-cardNumber");
// }
// }
//
// if (!super.getBuffer().getErrors().hasErrors("expiryDate")) {
// expiryDate = super.getRequest().getData("expiryDate", Date.class);
// if (expiryDate != null)
// super.state(MomentHelper.isFuture(expiryDate), "expiryDate", "student.enrolment.form.error.card-expired");
// }
//
// if (!super.getBuffer().getErrors().hasErrors("cvc")) {
// cvc = super.getRequest().getData("cvc", String.class);
// if (!cvc.equals("")) {
// final String cvcRegex = "\\d{3}";
// super.state(cvc.matches(cvcRegex), "cvc", "student.enrolment.form.error.wrong-cvc");
// }
// }
// }
//
// @Override
// public void perform(final Enrolment object) {
// assert object != null;
//
// this.repository.save(object);
// }
//
// @Override
// public void unbind(final Enrolment object) {
// assert object != null;
//
// final int LOWER_NIBBLE_START = 12;
//
// Collection<Course> courses;
// SelectChoices choices;
// String ccNumber;
// final String ccLowerNibble;
// Tuple tuple;
//
// courses = this.repository.findAllCourses();
// choices = SelectChoices.from(courses, "title", object.getCourse());
//
// ccNumber = super.getRequest().getData("ccNumber", String.class);
//
// tuple = super.unbind(object, "code", "motivation", "goals", "finalised", "ccHolder");
// tuple.put("course", choices.getSelected().getKey());
// tuple.put("courses", choices);
//
// if (ccNumber.length() == 16) {
// ccLowerNibble = ccNumber.substring(LOWER_NIBBLE_START);
// tuple.put("ccLowerNibble", ccLowerNibble);
// }
//
// super.getResponse().setData(tuple);
// }
//
// }

package acme.features.student.enrolment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.enrolments.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentEnrolmentUpdateService extends AbstractService<Student, Enrolment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentEnrolmentRepository repository;

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
		int masterId;
		Enrolment enrolment;
		Student student;

		masterId = super.getRequest().getData("id", int.class);
		enrolment = this.repository.findOneEnrolmentById(masterId);
		student = enrolment == null ? null : enrolment.getStudent();
		status = enrolment != null && !enrolment.getFinalised() && super.getRequest().getPrincipal().hasRole(student);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Enrolment object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneEnrolmentById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;

		final int LOWER_NIBBLE_START = 12;

		int courseId;
		Course course;
		String creditCardNumber;
		String creditCardLowerNibble;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		creditCardNumber = super.getRequest().getData("creditCardNumber", String.class);

		super.bind(object, "code", "motivation", "goals", "creditCardHolder");
		object.setCourse(course);
		if (creditCardNumber.length() == 16) {
			creditCardLowerNibble = creditCardNumber.substring(LOWER_NIBBLE_START);
			object.setCcLowerNibble(creditCardLowerNibble);
		}
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Enrolment existing;

			existing = this.repository.findOneEnrolmentByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "student.enrolment.form.error.code-duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("creditCardNumber")) {
			String creditCardNumber;

			creditCardNumber = super.getRequest().getData("creditCardNumber", String.class);
			if (!creditCardNumber.equals(""))
				super.state(creditCardNumber.matches("\\d{16}"), "creditCardNumber", "student.enrolment.form.error.wrong-cardNumber");
		}

		if (!super.getBuffer().getErrors().hasErrors("expiryDate")) {
			Date expiryDate;

			expiryDate = super.getRequest().getData("expiryDate", Date.class);
			if (expiryDate != null)
				super.state(MomentHelper.isFuture(expiryDate), "expiryDate", "student.enrolment.form.error.card-expired");
		}

		if (!super.getBuffer().getErrors().hasErrors("cvc")) {
			String cvc;

			cvc = super.getRequest().getData("cvc", String.class);
			if (!cvc.equals(""))
				super.state(cvc.matches("\\d{3}"), "cvc", "student.enrolment.form.error.wrong-cvc");
		}
	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;

		final int LOWER_NIBBLE_START = 12;

		Collection<Course> courses;
		SelectChoices choices;
		String creditCardNumber;
		String creditCardLowerNibble;
		Tuple tuple;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		creditCardNumber = super.getRequest().getData("creditCardNumber", String.class);

		tuple = super.unbind(object, "code", "motivation", "goals", "draftMode", "creditCardHolder");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		if (creditCardNumber.length() == 16) {
			creditCardLowerNibble = creditCardNumber.substring(LOWER_NIBBLE_START);
			tuple.put("creditCardLowerNibble", creditCardLowerNibble);
		}

		super.getResponse().setData(tuple);
	}

}
