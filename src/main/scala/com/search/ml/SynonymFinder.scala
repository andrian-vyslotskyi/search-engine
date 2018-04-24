package com.search.ml

import java.io.File

import org.apache.spark.SparkContext
import org.apache.spark.mllib.feature.{Word2Vec, Word2VecModel}
import org.apache.spark.rdd.RDD

class SynonymFinder(
                     sparkContext: SparkContext,
                     synonymsNumber: Int,
                     minimalSimilarity: Double = 0.75,
                     modelPath: String = "./word2vec",
                     vectorsMapFile: Option[String] = None,
                     trainSentences: Option[RDD[String]] = None
                   ) {
  val word2vec = new Word2Vec()

  val model: Word2VecModel = loadModel()

  def findSynonyms(word: String): Array[String] = {
    model.findSynonyms(word, synonymsNumber)
      .filter { case (_, similarity) => similarity > minimalSimilarity }
      .map { case (synonym, _) => synonym }
  }


  def loadModel(): Word2VecModel = {
    if (new File(modelPath).exists()) Word2VecModel.load(sparkContext, modelPath)
    else createModelFromVectors()
  }

  def createModelFromVectors(): Word2VecModel = {
    vectorsMapFile match {
      case Some(path) if new File(path).exists() =>
        val model = new Word2VecModel(convertTextFile(path))
        model.save(sparkContext, modelPath)
        model
      case _ => trainModel()
    }
  }

  def trainModel(): Word2VecModel = {
    trainSentences match {
      case Some(rdd) =>
        val model = word2vec.fit(rdd.map(_.split(" ").toList))
        model.save(sparkContext, modelPath)
        model
      case _ => new Word2VecModelDummy
    }
  }

  private def convertTextFile(path: String): Map[String, Array[Float]] = {
    sparkContext.textFile(path)
      .map(_.split(" ").toList)
      .map { case head :: rest => Map[String, Array[Float]](head -> rest.map(_.toFloat).toArray) }
      .reduce(_ ++ _)
  }
}

class Word2VecModelDummy extends Word2VecModel(Map("" -> Array(0F))) {
  override def findSynonyms(word: String, num: Int): Array[(String, Double)] = Array()
}
