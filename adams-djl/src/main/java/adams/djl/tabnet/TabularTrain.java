package adams.djl.tabnet;

import ai.djl.basicdataset.tabular.AmesRandomAccess;
import ai.djl.basicdataset.tabular.ListFeatures;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.zero.Performance;

import java.nio.file.Path;

public class TabularTrain {

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
    model.save(Path.of("src/main/flows/output"), "housestyle");
    model.close();
  }
}
