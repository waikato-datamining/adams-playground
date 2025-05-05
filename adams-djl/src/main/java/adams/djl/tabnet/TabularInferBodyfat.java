package adams.djl.tabnet;

import ai.djl.Model;
import ai.djl.basicdataset.tabular.ListFeatures;
import ai.djl.basicdataset.tabular.TabularTranslator;
import ai.djl.inference.Predictor;
import ai.djl.translate.Translator;
import ai.djl.zero.Performance;
import nz.ac.waikato.cms.adams.djl.dataset.ArffDataset;
import nz.ac.waikato.cms.adams.djl.dataset.ArffUtils;

import java.nio.file.Path;
import java.util.Collections;

public class TabularInferBodyfat {

  public static void main(String[] args) throws Exception {
    // will load "bodyfat-XXXX.params" with model.load(...)
    Model model = Model.newInstance("bodyfat");

    // TODO without dataset?
    ArffDataset dataset = ArffDataset.builder()
			    .setSampling(32, true)
			    .fromJson(Path.of("src/main/flows/output/bodyfat.json"))
			    .build();

    // structure must be known (model was built with performance, ACCURATE, inputDim=14, outputDim=1)
    model.setBlock(TabularRegression.createBlock(Performance.ACCURATE, dataset.getFeatureSize(), dataset.getLabelSize()));
    model.load(Path.of("src/main/flows/output"));

    Translator<ListFeatures, Float> translator = new TabularTranslator(
      dataset.getFeatures(), dataset.getLabels()).getExpansions().option(ListFeatures.class, Float.class);

    Predictor<ListFeatures, Float> predictor = model.newPredictor(translator);
    ListFeatures input = new ListFeatures();
    Collections.addAll(input, ArffUtils.split("1.0708,23,154.25,67.75,36.2,93.1,85.2,94.5,59,37.3,21.9,32,27.4,17.1", ',', true, '\'', true));
    Float pred = predictor.predict(input);
    System.out.println(pred);
    model.close();
  }
}
