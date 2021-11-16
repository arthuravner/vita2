package dao;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

public abstract class AbstractVitaDAO extends AbstractDAO {

	protected EntityManager em;

	@PersistenceContext(unitName = "primary")
	public void setEntityManager(final EntityManager em) {
		this.em = em;
	}

	@Override
	public EntityManager getEntityManager() {
		return this.em;
	}
	
	public Predicate and(Predicate where, CriteriaBuilder builder, Predicate and) {
		if(where == null) {
			return and;
		}else {
			return builder.and(where, and);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public <T> T save(final T entity) throws Exception {
		try {
			this.getEntityManager().persist(entity);
			return entity;
		} catch (final Exception ex) {
			throw new Exception(ex);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public <T> T update(final T entity) throws Exception {
		try {
			return this.getEntityManager().merge(entity);
		} catch (final Exception ex) {
			throw new Exception(ex);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public <T> void delete(final T entity) throws Exception {
		try {
			T delete = entity;
			if (!this.getEntityManager().contains(entity)) {
				delete = this.getEntityManager().merge(entity);
			}
			this.getEntityManager().remove(delete);
		} catch (final Exception ex) {
			throw new Exception(ex);
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T> T getById(final Class<T> clazz, final Object id) throws Exception {
		try {
			return this.getEntityManager().find(clazz, id);
		} catch (final Exception ex) {
			throw new Exception(ex);
		}
	}

}