package service.external;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.azure.ai.textanalytics.models.DocumentSentiment;

import model.azure.RetornoAzure;

@Stateless
@LocalBean
public class APIAnaliseDeTexto {

	@EJB
	APIAnaliseDeTextoUnit unit;

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public RetornoAzure analiseDeSentimento(String texto) throws Exception {

		Future<DocumentSentiment> futureDocumentSentiment = this.unit.getDocumentSentiment(texto);

		DocumentSentiment documentSentiment = null;

		if (futureDocumentSentiment != null) {
			documentSentiment = futureDocumentSentiment.get(15, TimeUnit.SECONDS);

			RetornoAzure retornoAzure = new RetornoAzure();
			retornoAzure.setSentiment(documentSentiment.getSentiment().toString());
			retornoAzure.setPositiveScore(documentSentiment.getConfidenceScores().getPositive());
			retornoAzure.setNeutralScore(documentSentiment.getConfidenceScores().getNeutral());
			retornoAzure.setNegativeScore(documentSentiment.getConfidenceScores().getNegative());

			return retornoAzure;
		} else {
			return null;
		}
	}

}
