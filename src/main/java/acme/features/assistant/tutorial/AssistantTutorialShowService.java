
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
public class AssistantTutorialShowService extends AbstractService<Assistant, Tutorial> {

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
		Tutorial tutorial;
		Integer id;
		id = super.getRequest().getData("id", int.class);
		tutorial = this.repositorio.findTutorialById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final Integer userId = principal.getAccountId();
		super.getResponse().setAuthorised(tutorial.getAssitant().getUserAccount().getId() == userId);
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
	public void unbind(final Tutorial object) {
		assert object != null;
		Tuple tupla;
		Collection<Course> cursos;
		SelectChoices opciones;
		tupla = super.unbind(object, "code", "title", "abstract$", "goals", "estimatedTime", "draftMode");
		cursos = this.repositorio.findAllCourses();
		opciones = SelectChoices.from(cursos, "code", object.getCourse());
		tupla.put("course", opciones.getSelected().getKey());
		tupla.put("courses", opciones);
		super.getResponse().setData(tupla);
	}

}
