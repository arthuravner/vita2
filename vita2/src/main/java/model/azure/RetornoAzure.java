package model.azure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.tuple.Pair;

import model.Avaliacao;

@Entity(name = "retorno_azure")
public class RetornoAzure {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "sentiment")
	private String sentiment;

	@Column(name = "positive_score")
	private Double positiveScore;

	@Column(name = "neutral_score")
	private Double neutralScore;

	@Column(name = "negative_score")
	private Double negativeScore;

	@Column(name = "score_normalizado")
	private Integer scoreNormalizado;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "avaliacao", referencedColumnName = "id")
	private Avaliacao avaliacao;

	public void calculaScoreNormalizado() {

		switch (this.sentiment) {
		case "neutral":
			this.scoreNormalizado = 3;
			break;
		case "positive":
			if (this.positiveScore >= 0.8)
				this.scoreNormalizado = 5;
			else
				this.scoreNormalizado = 4;
			break;
		case "negative":
			if (this.negativeScore >= 0.8)
				this.scoreNormalizado = 1;
			else
				this.scoreNormalizado = 2;
			break;
		case "mixed":
			calculaScoreNormalizadoMixed();
			break;
		default:
			break;
		}

	}

	private void calculaScoreNormalizadoMixed() {
		List<Pair<String, Double>> sentimentos = new ArrayList<>();
		sentimentos.add(Pair.of("neutral", this.neutralScore));
		sentimentos.add(Pair.of("positive", this.positiveScore));
		sentimentos.add(Pair.of("negative", this.negativeScore));

		Collections.sort(sentimentos, Comparator.comparing(p -> -p.getRight()));

		Pair<String, Double> primeiro = sentimentos.get(0);
		Pair<String, Double> segundo = sentimentos.get(1);

		if (segundo.getRight() + (segundo.getRight() * 0.5) >= primeiro.getRight()) {
			this.scoreNormalizado = 3;
		} else {
			if (primeiro.getLeft().equals("positive")) {
				this.scoreNormalizado = 4;
			} else if (primeiro.getLeft().equals("negative")) {
				this.scoreNormalizado = 2;
			} else {
				this.scoreNormalizado = 3;
			}
		}
	}
	
	public static String getDescSatisfacao(Integer scoreNormalizado) {
		switch (scoreNormalizado) {
		case 1:
			return "Muito negativo";
		case 2:
			return "Negativo";
		case 3:
			return "Neutro";
		case 4:
			return "Positivo";
		case 5:
			return "Muito positivo";			
		default:
			return "Neutro";
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public Double getPositiveScore() {
		return positiveScore;
	}

	public void setPositiveScore(Double positiveScore) {
		this.positiveScore = positiveScore;
	}

	public Double getNeutralScore() {
		return neutralScore;
	}

	public void setNeutralScore(Double neutralScore) {
		this.neutralScore = neutralScore;
	}

	public Double getNegativeScore() {
		return negativeScore;
	}

	public void setNegativeScore(Double negativeScore) {
		this.negativeScore = negativeScore;
	}

	public Integer getScoreNormalizado() {
		return scoreNormalizado;
	}

	public void setScoreNormalizado(Integer scoreNormalizado) {
		this.scoreNormalizado = scoreNormalizado;
	}

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		RetornoAzure other = (RetornoAzure) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
