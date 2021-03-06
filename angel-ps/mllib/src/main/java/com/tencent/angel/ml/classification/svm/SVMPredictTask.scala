/*
 * Tencent is pleased to support the open source community by making Angel available.
 *
 * Copyright (C) 2017 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.tencent.angel.ml.classification.svm


import com.tencent.angel.ml.conf.MLConf
import com.tencent.angel.ml.feature.LabeledData
import com.tencent.angel.ml.utils.DataParser
import com.tencent.angel.utils.HdfsUtil
import com.tencent.angel.worker.task.{TaskContext, TrainTask}
import org.apache.hadoop.io.{LongWritable, Text}

class SVMPredictTask(val ctx: TaskContext) extends TrainTask[LongWritable, Text](ctx) {

  val feaNum: Int = conf.getInt(MLConf.ML_FEATURE_NUM, 1)
  val dataFormat: String = conf.get(MLConf.ML_DATAFORMAT)

  @throws[Exception]
  def train(ctx: TaskContext) {
    val svmModel = new SVMModel(conf)

    val predictResult = svmModel.predict(dataBlock)

    // TODO delet
    System.out.println("predictstorage.len=" + predictResult.getTotalElemNum)
    HdfsUtil.writeStorage(predictResult, ctx)
  }

  def parse(key: LongWritable, value: Text): LabeledData = {
    DataParser.parseVector(key, value, feaNum, dataFormat, false)
  }
}