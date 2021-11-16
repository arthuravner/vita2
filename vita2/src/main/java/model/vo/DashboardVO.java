package model.vo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.axes.radial.RadialScales;
import org.primefaces.model.charts.axes.radial.linear.RadialLinearTicks;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.elements.Elements;
import org.primefaces.model.charts.optionconfig.elements.ElementsLine;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.radar.RadarChartDataSet;
import org.primefaces.model.charts.radar.RadarChartModel;
import org.primefaces.model.charts.radar.RadarChartOptions;

import model.Avaliacao;
import model.LinkAvaliacao;
import model.Turma;
import model.azure.RetornoAzure;

public class DashboardVO {

	private LineChartModel lineModel;

	private org.primefaces.model.chart.LineChartModel lineModel2;

	private RadarChartModel radarModel;

	private Turma turma;

	public DashboardVO(Turma turma) {
		this.turma = turma;
		createLineModel();
		createRadarModel();
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
		createLineModel();
		createRadarModel();
	}

	public Double getMediaGeral() {
		Double media = 0D;
		if (turma != null) {
			List<Double> mediasLins = new ArrayList<Double>();
			for (LinkAvaliacao link : turma.getLinks()) {
				if (CollectionUtils.isNotEmpty(link.getAvaliacoes())) {
					Double mediaDiaria = link.getAvaliacoes().stream().map(Avaliacao::getRetornoAzure)
							.mapToInt(RetornoAzure::getScoreNormalizado).average().orElse(0D);
					mediasLins.add(mediaDiaria);
				}
			}
			media = mediasLins.stream().mapToDouble(Double::doubleValue).average().orElse(0D);
			return media;
		} else {
			return media;
		}

	}

	public void createLineModel() {

		lineModel = new LineChartModel();
		ChartData data = new ChartData();

		LineChartDataSet dataSet = new LineChartDataSet();
		List<Object> values = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		if (turma != null) {
			for (LinkAvaliacao link : turma.getLinks()) {
				Double mediaDiaria = link.getAvaliacoes().stream().map(Avaliacao::getRetornoAzure)
						.mapToInt(RetornoAzure::getScoreNormalizado).average().orElse(0D);
				values.add(mediaDiaria);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				labels.add(sdf.format(link.getDataAula()));
			}
		}

		dataSet.setData(values);
		dataSet.setFill(false);
		dataSet.setLabel("Satisfação");
		dataSet.setBorderColor("rgb(75, 192, 192)");
		dataSet.setLineTension(0.1);

		data.addChartDataSet(dataSet);
		data.setLabels(labels);

		LineChartOptions options = new LineChartOptions();
		Title title = new Title();
		title.setDisplay(true);
		title.setText("Média da satisfação dos alunos no período, por data");
		options.setTitle(title);

		CartesianLinearTicks ticks = new CartesianLinearTicks();
		ticks.setMin(1);
		ticks.setMax(5);
		CartesianScales cScales = new CartesianScales();
		CartesianLinearAxes linearAxes = new CartesianLinearAxes();
		linearAxes.setId("left-y-axis");
		linearAxes.setPosition("left");
		linearAxes.setTicks(ticks);
		cScales.addYAxesData(linearAxes);
		options.setScales(cScales);

		Legend legend = new Legend();
		legend.setDisplay(false);
		options.setLegend(legend);

		lineModel.setOptions(options);
		lineModel.setData(data);
	}

	public void createRadarModel() {

		radarModel = new RadarChartModel();
		ChartData data = new ChartData();

		HashMap<Integer, Pair<Integer, Integer>> mapDataSet = new HashMap<>();

		mapDataSet.put(1, Pair.of(1, 0));
		mapDataSet.put(2, Pair.of(2, 0));
		mapDataSet.put(3, Pair.of(3, 0));
		mapDataSet.put(4, Pair.of(4, 0));
		mapDataSet.put(5, Pair.of(5, 0));

		if (turma != null) {
			for (LinkAvaliacao link : turma.getLinks()) {
				for (Avaliacao avaliacao : link.getAvaliacoes()) {
					Pair<Integer, Integer> pair = mapDataSet.get(avaliacao.getRetornoAzure().getScoreNormalizado());
					if (pair == null) {
						mapDataSet.put(avaliacao.getRetornoAzure().getScoreNormalizado(),
								Pair.of(avaliacao.getRetornoAzure().getScoreNormalizado(), 1));
					} else {
						pair = Pair.of(pair.getLeft(), pair.getRight() + 1);
						mapDataSet.put(pair.getLeft(), pair);
					}
				}
			}
		}

		RadarChartDataSet radarDataSet = new RadarChartDataSet();
		radarDataSet.setLabel("Satisfação");
		radarDataSet.setFill(true);
		radarDataSet.setBackgroundColor("rgba(54, 162, 235, 0.2)");
		radarDataSet.setBorderColor("rgb(54, 162, 235)");
		radarDataSet.setPointBackgroundColor("rgb(54, 162, 235)");
		radarDataSet.setPointBorderColor("#fff");
		radarDataSet.setPointHoverBackgroundColor("#fff");
		radarDataSet.setPointHoverBorderColor("rgb(54, 162, 235)");

		List<Number> dataVal = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		for (Pair<Integer, Integer> pair : mapDataSet.values()) {			
			dataVal.add(pair.getRight());
			labels.add(RetornoAzure.getDescSatisfacao(pair.getLeft()));
		}

		radarDataSet.setData(dataVal);
		data.addChartDataSet(radarDataSet);
		data.setLabels(labels);

		RadarChartOptions options = new RadarChartOptions();
		Elements elements = new Elements();
		ElementsLine elementsLine = new ElementsLine();
		elementsLine.setTension(0);
		elementsLine.setBorderWidth(3);
		elements.setLine(elementsLine);
		options.setElements(elements);

		RadialScales radialScales = new RadialScales();
		RadialLinearTicks ticks = new RadialLinearTicks();
		ticks.setMin(0);
		ticks.setStepSize(1);
		ticks.setDisplay(true);
		radialScales.setTicks(ticks);
		options.setScales(radialScales);

		Legend legend = new Legend();
		legend.setDisplay(false);
		options.setLegend(legend);

		Title title = new Title();
		title.setDisplay(true);
		title.setText("Satisfação por rótulo");
		options.setTitle(title);

		radarModel.setOptions(options);
		radarModel.setData(data);
	}

	public LineChartModel getLineModel() {
		return lineModel;
	}

	public RadarChartModel getRadarModel() {
		return radarModel;
	}

	public org.primefaces.model.chart.LineChartModel getLineModel2() {
		return lineModel2;
	}

}
