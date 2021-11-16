package model;

import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import util.VitaUtil;

@Entity(name = "link_avaliacao")
public class LinkAvaliacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "descricao")
	private String descricao;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_aula")
	private Date dataAula;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_criacao")
	private Date dataCriacao;

	@Column(name = "hashlink")
	private String hashlink;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_validadee")
	private Date dataValidadeLink;

	@Column(name = "com_identificacao")
	private boolean comIdentificacao;

	@OneToMany(mappedBy = "link")
	private List<Avaliacao> avaliacoes;

	@ManyToOne
	private Turma turma;

	public LinkAvaliacao() {
		Calendar calendar = Calendar.getInstance();
		this.dataCriacao = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		this.dataValidadeLink = calendar.getTime();
	}

	public String getUrl(String urlServer) throws Exception {
		if (StringUtils.isNotEmpty(this.hashlink)) {
			return urlServer + "/vita2/avaliacao.xhtml" + "?h=" + this.hashlink;
		} else {
			throw new Exception("Link indisponível");
		}
	}

	public String getLinkRelatorio() {
		return "satisfacao.xhtml?faces-redirect=true&x=" + this.id;
	}

	public void geraHashlink() throws Exception {

		if (StringUtils.isEmpty(this.hashlink)) {
			Date now = Calendar.getInstance().getTime();
			Integer idProfessor = this.turma.getProfessor().getId();

			String nowString = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS").format(now);
			String idProfString = String.valueOf(idProfessor);

			this.hashlink = URLEncoder.encode(VitaUtil.convertStringToMd5(nowString.concat(idProfString)),
					StandardCharsets.UTF_8.toString());

			return;
		}
	}

	public void copy(LinkAvaliacao other) {
		this.descricao = other.descricao;
		this.dataValidadeLink = other.dataValidadeLink;
		this.dataAula = other.dataAula;
	}

	public String getDescricaoCompleta() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String desc = sdf.format(dataAula);
		if (StringUtils.isNotEmpty(this.descricao)) {
			desc += "-" + this.descricao;
		}
		return desc;
	}

	public Integer getCountAvaliacoes() {
		return CollectionUtils.isEmpty(avaliacoes) ? 0 : avaliacoes.size();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHashlink() {
		return hashlink;
	}

	public void setHashlink(String hashlink) {
		this.hashlink = hashlink;
	}

	public Date getDataValidadeLink() {
		return dataValidadeLink;
	}

	public void setDataValidadeLink(Date dataValidadeLink) {
		this.dataValidadeLink = dataValidadeLink;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public boolean isComIdentificacao() {
		return comIdentificacao;
	}

	public void setComIdentificacao(boolean comIdentificacao) {
		this.comIdentificacao = comIdentificacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Avaliacao> getAvaliacoes() {
		return avaliacoes;
	}

	public void setAvaliacoes(List<Avaliacao> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}

	public Date getDataAula() {
		return dataAula;
	}

	public void setDataAula(Date dataAula) {
		this.dataAula = dataAula;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkAvaliacao other = (LinkAvaliacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
