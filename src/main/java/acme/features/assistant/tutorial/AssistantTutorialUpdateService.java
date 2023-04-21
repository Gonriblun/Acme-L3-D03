
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialUpdateService extends AbstractService<Assistant, Tutorial> {

	@Autowired
	protected AssistantTutorialRepository repositorio;


	@Override
	public void check() {
		boolean estado;
		estado = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(estado);
	}

	@Override
	public void authorise() {
		boolean estado;
		Tutorial tutorial;
		Principal principal;
		Integer tutorialId;

		tutorialId = super.getRequest().getData("id", int.class);
		tutorial = this.repositorio.findTutorialById(tutorialId);
		principal = super.getRequest().getPrincipal();
		estado = tutorial.getAssitant().getId() == principal.getActiveRoleId();
		super.getResponse().setAuthorised(estado);
	}

	@Override
	public void load() {
		Tutorial tutorial;
		Integer id;

		id = super.getRequest().getData("id", int.class);
		tutorial = this.repositorio.findTutorialById(id);

		super.getBuffer().setData(tutorial);
	}

	@Override
	public void bind(final Tutorial object) {
		assert object != null;

		int cursoId;
		Course curso;

		cursoId = super.getRequest().getData("course", int.class);
		curso = this.repositorio.findCourseById(cursoId);
		super.bind(object, "code", "title", "abstract$", "goal");
		object.setCourse(curso);
	}

	@Override
	public void validate(final Tutorial object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Tutorial duplica;
			duplica = this.repositorio.findTutorialByCode(object.getCode());
			super.state(duplica == null || duplica.equals(object), "code", "El tutorial ya existe");
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
		tupla = super.unbind(object, "code", "title", "abstract$", "goal");
		tupla.put("course", opciones.getSelected().getKey());
		tupla.put("courses", opciones);
		super.getResponse().setData(tupla);
	}

}
