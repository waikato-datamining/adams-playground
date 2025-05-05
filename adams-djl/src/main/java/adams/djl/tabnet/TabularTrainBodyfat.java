package adams.djl.tabnet;

import ai.djl.basicdataset.tabular.ListFeatures;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.zero.Performance;
import nz.ac.waikato.cms.adams.djl.dataset.ArffDataset;

import java.nio.file.Path;

public class TabularTrainBodyfat {

  public static void main(String[] args) throws Exception {
    ArffDataset dataset = ArffDataset.builder()
			    .optArffFile(Path.of("src/main/flows/data/bodyfat.arff"))
			    .setSampling(32, true)
			    .classIsLast()
			    .addAllFeatures()
			    .build();
    dataset.prepare();
    dataset.toJson(Path.of("src/main/flows/output/bodyfat.json"));
    System.out.println(dataset.getRelationName());
    // required at least 3 input features?
    ZooModel<ListFeatures, Float> model = TabularRegression.train(dataset, Performance.ACCURATE);
    System.out.println(model);
    model.save(Path.of("src/main/flows/output"), "bodyfat");
    model.close();
  }
}
