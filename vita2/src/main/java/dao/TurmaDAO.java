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

import model.Turma;
import model.Usuario;
import model.filtro.FiltroTurma;

@Stateless
@LocalBean
public class TurmaDAO extends AbstractVitaDAO {

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Turma> getTurmas(FiltroTurma filtro) throws Exception {

		CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Turma> query = builder.createQuery(Turma.class);
		Root<Turma> root = query.from(Turma.class);
		
		Predicate where = builder.equal(root.get("professor"), filtro.getProfessorId());

		if (StringUtils.isNotEmpty(filtro.getNome())) {
			where = builder.and(where, builder.like(root.get("nome"), "%" + filtro.getNome() + "%"));
		}

		if (filtro.getAno() != null) {
			where = builder.and(where, builder.equal(root.get("ano"), filtro.getAno()));
		}

		if (filtro.getPeriodo() != null) {
			where = builder.and(where, builder.equal(root.get("periodo"), filtro.getPeriodo()));
		}

		query.where(where);
		query.orderBy(builder.desc(root.get("id")));
		query.select(root);

		return this.getEntityManager().createQuery(query).setMaxResults(100).getResultList();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void save(Turma turma, Usuario professor) throws Exception {

		if (professor == null) {
			throw new Exception("A turma deve ter um professor");
		}

		Usuario entityProfessor = this.em.find(Usuario.class, professor.getId());

		if (entityProfessor != null) {
			turma.setProfessor(entityProfessor);

			if (turma.getId() == null) {
				this.getEntityManager().persist(turma);
			} else {
				Turma entityTurma = this.em.find(Turma.class, turma.getId());
				if (entityTurma != null) {
					entityTurma.copy(turma);
					this.update(entityTurma);
				} else {
					throw new Exception("Turma não encontrada");
				}
			}
		} else {
			throw new Exception("Professor não encontrado");
		}

	}

}
