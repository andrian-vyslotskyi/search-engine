import com.search.model.{Request, RequestType}
import org.scalatest.FlatSpec

class RequestDeserializeTest extends FlatSpec {

  "json" should "correctly deserialize to model" in {
    val json =
      """{
        |   "requestType": "Put",
        |   "text": "Some text"
        |}""".stripMargin

    val expected = Request(RequestType.Put, "Some text")

    assert(Request.fromJson(json) == expected)
  }

}
