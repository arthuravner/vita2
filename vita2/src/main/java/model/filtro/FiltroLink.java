package model.filtro;

import java.util.Date;

public class FiltroLink {

	private String descricao;

	private boolean identificacao;

	private Integer turmaId;

	private String hash;

	private Date dataValidade;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(boolean identificacao) {
		this.identificacao = identificacao;
	}

	public Integer getTurmaId() {
		return turmaId;
	}

	public void setTurmaId(Integer turmaId) {
		this.turmaId = turmaId;
	}

	public Date getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(Date dataValidade) {
		this.dataValidade = dataValidade;
	}

}
