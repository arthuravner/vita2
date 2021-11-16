package model;

import model.Usuario.NivelAcesso;

public class Auth {

	private Usuario usuario;

	public Auth() {
	}
	
	public boolean isAdmin() {
		return this.usuario.getNivelDeAcesso().getValue().equals(NivelAcesso.ADMIN.getValue());
	}

	public Auth(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}		

}
