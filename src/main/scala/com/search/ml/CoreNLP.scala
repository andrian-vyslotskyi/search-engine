package com.search.ml

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations._
import edu.stanford.nlp.pipeline.Annotation

object CoreNLP {
  def main(args: Array[String]): Unit = {
    import edu.stanford.nlp.pipeline.StanfordCoreNLP
    val props = new Properties()
    props.put("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,natlog,openie")
    val pipeline = new StanfordCoreNLP(props)

    val text = "Anadarko will rise the price on his production up to end of this year. What do you think about it?"

    val document = new Annotation(text)

    pipeline.annotate(document)
    document.get(classOf[SentencesAnnotation])
  }
}
