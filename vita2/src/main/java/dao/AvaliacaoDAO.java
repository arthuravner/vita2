package dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import model.Auth;
import model.Avaliacao;
import model.LinkAvaliacao;
import model.Turma;
import model.Usuario;
import model.filtro.FiltroAvaliacao;

@Stateless
@LocalBean
public class AvaliacaoDAO extends AbstractVitaDAO{

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Avaliacao> getAvaliacoes(final Auth auth, FiltroAvaliacao filtro) throws Exception {

		CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Avaliacao> query = builder.createQuery(Avaliacao.class);
		Root<Avaliacao> root = query.from(Avaliacao.class);
		Join<Avaliacao, LinkAvaliacao> joinLinkAvaliacao = root.join("link");
		Join<Avaliacao, Turma> joinTurma = joinLinkAvaliacao.join("turma");
		Join<Avaliacao, Usuario> joinProfessor = joinTurma.join("professor");
				
		Predicate where = null;
				
		if(!auth.isAdmin()) {
			where = this.and(where, builder, builder.equal(joinProfessor.get("id"), filtro.getProfessorId()));
		}

		if (filtro.getLinkId() != null) {
			where = this.and(where, builder, builder.equal(joinLinkAvaliacao.get("id"), filtro.getLinkId()));
		} 
		
		if (filtro.getTurmaId() != null) {
			where = this.and(where, builder, builder.equal(joinTurma.get("id"), filtro.getTurmaId()));
		} 
		
		if (StringUtils.isNoneEmpty(filtro.getTurmaNome())) {
			where = this.and(where, builder, builder.like(joinTurma.get("nome"), "%" + filtro.getTurmaNome() + "%"));
		} 		
		
		if (filtro.getTurmaAno() != null) {
			where = this.and(where, builder, builder.equal(joinTurma.get("ano"), filtro.getTurmaAno()));
		}

		if (filtro.getTurmaPeriodo() != null) {
			where = this.and(where, builder, builder.equal(joinTurma.get("periodo"), filtro.getTurmaPeriodo()));
		}		

		if (StringUtils.isNotEmpty(filtro.getDescLinkAvaliacao())) {
			where = this.and(where, builder, builder.like(joinLinkAvaliacao.get("descricao"), "%" + filtro.getDescLinkAvaliacao() + "%"));
		}

		if (where != null) {
			query.where(where);
			query.orderBy(builder.desc(root.get("id")));
			query.select(root);
			return this.getEntityManager().createQuery(query).setMaxResults(100).getResultList();
		} else {
			return null;
		}

	}
	
}
