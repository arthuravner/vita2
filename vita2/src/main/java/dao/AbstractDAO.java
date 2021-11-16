package dao;

import javax.persistence.EntityManager;

public abstract class AbstractDAO {
	public abstract EntityManager getEntityManager();
}
