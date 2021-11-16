package service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import dao.UsuarioDAO;
import model.Usuario;
import model.filtro.FiltroUsuario;

@Stateless
public class UsuarioService {

	@EJB
	private UsuarioDAO dao;
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Usuario login(String login, String senha) throws Exception{
		return this.dao.login(login, senha);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Usuario> getUsuarios(FiltroUsuario filtro) throws Exception{
		return this.dao.getUsuarios(filtro);
	}
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Usuario usuario) throws Exception{
		this.dao.delete(usuario);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Usuario save(Usuario usuario) throws Exception{
		Usuario entity = null;
				
		if(usuario.getId() != null) {
			entity = this.dao.getById(Usuario.class, usuario.getId());			
		}
		
		if(entity == null) {
			entity = this.dao.save(usuario);
		}else {
			entity.copy(usuario);
			this.dao.save(entity);
		}
		
		return entity;
	}
	
}
