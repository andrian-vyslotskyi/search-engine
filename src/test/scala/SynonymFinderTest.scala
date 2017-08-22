import com.search.request_handler.SynonymFinder
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{FlatSpec, Matchers}

class SynonymFinderTest extends FlatSpec with Matchers {
  val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SynonymTest")
  val sc = new SparkContext(sparkConf)

  "class without any saved data" should "produce 0 synonyms" in {
    val sf = new SynonymFinder(sc, 2)

    sf.findSynonyms("random") shouldBe empty
  }
}
