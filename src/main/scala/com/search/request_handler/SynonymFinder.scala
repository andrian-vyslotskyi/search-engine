package com.search.request_handler

import java.io.File

import org.apache.spark.SparkContext
import org.apache.spark.mllib.feature.{Word2Vec, Word2VecModel}

class SynonymFinder(
                     sparkContext: SparkContext,
                     synonymsNumber: Int,
                     minimalSimilarity: Double = 0.75,
                     modelPath: Option[String] = None,
                     vectorsMapFile: Option[String] = None
                   ) {
  val word2vec = new Word2Vec()

  val model: Word2VecModel = loadModel()

  def findSynonyms(word: String): Array[String] = {
    model.findSynonyms(word, synonymsNumber)
      .filter { case (_, similarity) => similarity > minimalSimilarity }
      .map { case (synonym, _) => synonym }
  }


  def loadModel(): Word2VecModel = {
    modelPath match {
      case Some(path) if new File(path).exists() => Word2VecModel.load(sparkContext, path)
      case _ => createModelFromVectors()
    }
  }

  def createModelFromVectors(): Word2VecModel = {
    vectorsMapFile match {
      case Some(path) if new File(path).exists() => new Word2VecModel(convertTextFile(path))
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
