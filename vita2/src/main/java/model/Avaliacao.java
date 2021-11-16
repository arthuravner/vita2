package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.StringUtils;

import model.azure.RetornoAzure;

@Entity
public class Avaliacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "aluno")
	private String aluno;

	@Column(name = "nota")
	private Integer nota;

	@Column(name = "texto")
	private String texto;

	@ManyToOne
	private LinkAvaliacao link;

	@OneToOne(mappedBy = "avaliacao")
	private RetornoAzure retornoAzure;

	public String getDescAluno() {
		if(StringUtils.isNotEmpty(this.aluno)) {
			return this.aluno;
		}else {
			return "Anônimo";
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAluno() {
		return aluno;
	}

	public void setAluno(String aluno) {
		this.aluno = aluno;
	}

	public Integer getNota() {
		return nota;
	}

	public void setNota(Integer nota) {
		this.nota = nota;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LinkAvaliacao getLink() {
		return link;
	}

	public void setLink(LinkAvaliacao link) {
		this.link = link;
	}

	public RetornoAzure getRetornoAzure() {
		return retornoAzure;
	}

	public void setRetornoAzure(RetornoAzure retornoAzure) {
		this.retornoAzure = retornoAzure;
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
		Avaliacao other = (Avaliacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
