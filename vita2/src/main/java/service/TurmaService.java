package service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.collections4.CollectionUtils;

import dao.TurmaDAO;
import model.LinkAvaliacao;
import model.Turma;
import model.Usuario;
import model.filtro.FiltroTurma;

@Stateless
public class TurmaService {

	@EJB
	private TurmaDAO dao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Turma getTurmaById(Integer turmaId) throws Exception {
		Turma turma = dao.getById(Turma.class, turmaId);
		CollectionUtils.sizeIsEmpty(turma.getLinks());
		for (LinkAvaliacao link : turma.getLinks()) {
			CollectionUtils.sizeIsEmpty(link.getAvaliacoes());			
		}
		return turma;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Turma> getTurmas(FiltroTurma filtro) throws Exception {
		return dao.getTurmas(filtro);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void save(Turma turma, Usuario professor) throws Exception {
		this.dao.save(turma, professor);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(Turma turma) throws Exception {
		Turma entidade = this.dao.getEntityManager().find(Turma.class, turma.getId());
		if (entidade != null) {
			this.dao.getEntityManager().remove(entidade);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveLink(LinkAvaliacao link) throws Exception {
		if (link.getTurma() == null) {
			throw new Exception("Turma null");
		}

		Turma entidadeTurma = this.dao.getById(Turma.class, link.getTurma().getId());

		if (entidadeTurma != null) {
			link.setTurma(entidadeTurma);

			if (link.getId() == null) {
				this.dao.save(link);
			} else {
				LinkAvaliacao entity = this.dao.getById(LinkAvaliacao.class, link.getId());
				if (entity != null) {
					entity.copy(link);
					this.dao.update(entity);
				} else {
					throw new Exception("Entidade link inexistente");
				}
			}
		} else {
			throw new Exception("Professor não encontrado");
		}
	}
}
