package example

import org.scalatest._

class NaiveBayesSpec extends FreeSpec with Matchers {

  val input = Vector("test", "important", "document")

  val inputSameInDifferentRows = input ++ Vector("important")

  val inputSameInOneRowAndDifferentRows = input ++ Vector("important",
                                                          "important")

  val diffCategoriesInput = Vector(
    TrainInst(Vector("y", "x"), "2"),
    TrainInst(Vector("x"), "1"),
    TrainInst(Vector("x"), "2"),
    TrainInst(Vector("x"), "3")
  )

  "word frequencies" - {

    "should output word frequencies with categories split" in {
      assert(
        WordFrequency.splitCategoriesWithFrequencies(diffCategoriesInput) ==
          Map(
            "1" -> Map("x" -> 1),
            "2" -> Map("y" -> 1, "x" -> 2),
            "3" -> Map("x" -> 1)
          )
      )
    }

    "should split different categories into separate Maps" in {
      assert(
        WordFrequency.splitIntoCategoryMaps(diffCategoriesInput) ==
          Map(
            "1" -> Vector("x"),
            "2" -> Vector("y", "x", "x"),
            "3" -> Vector("x")
          ))
    }

    "should generate a list of unique words" in {
      assert(
        WordFrequency.wordFrequencyList(input) ==
          Map("test" -> 1, "important" -> 1, "document" -> 1)
      )
    }

    "should count increment number if word appears twice in different rows" in {
      assert(
        WordFrequency.wordFrequencyList(inputSameInDifferentRows) ==
          Map("test" -> 1, "important" -> 2, "document" -> 1)
      )
    }

    "should count increment number if word appears twice in same training" in {
      assert(
        WordFrequency.wordFrequencyList(inputSameInOneRowAndDifferentRows) ==
          Map("test" -> 1, "important" -> 3, "document" -> 1)
      )
    }
  }

  "classification" - {
    "should return probability that sentence is spam and not spam" in {

      val mySentence = Vector("x", "y")

      assert(
        NaiveBayes.classify(
          mySentence,
          WordFrequency.splitCategoriesWithFrequencies(diffCategoriesInput),
          diffCategoriesInput
        ) == ("2", 0.70)
      )
    }
  }

}