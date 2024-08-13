// https://github.com/deepjavalibrary/djl/blob/v0.29.0/examples/src/main/java/ai/djl/examples/inference/cv/ImageClassification.java
package adams.djl.mnist;

/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An example of inference using an image classification model.
 *
 * <p>See this <a
 * href="https://github.com/deepjavalibrary/djl/blob/master/examples/docs/image_classification.md">doc</a>
 * for information about this example.
 */
public final class Predict2 {

  private static final Logger logger = LoggerFactory.getLogger(Predict2.class);

  private Predict2() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    String[] images = args;
    if (images.length == 0)
      images = new String[]{"src/test/resources/mnist/0.png"};

    // Assume you have run Train.java example.
    try (Model model = loadModel("src/main/flows/output", "mlp")) {
      try (Predictor<Image, Classifications> predictor = newPredictor(model)) {
	for (String image : images) {
	  System.out.println("--> " + image);
	  Path imageFile = Paths.get(image);
	  Image img = ImageFactory.getInstance().fromFile(imageFile);
	  Classifications classifications = predictor.predict(img);
	  System.out.println(classifications);
	}
      }
    }
  }

  public static Model loadModel(String path, String modelName) throws IOException, ModelException, TranslateException {
    Model model = Model.newInstance(modelName, "PyTorch");
    model.setBlock(new Mlp(28 * 28, 10, new int[]{128, 64}));
    model.load(Paths.get(path));
    return model;
  }

  public static Predictor<Image, Classifications> newPredictor(Model model) throws IOException, ModelException, TranslateException {
    List<String> classes =
      IntStream.range(0, 10).mapToObj(String::valueOf).collect(Collectors.toList());
    Translator<Image, Classifications> translator =
      ImageClassificationTranslator.builder()
	.addTransform(new ToTensor())
	.optSynset(classes)
	.optApplySoftmax(true)
	.build();

    return model.newPredictor(translator);
  }
}