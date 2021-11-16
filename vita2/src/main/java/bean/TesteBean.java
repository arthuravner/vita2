package bean;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "testeBean")
@ViewScoped
public class TesteBean extends AbstractBean{
	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void postConstruct() {
		System.out.println("Bean iniciado");
	}
	
	public String getTextoTeste() {
		return "Texto teste";
	}
}
