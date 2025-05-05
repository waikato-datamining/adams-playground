package adams.djl.tabnet;

import ai.djl.Model;
import ai.djl.basicdataset.tabular.AmesRandomAccess;
import ai.djl.basicdataset.tabular.ListFeatures;
import ai.djl.basicdataset.tabular.TabularTranslator;
import ai.djl.inference.Predictor;
import ai.djl.translate.Translator;
import ai.djl.zero.Performance;

import java.nio.file.Path;

public class TabularInfer {

  public static void main(String[] args) throws Exception {
    // will load "housestyle-XXXX.params" with model.load(...)
    Model model = Model.newInstance("housestyle");

    // structure must be known (model was built with performance, FAST, inputDim=3, outputDim=1)
    model.setBlock(TabularRegression.createBlock(Performance.FAST, 3, 1));
    model.load(Path.of("src/main/flows/output"));

    // TODO without dataset?
    AmesRandomAccess dataset = AmesRandomAccess.builder()
				 .setSampling(32, true)
				 .addFeature("housestyle")
				 .addFeature("yearbuilt")
				 .addFeature("lotarea")
				 .addNumericLabel("saleprice")
				 //.addAllFeatures()
				 .build();

    Translator<ListFeatures, Float> translator = new TabularTranslator(
      dataset.getFeatures(), dataset.getLabels()).getExpansions().option(ListFeatures.class, Float.class);

    Predictor<ListFeatures, Float> predictor = model.newPredictor(translator);
    ListFeatures input = new ListFeatures();
    input.add("2");
    input.add("2003");
    input.add("8450");
    Float pred = predictor.predict(input);
    System.out.println(pred);
    model.close();
  }
}
