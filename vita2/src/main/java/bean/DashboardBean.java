package bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import jsf.JSFUtil;
import model.Turma;
import model.filtro.FiltroTurma;
import model.vo.DashboardVO;
import service.LinkAvaliacaoService;
import service.TurmaService;

@Named(value = "dashboardBean")
@ViewScoped
public class DashboardBean extends AbstractBean {
	private static final long serialVersionUID = 1L;

	private DashboardVO vo;

	private List<Turma> turmas;

	private Integer idTurmaSelecionada;

	private Turma turmaSelecionada;

	@EJB
	private TurmaService service;

	@EJB
	private LinkAvaliacaoService linkAvaliacaoService;

	@PostConstruct
	public void postContruct() {
		try {
			FiltroTurma filtro = new FiltroTurma();
			filtro.setProfessorId(this.getAuth().getUsuario().getId());
			this.turmas = this.service.getTurmas(filtro);
			this.vo = new DashboardVO(null);
		} catch (Exception e) {
			JSFUtil.mensagemErro(null, e.getMessage(), null);
		}
	}

	public void aplicarFiltro() {
		try {
			if (turmaSelecionada == null) {
				JSFUtil.mensagemErro(null, "Selecione uma turma", null);
				return;
			}

			if (this.vo == null) {
				this.vo = new DashboardVO(this.turmaSelecionada);
			} else {
				this.vo.setTurma(turmaSelecionada);
			}
		} catch (Exception e) {
			JSFUtil.mensagemErro(null, e.getMessage(), null);
		}
	}

	public DashboardVO getVo() {
		return vo;
	}

	public Turma getTurmaSelecionada() {
		return turmaSelecionada;
	}

	public void setTurmaSelecionada(Turma turmaSelecionada) {
		this.turmaSelecionada = turmaSelecionada;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public Integer getIdTurmaSelecionada() {
		return idTurmaSelecionada;
	}

	public void setIdTurmaSelecionada(Integer idTurmaSelecionada) {
		try {
			this.idTurmaSelecionada = idTurmaSelecionada;
			if(idTurmaSelecionada == null) {
				return;
			}
			this.turmaSelecionada = this.service.getTurmaById(idTurmaSelecionada);			
		} catch (Exception e) {
			JSFUtil.mensagemErro(null, e.getMessage(), null);
		}
	}

}
