import computation.contextfreegrammar.*;

import java.util.ArrayList;
import java.util.HashSet;

public class MyGrammar {
	public static ContextFreeGrammar simpleGrammar() {
		//Variable A0 = new Variable("S");
		Variable A0 = new Variable("A0");
		Variable A = new Variable('A');
		Variable B = new Variable('B');
		Variable Z = new Variable('Z');
		Variable Y = new Variable('Y');

		HashSet<Variable> variables = new HashSet<>();
		variables.add(A0);
		variables.add(A);
		variables.add(B);
		variables.add(Z);
		variables.add(Y);

		Terminal zero = new Terminal('0');
		Terminal one = new Terminal('1');

		HashSet<Terminal> terminals = new HashSet<>();
		terminals.add(zero);
		terminals.add(one);

		ArrayList<Rule> rules = new ArrayList<>();
		//rules.add(new Rule(A0, Word.emptyWord));
		rules.add(new Rule(A0, new Word(Z, Y)));
		rules.add(new Rule(A0, new Word(Z, B)));
		rules.add(new Rule(A, new Word(Z, Y)));
		rules.add(new Rule(A, new Word(Z, B)));
		rules.add(new Rule(B, new Word(A, Y)));
		rules.add(new Rule(Z, new Word(zero)));
		rules.add(new Rule(Y, new Word(one)));


		ContextFreeGrammar cfg = new ContextFreeGrammar(variables, terminals, rules, A0);
		return cfg;
	}

	public static ContextFreeGrammar makeGrammar() {
		Variable E0 = new Variable("E0");
		Variable E = new Variable('E');
		Variable T = new Variable('T');
		Variable F = new Variable('F');
		Variable C = new Variable('C');
		Variable E1 = new Variable("E1");
		Variable T1 = new Variable("T1");
		Variable E2 = new Variable("E2");
		Variable T2 = new Variable("T2");
		Variable C1 = new Variable("C1");

		HashSet<Variable> variables = new HashSet<>();
		variables.add(E0);
		variables.add(E);
		variables.add(T);
		variables.add(F);
		variables.add(C);
		variables.add(E1);
		variables.add(T1);
		variables.add(E2);
		variables.add(T2);
		variables.add(C1);

		Terminal one = new Terminal('1');
		Terminal zero = new Terminal('0');
		Terminal x = new Terminal('x');
		Terminal plus = new Terminal('+');
		Terminal star = new Terminal('*');
		Terminal minus = new Terminal('-');

		HashSet<Terminal> terminals = new HashSet<>();
		terminals.add(one);
		terminals.add(zero);
		terminals.add(x);
		terminals.add(plus);
		terminals.add(star);
		terminals.add(minus);

		ArrayList<Rule> rules = new ArrayList<>();
		rules.add(new Rule(E0, new Word(E1, T)));
		rules.add(new Rule(E0, new Word(T1, F)));
		rules.add(new Rule(E0, new Word(one)));
		rules.add(new Rule(E0, new Word(zero)));
		rules.add(new Rule(E0, new Word(x)));
		rules.add(new Rule(E0, new Word(C1, C)));

		rules.add(new Rule(E, new Word(E1, T)));
		rules.add(new Rule(E, new Word(T1, F)));
		rules.add(new Rule(E, new Word(one)));
		rules.add(new Rule(E, new Word(zero)));
		rules.add(new Rule(E, new Word(x)));
		rules.add(new Rule(E, new Word(C1, C)));

		rules.add(new Rule(T, new Word(T1, F)));
		rules.add(new Rule(T, new Word(one)));
		rules.add(new Rule(T, new Word(zero)));
		rules.add(new Rule(T, new Word(x)));
		rules.add(new Rule(T, new Word(C1, C)));

		rules.add(new Rule(F, new Word(one)));
		rules.add(new Rule(F, new Word(zero)));
		rules.add(new Rule(F, new Word(x)));
		rules.add(new Rule(F, new Word(C1, C)));

		rules.add(new Rule(C, new Word(one)));
		rules.add(new Rule(C, new Word(zero)));
		rules.add(new Rule(C, new Word(x)));

		rules.add(new Rule(E1, new Word(E, E2)));
		rules.add(new Rule(T1, new Word(T, T2)));

		rules.add(new Rule(E2, new Word(plus)));
		rules.add(new Rule(T2, new Word(star)));

		rules.add(new Rule(C1, new Word(minus)));

		ContextFreeGrammar cfg = new ContextFreeGrammar(variables, terminals, rules, E0);
		return cfg;
	}


	// this version of the grammar reuses rules where possible (fewer rules gives inefficient code a better
	// chance of running) plus doesn't use subscript variables
	public static ContextFreeGrammar courseworkCNF() {
		Variable S = new Variable('S');
		Variable E = new Variable('E');
		Variable T = new Variable('T');
		Variable F = new Variable('F');

		Variable A = new Variable('A');
		Variable B = new Variable('B');

		Variable P = new Variable('P');
		Variable M = new Variable('M');
		Variable N = new Variable('N');
		Variable C = new Variable('C');

		HashSet<Variable> variables = new HashSet<>();
		variables.add(S);
		variables.add(E);
		variables.add(T);
		variables.add(F);

		variables.add(A);
		variables.add(B);

		variables.add(P);
		variables.add(M);
		variables.add(N);
		variables.add(C);


		Terminal plus = new Terminal('+');
		Terminal mult = new Terminal('*');
		Terminal neg = new Terminal('-');
		Terminal one = new Terminal('1');
		Terminal zero = new Terminal('0');
		Terminal x = new Terminal('x');

		HashSet<Terminal> terminals = new HashSet<>();
		terminals.add(plus);
		terminals.add(mult);
		terminals.add(neg);
		terminals.add(one);
		terminals.add(zero);
		terminals.add(x);


		ArrayList<Rule> rules = new ArrayList<>();
		rules.add(new Rule(S, new Word(E, A)));
		rules.add(new Rule(S, new Word(T, B)));
		rules.add(new Rule(S, new Word(N, C)));
		rules.add(new Rule(S, new Word(one)));
		rules.add(new Rule(S, new Word(zero)));
		rules.add(new Rule(S, new Word(x)));

		rules.add(new Rule(E, new Word(E, A)));
		rules.add(new Rule(E, new Word(T, B)));
		rules.add(new Rule(E, new Word(N, C)));
		rules.add(new Rule(E, new Word(one)));
		rules.add(new Rule(E, new Word(zero)));
		rules.add(new Rule(E, new Word(x)));

		rules.add(new Rule(T, new Word(T, B)));
		rules.add(new Rule(T, new Word(N, C)));
		rules.add(new Rule(T, new Word(one)));
		rules.add(new Rule(T, new Word(zero)));
		rules.add(new Rule(T, new Word(x)));

		rules.add(new Rule(F, new Word(N, C)));
		rules.add(new Rule(F, new Word(one)));
		rules.add(new Rule(F, new Word(zero)));
		rules.add(new Rule(F, new Word(x)));

		rules.add(new Rule(A, new Word(P, T)));
		rules.add(new Rule(B, new Word(M, F)));

		rules.add(new Rule(N, new Word(neg)));
		rules.add(new Rule(P, new Word(plus)));
		rules.add(new Rule(M, new Word(mult)));
		rules.add(new Rule(C, new Word(one)));
		rules.add(new Rule(C, new Word(zero)));
		rules.add(new Rule(C, new Word(x)));


		ContextFreeGrammar cfg = new ContextFreeGrammar(variables, terminals, rules, S);
		return cfg;
	}

}

