package dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import model.Usuario;
import model.filtro.FiltroUsuario;

@Stateless
@LocalBean
public class UsuarioDAO extends AbstractVitaDAO{

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario getUsuario(Usuario usuario) {	
		return usuario;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Usuario> getUsuarios(FiltroUsuario filtro) {	
		CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
		Root<Usuario> root = query.from(Usuario.class);
		
		Predicate where = builder.equal(root.get("ativo"), true);
		
		if(StringUtils.isNotEmpty(filtro.getLogin())) {
			where = builder.and(where, builder.equal(root.get("login"), filtro.getLogin()));
		}
		
		if(StringUtils.isNotEmpty(filtro.getEmail())) {
			where = builder.and(where, builder.equal(root.get("email"), filtro.getEmail()));
		}
		
		if(StringUtils.isNotEmpty(filtro.getNome())) {
			where = builder.and(where, builder.like(root.get("email"), "%" + filtro.getNome() + "%"));
		}
		
		if(filtro.getNivelAcesso() != null) {
			where = builder.and(where, builder.equal(root.get("niveAcesso"), filtro.getNivelAcesso()));
		}
		
		query.where(where);
		query.orderBy(builder.desc(root.get("id")));
		query.select(root);
				
		return getEntityManager().createQuery(query).setMaxResults(100).getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Usuario login(String login, String senha) throws Exception{
		CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
		Root<Usuario> root = query.from(Usuario.class);
		
		Predicate where = builder.and(builder.equal(root.get("login"), login), builder.equal(root.get("senha"), senha)); 
		where = builder.and(where, builder.equal(root.get("ativo"), true));
		query.where(where);
		query.select(root);
				
		return getEntityManager().createQuery(query).getResultList().stream().findFirst().orElse(null);
	}
}