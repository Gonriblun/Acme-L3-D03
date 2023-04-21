
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialCreateService extends AbstractService<Assistant, Tutorial> {

	@Autowired
	protected AssistantTutorialRepository repositorio;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Tutorial tutorial;
		Assistant asistente;

		asistente = this.repositorio.findAssistantById(super.getRequest().getPrincipal().getActiveRoleId());
		tutorial = new Tutorial();
		tutorial.setDraftMode(true);
		tutorial.setAssitant(asistente);
		super.getBuffer().setData(tutorial);
	}

	@Override
	public void bind(final Tutorial object) {
		assert object != null;
		final Integer cursoId;
		Course curso;

		cursoId = super.getRequest().getData("course", int.class);
		curso = this.repositorio.findCourseById(cursoId);
		super.bind(object, "code", "title", "abstract$", "goals", "estimatedTime");
		object.setCourse(curso);
	}

	@Override
	public void validate(final Tutorial object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			final Tutorial duplica;
			duplica = this.repositorio.findTutorialByCode(object.getCode());
			super.state(duplica == null, "code", "Tutorial ya creado");
		}
	}

	@Override
	public void perform(final Tutorial object) {
		assert object != null;
		this.repositorio.save(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;

		Collection<Course> cursos;
		SelectChoices opciones;
		Tuple tupla;

		cursos = this.repositorio.findAllCourses();
		opciones = SelectChoices.from(cursos, "code", object.getCourse());

		tupla = super.unbind(object, "code", "title", "abstract$", "goals", "estimatedTime");
		tupla.put("course", opciones.getSelected().getKey());
		tupla.put("courses", opciones);

		super.getResponse().setData(tupla);

	}

}
