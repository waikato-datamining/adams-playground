package adams.djl.tabnet;

import ai.djl.basicdataset.tabular.AmesRandomAccess;
import ai.djl.basicdataset.tabular.ListFeatures;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.zero.Performance;
import ai.djl.zero.tabular.TabularRegression;

public class Tabular {

  public static void main(String[] args) throws Exception {
    AmesRandomAccess dataset = AmesRandomAccess.builder()
				 .setSampling(32, true)
				 .addFeature("housestyle")
				 .addFeature("yearbuilt")
				 .addFeature("lotarea")
				 .addNumericLabel("saleprice")
      				 //.addAllFeatures()
				 .build();
    // required at least 3 input features?
    ZooModel<ListFeatures, Float> model = TabularRegression.train(dataset, Performance.FAST);
    System.out.println(model);
    Predictor<ListFeatures, Float> predictor = model.newPredictor();
    ListFeatures input = new ListFeatures();
    input.add("2");
    input.add("2003");
    input.add("8450");
    Float pred = predictor.predict(input);
    System.out.println(pred);
    model.close();
  }
}
