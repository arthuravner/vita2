package bean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import jsf.JSFUtil;
import model.Usuario;

@SessionScoped
@Named(value = "mainBean")
public class MainBean extends AbstractBean {
	private static final long serialVersionUID = 1L;

	public Usuario getUsuario() {
		if (this.getAuth() != null) {
			return this.getAuth().getUsuario();
		} else {
			JSFUtil.mensagemErro("messages", "Usuario não autenticado.", null);
			return null;
		}
	}

	public boolean isAdmin() {
		return this.getAuth().isAdmin();
	}
	
}
