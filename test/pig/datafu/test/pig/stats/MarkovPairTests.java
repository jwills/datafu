package datafu.test.pig.stats;

import static org.testng.Assert.*;

import java.util.Iterator;

import org.apache.pig.data.Tuple;
import org.apache.pig.pigunit.PigTest;
import org.testng.annotations.Test;

import datafu.test.pig.PigTests;

public class MarkovPairTests extends PigTests
{
  @Test
  public void markovPairDefaultTest() throws Exception
  {
    PigTest test = createPigTest("test/pig/datafu/test/pig/stats/markovPairDefault.pig",
                                 "schema=(val:int)");
    
    String[] input = {"10","20","30","40","50","60"};
    writeLinesToFile("input", input);
    
    String[] expectedOutput = {
        "({((10),(20)),((20),(30)),((30),(40)),((40),(50)),((50),(60))})"
      };
    
    test.runScript();
    
    Iterator<Tuple> actualOutput = test.getAlias("data_out");
    
    assertTuplesMatch(expectedOutput, actualOutput);
  }
  
  @Test
  public void markovPairMultipleInput() throws Exception
  {    
    PigTest test = createPigTest("test/pig/datafu/test/pig/stats/markovPairDefault.pig",
                                 "schema=(val1:int,val2:int)");
    
    String[] input = {"10\t100","20\t200","30\t300","40\t400","50\t500","60\t600"};    
    writeLinesToFile("input", input);
    
    String[] expectedOutput = {
        "({((10,100),(20,200)),((20,200),(30,300)),((30,300),(40,400)),((40,400),(50,500)),((50,500),(60,600))})"
      };    
    
    
    test.runScript();
    
    Iterator<Tuple> actualOutput = test.getAlias("data_out");
    
    assertTuplesMatch(expectedOutput, actualOutput);
  }
  
  @Test
  public void markovPairLookaheadTest() throws Exception
  {
    PigTest test = createPigTest("test/pig/datafu/test/pig/stats/markovPairLookahead.pig", 
                                 "schema=(val:int)",
                                 "lookahead=3");
    
    String[] input = {"10","20","30","40","50"};
    writeLinesToFile("input", input);
    
    String[] expectedOutput = {
        "({((10),(20)),((10),(30)),((10),(40)),((20),(30)),((20),(40)),((20),(50)),((30),(40)),((30),(50)),((40),(50))})"
      };
    
    test.runScript();
    
    Iterator<Tuple> actualOutput = test.getAlias("data_out");
    
    assertTuplesMatch(expectedOutput, actualOutput);
  }
  
  private void assertTuplesMatch(String[] expectedOutput, Iterator<Tuple> actualOutput)
  {
    Iterator<Tuple> tuples = actualOutput;
    
    for (String outputLine : expectedOutput)
    {
      assertTrue(tuples.hasNext());
      Tuple outputTuple = tuples.next();
      System.out.println(String.format("expected: %s", outputLine));
      System.out.println(String.format("actual: %s", outputTuple.toString()));
      assertEquals(outputLine,outputTuple.toString());
    }
  }
}
