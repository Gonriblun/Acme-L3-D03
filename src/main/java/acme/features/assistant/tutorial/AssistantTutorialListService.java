
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialListService extends AbstractService<Assistant, Tutorial> {

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
		Collection<Tutorial> tutoriales;
		final Principal principal = super.getRequest().getPrincipal();
		final int userId = principal.getAccountId();
		tutoriales = this.repositorio.findTutorialsByAssistantId(userId);
		super.getBuffer().setData(tutoriales);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;

		Tuple tupla;
		tupla = super.unbind(object, "title", "abstract$", "goals");
		super.getResponse().setData(tupla);
	}

}
