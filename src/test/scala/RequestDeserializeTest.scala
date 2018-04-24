import com.search.model.{Request, RequestType}
import org.scalatest.FlatSpec

class RequestDeserializeTest extends FlatSpec {

  "json" should "correctly deserialize to model" in {
    val json =
      """{
        |   "uuid": "1-fdf-2"
        |   "requestType": "Put",
        |   "text": "Some text"
        |}""".stripMargin

    val expected = Request("1-dfd-2", RequestType.Put, "Some text")

    assert(Request.fromJson(json) == expected)
  }

}
