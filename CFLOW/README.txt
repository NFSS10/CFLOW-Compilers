# README #

**PROJECT TITLE:** CFlow
**GROUP:** G34

NAME1: Jorge Miguel Rodrigues Ferreira, 
NR1: 201207133,
GRADE1: 17,
CONTRIBUTION1: 33%

NAME2: Pedro Daniel Viana Lima,
NR2: 201403381,
GRADE2: 17,
CONTRIBUTION2: 33%

NAME3: Nuno Filipe Sousa e Silva,
NR3: 201404380,
GRADE3: 17,
CONTRIBUITON3: 33%



** SUMMARY:** 
O nosso projeto visa verificar o controlo da execução de um programa. Assim, a nossa aplicação gera um autómato finito determinista de acordo com a expressão regular introduzida inicialmente, que verifica se um programa após a sua execução, passa nessa mesma expressão regular. O programa, antes de ser processado pelo Kadabra, deve estar devidamente comentado com os "basic blocks" que assinalam as transições no DFA. Assim, é gerado um autómato que é serializado, e com a ajuda do Kadabra, é injetado código no programa no qual pretendemos verificar o fluxo que faz a desserialização obtendo assim um objeto DFA no inicio do main do programa. É criada tambem uma string que acumula todos as transições pretendidas para no final ser verificado se a string final é aceite pelo automato.

** EXECUTE:**
Para correr a aplicação é necessário compilar todos os ficheiros java, e correr a class Cflow que irá perguntar qual a expressão regular pretendida, qual o output pretendido para serializar o DFA e, opcionalmente se pretende processar algum ficheiro de input com o kadabra.

**OVERVIEW:**
A nossa aplicação consiste em três fases principais até a geração do automato. A primeira é o parser da expressão regular em formato PCRE, a segunda é criação do e-NFA a partir da árvore abstrata gerada pelo parser e a terceira é a conversão do e-NFA para DFA.
Para a criação do e-NFA foi utilizado o "Thompson's Algorithm" e para a conversão do e-NFA para o DFA foi utilizado o "e-clousure".


**TESTSUITE AND TEST INFRASTRUCTURE:**
Os diversos teste realizados tiveram como objetivo cobrir as várias entradas para as RE, desde as várias multiplicidades (*, ?, {5}, {x,y}, etc) assim como o uso de nested parentesis. Exemplo disso é o exemplo1.java que pode ser testado por exemplo para várias RE como : ab*c, ab+c, ab{10}c, ab{10,}c visto que a string gerada pelo código é abbbbbbbbbbc.


**TASK DISTRIBUTION:**
A realização do parser assim como a obtenção da árvore sintática na forma correta (correta no sentido de ser compatível com a conversão para NFA realizada) ficou encarregue pelo Pedro Lima.
A conversão da arvore sintática para NFA e a incorporação do kadabra a cargo do Jorge Ferreira.
Por fim, o responsável pela conversão de NFA para DFA foi o Nuno Silva.

**PROS:**
Aceita praticamente todas as expressões regulares do formato PCRE. Inclusive algumas complexas com, por exemplo, [A-z], [^A-z], [0-9] e {9,}.

**CONS:**
É necessário serializar o objeto automato criado e voltar a desserializar para verificar se o programa corre a expressão regular pretendida.

### Compilar ###

Para compilar é necessário fazer download do [kadabra.jar](http://specs.fe.up.pt/tools/kadabra.jar), colocar no root do projeto e no eclipse fazer:
* Selecionar no kadabra.jar e rato lado direito -> Build Path -> Add to Build Path
Desta forma é possível trabalhar e compilar o trabalho direito.

Para testar o fluxo do código de teste é necessário ter sempre o ficheiro "dfa.ser" no mesmo diretório do ficheiro de teste a correr. 

Para correr o programa de teste, é necessário adicionar da mesma forma que o kadabra.jar foi adicionado, o DFA.jar ao ficheiro que foi feito output porque este contem as classes responsáveis pelas transições no DFA.

Depois disto basta correr o ficheiro que foi feito output e verificar se este passa ou nao na expressão regular.