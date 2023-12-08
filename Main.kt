const val MENU_PRINCIPAL = 100
const val MENU_DEFINIR_TABULEIRO = 101
const val MENU_DEFINIR_NAVIOS = 102
const val MENU_JOGAR = 103
const val MENU_LER_FICHEIRO = 104
const val MENU_GRAVAR_FICHEIRO = 105
const val SAIR = 106

var numLinhas = -1
var numColunas = -1

var tabuleiroHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroComputador: Array<Array<Char?>> = emptyArray()

var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()

fun menuPrincipal(): Int {
    println()
    println("""> > Batalha Naval < <
        |
        |1 - Definir Tabuleiro e Navios
        |2 - Jogar
        |3 - Gravar
        |4 - Ler
        |0 - Sair
        |
    """.trimMargin())
    var menuEscolhido: Int?
    do {
        menuEscolhido = readln().toIntOrNull()
        if (menuEscolhido == null || menuEscolhido >= 5 || menuEscolhido < -1) {
            println("!!! Opcao invalida, tente novamente")
        }
    } while (menuEscolhido == null || menuEscolhido >= 5 || menuEscolhido < -1)

    when (menuEscolhido) {
        1 -> return MENU_DEFINIR_TABULEIRO
        2, 3, 4 -> {
            println("!!! POR IMPLEMENTAR, tente novamente")
            return MENU_PRINCIPAL
        }

        -1 -> return MENU_PRINCIPAL
        0 -> return SAIR
    }
    println()
    return menuEscolhido
}


fun menuDefinirTabuleiro(): Int {
    println()
    println("""> > Batalha Naval < <
        |
        |Defina o tamanho do tabuleiro:
    """.trimMargin())
    var linhas: Int?
    var colunas: Int?

    do {

        do {
            println("Quantas linhas?")
            linhas = readln().toIntOrNull()

            when (linhas) {
                null -> println("!!! Número de linhas invalidas, tente novamente")
                -1 -> return MENU_PRINCIPAL
                0 -> return SAIR
            }
        } while (linhas == null)

        do {
            println("Quantas colunas?")
            colunas = readln().toIntOrNull()

            when (colunas) {
                null -> println("!!! Número de linhas invalidas, tente novamente")
                -1 -> return MENU_PRINCIPAL
                0 -> return SAIR
            }
        } while (colunas == null)

    } while (!tamanhoTabuleiroValido(linhas, colunas))

    if (linhas != null && colunas != null) {
        println(criaTerreno(linhas, colunas))

        println("""Insira as coordenadas do navio:
            |Coordenadas? (ex: 6,G)
        """.trimMargin())
        var coordenadas: String?
        do {
            coordenadas = readlnOrNull()
            if (coordenadas != null && coordenadas != "") {
                if (coordenadas.toIntOrNull() == -1) return MENU_PRINCIPAL
                if (coordenadas.toIntOrNull() == 0) return SAIR
            }
        } while (processaCoordenadas(coordenadas, linhas, colunas) == null)
    }

    println("""Insira a orientacao do navio:
        |Orientacao? (N, S, E, O)
    """.trimMargin())
    var orientacao: String?
    do {
        orientacao = readlnOrNull()
        if (orientacao != null && orientacao != "") {
            if (orientacao.toIntOrNull() == -1) return MENU_PRINCIPAL
            if (orientacao.toIntOrNull() == 0) return SAIR
        }
        when (orientacao) {
            "N", "S", "E", "O" -> return MENU_PRINCIPAL
            else -> {
                orientacao = null
                println("""!!! Orientacao invalida, tente novamente
                |Orientacao? (N, S, E, O)
            """.trimMargin())
            }
        }
    } while (orientacao == null)
    return SAIR
}


fun menuDefinirNavios(): Int {
    return MENU_PRINCIPAL

}

fun menuJogar(): Int {
    return MENU_PRINCIPAL
}

fun menuLerFicheiro(): Int {
    return MENU_PRINCIPAL
}

fun menuGravarFicheiro(): Int {
    return MENU_PRINCIPAL
}


fun tamanhoTabuleiroValido(numLinhas: Int?, numColunas: Int?): Boolean {
    return when {
        numColunas == 4 && numLinhas == 4 -> true
        numColunas == 5 && numLinhas == 5 -> true
        numColunas == 7 && numLinhas == 7 -> true
        numColunas == 8 && numLinhas == 8 -> true
        numColunas == 10 && numLinhas == 10 -> true
        else -> {
            println("!!! Tamanho de tabuleiro invalido")
            return false
        }
    }
}


fun processaCoordenadas(coordenadas: String?, numLinhas: Int, numColunas: Int): Pair<Int, Int>? {


    if (coordenadas == null || coordenadas.length < 3 || coordenadas.length > 4) {
        println("""!!! Coordenadas invalidas, tente novamente
            |Coordenadas? (ex: 6,G)
        """.trimMargin())
        return null
    } else {

        val colunaCode = coordenadas[coordenadas.length - 1].code
        var linha = coordenadas[0].toString().toIntOrNull()

        if (coordenadas.length == 3 && coordenadas[1] == ',' && colunaCode in 65..90) {
            if (linha != null) {
                return when {
                    linha == 0 -> null
                    linha <= numLinhas && colunaCode <= numColunas + 64 -> Pair(linha, colunaCode - 64)
                    else -> {
                        println("""!!! Coordenadas invalidas, tente novamente
                |Coordenadas? (ex: 6,G)
            """.trimMargin())
                        return null
                    }
                }
            }
        }
        if (coordenadas.length == 4 && coordenadas[2] == ',' && colunaCode in 65..90) {

            linha = (coordenadas[0].toString() + coordenadas[1].toString()).toInt()

            return when {
                linha == 0 -> null
                linha <= numLinhas && colunaCode <= numColunas + 64 -> Pair(linha, colunaCode - 64)
                else -> null
            }
        }
        println("""!!! Coordenadas invalidas, tente novamente
                |Coordenadas? (ex: 6,G)
            """.trimMargin())
        return null
    }
}


fun criaLegendaHorizontal(numColunas: Int): String {
    var count = 0
    var codigoAsciiLetra = 65
    var legendaHorizontal = ""
    while (count < numColunas) {
        legendaHorizontal += if (count == numColunas - 1) {
            "${codigoAsciiLetra.toChar()}"
        } else {
            "${codigoAsciiLetra.toChar()} | "
        }
        count++
        codigoAsciiLetra++
    }
    return legendaHorizontal
}

fun criaTerreno(numLinhas: Int, numColunas: Int): String {
    var linha = 0
    var terreno = ""
    terreno += "\n| "
    terreno += (criaLegendaHorizontal(numColunas))
    terreno += " |\n"
    while (linha < numLinhas) {
        var coluna = 0
        while (coluna < numColunas) {
            terreno += "|   "

            if (coluna == numColunas - 1) {
                terreno += "| ${linha + 1}"
            }
            coluna++
        }
        terreno += "\n"
        linha++
    }
    return terreno
}


fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int> {

    val numNavios: Array<Int> = when {
        numLinhas == 4 && numColunas == 4 -> arrayOf(2, 0, 0, 0)
        numLinhas == 5 && numColunas == 5 -> arrayOf(1, 1, 1, 0)
        numLinhas == 7 && numColunas == 7 -> arrayOf(2, 1, 1, 1)
        numLinhas == 8 && numColunas == 8 -> arrayOf(2, 2, 1, 1)
        numLinhas == 10 && numColunas == 10 -> arrayOf(3, 2, 1, 1)
        else -> emptyArray()

    }
    return numNavios
}


fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int): Array<Array<Char?>> {
    return Array(numLinhas) { Array(numColunas) { null } }
}


fun coordenadaContida(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    return when {
        linha <= 0 || coluna <= 0 -> false
        linha <= tabuleiro.size && coluna <= tabuleiro[linha].size -> true
        else -> false
    }
    //a linha e a coluna começam em 1

}

fun limparCoordenadasVazias(coordenadas: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    var coordenadasLimpas = emptyArray<Pair<Int, Int>>()

    for (coordenada in coordenadas)
        if (coordenada != Pair(0, 0)) {
            coordenadasLimpas += coordenada
        }
    return coordenadasLimpas
}


fun juntarCoordenadas(coordenadas: Array<Pair<Int, Int>>, coordenadas2: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    var coordenadasJuntas = coordenadas

    for (coordenada in coordenadas2)
        coordenadasJuntas += coordenada
    //podemos assumir que as coordenadas não são (0,0)

    return coordenadasJuntas
}

fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>,
                          linha: Int,
                          coluna: Int,
                          orientacao: String,
                          dimensao: Int): Array<Pair<Int, Int>> {

    var coordenadasNavio = emptyArray<Pair<Int, Int>>()

    if (linha != 0 && coluna != 0) {
        when (orientacao) {
            "E" -> for (posicao in coluna - 1..coluna + 2) {
                if (coordenadaContida(tabuleiro, linha, posicao + 1)) {
                    coordenadasNavio += Pair(linha, posicao + 1)
                }
            }

            "N" -> for (posicao in linha - 1 downTo linha - 4) {
                if (coordenadaContida(tabuleiro, posicao + 1, coluna)) {
                    coordenadasNavio += Pair(posicao + 1, coluna)
                }
            }

            "O" -> for (posicao in coluna - 1 downTo coluna - 4) {
                if (coordenadaContida(tabuleiro, linha, posicao + 1)) {
                    coordenadasNavio += Pair(linha, posicao + 1)
                }
            }

            "S" -> {
                for (posicao in linha - 1..linha + 2) {
                    if (coordenadaContida(tabuleiro, posicao+1, coluna)) {
                        coordenadasNavio += Pair(linha, posicao + 1)
                    }
                }
            }
        }
    }else return emptyArray()
    return when {
        coordenadasNavio.size >= dimensao -> coordenadasNavio
        else -> emptyArray()
    }
}

//  | 0 | 1 | 2 | 3 | Fazer um ciclo que verifique as posicoes todas e coloque no array se e válida ou não
// e depois verificamos a ultima posicao em relacao á dimensao do navio
// 0| P | P | N | N
//E-linha+1
//N=linha-1
//O=coluna-1
//S=linha +1

fun gerarCoordenadasFronteira(tabuleiro: Array<Array<Char?>>,
                              linha: Int,
                              coluna: Int,
                              orientacao: String,
                              dimensao: Int): Array<Array<Pair<Int, Int>>> {

    return emptyArray()
}


fun estaLivre(tabuleiro: Array<Array<Char?>>, conjuntoDeCoordenadas: Array<Pair<Int, Int>>): Boolean {
    return false
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, dimensao: Int): Boolean {
    //assume sempre orientação Este
    return false
}


fun insereNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Boolean {
    //quando receber a orientaçao E usar a fun inserirNavioSimples
    return false
}


fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, numeroDeNavios: Array<Int>): Array<Array<Char?>> {
    return arrayOf(arrayOf(null))
}


fun navioCompleto(tabuleiroPalpites: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    return false
}


fun obtemMapa(tabuleiroReal: Array<Array<Char?>>, eTabuleiroReal: Boolean): Array<Array<String>> {
    //Substitui criaTerreno()
    return Array(0) { Array(0) { "" } }
}

fun lancarTiro(tabuleiroRealComputador: Array<Array<Char?>>,
               tabuleiroPalpitesHumano: Array<Array<Char?>>,
               linhaEColuna: Pair<Int, Int>): String {
    return ""
}

fun geraTiroComputador(tabuleiroPalpitesComputador: Array<Array<Char?>>): Pair<Int, Int> {
    return Pair(0, 0)
}

fun contarNaviosDeDimensao(tabuleiro: Array<Array<Char?>>, dimensao: Int): Int {
    return 0
}

fun venceu(tabuleiro: Array<Array<Char?>>): Boolean {
    return false
}

fun lerJogo(nomeDoFicheiro: String, tipoDeTabuleiro: Int): Array<Array<Char?>> {
    return Array(0) { Array(0) { ' ' } }
}

fun gravarJogo(nomeDoFicheiro: String,
               tabuleiroRealHumano: Array<Array<Char?>>,
               tabuleiroPalpitesHumano: Array<Array<Char?>>,
               tabuleiroRealComputador: Array<Array<Char?>>,
               tabuleiroPalpitesComputador: Array<Array<Char?>>) {

}

fun main() {
    var menuAtual = MENU_PRINCIPAL
    while (true) {
        menuAtual = when (menuAtual) {
            MENU_PRINCIPAL -> menuPrincipal()
            MENU_DEFINIR_TABULEIRO -> menuDefinirTabuleiro()
            MENU_DEFINIR_NAVIOS -> menuDefinirNavios()
            MENU_JOGAR -> menuJogar()
            MENU_LER_FICHEIRO -> menuLerFicheiro()
            MENU_GRAVAR_FICHEIRO -> menuGravarFicheiro()
            SAIR -> return
            else -> return
        }
    }
}