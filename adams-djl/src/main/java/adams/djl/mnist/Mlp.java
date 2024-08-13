// https://github.com/deepjavalibrary/djl/blob/v0.29.0/model-zoo/src/main/java/ai/djl/basicmodelzoo/basic/Mlp.java
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

import ai.djl.ndarray.NDList;
import ai.djl.nn.Activation;
import ai.djl.nn.Blocks;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;

import java.util.function.Function;

public class Mlp extends SequentialBlock {

  /**
   * Create an MLP NeuralNetwork using RELU.
   *
   * @param input  the size of the input vector
   * @param output the size of the output vector
   * @param hidden the sizes of all of the hidden layers
   */
  public Mlp(int input, int output, int[] hidden) {
    this(input, output, hidden, Activation::relu);
  }

  /**
   * Create an MLP NeuralNetwork.
   *
   * @param input      the size of the input vector
   * @param output     the size of the output vector
   * @param hidden     the sizes of all of the hidden layers
   * @param activation the activation function to use
   */
  @SuppressWarnings("this-escape")
  public Mlp(int input, int output, int[] hidden, Function<NDList, NDList> activation) {
    add(Blocks.batchFlattenBlock(input));
    for (int hiddenSize : hidden) {
      add(Linear.builder().setUnits(hiddenSize).build());
      add(activation);
    }

    add(Linear.builder().setUnits(output).build());
  }
}
