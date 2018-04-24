package com.search.ml

import org.apache.spark.{SparkConf, SparkContext}

object ModelLoader {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("word vectoring model").setMaster("local[*]")
      .set("spark.driver.maxResultSize", "3g").set("spark.executor.memory", "5g")
    val sc = new SparkContext(sparkConf)

    val gloveTxt = "/home/andrian/Downloads/glove.42B.300d.txt"
    val smallWord2Vec = "/home/andrian/Downloads/deps.words"

    try {
//      new SynonymFinder(sc, 1, modelPath = "./bigGlove", vectorsMapFile = Option(gloveTxt))
      new SynonymFinder(sc, 1, modelPath = "./smallW2V", vectorsMapFile = Option(smallWord2Vec))
    } finally {
      sc.stop()
    }
  }
}
