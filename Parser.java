import computation.contextfreegrammar.*;
import computation.parser.*;
import computation.parsetree.*;
import computation.derivation.*;

import java.util.*;

public class Parser implements IParser {
  private ArrayList<ParseTreeNode> finalTree = new ArrayList<>();
  private final Word emptyWord = new Word();
  private boolean finalResult = false;


/* CYK */
  private void mainLogic(ContextFreeGrammar cfg, Word w) {
    final Word startVariable = new Word(cfg.getStartVariable());
    final boolean debug = false; // for debug purposes print lines

    /* Word Related */
    int n = w.length();
    List<Rule> rules = cfg.getRules(); // hold all rules
    Word startVar = new Word(cfg.getStartVariable()); // initial variable
    finalResult = false;
    finalTree.clear() ;

    if (debug) {
      System.out.println("************");
      System.out.println("The language:");
      System.out.println(cfg.toString());
      System.out.println("************");
      System.out.println("Word: " + w.toString());
      System.out.println("--------------");
    }

    // if we have empty word - need just check if there is a rule for it
    if (n == 0){
      for (Rule rule : rules){ //Paranoid.. check that epsilon is reached with start variable
        if ( new Word(rule.getVariable()).equals(startVar) && rule.getExpansion().equals(emptyWord) )
        {
          finalResult = true;
          finalTree.add(ParseTreeNode.emptyParseTree(cfg.getStartVariable()));
          return;
        }
      }
      return;
    }

    /* Populate diagonals */
    HashMap<String, ArrayList<ParseTreeNode>> table = new HashMap<>();
    for (int i = 1; i <= n; i++){
      Symbol terminalSymbol = w.get(i-1);
      Word terminalAsWord = new Word(terminalSymbol);
      ArrayList<ParseTreeNode> toPutInListList = new ArrayList<>();
      for (Rule rule : rules){
        Word ruleExp = rule.getExpansion();
        //If the word is n>1, then we can skip derivations to start variable as there will be no rule later
        if (n>1 && rule.getVariable().equals(cfg.getStartVariable())) {continue;}
        if (ruleExp.equals(terminalAsWord)) {
          Variable ruleVar = rule.getVariable();
          ParseTreeNode terminalNode = new ParseTreeNode(terminalSymbol);
          ParseTreeNode toPutInList = new ParseTreeNode(ruleVar, terminalNode);
          toPutInListList.add(toPutInList); // Build a list of nodes that are based on A -> x derivations
        }
      }
      String tableIndex = i + "," + i; // the map is referenced by keys of 1,1 ... n,n
      table.put(tableIndex, toPutInListList); // put the list into our map
    }

  /*
    The table should be n-terms in diagonals, so if the table is < n, then the previous loop failed to find
    a rule for a certain variable, this means there was an illegal character (that is not a terminal).
    I could have also used the is isWordOnlyTerminals(), but I wanted to use CYK style as much as possible.
   */
    if ( table.size() < n ) {
      return;
    }

    /* Thank you Michael Sipser ISBN-13-978-1-133-18779-0 */
    for (int l = 2; l <=n; l++){
      for (int i = 1; i <= n-l+1; i++){
        int j = i+l-1;
        for (int k=i; k <= j-1; k++) {
          int i2 = k+1;
          ArrayList<ParseTreeNode> toPutInListList = new ArrayList<>();
          String tableA = i + "," + k;
          String tableB = i2 + "," + j;
          if (debug) { System.out.println(tableA + " to " + tableB + "    into " + i+","+j); } // debug

          for (Rule rule : rules){
            if (rule.getExpansion().length()>1){ // Focus on rules of the form Z -> AB
              ArrayList<ParseTreeNode> tableForA = table.get(tableA); // Multiple A's may exist
              for (ParseTreeNode nodeA : tableForA){
                ArrayList<ParseTreeNode> tableForB = table.get(tableB); // Multiple B's may exist
                for (ParseTreeNode nodeB : tableForB){
                  Word wordToCheckAsWord = new Word(nodeA.getSymbol(), nodeB.getSymbol()); // Generate Word "AB"
                  if (rule.getExpansion().equals(wordToCheckAsWord)){
                    if (debug) {
                      String wordToCheck= nodeA.getSymbol().toString()+nodeB.getSymbol().toString();
                      System.out.println(wordToCheck + " found by the rule... " + rule.toString());
                    }
                    Variable ruleVar = rule.getVariable();
                    ParseTreeNode toPutInList = new ParseTreeNode(ruleVar, nodeA, nodeB);
                    toPutInListList.add(toPutInList); // grow a list
                  }
                }
              }
            }
          }
          String tableIndex = i + "," + j;
        /*
          This feels like a hack - there must be a neater way of appending the list
          But we will simply check if the index exists in the map
          If yes, grab a copy of the list, and add all the latest list of nodes to that list
          put the new larger list back (this process must be heavy on memory... since we need
          memory for list (A) + list (A+B)
        */
          if (table.containsKey(tableIndex)){
            ArrayList<ParseTreeNode> bufferList;
            bufferList = table.get(tableIndex);
            bufferList.addAll(toPutInListList);
            table.put(tableIndex, bufferList);
          }
          else {
            table.put(tableIndex, toPutInListList); // if there is no index existing, just put in the list
          }
        }
      }
    }
    String toCheckPosition = "1" + ","+n;
    ArrayList<ParseTreeNode> toCheckNode = table.get(toCheckPosition);
    if (toCheckNode.size() == 0)
    {
      return; // if this map index is size 0, it means no rule reached till here.
    }
    else {
      for (ParseTreeNode node : toCheckNode) {
        Word test = new Word(node.getSymbol());
        if (test.equals(startVariable)) {
          finalResult = true;
          finalTree.add(node);
          return;
        }
      }
    }
  }

  public boolean isInLanguage(ContextFreeGrammar cfg, Word w) {
    //In CYK case there is no advantage for doing this otherwise.
    //mainLogic(cfg, w); // run the main logic to set class attributes;
    //return finalResult;
    return generateParseTree(cfg, w) != null;
  }

  public ParseTreeNode generateParseTree(ContextFreeGrammar cfg, Word w) {
    mainLogic(cfg, w); // run the main logic to set class attributes;
    if (!finalResult) { return null; }
    return finalTree.get(0);
  }

  public boolean isWordOnlyTerminals(ContextFreeGrammar cfg, Word w) {
    // this can check if a single character is a terminal or if a word is made purely of terminals
    Set<Terminal> terminals = cfg.getTerminals();
    for (Symbol x : w) {
      if (!terminals.contains(x)) { return false; }
    }
    return true;
  }

}
