package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "turma")
public class Turma implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "ano")
	private Integer ano;

	@Column(name = "periodo")
	private Integer periodo;

	@ManyToOne
	@JoinColumn(name = "professor_id", referencedColumnName = "id")
	private Usuario professor;

	@OneToMany(mappedBy = "turma", cascade = CascadeType.ALL)
	private List<LinkAvaliacao> links;

	public void copy(Turma other) {
		this.nome = other.nome;
		this.ano = other.ano;
		this.periodo = other.periodo;
		this.professor = other.professor;
		this.links = other.links;
	}
	
	public String toString() {
		return this.nome + " / " + this.ano + "." + this.periodo; 
	}
	
	public String getAnoPeriodo() {
		return this.ano + "." + this.periodo;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Usuario getProfessor() {
		return professor;
	}

	public void setProfessor(Usuario professor) {
		this.professor = professor;
	}

	public List<LinkAvaliacao> getLinks() {
		return links;
	}

	public void setLinks(List<LinkAvaliacao> links) {
		this.links = links;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
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
		Turma other = (Turma) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}