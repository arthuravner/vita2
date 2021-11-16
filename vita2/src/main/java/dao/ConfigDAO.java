package dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import model.Config;

@Stateless
@LocalBean
public class ConfigDAO extends AbstractVitaDAO {

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Config getConfig(String chave) throws Exception {

		CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Config> query = builder.createQuery(Config.class);
		Root<Config> root = query.from(Config.class);

		Predicate where = builder.equal(root.get("chave"), chave);

		query.where(where);
		query.select(root);

		return this.getEntityManager().createQuery(query).getResultStream().findFirst().orElse(null);
	}

}
