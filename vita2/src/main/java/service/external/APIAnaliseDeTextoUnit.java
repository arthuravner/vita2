package service.external;

import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.core.credential.AzureKeyCredential;

import model.Config;
import service.ConfigService;

@Stateless
@LocalBean
public class APIAnaliseDeTextoUnit {
	Logger logger = Logger.getLogger(this.getClass().getName());

	private TextAnalyticsClient client = null;

	@EJB
	private ConfigService configService;

	@PostConstruct
	public void postConstructed() {

		try {
			final Config key = configService.getConfig("key_api_analise_texto");
			final Config endoint = configService.getConfig("endpoint_api_analise_texto");

			client = authenticateClient(key.getValor(), endoint.getValor());
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	private TextAnalyticsClient authenticateClient(String key, String endpoint) {
		return new TextAnalyticsClientBuilder().credential(new AzureKeyCredential(key)).endpoint(endpoint)
				.buildClient();
	}

	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Future<DocumentSentiment> getDocumentSentiment(String text) {
		
		if(client != null) {
			DocumentSentiment documentSentiment = client.analyzeSentiment(text, "pt-BR");
			System.out.printf(
					"Recognized document sentiment: %s, positive score: %s, neutral score: %s, negative score: %s.%n",
					documentSentiment.getSentiment(), documentSentiment.getConfidenceScores().getPositive(),
					documentSentiment.getConfidenceScores().getNeutral(),
					documentSentiment.getConfidenceScores().getNegative());

			return new AsyncResult<>(documentSentiment);	
		}else {
			return null;	
		}		
	}

}
